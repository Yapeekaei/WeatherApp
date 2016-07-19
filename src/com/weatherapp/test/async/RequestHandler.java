package com.weatherapp.test.async;

public interface RequestHandler {
	
	public void onResponseDone(String requestType, Object response);

}
