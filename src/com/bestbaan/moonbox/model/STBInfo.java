package com.bestbaan.moonbox.model;

import java.io.Serializable;

public class STBInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2346089976737074744L;
	private String result;
	private String model;
	private String firmware;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getFirmware() {
		return firmware;
	}
	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}
	
	
}
