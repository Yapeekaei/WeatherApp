package com.weatherapp.test.controllers;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import android.util.Log;

import com.weatherapp.test.ConnectionUtils;
import com.weatherapp.test.dao.DAOFactory;
import com.weatherapp.test.dao.DBHelper;
import com.weatherapp.test.dao.SharedPreferencesHelper;
import com.weatherapp.test.model.City;

public class DataController {
	
	public static final long MIN_REQUEST_PERIOD_MILLS = 600000;
	
	public static final class RequestType {
		public static final String GET_CITIES_LIST = "get_cities";
		public static final String GET_CITIES_LIST_FORCE = "get_cities_force";
		public static final String GET_WEATHER_FOR = "get_weather_for";
		public static final String GET_WEATHER_FORECAST_FOR = "get_weather_forecast_for";
		public static final String REMOVE_CITY = "remove_city";
	}
		
	public static final class RequestMethod {
		public static final String DB_REQUEST = "db_request";
		public static final String REMOUTE_REQUEST = "remoute_request";
	}
	
	private Set<City> cities = new LinkedHashSet<City>();
	private static DataController instance;
	
	public static DataController getInstance() {
		if (instance == null) {
			instance = new DataController();
		}
		return instance;
	}
		
	public Set<City> getCitiesSet(boolean force) {
		long lastRequestDate = SharedPreferencesHelper.getInstance().getLastRequestDate();
		long currentDate = new Date().getTime();
		Log.i("WeatherApp", "Updated: " + lastRequestDate);
		Log.i("WeatherApp", "Current: " + currentDate);
		if ((lastRequestDate == 0 || currentDate - lastRequestDate > MIN_REQUEST_PERIOD_MILLS || force) && ConnectionUtils.isConnected()) {
			Set<City> cities = DAOFactory.getInstance().getWeatherDAO(DAOFactory.DAOType.REMOUTE_REQUEST).getCities();
			DAOFactory.getInstance().getWeatherDAO(DAOFactory.DAOType.DATA_BASE).updateCities(cities);
			updateCities(cities);	
			if (cities != null && cities.size() != 0) {
				SharedPreferencesHelper.getInstance().setLastRequestDate(currentDate);
			}
			return cities;	
		}
		if (cities.size() != 0) {
			return cities;
		}
		Set<City> cities = DAOFactory.getInstance().getWeatherDAO(DAOFactory.DAOType.DATA_BASE).getCities();
		updateCities(cities);
		return cities;
	}
	
	public City getWeatherFor(String cityName) {
		City city = DAOFactory.getInstance().getWeatherDAO(DAOFactory.DAOType.REMOUTE_REQUEST).getCityForName(cityName);
		DAOFactory.getInstance().getWeatherDAO(DAOFactory.DAOType.DATA_BASE).updateCity(city);
		updateCity(city);
		return city;
	}
	
	public City getWeatherForecastFor(long cityID) {
		long currentDate = new Date().getTime();
		City city = getCity(cityID);
		if (city == null) {
			city = DAOFactory.getInstance().getWeatherDAO(DAOFactory.DAOType.DATA_BASE).getCityForID(cityID);
			updateCity(city);
		}
		if (city != null && city.getWeatherForecast() != null && city.getWeatherForecast().size() != 0 && 
			(!ConnectionUtils.isConnected() || (city.getUpdatedDate() != -1 && currentDate - city.getUpdatedDate() <= MIN_REQUEST_PERIOD_MILLS))) {
			return city;
		}
		City requestedCity = DAOFactory.getInstance().getWeatherDAO(DAOFactory.DAOType.REMOUTE_REQUEST).updateForecastFor(cityID);		
		if (requestedCity != null) {
			requestedCity.setUpdatedDate(currentDate);
			DBHelper.getInstance().updateCity(requestedCity);
			updateCity(requestedCity);
			return requestedCity;
		}
		return null;
	}
	
	public void remove(City city) {
		DBHelper.getInstance().remove(city);
		removeCity(city);
	}
	
	// Local
	public Set<City> getCities() {
		synchronized (cities) {
			return this.cities; 	
		}
	}
	
	public void removeCity(City city) {
		synchronized (cities) {
			this.cities.remove(city); 	
		}
	}
	
	public void updateCity(City city) {
		if (city == null) {
			return;
		}
		synchronized (cities) {
			City c = getCity(city.getCityID());
			if (c != null) {
				if (city.getWeatherDetails() != null) {
					c.setWeatherDetails(city.getWeatherDetails());
				}
				if (city.getWeatherForecast().size() != 0) {
					c.setWeatherForecast(city.getWeatherForecast());
				}
				c.setUpdatedDate(city.getUpdatedDate());
			} else {
				this.cities.add(city);
			}
		}
	}
	
	public void updateCities(Set<City> cities) {
		if (cities == null || cities.size() == 0) {
			return;
		}
		synchronized (cities) {
			this.cities = cities;	
		}
	}
	
	public City getCity(long cityID) {
		synchronized (cities) {
			if (cities == null || cities.isEmpty() || cityID == 0) {
				return null;
			}
			Iterator<City> iterator = cities.iterator();
		    while(iterator.hasNext()){
		    	City c = iterator.next();
		    	if (c.getCityID() == cityID) {
		    		return c;
		    	}
		    }
		    return null;
		}
	}
	
}