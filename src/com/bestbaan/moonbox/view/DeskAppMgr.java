package com.bestbaan.moonbox.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bestbaan.moonbox.adapter.DeskAppMgrAdapter;
import com.bestbaan.moonbox.database.AppDAO;
import com.bestbaan.moonbox.util.Logger;
import com.bestbaan.moonbox.util.AppUtils.AppInfo;
import com.bestbaan.moonbox.view.AppGrid.AppsAdapter2;
import com.moonX.util.AppUtil;
import com.moonbox.android.iptv.R;

public class DeskAppMgr extends LinearLayout {

	private Logger logger = Logger.getInstance();
	private Context mContext;
	private PopupWindow mPopup;
	private View mView;
	private GridView mGrid;

	private DeskAppMgrAdapter mAdapter;
	private List<AppInfo> mAppList;

	private AppsAdapter2 mAppsAdapter;
	
	public DeskAppMgr(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initView();
	}

	public DeskAppMgr(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DeskAppMgr(Context context) {
		this(context, null);
	}

	private void initView() {
		mView = LayoutInflater.from(mContext).inflate(R.layout.popup_mgr, this);
		mGrid = (GridView) mView.findViewById(R.id.grid_mgr);
		mGrid.setOnItemClickListener(mOnItemClickListener);
		mGrid.setOnItemSelectedListener(mOnItemSelectedListener);
		mGrid.setOnKeyListener(mOnKeyListener);
		mPopup = new PopupWindow(mView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		mPopup.setAnimationStyle(R.style.style_popup_mgr);
		// mPopup.setBackgroundDrawable(new ColorDrawable());
	}

	public void setData(AppsAdapter2 appsAdapter, List<AppInfo> appList) {
		mAppList = appList;
		mAppsAdapter=appsAdapter;
		mAdapter = new DeskAppMgrAdapter(mContext, mAppList);
		mGrid.setAdapter(mAdapter);
//		new Intent().setAction("11");
	}

	public void show() {
		logger.i("show()");
		mPopup.showAtLocation(mView, Gravity.CENTER, 0, 0);
	}

	public void dismiss() {
		logger.i("dismiss()");
		mPopup.dismiss();
	}

	private int mClickedItemPosition = 0;
	private boolean mShake = false;
	private boolean mCanClosePopup=true;
	OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mClickedItemPosition = position;
			mShake = true;
			mCanClosePopup=false;
			mAdapter.notifyDataSetChanged(mClickedItemPosition, mShake);
		}
	};

	private OnKeyListener mOnKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (KeyEvent.ACTION_UP == event.getAction()) {
				if (mShake == true) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_CENTER:
						logger.i("ok pressed...");
						mShake = false;
						mCanClosePopup=true;
						mAdapter.notifyDataSetChanged(mClickedItemPosition,mShake);
						mAppsAdapter.notifyDataSetChanged();
						break;
					case KeyEvent.KEYCODE_DPAD_LEFT:
						if (AppUtil.canMovePre(mClickedItemPosition)) {
							AppUtil.movePre(mGrid, mAppList, mAdapter,
									mClickedItemPosition);
							--mClickedItemPosition;
						}
						break;
					case KeyEvent.KEYCODE_DPAD_RIGHT:
						if (AppUtil.canMoveNext(mAppList.size(),mClickedItemPosition)) {
							AppUtil.moveNext(mGrid, mAppList, mAdapter,mClickedItemPosition);
							++mClickedItemPosition;
						}
						break;
					case KeyEvent.KEYCODE_DPAD_UP:
						if (AppUtil.canMoveUp(mClickedItemPosition)) {
							AppUtil.moveUp(mGrid, mAppList, mAdapter,
									mClickedItemPosition);
							mClickedItemPosition = mClickedItemPosition - 8;
						}
						break;
					case KeyEvent.KEYCODE_DPAD_DOWN:
						if (AppUtil.canMoveDown(mAppList.size(),
								mClickedItemPosition)) {
							AppUtil.moveDown(mGrid, mAppList, mAdapter,
									mClickedItemPosition);
							mClickedItemPosition = mClickedItemPosition + 8;
						}
						break;
					}
				}else{
					if(mCanClosePopup==true&&keyCode==KeyEvent.KEYCODE_BACK){
						new Thread(new SaveToDBRunnable()).start();
						dismiss();
					}
				}
			}
			boolean obtainEvent = mShake == true ? true : false;//if obtainEvent=true,this event will not delivery,false otherwise
			return obtainEvent;
		}
	};
	
	private OnItemSelectedListener mOnItemSelectedListener=new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			mAdapter.notifyDataSetChanged(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	
	private class SaveToDBRunnable implements Runnable{
		@Override
		public void run() {
			AppDAO dao=new AppDAO(mContext);
			dao.updatePosition(mAppList);
		}
		
	}
}
