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

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.auratech.theme.bean.DescriptionBean;
import com.auratech.theme.utils.ThemeImageLoader.ThemeImageOptions;

public class ThemeResouceManager {
	
	public static final int READ_FILE_SIZE = 4096;
	public static final String THEME_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Theme/.data/";
	
	public static final String THEME_PATH_DOWNLOAD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Theme/.download/";
	public static final String THEME_USED_PATH = "/data/system/theme/";
//	public static final String THEME_USED_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"system/theme";
	public static final String THEME_USED_NAME = "theme_using.arz";
	public static final String THEME_USED_ABSOLUTE_PATH = THEME_USED_PATH+THEME_USED_NAME;
	
	public static final String THEME_DEFAULT_PATH = "/system/media/theme/default/";
	public static final String THEME_DEAFULT_NAME = "theme_default.arz";
	public static final String THEME_DEAFULT_ABSOLUTE_PATH = THEME_DEFAULT_PATH+THEME_DEAFULT_NAME;
	
	public static final String THEME_TYPE_ICONS = "icons/";
	public static final String THEME_TYPE_WALLPAPER = "wallpaper/";
	public static final String THEME_TYPE_PREVIEW = "preview/";
	public static final String THEME_TYPE_DESCRIPTION = "description.art";
	public static final String THEME_TYPE_AUDIO_ALARMS = "audio/alarms/alarms.ogg";
	public static final String THEME_TYPE_AUDIO_NOTIFICATIONS = "audio/notifications/notifications.ogg";
	public static final String THEME_TYPE_AUDIO_RINGTONES = "audio/ringtones/ringtones.ogg";
	
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
	
	/** ��ȡtheme/.data/Ŀ¼�����е�����
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
	
	//�õ�theme/.data��fileֵ
	public File getThemeDatePath() {
		if (!mThemeDir.exists()) {
			mThemeDir.mkdirs();
		}
		
		return mThemeDir;
	}

	/**
	 * ��ȡͼƬ��Դ
	 * 
	 * @param theme ���������
	 * @param resouces ��Դ������
	 * @param resTheme ��Ӧ��Դ���ļ���
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
	 *  ��ȡͼƬ��Դ
	 * @param theme ���������
	 * @param resouces ��Դ������
	 * @param resTheme ��Ӧ��Դ���ļ���
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
		
		//�����ϵͳĬ�ϵľͶ�ȡ/system/media/themeĿ¼�µ����⣬���ǾͶ�ȡ/data/system/theme/Ŀ¼�µ�����
		if (!THEME_DEAFULT_ABSOLUTE_PATH.equals(themePath)) {
			themePath = THEME_USED_ABSOLUTE_PATH;
		}

		bmp = ThemeImageLoader.getInstance().getBitmapFromInputStream(themePath, resTheme + resouces, options);
		return bmp;
	}
	
	public Bitmap getImageResourceFromPath(String themePath, String resName, String resFolder, ThemeImageOptions options) {
		
		Bitmap bmp = null;

		if (TextUtils.isEmpty(resFolder) || THEME_TYPE_DESCRIPTION.equals(resFolder)) {
			return bmp;
		}

		if (!mThemeDir.exists()) {
			mThemeDir.mkdirs();
		}

		if (THEME_DEAFULT_ABSOLUTE_PATH.equals(themePath)) {
			themePath = THEME_DEFAULT_PATH;
		} else {
			themePath = THEME_USED_PATH;
		}

		bmp = ThemeImageLoader.getInstance().getBitmapFromPath(themePath, resFolder+resName, options);
		return bmp;
	}
	
	/**
	 * ��ȡdecription.art���ı���Ϣ
	 * @param theme ��������
	 * @return
	 */
	private String getThemeDescription(String theme, String path) {
		String result = "";
		InputStream is = null;
		ByteArrayOutputStream baos = null;

		if (!mThemeDir.exists()) {
			mThemeDir.mkdirs();
		}
		
		//���pathΪnull�ͻ�ȡsdcard/system/theme/.dataĿ¼�µ��ļ�
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
	 * ��ȡ��ǰ����ʹ�õ����� decription.art�е�����
	 * @param theme �������·��
	 * @return ��ǰ����ʹ�õ�����decription.art�е�����
	 */
	public DescriptionBean getThemeDescriptionBean(String theme) {
		DescriptionBean bean = null;
		
		if (THEME_DEAFULT_ABSOLUTE_PATH.equals(theme)) {
			bean = parseDescription(THEME_DEAFULT_NAME, THEME_DEFAULT_PATH);
		} else {
			bean = parseDescription(THEME_USED_NAME, THEME_USED_PATH);
		}
		
		return bean;
	}
	
	/**
	 * ��ȡsdcard/system/theme/.dataĿ¼�µ������ļ�����
	 * @param theme
	 * @return
	 */
	public DescriptionBean parseDescription(String theme) {
		return parseDescription(theme, null);
	}

	/**
	 * ����arz�ļ���decription.art�ļ���������ΪDescriptionBean����
	 * @param theme ��������
	 * @return
	 */
	public DescriptionBean parseDescription(String theme, String path) {
		String description = getThemeDescription(theme, path);
		DescriptionBean bean = null;
		try {
			JSONObject jo = new JSONObject(description);

			bean = new DescriptionBean();

			String authors = jo.getString(DescriptionBean.AUTHORS);
			String designers = jo.getString(DescriptionBean.DESIGNERS);
			String titles = jo.getString(DescriptionBean.TITLES);
			String descriptions = jo.getString(DescriptionBean.DESCRIPTIONS);
			String thumbnails = jo.getString(DescriptionBean.THUMBNAILS);
			String wallPaper = jo.getString(DescriptionBean.WALLPAPER);
			boolean isLiveWallpaper = jo.getBoolean(DescriptionBean.ISLIVEWALLPAPER);

			bean.setTheme(theme);
			bean.setPath(path);
			bean.setAuthors(authors);
			bean.setDesigners(designers);
			bean.setTitles(titles);
			bean.setDescriptions(descriptions);
			bean.setThumbnails(thumbnails);
			bean.setWallPaper(wallPaper);
			bean.setIsLiveWallpaper(isLiveWallpaper);
			

			if (jo.has(DescriptionBean.FONT)) {
				JSONObject joFont = jo.getJSONObject(DescriptionBean.FONT);
				int size = joFont.getInt(DescriptionBean.SIZE);
				int colorAlpha = joFont.getInt(DescriptionBean.COLORALPHA);
				int colorRed = joFont.getInt(DescriptionBean.COLORRED);
				int colorGreen = joFont.getInt(DescriptionBean.COLORGREEN);
				int colorBlue = joFont.getInt(DescriptionBean.COLORBLUE);
				
				bean.setFontSize(size);
				bean.setFontColorAlpha(colorAlpha);
				bean.setFontColorRed(colorRed);
				bean.setFontColorGreen(colorGreen);
				bean.setFontColorBlue(colorBlue);
			}
			
			if (jo.has(DescriptionBean.ICON)) {
				JSONObject joIcon = jo.getJSONObject(DescriptionBean.ICON);
				int paddingLeft = joIcon.getInt(DescriptionBean.PADDINGLEFT);
				int paddingTop = joIcon.getInt(DescriptionBean.PADDINGTOP);
				int paddingRight = joIcon.getInt(DescriptionBean.PADDINGRIGHT);
				int paddingBottom = joIcon.getInt(DescriptionBean.PADDINGBOTTOM);
				
				bean.setIconPadLeft(paddingLeft);
				bean.setIconPadTop(paddingTop);
				bean.setIconPadRight(paddingRight);
				bean.setIconPadBottom(paddingBottom);
			}
			
			JSONArray previews = jo.getJSONArray(DescriptionBean.PREVIEWS);
			for (int i = 0; i < previews.length(); i++) {
				JSONObject previewJson = previews.getJSONObject(i);
				String preview = previewJson.getString(DescriptionBean.PREVIEW);
				bean.setPreviews(preview);
			}

		} catch (JSONException e) {
			e.printStackTrace();
			bean = null;
		}

		return bean;
	}
	
	/**
	 * ɾ��ָ���ļ����µ������ļ�
	 * @param dirFile �ļ���·��
	 */
	public void deleteDirFile(File dirFile){
		File files[] = dirFile.listFiles();  
		if (files != null)  {
			for (File f : files) {  
	            if (f.isDirectory()) { // �ж��Ƿ�Ϊ�ļ���  
	            	deleteDirFile(f);  
	                try {  
	                    f.delete();  
	                } catch (Exception e) {  
	                }  
	            } else {  
	                if (f.exists()) { // �ж��Ƿ����  
	                    try {  
	                        f.delete();  
	                    } catch (Exception e) {  
	                    }  
	                }  
	            }  
			} 
		}
    }
	
	
	/**
	 * ��ѹzip��
	 * @param unZipFile ����ѹ��zip�ļ�����·��
	 * @param destFileDir ��ѹ���ļ��ı���·��
	 */
	public boolean unZip(String unZipFile, String destFileDir) {
		boolean ret = false;
		
		if (mZipUtil != null) {
			mZipUtil.unZip(unZipFile, destFileDir);
			ret = true;
		} 
		
		return ret;
	}
	
	public boolean isExist(String zipFilePath, String resoucePathName) {
		boolean ret = false;
		
		if (mZipUtil != null) {
			ret = mZipUtil.isExist(zipFilePath, resoucePathName);
		}
		
		return ret;
	}
	
	/**
	 * ����ָ�����͵�����
	 * @param context
	 * @param type �������ͣ�RingtoneManager.TYPE_RINGTONE��RingtoneManager.TYPE_NOTIFICATION
	 * RingtoneManager.TYPE_ALARM��RingtoneManager.TYPE_ALL
	 * @param path �����ľ���·��
	 */
	private void setSoundEffects(Context context, int type, String path) {
		File file = new File(path);
		if (file.exists()) {
			ContentValues values = new ContentValues();
			values.put(MediaStore.MediaColumns.DATA, path);
			values.put(MediaStore.MediaColumns.TITLE, file.getName());
			values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
			
			switch (type) {
			case RingtoneManager.TYPE_RINGTONE:
				values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
				values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
				values.put(MediaStore.Audio.Media.IS_ALARM, false);
				values.put(MediaStore.Audio.Media.IS_MUSIC, false);
				break;

			case RingtoneManager.TYPE_NOTIFICATION:
				values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
				values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
				values.put(MediaStore.Audio.Media.IS_ALARM, false);
				values.put(MediaStore.Audio.Media.IS_MUSIC, false);	
				break;
			case RingtoneManager.TYPE_ALARM:
				values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
				values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
				values.put(MediaStore.Audio.Media.IS_ALARM, true);
				values.put(MediaStore.Audio.Media.IS_MUSIC, false);
				break;
			case RingtoneManager.TYPE_ALL:
				values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
				values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
				values.put(MediaStore.Audio.Media.IS_ALARM, false);
				values.put(MediaStore.Audio.Media.IS_MUSIC, true);
				break;
			default:
				break;
			}
			
			Uri uri = MediaStore.Audio.Media.getContentUriForPath(path);
			context.getContentResolver().delete(uri,MediaStore.MediaColumns.DATA + "=\"" + path + "\"", null);
			Uri newUri = context.getContentResolver().insert(uri, values);

			RingtoneManager.setActualDefaultRingtoneUri(context, type, newUri);
		} 
	}
	
	/**
	 * ����THEME_USED_PATH�������������
	 * @param context
	 */
	public void setDefalutSound(Context context) {
		setSoundEffects(context, RingtoneManager.TYPE_NOTIFICATION, THEME_USED_PATH+THEME_TYPE_AUDIO_NOTIFICATIONS);
		setSoundEffects(context, RingtoneManager.TYPE_ALARM, THEME_USED_PATH+THEME_TYPE_AUDIO_ALARMS);
		setSoundEffects(context, RingtoneManager.TYPE_RINGTONE, THEME_USED_PATH+THEME_TYPE_AUDIO_RINGTONES);
	}
	
	/**
	 * ��ȡ����������ļ�����
	 * @param name �������Ϣ
	 * @return �õ����������
	 */
	public String getThemeFileName(String name) {
		return name.substring(name.lastIndexOf("/")+1);
	}
}
