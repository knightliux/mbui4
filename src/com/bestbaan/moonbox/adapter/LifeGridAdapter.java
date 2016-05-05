package com.bestbaan.moonbox.adapter;

import java.util.List;

import com.bestbaan.moonbox.adapter.AppAdminAdapter.Holder;
import com.bestbaan.moonbox.adapter.DeskAppMgrAdapter.ViewHolder;
import com.bestbaan.moonbox.model.LifeMenu;
import com.bestbaan.moonbox.util.CustomAppInfo;
import com.bestbaan.moonbox.util.AppUtils.AppInfo;

import com.moonbox.android.iptv.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LifeGridAdapter extends BaseAdapter {
	private List<LifeMenu> mlist;
	private LayoutInflater mInflater;
//	private DisplayImageOptions mOptions;


	public LifeGridAdapter(Context context, List<LifeMenu> list) {
		mlist = list;
		mInflater = LayoutInflater.from(context);
	//	initDisplayImageOptions();

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

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder holder = null;
		//IndexList item = mlist.get(position);
		if (null == convertView) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.life_grid_item, null);
			//holder.image = (ImageView) convertView.findViewById(R.id.index_img);
		//	holder.text = (TextView) convertView.findViewById(R.id.index_text);

			convertView.setTag(holder);

			//
		} else {
			holder = (Holder) convertView.getTag();
		}
	//	holder.text.setText(item.getName());

		// holder.image.setImageBitmap(ImgUtil.GetBitMap(item.getIco()));
		// holder.image.set
		// holder.image.setBackgroundResource(IndexList.get());

		return convertView;

	}

	class Holder {
		ImageView image;
		TextView text;
	}



}