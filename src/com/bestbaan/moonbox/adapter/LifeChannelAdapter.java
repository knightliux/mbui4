package com.bestbaan.moonbox.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bestbaan.moonbox.model.LifeModel;
import com.bestbaan.moonbox.util.DbUtil;
import com.bestbaan.moonbox.util.Logger;
import com.bestbaan.moonbox.util.AppUtils.AppInfo;
import com.moonbox.android.iptv.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LifeChannelAdapter extends BaseAdapter<LifeModel> {
	  



	Animation shake = null;
	Animation small = null;
	boolean canShake=true;
	int currentLongClickItem;
	int currentClickItem;
	int currentSelectItem=-1;
	private Logger logger=Logger.getInstance();
	private List<LifeModel> mListApps;
	private LayoutInflater mInflater;
	private DisplayImageOptions options;
	public boolean initView=true;
	public boolean isFocus=false;
	public LifeChannelAdapter(Context context, List<LifeModel> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
		initoptions();
		shake = AnimationUtils.loadAnimation(context, R.anim.anim_scale_big);
		small = AnimationUtils.loadAnimation(context, R.anim.anim_scale_small);
		mListApps = list;
		mInflater = LayoutInflater.from(context);
	
	}
	private void initoptions() {
		// TODO Auto-generated method stub
		options=new DisplayImageOptions.Builder()  
  
        .cacheInMemory(true)  
        .cacheOnDisk(true)  
        .bitmapConfig(Bitmap.Config.RGB_565)  
        .build();  
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Holder holder = null;
		if(null == convertView){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.life_grid_item, null);
			holder.bg_img = (ImageView) convertView.findViewById(R.id.life_item_bg);
			holder.Name = (TextView) convertView.findViewById(R.id.life_item_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		LifeModel item=mList.get(position);

		//		AppInfo appInfo = mListApps.get(position);
//		holder.appIcon.setImageDrawable(appInfo.getAppIcon());
//		holder.appName.setText(appInfo.getAppName());
//		holder.bg_img.setBackground(mContext.getResources().getDrawable(R.drawable.ad_default));
	
		if(initView){ 
			holder.Name.setText(item.getName());
			ImageLoader.getInstance().displayImage(item.getImg(), holder.bg_img,options); 
		}
		 
//		logger.i(currentLongClickItem+"  "+position+"  "+canShake);
		if(isFocus){
			if(currentSelectItem==position){
				holder.Name.setVisibility(View.VISIBLE);
			}else{
				holder.Name.setVisibility(View.GONE);
			}
		}else{
			holder.Name.setVisibility(View.GONE);
		}
		
		
		return convertView;
	}
	
	static class Holder {
		ImageView bg_img;
		TextView Name;
		
	}

	
   public void notifyDataClick(int position){
	    this.currentClickItem=position; 
	    this.canShake=false;

		super.notifyDataSetChanged();
   }
	/**
	 * @param position 位置
	 * @param canShake 能否抖动
	 * 当长按Item的时候，canShake=true,那个Item就
	 * 会抖动起来,当返回键按下时canShake=false,就会
	 * 停止抖动
	 */
	public void initOver(){
		this.initView=false;
	}
	public void FocusChanged(){
		this.isFocus=true;
		super.notifyDataSetChanged();
	}
	public void notifyFocusChanged(boolean isfocus){
		this.isFocus=isfocus;
		super.notifyDataSetChanged();
	}
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
