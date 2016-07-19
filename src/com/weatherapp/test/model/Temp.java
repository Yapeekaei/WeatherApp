package com.weatherapp.test.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Temp {
		
	private float day;
	private float night;
	private float morn;
	private float min;
	private float max;
	private float eve;
	
	public Temp() {	}
	
	public Temp(String tempJson) {
		try {
			JSONObject o = new JSONObject(tempJson);
			this.day = (float)o.getDouble("day");
			this.night = (float)o.getDouble("night");
			this.morn = (float)o.getDouble("morn");
			this.min = (float)o.getDouble("min");
			this.max = (float)o.getDouble("max");
			this.eve = (float)o.getDouble("eve");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
	
	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		try {
			o.put("day", this.day);
			o.put("night", this.night);
			o.put("morn", this.morn);
			o.put("min", this.min);
			o.put("max", this.max);
			o.put("eve", this.eve);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return o;
	}
	
	public String toJSONString() {
		return toJSON().toString();
	}
	
	public float getDay() {
		return day;
	}
	
	public void setDay(float day) {
		this.day = day;
	}
	
	public float getNight() {
		return night;
	}
	
	public void setNight(float night) {
		this.night = night;
	}
	
	public float getMorn() {
		return morn;
	}
	
	public void setMorn(float morn) {
		this.morn = morn;
	}
	
	public float getMin() {
		return min;
	}
	
	public void setMin(float min) {
		this.min = min;
	}
	
	public float getMax() {
		return max;
	}
	
	public void setMax(float max) {
		this.max = max;
	}
	
	public float getEve() {
		return eve;
	}
	
	public void setEve(float eve) {
		this.eve = eve;
	}
	
}