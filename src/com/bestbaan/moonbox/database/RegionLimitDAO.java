package com.bestbaan.moonbox.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bestbaan.moonbox.model.Regionlimit;

public class RegionLimitDAO {

	public static final String TAG = "RegionLimitDAO";
	private DBHelper mDBHelper;
	private Context mContext;

	public RegionLimitDAO(Context context) {
		mContext = context;
		mDBHelper = new DBHelper(mContext);
	}
	
	public void changeLoadStatus(String status, String msg){
		String sql = "update " + DBHelper.TABLE_LIMIT +" set isauth = '"+status+"', msg ='" + msg+"' where id = 1";
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		db.execSQL(sql);
	}
	
	public Regionlimit getRegionLimit(){
		Cursor c = null;
		SQLiteDatabase db = null;
		Regionlimit regionLimit = new Regionlimit();
		try {
			db = mDBHelper.getReadableDatabase();
			c = db.query(DBHelper.TABLE_LIMIT, null, null, null, null,
					null, null, null);
			while (c != null && c.moveToNext()) {
				String limitStatus = c.getString(c.getColumnIndex("isauth"));
				String msg = c.getString(c.getColumnIndex("msg"));
				regionLimit.setCode(limitStatus);
				regionLimit.setMsg(msg);
				return regionLimit;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != c) c.close();
			if(null != db) db.close();
		}
		return null;
	}
}
