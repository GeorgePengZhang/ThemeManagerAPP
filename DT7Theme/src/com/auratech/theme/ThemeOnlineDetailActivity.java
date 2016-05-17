package com.auratech.theme;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auratech.theme.bean.ThemeModel;
import com.auratech.theme.executor.ServerConfig;
import com.auratech.theme.utils.BitmapHelp;
import com.auratech.theme.utils.CircleImageView;
import com.auratech.theme.utils.FileCopyManager;
import com.auratech.theme.utils.FileUtils;
import com.auratech.theme.utils.PreferencesManager;
import com.auratech.theme.utils.ThemeImageLoader;
import com.auratech.theme.utils.ThemeResouceManager;
import com.auratech.theme.utils.VolleyImageLoader;
import com.auratech.theme.utils.view.NumberProgressBar;
import com.auratech.theme.utils.view.OnProgressBarListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.sample.R;
import com.lidroid.xutils.util.LogUtils;

/**
 * 在線主題詳情界面
 * 
 * @Description TODO
 * @author Robin
 * @date
 * @Copyright:
 */
public class ThemeOnlineDetailActivity extends Activity implements
		OnProgressBarListener {

	public static final String THEME_ONLINE = "theme_online"; // 在線主題標籤
	private static final int PAGE_LIMIT = 3;
	private static final int PAGE_MARGIN = 40;

	private ArrayList<String> previewsList;
	private LinearLayout mContainer;
	private ViewGroup mBack;
	private ViewPager mViewPager;
	private LinearLayout mIndicator;
	private TextView mApply;
	protected boolean mClicked;

	/* 以下是 在線主題屬性 * */
	private ThemeModel themBean;
	private VolleyImageLoader mVolleyImageLoader;


	private NumberProgressBar bnp;
	private boolean upSuccs = false;
	private BitmapUtils mBitmapUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.theme_detail_layout);
		bnp = (NumberProgressBar) findViewById(R.id.npb_progress);
		mVolleyImageLoader = VolleyImageLoader
				.getImageLoader(ThemeOnlineDetailActivity.this);
		Bundle intent = getIntent().getBundleExtra(THEME_ONLINE);
		themBean = (ThemeModel) intent.getSerializable(THEME_ONLINE);
		previewsList = getListData(themBean);
		mClicked = false;
		filenameTemp = ThemeResouceManager.THEME_PATH_UPLOADER + "/"
				+ themBean.getThemefile();
		bnp.setOnProgressBarListener(this);
		
		mBitmapUtils = BitmapHelp.getBitmapUtils(getApplicationContext());
		mBitmapUtils.configDefaultLoadingImage(R.drawable.theme_preview_icon_default);
		mBitmapUtils.configDefaultLoadFailedImage(R.drawable.theme_preview_icon_default);
		mBitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);

		initTab();
		initIndicator();
		initViews();
	}
	
	

	private ArrayList<String> getListData(ThemeModel themBean) {
		ArrayList<String> list = new ArrayList<String>();
		if (null != themBean) {
			String da[] = themBean.getPreviewPath().toString().split(";");
			for (int i = 0; i < da.length; i++) {
				list.add(da[i]);
			}
		}

		return list;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// 创建文件夹及文件
	public void CreateText(String flieUrl) {
		File file = new File(FileUtils.mSdRootPath);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		File dir = new File(flieUrl);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {
			}
		}

	}

	private String filenameTemp = null;

	@SuppressWarnings("unchecked")
	public void download() {

		CreateText(filenameTemp);
		HttpUtils http = new HttpUtils();
		
		Log.e("TAG", "download:"+(ServerConfig.SERVER_URL + "/" + themBean.getThemefile()));
		
		http.download(ServerConfig.SERVER_URL + "/" + themBean.getThemefile(),
				filenameTemp, true, true, new RequestCallBack() {
					@Override
					public void onStart() {
						mApply.setText(getString(R.string.theme_network_connection));
					}

					@Override
					public void onLoading(final long total, final long current,
							boolean isUploading) {
						bnp.setVisibility(View.VISIBLE);
						mApply.setText(getString(R.string.theme_downloading));
						//总共大小
						bnp.incrementProgressBy((int)((double)current/(double)total*100));
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						bnp.setVisibility(View.GONE);
						bnp.setProgress(0);
						mApply.setText(getString(R.string.theme_is_downloading));
						upSuccs = false;
						Toast.makeText(ThemeOnlineDetailActivity.this, msg, 500)
								.show();
						System.out.println("error" + msg);
					}

					@Override
					public void onSuccess(ResponseInfo responseInfo) {
						Toast.makeText(getApplicationContext(),
								"Download Successful"+(ServerConfig.SERVER_URL + "/" + themBean.getThemefile()), Toast.LENGTH_SHORT)
								.show();
						bnp.setProgress(100);
						upSuccs = true;
						mApply.setText(getString(R.string.theme_application));
					}
					
				});

	}

	private void initViews() {
		mApply = (TextView) findViewById(R.id.id_apply);
		File file = new File(ThemeResouceManager.THEME_PATH_UPLOADER + "/"
				+ themBean.getThemefile());

		if (file.exists()) {
			upSuccs = true;
			mApply.setText(getString(R.string.theme_application));

		} else {
			upSuccs = false;
			mApply.setText(getString(R.string.theme_is_downloading));
		}
		mApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mClicked) {
					if (!upSuccs) {
						bnp.setVisibility(View.VISIBLE);
						download();
					} else {
						bnp.setVisibility(View.GONE);
						File sourceFile = new File(filenameTemp);

						if (!sourceFile.exists()) {
							Toast.makeText(getApplicationContext(),
									getString(R.string.theme_prompt), Toast.LENGTH_SHORT)
									.show();
							// mClicked = true;
							upSuccs = false;
							return;
						}

						File dirFile = new File(
								ThemeResouceManager.THEME_USED_PATH);
						if (!dirFile.exists()) {
							dirFile.mkdirs();
						}
						String daFlie[] = themBean.getThemefile().split("/");
						File destFile = new File(dirFile, daFlie[1]);
						destFile.canRead();

						
						File dirFiles = new File(
								ThemeResouceManager.THEME_USED_ABSOLUTE_PATH);
						if (!dirFile.exists()) {
							dirFile.mkdirs();
						}
						try {
							FileCopyManager.copyFile(sourceFile, destFile);
							FileCopyManager.copyFile(sourceFile, dirFiles);
						} catch (IOException e) {
							e.printStackTrace();
						}

						PreferencesManager
								.getInstance(getApplicationContext())
								.setThemeKey(
										ThemeResouceManager.THEME_PATH_UPLOADER
												+ "/" + themBean.getThemefile());

						WallpaperManager wallpaperManager = WallpaperManager
								.getInstance(getApplicationContext());
						try {
							Bitmap bmp = ThemeResouceManager
									.getInstance()
									.getImageFromResource(
											daFlie[1],
											"default_wallpaper.jpg",
											ThemeResouceManager.THEME_TYPE_WALLPAPER,
											ThemeResouceManager.THEME_PATH_UPLOADER
													+ daFlie[0] + "/");
							if (bmp != null) {
								wallpaperManager.setBitmap(bmp);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

						ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
						activityManager
								.killBackgroundProcesses("com.auratech.launcher");

						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);

						android.os.Process.killProcess(android.os.Process
								.myPid());
					}
				}
			}
		});

		mContainer = (LinearLayout) findViewById(R.id.id_container);
		mViewPager = (ViewPager) findViewById(R.id.id_viewPager);
		mViewPager.setOffscreenPageLimit(PAGE_LIMIT);
		mViewPager.setPageMargin(PAGE_MARGIN);

		mContainer.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mViewPager.dispatchTouchEvent(event);
			}
		});

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				resetIndicator(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		mViewPager.setAdapter(mPagerAdapter);
	}

	private void initIndicator() {
		mIndicator = (LinearLayout) findViewById(R.id.id_indicator);

		for (int i = 0; i < previewsList.size(); i++) {
			ImageView iv = new ImageView(getApplication());
			if (i == 0) {
				iv.setImageResource(R.drawable.theme_indicator_current);
			} else {
				iv.setImageResource(R.drawable.theme_indicator_default);
			}

			mIndicator.addView(iv);
		}
	}

	private void initTab() {
		mBack = (ViewGroup) findViewById(R.id.id_back);

		String title = themBean.getName();
		TextView titleTV = (TextView) mBack.getChildAt(1);
		titleTV.setText(title);

		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void resetIndicator(int index) {
		int count = mIndicator.getChildCount();
		for (int i = 0; i < count; i++) {
			ImageView iv = (ImageView) mIndicator.getChildAt(i);
			if (i == index) {
				iv.setImageResource(R.drawable.theme_indicator_current);
			} else {
				iv.setImageResource(R.drawable.theme_indicator_default);
			}
		}
	}

	PagerAdapter mPagerAdapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return previewsList.size();
		}

		public Object instantiateItem(ViewGroup container, int position) {
			CircleImageView imageview = new CircleImageView(getApplicationContext());

			imageview.setBorderColor(Color.parseColor("#c8abcc"));
			imageview.setBorderRadius(5);
			imageview.setBorderWidth(2);

			imageview.setLayoutParams(new LayoutParams(194, 342));
			String urlStr = ServerConfig.SERVER_URL + "/" + previewsList.get(position);
			mVolleyImageLoader.showImage(imageview, urlStr);
			
//			mBitmapUtils.display(imageview, urlStr);
			
			container.addView(imageview);
			return imageview;
		}

		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	};

	@Override
	public void onProgressChange(int current, int max) {
		if (current == max) {
			Toast.makeText(getApplicationContext(), getString(R.string.theme_application), Toast.LENGTH_SHORT).show();
			mApply.setText(getString(R.string.theme_application));
		}
	}

}
