package com.auratech.theme.utils;



import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.auratech.theme.R;

public class VolleyImageLoader {
	
	private static VolleyImageLoader mImageLoader;
	private static Object obj = new Object();
	private ImageLoader imageLoader = null;
	private ImageListener listener = null;
	private ImageCache mImageCache = null;
	private RequestQueue mQueue;
     
	private VolleyImageLoader(Context context){
		mQueue = Volley.newRequestQueue(context);
		mImageCache = new ImageCache(context);
		imageLoader = new ImageLoader(mQueue, mImageCache);
	}
	
	public static VolleyImageLoader getImageLoader(Context context){
		synchronized (obj) {
			if (mImageLoader == null) {
				mImageLoader = new VolleyImageLoader(context);
			}
		}
		return mImageLoader;
	}
	
	public ImageLoader getVImageLoader(){
		return imageLoader;
	}
	
	public void showImage(ImageView imageView, String strImgUrl){
		if (TextUtils.isEmpty(strImgUrl) || !strImgUrl.contains("http")) {
			imageView.setImageResource(R.drawable.theme_preview_icon_default);
			return;
		}
//		mImageCache.setScaleSize(false, 0, 0);
//		imageView.setScaleType(ScaleType.CENTER);
		listener = ImageLoader.getImageListener(imageView, R.drawable.theme_preview_icon_default, R.drawable.theme_preview_icon_default);
		if(listener!=null){
			imageLoader.get(strImgUrl, listener);
		}
			
	}
}
