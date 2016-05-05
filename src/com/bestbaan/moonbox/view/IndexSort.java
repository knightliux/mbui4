package com.bestbaan.moonbox.view;

import java.util.ArrayList;
import java.util.List;

import com.bestbaan.moonbox.adapter.IndexAppAdapter;
import com.bestbaan.moonbox.util.ActivityUtils;
import com.bestbaan.moonbox.util.AppUtils;
import com.bestbaan.moonbox.util.RequestUtil;
import com.bestbaan.moonbox.util.AppUtils.AppInfo;
import com.moonbox.android.iptv.R;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout.LayoutParams;

public class IndexSort extends LinearLayout {
	private Context mContext;
	private PopupWindow mPopup;
	private View mView;
	private GridView mGridApp;
	private IndexAppAdapter mAppsAdapter;
	private List<AppInfo> mListAppInfo = new ArrayList<AppInfo>();
	private static IndexSort indexSort=null;
	private static Handler mhandler;
	private boolean islongclick=false;
	private IndexSort(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		
		// TODO Auto-generated constructor stub
	}

	private IndexSort(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	private IndexSort(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
    public static IndexSort getIndexSort(Context context,Handler handler){
    	if (indexSort == null){
    		indexSort = new IndexSort(context);
    	}
    	mhandler=handler;	
		return indexSort;
		
    	
    }
	public void init() {
	
		// TODO Auto-generated method stub
		mView = LayoutInflater.from(mContext)
				.inflate(R.layout.index_sort, this);
		mPopup = new PopupWindow(mView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		mPopup.showAtLocation(mView, Gravity.CENTER, 0, 0);
		mGridApp = (GridView) mView.findViewById(R.id.index_grid_sotr);
		mGridApp.setNumColumns(10);
		mGridApp.setOnItemSelectedListener(mGirdselectListener);
		mGridApp.setOnKeyListener(mgridkey);
		mGridApp.setOnItemLongClickListener(mGridItemLongClickListener);
		mGridApp.setOnItemClickListener(mGridItemClickListener);
		updateGridView();

	}
	private OnItemSelectedListener mGirdselectListener=new OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Log.d("select",arg2+"");
			if(!islongclick){
				mAppsAdapter.notifyDataSetChanged(arg2);
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	private OnItemClickListener mGridItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			    if(islongclick){
			    	mAppsAdapter.notifyDataClick(position);
			    	islongclick=false;
			    	mAppsAdapter.notifyDataSetChanged(position);
			    }else{
			    	islongclick=true;
					mAppsAdapter.notifyDataSetChanged(position, true);
			    }
				
		}
	};
	private OnItemLongClickListener mGridItemLongClickListener = new OnItemLongClickListener() {// GridView的Item长按事件

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			//IsLongClick=true;   
		
		
//			DeskAppMgr deskAppMgr=new DeskAppMgr(mContext);
//			deskAppMgr.setData(mAppsAdapter,mListAppInfo);
//			deskAppMgr.show();
			return true;
		}
	};
	private OnKeyListener mgridkey = new OnKeyListener() {

		@Override
		public boolean onKey(View arg0, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (KeyEvent.ACTION_UP == event.getAction()) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					
					mhandler.sendEmptyMessage(9999);
					mPopup.dismiss();
					break;
				}
			}
			return false;
		}

	};

	private void updateGridView() {

		List<AppInfo> apps = AppUtils.getUserApp(mContext, true);
		mListAppInfo.clear();
		mListAppInfo.addAll(apps);
	
		mAppsAdapter = new IndexAppAdapter(mContext, mListAppInfo,R.layout.app_item_sort);
		mGridApp.setAdapter(mAppsAdapter);
		mGridApp.setVisibility(View.VISIBLE);

	}
}
