package com.moon.android.iptv;

import java.util.List;















import com.bestbaan.moonbox.adapter.Adapter_AllApp;
import com.bestbaan.moonbox.model.Model_App;
import com.bestbaan.moonbox.util.AppUtils;
import com.bestbaan.moonbox.view.IndexSort;
import com.moonX.util.AppUtil;


import com.moonbox.android.iptv.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class AllAppActicity extends Activity{
	private GridView mgv;
	private List<Model_App> mApplist;
	private Adapter_AllApp mGvAdapter;
	private Handler mhandler;
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		mGvAdapter.saveApp();
		Configs.indexView.updateGridView(false);
		new AlertDialog.Builder(AllAppActicity.this).setTitle(AllAppActicity.this.getResources().getString(R.string.title_alert)).setMessage(AllAppActicity.this.getResources().getString(R.string.add_over)).
		setNegativeButton(AllAppActicity.this.getResources().getString(R.string.ok),new DialogInterface.OnClickListener() {//添加返回按钮    
	         @Override  
	         public void onClick(DialogInterface dialog, int which) {//响应事件  
	        	
	        		finish();
	        		Configs.indexView.startSortView(); 
	         }  
	  
	     }).setPositiveButton(AllAppActicity.this.getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {//添加确定按钮  
	         @Override  
	         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
//	        	    IndexSort.getIndexSort(AllAppActicity.this).init();
	        		finish();
	         }  
	  
	     }).show();//在按键响应事件中显示此对话框  
		//super.onBackPressed();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		
		super.onDestroy();
	}
	
      @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_allapp);
    	initwidget();
    	initGird();
    }
	private void initGird() {
		// TODO Auto-generated method stub
		//mhandler= (Handler) getIntent().getSerializableExtra("Handler");
		mApplist=mApplist=AppUtils.getAllApp(AllAppActicity.this);
		mGvAdapter=new Adapter_AllApp(AllAppActicity.this, mApplist,Configs.NowTag);
		mgv.setAdapter(mGvAdapter);
	}
	private void initwidget() {
		// TODO Auto-generated method stub
		mgv=(GridView) findViewById(R.id.allapp_gv);
		mgv.setOnItemClickListener(mGvClick);
		
	}
	OnItemClickListener mGvClick=new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			mGvAdapter.ToggleItem(arg2);
		}
		
	};

}
