package com.bestbaan.moonbox.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bestbaan.moonbox.util.AppUtils;
import com.bestbaan.moonbox.util.CustomAppInfo;
import com.bestbaan.moonbox.util.Logger;
import com.bestbaan.moonbox.util.AppUtils.AppInfo;
import com.moon.android.iptv.Configs;
import com.moon.android.iptv.Constant;
import com.moonbox.android.iptv.R;

@SuppressLint("NewApi")
public class AppGrid extends LinearLayout implements OnKeyListener{
	
	private Logger logger=Logger.getInstance();
	
	private LinearLayout mArrowLeft;
	private LinearLayout mArrowRight;
	private GridView mGridApps;
	private Context mContext;
	private int mColumnNum = 6;
	private int mRows = 3;
	private int mCurrentPage = 1;
	private int mAppNumPerPage;
	private int mTotalPage;
	private List<CustomAppInfo> mListAppInfo;
	private PageIndicatorView mPageView;
	public static final String PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";
	public static final String  PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";
	public AppGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		View view = LayoutInflater.from(context).inflate(R.layout.apps_view, this);
		mArrowLeft = (LinearLayout) view.findViewById(R.id.arrow_left);
		mArrowRight = (LinearLayout) view.findViewById(R.id.arrow_right);
		mGridApps = (GridView) view.findViewById(R.id.page_app_gridview);
		mPageView = (PageIndicatorView) view.findViewById(R.id.ad_page_num);
		mGridApps.setOnItemClickListener(mGridItemClickListener);
		mGridApps.setOnItemSelectedListener(mSelectListener);
		mArrowLeft.setOnClickListener(mArrowClickListener);
		mArrowRight.setOnClickListener(mArrowClickListener);
		mGridApps.setOnKeyListener(this);
		mGridApps.setFocusable(true);
		
		getApps();
		setAdapter(mListAppInfo);
		regApkOp();
	}
	
	private void getApps() {
		mListAppInfo = new ArrayList<CustomAppInfo>();
	
			mListAppInfo.addAll(Constant.getSysApp(mContext));
//		List<CustomAppInfo> userInstall  = AppUtils.loadApplications(mContext,Configs.showSystemApp());
		List<CustomAppInfo> userInstall  = AppUtils.loadApplications(mContext,true);
		mListAppInfo.addAll(userInstall);
	}
	
	public AppGrid(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public AppGrid(Context context) {
		this(context,null);
	}
	
	
	private void regApkOp() {
	    IntentFilter filter = new IntentFilter();
	    filter.addAction("android.intent.action.PACKAGE_ADDED"); 
	    filter.addAction("android.intent.action.PACKAGE_REMOVED");  
	    filter.addDataScheme("package");  
	    mContext.registerReceiver(appOperateReceiver, filter);
	}

	public void unRegApkOp(){
		if(null != appOperateReceiver){
			mContext.unregisterReceiver(appOperateReceiver);
		}
	}
	
	private BroadcastReceiver appOperateReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			mListAppInfo = new ArrayList<CustomAppInfo>();
			if(Configs.showSystemApp())
				mListAppInfo.addAll(Constant.getSysApp(mContext));
			List<CustomAppInfo> userInstall  = AppUtils.loadApplications(mContext,Configs.showSystemApp());
			mListAppInfo.addAll(userInstall);
			setAdapter(mListAppInfo);
//			List<CustomAppInfo> list = Constant.getSysApp(mContext);
//			list.addAll(AppUtils.loadApplications(getContext(), Configs.isShowSystemApp));
//			setAdapter(AppUtils.loadApplications(getContext(), Configs.isShowSystemApp));
		}
	};
	
	
	private OnItemSelectedListener mSelectListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			if(null != view)
				view.setSelected(true);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
	
	private OnClickListener mArrowClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == mArrowLeft) {
				if(1 == mCurrentPage) new CustomToast(mContext, getResources().getString(R.string.has_to_first), Configs.TOAST_TEXT_SIZE).show();
				else turnPage(true);
			} else if (v == mArrowRight) {
				if(mTotalPage == mCurrentPage)new CustomToast(mContext, getResources().getString(R.string.has_to_last), Configs.TOAST_TEXT_SIZE).show();
				else turnPage(false);
			}
		}
	};
	
	private OnItemClickListener mGridItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			int posInAllApps = getClickPosition(position);
			CustomAppInfo appInfo = mListAppInfo.get(posInAllApps);
			logger.i("app clicked : "+appInfo);
			mContext.startActivity(appInfo.intent);
		}
	};
	
	private int getClickPosition(int position) {
		return (mCurrentPage - 1) * mAppNumPerPage + position;
	}
	
	public void setAdapter(List<CustomAppInfo> list){
		mGridApps.setNumColumns(mColumnNum);
		mListAppInfo = list;
		mTotalPage = caculaterPages(list);
		mAppNumPerPage = mColumnNum * mRows;
		if(mCurrentPage == 0) 
			mCurrentPage = 1;
		if(mCurrentPage >= mTotalPage){
			mCurrentPage = mTotalPage;
		}
		if(list.size() > 0){
			fillGridView(mCurrentPage);
		}
	}
	
	private void fillGridView(int currentPage) {
		List<CustomAppInfo> listAppInfo = new ArrayList<CustomAppInfo>();
		for (int i = (currentPage - 1) * mAppNumPerPage; i < currentPage
				* mAppNumPerPage; i++) {
			try {
				listAppInfo.add(mListAppInfo.get(i));
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		if (0 != listAppInfo.size()) {
			AppsAdapter appsAdapter = new AppsAdapter(mContext, listAppInfo);
			mGridApps.setAdapter(appsAdapter);
			mPageView.setTotalPage(mTotalPage);
			mPageView.setCurrentPage(mCurrentPage-1);
			mPageView.invalidate();
		}
	}
	
	private int caculaterPages(List<CustomAppInfo> list){
		int appSize = list.size();
		int mAppsNum = mColumnNum * mRows;
		if (0 == appSize % mAppsNum)
			return appSize / mAppsNum;
		else
			return appSize / mAppsNum + 1;
	}
	
	public void setNumColumns(int columns){
		mColumnNum = columns;
		mGridApps.setNumColumns(columns);
	}
	
	public void setNumRows(int rows){
		mRows = rows;
	}
	
	public void setArrowVisibility(int visible){
		mArrowLeft.setVisibility(visible);
		mArrowRight.setVisibility(visible);
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (v == mGridApps && event.getAction() == KeyEvent.ACTION_DOWN) {
			int gridSelection = mGridApps.getSelectedItemPosition();
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_RIGHT: 
				if (gridSelection + (mAppNumPerPage * (mCurrentPage-1)) + 1 == mListAppInfo.size()){
					return true;
					//翻到下一页
//					return false;
					}
				
				if ((gridSelection + 1) % mColumnNum == 0) {
					if(mCurrentPage == mTotalPage) 
						return true;
					//翻到下一页
//						return false;
					turnPage(false);
					int nextSelection = gridSelection - (mColumnNum - 1);
					mGridApps.setSelection(nextSelection);
					return true;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if (gridSelection %  mColumnNum == 0) {
					if (mCurrentPage == 0 || mCurrentPage == 1){
						return true;
//						return false;
					}
					else{
						turnPage(true);
						int nextSelection = gridSelection + (mColumnNum - 1);
						mGridApps.setSelection(nextSelection);
						return true;
					}
				}
				break;
			default:
				break;
			}
		}
		return false;
	}

	private void turnPage(boolean direction){
		if(direction){
			mCurrentPage--;
		} else {
			mCurrentPage++;
		}
		fillGridView(mCurrentPage);
	}
	
	public static class AppsAdapter extends BaseAdapter{
		private List<CustomAppInfo> mListApps;
		private LayoutInflater mInflater;
		public AppsAdapter(Context context,List<CustomAppInfo> list){
			mListApps = list;
			mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return mListApps.size();
		}

		@Override
		public Object getItem(int position) {
			return mListApps.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			Holder holder = null;
			if(null == convertView){
				holder = new Holder();
				convertView = mInflater.inflate(R.layout.app_item, null);
				holder.appIcon = (ImageView) convertView.findViewById(R.id.app_item_imageview);
				holder.appName = (TextView) convertView.findViewById(R.id.app_item_name);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			CustomAppInfo appInfo = mListApps.get(position);
			holder.appIcon.setImageDrawable(appInfo.icon);
			holder.appName.setText(appInfo.title);
//			convertView.setBackgroundResource(R.drawable.bg_app);
			return convertView;
		}
		
		class Holder {
			ImageView appIcon;
			TextView appName;
		}
	}
	
	
	
	public static class AppsAdapter2 extends BaseAdapter{
		
		Animation shake = null;
		boolean canShake=false;

		private Logger logger=Logger.getInstance();
		private List<AppInfo> mListApps;
		private LayoutInflater mInflater;
		public AppsAdapter2(Context context,List<AppInfo> list){
			shake = AnimationUtils.loadAnimation(context, R.anim.anim_shake);
			mListApps = list;
			mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return mListApps.size();
		}

		@Override
		public Object getItem(int position) {
			return mListApps.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			Holder holder = null;
			if(null == convertView){
				holder = new Holder();
				convertView = mInflater.inflate(R.layout.app_item, null);
				holder.appIcon = (ImageView) convertView.findViewById(R.id.app_item_imageview);
				holder.appName = (TextView) convertView.findViewById(R.id.app_item_name);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			AppInfo appInfo = mListApps.get(position);
			holder.appIcon.setImageDrawable(appInfo.getAppIcon());
			holder.appName.setText(appInfo.getAppName());
			
			/*logger.i(currentLongClickItem+"  "+position+"  "+canShake);
			if(currentLongClickItem==position){
				if(canShake==true){
					shake.reset();
					shake.setFillAfter(true);
					holder.appIcon.startAnimation(shake);
				}
				else{
					holder.appIcon.clearAnimation();
				}
			}else{
				holder.appIcon.clearAnimation();
			}*/
			
			return convertView;
		}
		
		static class Holder {
			ImageView appIcon;
			TextView appName;
		}

		/**
		 * @param position 位置
		 * @param canShake 能否抖动
		 * 当长按Item的时候，canShake=true,那个Item就
		 * 会抖动起来,当返回键按下时canShake=false,就会
		 * 停止抖动
		 *//*
		public void notifyDataSetChanged(int position,boolean canShake) {
			currentLongClickItem=position;
			this.canShake=canShake;
			super.notifyDataSetChanged();
		}*/
		
	}
	
	
}
