package com.bestbaan.moonbox.adapter;

import java.util.ArrayList;
import java.util.List;







import com.bestbaan.moonbox.model.Model_App;
import com.bestbaan.moonbox.util.DbUtil;


import com.moonbox.android.iptv.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_AllApp extends BaseAdapter {
	private List<Model_App> mlist;
	private LayoutInflater mInflater;
	private Context mContext;
    private Boolean isCheck[];
	// private DisplayImageOptions mOptions;
    private String Tag;
    private DbUtil db;
	public Adapter_AllApp(Context context, List<Model_App> list,String Tag) {
		mlist = list;
		mContext = context;
		mInflater = LayoutInflater.from(context);
		isCheck=new Boolean[mlist.size()];
		db=new DbUtil(context);
		this.Tag=Tag;
		initCheck();
	
		
		// initDisplayImageOptions();

	}
    public void saveApp(){
    	List<String> pkglist=new ArrayList<String>();
    	db.delShowApp(Tag);
    	for(int i=0;i<isCheck.length;i++){
    		if(isCheck[i]==true){
    			Log.d("isTrueIndex:",""+i);
    			pkglist.add(mlist.get(i).pkgName);
    		}
    	}
    	db.saveShowApp(pkglist, Tag);
    }
	private void initCheck() {
		// TODO Auto-generated method stub
//		for(int i=0;i<mlist.size();i++){
//			isCheck[i]=false;
//		}
		
		
		List<String> applist=db.GetAppByTag(Tag);
		for(int i=0;i<applist.size();i++){
			Log.d("app",applist.get(i));
		}
		for(int i=0;i<mlist.size();i++){
			Model_App item=mlist.get(i);
			String pkg=item.pkgName;
			isCheck[i]=false;
			for(String p : applist){
				if(pkg.equals(p)){
					isCheck[i]=true;
				}
			}
		}
//		for(int i = 0;i<isCheck.length;i++){
//			isCheck[i]=false;
//		}
	}
    public void ToggleItem(int pos){
    	if(isCheck[pos]){
    		isCheck[pos]=false;
    	}else{
    		isCheck[pos]=true;
    	}
    	SaveAppStatus(pos,isCheck[pos]);
    	notifyDataSetChanged();
    }
    private void SaveAppStatus(int pos,boolean isSave){
    	Model_App app=mlist.get(pos);
    	if(isSave){
    		//db.SaveApp(app.pkgName, Tag);
    	}else{
    	//	db.delApp(app.pkgName, Tag);
    	}
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder holder = null;
		Model_App item = mlist.get(position);
		// IndexList item = mlist.get(position);
		if (null == convertView) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.allapp_appgrid_item, null);
			holder.image = (ImageView) convertView
					.findViewById(R.id.allapp_appico);
			holder.ico = (ImageView) convertView
					.findViewById(R.id.allapp_ico);
			holder.text = (TextView) convertView
					.findViewById(R.id.allapp_appname);

			convertView.setTag(holder);

			//
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		holder.text.setText(item.AppName);
		holder.image.setBackgroundDrawable(item.icon);
        if(isCheck[position]){
        	holder.ico.setVisibility(View.VISIBLE);
        }else{
        	holder.ico.setVisibility(View.GONE);
        }
		return convertView;

	}

	class Holder {
		ImageView image,ico;
		TextView text;
	}

}