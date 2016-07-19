package com.weatherapp.test;

public final class Constants {
	
	public static final String WEATHER_API_KEY = "3798e038d2fccae3a690fe94b7eb6c1b"; 
	
	public static class UrlConstants {
		public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
		public static final String CITY_FORCAST = BASE_URL + "weather"; 
		public static final String DAILY_FORCAST = BASE_URL + "forecast/daily";
		public static final String CITIES_SET = BASE_URL + "group";
		public static final String CITIES_SET_CYCLE = BASE_URL + "find";
	}

}
