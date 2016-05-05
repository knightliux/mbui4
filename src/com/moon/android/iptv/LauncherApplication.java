package com.moon.android.iptv;

import com.bestbaan.moonbox.model.UpdateData;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

public class LauncherApplication extends Application{
	public static final String TAG = "LauncherApplication";
	private static LauncherApplication application;
	public static UpdateData updateData;

	/**
	 * current position of AppInfo witch remove from desk 
	 */
	public static int position=0;
	
	public static LauncherApplication getApplication() {
		return application;
	}
	public static void initImageLoader(Context context) {
		//
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		application = this;
	}
	
	
	
}
