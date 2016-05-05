package com.moon.android.iptv;

import com.bestbaan.moonbox.view.CustomToast;
import com.moonbox.android.iptv.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class StartAppActivity extends Activity{

	private static final String TAG = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String pkgName = getIntent().getStringExtra(Configs.PARAM_1);
		Intent intent = null;
		try{
			if(null != pkgName && !"".equals(pkgName))
//				ActivityUtils.startActivity(this,pkgName);
			intent = getPackageManager().getLaunchIntentForPackage(pkgName);
			if (null != intent){
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Log.i(TAG, "start application package ["+pkgName+"]");
				startActivity(intent);
			} else {
				new CustomToast(this, getString(R.string.app_not_found), Configs.TOAST_TEXT_SIZE).show();
				Log.e(TAG, "***************************************");
				Log.e(TAG, "****   not found start application package ["+pkgName+"]");
				Log.e(TAG, "****   please check ");
				Log.e(TAG, "***************************************");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		finish();
	}
}
