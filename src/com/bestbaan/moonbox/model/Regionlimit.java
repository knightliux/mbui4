package com.bestbaan.moonbox.model;

import java.io.Serializable;

public class Regionlimit implements Serializable{
	
	private static final long serialVersionUID = -2358281745406353738L;
	
	private String code;
	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
