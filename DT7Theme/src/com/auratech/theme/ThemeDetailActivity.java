package com.auratech.theme;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
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

import com.auratech.theme.utils.CircleImageView;
import com.auratech.theme.utils.DescriptionBean;
import com.auratech.theme.utils.FileCopyManager;
import com.auratech.theme.utils.PreferencesManager;
import com.auratech.theme.utils.ThemeImageLoader;
import com.auratech.theme.utils.ThemeImageLoader.ThemeImageOptions;
import com.auratech.theme.utils.ThemeResouceManager;

public class ThemeDetailActivity extends Activity {

	public static final String THEME_DETAIL = "theme_preview";
	private static final int PAGE_LIMIT = 3;
	private static final int PAGE_MARGIN = 40;

	private DescriptionBean mBean;
	private ArrayList<String> previewsList;
	private LinearLayout mContainer;
	private ViewGroup mBack;
	private ViewPager mViewPager;
	private LinearLayout mIndicator;
	private TextView mApply;
	private String mTheme;
	protected boolean mClicked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.theme_detail_layout);

		Intent intent = getIntent();
		mBean = intent.getParcelableExtra(THEME_DETAIL);
		previewsList = mBean.getPreviews();
		mTheme = mBean.getTheme();
		mClicked = false;

		initTab();
		initIndicator();
		initViews();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		ThemeImageLoader.getInstance().cancel();
		Log.d("TAG", "onDestroy ThemeDetailActivity");
	}

	private void initViews() {
		mApply = (TextView) findViewById(R.id.id_apply);
		mApply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!mClicked) {
					
					String themePath = mBean.getPath()+mTheme;
					File sourceFile = new File(themePath);
					
					if (!sourceFile.exists()) {
						Toast.makeText(getApplicationContext(), "本主题不存在，请检查sdcard", Toast.LENGTH_SHORT).show();
						mClicked = true;
						return ;
					}
					
					File dirFile = new File(ThemeResouceManager.THEME_USED_PATH);
					if (!dirFile.exists()) {
						dirFile.mkdirs();
					}
					dirFile.setReadable(true, false);
					dirFile.setExecutable(true, false);
					
					ThemeResouceManager.getInstance().deleteDirFile(dirFile);
					
					File destFile = new File(dirFile, ThemeResouceManager.THEME_USED_NAME);
					
					try {
						FileCopyManager.copyFile(sourceFile, destFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					destFile.setReadable(true, false);
					destFile.setExecutable(true, false);
					
					ThemeResouceManager.getInstance().unZip(ThemeResouceManager.THEME_USED_ABSOLUTE_PATH, ThemeResouceManager.THEME_USED_PATH);
					
					ThemeResouceManager.getInstance().setDefalutSound(getApplicationContext());
					
					
					PreferencesManager.getInstance(getApplicationContext()).setThemeKey(mBean.getPath()+mTheme);
					
					WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
					try {
						Bitmap bmp = ThemeResouceManager.getInstance().getImageFromResource(mTheme, mBean.getWallPaper(), ThemeResouceManager.THEME_TYPE_WALLPAPER, mBean.getPath(), new ThemeImageOptions(1024, 600));
						if (bmp != null) {
							wallpaperManager.setBitmap(bmp);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
					activityManager.killBackgroundProcesses("com.auratech.launcher");
					
					
					Intent intent = new Intent(Intent.ACTION_MAIN);  
					intent.addCategory(Intent.CATEGORY_HOME);  
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	                startActivity(intent); 
					
					android.os.Process.killProcess(android.os.Process.myPid());
					
					PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
					powerManager.reboot("就是要重启");
					
					mClicked = true;
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

		String title = mTheme.substring(0, mTheme.lastIndexOf("."));
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
			
			String theme = mTheme;
			String themePath = mBean.getPath()+theme;
			String themeResource = ThemeResouceManager.THEME_TYPE_PREVIEW+previewsList.get(position);
			
			
			imageview.setImageResource(R.drawable.theme_preview_icon_default);
			imageview.setTag(themePath+"_"+themeResource);
			
			ThemeImageOptions options = new ThemeImageOptions();
			options.width = 200;
			options.height = 200;
			options.imageView = imageview;
			
			ThemeImageLoader.getInstance().loadImage(themePath, themeResource, options);
			
			container.addView(imageview);
			return imageview;
		}

		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	};

}
