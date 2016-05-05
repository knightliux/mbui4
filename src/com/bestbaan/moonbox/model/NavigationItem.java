package com.bestbaan.moonbox.model;

import android.view.View;

public class NavigationItem {

	private int id;
	private int nameRes;
	public View view;

	public NavigationItem(int id, int nameRes, View view) {
		this.id = id;
		this.nameRes = nameRes;
		this.view = view;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNameRes() {
		return nameRes;
	}

	public void setNameRes(int nameRes) {
		this.nameRes = nameRes;
	}

	public View getFragment() {
		return view;
	}

	public void setFragment(View fragment) {
		this.view = fragment;
	}
}
