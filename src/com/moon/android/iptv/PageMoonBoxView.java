package com.moon.android.iptv;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.poi.hdgf.chunks.Chunk.Command;
import org.apache.poi.hssf.record.PageBreakRecord.Break;

import android.annotation.SuppressLint;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bestbaan.moonbox.database.UserMsgDAO;
import com.bestbaan.moonbox.model.LifeModel;
import com.bestbaan.moonbox.model.PingInfo;
import com.bestbaan.moonbox.model.UserMsg;
import com.bestbaan.moonbox.util.AppUtils;
import com.bestbaan.moonbox.util.Logger;
import com.bestbaan.moonbox.util.MACUtils;
import com.bestbaan.moonbox.util.RequestUtil;
import com.bestbaan.moonbox.util.SystemInfoManager;
import com.bestbaan.moonbox.util.SystemInfoUtil;
import com.bestbaan.moonbox.util.VersionUtil;
import com.bestbaan.moonbox.util.AppUtils.AppInfo;
import com.bestbaan.moonbox.view.CustomToast;
import com.bestbaan.moonbox.view.AppGrid.AppsAdapter2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moonX.util.CommandUtil;
import com.moonbox.android.iptv.R;

public class PageMoonBoxView extends RelativeLayout {

	public static Logger logger = Logger.getInstance();
	private TextView mTextVersion;
	private TextView mTextMac;
	private TextView mTextHardware;
	private ListView mListViewUserMsg;
	private LinearLayout mArrowLeft;
	private LinearLayout mArrowRight;
	private UserMsgDAO mMsgDAO;
	private List<UserMsg> mListUserMsg;
	private List<UserMsg> mCurrentPageUserMsg;
	private TextView mTextPage;
	private int mCurrentPage = 1;
	private int mTotalPage;
	private LinearLayout mRightLine;
	private int LIST_PAGE_SIZE = 7;
	private Context mContext;
	private TextView mtv_mac, mtv_ver, mtv_model, mtv_modelver, mtv_kernelver,mtv_Ip;
	private Button mbt_net_test;
	private GridView mGv;
	private final int PING_SUCCESS = 0;
	private final int PING_FAIL = 1;
	private final int PING_INIT = 2;
	private final int PING_OVER = 3;
	private final int IP_INIT = 4;
	private String Ip;
	private boolean canclick = true;
	private int GetIP_MINUTE=5;
	private ImageView mAnmi;
	public List<PingInfo> mPingList = new ArrayList<PingInfo>();

	public PageMoonBoxView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		View view = LayoutInflater.from(context).inflate(R.layout.page_moonbox,
				this);
		initNewWidget(view);
		initNewData();
		initData();
		TimerStart();
		regBroadCast();
		initWidget(view);
	}

	private void TimerStart() {
		// TODO Auto-generated method stub
		 Timer timer = new Timer();
		 timer.schedule(new java.util.TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				initIp();
			}                 
			 
		 },1000*60*GetIP_MINUTE,1000*60*GetIP_MINUTE);
	}

	private void initNewData() {
		// TODO Auto-generated method stub
		mtv_mac.setText(MACUtils.getMac());
		mtv_ver.setText(VersionUtil.getVersionName(mContext));
		mtv_model.setText(SystemInfoManager.getModelNumber());
		mtv_modelver.setText(SystemInfoManager.getBuildNumber());
		mtv_kernelver.setText(SystemInfoManager.getKernelVersion());
//		Log.d("1:",SystemInfoManager.getAndroidVersion());
//		Log.d("2:",SystemInfoManager.getBuildNumber());
//		Log.d("3:",SystemInfoManager.getKernelVersion());
//		Log.d("4:",SystemInfoManager.getModelNumber());
		initIp();
		initPinginfo();

	}

	private void initIp() {
		// TODO Auto-generated method stub
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
			      
				try {
					 String ip=RequestUtil.getInstance().request(Configs.GetIpUrl());
					 Log.d("ip",ip);
				
					
					 
					 
					 boolean isIP = ip.length()>20?false:true; 
					
					 if(isIP){
						 Ip=ip;
//						 Log.d("ip",Ip);
						 mHandler.sendEmptyMessage(IP_INIT);
					 }
				} catch (Exception e) {
					// TODO: handle exception
				}
			   
			   
			}
			
		}.start();
	}

	private void initPinginfo() {
		// TODO Auto-generated method stub
	
		
		FinalHttp fh = new FinalHttp();

		AjaxParams params = new AjaxParams();
		params.put("mac", MACUtils.getMac());
		fh.post(Configs.getPingList(),params,new AjaxCallBack<Object>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				mPingList=Configs.getDefaultPing();
			}

			@Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
//				Log.d("res",t.toString());
				Gson gson = new Gson();
				try {
					mPingList = gson.fromJson(t.toString(),
							new TypeToken<List<PingInfo>>() {
							}.getType());
				} catch (Exception e) {
					// TODO: handle exception
					mPingList=Configs.getDefaultPing();
				}
			}
			   
		});
		
	}

	private void initNewWidget(View view) {
		// TODO Auto-generated method stub
		mtv_mac = (TextView) view.findViewById(R.id.mybox_mac);
		mtv_ver = (TextView) view.findViewById(R.id.mybox_ver);
		mtv_model = (TextView) view.findViewById(R.id.mybox_model);
		mtv_modelver = (TextView) view.findViewById(R.id.mybox_modelver);
		mtv_kernelver = (TextView) view.findViewById(R.id.mybox_kernelver);
		mtv_Ip=(TextView) view.findViewById(R.id.mybox_ip);
		mbt_net_test = (Button) view.findViewById(R.id.mybox_network_test);
		mRightLine = (LinearLayout) view.findViewById(R.id.mybox_rightLine);
		mbt_net_test.setOnClickListener(netTextClick);

	}
	public OnClickListener testClick = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
//			SystemInfoUtil.getInfo();
			Log.d("1:",SystemInfoManager.getAndroidVersion());
			Log.d("2:",SystemInfoManager.getBuildNumber());
			Log.d("3:",SystemInfoManager.getKernelVersion());
			Log.d("4:",SystemInfoManager.getModelNumber());
		}
		 
	};
	public OnClickListener netTextClick = new OnClickListener() {

		@SuppressLint("NewApi")
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			// SystemInfoUtil.getInfo();
			if (canclick) {
				canclick = false;
				mbt_net_test.setTextColor(mContext.getResources().getColor(
						R.color.test_ing));
				mbt_net_test.setText(mContext.getResources().getString(
						R.string.test_ing));
				mbt_net_test.setBackground(mContext.getResources().getDrawable(
						R.drawable.mybox_bt_click));
				mRightLine.removeAllViews();
				new Thread() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						for (int i = 0; i < mPingList.size(); i++) {
							PingInfo item = mPingList.get(i);
							Message message = new Message();
							message.arg1 = i;
							message.what = PING_INIT;
							Message message2 = new Message();
							message2.arg1 = i;

							mHandler.sendMessage(message);
							Boolean pingRe = CommandUtil.Ping(item.getAddr());

							if (pingRe) {
								message2.what = PING_SUCCESS;
							} else {
								message2.what = PING_FAIL;
							}
							//
							// message2.what = PING_SUCCESS;
							// Log.d("ping---", pingRe + "");
							mHandler.sendMessage(message2);

						}
						Message message3 = new Message();
						message3.what = PING_OVER;
						message3.arg2 = 1;
						mHandler.sendMessage(message3);

					}

				}.start();
			}

		}

	};
	Handler mHandler = new Handler() {

		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			TextView text = null;
			PingInfo item = null;
			if (msg.what == PING_INIT || msg.what == PING_SUCCESS
					|| msg.what == PING_FAIL) {
				text = new TextView(mContext);
				DisplayMetrics dm = new DisplayMetrics();
				dm = mContext.getApplicationContext().getResources()
						.getDisplayMetrics();

				text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ((int) Math
						.floor(mContext.getResources().getDimensionPixelSize(
								R.dimen.my_box_test_info)) / dm.density));

				item = mPingList.get(msg.arg1);
			}

			switch (msg.what) {
			case IP_INIT:
				mtv_Ip.setText(Ip);
				break;
			case PING_INIT:
				text.setText(item.getPrompt());
				mAnmi = new ImageView(mContext);
				// LinearLayout line=new LinearLayout(mContext);
				// line.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						55, 12);
				layoutParams.setMargins(0, 12, 0, 0);
				mAnmi.setLayoutParams(layoutParams);
				mAnmi.setMaxHeight(12);
				mAnmi.setMaxWidth(55);

				mAnmi.setBackground(mContext.getResources().getDrawable(
						R.anim.anim_connect_ing));
				AnimationDrawable anim = (AnimationDrawable) mAnmi
						.getBackground();
				anim.start();

				// mRightLine.addView(img);
				mRightLine.addView(text);
				mRightLine.addView(mAnmi);
				break;
			case PING_SUCCESS:
				mRightLine.removeView(mAnmi);
				text.setTextColor(mContext.getResources().getColor(
						R.color.ping_success));
				text.setText(item.getSuccess_info());
				mRightLine.addView(text);
				break;
			case PING_FAIL:
				mRightLine.removeView(mAnmi);
				text.setTextColor(mContext.getResources().getColor(
						R.color.ping_fail));

				text.setText(item.getFail_info());
				mRightLine.addView(text);
				break;
			case PING_OVER:
				canclick = true;
				mbt_net_test.setTextColor(mContext.getResources().getColor(
						R.color.text_white));
				mbt_net_test.setText(mContext.getResources().getString(
						R.string.test_start));
				mbt_net_test.setBackground(mContext.getResources().getDrawable(
						R.drawable.bg_mybox_bt));
				break;
			default:
				break;
			}

			// super.handleMessage(msg);
		}

	};

	public PageMoonBoxView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PageMoonBoxView(Context context) {
		this(context, null);
	}

	private void initData() {
		getListContent();
	}

	private void getListContent() {
		mMsgDAO = new UserMsgDAO(mContext);
		mListUserMsg = mMsgDAO.queryAll();
		mTotalPage = caculaterPages(mListUserMsg);
	}

	private void regBroadCast() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Configs.BroadCastConstant.GET_USER_MSG);
		myIntentFilter.addAction(Configs.BroadCastConstant.ACTION_GET_HARDWARE);
		mContext.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			logger.i("Moonbox get msg broadcast");
			String action = intent.getAction();
			if (action.equals(Configs.BroadCastConstant.GET_USER_MSG)) {
				getListContent();
				fillUserMsg();
				checkIsMsgNotRead();
			} else if (action
					.equals(Configs.BroadCastConstant.ACTION_GET_HARDWARE)) {
				mTextHardware.setText(Declare.HARDWARE_MODEL);
			}
		}
	};

	public int getTotalPage() {
		return mTotalPage;
	}

	private void fillUserMsg() {
		setPage();
		mCurrentPageUserMsg = new ArrayList<UserMsg>();
		for (int i = (mCurrentPage - 1) * LIST_PAGE_SIZE; i < mCurrentPage
				* LIST_PAGE_SIZE; i++) {
			try {
				mCurrentPageUserMsg.add(mListUserMsg.get(i));
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		if (0 != mCurrentPageUserMsg.size()) {
			MsgAdapter adapter = new MsgAdapter(mContext, mCurrentPageUserMsg);
			mListViewUserMsg.setAdapter(adapter);
		}
	}

	private int caculaterPages(List<UserMsg> list) {
		int appSize = list.size();
		if (0 == appSize % LIST_PAGE_SIZE)
			return appSize / LIST_PAGE_SIZE;
		else
			return appSize / LIST_PAGE_SIZE + 1;
	}

	private OnKeyListener mListKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (v == mListViewUserMsg
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
					toNextPage();
					return true;
				} else if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
					toPrePage();
					return true;
				}
			}
			return false;
		}
	};

	private void toNextPage() {
		if (mTotalPage > mCurrentPage) {
			mCurrentPage++;
			fillUserMsg();
		} else {
			new CustomToast(mContext, mContext.getString(R.string.has_to_last),
					Configs.TOAST_TEXT_SIZE).show();
		}
	}

	private void toPrePage() {
		if (mCurrentPage > 1) {
			mCurrentPage--;
			fillUserMsg();
		} else {
			new CustomToast(mContext,
					mContext.getString(R.string.has_to_first),
					Configs.TOAST_TEXT_SIZE).show();
		}
	}

	private void initWidget(View view) {
		mTextVersion = (TextView) view.findViewById(R.id.page_moonbox_version);
		mTextMac = (TextView) view.findViewById(R.id.page_moonbox_mac);
		mTextHardware = (TextView) view
				.findViewById(R.id.page_moonbox_hardware);
		mListViewUserMsg = (ListView) view
				.findViewById(R.id.page_moonbox_listview);
		mArrowLeft = (LinearLayout) view.findViewById(R.id.arrow_left);
		mArrowRight = (LinearLayout) view.findViewById(R.id.arrow_right);
		mTextPage = (TextView) view.findViewById(R.id.page_moonbox_msg_page);

		mTextMac.setText(MACUtils.getMac());
		mTextHardware.setText(Declare.HARDWARE_MODEL);
		String version = VersionUtil.getVersionName(mContext);
		mTextVersion.setText(version);
		fillUserMsg();
		// mListViewUserMsg.setOnItemClickListener(mListItemLisener);
		// mListViewUserMsg.setOnKeyListener(mListKeyListener);
		mArrowLeft.setOnClickListener(mArrowClickListener);
		mArrowRight.setOnClickListener(mArrowClickListener);
		setPage();
	}

	private void setPage() {
		if (null != mListUserMsg && mListUserMsg.size() >= 1) {
			mTextPage.setText(mCurrentPage + "/" + mTotalPage);
			mArrowLeft.setVisibility(View.VISIBLE);
			mArrowRight.setVisibility(View.VISIBLE);
		} else {
			mTextPage.setText("0/0");
			mArrowLeft.setVisibility(View.INVISIBLE);
			mArrowRight.setVisibility(View.INVISIBLE);
		}
	}

	private OnClickListener mArrowClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == mArrowLeft) {
				toPrePage();
			} else if (v == mArrowRight) {
				toNextPage();
			}
		}
	};

	private OnItemClickListener mListItemLisener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> views, View view, int position,
				long arg3) {
			UserMsg userMsg = mCurrentPageUserMsg.get(position);
			if (Configs.UserMsgVar.MSG_NOT_READ.equals(userMsg.getStatus())) {
				View viewImage = ((LinearLayout) ((RelativeLayout) view)
						.getChildAt(1)).getChildAt(1);
				viewImage.setVisibility(View.INVISIBLE);
				int msgPos = (mCurrentPage - 1) * LIST_PAGE_SIZE + position;
				userMsg.setStatus(Configs.UserMsgVar.MSG_HAS_READ);
				mListUserMsg.set(msgPos, userMsg);
				mMsgDAO.changeMsgStatus(userMsg);
				checkIsMsgNotRead();
			}
			Intent intent = new Intent();
			intent.setClass(mContext, UserMsgShowActivity.class);
			intent.putExtra(Configs.PARAM_1, userMsg);
			mContext.startActivity(intent);
		}
	};

	private void checkIsMsgNotRead() {
		String flag = mMsgDAO.hasNoReadMsg() ? Configs.BroadCastConstant.ACTION_NEW_USER_MSG
				: Configs.BroadCastConstant.ACTION_NEW_USER_NO_MSG;
		Intent intent = new Intent();
		intent.setAction(flag);
		mContext.sendBroadcast(intent);
	}

	public static class MsgAdapter extends BaseAdapter {
		private List<UserMsg> mListUserMsg;
		private LayoutInflater mInflater;

		public MsgAdapter(Context context, List<UserMsg> list) {
			mListUserMsg = list;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mListUserMsg.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mListUserMsg.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			Holder holder = null;
			if (null == convertView) {
				holder = new Holder();
				convertView = mInflater.inflate(R.layout.user_msg_item, null);
				holder.textTitle = (TextView) convertView
						.findViewById(R.id.user_msg_item_text);
				holder.imsgeMsg = (ImageView) convertView
						.findViewById(R.id.user_msg_item_icon);
				holder.textDate = (TextView) convertView
						.findViewById(R.id.user_msg_item_date);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			UserMsg msg = mListUserMsg.get(position);
			holder.textTitle.setText(msg.getTitle());
			int isRead = (Configs.UserMsgVar.MSG_HAS_READ.equals(msg
					.getStatus())) ? View.INVISIBLE : View.VISIBLE;
			holder.imsgeMsg.setVisibility(isRead);
			holder.textDate.setText(msg.getTime());
			if (position % 2 == 0) {
				convertView
						.setBackgroundResource(R.drawable.user_msg_no_transport);
			}
			return convertView;
		}

		class Holder {
			TextView textTitle;
			ImageView imsgeMsg;
			TextView textDate;
		}
	}
}
