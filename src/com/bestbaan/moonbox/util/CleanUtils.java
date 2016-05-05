package com.bestbaan.moonbox.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class CleanUtils {
	
	
	/**
	 * (/data/data/com.xxx.xxx/cache)
	 * @param ctx
	 */
	public static void cleanInternalCache(Context ctx) {
		deleteFilesByDirectory(ctx.getCacheDir());
	}
	
	public static void cleanFiles(Context ctx) {
		deleteFilesByDirectory(ctx.getFilesDir());
	}
	
	public static void cleanApplicationData(Context ctx) {
		cleanInternalCache(ctx);
		cleanFiles(ctx);
	}

	private static void deleteFilesByDirectory(File directory) {
		if((directory != null) && directory.exists() && directory.isDirectory()) {
			for(File item : directory.listFiles()) {
				item.delete();
			}
		}
	}
	
	/**
	 * (/data/data/com.xxx.xxx/databases)
	 * 
	 * @param context
	 * @param dbName
	 */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}
	
	/**
	 * (/data/data/com.xxx.xxx/shared_prefs)
	 * 
	 * @param context
	 */
	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/"+ context.getPackageName() + "/shared_prefs"));
	}
	
	/**
	 * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
	 * 
	 * @param context
	 */
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	/**
	 * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
	 * 
	 * @param filePath
	 */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}

	/**
	 * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
	 * 
	 * @param context
	 */
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}
	
	/**
	 * 清除本应用所有的数据
	 * 
	 * @param context
	 * @param filepath
	 */
	public static void cleanApplicationData(Context context, String... filepath) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
		for (String filePath : filepath) {
			cleanCustomCache(filePath);
		}
	}
}
