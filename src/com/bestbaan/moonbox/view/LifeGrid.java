package com.bestbaan.moonbox.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bestbaan.moonbox.adapter.AppAdminAdapter;
import com.bestbaan.moonbox.adapter.LifeChannelAdapter;
import com.bestbaan.moonbox.database.AppDAO;
import com.bestbaan.moonbox.model.LifeMenu;
import com.bestbaan.moonbox.model.LifeModel;
import com.bestbaan.moonbox.util.AppUtils;
import com.bestbaan.moonbox.util.CustomAppInfo;
import com.bestbaan.moonbox.util.Logger;
import com.bestbaan.moonbox.util.MACUtils;
import com.forcetech.android.ForceTV;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moon.android.iptv.Configs;
import com.moon.android.iptv.Constant;
import com.moon.android.iptv.LauncherApplication;
import com.moon.android.iptv.LifeImageActicvity;
import com.moon.android.iptv.LifeWebActivity;
import com.moon.android.iptv.MainActivity;
import com.moon.android.moonplayer.VodPlayerActivity;
import com.moon.android.moonplayer.service.VodVideo;
import com.moonbox.android.iptv.R;

@SuppressLint("NewApi")
public class LifeGrid extends LinearLayout implements OnKeyListener {

	private Logger logger = Logger.getInstance();
	private LinearLayout mArrowLeft;
	private LinearLayout mArrowRight;
	private GridView mGridApps;
	private Context mContext;
	private int mColumnNum = 2;
	private int mRows = 2;
	private int mCurrentPage = 1;
	private int mAppNumPerPage;
	private int mTotalPage = 1;
	// private AppDAO mAppDAOI;
	// private List<CustomAppInfo> mListAppInfo;
	private List<LifeModel> mListAppInfo = new ArrayList<LifeModel>();
	private PageIndicatorView mPageView;
	private boolean isDoOperationInCurrentPage = false;
	public static final String PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";
	public static final String PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";
	public static final int ACTION_UPDATE_DESKTOP = 0x1000002;
	private AnimationDrawable anim_selector = null;
	private LifeChannelAdapter mAppsAdapter;

	public LifeGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		View view = LayoutInflater.from(context).inflate(
				R.layout.apps_manage_view, this);
		mArrowLeft = (LinearLayout) view.findViewById(R.id.arrow_left);
		mArrowRight = (LinearLayout) view.findViewById(R.id.arrow_right);
		mGridApps = (GridView) view.findViewById(R.id.life_grid);
		// anim_selector = (AnimationDrawable) mGridApps.getSelector();
		// anim_selector.start();

		mPageView = (PageIndicatorView) view.findViewById(R.id.ad_page_num);
		mArrowLeft.setOnClickListener(mArrowClickListener);
		mArrowRight.setOnClickListener(mArrowClickListener);
		mGridApps.setOnKeyListener(this);
		mGridApps.setOnFocusChangeListener(mGridFocusChan);
		mGridApps.setOnItemSelectedListener(mOnItemSelectedListener);
		mGridApps.setOnItemClickListener(mGridItemClicklistener);
		// mAppDAOI = new AppDAO(context);
		mListAppInfo = new ArrayList<LifeModel>();
		// for (int i = 0; i < 50; i++) {
		// mListAppInfo.add(new LifeModel("" + i, null, null, null, null));
		// }
		// mAppsAdapter = new AppAdminAdapter(mContext, mListAppInfo);
		// mGridApps.setAdapter(mAppsAdapter);
		// mGridApps.setOnItemClickListener(mOnGridItemClickListener);
		// mGridApps.setOnItemSelectedListener(mOnGridItemSelectedListener);
		// mGridApps.smoothScrollToPosition(2);
		// setAdapter(AppUtils.loadApplications(context,
		// Configs.showSystemApp()));
		// =AppUtils.loadAppManager(context, false);
		initList();

	}

	private OnItemClickListener mGridItemClicklistener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			LifeModel item = mListAppInfo.get(position+(mCurrentPage-1)*(mRows*mColumnNum));

			String type = item.getType();
			if (type.equals("0")) {
				try {
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClass(mContext, LifeImageActicvity.class);
					// intent.setComponent(componetName);
					Bundle bundle = new Bundle();
					// 传递name参数为tinyphp

					bundle.putString("id", item.getId());
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			if (type.equals("1")) {
				try {
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClass(mContext, LifeWebActivity.class);
					// intent.setComponent(componetName);
					Bundle bundle = new Bundle();
					// 传递name参数为tinyphp

					bundle.putString("url", item.getUrl());
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			if (type.equals("2")) {
				try {
				    String Url=item.getUrl();
				    Log.d("listUrl",Url);
				    String ChannelId="",Streamip="";
				    if(Url.indexOf("force") == 0){
				    	String[] ForceArray=Url.split("/");
				    	ChannelId=ForceArray[3];
				    	Streamip=ForceArray[2];
				    }
					ComponentName componetName = new ComponentName(
							mContext,
							com.moon.android.moonplayer.VodPlayerActivity.class);
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					List<VodVideo> list = new ArrayList<VodVideo>();
					VodVideo v = new VodVideo();
					v.setChannelId(ChannelId);
					v.setLink("ZCM5P27nqPBuNQdI");
					v.setStreamip(Streamip);
					v.setType("ts");
					v.setUrl(Url);
					v.setName(item.getName());
					// force://vodlist.btvgod.com:9906/56e630380007ff5c04651b0c5ecd1a0b
					list.add(v);
					intent.putExtra("videolist", (Serializable) list);
					//intent.setClass(mContext, VodPlayerActivity.class);
					intent.setComponent(componetName);
					mContext.startActivity(intent);
				} catch (Exception e) {

				}
			}
		}

	};
	private OnFocusChangeListener mGridFocusChan = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub
		//	Log.d("focus", arg1 + "");

			mAppsAdapter.notifyFocusChanged(arg1);

		}

	};

	@SuppressWarnings("unchecked")
	private void initList() {
		// TODO Auto-generated method stub
		FinalHttp fh = new FinalHttp();

		AjaxParams params = new AjaxParams();
		params.put("mac", MACUtils.getMac());
//		Log.d("life", Configs.GetLifeList());
		fh.post(Configs.GetLifeList(), params, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
//				Log.d("lifelist", "error");
			}

			@Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
//				Log.d("res",t.toString());
				Gson gson = new Gson();
				try {
					mListAppInfo = gson.fromJson(t.toString(),
							new TypeToken<List<LifeModel>>() {
							}.getType());
				} catch (Exception e) {
					// TODO: handle exception
				}
				setAdapter(mListAppInfo);
				//
			}

		});
	}

	private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// Log.d("select:", position + "");
			// view.setLayoutParams(new GridView.LayoutParams(550,510));
			// Animation shake = AnimationUtils.loadAnimation(mContext,
			// R.anim.anim_life);
			// shake.reset();
			// shake.setFillAfter(true);
			// view.startAnimation(shake);
			mAppsAdapter.notifyDataSetChanged(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
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
			// logger.i("mCurrentPage="+mCurrentPage+"  select app posotion="+(position+mCurrentPage*(mRows*mColumnNum)));
			// CustomAppInfo appInfo =
			// mListAppInfo.get(position+(mCurrentPage-1)*(mRows*mColumnNum));
			// logger.i("onItemClick() appInfo : "+appInfo);
			// boolean toDesk = !appInfo.isDesktop;
			// appInfo.isDesktop = toDesk;
			// mAppsAdapter.notifyDataSetChanged(position, toDesk);
			//
			// toDeskTop(appInfo);
		}
	};

	public LifeGrid(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LifeGrid(Context context) {
		this(context, null);
	}

	public void setAdapter(List<LifeModel> list) {

		mGridApps.setNumColumns(mColumnNum);
		mListAppInfo = list;
		mTotalPage = caculaterPages(list);
		mAppNumPerPage = mColumnNum * mRows;
		if (mCurrentPage == 0)
			mCurrentPage = 1;
		if (mCurrentPage >= mTotalPage) {
			mCurrentPage = mTotalPage;
		}
		if (list.size() > 0) {
			fillGridView(mCurrentPage);
		}
	}

	private void fillGridView(int currentPage) {
		List<LifeModel> listAppInfo = new ArrayList<LifeModel>();
		for (int i = (currentPage - 1) * mAppNumPerPage; i < currentPage
				* mAppNumPerPage; i++) {
			try {
				listAppInfo.add(mListAppInfo.get(i));
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		if (0 <= listAppInfo.size()) {
			mAppsAdapter = new LifeChannelAdapter(mContext, listAppInfo);
			mGridApps.setAdapter(mAppsAdapter);
			// mAppsAdapter.initOver();
			mPageView.setTotalPage(mTotalPage);
			mPageView.setCurrentPage(mCurrentPage - 1);
			mPageView.invalidate();
		}
	}

	private int caculaterPages(List<LifeModel> list) {
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
				if (gridSelection + (mAppNumPerPage * (mCurrentPage - 1)) + 1 == mListAppInfo
						.size()) {
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
			// List<CustomAppInfo> list =
			// AppUtils.loadApplications(mContext,false);
			// list.addAll(Constant.getSysApp(mContext));
			// setAdapter(AppUtils.loadApplications(mContext,Configs.showSystemApp()));
			setAdapter(mListAppInfo);

		}
		fillGridView(mCurrentPage);
		mAppsAdapter.FocusChanged();
		isDoOperationInCurrentPage = false;
	}

	//
	// private void toDeskTop(CustomAppInfo appInfo) {
	// logger.i("toDeskTop...");
	// if (mAppDAOI.isExist(appInfo)) {
	// logger.i("desk is exist...");
	// LauncherApplication.position =
	// mAppDAOI.findAppInfo(appInfo.pkgName).getPosition();
	// mAppDAOI.delete(appInfo);
	// } else if (mAppDAOI.canInsert(appInfo)) {
	// logger.i("can insert into desk...");
	// mAppDAOI.insert(mContext, appInfo);
	// } else {
	// logger.i("show exit window...");
	// showExitWindow();
	// }
	// isDoOperationInCurrentPage = true;
	// Intent intent = new Intent(Configs.BroadCastConstant.ACTION_TO_DESKTOP);
	// mContext.sendBroadcast(intent);
	// }
	//
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
