package com.weatherapp.test.widgets;

import com.weatherapp.test.R;
import com.weatherapp.test.model.DailyWeatherDetails;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;


public class ForecastItemView extends LinearLayoutCompat {

	private LinearLayoutCompat itemLayout;
	private AppCompatTextView dateHolder;
	private AppCompatTextView temperatureHolder;
	private AppCompatTextView humidityHolder;
	private AppCompatTextView pressureHolder;
	private AppCompatTextView descriptionHolder;
	private AppCompatTextView windSpeedHolder;
	private DailyWeatherDetails details;

	public ForecastItemView(Context context) {
	   super(context);
	   init();
    }

    public ForecastItemView(Context context, AttributeSet attrs) {
       super(context, attrs);
       init();
    }

    public ForecastItemView(Context context, AttributeSet attrs, int defStyle) {
       super(context, attrs, defStyle);
       init();
    }

    private void init() {
        inflate(getContext(), R.layout.forecastitem, this);
        this.itemLayout = (LinearLayoutCompat)findViewById(R.id.forecast_item);
        this.dateHolder = (AppCompatTextView)findViewById(R.id.dateholder);
        this.humidityHolder = (AppCompatTextView)findViewById(R.id.humidity);
        this.temperatureHolder = (AppCompatTextView)findViewById(R.id.temp);
        this.pressureHolder = (AppCompatTextView)findViewById(R.id.pressure);
        this.descriptionHolder = (AppCompatTextView)findViewById(R.id.description);
        this.windSpeedHolder = (AppCompatTextView)findViewById(R.id.wind_speed);
    }

	public AppCompatTextView getDateHolder() {
		return dateHolder;
	}

	public AppCompatTextView getTemperatureHolder() {
		return temperatureHolder;
	}

	public AppCompatTextView getHumidityHolder() {
		return humidityHolder;
	}

	public AppCompatTextView getPressureHolder() {
		return pressureHolder;
	}

	public AppCompatTextView getDescriptionHolder() {
		return descriptionHolder;
	}

	public AppCompatTextView getWindSpeedHolder() {
		return windSpeedHolder;
	}

	public LinearLayoutCompat getItemLayout() {
		return itemLayout;
	}

	public DailyWeatherDetails getDetails() {
		return details;
	}

	public void setDetails(DailyWeatherDetails details) {
		this.details = details;
	}
	
}