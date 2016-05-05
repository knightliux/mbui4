package com.bestbaan.moonbox.model;

import java.io.Serializable;

public class LauncherMsg implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3696305385895845722L;
	/**
	 * 标记	说明
	   id	开机消息(ID为一的为首条)
	   title	开机消息标题
	   body	开机消息内容
	 */
	private String id;
	private String title;
	private String body;
	
	public LauncherMsg(){}
	
	public LauncherMsg(String id, String title, String body) {
		super();
		this.id = id;
		this.title = title;
		this.body = body;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
