package com.bestbaan.moonbox.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "vodDatabase";
	public static final String TABLE_COUNTRY_SET = "country_set";
	public static final String TABLE_APP_IN_DESKTOP = "desktop_apps";
	public static final String TABLE_USER_MSG = "user_msg";
	public static final int VERSION = 2;
	public static final String TAG = "DBHelper";
	public static final String TABLE_LIMIT = "table_region_limit";

	private Context mContext;
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		mContext=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String countrySetSQL = "create table if not exists "
				+ TABLE_COUNTRY_SET
				+ "(id integer primary key autoincrement,isfirst varchar(100))";

		String appSQL = "create table if not exists "
				+ TABLE_APP_IN_DESKTOP
				+ "(id integer primary key autoincrement,appname varchar(100),pkgname varchar(500),icon BLOB,position varchar(3))";

		String userMsgSql = "create table if not exists "
				+ TABLE_USER_MSG
				+ "(iid integer primary key autoincrement,id varchar(100),status varchar(10),msgid varchar(100),title varchar(200),body varchar(500),time varchar(100))";

		String initCountry = "insert into " + TABLE_COUNTRY_SET
				+ "(id,isfirst) values(1,'0')";

		db.execSQL(countrySetSQL);
		db.execSQL(appSQL);
		db.execSQL(userMsgSql);

		db.execSQL(initCountry);

		// region limit table
		String regionCreate = "create table if not exists "
				+ TABLE_LIMIT
				+ "(id integer primary key autoincrement,isauth varchar(100),msg varchar(100))";
		String initRegionLimit = "insert into " + TABLE_LIMIT
				+ "(id,isauth,msg) values(1,'0','')";

		db.execSQL(regionCreate);
		db.execSQL(initRegionLimit);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (db.getVersion() == 1) {// version before update
			db.execSQL("delete from " + TABLE_APP_IN_DESKTOP);//第一条的作用是清空表数据
			
//			db.execSQL("select * from sqlite_sequence");//第二条的作用是查询当前的id值
//			db.execSQL("update sqlite_sequence set seq=0 where name=" + TABLE_APP_IN_DESKTOP);
			
			db.execSQL("alter table " + TABLE_APP_IN_DESKTOP + " add column position");
		}

	}

}
