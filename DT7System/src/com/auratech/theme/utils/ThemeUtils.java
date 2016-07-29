package com.auratech.theme.utils;

import java.io.File;

import android.graphics.drawable.Drawable;
import android.os.Environment;

public class ThemeUtils {

	public static final String THEME_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Theme/.data/";
	public static final String THEME_PATH_DOWNLOAD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Theme/.download/";
	
	
	public static final String THEME_USED_PATH = "/data/system/theme/";
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
	
	
	public static final String THEME_TYPE_SYSTEMUI 				= "com.android.systemui/";
	public static final String THEME_TYPE_SYSTEMUI_VOLUME_ADD 	= "add_normal.png";
	public static final String THEME_TYPE_SYSTEMUI_CAPTURE 		= "capture.png";
	public static final String THEME_TYPE_SYSTEMUI_BACK 		= "ic_sysbar_back.png";
	public static final String THEME_TYPE_SYSTEMUI_BACK_IME 	= "ic_sysbar_back_ime.png";
	public static final String THEME_TYPE_SYSTEMUI_HOME 		= "ic_sysbar_home.png";
	public static final String THEME_TYPE_SYSTEMUI_MENU 		= "ic_sysbar_menu.png";
	public static final String THEME_TYPE_SYSTEMUI_RECENT 		= "ic_sysbar_recent.png";
	public static final String THEME_TYPE_SYSTEMUI_VOLUME_SUB 	= "sub_normal.png";
	
	public static final String THEME_SYSTEM_UI_NAVIGATIONBAR_UPDATE = "com.android.systemui.navigationbar.update";
	
	public static final String THEME_TYPE_UPDATE_THEME			= "com.android.systemui.updatetheme";
	public static final String THEME_TYPE_UPDATE_THEME_PROGRESS	= "com.android.systemui.updatetheme.progress";
	public static final String THEME_TYPE_REBOOT	= "com.android.system.reboot";
	
	/**
	 * 通过文件路径获取图片的drawable
	 * @param path 文件路径
	 * @return
	 */
	public static Drawable getDrawableFromPath(String path) {
		Drawable drawable = null;
		
		File file = new File(path);
		if (file.exists()) {
			drawable = Drawable.createFromPath(path);
		}
		
		return drawable;
	}
}
