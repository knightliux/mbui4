package com.moon.android.iptv;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DisclaimerSharepreference {
		
	public static final String DISCLAIMER_SHARE = "DISCLAIMER_SHARE";
	public static final String DISCLAIMER = "DISCLAIMER";
	public static final int HAS_READ = 1;
	public static final int NOT_READ = 0;
	
	public static void setHasRead(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(DISCLAIMER_SHARE, Context.MODE_PRIVATE);
		Editor delEditor = sharedPreferences.edit();
		delEditor.remove(DISCLAIMER);
		delEditor.commit();
		Editor addEditor = sharedPreferences.edit();
		addEditor.putInt(DISCLAIMER, HAS_READ);
		addEditor.commit();
	}
	
	public static Integer getHasRead(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(DISCLAIMER_SHARE, Context.MODE_PRIVATE);
		int value = sharedPreferences.getInt(DISCLAIMER, NOT_READ);
		return value;
	}

}
