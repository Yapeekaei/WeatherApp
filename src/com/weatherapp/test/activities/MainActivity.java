package com.weatherapp.test.activities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.weatherapp.test.ConnectionUtils;
import com.weatherapp.test.R;
import com.weatherapp.test.WeatherApp;
import com.weatherapp.test.async.RequestExecutor;
import com.weatherapp.test.async.RequestHandler;
import com.weatherapp.test.controllers.DataController;
import com.weatherapp.test.model.City;
import com.weatherapp.test.widgets.CitiesRecyclerViewAdapter;
import com.weatherapp.test.widgets.ForecastRecyclerViewAdapter;

import android.support.v7.app.*;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Toast;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements RequestHandler, OnClickListener {
	
	private RecyclerView citiesRecyclerView;
	private CitiesRecyclerViewAdapter adapter;
	private RecyclerView.LayoutManager lManager;
	private AppCompatTextView noConnectionView;
	private AlertDialog searchingDialog;
	private AlertDialog forecastDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		citiesRecyclerView = (RecyclerView)findViewById(R.id.cities_list);
		noConnectionView = (AppCompatTextView)findViewById(R.id.no_connection_view);
				
		lManager = new LinearLayoutManager(this);
	    citiesRecyclerView.setLayoutManager(lManager);
	    
		try {
		    ViewConfiguration config = ViewConfiguration.get(this);
		    Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
		    if(menuKeyField != null) {
		         menuKeyField.setAccessible(true);
		         menuKeyField.setBoolean(config, false);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Object[] requestParams = new Object[] {DataController.RequestType.GET_CITIES_LIST};
		new RequestExecutor(this).execute(requestParams);
		WeatherApp.getAppContext().setCurrentActivity(this);
	}
	
	@Override
	public void onResponseDone(String requestType, Object response) {
		switch (requestType) {
			case DataController.RequestType.GET_CITIES_LIST:
			case DataController.RequestType.GET_CITIES_LIST_FORCE: {
				ArrayList<City> list = new ArrayList<City>(DataController.getInstance().getCities());
				Collections.sort(list, new Comparator<City>() {
					@Override
					public int compare(City cityOne, City cityTwo) {
						return cityOne.getCityName().compareTo(cityTwo.getCityName());
					}
				});
				noConnectionView.setVisibility(list.size() == 0 ? View.VISIBLE : View.INVISIBLE);
				citiesRecyclerView.setVisibility(list.size() == 0 ? View.INVISIBLE : View.VISIBLE);
				adapter = new CitiesRecyclerViewAdapter(list);
				citiesRecyclerView.setAdapter(adapter);
				citiesRecyclerView.invalidate();
				break;
			}
			case DataController.RequestType.GET_WEATHER_FORECAST_FOR: {
				if (response != null) {
					final City city = (City)response;
					if (city.getWeatherForecast() == null && city.getWeatherForecast().isEmpty()) {
						Toast.makeText(this, R.string.forecast_is_empty, Toast.LENGTH_LONG).show();
						break;
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(WeatherApp.getAppContext().getCurrentActivity());
		        	builder.setView(R.layout.forecast);
		        	builder.setCancelable(true);
		        	forecastDialog = builder.create();
		        	forecastDialog.show();
		        	forecastDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		        	AppCompatTextView title = (AppCompatTextView)forecastDialog.findViewById(R.id.city_title);
		        	title.setText(city.getCityName());
		        	AppCompatButton sendBtn = (AppCompatButton)forecastDialog.findViewById(R.id.send_btn);
		        	sendBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							final Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
							intent.putExtra(Intent.EXTRA_SUBJECT, "Weather forecast for " + city.getCityName());
							intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(city.weatherForecastToEmailString()));
							try {
							    startActivity(Intent.createChooser(intent, "Send mail"));
							} catch (ActivityNotFoundException ex) {
							    Toast.makeText(MainActivity.this, "Can't find email client", Toast.LENGTH_SHORT).show();
							}
						}
					});
		        	RecyclerView forecastView = (RecyclerView)forecastDialog.findViewById(R.id.forecast_list);
		        	RecyclerView.LayoutManager lM = new LinearLayoutManager(this);
		        	forecastView.setLayoutManager(lM);
		        	ForecastRecyclerViewAdapter adapter = new ForecastRecyclerViewAdapter(city.getWeatherForecast());
		        	forecastView.setAdapter(adapter);
		        	forecastView.invalidate();
				}
				break;
			}
			case DataController.RequestType.GET_WEATHER_FOR: {
				searchingDialog.dismiss();
				if (response == null) {
					Toast.makeText(this, R.string.city_not_found_alert, Toast.LENGTH_LONG).show();
					break;
				}
				City city = (City)response;
				adapter.addItem(city);
				break;
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.app_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (!ConnectionUtils.isConnected()) {
    		Toast.makeText(this, R.string.no_connection_toast, Toast.LENGTH_LONG).show();
    		return true;
    	}
		 switch (item.getItemId()) {
	        case R.id.add_city: {
	        	AlertDialog.Builder builder = new AlertDialog.Builder(WeatherApp.getAppContext().getCurrentActivity());
	        	builder.setView(R.layout.search_city_layout);
	        	builder.setCancelable(true);
	        	searchingDialog = builder.create();
	        	searchingDialog.show();
	        	searchingDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	        	AppCompatButton searchBtn = (AppCompatButton)searchingDialog.findViewById(R.id.find);
	        	searchBtn.setOnClickListener(this);
	            return true;
	        }
	        case R.id.refresh: {
	        	Object[] requestParams = new Object[] {DataController.RequestType.GET_CITIES_LIST_FORCE};
	    		new RequestExecutor(this).execute(requestParams);
	        	return true;
	        }
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.find: {
				if (searchingDialog == null) {
					return;
				}
				AppCompatEditText searchEdit = (AppCompatEditText)searchingDialog.findViewById(R.id.city_name);
				String cityName = searchEdit.getText().toString();
				if (cityName.isEmpty()) {
					Toast.makeText(this, R.string.cant_be_empty_alert, Toast.LENGTH_SHORT).show();
					return;
				}
				Object[] requestParams = new Object[] {DataController.RequestType.GET_WEATHER_FOR, cityName};
				new RequestExecutor(this).execute(requestParams);
				break;
			}
		}
	}
	
}