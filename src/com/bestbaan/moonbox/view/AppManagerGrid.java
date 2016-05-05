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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bestbaan.moonbox.adapter.AppAdminAdapter;
import com.bestbaan.moonbox.database.AppDAO;
import com.bestbaan.moonbox.util.AppUtils;
import com.bestbaan.moonbox.util.CustomAppInfo;
import com.bestbaan.moonbox.util.Logger;
import com.moon.android.iptv.Configs;
import com.moon.android.iptv.Constant;
import com.moon.android.iptv.LauncherApplication;
import com.moonbox.android.iptv.R;

@SuppressLint("NewApi")
public class AppManagerGrid extends LinearLayout implements OnKeyListener {

	private Logger logger = Logger.getInstance();
	private LinearLayout mArrowLeft;
	private LinearLayout mArrowRight;
	private GridView mGridApps;
	private Context mContext;
	private int mColumnNum = 3;
	private int mRows = 4;
	private int mCurrentPage = 1;
	private int mAppNumPerPage;
	private int mTotalPage = 1;
	private AppDAO mAppDAOI;
	private List<CustomAppInfo> mListAppInfo;
	private PageIndicatorView mPageView;
	private boolean isDoOperationInCurrentPage = false;
	public static final String PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";
	public static final String PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";
	public static final int ACTION_UPDATE_DESKTOP = 0x1000002;

	private AppAdminAdapter mAppsAdapter;

	public AppManagerGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		View view = LayoutInflater.from(context).inflate(
				R.layout.apps_manage_view, this);
		mArrowLeft = (LinearLayout) view.findViewById(R.id.arrow_left);
		mArrowRight = (LinearLayout) view.findViewById(R.id.arrow_right);
		mGridApps = (GridView) view.findViewById(R.id.page_app_gridview);
		mPageView = (PageIndicatorView) view.findViewById(R.id.ad_page_num);
		mArrowLeft.setOnClickListener(mArrowClickListener);
		mArrowRight.setOnClickListener(mArrowClickListener);
		mGridApps.setOnKeyListener(this);
		mGridApps.setOnItemClickListener(mOnGridItemClickListener);
		mAppDAOI = new AppDAO(context);

		
//		setAdapter(AppUtils.loadApplications(context, Configs.showSystemApp()));
		mListAppInfo=AppUtils.loadAppManager(context, false);
		setAdapter(mListAppInfo);
		regApkOp();
	}

	private void regApkOp() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(PACKAGE_ADDED);
		filter.addAction(PACKAGE_REMOVED);
		filter.addDataScheme("package");
		mContext.registerReceiver(mAppOperateReceiver, filter);
	}

	public void unRegApkOp() {
		if (null != mAppOperateReceiver) {
			mContext.unregisterReceiver(mAppOperateReceiver);
		}
	}

	private BroadcastReceiver mAppOperateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
//			List<CustomAppInfo> list = AppUtils.loadApplications(mContext,Configs.showSystemApp());
			List<CustomAppInfo> list = AppUtils.loadApplications(mContext,false);
			String packageName = intent.getData().getSchemeSpecificPart();
			CustomAppInfo customAppInfo = new CustomAppInfo();
			if (PACKAGE_ADDED.equals(intent.getAction())) {
				customAppInfo.pkgName = packageName;
				logger.i("获取到安装应用的广播   pkg = " + customAppInfo.pkgName);
				AppDAO appDAO = new AppDAO(mContext);
				if (appDAO.canInsert(customAppInfo)) {
					logger.i("桌面未满，可以添加到桌面");
					appDAO.insert(mContext, customAppInfo);
					Intent intentToDeskTop = new Intent(Configs.BroadCastConstant.ACTION_TO_DESKTOP);
					mContext.sendBroadcast(intentToDeskTop);
				}
			}
			setAdapter(list);
			if (PACKAGE_REMOVED.equals(intent.getAction())) {
				customAppInfo.pkgName = packageName;
				AppDAO appDAO = new AppDAO(mContext);
				appDAO.delete(customAppInfo);
				Intent intentDesk = new Intent(Configs.BroadCastConstant.ACTION_UPDATE_DESKTOP);
				mContext.sendBroadcast(intentDesk);
			}
		}
	};

	private OnClickListener mArrowClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == mArrowLeft) {
				if (1 == mCurrentPage)
					new CustomToast(mContext, getResources().getString(
							R.string.has_to_first), Configs.TOAST_TEXT_SIZE)
							.show();
				else
					turnPage(true);
			} else if (v == mArrowRight) {
				if (mTotalPage == mCurrentPage)
					new CustomToast(mContext, getResources().getString(
							R.string.has_to_last), Configs.TOAST_TEXT_SIZE)
							.show();
				else
					turnPage(false);
			}
		}
	};

	private OnItemClickListener mOnGridItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			logger.i("mCurrentPage="+mCurrentPage+"  select app posotion="+(position+mCurrentPage*(mRows*mColumnNum)));
			CustomAppInfo appInfo = mListAppInfo.get(position+(mCurrentPage-1)*(mRows*mColumnNum));
			logger.i("onItemClick() appInfo : "+appInfo);
			boolean toDesk = !appInfo.isDesktop;
			appInfo.isDesktop = toDesk;
			mAppsAdapter.notifyDataSetChanged(position, toDesk);
			
			toDeskTop(appInfo);
		}
	};

	public AppManagerGrid(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AppManagerGrid(Context context) {
		this(context, null);
	}

	public void setAdapter(List<CustomAppInfo> customAppInfoList) {
		List<CustomAppInfo> appInfoDesktopList = new ArrayList<CustomAppInfo>();//this 2 lines for sorting,desked app in head,not decked in tail
		List<CustomAppInfo> appInfoNotDesktopList = new ArrayList<CustomAppInfo>();
		for (CustomAppInfo customAppInfo : customAppInfoList) {
			if (mAppDAOI.isExist(customAppInfo)) {
				customAppInfo.isDesktop = true;
				appInfoDesktopList.add(customAppInfo);
			} else{
				appInfoNotDesktopList.add(customAppInfo);
			}
		}
		appInfoDesktopList.addAll(appInfoNotDesktopList);
		mGridApps.setNumColumns(mColumnNum);
		mListAppInfo = appInfoDesktopList;
		mTotalPage = caculaterPages(appInfoDesktopList);
		mAppNumPerPage = mColumnNum * mRows;
		if (mCurrentPage == 0)
			mCurrentPage = 1;
		if (mCurrentPage >= mTotalPage) {
			mCurrentPage = mTotalPage;
		}
		if (appInfoDesktopList.size() >= 0) {
			fillGridView(mCurrentPage);
		}
	}

	private void fillGridView(int currentPage) {
		List<CustomAppInfo> listAppInfo = new ArrayList<CustomAppInfo>();
		for (int i = (currentPage - 1) * mAppNumPerPage; i < currentPage* mAppNumPerPage; i++) {
			try {
				listAppInfo.add(mListAppInfo.get(i));
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		if (0 <= listAppInfo.size()) {
		//	mAppsAdapter = new AppAdminAdapter(mContext, listAppInfo);
			mGridApps.setAdapter(mAppsAdapter);
			mPageView.setTotalPage(mTotalPage);
			mPageView.setCurrentPage(mCurrentPage - 1);
			mPageView.invalidate();
		}
	}

	private int caculaterPages(List<CustomAppInfo> list) {
		int appSize = list.size();
		int mAppsNum = mColumnNum * mRows;
		if (0 == appSize % mAppsNum)
			return appSize / mAppsNum;
		else
			return appSize / mAppsNum + 1;
	}

	public void setNumColumns(int columns) {
		mColumnNum = columns;
		mGridApps.setNumColumns(columns);
	}

	public void setNumRows(int rows) {
		mRows = rows;
	}

	public void setArrowVisibility(int visible) {
		mArrowLeft.setVisibility(visible);
		mArrowRight.setVisibility(visible);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (v == mGridApps && event.getAction() == KeyEvent.ACTION_DOWN) {
			int gridSelection = mGridApps.getSelectedItemPosition();
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if (gridSelection + (mAppNumPerPage * (mCurrentPage - 1)) + 1 == mListAppInfo.size()) {
					return true;
				}

				if ((gridSelection + 1) % mColumnNum == 0) {
					if (mCurrentPage == mTotalPage)
						return true;
					turnPage(false);
					int nextSelection = gridSelection - (mColumnNum - 1);
					mGridApps.setSelection(nextSelection);
					return true;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if (gridSelection % mColumnNum == 0) {
					if (mCurrentPage == 0 || mCurrentPage == 1) {
						return true;
					} else {
						turnPage(true);
						int nextSelection = gridSelection + (mColumnNum - 1);
						mGridApps.setSelection(nextSelection);
						return true;
					}
				}
				break;
			case KeyEvent.KEYCODE_ENTER:
			case KeyEvent.KEYCODE_DPAD_CENTER:
				try {
					return false;
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
		return false;
	}

	private void turnPage(boolean direction) {
		if (direction) {
			mCurrentPage--;
		} else {
			mCurrentPage++;
		}
		if (isDoOperationInCurrentPage) {
//			List<CustomAppInfo> list = AppUtils.loadApplications(mContext,false);
//			list.addAll(Constant.getSysApp(mContext));
//			setAdapter(AppUtils.loadApplications(mContext,Configs.showSystemApp()));
			setAdapter(mListAppInfo);
		}
		fillGridView(mCurrentPage);
		isDoOperationInCurrentPage = false;
	}

	private void toDeskTop(CustomAppInfo appInfo) {
		logger.i("toDeskTop...");
		if (mAppDAOI.isExist(appInfo)) {
			logger.i("desk is exist...");
			LauncherApplication.position = mAppDAOI.findAppInfo(appInfo.pkgName).getPosition();
			mAppDAOI.delete(appInfo);
		} else if (mAppDAOI.canInsert(appInfo)) {
			logger.i("can insert into desk...");
			mAppDAOI.insert(mContext, appInfo);
		} else {
			logger.i("show exit window...");
			showExitWindow();
		}
		isDoOperationInCurrentPage = true;
		Intent intent = new Intent(Configs.BroadCastConstant.ACTION_TO_DESKTOP);
		mContext.sendBroadcast(intent);
	}
	
	private PopupWindow mExitPopupWindow;
	private void showExitWindow() {
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		WindowManager mWm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		View view = mInflater.inflate(R.layout.p_exit_pop, null);
		int width = mWm.getDefaultDisplay().getWidth();
		int height = mWm.getDefaultDisplay().getHeight();
		mExitPopupWindow = new PopupWindow(view, width, height, true);
		mExitPopupWindow.showAsDropDown(view, 0, 0);
		mExitPopupWindow.setOutsideTouchable(false);
		Button sure = (Button) view.findViewById(R.id.p_eixt_sure);
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				exitPopDismiss();
			}
		});
	}

	private void exitPopDismiss() {
		if (null != mExitPopupWindow && mExitPopupWindow.isShowing())
			mExitPopupWindow.dismiss();
	}

}
