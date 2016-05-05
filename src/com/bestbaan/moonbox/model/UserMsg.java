package com.bestbaan.moonbox.model;

import java.io.Serializable;

public class UserMsg implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 979087703654362835L;
	private String iid;
	private String id;
	private String title;
	private String body;
	private String time;
	private String status;
	
	
	public UserMsg(){}
	
	public UserMsg(String iid, String id, String title, String body,
			String time, String status) {
		super();
		this.iid = iid;
		this.id = id;
		this.title = title;
		this.body = body;
		this.time = time;
		this.status = status;
	}
	
	
	public String getIid() {
		return iid;
	}
	public void setIid(String iid) {
		this.iid = iid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
