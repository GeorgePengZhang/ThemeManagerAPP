package com.auratech.theme.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

public class ThemeImageLoader {

	private static final int IMAGELOADER_SUCCESS = 1;
	private LruCache<String, Bitmap> mLruCache;
	private ZipUtil mZipUtil;
	private ExecutorService mExecutorService;
	public static ThemeImageLoader mInstance;
	private HashSet<String> mHashSet;

	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ThemeImageOptions options = null;

			switch (msg.what) {
			case IMAGELOADER_SUCCESS:
				options = (ThemeImageOptions) msg.obj;
				if (options.tag.equals(options.imageView.getTag())) {
					options.imageView
							.setImageBitmap(getBitmapFromCache(options.tag));
				}
				break;

			default:
				break;
			}

		}
	};

	public static class ThemeImageOptions {
		public int width = 100;
		public int height = 100;
		public ImageView imageView;
		public String tag;
		
		public ThemeImageOptions() {
		}
		
		public ThemeImageOptions(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}

	private ThemeImageLoader() {
		long maxMemory = Runtime.getRuntime().maxMemory();
		int maxSize = (int) (maxMemory / (1024 * 8));
		mLruCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount() / 1024;
			}
		};

		mZipUtil = new ZipUtil(ThemeResouceManager.READ_FILE_SIZE);
	}

	public static ThemeImageLoader getInstance() {
		if (mInstance == null) {
			synchronized (ThemeImageLoader.class) {
				if (mInstance == null) {
					mInstance = new ThemeImageLoader();
				}
			}
		}

		return mInstance;
	}

	private synchronized ExecutorService getExecutorService() {
		if (mExecutorService == null) {
			synchronized (ExecutorService.class) {
				if (mExecutorService == null) {
					int capacity = Runtime.getRuntime().availableProcessors() + 1;
					mHashSet = new HashSet<String>(capacity);
					mExecutorService = Executors.newFixedThreadPool(capacity);
				}
			}
		}

		return mExecutorService;
	}

	public Bitmap getBitmapFromCache(String key) {
		return mLruCache.get(key);
	}

	public void putBitmapToCache(String key, Bitmap bmp) {
		if (getBitmapFromCache(key) == null) {
			mLruCache.put(key, bmp);
		}
	}

	/**
	 * 从压缩包获取bitmap图片资源
	 * @param zipFilePath
	 * @param resoucesPathName
	 * @param imOptions
	 * @return
	 */
	public Bitmap getBitmapFromInputStream(String zipFilePath,
			String resoucesPathName, ThemeImageOptions imOptions) {
		InputStream is = mZipUtil.getResoucesFromZip(zipFilePath,
				resoucesPathName);
		
		Bitmap bmp = null;
		if (is == null) {
			return bmp;
		}
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		bmp = BitmapFactory.decodeStream(is, null, options);

		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / imOptions.width;
		int beHeight = h / imOptions.height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inDither = false; /* 不进行图片抖动处理 */
		options.inPreferredConfig = Bitmap.Config.RGB_565; /* 设置让解码器以最佳方式解码 */
		/* 下面两个字段需要组合使用 */
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inSampleSize = be; // inSampleSize进行图片压缩会以2^n的倍数进行，如，1,2,4,8,16等等如果be为10,inSampleSize就为8
		is = mZipUtil.getResoucesFromZip(zipFilePath, resoucesPathName);

		options.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeStream(is, null, options);
		try {
			if (is != null) {
				is.close();
				is = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmp;
	}
	
	/**
	 * 从文件路径获取bitmap图片资源
	 * @param zipFilePath 压缩包文件所在的路径
	 * @param resoucesPathName 资源所在的相对路径
	 * @param imOptions 图片的配置参数
	 * @return
	 */
	public Bitmap getBitmapFromPath(String zipFilePath,
			String resoucesPathName, ThemeImageOptions imOptions) {
		
		Bitmap bmp = null;
		
		String pathName = zipFilePath+resoucesPathName;
//		Log.d("TAG", "getBitmapFromPath:"+pathName);
		File file = new File(pathName);
		if (!file.exists()) {
			return bmp;
		}
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		bmp = BitmapFactory.decodeFile(pathName, options);

		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / imOptions.width;
		int beHeight = h / imOptions.height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inDither = false; /* 不进行图片抖动处理 */
		options.inPreferredConfig = Bitmap.Config.RGB_565; /* 设置让解码器以最佳方式解码 */
		/* 下面两个字段需要组合使用 */
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inSampleSize = be; // inSampleSize进行图片压缩会以2^n的倍数进行，如，1,2,4,8,16等等如果be为10,inSampleSize就为8

		options.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(pathName, options);
		return bmp;
	}

	public void loadImage(final String zipFilePath,
			final String resoucesPathName, final ThemeImageOptions options) {

		final String key = zipFilePath + "_" + resoucesPathName;

		Bitmap bitmap = getBitmapFromCache(key);
		if (bitmap != null) {
			options.imageView.setImageBitmap(bitmap);
			return;
		}

		getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				if (!mHashSet.contains(key)) {
					mHashSet.add(key);
					Bitmap bmp = getBitmapFromCache(key);
					if (bmp != null) {
						updateImageView(options, key);
					}

					bmp = getBitmapFromInputStream(zipFilePath, resoucesPathName, options);
					if (bmp != null) {
						putBitmapToCache(key, bmp);
						updateImageView(options, key);
					}
					mHashSet.remove(key);
				}
			}
		});
	}

	private void updateImageView(final ThemeImageOptions options,
			final String key) {
		Message msg = mHandler.obtainMessage();
		msg.what = IMAGELOADER_SUCCESS;
		msg.obj = options;
		options.tag = key;
		msg.sendToTarget();
	}

	/**
	 * 结束图片加载
	 */
	public void cancel() {
		if (mExecutorService != null) {
			mExecutorService.shutdownNow();
			mExecutorService = null;
		}
		
		if (mHashSet != null) {
			mHashSet.removeAll(mHashSet);
		}
	}
}
