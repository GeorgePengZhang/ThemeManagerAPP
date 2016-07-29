package com.auratech.system;

import android.app.ActivityManager;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;

import com.auratech.system.aidl.IAIDLUpdateTheme;
import com.auratech.theme.utils.PreferencesManager;

public class UpdateThemeService extends Service {
	
	public static final String START_SERVICE = "com.auratech.system.UpdateThemeService.start";

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private IBinder mBinder = new IAIDLUpdateTheme.Stub() {

		@Override
		public void killLauncher(String pkg) throws RemoteException {
			ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
			//"com.auratech.launcher"
			activityManager.forceStopPackage(pkg);
		}

		@Override
		public void reboot() throws RemoteException {
			PowerManager powerManager = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
			powerManager.reboot("");
		}

		@Override
		public void setLiveWallpaper(String pkg, String cls)
				throws RemoteException {
			try {
				WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
				//"com.auratech.wallpaper", "com.auratech.wallpaper.AuraWallpaper"
				ComponentName name = new ComponentName(pkg, cls);
				wallpaperManager.getIWallpaperManager().setWallpaperComponent(name);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void setThemeKey(String theme) throws RemoteException {
			PreferencesManager.getInstance(getApplicationContext()).setThemeKey(theme);
		}
	};
}
