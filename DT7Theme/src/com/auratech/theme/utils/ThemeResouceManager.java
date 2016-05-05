package com.auratech.theme.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import com.auratech.theme.utils.ThemeImageLoader.ThemeImageOptions;

public class ThemeResouceManager {
	private static final String TAG = "ThemeResouceManager";
	
	public static final int READ_FILE_SIZE = 4096;
	public static final String THEME_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "Theme/.data/";
	
	public static final String THEME_USED_PATH = "/data/system/theme";
//	public static final String THEME_USED_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"system/theme";
	public static final String THEME_USED_NAME = "theme_using.arz";
	public static final String THEME_USED_ABSOLUTE_PATH = THEME_USED_PATH+File.separator+THEME_USED_NAME;
	
	public static final String THEME_DEFAULT_PATH = "/system/media/theme/default/";
	public static final String THEME_DEAFULT_NAME = "theme_default.arz";
	public static final String THEME_DEAFULT_ABSOLUTE_PATH = THEME_DEFAULT_PATH+THEME_DEAFULT_NAME;
	
	public static final String THEME_TYPE_ICONS = "icons/";
	public static final String THEME_TYPE_WALLPAPER = "wallpaper/";
	public static final String THEME_TYPE_PREVIEW = "preview/";
	public static final String THEME_TYPE_DESCRIPTION = "description.art";
	private ZipUtil mZipUtil;
	private File mThemeDir;

	private static ThemeResouceManager mThemeResouceManager;

	private ThemeResouceManager() {
		mZipUtil = new ZipUtil(READ_FILE_SIZE);
		mThemeDir = new File(THEME_PATH);
		if (!mThemeDir.exists()) {
			mThemeDir.mkdirs();
		}
	}

	public static ThemeResouceManager getInstance() {
		if (mThemeResouceManager == null) {
			synchronized (ThemeResouceManager.class) {
				if (mThemeResouceManager == null) {
					mThemeResouceManager = new ThemeResouceManager();
				}
			}
		}
		return mThemeResouceManager;
	}
	
	/** 获取theme/.data/目录下所有的主题
	 * @return
	 */
	public String[] getAllLocalTheme() {
		if (!mThemeDir.exists()) {
			mThemeDir.mkdirs();
			return null;
		}

		String[] list = mThemeDir.list(fileFilter);
		return list;
	}

	private static FilenameFilter fileFilter = new FilenameFilter() {

		@Override
		public boolean accept(File dir, String filename) {
			return filename.toLowerCase(Locale.getDefault()).endsWith(".arz");
		}
	};
	
	//得到theme/.data的file值
	public File getThemeDatePath() {
		if (!mThemeDir.exists()) {
			mThemeDir.mkdirs();
		}
		
		return mThemeDir;
	}

	/**
	 * 获取图片资源
	 * 
	 * @param theme 主题的名字
	 * @param resouces 资源的名字
	 * @param resTheme 对应资源的文件夹
	 * @return
	 */
	public Bitmap getImageFromResource(String theme, String resouces,
			String resTheme, String path, ThemeImageOptions options) {
		Bitmap bmp = null;

		if (TextUtils.isEmpty(resTheme)
				|| THEME_TYPE_DESCRIPTION.equals(resTheme)) {
			return bmp;
		}

		if (!mThemeDir.exists()) {
			mThemeDir.mkdirs();
		}
		
		if (path == null) {
			path = THEME_PATH;
		}

		String themePath = path + theme;
		bmp = ThemeImageLoader.getInstance().getBitmapFromInputStream(themePath, resTheme + resouces, options);
		
		return bmp;
	}
	
	/**
	 *  获取图片资源
	 * @param theme 主题的名字
	 * @param resouces 资源的名字
	 * @param resTheme 对应资源的文件夹
	 * @return
	 */
	public Bitmap getImageFromResource(String theme, String resouces,
			String resTheme, String path) {
		return getImageFromResource(theme, resouces, resTheme, path, new ThemeImageOptions());
	}
	
	public Bitmap getImageResourceFromARZ(String themePath, String resouces,
			String resTheme, ThemeImageOptions options) {
		
		Bitmap bmp = null;

		if (TextUtils.isEmpty(resTheme)
				|| THEME_TYPE_DESCRIPTION.equals(resTheme)) {
			return bmp;
		}

		if (!mThemeDir.exists()) {
			mThemeDir.mkdirs();
		}
		
		//如果是系统默认的就读取/system/media/theme目录下的主题，不是就读取/data/system/theme/目录下的主题
		if (!THEME_DEAFULT_ABSOLUTE_PATH.equals(themePath)) {
			themePath = THEME_USED_ABSOLUTE_PATH;
		}

		bmp = ThemeImageLoader.getInstance().getBitmapFromInputStream(themePath, resTheme + resouces, options);
		return bmp;
	}
	
	/**
	 * 获取decription.art得文本信息
	 * @param theme 主题名称
	 * @return
	 */
	private String getThemeDescription(String theme, String path) {
		String result = "";
		InputStream is = null;
		ByteArrayOutputStream baos = null;

		if (!mThemeDir.exists()) {
			mThemeDir.mkdirs();
		}
		
		//如果path为null就获取sdcard/system/theme/.data目录下的文件
		if (path == null) {
			path = THEME_PATH;
		}

		try {
			is = mZipUtil.getResoucesFromZip(path + theme, THEME_TYPE_DESCRIPTION);
			if (is != null) {
				baos = new ByteArrayOutputStream();
				int len = 0;
				byte[] buffer = new byte[1024];

				while ((len = is.read(buffer)) > 0) {
					baos.write(buffer, 0, len);
				}
				result = baos.toString("GBK");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * 获取sdcard/system/theme/.data目录下的主题文件解析
	 * @param theme
	 * @return
	 */
	public DescriptionBean parseDescription(String theme) {
		return parseDescription(theme, null);
	}

	/**
	 * 解析arz文件中decription.art文件，并保存为DescriptionBean对象
	 * @param theme 主题名称
	 * @return
	 */
	public DescriptionBean parseDescription(String theme, String path) {
		String description = getThemeDescription(theme, path);
		DescriptionBean bean = null;
		try {
			JSONObject jo = new JSONObject(description);

			bean = new DescriptionBean();

			String authors = jo.getString("authors");
			String designers = jo.getString("designers");
			String titles = jo.getString("titles");
			String descriptions = jo.getString("descriptions");
			String thumbnails = jo.getString("thumbnails");
			String wallPaper = jo.getString("wallpaper");

			bean.setTheme(theme);
			bean.setPath(path);
			bean.setAuthors(authors);
			bean.setDesigners(designers);
			bean.setTitles(titles);
			bean.setDescriptions(descriptions);
			bean.setThumbnails(thumbnails);
			bean.setWallPaper(wallPaper);
			
			JSONArray previews = jo.getJSONArray("previews");
			for (int i = 0; i < previews.length(); i++) {
				JSONObject previewJson = previews.getJSONObject(i);
				String preview = previewJson.getString("preview");
				bean.setPreviews(preview);
			}

		} catch (JSONException e) {
			e.printStackTrace();
			bean = null;
		}

		return bean;
	}
}
