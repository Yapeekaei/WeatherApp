package com.weatherapp.test;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

public class WeatherApp extends Application {
	
	private static WeatherApp appContext;
	private AppCompatActivity currentActivity;
	
	@Override
	public void onCreate() {
		super.onCreate();
		appContext = (WeatherApp)getApplicationContext();
	}

	public static WeatherApp getAppContext() {
		return appContext; 
	}

	public AppCompatActivity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(AppCompatActivity currentActivity) {
		this.currentActivity = currentActivity;
	}

}