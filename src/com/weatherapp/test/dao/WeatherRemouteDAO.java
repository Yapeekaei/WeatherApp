package com.weatherapp.test.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.weatherapp.test.Constants;
import com.weatherapp.test.WeatherApp;
import com.weatherapp.test.controllers.DataController;
import com.weatherapp.test.model.City;

public class WeatherRemouteDAO implements WeatherDAO {
	
	private static final int CONNECTION_TIMEOUT = 60000;

	@Override
	public void updateCity(City city) {	}
	
	@Override
	public void updateCities(Set<City> cities) { }
	
	@Override
	public City getCityForName(String cityName) {
		String url = Constants.UrlConstants.CITY_FORCAST + "?q=" + cityName + "&appid=" + Constants.WEATHER_API_KEY + "&units=metric&lang=en";
		String requestResult = executeRemouteRequest(url);
		if (requestResult != null) {
			City city = new City(requestResult);
			return city.getCityID() != -1 && city.getCityName() != null && !city.getCityName().isEmpty() ? city : null; 
		}
		return null;
	}
	
	@Override
	public City updateForecastFor(long cityId) {
		String url = Constants.UrlConstants.DAILY_FORCAST + "?id=" + cityId + "&appid=" + Constants.WEATHER_API_KEY + "&units=metric&lang=en";
		String requestResult = executeRemouteRequest(url);
		if (requestResult != null) {
			return new City(requestResult);
		}
		return null;
	}
		
	@Override
	public Set<City> getCities() {
		Set<City> cachedSet = DataController.getInstance().getCities();
		String requestResult = null;
		if (cachedSet.size() == 0) {
			cachedSet = DBHelper.getInstance().getCities();
		}
		if (cachedSet.size() == 0) {
			LocationManager locationManager = (LocationManager)WeatherApp.getAppContext().getSystemService(Context.LOCATION_SERVICE);
			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location == null) {
				return null;
			}
			String url = Constants.UrlConstants.CITIES_SET_CYCLE + "?lon=" + location.getLongitude() + "&lat=" + location.getLatitude() + 
						"&appid=" + Constants.WEATHER_API_KEY + "&units=metric&lang=en&cnt=20";
			requestResult = executeRemouteRequest(url);
			if (requestResult == null) {
				return null;
			}
			return jsonToCities(requestResult);
		}
		StringBuilder idVals = new StringBuilder("");
		Iterator<City> iterator = cachedSet.iterator();
		while (iterator.hasNext()) {
			City c = iterator.next();
			idVals.append(c.getCityID());
			if (iterator.hasNext()) {
				idVals.append(",");
			}
		}
		String url = Constants.UrlConstants.CITIES_SET + "?id=" + idVals.toString() + "&appid=" + Constants.WEATHER_API_KEY + "&units=metric&lang=en";
		requestResult = executeRemouteRequest(url);
		if (requestResult == null) {
			return null;
		}
		return jsonToCities(requestResult);
	}
	
	private Set<City> jsonToCities(String jsonString) {
		try {
			JSONObject o = new JSONObject(jsonString);
			JSONArray list = o.getJSONArray("list");
			if (list == null || list.length() == 0) {
				return null;
			}
			Set<City> resultSet = new LinkedHashSet<City>();
			for (int i = 0; i < list.length(); i++) {
				City c = new City(list.getJSONObject(i));
				if (!c.getCityName().isEmpty() && c.getCityID() != 0) {
					resultSet.add(c);
				}
			}
			if (resultSet.size() == 0) {
				return null;
			}
			return resultSet;
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private final String executeRemouteRequest(String url) {
		if (url == null) {
			return null;
		}
		StringBuilder result = new StringBuilder("");
		
		HttpURLConnection urlc = null;
		BufferedReader rd = null;
		InputStream is = null;
		try {
			URL requestURL = new URL(url);	
			urlc = (HttpURLConnection)requestURL.openConnection();
			urlc.setRequestMethod("GET");
			urlc.setDoOutput(false);
			urlc.setUseCaches(false);
	        urlc.setAllowUserInteraction(false);
	        urlc.setConnectTimeout(CONNECTION_TIMEOUT);
	        urlc.setReadTimeout(CONNECTION_TIMEOUT);
			
			int respCode = urlc.getResponseCode();
			if (respCode == 404) {
				return null;
			}
			is = respCode >= 200 && respCode <= 226 ? urlc.getInputStream() : urlc.getErrorStream();
			rd = new BufferedReader(new InputStreamReader(is), 8096);
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (is != null) {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        if (rd != null) {
	            try {
	            	rd.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
		}
		return result.toString();
	}

	@Override
	public City getCityForID(long cityID) {
		// TODO Auto-generated method stub
		return null;
	}
}