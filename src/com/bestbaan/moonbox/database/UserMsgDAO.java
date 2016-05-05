package com.bestbaan.moonbox.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bestbaan.moonbox.model.UserMsg;
import com.bestbaan.moonbox.util.Logger;
import com.moon.android.iptv.Configs;

public class UserMsgDAO{
	
	private static Logger logger = Logger.getInstance();
	private DBHelper mDBHelper;
	private Context mContext;
	
	public UserMsgDAO(Context context){
		mContext = context;
	}
	
	
	public List<UserMsg> queryAll(){
		Cursor c = null;
		SQLiteDatabase database = null;
		List<UserMsg> list = new ArrayList<UserMsg>();
		try{
			mDBHelper = new DBHelper(mContext);
			database = mDBHelper.getReadableDatabase();
			c = database.query(DBHelper.TABLE_USER_MSG, null, null, null, null, null, "iid desc", null);
			while (c != null && c.moveToNext()) {
				UserMsg userMsg = new UserMsg();
				userMsg.setId(c.getString(c.getColumnIndex("id")));
				userMsg.setIid(c.getString(c.getColumnIndex("iid")));
				userMsg.setBody(c.getString(c.getColumnIndex("body")));
				userMsg.setStatus(c.getString(c.getColumnIndex("status")));
				userMsg.setTime(c.getString(c.getColumnIndex("time")));
				userMsg.setTitle(c.getString(c.getColumnIndex("title")));
				
				logger.i("iid = " + c.getString(c.getColumnIndex("iid")) + " id = " + c.getString(c.getColumnIndex("id")));
				list.add(userMsg);
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			if(null != database) database.close();
			if(null != c) c.close();
		}
		return list;
	}
	
	public boolean hasNoReadMsg(){
		Cursor c = null;
		SQLiteDatabase database = null;
		try{
			mDBHelper = new DBHelper(mContext);
			database = mDBHelper.getReadableDatabase();
			c = database.query(DBHelper.TABLE_USER_MSG, null, "status="+Configs.UserMsgVar.MSG_NOT_READ, null, null, null, null, null);
			while (c != null && c.getCount() > 0) {
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			if(null != database) database.close();
			if(null != c) c.close();
		}
		return false;
	}
	
	public String getMaxId(){
		Cursor c = null;
		SQLiteDatabase database = null;
		String maxMsgId = null;
		try{
			mDBHelper = new DBHelper(mContext);
			database = mDBHelper.getReadableDatabase();
			c = database.query(DBHelper.TABLE_USER_MSG, null, null, null, null, null, "id desc", null);
			while (c != null && c.moveToNext()) {
				maxMsgId = c.getString(c.getColumnIndex("id"));
				break;
			}
			return maxMsgId;
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			if(null != database) database.close();
			if(null != c) c.close();
		}
		return null;
	}
	
	public void changeMsgStatus(UserMsg msg){
		String sql = "update " + DBHelper.TABLE_USER_MSG +" set status = "+msg.getStatus()+" where iid = " + msg.getIid();
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		db.execSQL(sql);
		if(null != db) db.close();
	}
	
	
	public void insert(UserMsg msg){
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			mDBHelper = new DBHelper(mContext);
			db = mDBHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("id", msg.getId());
			values.put("title", msg.getTitle());
			values.put("body", msg.getBody());
			values.put("time", msg.getTime());
			values.put("status", 1);
			db.insert(DBHelper.TABLE_USER_MSG,null,values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(null != db) db.close();
			if(null != cursor) cursor.close();
		}
	}
	
	public boolean checkIsNull(){
		Cursor c = null;
		SQLiteDatabase database = null;
		try{
			mDBHelper = new DBHelper(mContext);
			database = mDBHelper.getReadableDatabase();
			c = database.query(DBHelper.TABLE_USER_MSG, null, null, null, null, null, null, null);
			if(c.getCount() > 0) return false;
			else return true;
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			if(null != database) database.close();
			if(null != c) c.close();
		}
		return false;
	}

}
