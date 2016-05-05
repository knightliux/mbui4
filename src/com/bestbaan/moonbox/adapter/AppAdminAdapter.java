package com.bestbaan.moonbox.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestbaan.moonbox.model.LifeMenu;
import com.bestbaan.moonbox.util.CustomAppInfo;
import com.moonbox.android.iptv.R;

public class AppAdminAdapter extends BaseAdapter<LifeMenu> {

	private int mCurrentClickPosition=-1;
	private boolean mIsToDesked=false;
	private int p = 0;
	public AppAdminAdapter(Context context, List<LifeMenu> list) {
		super(context, list);
	}
	public void setNotifyDataChange(int id) {
	
		super.notifyDataSetChanged();
	}
	public void setCurrentPosition(int pos) {
		// 刷新adapter前，在activity中调用这句传入当前选中的item在屏幕中的次序
		this.p = pos;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(null == convertView){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.life_grid_item, null);
			initHolder(holder,convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		//convertView.setLayoutParams(new GridView.LayoutParams(150, 210));
		initHolderData(holder,position);
		return convertView;
	}
	
	private void initHolderData(Holder holder, int position) {
		LifeMenu item=mList.get(position);
		
	//	holder.life_text.setText(item.getName());
//		CustomAppInfo appInfo = mList.get(position);
//		int toDeskBtnText = appInfo.isDesktop ? R.string.cancel_to_desktp : R.string.to_desktp;
//		
//		holder.appIcon.setImageDrawable(appInfo.icon);
//		holder.appName.setText(appInfo.title);
//		holder.version.setText(appInfo.versionName);
//		if(mCurrentClickPosition!=-1){
//			if(mCurrentClickPosition==position){
//				if(mIsToDesked==true){
//					holder.toDeskTop.setText(R.string.cancel_to_desktp);
//				}else{
//					holder.toDeskTop.setText(R.string.to_desktp);
//				}
//			}
//		}else{
//			holder.toDeskTop.setText(toDeskBtnText);
//		}
//		
//		holder.toDeskTop.setTag(appInfo);
	}

	private void initHolder(Holder holder, View convertView) {
		//holder.life_text=(TextView) convertView.findViewById(R.id.life_text);
//		holder.appIcon = (ImageView) convertView.findViewById(R.id.app_item_imageview);
//		holder.appName = (TextView) convertView.findViewById(R.id.app_item_name);
//		holder.toDeskTop = (Button) convertView.findViewById(R.id.app_mamager_item_btn_toindex);
//		holder.version = (TextView) convertView.findViewById(R.id.app_item_version);
	}

	public void notifyDataSetChanged(int position,boolean isInDesk) {
		mCurrentClickPosition=position;
		mIsToDesked=isInDesk;
		super.notifyDataSetChanged();
	}



	static class Holder {
		ImageView appIcon;
		TextView life_text;
		Button toDeskTop;
		TextView version;
	}
}
