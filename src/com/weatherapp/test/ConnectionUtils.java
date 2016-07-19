package com.weatherapp.test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionUtils {
	
	private static NetworkInfo getNetworkInfo(){
        ConnectivityManager cm = (ConnectivityManager) WeatherApp.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isConnected(){
        NetworkInfo info = ConnectionUtils.getNetworkInfo();
        return (info != null && info.isConnected());
    }

}
