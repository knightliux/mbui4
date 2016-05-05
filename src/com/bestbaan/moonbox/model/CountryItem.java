package com.bestbaan.moonbox.model;

import java.util.Locale;

public class CountryItem {
	public int imageRes;
	public int nameRes;
	public Locale locale;

	public CountryItem(int imageRes, int nameRes, Locale locale) {
		this.imageRes = imageRes;
		this.nameRes = nameRes;
		this.locale = locale;
	}
}
