package com.bestbaan.moonbox.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.bestbaan.moonbox.util.CustomAppInfo;
import com.bestbaan.moonbox.util.Logger;
import com.bestbaan.moonbox.util.AppUtils.AppInfo;
import com.moon.android.iptv.Configs;
import com.moon.android.iptv.LauncherApplication;
import com.moonX.util.AppUtil;

public class AppDAO {

	public static Logger logger = Logger.getInstance();
	private DBHelper mDBHelper;

	public AppDAO(Context context) {
		mDBHelper = new DBHelper(context);
	}

	public boolean insert(Context context, CustomAppInfo appInfo) {
		SQLiteDatabase db = null;
		try {
			db = mDBHelper.getReadableDatabase();
			ContentValues values = new ContentValues();
			
			values.put("pkgname", appInfo.pkgName);
			values.put("position", AppUtil.getCount(db));
			logger.i("insert app pkgname = " + appInfo.pkgName);

			long insertId = db.insert(DBHelper.TABLE_APP_IN_DESKTOP, null,values);
			logger.i("Add " + appInfo.title + " to " + DBHelper.TABLE_APP_IN_DESKTOP);
			if (-1 == insertId) {
				logger.i("Add " + appInfo.pkgName + " to " + DBHelper.TABLE_APP_IN_DESKTOP + " fail");
				return false;
			} else {
				logger.i("Add " + appInfo.pkgName + " to " + DBHelper.TABLE_APP_IN_DESKTOP + " true");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != db)
				db.close();
			if (null != mDBHelper)
				mDBHelper.close();
		}
		return false;
	}

	public void delete(CustomAppInfo appInfo) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		String sql = "delete from " + DBHelper.TABLE_APP_IN_DESKTOP + " where pkgname = '" + appInfo.pkgName + "'";
		db.execSQL(sql);
		changeAppInfoPosition();
		db.close();
		mDBHelper.close();
	}

	/**
	 * 删除数据表中间的一条记录index的时候，所有的position字段>index的都要重新赋值,赋值后,从表头到表尾的position序列为1,2,3,4,...
	 */
	private void changeAppInfoPosition() {
		List<AppInfo> appInfoList = getAppInfo(false);
		SQLiteDatabase db = null;
		try {
			db = mDBHelper.getReadableDatabase();
			for (int i=0,len=appInfoList.size();i<len;++i) {
				AppInfo appInfo=appInfoList.get(i);
				int position=appInfo.getPosition();
				if(position>LauncherApplication.position){
					db.execSQL("update "+DBHelper.TABLE_APP_IN_DESKTOP+" set position = "+(position-1)+" where pkgname = '"+appInfo.getPackageName()+"'");
				}
//				db.execSQL("update "+DBHelper.TABLE_APP_IN_DESKTOP+" set position = "+i+" where pkgname = '"+appInfo.getPackageName()+"'");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (null != db)
				db.close();
		}
	}

	public boolean canInsert(CustomAppInfo appInfo) {
		/**
		 * 一些需要隐藏的应用不让显示在桌面
		 */
		for (String pkg : Configs.getHiddenAppPkg()){
			logger.i("appInfo.pkgName="+appInfo.pkgName);
			if (appInfo.pkgName.equals(pkg))
				return false;
		}
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = mDBHelper.getReadableDatabase();
			cursor = db.query(DBHelper.TABLE_APP_IN_DESKTOP, null, " pkgname = '" + appInfo.pkgName + "'", null, null, null,null, null);
			if (cursor.getCount() <= 0 && AppUtil.getCount(db) < 48) {
				logger.i(appInfo.title + " can be send to desktop");
				return true;
			} else
				logger.i(appInfo.title + " cannt be send to desktop");
		} catch (Exception e) {
			logger.e(e.toString());
		} finally {
			if (null != cursor)
				cursor.close();
			if (null != db)
				db.close();
		}
		return false;
	}

	public boolean isExist(CustomAppInfo appInfo) {
		Cursor c = null;
		SQLiteDatabase db = null;
		try {
			db = mDBHelper.getReadableDatabase();
			c = db.query(DBHelper.TABLE_APP_IN_DESKTOP, null, " pkgname = '" + appInfo.pkgName + "'", null, null, null, null, null);

			if (c.getCount() > 0) {
				logger.i(appInfo.title + " is Exist"+ "    appInfo.count="+c.getCount());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != c)
				c.close();
			if (null != db)
				db.close();
		}
		return false;
	}

	/**
	 * @param pkgName
	 * @return AppInfo object,if have no this app,if will return null
	 */
	public AppInfo findAppInfo(String pkgName){
		Cursor cursor = null;
		SQLiteDatabase db = null;
		AppInfo appInfo=null;
		try {
			db = mDBHelper.getReadableDatabase();
			cursor = db.query(DBHelper.TABLE_APP_IN_DESKTOP, null, " pkgname = '" + pkgName + "'", null, null, null, null, null);

			while (cursor != null && cursor.moveToNext()) {
				appInfo = new AppInfo();
				String pkg = cursor.getString(cursor.getColumnIndex("pkgname"));
				int position = cursor.getInt(cursor.getColumnIndex("position"));

				appInfo.setPackageName(pkg);
				appInfo.setPosition(position);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != cursor)
				cursor.close();
			if (null != db)
				db.close();
		}
		return appInfo;
	}
	
	/**
	 * @return AppInfo List
	 */
	private List<AppInfo> getAppInfo() {
		List<AppInfo> appInfoList=new ArrayList<AppInfo>();
		// List<AppInfo> list = new ArrayList<AppInfo>();
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = mDBHelper.getReadableDatabase();
			cursor = db.query(DBHelper.TABLE_APP_IN_DESKTOP, null, null, null,
					null, null, null, null);
			while (cursor != null && cursor.moveToNext()) {
				AppInfo app = new AppInfo();
				String pkg = cursor.getString(cursor.getColumnIndex("pkgname"));
				int position = cursor.getInt(cursor.getColumnIndex("position"));

				app.setPackageName(pkg);
				app.setPosition(position);
				logger.i("桌面的PKG = " + pkg +"  position="+position);

				appInfoList.add(app);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != cursor)
				cursor.close();
			if (null != db)
				db.close();
		}
		return appInfoList;
	}
	
	/**
	 * @param sorted 是否根据position字段排序
	 * @return
	 */
	public List<AppInfo> getAppInfo(boolean sorted){
		List<AppInfo> appInfoList=this.getAppInfo();
		if(sorted==true){
			AppUtil.sortByPosition(appInfoList);
		}
		return appInfoList;
	}

	/**
	 * execute when desk app manager UI closing,it restore the position on db table by the value of memery
	 * @param appInfoList 
	 */
	public void updatePosition(List<AppInfo> appInfoList){
		SQLiteDatabase db = null;
		try {
			db = mDBHelper.getReadableDatabase();
			AppInfo appInfo=null;
			int position=0;
			for (int i=0,len=appInfoList.size();i<len;++i) {
				appInfo=appInfoList.get(i);
				position=appInfo.getPosition();
				db.execSQL("update "+DBHelper.TABLE_APP_IN_DESKTOP+" set position = "+position+" where pkgname = '"+appInfo.getPackageName()+"'");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (null != db)
				db.close();
		}
	}
}
