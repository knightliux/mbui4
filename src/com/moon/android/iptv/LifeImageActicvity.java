package com.moon.android.iptv;

import java.util.ArrayList;
import java.util.List;





import com.bestbaan.moonbox.model.DetailImgModel;
import com.bestbaan.moonbox.util.RequestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moonbox.android.iptv.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

//public class LifeImageActicvity extends Activity {
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_life_image);
//	}
//}

public class LifeImageActicvity extends Activity implements
		OnPageChangeListener {
	private ViewGroup mGroup;
	private ViewPager mViewPager;

	/**
	 * 装导航圆点的ImageView数组
	 */
	private ImageView[] tips;

	/**
	 * 装ImageView数组
	 */
	private ImageView[] mImageViews;

	/**
	 * 图片资源id
	 */
	private int[] imgIdArray;
	private String Pid;

	public ImageView mLeftImg, mRightImg,mErrorImg;
	public List<DetailImgModel> Imglist = new ArrayList<DetailImgModel>();
	public final int GET_BANNER_ERROE = 1;
	public final static int ShowDetailImg=100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_life_image);
		initWidget();
//		Configs.Order.NowProduct = null;
//		Configs.Order.NowUserAddr = null;
		Bundle bundle = LifeImageActicvity.this.getIntent().getExtras();
		Pid = bundle.getString("id");
		initParam();
		GetImgByPid(Pid);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		// initParam();
		super.onStart();
	}

	private void initParam() {
		// TODO Auto-generated method stub
		InitProductModel();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initParam();
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// log.d(""+keyCode+"---"+keyCode);
	
		return super.onKeyDown(keyCode, event);
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		mGroup = (ViewGroup) findViewById(R.id.viewGroup);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mLeftImg = (ImageView) findViewById(R.id.left);
		mRightImg = (ImageView) findViewById(R.id.right);
		mErrorImg=(ImageView) findViewById(R.id.load_error);
	}

	private void initData() {
		// TODO Auto-generated method stub
		// 载入图片资源ID
		//
		// imgIdArray = new int[] { R.drawable.menu_0, R.drawable.menu_1
		// };

		tips = new ImageView[Imglist.size()];
		for (int i = 0; i < Imglist.size(); i++) {
			ImageView imageView = new ImageView(this);
			// LayoutParams layout=new LayoutParams(20, 20);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 20);
			lp.setMargins(10, 0, 0, 0);
			imageView.setLayoutParams(lp);

			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}

			mGroup.addView(imageView);
		}

		// 将图片装载到数组中
		mImageViews = new ImageView[Imglist.size()];
		for (int i = 0; i < Imglist.size(); i++) {
			ImageView imageView = new ImageView(this);
			mImageViews[i] = imageView;
			ImageLoader.getInstance().displayImage(Imglist.get(i).getUrl(),
					imageView);
			// imageView.setBackgroundResource(imgIdArray[i]);
		}
//		log.d("---imgNunber---" + mImageViews.length);
		// 设置Adapter
		mViewPager.setAdapter(new MyAdapter());
		// 设置监听，主要是设置点点的背景
		mViewPager.setOnPageChangeListener(this);
		// 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
		// mViewPager.setCurrentItem(4);

	}

	private void InitProductModel() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				try {
//					String res = RequestUtil.getInstance().request(
//							Configs.Url.Prouduct + "?Pid=" + Pid);
					//ProductModel p = null;
//					p = new Gson().fromJson(res, new TypeToken<ProductModel>() {
//					}.getType());
//					if (p != null) {
//						Configs.Order.NowProduct = p;

//					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			};
		}.start();
	}

	private void GetImgByPid(final String pid2) {
		// TODO Auto-generated method stub

		new Thread() {
			public void run() {
				try {
					String res = RequestUtil.getInstance().request(
							Configs.GetlifeImgList() + "&id=" + pid2);
//					Log.d("res",res);
					Imglist = new Gson().fromJson(res,
							new TypeToken<List<DetailImgModel>>() {
							}.getType());

//					log.d(Imglist.size() + "");
					if (Imglist.size() > 0) {
						Message message = new Message();
						message.what = ShowDetailImg;
						mHandler.sendMessage(message);
					} else {
						Message message = new Message();
						message.what = GET_BANNER_ERROE;
						mHandler.sendMessage(message);

					}
				} catch (Exception e) {
					Message message = new Message();
					message.what = GET_BANNER_ERROE;
					mHandler.sendMessage(message);

				}

			}
		}.start();
	}

	public Handler mHandler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ShowDetailImg:
				initData();
				break;
			case GET_BANNER_ERROE:
				mRightImg.setVisibility(View.GONE);
		
				mErrorImg.setBackground(getResources().getDrawable(R.drawable.bg_load_error));
				mErrorImg.setVisibility(View.VISIBLE);
				Toast.makeText(
						LifeImageActicvity.this,
						LifeImageActicvity.this.getResources().getString(
								R.string.get_banner_error), Toast.LENGTH_LONG)
						.show();
			
				break;
			}
		}
	};

	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			return mImageViews.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mImageViews[position]);

		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			// log.d("instantiateImg"+position);
			((ViewPager) container).addView(mImageViews[position], 0);
			return mImageViews[position];
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setImageBackground(arg0);
		// log.d("nowPage:"+arg0);
		if (arg0 == 0) {
			mLeftImg.setVisibility(View.GONE);
		} else {
			mLeftImg.setVisibility(View.VISIBLE);
		}
		if (arg0 == mImageViews.length - 1) {
			mRightImg.setVisibility(View.GONE);
		} else {
			mRightImg.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}
}