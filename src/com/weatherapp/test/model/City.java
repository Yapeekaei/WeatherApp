package com.weatherapp.test.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class City {

	private String cityName = "";
	private long cityID = -1;
	private long updatededDate = -1;
	private WeatherDetails weatherDetails;
	private List<DailyWeatherDetails> weatherForecast = new ArrayList<DailyWeatherDetails>();
	
	public City() {
		
	}
	
	public City(JSONObject json) {
		parseJSONObject(json);
	}
	
	public City(String jsonString) {
		try {
			JSONObject o = new JSONObject(jsonString);
			parseJSONObject(o);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
	
	private void parseJSONObject(JSONObject json) {
		try {
			this.cityName = json.optString("name", null);
			this.cityID = json.optLong("id", 0);
			if (this.cityName == null) {
				JSONObject city = json.optJSONObject("city");
				if (city != null) {
					this.cityName = city.getString("name");
					this.cityID = city.getLong("id");
				}
			}
			if (json.optJSONObject("main") != null) {
				this.weatherDetails = new WeatherDetails(json);
			}
			if (json != null) {
				this.weatherForecast = parseWeatherList(json.toString());
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
	
	public static List<DailyWeatherDetails> parseWeatherList(String json) {
		if (json == null) {
			return null;
		}
		List<DailyWeatherDetails> result = new ArrayList<DailyWeatherDetails>();
		try {
			JSONObject o = new JSONObject(json);
			JSONArray list = o.optJSONArray("list");
			if (list != null) {
				for (int i = 0; i < list.length(); i++) {
					JSONObject item = list.getJSONObject(i);
					DailyWeatherDetails details = new DailyWeatherDetails(item);
					result.add(details);
				}
 			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	public String weatherForecastToEmailString() {
		if (this.weatherForecast.size() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder("");
		try {
			sb.append("<b style='text-decoration: underline;'>" + this.cityName + "</b>").append("<br/>");
			for (DailyWeatherDetails d: weatherForecast) {
				sb.append("<b>" + new SimpleDateFormat("dd.MM.y EEEE", Locale.US).format(new Date(d.getDt() * 1000)) + "</b>").append("<br/>");
				int temp = Math.round((d.getTemp().getMax() + d.getTemp().getMin()) / 2);
				sb.append("Average temperature: " + ((temp > 0 ? "+" : "") + String.valueOf(temp) + (char) 0x00B0 + "C") + "").append("<br/>");
				sb.append("Humidity: " + d.getHumidity() + "%").append("<br/>");
				sb.append("Pressure: " + d.getPressure()).append("<br/>");
				sb.append("Wind speed: " + d.getWindSpeed() + "m/s").append("<br/>");
				sb.append("Description: " + d.getWeatherDescription()).append("<br/>");
				sb.append("<br/>");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return sb.toString();
	}
	
	public String weatherForecastToJSONString() {
		if (this.weatherForecast.size() == 0) {
			return null;
		}
		JSONObject o = new JSONObject();
		JSONArray a = new JSONArray();
		try {
			for (DailyWeatherDetails details: this.weatherForecast) {
				JSONObject d = details.toJSON();
				a.put(d);
			}
			o.put("list", a);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return o.toString();
	}
	
	public long getCityID() {
		return cityID;
	}

	public void setCityID(long cityID) {
		this.cityID = cityID;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String name) {
		this.cityName = name;
	}
	
	public long getUpdatedDate() {
		return updatededDate;
	}

	public void setUpdatedDate(long updatedDate) {
		this.updatededDate = updatedDate;
	}

	public WeatherDetails getWeatherDetails() {
		return weatherDetails;
	}

	public void setWeatherDetails(WeatherDetails weatherDetails) {
		this.weatherDetails = weatherDetails;
	}

	public List<DailyWeatherDetails> getWeatherForecast() {
		return weatherForecast;
	}

	public void setWeatherForecast(List<DailyWeatherDetails> weatherForecast) {
		this.weatherForecast = weatherForecast;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(this.cityID).hashCode() + this.cityName.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof City)) {
			return false;
		}
		return this.cityID == ((City)o).getCityID();
	}
	
}