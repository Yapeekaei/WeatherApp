package com.weatherapp.test.dao;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.weatherapp.test.WeatherApp;
import com.weatherapp.test.model.City;
import com.weatherapp.test.model.WeatherDetails;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	public final static int DB_VERSION = 1;
	public final static String DB_NAME = "weatherDB";
	
	public static final String WEATHER_TABLE = "weather_table";
	
	public final static String WT_COLUMN_CITY_ID = "id";
	public final static String WT_COLUMN_CITY_NAME = "name";
	public final static String WT_COLUMN_LAST_REQUEST = "last_request_date";
	public final static String WT_COLUMN_WEATHER_DETAILS = "weather_json";
	public final static String WT_COLUMN_WEEK_WEATHER_DETAILS = "week_weather_json";
	
	private static SQLiteDatabase db;
	private static DBHelper dbHelper;
	
	public static DBHelper getInstance() {
		if (dbHelper == null) {
			dbHelper = new DBHelper();
		}
		return dbHelper;
	}
	
	public DBHelper() {
		super(WeatherApp.getAppContext(), DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		initDB(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	private void initDB(SQLiteDatabase database) {
		db = database;
		db.execSQL("create table if not exists " + WEATHER_TABLE + " (" + WT_COLUMN_CITY_ID + " integer primary key," + WT_COLUMN_CITY_NAME + " text not null, " +
			    WT_COLUMN_LAST_REQUEST + " integer, " + WT_COLUMN_WEATHER_DETAILS + " text, " + WT_COLUMN_WEEK_WEATHER_DETAILS + " text);");
		
	}
	
	public void updateCity(City city) {
		if (city == null) {
			return;
		}
		db = null;
		db = getWritableDatabase();
		
		synchronized (db) {
			insertOrUpdateCity(city);	
		}
		db.close();
	}
	
	private void insertOrUpdateCity(City city) {
		long cityID = city.getCityID();
		String cityName = city.getCityName();
		String weatherJson = city.getWeatherDetails() != null ? city.getWeatherDetails().toJSONString() : null;
		String weekWeatherJson = city.weatherForecastToJSONString();
		if (cityID == -1 || cityName == null || cityName.isEmpty()) {
			return;
		}
		ContentValues cv = new ContentValues();
		
		cv.put(WT_COLUMN_LAST_REQUEST, new Date().getTime());
		if (weatherJson != null) {
			cv.put(WT_COLUMN_WEATHER_DETAILS, weatherJson);
		}
		if (weekWeatherJson != null) {
			cv.put(WT_COLUMN_WEEK_WEATHER_DETAILS, weekWeatherJson);
		}
		
		int rowsAffected = db.update(WEATHER_TABLE, cv, WT_COLUMN_CITY_ID + "=?", new String[] {String.valueOf(cityID)});
		if (rowsAffected == 0) {
			cv.put(WT_COLUMN_CITY_ID, cityID);
			cv.put(WT_COLUMN_CITY_NAME, cityName);
			db.insert(WEATHER_TABLE, null, cv);
		}
	}
	
	public void updateCities(Set<City> cities) {
		if (cities == null || cities.size() == 0) {
			return;
		}
		db = null;
		db = getWritableDatabase();
		
		synchronized (db) {
			for (City c: cities) {
				insertOrUpdateCity(c);
			}
		}
 		db.close();
	}
	
	public City getCity(long cityID) {
		db = null;
		db = getReadableDatabase();
		Cursor c = db.query(WEATHER_TABLE, null, WT_COLUMN_CITY_ID + "=?", 
							new String[] {String.valueOf(cityID)}, null, null, null);
		if (!(c.moveToFirst()) || c.getCount() == 0) {
			 return null;
		}
		City city = null;
		if (c.moveToFirst()) {
			city = createFromCursor(c);
		}
		c.close();
		db.close();
		return city;
	}
	
	public Set<City> getCities() {
		db = null;
		db = getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + WEATHER_TABLE, null);
		
		Set<City> cities = new LinkedHashSet<City>();
		if (c.moveToFirst()) {
			do {
				City city = createFromCursor(c);
				cities.add(city);
			} while (c.moveToNext());
		}
		c.close();
		db.close();
		return cities;
	}
	
	public void remove(City city) {
		if (city == null || city.getCityID() == 0 || city.getCityName() == null || city.getCityName().isEmpty()) {
			return;
		}
		db = null;
		db = getWritableDatabase();
		
		db.delete(WEATHER_TABLE, WT_COLUMN_CITY_ID + "=? and " + WT_COLUMN_CITY_NAME + "=?", new String[] {String.valueOf(city.getCityID()), city.getCityName()});
	}
	
	
	private City createFromCursor(Cursor c) {
		City city = new City();
		city.setCityID(c.getLong(0));
		city.setCityName(c.getString(1));
		city.setUpdatedDate(c.getLong(2));
		city.setWeatherDetails(new WeatherDetails(c.getString(3)));
		city.setWeatherForecast(City.parseWeatherList(c.getString(4)));
		return city;
	}
}