package com.bestbaan.moonbox.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestbaan.moonbox.util.Logger;
import com.bestbaan.moonbox.util.AppUtils.AppInfo;

import com.moonbox.android.iptv.R;

public class CopyOfIndexAppAdapterbak extends BaseAdapter<AppInfo> {
	  



	Animation shake = null;
	boolean canShake=false;
	int currentLongClickItem;
	int currentSelectItem;
	private Logger logger=Logger.getInstance();
	private List<AppInfo> mListApps;
	private LayoutInflater mInflater;
    
	public CopyOfIndexAppAdapterbak(Context context, List<AppInfo> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
		shake = AnimationUtils.loadAnimation(context, R.anim.anim_shake);
		mListApps = list;
		mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return mListApps.size();
	}

	@Override
	public Object getItem(int position) {
		return mListApps.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Holder holder = null;
		if(null == convertView){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.app_item, null);
			holder.appIcon = (ImageView) convertView.findViewById(R.id.app_item_imageview);
			holder.appName = (TextView) convertView.findViewById(R.id.app_item_name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		AppInfo appInfo = mListApps.get(position);
		holder.appIcon.setImageDrawable(appInfo.getAppIcon());
		holder.appName.setText(appInfo.getAppName());
		
		logger.i(currentLongClickItem+"  "+position+"  "+canShake);
		if(currentLongClickItem==position){
			if(canShake==true){
				shake.reset();
				shake.setFillAfter(true);
				holder.appIcon.startAnimation(shake);
			}
			else{
				holder.appIcon.clearAnimation();
			}
		}else{
			holder.appIcon.clearAnimation();
		}
		
		return convertView;
	}
	
	static class Holder {
		ImageView appIcon;
		TextView appName;
	}

	/**
	 * @param position 位置
	 * @param canShake 能否抖动
	 * 当长按Item的时候，canShake=true,那个Item就
	 * 会抖动起来,当返回键按下时canShake=false,就会
	 * 停止抖动
	 */
	
	public void notifyDataSetChanged(int position,boolean canShake) {
		currentLongClickItem=position;
		this.canShake=canShake;
		super.notifyDataSetChanged();
	}
	public void notifyDataSetChanged(int position) {
		currentSelectItem=position;
		super.notifyDataSetChanged();
	}
}
