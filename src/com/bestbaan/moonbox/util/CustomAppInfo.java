/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bestbaan.moonbox.util;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Represents a launchable application. An application is made of a name (or
 * title), an intent and an icon.
 */
public class CustomAppInfo {
	/**
	 * The application name.
	 */
	public CharSequence title;

	/**
	 * The intent used to start the application.
	 */
	public Intent intent;

	/**
	 * The application icon.
	 */
	public Drawable icon;

	/**
	 * When set to true, indicates that the icon has been resized.
	 */
	public boolean filtered;

	public String pkgName;

	public String activityInfoName;

	public boolean isDesktop;

	public String versionName;

	public int versionCode;

	public Long firstInstallTime;

	public Long lastUpdateTime;

	public boolean isSystemApp;

	public CustomAppInfo() {
	}

	public CustomAppInfo(CharSequence title, String pkg, String actvityName,
			Drawable icon, boolean isSystemApp) {
		super();
		this.title = title;
		this.icon = icon;
		this.isSystemApp = isSystemApp;
		this.pkgName=pkg;
		ComponentName c = new ComponentName(pkg, actvityName);

		setActivity(c, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	}

	/**
	 * Creates the application intent based on a component name and various
	 * launch flags.
	 * 
	 * @param className
	 *            the class name of the component representing the intent
	 * @param launchFlags
	 *            the launch flags
	 */
	final void setActivity(ComponentName className, int launchFlags) {
		intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(className);
		intent.setFlags(launchFlags);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CustomAppInfo)) {
			return false;
		}

		CustomAppInfo that = (CustomAppInfo) o;
		return title.equals(that.title)
				&& intent.getComponent().getClassName()
						.equals(that.intent.getComponent().getClassName());
	}

	@Override
	public int hashCode() {
		int result;
		result = (title != null ? title.hashCode() : 0);
		final String name = intent.getComponent().getClassName();
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "CustomAppInfo [title=" + title + ", intent=" + intent
				+ ", pkgName=" + pkgName + ", activityInfoName="
				+ activityInfoName + ", isDesktop=" + isDesktop
				+ ", isSystemApp=" + isSystemApp + "]";
	}
	
}
