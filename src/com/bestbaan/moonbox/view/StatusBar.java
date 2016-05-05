package com.bestbaan.moonbox.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bestbaan.moonbox.util.CheckSdcard;
import com.moon.android.iptv.Configs;
import com.moonbox.android.iptv.R;

public class StatusBar extends LinearLayout {

	private Context mContext;
	private ImageView mLanImg;
	private ImageView mWifiImg;
	private ImageView mUsbImg;
	private ImageView mMsgImg;

	public StatusBar(Context paramContext) {
		this(paramContext, null);
	}

	public StatusBar(Context context, AttributeSet attributeset) {
		super(context, attributeset);
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.status_bar, this) ;
		mContext = context;
		mWifiImg = (ImageView) view.findViewById(R.id.status_bar_wifi);
		mLanImg = (ImageView) view.findViewById(R.id.status_bar_lan);
		mUsbImg = (ImageView) view.findViewById(R.id.status_bar_sub);
		mMsgImg = (ImageView) view.findViewById(R.id.status_bar_msg);
		initEthConnect();
		regNetworkReceiver();
		setSdcardStatus();
	}

	private void regNetworkReceiver() {
		IntentFilter filter = new IntentFilter();
        
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);  
        filter.addAction(Intent.ACTION_MEDIA_CHECKING);  
        filter.addAction(Intent.ACTION_MEDIA_EJECT);  
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);  
        filter.addDataScheme("file"); 
        mContext.registerReceiver(mReceiver, filter);
        
        IntentFilter filterNetwrok = new IntentFilter();
        filterNetwrok.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filterNetwrok.addAction(Configs.BroadCastConstant.ACTION_NEW_USER_MSG);
        filterNetwrok.addAction(Configs.BroadCastConstant.ACTION_NEW_USER_NO_MSG);
        mContext.registerReceiver(networkReceiver, filterNetwrok);   
        
	}

	private void initEthConnect() {
		int status = getNetworkConnect(mContext);
		setNetworkConnect(status);
	}
	
	private void setNetworkConnect(int status){
		if(status == 1){
			mWifiImg.setBackgroundResource(R.drawable.icon_wifi);
			mWifiImg.setVisibility(View.VISIBLE);
			mLanImg.setBackgroundResource(R.drawable.transport);
			mLanImg.setVisibility(View.GONE);
		}else if(status == 2){
			mWifiImg.setBackgroundResource(R.drawable.transport);
			mWifiImg.setVisibility(View.GONE);
			mLanImg.setBackgroundResource(R.drawable.icon_eth_focus);
			mLanImg.setVisibility(View.VISIBLE);
		} else{
			mWifiImg.setBackgroundResource(R.drawable.transport);
			mLanImg.setBackgroundResource(R.drawable.transport);
			mLanImg.setVisibility(View.GONE);
			mWifiImg.setVisibility(View.GONE);
		} 
	}
	
	public int getNetworkConnect(Context context){
		NetworkInfo networkinfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
	    if(networkinfo != null){
	        String s = networkinfo.getTypeName();
	        if(s.equalsIgnoreCase("wifi"))
	           return 1;
	        else
	        if(s.equalsIgnoreCase("ETH")|| s.equalsIgnoreCase("ETHERNET"))
	            return 2;
	    }
		return 0;
    }
	
	
	private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            	setNetworkConnect(getNetworkConnect(mContext));
            }
            
            if(action.equals(Configs.BroadCastConstant.ACTION_NEW_USER_MSG)){
            	mMsgImg.setBackgroundResource(R.drawable.icon_msg_focus);
            	mMsgImg.setVisibility(View.VISIBLE);
            } else if(action.equals(Configs.BroadCastConstant.ACTION_NEW_USER_NO_MSG)){
            	mMsgImg.setBackgroundResource(R.drawable.transport);
            	mMsgImg.setVisibility(View.GONE);
            }
		};
	};
	
	 private BroadcastReceiver mReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String action = intent.getAction();
	            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
	            	setNetworkConnect(getNetworkConnect(mContext));
	            }
	            
	            if (action.equals(Intent.ACTION_MEDIA_MOUNTED) || 
	            		action.equals(Intent.ACTION_MEDIA_CHECKING)
	            		|| action.equals(Intent.ACTION_MEDIA_REMOVED)) {
	            	setSdcardStatus();
                }
	            
	        }
	    };
	    
	    private void setSdcardStatus(){
	    	boolean has = CheckSdcard.isHasExtendStorage(mContext);
        	int visibility = has ? View.VISIBLE : View.GONE;
        	mUsbImg.setVisibility(visibility);
	    }
	    
	    public void unRegReceiver(){
	    	mContext.unregisterReceiver(mReceiver);
	    	mContext.unregisterReceiver(networkReceiver);
	    }
	    
}