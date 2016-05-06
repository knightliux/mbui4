package com.moon.android.iptv;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bestbaan.moonbox.util.CustomAppInfo;
import com.moonbox.android.iptv.R;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AppInfoActicity extends Activity {
	private TextView mtv_appname,mtv_appvercode,mtv_appvername,mtv_ftime,mtv_ltime;
	PackageManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appinfo);
		initwidget();
		
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		if(Configs.nowAppinfo!=null){
			
			CustomAppInfo item=Configs.nowAppinfo;
			PackageInfo packageInfo;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				packageInfo=manager.getPackageInfo(item.pkgName, 0);
				mtv_appname.setText(packageInfo.applicationInfo.loadLabel(manager).toString());
				mtv_appvercode.setText(packageInfo.versionCode+"");
				mtv_appvername.setText(packageInfo.versionName+"");
				mtv_ftime.setText(sdf.format(new Date(Long.parseLong(packageInfo.firstInstallTime+""))));
				mtv_ltime.setText(sdf.format(new Date(Long.parseLong(packageInfo.lastUpdateTime+""))));
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			Log.d("info",item.);
//			mtv_appname.setText(item.title);
//			mtv_appvercode.setText(item.versionCode);
		}
	}

	private void initwidget() {
		// TODO Auto-generated method stub
		manager = AppInfoActicity.this.getPackageManager();
		mtv_appname=(TextView) findViewById(R.id.appinfo_appname);
		mtv_appvercode=(TextView)findViewById(R.id.appinfo_vercode);
		mtv_appvername=(TextView)findViewById(R.id.appinfo_vername);
		mtv_ftime=(TextView)findViewById(R.id.appinfo_ftime);
		mtv_ltime=(TextView)findViewById(R.id.appinfo_ltime);
	}
}
