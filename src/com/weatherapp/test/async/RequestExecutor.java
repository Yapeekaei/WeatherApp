package com.weatherapp.test.async;

import com.weatherapp.test.controllers.DataController;
import com.weatherapp.test.model.City;

import android.os.AsyncTask;

public class RequestExecutor extends AsyncTask<Object, Object, Object> {
	
	private String requestType;
	private RequestHandler handler; 

	public RequestExecutor(RequestHandler handler) {
		this.handler = handler;
	}

	@Override
	protected Object doInBackground(Object... params) {
		requestType = (String)params[0];
		switch (requestType) {
			case DataController.RequestType.GET_CITIES_LIST: {
				return DataController.getInstance().getCitiesSet(false);
			}
			case DataController.RequestType.GET_CITIES_LIST_FORCE: {
				return DataController.getInstance().getCitiesSet(true);
			}
			case DataController.RequestType.GET_WEATHER_FORECAST_FOR: {
				long cityID = (long)params[1];
				return DataController.getInstance().getWeatherForecastFor(cityID);
			}
			case DataController.RequestType.GET_WEATHER_FOR: {
				String cityName = (String)params[1]; 
				return DataController.getInstance().getWeatherFor(cityName);
			}
			case DataController.RequestType.REMOVE_CITY: {
				City city = (City)params[1];
				DataController.getInstance().remove(city);
				return null;
			}
			default: {
				return null;		
			}
		}
	}
	
	@Override
	protected void onPostExecute(Object result) {
		if (handler == null) {
			return;
		}
		handler.onResponseDone(requestType, result);
	}
}