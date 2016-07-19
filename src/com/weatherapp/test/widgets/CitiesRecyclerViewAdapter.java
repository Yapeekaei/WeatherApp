package com.weatherapp.test.widgets;

import java.util.List;

import com.weatherapp.test.R;
import com.weatherapp.test.WeatherApp;
import com.weatherapp.test.async.RequestExecutor;
import com.weatherapp.test.async.RequestHandler;
import com.weatherapp.test.controllers.DataController;
import com.weatherapp.test.model.City;
import com.weatherapp.test.model.WeatherDetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CitiesRecyclerViewAdapter extends RecyclerView.Adapter<CitiesRecyclerViewAdapter.ViewHolder> {
	
    private List<City> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CityItemView cityItem;
        public ViewHolder(CityItemView v) {
            super(v);
            cityItem = v;
        }
    }

    public CitiesRecyclerViewAdapter(List<City> data) {
       this.data = data;
    }

    @Override
    public CitiesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CityItemView v = new CityItemView(parent.getContext());
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
    	City c = data.get(position);
    	WeatherDetails d = c.getWeatherDetails();
    	holder.cityItem.setCity(c);
        holder.cityItem.getCityNameHolder().setText(c.getCityName());
        if (d != null) {
        	int temp = Math.round(d.getTemp());
        	holder.cityItem.getTemperatureHolder().setText((temp > 0 ? "+" : "") + String.valueOf(temp) + (char) 0x00B0 + "C");
        }
        if (c.getWeatherDetails() != null) {
        	holder.cityItem.getDetailsHolder().setText(c.getWeatherDetails().getWeatherDesc());
        }
        holder.cityItem.getDeleteButton().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final City item = data.get(position);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(WeatherApp.getAppContext().getCurrentActivity());
				builder.setMessage("Are you sure you want remove " + item.getCityName() + "?");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Object[] requestParams = new Object[] {DataController.RequestType.REMOVE_CITY, item};
							new RequestExecutor((RequestHandler)WeatherApp.getAppContext().getCurrentActivity()).execute(requestParams);
							data.remove(item);
							notifyItemRemoved(position);
							notifyItemRangeChanged(position, data.size());
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
				builder.setNegativeButton("Cancel", null);
				builder.show();
			}
		});
    }
    
    public void addItem(City city) {
    	 if (data.contains(city)) {
    		 Toast.makeText(WeatherApp.getAppContext().getCurrentActivity(), R.string.city_is_already_exists, Toast.LENGTH_LONG).show();
    		 return;
    	 }
    	 data.add(0, city);
         this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}