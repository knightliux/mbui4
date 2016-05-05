package com.bestbaan.moonbox.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CountryDAO {

	public static final String TAG = "CountryDAO";
	private DBHelper mDBHelper;
	private Context mContext;

	public CountryDAO(Context context) {
		mContext = context;
		mDBHelper = new DBHelper(mContext);
	}
	
	public boolean isFirstLoad(){
		Cursor c = null;
		SQLiteDatabase db = null;
		try{
			db = mDBHelper.getReadableDatabase();
			c = db.query(DBHelper.TABLE_COUNTRY_SET, null, null, null, null,
					null, null, null);
			String loadTAG = null;
			while (c != null && c.moveToNext()) {
				loadTAG = c.getString(c.getColumnIndex("isfirst"));
				break;
			}
			if(null != loadTAG && !"0".equals(loadTAG))
				return false;
			else return true;
		} catch (Exception e) {
		}finally{
			if(null != c) c.close();
			if(null != db) db.close();
		}
		return false;
	}
	
	public void changeLoadStatus(){
		String sql = "update " + DBHelper.TABLE_COUNTRY_SET +" set isfirst = 1 where id = 1";
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		db.execSQL(sql);
	}
}
