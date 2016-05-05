package com.bestbaan.moonbox.view;

import java.util.ArrayList;
import java.util.List;

import com.bestbaan.moonbox.adapter.LifeGridAdapter;
import com.bestbaan.moonbox.model.LifeMenu;


import com.bestbaan.moonbox.util.AppUtils;
import com.bestbaan.moonbox.util.CustomAppInfo;
import com.bestbaan.moonbox.view.AppGrid.AppsAdapter;
import com.bestbaan.moonbox.view.AppGrid.AppsAdapter.Holder;
import com.moon.android.iptv.Configs;
import com.moon.android.iptv.Constant;
import com.moonbox.android.iptv.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CopyOfLifeGridBAK extends LinearLayout implements OnKeyListener{
	private Context mContext;
    private List<HoverBigLayout> mLayout;
    public List<View> mMenuView;
    public List<LifeMenu> menulist;
    public GridView mGrid;
    public int menuId[] = { R.id.menu_0, R.id.menu_1, R.id.menu_2, R.id.menu_3,};


	public CopyOfLifeGridBAK(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		// TODO Auto-generated constructor stub
		View view = LayoutInflater.from(context).inflate(R.layout.life_view, this);
		initwidget();
	//	init();
	}




	private void initwidget() {
		// TODO Auto-generated method stub
		mGrid=(GridView) findViewById(R.id.main_grid);
		menulist=new ArrayList<LifeMenu>();
		menulist.add(new LifeMenu(R.drawable.ad_default, 0, "0000", "0"));
		menulist.add(new LifeMenu(R.drawable.ad_bg_focus_2, 0, "1111", "1"));

	
		mGrid.setAdapter(new LifeGridAdapter(mContext,menulist));
	}




	private void init() {
		// TODO Auto-generated method stub
		List<LifeMenu> list = new ArrayList<LifeMenu>();
		list.add(new LifeMenu(R.drawable.ad_default, 0, "0000", "0"));
		list.add(new LifeMenu(R.drawable.msg_dialog, 0, "1111", "1"));
		list.add(new LifeMenu(R.drawable.ad_default, 0, "2222", "2"));
		list.add(new LifeMenu(R.drawable.msg_dialog, 0, "3333", "3"));
		menulist=list;
		mMenuView = new ArrayList<View>();
		for (int i = 0; i < menuId.length; i++) {
			mMenuView.add(findViewById(menuId[i]));
		}
		fillMenu(menulist);
		
	}
	private void fillMenu(List<LifeMenu> menulist2) {
		// TODO Auto-generated method stub
		for (int i = 0; i < menulist2.size(); i++) {
			LifeMenu item = menulist2.get(i);	
			View vItem = mMenuView.get(i);
			FillView(vItem,item);
			//vItem.setOnClickListener(MenuClick);
		}
	}
	private void FillView(View v, LifeMenu item) {
		// TODO Auto-generated method stub
	
	    View vItem=v;
		ImageView img = (ImageView) vItem.findViewById(R.id.img);
		ImageView hover = (ImageView) vItem.findViewById(R.id.hover);
		TextView tv = (TextView) vItem.findViewById(R.id.text);
		tv.setText(item.getName());
		tv.setTag(item);
		if(item.getImgRes()!=0)img.setBackgroundResource(item.getImgRes());
        if(item.getHoverImgRes()!=0)hover.setBackgroundResource(item.getHoverImgRes());
		vItem.setVisibility(View.VISIBLE);
	}



	public CopyOfLifeGridBAK(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}




	public CopyOfLifeGridBAK(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}




	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

	
	
	

         
}
