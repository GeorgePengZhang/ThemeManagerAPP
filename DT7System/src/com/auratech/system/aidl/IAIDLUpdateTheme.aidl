package com.auratech.system.aidl;

interface  IAIDLUpdateTheme {

	void killLauncher(String pkg);
	
	void reboot();
	
	void setLiveWallpaper(String pkg, String cls);
	
	void setThemeKey(String theme);
	
}