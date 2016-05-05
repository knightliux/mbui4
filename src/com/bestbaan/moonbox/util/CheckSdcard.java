package com.bestbaan.moonbox.util;

import android.content.Context;
import android.os.storage.StorageManager;

public class CheckSdcard {

	public static boolean isHasExtendStorage(Context context) {
		StorageManager sm = (StorageManager) context
				.getSystemService(Context.STORAGE_SERVICE);
		int sdnum = 0;
		try {
			String[] paths = (String[]) sm.getClass()
					.getMethod("getVolumePaths", null).invoke(sm, null);
			for (int i = 0; i < paths.length; i++) {
				String status = (String) sm.getClass()
						.getMethod("getVolumeState", String.class)
						.invoke(sm, paths[i]);
				if (status.equals(android.os.Environment.MEDIA_MOUNTED)) {
					sdnum++;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (sdnum > 1)
			return true;
		return false;
	}
}
