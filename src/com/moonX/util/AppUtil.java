package com.moonX.util;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.GridView;

import com.bestbaan.moonbox.adapter.DeskAppMgrAdapter;
import com.bestbaan.moonbox.database.DBHelper;
import com.bestbaan.moonbox.util.Logger;
import com.bestbaan.moonbox.util.AppUtils.AppInfo;
import com.bestbaan.moonbox.view.AppGrid.AppsAdapter2;


public class AppUtil {

	private static final int COLUMN=8;
	/**
	 * @param appInfoList 数据库查询的AppInfo列表
	 * @return 对AppInfo列表进行按position字段重排序
	 */
	public static List<AppInfo> sortByPosition(List<AppInfo> appInfoList) {
		final Logger logger=Logger.getInstance();
		logger.i("排序前:"+appInfoList.toString());
		Collections.sort(appInfoList, new Comparator<AppInfo>() {
			
			@Override
			public int compare(AppInfo appL, AppInfo appR) {
				
				int result=0;
				
				int pos1=appL.getPosition();
				int pos2=appR.getPosition();
				
				if(pos1>pos2) result=1;
				else result=-1;
				return result;
			}
		});
		logger.i("排序后:"+appInfoList.toString());
		return appInfoList;
	}
	
	/**
	 * @param db 
	 * @return desktop_apps数据表的记录数量
	 */
	public static int getCount(SQLiteDatabase db){
		Cursor cursor = db.query(DBHelper.TABLE_APP_IN_DESKTOP, null, null, null, null, null, null, null);
		return cursor.getCount();
	}
	
	/**
	 * @param position desk position of app when long clicked
	 * @return judge this app can move to previous,if true,this app can move to previous 1 step
	 */
	public static boolean canMovePre(int position){
		return position>0?true:false;
	}
	
	/**
	 * @param total total number of desk'app
	 * @param position desk position of app when long clicked
	 * @return judge this app can move to next,if true,this app can move to next 1 step
	 */
	public static boolean canMoveNext(int total,int position){
		return position<total-1?true:false;
	}
	
	/**
	 * @param position desk position of app when long clicked
	 * @return judge this app can move up,if true,this app can move to previous 4 step
	 */
	public static boolean canMoveUp(int position){
		return position-COLUMN>=0?true:false;
	}
	
	/**
	 * @param total total number of desk'app
	 * @param position desk position of app when long clicked
	 * @return judge this app can move down,if true,this app can move to next 4 step
	 */
	public static boolean canMoveDown(int total,int position){
		return position+COLUMN<total?true:false;
	}
	
	/**
	 * change position in desk app where current and previous
	 * @param position current long clicked position of app in desk
	 */
	public static void movePre(GridView gridView, List<AppInfo> appInfoList,DeskAppMgrAdapter adapter,int position){
		appInfoList.get(position).setPosition(position-1);
		appInfoList.get(position-1).setPosition(position);
		sortByPosition(appInfoList);
		adapter.notifyDataSetChanged(position-1, true);
		gridView.setSelection(position-1);
	}
	
	/**
	 * change position in desk app where current and next
	 * @param position current long clicked position of app in desk
	 */
	public static void moveNext(GridView gridView, List<AppInfo> appInfoList,DeskAppMgrAdapter adapter,int position){
		appInfoList.get(position).setPosition(position+1);
		appInfoList.get(position+1).setPosition(position);
		sortByPosition(appInfoList);
		adapter.notifyDataSetChanged(position+1, true);
		gridView.setSelection(position+1);
	}
	
	/**
	 * change position in desk app where current and up
	 * @param position current long clicked position of app in desk
	 */
	public static void moveUp(GridView gridView, List<AppInfo> appInfoList,DeskAppMgrAdapter adapter,int position){
		appInfoList.get(position).setPosition(position-COLUMN);
		appInfoList.get(position-COLUMN).setPosition(position);
		sortByPosition(appInfoList);
		adapter.notifyDataSetChanged(position-COLUMN, true);   
		gridView.setSelection(position-COLUMN);
	}
	
	/**
	 * change position in desk app where current and down
	 * @param position current long clicked position of app in desk
	 */
	public static void moveDown(GridView gridView, List<AppInfo> appInfoList,DeskAppMgrAdapter adapter,int position){
		appInfoList.get(position).setPosition(position+COLUMN);
		appInfoList.get(position+COLUMN).setPosition(position);
		sortByPosition(appInfoList);
		adapter.notifyDataSetChanged(position+COLUMN, true);
		gridView.setSelection(position+COLUMN);
	}
	
}
















