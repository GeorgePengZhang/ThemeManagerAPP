package com.auratech.theme.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * 
 * @author 张鹏
 * @date 2016-03-26
 * 
 */
public class ImageLoader {

	// 首先从网络获取，并保存到本地，在从本地读取并压缩图片，保存到内存缓存中
//	private static final String TAG = "ImageLoader";

	protected static final int IMAGELOADER_SUCCESS = 1;
	protected static final int IMAGELOADER_FAILED = 2;

	private LruCache<String, Bitmap> mLruCache;

	private ExecutorService mExecutorService;

	private static ImageLoader mImageLoader;
	
	private HashSet<String> mHashSet;

	private Handler mHandler = new Handler(Looper.getMainLooper()) {

		public void handleMessage(Message msg) {
			ImageData data = null;
			switch (msg.what) {
			case IMAGELOADER_SUCCESS:
				data = (ImageData) msg.obj;
				if (data.key.equals(data.imageView.getTag())) {
					Bitmap bitmap = getBitmapFromMemCache(data.key);
					data.imageView.setImageBitmap(bitmap);
				}
				break;
			case IMAGELOADER_FAILED:
				data = (ImageData) msg.obj;
				if (data.key.equals(data.imageView.getTag())) {
					data.imageView.setImageDrawable(null);
				}
				break;

			default:
				break;
			}
		}
	};

	private ImageLoader() {
		long maxMemory = Runtime.getRuntime().maxMemory();
		mLruCache = new LruCache<String, Bitmap>((int) (maxMemory / (1024 * 8))) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount() / 1024;
			}
		};

	}

	public static ImageLoader getInstance() {
		if (mImageLoader == null) {
			synchronized (ImageLoader.class) {
				if (mImageLoader == null) {
					mImageLoader = new ImageLoader();
				}
			}
		}

		return mImageLoader;
	}
	
	private synchronized ExecutorService getExecutorService() {
		if (mExecutorService == null) {
			synchronized (ExecutorService.class) {
				if (mExecutorService == null) {
					int capacity = Runtime.getRuntime().availableProcessors() + 1;
					mExecutorService = Executors.newFixedThreadPool(capacity);
					mHashSet = new HashSet<String>(capacity);
				}
			}
		}
		
		return mExecutorService;
	}
	
	/**
	 *view获取图片并显示 
	 * @param urlString 网络地址
	 * @param key 本地保存地址
	 * @param width 要显示宽度
	 * @param height 要显示的高度
	 * @param imageView 要显示的view
	 */
	public void loadImage(final String urlString, final String key,
			final int width, final int height, final ImageView imageView) {
		if (imageView == null) {
			return ;
		}
		imageView.setImageDrawable(null);
		Bitmap bitmap = getBitmapFromMemCache(key);
	
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			return;
		}
		
		getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				//防止同一个图片地址重复加载
				if (!mHashSet.contains(key)) {
					mHashSet.add(key);
					Bitmap bmp = getBitmapFromMemCache(key);
					if (bmp != null) {
						updateImageView(key, IMAGELOADER_SUCCESS, imageView);
						return;
					}
					
					bmp = getBitmapFromFile(key, width, height);
					if (bmp != null) {
						addBitmapToMemoryCache(key, bmp);
						updateImageView(key, IMAGELOADER_SUCCESS, imageView);
						return;
					}
					
					downloadImageFromURL(urlString, key);
					bmp = getBitmapFromFile(key, width, height);
					if (bmp != null) {
						addBitmapToMemoryCache(key, bmp);
						updateImageView(key, IMAGELOADER_SUCCESS, imageView);
					} else {
						updateImageView(key, IMAGELOADER_FAILED, imageView);
					}
					
					mHashSet.remove(key);
				}
			}
		});
	}
	
	public Bitmap loadBitmap(String url, String path, int width, int height) {
		Bitmap bmp = null;
		
		bmp = getBitmapFromMemCache(path);
		if (bmp == null) {
			bmp = getBitmapFromFile(path, width, height);
		}
		
		return bmp;
	}

	/**
	 *结束图片加载 
	 */
	public void destroy() {
		if (mExecutorService != null) {
			mExecutorService.shutdownNow();
			mExecutorService = null;
		}
		if (mHashSet != null) {
			mHashSet.removeAll(mHashSet);
		}
	}
	
	/**
	 * 更新view的显示
	 * @param key 文件保存路径 
	 * @param what 消息类型
	 * @param imageView 要更新的View
	 */
	private void updateImageView(final String key, int what, final ImageView imageView) {
		Message message = mHandler.obtainMessage();
		ImageData data = new ImageData();
		data.imageView = imageView;
		data.key = key;
		message.obj = data;
		message.what = what;
		mHandler.sendMessage(message);
	}

	private static class ImageData {
		ImageView imageView;
		String key;
	}

	/**
	 * 增加bitmap到内存缓存中
	 * 
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mLruCache.put(key, bitmap);
		}
	}

	/**
	 * 获取bitmap从内存缓存中
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap getBitmapFromMemCache(String key) {
		return mLruCache.get(key);
	}

	/**
	 * 从本地文件 获取bitmap
	 * 
	 * @param filePathName
	 *            文件路径
	 * @param width
	 *            指定的宽度
	 * @param height
	 *            指定的高度
	 * @return
	 */
	public Bitmap getBitmapFromFile(String filePathName, int width, int height) {
		if (TextUtils.isEmpty(filePathName)) {
			return null;
		}

		File file = new File(filePathName);
		if (!file.exists()) {
			return null;
		}

		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(filePathName, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
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
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(filePathName, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 从网络地址上获取图片并保存到本地文件中
	 * 
	 * @param urlString
	 *            网络地址
	 * @param filePath
	 *            本地文件路径
	 */
	public void downloadImageFromURL(String urlString, String filePathName) {
		if (TextUtils.isEmpty(urlString) || "null".equals(urlString)) {
			return ;
		}
		
		File file = null;
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		FileOutputStream fos = null;
		
		filePathName = setFileExtension(filePathName, getURLExtension(urlString));

		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5 * 1000);
			connection.setReadTimeout(5 * 1000);
			inputStream = connection.getInputStream();
			int length = connection.getContentLength();
			if (length <= 0) {
				return ;
			}

			int code = connection.getResponseCode();
			if (code == 200) {
				makedirByFilePathName(filePathName);
				file = new File(filePathName);
				if (!file.exists()) {
					file.createNewFile();
				}
				fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;

				while ((len = inputStream.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				
				long fileSize = file.length();
				if (fileSize == 0 && fileSize != length) {
					deleteFile(file);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			deleteFile(file);
		} catch (IOException e) {
			e.printStackTrace();
			deleteFile(file);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			closeSteam(fos);
			closeSteam(inputStream);
			makePNGFromOtherPicFormat(filePathName);
		}
	}

	/**
	 * 从网络地址上获取图片并保存到本地文件中
	 * 
	 * @param urlString
	 *            网络地址
	 * @param filePath
	 *            本地文件路径
	 * @param fileName
	 *            本地文件名称
	 */
	public void downloadImageFromURL(String urlString, String filePath,
			String fileName) {
		File dir = null;
		File file = null;
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		FileOutputStream fos = null;

		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5 * 1000);
			connection.setReadTimeout(5 * 1000);
			inputStream = connection.getInputStream();

			int code = connection.getResponseCode();
			if (code == 200) {
				String extension = getURLExtension(urlString);
				if (!TextUtils.isEmpty(extension)) {
					// 创建文件路径
					dir = new File(filePath);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					// 创建文件
					file = new File(dir, fileName + extension);
					if (!file.exists()) {
						file.createNewFile();
					}

					fos = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
					int len = 0;

					while ((len = inputStream.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			deleteFile(file);
		} catch (IOException e) {
			e.printStackTrace();
			deleteFile(file);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			closeSteam(fos);
			closeSteam(inputStream);
		}
	}

	/**
	 * 从网络地址上获取图片的bitmap
	 * 
	 * @param urlString
	 *            网络地址
	 * @param filePath
	 *            本地文件路径
	 */
	public void downloadBitmapFromURL(String urlString, String filePath) {

		File file = null;
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		FileOutputStream fos = null;

		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5 * 1000);
			connection.setReadTimeout(5 * 1000);
			inputStream = connection.getInputStream();

//			int code = connection.getResponseCode();
			String extension = getURLExtension(urlString);
			if (!TextUtils.isEmpty(extension)) {
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

				file = new File(filePath + extension);
				if (!file.exists()) {
					file.createNewFile();
				}

				fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			deleteFile(file);
		} catch (IOException e) {
			e.printStackTrace();
			deleteFile(file);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			closeSteam(fos);
			closeSteam(inputStream);
		}
	}

	/**
	 * 关闭继承自Closeable的字节流
	 * 
	 * @param closeable
	 */
	public void closeSteam(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除指定的文件
	 * 
	 * @param file
	 */
	public void deleteFile(File file) {
		if (file != null && file.exists()) {
			file.delete();
		}
	}

	/**
	 * 获取网络图片地址的文件后缀名
	 * 
	 * @param url
	 */
	public String getURLExtension(String url) {
		String ret = null;

		if (!TextUtils.isEmpty(url) && !"null".equals(url)) {
			ret = url.substring(url.lastIndexOf("."), url.length());
		}

		return ret;
	}

	/**
	 * 修改文件后缀
	 * 
	 * @param fileName
	 * @return
	 */
	public String setFileExtension(String fileName, String extension) {
		int indexOf = fileName.lastIndexOf(".");
		String ret = fileName.substring(0, indexOf) + extension;
		return ret;
	}

	/**
	 * 根据文件绝对路径获取其所在的文件夹路径
	 * 
	 * @param filePathName
	 */
	public void makedirByFilePathName(String filePathName) {
		if (!TextUtils.isEmpty(filePathName)) {
			String dir = filePathName.substring(0,
					filePathName.lastIndexOf("/"));
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
		}
	}
	
	//指定压缩图片的宽度和高度
	private static final int IMAGE_WIDTH = 124;
	private static final int IMAGE_HEIGHT = 124;
	
	public void makePNGFromOtherPicFormat(String filePathName) {
		if (filePathName != null) {
			File  file = new File(filePathName);
			if (file.exists()) {
				Bitmap bitmap = getBitmapFromFile(filePathName, IMAGE_WIDTH, IMAGE_HEIGHT);
				if (bitmap != null) {
					deleteFile(file);
					FileOutputStream fos = null;
					File newFile = null;
					try {
						String newFilePathName = setFileExtension(filePathName, ".png");
						newFile = new File(newFilePathName);
						fos = new FileOutputStream(newFile);
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
					} catch (FileNotFoundException e) {
						deleteFile(newFile);
						e.printStackTrace();
					} finally {
						closeSteam(fos);
					}
				}
			}
		}
	}
}
