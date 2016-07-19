package com.weatherapp.test.widgets;

import com.weatherapp.test.R;
import com.weatherapp.test.WeatherApp;
import com.weatherapp.test.async.RequestExecutor;
import com.weatherapp.test.async.RequestHandler;
import com.weatherapp.test.controllers.DataController;
import com.weatherapp.test.model.City;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;

import android.view.View;
import android.view.View.OnClickListener;

public class CityItemView extends LinearLayoutCompat implements OnClickListener {

	private LinearLayoutCompat itemLayout;
	private AppCompatTextView cityNameHolder;
	private AppCompatTextView temperatureHolder;
	private AppCompatTextView detailsHolder;
	private AppCompatButton deleteButton;
	private City city;

	public CityItemView(Context context) {
	   super(context);
	   init();
    }

    public CityItemView(Context context, AttributeSet attrs) {
       super(context, attrs);
       init();
    }

    public CityItemView(Context context, AttributeSet attrs, int defStyle) {
       super(context, attrs, defStyle);
       init();
    }

    private void init() {
        inflate(getContext(), R.layout.city_list_item, this);
        this.itemLayout = (LinearLayoutCompat)findViewById(R.id.city_item);
        this.cityNameHolder = (AppCompatTextView)findViewById(R.id.city_name);
        this.temperatureHolder = (AppCompatTextView)findViewById(R.id.temperature);
        this.detailsHolder = (AppCompatTextView)findViewById(R.id.details);
        this.deleteButton = (AppCompatButton)findViewById(R.id.delete_item);
        this.itemLayout.setOnClickListener(this);
    }
    
    public AppCompatTextView getCityNameHolder() {
		return cityNameHolder;
	}
	
	public AppCompatTextView getTemperatureHolder() {
		return temperatureHolder;
	}

	public AppCompatButton getDeleteButton() {
		return deleteButton;
	}

	public AppCompatTextView getDetailsHolder() {
		return detailsHolder;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.city_item: {
				AppCompatActivity currentActivity = WeatherApp.getAppContext().getCurrentActivity();
				RequestHandler handler = currentActivity != null ? (RequestHandler)currentActivity : null;
				new RequestExecutor(handler).execute(DataController.RequestType.GET_WEATHER_FORECAST_FOR, this.city.getCityID());
				break;
			}
		}
	}
	
}