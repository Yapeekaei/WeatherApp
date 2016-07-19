package com.weatherapp.test.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DailyWeatherDetails {
	
	private long dt;
	private float pressure;
	private int humidity;
	private float windSpeed;
	private String weatherDescription;
	private Temp temp;
	
	public DailyWeatherDetails() { }
	
	public DailyWeatherDetails(String details) {
		try {
			JSONObject o = new JSONObject(details);
			parseJSONObject(o);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
	
	public DailyWeatherDetails(JSONObject o) {
		parseJSONObject(o);
	}
	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		JSONArray weather = new JSONArray();
		try {
			o.put("dt", this.dt);
			o.put("pressure", this.pressure);
			o.put("humidity", this.humidity);
			o.put("speed", this.windSpeed);
			JSONObject weatherItem = new JSONObject();
			weatherItem.put("description", this.weatherDescription);
			weather.put(weatherItem);
			o.put("weather", weather);
			if (this.temp != null) {
				o.put("temp", this.temp.toJSON());
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return o;
	}
	
	public String toJSONString() {
		return toJSON().toString();
	}
	
	private void parseJSONObject(JSONObject o) {
		try {
			this.dt = o.optLong("dt", 0);
			this.pressure = (float)o.getDouble("pressure");
			this.humidity = o.getInt("humidity");
			this.windSpeed = (float)o.getDouble("speed");
			JSONArray weather = o.getJSONArray("weather");
			if (weather != null) {
				JSONObject weatherItem = weather.getJSONObject(0);
				this.weatherDescription = weatherItem.optString("description", "");
			}
			this.temp = new Temp(o.getString("temp"));
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
	
	public long getDt() {
		return dt;
	}
	
	public void setDt(long dt) {
		this.dt = dt;
	}
	
	public float getPressure() {
		return pressure;
	}
	
	public void setPressure(float pressure) {
		this.pressure = pressure;
	}
	
	public int getHumidity() {
		return humidity;
	}
	
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	
	public float getWindSpeed() {
		return windSpeed;
	}
	
	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}
	
	public Temp getTemp() {
		return temp;
	}

	public void setTemp(Temp temp) {
		this.temp = temp;
	}

	public String getWeatherDescription() {
		return weatherDescription;
	}

	public void setWeatherDescription(String weatherDescription) {
		this.weatherDescription = weatherDescription;
	}

}