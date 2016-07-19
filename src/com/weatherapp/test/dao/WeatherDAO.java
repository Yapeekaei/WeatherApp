package com.weatherapp.test.dao;

import java.util.Set;

import com.weatherapp.test.model.City;

public interface WeatherDAO {
	
	public City getCityForName(String cityName);
	public City getCityForID(long cityID);
	public City updateForecastFor(long cityID);
	public Set<City> getCities();
	public void updateCity(City city);
	public void updateCities(Set<City> cities);

}
