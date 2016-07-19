package com.weatherapp.test.dao;

import com.weatherapp.test.ConnectionUtils;

public class DAOFactory {
	
	public static final class DAOType {
		public static final String DATA_BASE = "data_base";
		public static final String REMOUTE_REQUEST = "remoute_request";
	}
	
	private static DAOFactory instance = null;
	
	public static DAOFactory getInstance() {
		if (instance == null) {
			instance = new DAOFactory();
		}
		return instance;
	}
	
	public WeatherDAO getWeatherDAO(String daoType) {
		switch (daoType) {
			case DAOType.DATA_BASE: {
				return new WeatherDatabaseDAO();
			}
			case DAOType.REMOUTE_REQUEST: {
				if (!ConnectionUtils.isConnected()) {
					return new WeatherDatabaseDAO();
				}
				return new WeatherRemouteDAO();
			}
			default: {
				return new WeatherRemouteDAO();
			}
		}
	}
}
