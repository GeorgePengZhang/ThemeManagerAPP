package com.auratech.theme;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.auratech.theme.bean.DescriptionBean;
import com.auratech.theme.utils.CircleImageView;
import com.auratech.theme.utils.FileCopyManager;
import com.auratech.theme.utils.PreferencesManager;
import com.auratech.theme.utils.ThemeImageLoader;
import com.auratech.theme.utils.ThemeImageLoader.ThemeImageOptions;
import com.auratech.theme.utils.ThemeResouceManager;
import com.auratech.theme.utils.view.NumberProgressBar;

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
	private TextView mDelete;
	private TextView mDownload;
	
	private String mTheme;
	
	private AlertDialog mDialog;
	private TextView mContent;
	private View mApplyingLayout;
	private NumberProgressBar mProgressBar;
	private View mRebootLayout;
	private View mDeleteLayout;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.theme_detail_layout);

		Intent intent = getIntent();
		mBean = intent.getParcelableExtra(THEME_DETAIL);
		previewsList = mBean.getPreviews();
		mTheme = mBean.getTheme();
		
		
		initTab();
		initIndicator();
		initViews();
		initDialog();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		ThemeImageLoader.getInstance().cancel();
	}

	private void initViews() {
		mApply = (TextView) findViewById(R.id.id_apply);
		mDelete = (TextView) findViewById(R.id.id_cancel);
		mDownload = (TextView) findViewById(R.id.id_download);
		
		String path = mBean.getPath();
		if (ThemeResouceManager.THEME_DEFAULT_PATH.equals(path)) {
			mDownload.setVisibility(View.VISIBLE);
			mDownload.setText(getResources().getString(R.string.theme_apply));
			mApply.setVisibility(View.GONE);
			mDelete.setVisibility(View.GONE);
		} else {
			mDownload.setVisibility(View.GONE);
			mApply.setVisibility(View.VISIBLE);
			mDelete.setVisibility(View.VISIBLE);
		}
		
		mApply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				applyTheme();
			}
		});
		mDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteTheme();
			}
		});
		mDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				applyTheme();
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
	
	private void initDialog() {
		if (mDialog == null) {
			mDialog = new AlertDialog.Builder(this).create();
			mDialog.setCancelable(false);
			
			//正在配置主题包的dialog view
			mApplyingLayout = View.inflate(getApplicationContext(), R.layout.theme_dialog_layout, null);
			mContent = (TextView) mApplyingLayout.findViewById(R.id.id_textView);
			mProgressBar = (NumberProgressBar) mApplyingLayout.findViewById(R.id.id_progressBar);
			
			//是否要重启系统的dialog view
			mRebootLayout = View.inflate(getApplicationContext(), R.layout.theme_dialog_reboot_layout, null);
			TextView no = (TextView) mRebootLayout.findViewById(R.id.id_no);
			TextView yes = (TextView) mRebootLayout.findViewById(R.id.id_yes);
			no.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
					//启动launcher
					Intent intent = new Intent(Intent.ACTION_MAIN);  
					intent.addCategory(Intent.CATEGORY_HOME);  
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
					startActivity(intent); 
					//结束自己的进程
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			});
			yes.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//重启
					PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
					powerManager.reboot("");
				}
			});
			
			
			mDeleteLayout = View.inflate(getApplicationContext(), R.layout.theme_dialog_delete_layout, null);
			TextView cancel = (TextView) mDeleteLayout.findViewById(R.id.id_no);
			TextView ok = (TextView) mDeleteLayout.findViewById(R.id.id_yes);
			cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
				}
			});
			ok.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//删除指定的主题包
					File file  = new File(mBean.getPath()+mBean.getTheme());
					if (file.exists()) {
						file.delete();
					}
					
					onBackPressed();
				}
			});
		}
	}

	private void updateTheme() {
		String themePath = mBean.getPath()+mTheme;
		File sourceFile = new File(themePath);
		
		if (!sourceFile.exists()) {
//			Toast.makeText(getApplicationContext(), "本主题不存在，请检查sdcard", Toast.LENGTH_SHORT).show();
			return ;
		}
		mProgressBar.setProgress(10);
		
		File dirFile = new File(ThemeResouceManager.THEME_USED_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		dirFile.setReadable(true, false);
		dirFile.setExecutable(true, false);
		//将/data/system/theme目录下的文件删除
		ThemeResouceManager.getInstance().deleteDirFile(dirFile);
		
		mProgressBar.setProgress(20);
		
		
		File destFile = new File(dirFile, ThemeResouceManager.THEME_USED_NAME);
		//将源文件主题包拷贝到/data/system/theme目录下
		try {
			FileCopyManager.copyFile(sourceFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		destFile.setReadable(true, false);
		destFile.setExecutable(true, false);
		
		mProgressBar.setProgress(40);
		//解压主题包到当前路径
		ThemeResouceManager.getInstance().unZip(ThemeResouceManager.THEME_USED_ABSOLUTE_PATH, ThemeResouceManager.THEME_USED_PATH);
		
		mProgressBar.setProgress(60);
		
		//设置声音特效，铃声，通知声，来电声
		ThemeResouceManager.getInstance().setDefalutSound(getApplicationContext());
		
		PreferencesManager.getInstance(getApplicationContext()).setThemeKey(mBean.getPath()+mTheme);
		//设置墙纸
		WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
		try {
			Bitmap bmp = ThemeResouceManager.getInstance().getImageFromResource(mTheme, mBean.getWallPaper(), ThemeResouceManager.THEME_TYPE_WALLPAPER, mBean.getPath(), new ThemeImageOptions(1024, 600));
			if (bmp != null) {
				wallpaperManager.setBitmap(bmp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mProgressBar.setProgress(80);
		
		//结束launcher进程
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.killBackgroundProcesses("com.auratech.launcher");
		
		
		mProgressBar.setProgress(100);
		
		boolean fontsExist = ThemeResouceManager.getInstance().isExist(themePath, "fonts/Roboto-Regular.ttf");
		boolean effectExist = ThemeResouceManager.getInstance().isExist(themePath, "audio/ui/Effect_Tick.ogg");
		boolean lockExist = ThemeResouceManager.getInstance().isExist(themePath, "audio/ui/Lock.ogg");
		boolean unLockExist = ThemeResouceManager.getInstance().isExist(themePath, "audio/ui/Unlock.ogg");
		Log.d("TAG", "onClick:"+fontsExist+",effectExist:"+effectExist+",lockExist:"+lockExist+",unLockExist:"+unLockExist);
		
		//假如主题包中存在 字体，触摸，开屏，锁屏音效就弹出一个显示是否重启的对话框
		if (fontsExist || effectExist || lockExist || unLockExist) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					mDialog.setContentView(mRebootLayout);
				}
			});
			
		} else {
			//启动launcher
			Intent intent = new Intent(Intent.ACTION_MAIN);  
			intent.addCategory(Intent.CATEGORY_HOME);  
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			startActivity(intent); 
			//重启
//			PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//			powerManager.reboot("");
			//结束自己的进程
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	private void applyTheme() {
		mDialog.show();
		
		Window window = mDialog.getWindow();
		window.setContentView(mApplyingLayout);
		WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
		layoutParams.gravity = Gravity.BOTTOM;
		layoutParams.width = 600;
		layoutParams.height = 240;
		mDialog.getWindow().setAttributes(layoutParams);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				updateTheme();
			}
		}).start();
	}

	private void deleteTheme() {
		mDialog.show();
		
		Window window = mDialog.getWindow();
		window.setContentView(mDeleteLayout);
		WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
		layoutParams.gravity = Gravity.BOTTOM;
		layoutParams.width = 600;
		layoutParams.height = 240;
		mDialog.getWindow().setAttributes(layoutParams);
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
