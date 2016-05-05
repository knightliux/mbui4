package com.bestbaan.moonbox.model;

public class LifeModel {
	public String name;
	public String url;
	public String img;
	public String type;
	public String detail;
	public String id;
    public LifeModel(String name,String url,String img,String type,String detail){
    	this.name=name;
    	this.url=url;
    	this.img=img;
    	this.type=type;
    	this.detail=detail;
    }
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
