package com.moon.android.iptv;

import com.moonbox.android.iptv.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class LifeWebActivity extends Activity {
	private WebView mWeb;
	private String url;
	private ImageView mImgLoad;
	private AnimationDrawable anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_life_web);
		initweiget();
		
		init();
		//
	}

	private void init() {
		// TODO Auto-generated method stub
		Bundle bundle = LifeWebActivity.this.getIntent().getExtras();
		url = bundle.getString("url");
		Log.d("loadurl", url);
		mWeb.getSettings().setJavaScriptEnabled(true);
		mWeb.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				mWeb.loadUrl("file:///android_asset/error.html");
				//
				// super.onReceivedError(view, errorCode, description,
				// "file:///android_asset/error.html");
			}

			// 这个方法在用户试图点开页面上的某个链接时被调用
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url != null) {
					// 如果想继续加载目标页面则调用下面的语句
					view.loadUrl(url);
					// 如果不想那url就是目标网址，如果想获取目标网页的内容那你可以用HTTP的API把网页扒下来。
				}
				// 返回true表示停留在本WebView（不跳转到系统的浏览器）
				return true;
			}
            
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				
				anim.stop();
				mImgLoad.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				
				anim.start();
				mImgLoad.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				// TODO Auto-generated method stub
				anim.stop();
				mImgLoad.setVisibility(View.GONE);
				super.onLoadResource(view, url);
			}

			
		});
		mWeb.loadUrl(url);
		// mWeb.loadUrl("file:///android_asset/error.html");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mWeb.canGoBack()) {
			mWeb.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initweiget() {
		// TODO Auto-generated method stub
		mWeb = (WebView) findViewById(R.id.life_web);
		mImgLoad = (ImageView) findViewById(R.id.life_web_loadimg);
		anim = (AnimationDrawable) mImgLoad.getBackground();

	}
}
