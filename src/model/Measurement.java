package model;

import java.util.Date;

public class Measurement {
	private Date time;
	private float value;
	
	public Measurement(float value){
		this.time = new Date();
		this.value = value;
	}

	public Date getTime() {
		return time;
	}

	public float getValue() {
		return value;
	}
	
}
