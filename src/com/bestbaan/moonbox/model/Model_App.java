package com.bestbaan.moonbox.model;

import android.graphics.drawable.Drawable;

public class Model_App {
       public String AppName;
       public String pkgName;
       public String activityInfoName;
       public Drawable icon;
       public Long firstInstallTime;
       public Long lastUpdateTime;
       public String versionName;
	   public int versionCode;
	   public Model_App(){}
       public Model_App( String AppName,String pkgName,String activityInfoName,Drawable icon,Long firstInstallTime,Long lastUpdateTime,String versionName,int versionCode){
    	   this.AppName=AppName;
    	   this.pkgName=pkgName;
    	   this.activityInfoName=activityInfoName;
    	   this.icon=icon;
    	   this.firstInstallTime=firstInstallTime;
    	   this.lastUpdateTime=lastUpdateTime;
    	   this.versionName=versionName;
    	   this.versionCode=versionCode;
       }
       
}
