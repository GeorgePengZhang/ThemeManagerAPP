package com.auratech.theme.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;

public class PreferencesManager {
	
	private static final String THEMES = "themes";
	private static final String THEME_KEY = "theme_key";
	private static final String TAG = "PreferencesManager";
	
	private SharedPreferences mSharedPreferences;
	private Context mThemeContext;
	
	private static PreferencesManager mPreferencesManager;
	
	private static final String PREFERENCE_PACKAGE_NAME = "com.auratech.system";
	
	private PreferencesManager(Context context) {
		try {
			mThemeContext = context.createPackageContext(PREFERENCE_PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} 
		if (mThemeContext != null) {
			mSharedPreferences = mThemeContext.getSharedPreferences(THEMES, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE | Context.MODE_MULTI_PROCESS);
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
		mSharedPreferences = mThemeContext.getSharedPreferences(THEMES, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
		if (mSharedPreferences == null) {
			return ThemeResouceManager.THEME_DEAFULT_ABSOLUTE_PATH;
		}
		
		String themekey = mSharedPreferences.getString(THEME_KEY, ThemeUtils.THEME_DEAFULT_ABSOLUTE_PATH);
	
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
