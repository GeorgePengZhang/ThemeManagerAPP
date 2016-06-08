package com.auratech.launcher.widget;

import java.io.File;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.widget.RemoteViews;

import com.auratech.launcher.R;
import com.auratech.theme.utils.PreferencesManager;
import com.auratech.theme.utils.ThemeResouceManager;

public class GalleryWidgetProvider extends AppWidgetProvider {

	private final String GALLERY_ACTION = "com.aura.theme.widgetGallery";
	private final String THEME_SYSTEM_UI_NAVIGATIONBAR_UPDATE = "com.android.systemui.navigationbar.update";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (GALLERY_ACTION.equals(action)) {
			
			Intent galleryIntent = new Intent();
			ComponentName comName = new ComponentName("com.android.gallery3d", "com.android.gallery3d.app.GalleryActivity");
			galleryIntent.setComponent(comName);
			galleryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(galleryIntent);
			
		} else if (AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED.equals(action) 
				|| LocationManager.MODE_CHANGED_ACTION.equals(action)
				|| THEME_SYSTEM_UI_NAVIGATIONBAR_UPDATE.equals(action)) {
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			updateView(context, appWidgetManager);
		}
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		updateView(context, appWidgetManager);
	}
	
	private void updateView(Context context, AppWidgetManager appWidgetManager) {
		String themeKey = PreferencesManager.getInstance(context).getThemeKey();
		String path = "";

		if (ThemeResouceManager.THEME_DEAFULT_ABSOLUTE_PATH.equals(themeKey)) {
        	path = ThemeResouceManager.THEME_DEFAULT_PATH  + ThemeResouceManager.THEME_TYPE_GALLERY_IMAGE;
        } else {
        	path = ThemeResouceManager.THEME_USED_PATH  + ThemeResouceManager.THEME_TYPE_GALLERY_IMAGE;
        }
		
		Intent actClick=new Intent(GALLERY_ACTION);
		PendingIntent pending= PendingIntent.getBroadcast(context, 0, actClick, 0);
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.gallery_widget);
		File file = new File(path);
		if (file.exists()) {
			rv.setImageViewUri(R.id.galleryCover, Uri.fromFile(new File(path)));
		} else {
			rv.setImageViewResource(R.id.galleryCover, R.drawable.disney_gallery);
		}
		rv.setOnClickPendingIntent(R.id.galleryCover, pending);
		appWidgetManager.updateAppWidget(new ComponentName(context, GalleryWidgetProvider.class), rv);
	}
}
