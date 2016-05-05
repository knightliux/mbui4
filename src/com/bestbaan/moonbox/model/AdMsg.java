package com.bestbaan.moonbox.model;

import java.io.Serializable;

public class AdMsg implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1564658660116635499L;
	
	
	private String id;
	private String picurl;
	private String adurl;
	private String type;
	private String key;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getAdurl() {
		return adurl;
	}
	public void setAdurl(String adurl) {
		this.adurl = adurl;
	}
}
