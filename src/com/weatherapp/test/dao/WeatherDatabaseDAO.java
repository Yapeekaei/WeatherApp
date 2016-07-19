package com.weatherapp.test.dao;
import java.util.Set;

import com.weatherapp.test.model.City;

public class WeatherDatabaseDAO implements WeatherDAO {
	
	@Override
	public City getCityForName(String cityName) {
		return null;
	}
	
	@Override
	public City getCityForID(long cityID) {
		return DBHelper.getInstance().getCity(cityID);
	}

	@Override
	public void updateCity(City city) {
		DBHelper.getInstance().updateCity(city);
	}

	@Override
	public Set<City> getCities() {
		return DBHelper.getInstance().getCities();
	}

	@Override
	public void updateCities(Set<City> cities) {
		DBHelper.getInstance().updateCities(cities);
	}

	@Override
	public City updateForecastFor(long cityID) {
		return DBHelper.getInstance().getCity(cityID);
	}
}