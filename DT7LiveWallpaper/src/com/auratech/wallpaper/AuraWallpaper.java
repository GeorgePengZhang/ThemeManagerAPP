package com.auratech.wallpaper;

import java.io.File;
import java.io.FileFilter;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.auratech.theme.utils.ConfigBean;
import com.auratech.theme.utils.ConfigManager;
import com.auratech.theme.utils.PreferencesManager;
import com.auratech.theme.utils.ThemeUtils;

public class AuraWallpaper extends WallpaperService{
	
	public static final String TAG = "AuraWallpaper";
	private Handler mHandler = new Handler();
	
	

	@Override
	public Engine onCreateEngine() {
		return new MyEngine();
	}
	
	
	class MyEngine extends Engine{
		
		private List<File> mList;
		private int mFrameIndex;
		private int mFrameMax;
		private int mScreenWidth;
		private int mScreenHeight;
		
		private Bitmap mFrameBg;
		private boolean mVisible;
		
		private String mThemeKey;
		private int mUpdateTime;
		
		private Runnable mDrawThread = new Runnable() {
			
			@Override
			public void run() {
				draw();
			}
		};
		
		public MyEngine() {
			initFrameResource();
		}


		private void initFrameResource() {
			mThemeKey = PreferencesManager.getInstance(getApplicationContext()).getThemeKey();
			mFrameIndex = 0;
			mFrameMax 	= 0;
			
			String wallPaperPath = "";
			String wallPaperBgPath = "";
			String wallPaperConfigPath = "";
			if (ThemeUtils.THEME_DEAFULT_ABSOLUTE_PATH.equals(mThemeKey)) {
				wallPaperPath = ThemeUtils.THEME_DEFAULT_PATH+ThemeUtils.THEME_TYPE_LIVEWALLPAPER;
			} else {
				wallPaperPath = ThemeUtils.THEME_USED_PATH+ThemeUtils.THEME_TYPE_LIVEWALLPAPER;
			}
			
			wallPaperBgPath = wallPaperPath+ThemeUtils.THEME_TYPE_LIVEWALLPAPER_BG;
			wallPaperConfigPath = wallPaperPath+ThemeUtils.THEME_TYPE_LIVEWALLPAPER_CONFIG;
			
			File file = new File(wallPaperPath);
			if (file.exists()) {
				File[] listFiles = file.listFiles(new FileFilter() {
					
					@Override
					public boolean accept(File pathname) {
						return pathname.getName().endsWith(".jpg") || pathname.getName().endsWith(".png");
					}
				});
				mList = Arrays.asList(listFiles);
				
				Collections.sort(mList, new Comparator<File>() {
	                final Collator mCollator = Collator.getInstance();

					@Override
					public int compare(File lhs, File rhs) {
						return mCollator.compare(lhs.getName(), rhs.getName());
					}
	            });
				
				
				mFrameMax = mList.size();
			}
			
			File bgFile = new File(wallPaperBgPath);
			
			
			Log.d(TAG, "initFrameResource:"+wallPaperBgPath+",wallPaperPath:"+wallPaperPath+",a:"+file.exists()+",b:"+bgFile.exists());
			if (bgFile.exists()) {
				mFrameBg = BitmapFactory.decodeFile(wallPaperBgPath);
			} else {
				Resources sysRes = Resources.getSystem();
			    int resId = sysRes.getIdentifier("default_wallpaper", "drawable", "android");
			    mFrameBg = BitmapFactory.decodeResource(sysRes, resId);
			}
			
			
			ConfigBean bean = ConfigManager.parseConfig(wallPaperConfigPath);
			
			if (bean != null) {
				mUpdateTime = bean.getUpdateTime();
			} else {
				mUpdateTime = 50;
			}
			
			Log.d(TAG, "initFrameResource:"+mUpdateTime);
		}
		
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
		}
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			mHandler.removeCallbacks(mDrawThread);
			if (mFrameBg != null) {
				mFrameBg.recycle();
				mFrameBg = null;
			}
		}
		
		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}
		
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mHandler.removeCallbacks(mDrawThread);
		}
		
		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			mScreenWidth = width;
			mScreenHeight = height;
			draw();
		}
		
		@Override
		public void onOffsetsChanged(float xOffset, float yOffset,float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
			//draw();
		}
		
		@Override
		public void onTouchEvent(MotionEvent event) {
			//super.onTouchEvent(event);
		}
		
		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			mVisible = visible;

			if (visible) {
				draw();
			} else {
				mHandler.removeCallbacks(mDrawThread);
			}
		}
		
		
		private void draw() {
			SurfaceHolder holder = getSurfaceHolder();
			
			Canvas canvas = null;
			
			canvas = holder.lockCanvas();
			if (canvas != null) {
				
				drawFrameBitmap(canvas);
				
				holder.unlockCanvasAndPost(canvas);
			}
			
			mHandler.removeCallbacks(mDrawThread);
			if (mVisible) {
				mHandler.postDelayed(mDrawThread, mUpdateTime);
			}
		}
		
		/**
		 * 帧动画
		 * @param canvas
		 */
		private void drawFrameBitmap(Canvas canvas) {
			canvas.save();
			canvas.drawColor(Color.BLACK);
			
//			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
//			
//			if (Math.abs(mXPixelOffset)+mScreenWidth>bmp.getWidth()) {
//				mXPixelOffset = mScreenWidth-bmp.getWidth();
//			}
//			
//			canvas.drawBitmap(bmp, mXPixelOffset, 0, null);
			
			drawBitmapByCenter(canvas, mFrameBg);
			
			if (mFrameMax > 0) {
				Bitmap bitmap = BitmapFactory.decodeFile(mList.get(mFrameIndex).getAbsolutePath());
				drawBitmapByCenter(canvas, bitmap);
			}
			
			canvas.restore();
			
			mFrameIndex++;
			if (mFrameIndex == mFrameMax) {
				mFrameIndex = 0;
			}
		}
		
		/**
		 * 绘制到中心
		 * @param canvas
		 * @param bitmap
		 */
		private void drawBitmapByCenter(Canvas canvas, Bitmap bitmap) {
			if (bitmap == null) {
				return ;
			}
				 
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			
			int left = (mScreenWidth - width) / 2;
			int top = (mScreenHeight - height) / 2;
			
			Rect src = new Rect(0, 0, width, height);
			Rect dst = new Rect(left, top, left+width, top+height);
			
			canvas.drawBitmap(bitmap, src, dst, null);
		}
		
		/**
		 * 以坐标绘制
		 * @param canvas
		 * @param bitmap
		 * @param x
		 * @param y
		 */
		private void drawBitmapByXY(Canvas canvas, Bitmap bitmap, int x, int y) {
			if (bitmap == null) {
				return ;
			}
			
			canvas.drawBitmap(bitmap, x, y, null);
		}
	}

}
