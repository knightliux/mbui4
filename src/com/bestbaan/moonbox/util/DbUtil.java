package com.bestbaan.moonbox.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbUtil extends SQLiteOpenHelper {
	public SQLiteDatabase db;
	public boolean debug = false;
	private static final String name = "MBUI4"; // 数据库名称
	private static final int version = 1; // 数据库版本


	public DbUtil(Context context) {
		super(context, name, null, version);

		// TODO Auto-generated constructor stub
	}

	public SQLiteDatabase getDb() {
		return getReadableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL("CREATE TABLE IF NOT EXISTS index_app_info (id integer primary key autoincrement,pkgname varchar(255) UNIQUE,orderid integer(11))");
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
                
	}
	public void delAppindex(String pkgname){
		try {
			db=getReadableDatabase();
			db.execSQL("DELETE FROM index_app_info where pkgname=?",new String[]{pkgname});
			db.close();
	
    	}catch (Exception e) {
			// TODO: handle exception
		}
	}
    public void SaveAppIndex(String pkgname,int index){
    	try {
			db=getReadableDatabase();
			db.execSQL("REPLACE INTO index_app_info(pkgname,orderid) values(?,?)",new Object[]{pkgname,index});
			db.close();
	
    	}catch (Exception e) {
			// TODO: handle exception
		}
    }
    public int GetAppIndex(String pkgname){
    	try {
			db=getReadableDatabase();
			Cursor Row =db.rawQuery("Select * from index_app_info where pkgname=?",new String[]{pkgname});
			if(Row.moveToFirst()){
				String index=Row.getString(2);
				
				if(debug){
					Log.d("Row0","Row0:"+Row.getString(0));
					Log.d("Row1","Row1:"+Row.getString(1));
					Log.d("Row12","Row2:"+Row.getString(2));
				}
				return Integer.parseInt(index);
			}
			db.close();
		
    	}catch (Exception e) {
			// TODO: handle exception
    		return -1;
		}
    	return -1;
    }



}
