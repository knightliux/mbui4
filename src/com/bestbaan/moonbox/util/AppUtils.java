package com.bestbaan.moonbox.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.moon.android.iptv.Configs;

public class AppUtils {

	public static final Logger log = Logger.getInstance();

	public static boolean isAppInstalled(Context context, String uri) {
		PackageManager pm = context.getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(uri, 0);
			log.i("Is installed ");
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			log.e(e.toString());
			installed = false;
		}
		return installed;
	}

	/**
	 * @param context
	 * @param deskAppInfoList desk appList in database
	 * @return 设置为置桌面的APP，如果系统也存在，则显示在桌面
	 */
	public static List<AppInfo> getAppsByPackage(Context context,
			List<AppInfo> deskAppInfoList) {
		List<AppInfo> listResult = new ArrayList<AppInfo>();
		List<AppInfo> userInstalledList = getUserInstalledApps(context, false);
		for (AppInfo deskAppInfo : deskAppInfoList) {
			for (AppInfo info : userInstalledList) {
				if (info.packageName.equals(deskAppInfo.packageName)) {
					info.setPosition(deskAppInfo.getPosition());
					Log.d("ppkg","pack:"+info.packageName+"title:"+info.appName);
					boolean go = true;
					for (String pkg : Configs.getHiddendestopPkg()) {
					//	Log.d("hid","hid"+pkg);
						if (info.packageName.equals(pkg)) {
						    Log.d("hid", pkg);
							go = false;
							break;
						}
					}
					if (!go)
						continue;
					//if(info.packageName.equals(""))
					listResult.add(info);
					break;
				}
			}
		}
		// AppTimerComparator comparator = new AppTimerComparator();
		// Collections.sort(listResult, comparator);
		return listResult;
	}
	public static List<AppInfo> getConfApp(Context context,
			boolean isSortByTime) {
		DbUtil db=new DbUtil(context);
		ArrayList<AppInfo> listReturn = new ArrayList<AppInfo>();
		PackageManager packageManager = context.getPackageManager();

		List<PackageInfo> packages = packageManager.getInstalledPackages(0);

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = packageManager.queryIntentActivities(
				mainIntent, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(
				packageManager));

		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			// if(packageInfo.packageName.equals(Configs.THIS_APP_PACKAGE_NAME))
			// continue;
			// 判断是否显示此应用 start
			boolean go = false;
			for (String pkg : Configs.getShopApp()) {
				if (packageInfo.packageName.equals(pkg)) {
					go=true;
					break;
				}
				
			}
			if (!go) continue;
			if (packageInfo.packageName.equals(Configs.THIS_APP_PACKAGE_NAME))continue;
			// 判断是否显示此应用 end
			AppInfo appInfo = new AppInfo();
			
				
			try {
				appInfo.appName = packageInfo.applicationInfo.loadLabel(
						packageManager).toString();
				appInfo.packageName = packageInfo.packageName;
				appInfo.versionName = packageInfo.versionName;
				appInfo.versionCode = packageInfo.versionCode;
				appInfo.appIcon = packageInfo.applicationInfo
						.loadIcon(packageManager);
				appInfo.firstInstallTime = packageInfo.firstInstallTime;
				appInfo.position=db.GetAppIndex(appInfo.packageName);
				// Logger.getLogger().i(appInfo.appName + "  " +
				// appInfo.firstInstallTime);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 系统应用
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0
					&& true) {
			
			} else{
				 //listReturn.add(appInfo);
				listReturn.add(appInfo);
			}
				// 非系统应用
				//listReturn.add(appInfo);
				//listReturn.add(new AppInfo());
		}
//		if (isSortByTime) {
			AppTimerDesc comparator = new AppTimerDesc();
			Collections.sort(listReturn, comparator);
//		}
		
	
//		for(int i=0;i<listReturn.size();i++){
//			Log.d("index",listReturn.get(i).packageName+"----"+listReturn.get(i).appName+"#index:"+listReturn.get(i).position);
//		}
		return listReturn;
	}
	public static List<AppInfo> getUserApp(Context context,
			boolean isSortByTime) {
		DbUtil db=new DbUtil(context);
		ArrayList<AppInfo> listReturn = new ArrayList<AppInfo>();
		PackageManager packageManager = context.getPackageManager();

		List<PackageInfo> packages = packageManager.getInstalledPackages(0);

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = packageManager.queryIntentActivities(
				mainIntent, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(
				packageManager));

		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			// if(packageInfo.packageName.equals(Configs.THIS_APP_PACKAGE_NAME))
			// continue;
			// 判断是否显示此应用 start
			boolean go = true;
			for (String pkg : Configs.getHiddendestopPkg()) {
				if (packageInfo.packageName.equals(pkg)) {
					go = false;
					break;
				}
				
			}
			if (!go) continue;
			if (packageInfo.packageName.equals(Configs.THIS_APP_PACKAGE_NAME))continue;
			// 判断是否显示此应用 end
			AppInfo appInfo = new AppInfo();
			
				
			try {
				appInfo.appName = packageInfo.applicationInfo.loadLabel(
						packageManager).toString();
				appInfo.packageName = packageInfo.packageName;
				appInfo.versionName = packageInfo.versionName;
				appInfo.versionCode = packageInfo.versionCode;
				appInfo.appIcon = packageInfo.applicationInfo
						.loadIcon(packageManager);
				appInfo.firstInstallTime = packageInfo.firstInstallTime;
				appInfo.position=db.GetAppIndex(appInfo.packageName);
				// Logger.getLogger().i(appInfo.appName + "  " +
				// appInfo.firstInstallTime);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 系统应用
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0
					&& true) {
			
			} else{
				 //listReturn.add(appInfo);
				listReturn.add(appInfo);
			}
				// 非系统应用
				//listReturn.add(appInfo);
				//listReturn.add(new AppInfo());
		}
//		if (isSortByTime) {
//			AppTimerDesc comparator = new AppTimerDesc();
//			Collections.sort(listReturn, comparator);
//		}
		
		AppPos comparator_pos = new AppPos();
		Collections.sort(listReturn, comparator_pos);
//		for(int i=0;i<listReturn.size();i++){
//			Log.d("index",listReturn.get(i).packageName+"----"+listReturn.get(i).appName+"#index:"+listReturn.get(i).position);
//		}
		return listReturn;
	}
	public static List<AppInfo> getUserInstalledApps(Context context,
			boolean isSortByTime) {
		ArrayList<AppInfo> listReturn = new ArrayList<AppInfo>();
		PackageManager packageManager = context.getPackageManager();

		List<PackageInfo> packages = packageManager.getInstalledPackages(0);

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = packageManager.queryIntentActivities(
				mainIntent, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(
				packageManager));

		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			// if(packageInfo.packageName.equals(Configs.THIS_APP_PACKAGE_NAME))
			// continue;
			// 判断是否显示此应用 start
			boolean go = true;
			for (String pkg : Configs.getHiddenAppPkg()) {
				if (packageInfo.packageName.equals(pkg)) {
					go = false;
					break;
				}
				if (packageInfo.packageName.equals(Configs.THIS_APP_PACKAGE_NAME))
					go=false;
					break;
				
			}
			if (!go) continue;
			
			
			
			// 判断是否显示此应用 end
			AppInfo appInfo = new AppInfo();
			try {
				appInfo.appName = packageInfo.applicationInfo.loadLabel(
						packageManager).toString();
				appInfo.packageName = packageInfo.packageName;
				
				
				appInfo.versionName = packageInfo.versionName;
				appInfo.versionCode = packageInfo.versionCode;
				appInfo.appIcon = packageInfo.applicationInfo
						.loadIcon(packageManager);
				appInfo.firstInstallTime = packageInfo.firstInstallTime;
				// Logger.getLogger().i(appInfo.appName + "  " +
				// appInfo.firstInstallTime);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 系统应用
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0
					&& !Configs.showSystemApp()) {
				listReturn.add(appInfo);
			} else
				// 非系统应用
				listReturn.add(appInfo);
			//	listReturn.add(new AppInfo());
		}
		if (isSortByTime) {
			AppTimerComparator comparator = new AppTimerComparator();
			Collections.sort(listReturn, comparator);
		}
		for(int i=0;i<listReturn.size();i++){
		//	Log.d("thr",listReturn.get(i).packageName+"----"+listReturn.get(i).appName+"a");
		}
		return listReturn;
	}
	/**
	 * @param context
	 * @param containSysApp weather load system app
	 * @return load all app
	 */
	public static List<CustomAppInfo> loadApplications(Context context,
			boolean containSysApp) {

		PackageManager manager = context.getPackageManager();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

		List<CustomAppInfo> customAppInfoList = new ArrayList<CustomAppInfo>();
		if (apps != null) {

			for (int i = 0,len=apps.size(); i < len; i++) {
				CustomAppInfo customAppInfo = new CustomAppInfo();
				ResolveInfo info = apps.get(i);

				customAppInfo.title = info.loadLabel(manager);
				customAppInfo.setActivity(new ComponentName(
						info.activityInfo.applicationInfo.packageName,
						info.activityInfo.name), Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			
				String pkgName = info.activityInfo.packageName;
				String activityInfoName = info.activityInfo.name;

				customAppInfo.pkgName = pkgName;
				customAppInfo.activityInfoName = activityInfoName;

				boolean go = true;
				for (String pkg : Configs.getHiddenAppPkg()) {
					if (customAppInfo.pkgName.equals(pkg)) {
						go = false;
						break;
					}
				}
				if (!go)
					continue;

				if (customAppInfo.pkgName.equals(Configs.THIS_APP_PACKAGE_NAME))
					continue;

				PackageInfo packageInfo;
				try {
					packageInfo = manager.getPackageInfo(pkgName, 0);
					customAppInfo.firstInstallTime = packageInfo.firstInstallTime;
					// log.i(application.title + "  installed time " +
					// application.firstInstallTime);
					customAppInfo.lastUpdateTime = packageInfo.lastUpdateTime;
					boolean isSystemApp = false;
					if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
						// 第三方应用
					
					} else { // 系统应用 {
						//Log.d("app",pkgName+"---"+packageInfo.applicationInfo.loadLabel(manager).toString());
					
						isSystemApp = true;
						if (!containSysApp)
							continue;
					}
					//Log.d("app",pkgName+"---"+packageInfo.applicationInfo.loadLabel(manager).toString());
					customAppInfo.isSystemApp = isSystemApp;
					customAppInfo.versionName = packageInfo.versionName;
					customAppInfo.versionCode = packageInfo.versionCode;
					customAppInfo.icon = packageInfo.applicationInfo.loadIcon(manager);
					customAppInfo.title = packageInfo.applicationInfo.loadLabel(manager).toString();
					//for(int j=0;j<customAppInfoList.size();j++){
						//Log.d("3app",pkgName+"---"+customAppInfoList.get(j).pkgName);
						
				//	}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				
				customAppInfoList.add(customAppInfo);
			}

			AppTimerComparator2 comparator = new AppTimerComparator2();
			Collections.sort(customAppInfoList, comparator);
//			for(int i=0;i<customAppInfoList.size();i++){
//				Log.d("list",customAppInfoList.get(i).pkgName+"---"+customAppInfoList.get(i).title+"---"+customAppInfoList.get(i).activityInfoName);
//				
//			}
		}
		return customAppInfoList;
	}
	public static List<CustomAppInfo> loadAppManager(Context context,
			boolean containSysApp) {

		PackageManager manager = context.getPackageManager();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

		List<CustomAppInfo> customAppInfoList = new ArrayList<CustomAppInfo>();
		if (apps != null) {

			for (int i = 0,len=apps.size(); i < len; i++) {
				CustomAppInfo customAppInfo = new CustomAppInfo();
				ResolveInfo info = apps.get(i);

				customAppInfo.title = info.loadLabel(manager);
				customAppInfo.setActivity(new ComponentName(
						info.activityInfo.applicationInfo.packageName,
						info.activityInfo.name), Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			
				String pkgName = info.activityInfo.packageName;
				String activityInfoName = info.activityInfo.name;

				customAppInfo.pkgName = pkgName;
				customAppInfo.activityInfoName = activityInfoName;

				/*boolean go = true;
				for (String pkg : Configs.getHiddenAppPkg()) {
					if (customAppInfo.pkgName.equals(pkg)) {
						go = false;
						break;
					}
				}
				if (!go)
					continue;*/

				if (customAppInfo.pkgName.equals(Configs.THIS_APP_PACKAGE_NAME))
					continue;

				PackageInfo packageInfo;
				try {
					packageInfo = manager.getPackageInfo(pkgName, 0);
					customAppInfo.firstInstallTime = packageInfo.firstInstallTime;
					// log.i(application.title + "  installed time " +
					// application.firstInstallTime);
					customAppInfo.lastUpdateTime = packageInfo.lastUpdateTime;
					boolean isSystemApp = false;
					if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
						// 第三方应用
					
					} else { // 系统应用 {
						//Log.d("app",pkgName+"---"+packageInfo.applicationInfo.loadLabel(manager).toString());
					
						isSystemApp = true;
						if (!containSysApp){
							if(customAppInfo.pkgName.equals("com.google.android.gms")|| customAppInfo.pkgName.equals("com.google.android.youtube") ){
								
							}else{
								continue;	
							}
							
						}
							
					}
					//Log.d("app",pkgName+"---"+packageInfo.applicationInfo.loadLabel(manager).toString());
					customAppInfo.isSystemApp = isSystemApp;
					customAppInfo.versionName = packageInfo.versionName;
					customAppInfo.versionCode = packageInfo.versionCode;
					customAppInfo.icon = packageInfo.applicationInfo.loadIcon(manager);
					customAppInfo.title = packageInfo.applicationInfo.loadLabel(manager).toString();
					//for(int j=0;j<customAppInfoList.size();j++){
						//Log.d("3app",pkgName+"---"+customAppInfoList.get(j).pkgName);
						
				//	}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				
				customAppInfoList.add(customAppInfo);
			}

			AppTimerComparator2 comparator = new AppTimerComparator2();
			Collections.sort(customAppInfoList, comparator);
			for(int i=0;i<customAppInfoList.size();i++){
			//	Log.d("list",customAppInfoList.get(i).pkgName+"---APPNAME:"+customAppInfoList.get(i).title);
				
			}
			//Log.d("list",":"+customAppInfoList.size());
		}
		return customAppInfoList;
	}
	/*
	 * 对app进行排序 时间 / 应用名称 / 包名
	 */
	public static class AppTimerComparator implements Comparator<AppInfo> {
		@Override
		public int compare(AppInfo lhs, AppInfo rhs) {
			if (lhs.getFirstInstallTime() == 0) {
				return 1;
			}
			int flag = lhs.getFirstInstallTime().compareTo(
					rhs.getFirstInstallTime());
			if (flag == 0) {
				flag = lhs.getAppName().compareTo(rhs.getAppName());
				if (flag == 0) {
					flag = lhs.getPackageName().compareTo(rhs.getPackageName());
				}
			}
			return flag;
		}
	}
	
	
	public static class AppPos implements Comparator<AppInfo> {
		@Override
		public int compare(AppInfo lhs, AppInfo rhs) {
		
			int flag = new Long(lhs.getPosition()).compareTo((long)rhs.getPosition());
		    
			if (flag == 0) {
				flag = rhs.getFirstInstallTime().compareTo(lhs.getFirstInstallTime());
				if (flag == 0) {
					flag = lhs.getPackageName().compareTo(rhs.getPackageName());
				}
			}
			return flag;
		}
	}
	public static class AppTimerDesc implements Comparator<AppInfo> {
		@Override
		public int compare(AppInfo lhs, AppInfo rhs) {
			if (lhs.getFirstInstallTime() == 0) {
				return 1;
			}
			int flag = rhs.getFirstInstallTime().compareTo(
					lhs.getFirstInstallTime());
		
			if (flag == 0) {
				flag = lhs.getAppName().compareTo(rhs.getAppName());
				if (flag == 0) {
					flag = lhs.getPackageName().compareTo(rhs.getPackageName());
				}
			}
			return flag;
		}
	}
	/*
	 * 对app进行排序 时间 / 应用名称 / 包名
	 */
	public static class AppTimerComparator2 implements
			Comparator<CustomAppInfo> {
		@Override
		public int compare(CustomAppInfo lhs, CustomAppInfo rhs) {
		
			int flag = lhs.firstInstallTime.compareTo(rhs.firstInstallTime);
	
			return flag;
		}
	}

	public static class AppInfo {
		private Drawable appIcon;
		private String appName;
		private String packageName;
		private int versionCode;
		private String versionName;
		private boolean isInDesktop;
		private Long firstInstallTime;
		public int position=0;

		public Long getFirstInstallTime() {
			return firstInstallTime;
		}

		public void setFirstInstallTime(Long firstInstallTime) {
			this.firstInstallTime = firstInstallTime;
		}

		public boolean isInDesktop() {
			return isInDesktop;
		}

		public void setInDesktop(boolean isInDesktop) {
			this.isInDesktop = isInDesktop;
		}

		public Drawable getAppIcon() {
			return appIcon;
		}

		public void setAppIcon(Drawable appIcon) {
			this.appIcon = appIcon;
		}

		public String getAppName() {
			return appName;
		}

		public void setAppName(String appName) {
			this.appName = appName;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public int getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		@Override
		public String toString() {
			return "AppInfo [packageName=" + packageName + ", position="
					+ position + "]";
		}
		
	}
}
