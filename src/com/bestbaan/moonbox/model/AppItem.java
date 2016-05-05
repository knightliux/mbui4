package com.bestbaan.moonbox.model;

import android.graphics.drawable.Drawable;

public class AppItem {

	Drawable icon;
	String appName;
	String pkgName;
	
	public AppItem(Drawable iconRes, String appName, String pkgName) {
		super();
		this.icon = iconRes;
		this.appName = appName;
		this.pkgName = pkgName;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	
	
}
