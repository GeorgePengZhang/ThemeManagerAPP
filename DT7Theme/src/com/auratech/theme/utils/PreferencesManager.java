package com.auratech.theme.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

public class PreferencesManager {
	
	private static final String THEMES = "themes";
	private static final String THEME_KEY = "theme_key";
	private static final String TAG = "PreferencesManager";
	
	private SharedPreferences mSharedPreferences;
	
	private static PreferencesManager mPreferencesManager;
	
	private PreferencesManager(Context context) {
		Context packageContext = null;
		
		try {
			packageContext = context.createPackageContext("com.auratech.theme", Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} 
		if (packageContext != null) {
			mSharedPreferences = packageContext.getSharedPreferences(THEMES, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE | Context.MODE_MULTI_PROCESS);
		}
	}
	
	public static PreferencesManager getInstance(Context context) {
		if (mPreferencesManager == null) {
			synchronized (PreferencesManager.class) {
				if (mPreferencesManager == null) {
					mPreferencesManager = new PreferencesManager(context.getApplicationContext());
				}
			}
		}
		
		return mPreferencesManager;
	}

	public String getThemeKey() {
		if (mSharedPreferences == null) {
			return "";
		}
		
		String themekey = mSharedPreferences.getString(THEME_KEY, "");
		if (TextUtils.isEmpty(themekey)) {
			themekey = ThemeResouceManager.THEME_DEFAULT_PATH+ThemeResouceManager.THEME_DEAFULT_NAME;
		}
	
		return themekey;
	}
	
	public void setThemeKey(String theme) {
		if (mSharedPreferences != null) {
			Editor editor = mSharedPreferences.edit();
			editor.putString(THEME_KEY, theme);
			editor.apply();
		}
	}
}
