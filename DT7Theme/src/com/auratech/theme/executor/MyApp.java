package com.auratech.theme.executor;

import java.util.Date;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.lidroid.xutils.DbUtils;


/* implements UncaughtExceptionHandler */
public class MyApp extends Application {

	/** 提示对话窗是否正在显示 */
	public static boolean sIsHintDlgShowing = false;
	public static Date sHintDlgEndTime;
	public static Date sDockEndTime;

	public static MyApp mApp;
	public static Context mContext;
	public static DbUtils mDbUtils;

	/** 数据库名称 */
	private static final String DATABASE_NAME = "restaurant.db";
	private static final int DATABASE_VERSION = 2;

	/** 屏幕宽度（像素） */
	public static int phoneWidth = 0;

	/** 屏幕高度（像素） */
	public static int phoneHeight = 0;

	/** 屏幕密度（0.75 / 1.0 / 1.5） */
	public static float phoneDensity = 0;

	/** 屏幕密度DPI（120 / 160 / 240） */
	public static int phoneDPI = 0;


	@Override
	public void onCreate() {
		super.onCreate();

		mApp = this;
		mContext = getApplicationContext();
		// 创建数据库操作对象
		mDbUtils = DbUtils.create(mContext, DATABASE_NAME, DATABASE_VERSION,
				null);

		savePhoneInfo();

	}



	/**
	 * 网络是否连接 add by donsen
	 * 
	 * @param mContext
	 */
	public static boolean isNetConnect() {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 读取并保存手机屏幕
	 */
	private void savePhoneInfo() {
		DisplayMetrics metric = new DisplayMetrics();
		metric = getApplicationContext().getResources().getDisplayMetrics();
		phoneWidth = metric.widthPixels;
		phoneHeight = metric.heightPixels;
		phoneDensity = metric.density;
		phoneDPI = metric.densityDpi;
	}

	
}
