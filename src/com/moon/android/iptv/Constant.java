package com.moon.android.iptv;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bestbaan.moonbox.model.CountryItem;
import com.bestbaan.moonbox.util.AppUtils;
import com.bestbaan.moonbox.util.CustomAppInfo;
import com.moonbox.android.iptv.R;

public class Constant {

	private static final String SYS_DOWNLOAD = "com.android.providers.downloads.ui";
	private static final String SYS_DOWNLOAD_AC = "com.android.providers.downloads.ui.DownloadList";

	private static final String SYS_BROWSER = "com.android.browser";
	private static final String SYS_BROWSER_AC = "com.android.browser.BrowserActivity";
	private static final String SYS_SETTING = "com.android.tv.settings";
	private static final String SYS_SETTING_AC = "com.android.tv.settings.MainSettings";
	private static final String SYS_FILE_BROWSER = "com.droidlogic.FileBrower";
	private static final String SYS_FILE_BROWSER_AC = "com.droidlogic.FileBrower.FileBrower";

	private static String SYS_VIDEO = "com.droidlogic.videoplayer";
	private static String SYS_VIDEO_AC = "com.droidlogic.videoplayer.FileList";

	// private static final String SYS_PIC = "com.amlogic.PicturePlayer";
	private static String SYS_PIC = "com.android.gallery3d";
	// private static final String SYS_PIC_AC =
	// "org.geometerplus.android.fbreader.FBReader";
	private static String SYS_PIC_AC = "com.android.gallery3d.app.GalleryActivity";

	private static final String SYS_MUSIC = "org.geometerplus.zlibrary.ui.android";
	private static final String SYS_MUSIC_AC = "org.geometerplus.android.fbreader.FBReader";

	// private static final String DLNA = "com.amlogic.mediacenter";
	// private static final String DLNA_AC =
	// "com.amlogic.mediacenter.AmlMediaCenter";

	private static final String MEDIA_CENTER = "com.amlogic.mediacenter";
	private static final String MEDIA_CENTER_AC = "com.amlogic.mediacenter.MediaCenterActivity";

	private static final String APP_STORE = "com.android.vending";
	private static final String APP_STORE_AC = "com.android.vending.AssetBrowserActivity";

	private static final String MOON_APP_STORE = "com.moon.appstore";
	private static final String MOON_APP_STORE_AC = "com.moon.appstore.WelcomeActivity";
	// MX
	private static final String SYS_MUSIC_MX = "com.android.music";
	private static final String SYS_MUSIC_MX_AC = "com.android.music.MusicBrowserActivity";
	public static final String UPGRADE = "com.example.Upgrade";
	private static final String UPGRADE_AC = "com.example.Upgrade.UpgradeActivity";

	// private static final String BOX_SETTING = "com.aml.settings";
	private static final String BOX_SETTING = "com.mbx.settingsmbox";
	// private static final String BOX_SETTING_AC =
	// "com.aml.settings.PreferenceWithHeaders";
	private static final String BOX_SETTING_AC = "com.mbx.settingsmbox.SettingsMboxActivity";

	public static List<CustomAppInfo> getSysApp(Context context) {
		List<CustomAppInfo> list = new ArrayList<CustomAppInfo>();

		if (Configs.getType() == Configs.BOX_TYPE_M3) {
			SYS_PIC = "com.amlogic.PicturePlayer";
			SYS_PIC_AC = "org.geometerplus.android.fbreader.FBReader";

			SYS_VIDEO = "com.farcore.videoplayer";
			SYS_VIDEO_AC = "com.farcore.videoplayer.FileList";
		}

		if (AppUtils.isAppInstalled(context, SYS_SETTING)) {
			list.add(new CustomAppInfo(getString(context, R.string.setting),
					SYS_SETTING, SYS_SETTING_AC, getDrawable(context,
							R.drawable.app_setting), true));
		}

		if (AppUtils.isAppInstalled(context, SYS_FILE_BROWSER)) {
			list.add(new CustomAppInfo(getString(context, R.string.file),
					SYS_FILE_BROWSER, SYS_FILE_BROWSER_AC, getDrawable(context,
							R.drawable.app_file), true));
		}

		if (AppUtils.isAppInstalled(context, SYS_VIDEO)) {
			list.add(new CustomAppInfo(getString(context, R.string.video),
					SYS_VIDEO, SYS_VIDEO_AC, getDrawable(context,
							R.drawable.app_video), true));
		}

		if (AppUtils.isAppInstalled(context, SYS_PIC)) {
			list.add(new CustomAppInfo(getString(context, R.string.gallery),
					SYS_PIC, SYS_PIC_AC, getDrawable(context,
							R.drawable.app_pic), true));
		}
		
		String musicPkg = null;
		String musicPkgAc = null;

		if (AppUtils.isAppInstalled(context, SYS_MUSIC)) {
			musicPkg = SYS_MUSIC;
			musicPkgAc = SYS_MUSIC_AC;
		} else if (AppUtils.isAppInstalled(context, SYS_MUSIC_MX)) {
			musicPkg = SYS_MUSIC_MX;
			musicPkgAc = SYS_MUSIC_MX_AC;
		}

		if (null != musicPkg) {
			list.add(new CustomAppInfo(getString(context, R.string.music),
					musicPkg, musicPkgAc, getDrawable(context,
							R.drawable.app_music), true));
		}

		if (AppUtils.isAppInstalled(context, SYS_BROWSER)) {
			list.add(new CustomAppInfo(getString(context, R.string.broswer),
					SYS_BROWSER, SYS_BROWSER_AC, getDrawable(context,
							R.drawable.app_browser), true));
		} 
		list.add(new CustomAppInfo("Wi-Fi",
				"com.android.settings.wifi", "com.android.settings.wifi.WifiSetupActivity", getDrawable(context,
						R.drawable.wifi), true));
		if (AppUtils.isAppInstalled(context, SYS_DOWNLOAD)) {
			list.add(new CustomAppInfo(getString(context, R.string.download),
					SYS_DOWNLOAD, SYS_DOWNLOAD_AC, getDrawable(context,
							R.drawable.app_download), true));
		}
    
		if (AppUtils.isAppInstalled(context, APP_STORE)) {
			list.add(new CustomAppInfo(
					getString(context, R.string.google_play), APP_STORE,
					APP_STORE_AC, getDrawable(context, R.drawable.googleplay),
					true));
		}

		if (AppUtils.isAppInstalled(context, UPGRADE)) {
			list.add(new CustomAppInfo(getString(context, R.string.upgrade),
					UPGRADE, UPGRADE_AC, getDrawable(context,
							R.drawable.icon_upgrade), true));
		}

		if (AppUtils.isAppInstalled(context, BOX_SETTING)) {
			list.add(new CustomAppInfo(
					getString(context, R.string.box_setting), BOX_SETTING,
					BOX_SETTING_AC, getDrawable(context,
							R.drawable.setting_icon), true));
		}

		if (AppUtils.isAppInstalled(context, MOON_APP_STORE)) {
			list.add(new CustomAppInfo(getString(context, R.string.appstore2),
					MOON_APP_STORE, MOON_APP_STORE_AC, getDrawable(context,
							R.drawable.icon_appstore), true));
		}

		/*if (AppUtils.isAppInstalled(context, MEDIA_CENTER)) {
			list.add(new CustomAppInfo(
					getString(context, R.string.media_center), MEDIA_CENTER,
					MEDIA_CENTER_AC, getDrawable(context,
							R.drawable.icon_media_center), true));
		}*/
//        for(int i=0;i<list.size();i++){
//        	CustomAppInfo now=list.get(i);
//        	Log.d("pkg",now.pkgName+"--"+now.title+"");
//        }
		return list;
	}

	public static List<CountryItem> getCountrys() {
		List<CountryItem> list = new ArrayList<CountryItem>();
		list.add(new CountryItem(R.drawable.icon_tibet, R.string.Vietnamese,
				new Locale("vi", "VN")));
		list.add(new CountryItem(R.drawable.icon_china,
				R.string.chinese_simple, Locale.SIMPLIFIED_CHINESE));
		list.add(new CountryItem(R.drawable.icon_english, R.string.english,
				Locale.ENGLISH));
		list.add(new CountryItem(R.drawable.icon_hongkong,
				R.string.Chinese_traditional, Locale.TRADITIONAL_CHINESE));
		// list.add(new
		// CountryItem(R.drawable.icon_japan,R.string.japan,Locale.JAPANESE));
		list.add(new CountryItem(R.drawable.icon_france, R.string.france,
				Locale.FRANCE));
		return list;
	}

	private static Drawable getDrawable(Context context, int resId) {
		return context.getResources().getDrawable(resId);
	}

	private static String getString(Context context, int resId) {
		return context.getResources().getString(resId);
	}

}
