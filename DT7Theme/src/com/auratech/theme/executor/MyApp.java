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

	/** ��ʾ�Ի����Ƿ�������ʾ */
	public static boolean sIsHintDlgShowing = false;
	public static Date sHintDlgEndTime;
	public static Date sDockEndTime;

	public static MyApp mApp;
	public static Context mContext;
	public static DbUtils mDbUtils;

	/** ���ݿ����� */
	private static final String DATABASE_NAME = "restaurant.db";
	private static final int DATABASE_VERSION = 2;

	/** ��Ļ��ȣ����أ� */
	public static int phoneWidth = 0;

	/** ��Ļ�߶ȣ����أ� */
	public static int phoneHeight = 0;

	/** ��Ļ�ܶȣ�0.75 / 1.0 / 1.5�� */
	public static float phoneDensity = 0;

	/** ��Ļ�ܶ�DPI��120 / 160 / 240�� */
	public static int phoneDPI = 0;


	@Override
	public void onCreate() {
		super.onCreate();

		mApp = this;
		mContext = getApplicationContext();
		// �������ݿ��������
		mDbUtils = DbUtils.create(mContext, DATABASE_NAME, DATABASE_VERSION,
				null);

		savePhoneInfo();

	}



	/**
	 * �����Ƿ����� add by donsen
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
	 * ��ȡ�������ֻ���Ļ
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
