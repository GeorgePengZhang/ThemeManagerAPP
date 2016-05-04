package com.auratech.launcher.widget;

import com.auratech.launcher.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class GalleryWidgetProvider extends AppWidgetProvider {

	private final String GALLERY_ACTION = "com.aura.theme.widgetGallery";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (GALLERY_ACTION.equals(action)) {
			
			Intent galleryIntent = new Intent();
			//com.android.gallery3d/.app.GalleryActivity
			ComponentName comName = new ComponentName("com.android.gallery3d", "com.android.gallery3d.app.GalleryActivity");
			galleryIntent.setComponent(comName);
			galleryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(galleryIntent);
			
		}
		super.onReceive(context, intent);
	}


	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Intent actClick=new Intent(GALLERY_ACTION);
		PendingIntent pending= PendingIntent.getBroadcast(context, 0, actClick, 0);
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.gallery_widget);
		rv.setOnClickPendingIntent(R.id.galleryCover, pending);
		appWidgetManager.updateAppWidget(appWidgetIds, rv);

		
	}

	
}
