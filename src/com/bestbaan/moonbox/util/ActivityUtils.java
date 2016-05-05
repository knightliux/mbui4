package com.bestbaan.moonbox.util;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bestbaan.moonbox.view.CustomToast;
import com.moon.android.iptv.Configs;
import com.moonbox.android.iptv.R;

public class ActivityUtils {
	
	public static void startActivity(Context context,String pkgName){
		Intent intent = new Intent();
		try{
			if(null != pkgName && !"".equals(pkgName))
			intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
			if (null != intent){
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			} else {
				new CustomToast(context, context.getString(R.string.app_not_found), Configs.TOAST_TEXT_SIZE).show();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void startActivity(Activity context, Class<?> classs){
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	intent.setClass(context, classs);
    	context.startActivity(intent);
	}
	
	public static void startActivityForResult(Activity context, Class<?> classs, String strName, String result){
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(context, classs);
		Bundle  budle = new Bundle();
		budle.putString(strName, result);
		intent.putExtras(budle);
    	context.startActivityForResult(intent, 0);
	}
	
	public static void startActivityForResult(Activity context, Class<?> classs, int resultFlag){
		Intent intent = new Intent(context, classs); 
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivityForResult(intent, resultFlag);  
	}
	
	public static void startActivity(Activity context, Class<?> classs, String key, List<?> result){
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(context, classs);
		intent.putExtra(key, result.toArray());
    	context.startActivity(intent);
	}
	
	public static void startActivity(Activity context, Class<?> classs, String key, Serializable  result){
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(context, classs);
		intent.putExtra(key, result);
    	context.startActivity(intent);
	}

}
