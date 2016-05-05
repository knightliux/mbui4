package com.bestbaan.moonbox.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestbaan.moonbox.util.AppUtils.AppInfo;
import com.moonbox.android.iptv.R;

public class DeskAppMgrAdapter extends BaseAdapter<AppInfo> {
	
	Animation shake = null;
	boolean canShake=false;
	int currentLongClickItem;
	int currentSelectItem;
	
	public DeskAppMgrAdapter(Context context, List<AppInfo> list) {
		super(context, list);
		shake = AnimationUtils.loadAnimation(context, R.anim.anim_shake);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.popup_mgr_item, null);
			holder=new ViewHolder();
			initHolder(holder,convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		initHolderData(holder,position);
		
		return convertView;
	}
	
	private void initHolderData(ViewHolder holder, int position) {
		AppInfo appInfo = mList.get(position);
//		holder.appPosition.setText((position+1)+"");
//		TextPaint tp = holder.appPosition.getPaint();
//		tp.setFakeBoldText(true);
		holder.appLogo.setImageDrawable(appInfo.getAppIcon());
		if(currentSelectItem==position){
			holder.appLabel.setText(appInfo.getAppName());
		}else{
			holder.appLabel.setText((position+1)+"");
		}
		
		if(currentLongClickItem==position){
			if(canShake==true){
				shake.reset();
				shake.setFillAfter(true);
				holder.appLogo.startAnimation(shake);
			}
			else{
				holder.appLogo.clearAnimation();
			}
		}else{
			holder.appLogo.clearAnimation();
		}
		
	}

	private void initHolder(ViewHolder holder, View view) {
//		holder.appPosition=(TextView)view.findViewById(R.id.mgr_position);
		holder.appLogo=(ImageView)view.findViewById(R.id.mgr_app_logo);
		holder.appLabel=(TextView)view.findViewById(R.id.mgr_app_label);
	}

	static class ViewHolder{
		ImageView appLogo;
		TextView appLabel;
		TextView appPosition;
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
