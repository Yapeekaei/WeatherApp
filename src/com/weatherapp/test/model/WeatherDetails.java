package com.weatherapp.test.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDetails {
	
	private float temp;
	private int humidity;
	private float pressure;
	private float windSpeed;
	private String weatherDesc;
	
	public WeatherDetails() {}
	
	public WeatherDetails(JSONObject json) {
		parseJSON(json);
	}
	
	public WeatherDetails(String json) {
		try {
			JSONObject o = new JSONObject(json);
			parseJSON(o);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
	
	private void parseJSON(JSONObject o) {
		try {
			JSONObject main = o.optJSONObject("main");
			if (main == null) {
				return;
			}
			this.temp = (float)main.getDouble("temp");
			this.humidity = main.getInt("humidity");
			this.pressure = (float)main.getDouble("pressure");
			JSONObject wind = o.getJSONObject("wind");
			this.windSpeed = (float)wind.getDouble("speed");
			JSONArray weatherArray = o.getJSONArray("weather");
			JSONObject weather = weatherArray.getJSONObject(0);
			this.weatherDesc = weather.optString("description", "");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		JSONObject main = new JSONObject();
		JSONObject wind = new JSONObject();
		JSONArray weatherArray = new JSONArray();
		JSONObject weather = new JSONObject();
		try {
			main.put("temp", this.temp);
			main.put("humidity", this.humidity);
			main.put("pressure", this.pressure);
			wind.put("speed", this.windSpeed);
			weather.put("description", this.weatherDesc);
			weatherArray.put(weather);
			o.put("main", main);
			o.put("wind", wind);
			o.put("weather", weatherArray);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return o;
	}
	
	public String toJSONString() {
		return toJSON().toString();
	}
	
	public float getTemp() {
		return temp;
	}
	
	public void setTemp(float temp) {
		this.temp = temp;
	}
	
	public int getHumidity() {
		return humidity;
	}
	
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	
	public float getPressure() {
		return pressure;
	}
	
	public void setPressure(float pressure) {
		this.pressure = pressure;
	}
	
	public float getWindSpeed() {
		return windSpeed;
	}
	
	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getWeatherDesc() {
		return weatherDesc;
	}

	public void setWeatherDesc(String weatherDesc) {
		this.weatherDesc = weatherDesc;
	}
}