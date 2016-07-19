package com.weatherapp.test.widgets;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.weatherapp.test.model.DailyWeatherDetails;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ViewHolder> {
	
    private List<DailyWeatherDetails> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ForecastItemView forecastItem;
        public ViewHolder(ForecastItemView v) {
            super(v);
            forecastItem = v;
        }
    }

    public ForecastRecyclerViewAdapter(List<DailyWeatherDetails> data) {
       this.data = data;
    }

    @Override
    public ForecastRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    	ForecastItemView v = new ForecastItemView(parent.getContext());
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
    	DailyWeatherDetails d = data.get(position);
    	holder.forecastItem.setDetails(d);
        holder.forecastItem.getDateHolder().setText(new SimpleDateFormat("dd.MM.yyyy EEEE", Locale.US).format(new Date(d.getDt() * 1000)));
        holder.forecastItem.getPressureHolder().setText(String.valueOf(d.getPressure()));
        holder.forecastItem.getHumidityHolder().setText(String.valueOf(d.getHumidity()) + "%");
        holder.forecastItem.getWindSpeedHolder().setText(String.valueOf(d.getWindSpeed()) + "m/s");
        holder.forecastItem.getDescriptionHolder().setText(d.getWeatherDescription());
        if (d != null) {
        	int temp = Math.round((d.getTemp().getMax() + d.getTemp().getMin()) / 2);
        	holder.forecastItem.getTemperatureHolder().setText((temp > 0 ? "+" : "") + String.valueOf(temp) + (char) 0x00B0 + "C");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}