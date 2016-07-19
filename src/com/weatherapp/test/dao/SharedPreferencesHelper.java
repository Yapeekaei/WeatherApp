package com.weatherapp.test.dao;

import com.weatherapp.test.WeatherApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesHelper {
	
	private static final String WEATHER_APP_PREF = "weather_app_prefs";
	
	private static final String LAST_REQUEST_DATE = "last_request_date";
	
	private SharedPreferences preferences;
	private static SharedPreferencesHelper instance;
	
	public static SharedPreferencesHelper getInstance() {
		if (instance == null) {
			instance = new SharedPreferencesHelper(WeatherApp.getAppContext());
		}
		return instance;
	}
	
	public SharedPreferencesHelper(Context ctx) {
		preferences = ctx.getSharedPreferences(WEATHER_APP_PREF, Context.MODE_PRIVATE);
	}
	
	public void setLastRequestDate(long dateValue) {
		putLong(LAST_REQUEST_DATE, dateValue);
	}
	
	public long getLastRequestDate() {
		return preferences.getLong(LAST_REQUEST_DATE, 0);
	}
	
	private void putLong(String key, long value) {
		Editor editor = preferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}
}
