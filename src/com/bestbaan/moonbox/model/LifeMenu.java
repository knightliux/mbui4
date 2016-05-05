package com.bestbaan.moonbox.model;


public class LifeMenu {
	public int imgRes;
	public int hoverImgRes;
	public String Name;
	public String Pid;

	public LifeMenu(int imgRes, int hoverImgRes, String Name, String Pid) {
		this.imgRes = imgRes;
		this.Name = Name;
		this.Pid = Pid;

		if (hoverImgRes == 0) {
			this.hoverImgRes = imgRes;
		} else {
			this.hoverImgRes = hoverImgRes;
		}
	}

	public int getHoverImgRes() {
		return hoverImgRes;
	}

	public void setHoverImgRes(int hoverImgRes) {
		this.hoverImgRes = hoverImgRes;
	}

	public int getImgRes() {
		return imgRes;
	}

	public void setImgRes(int imgRes) {
		this.imgRes = imgRes;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPid() {
		return Pid;
	}

	public void setPid(String pid) {
		Pid = pid;
	}

}
