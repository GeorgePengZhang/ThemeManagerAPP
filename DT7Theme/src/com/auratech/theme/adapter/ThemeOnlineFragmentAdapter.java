package com.auratech.theme.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.auratech.theme.R;
import com.auratech.theme.bean.ThemeInfoBean;
import com.auratech.theme.utils.BitmapHelp;
import com.auratech.theme.utils.CircleImageView;
import com.auratech.theme.utils.HttpConfig;
import com.auratech.theme.utils.PreferencesManager;
import com.auratech.theme.utils.ThemeResouceManager;
import com.lidroid.xutils.BitmapUtils;

public class ThemeOnlineFragmentAdapter extends BaseAdapter {

	private PreferencesManager mPreferencesManager;
	private List<ThemeInfoBean> mList;
	private Context mContext;
	private BitmapUtils mBitmapUtils;
	public ThemeOnlineFragmentAdapter(Context context, List<ThemeInfoBean> list) {
		mContext = context;
		mList = list;
		
		mPreferencesManager = PreferencesManager.getInstance(context);
		
		mBitmapUtils = BitmapHelp.getBitmapUtils(context.getApplicationContext());
		mBitmapUtils.configDefaultLoadingImage(R.drawable.theme_preview_icon_default);
		mBitmapUtils.configDefaultLoadFailedImage(R.drawable.theme_preview_icon_default);
		mBitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public ThemeInfoBean getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder ih = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.theme_preview_item, null);
			convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 344));

			CircleImageView imageView = (CircleImageView) convertView.findViewById(R.id.id_image);
			ImageView selected = (ImageView) convertView.findViewById(R.id.id_selected);

			ih = new ViewHolder();
			ih.imageView = (ImageView) imageView;

			ih.imageSelected = selected;

			convertView.setTag(ih);
		} else {
			ih = (ViewHolder) convertView.getTag();
		}

		ThemeInfoBean bean = getItem(position);
		String urlStr = HttpConfig.SERVER_URL+"/"+bean.getThumbnails();
		mBitmapUtils.display(ih.imageView, urlStr);
		
		String themeFileName = ThemeResouceManager.getInstance().getThemeFileName(bean.getThemefile());
		String themeKey = mPreferencesManager.getThemeKey();

		Log.d("TAG", "getView:"+themeFileName+",themeKey:"+themeKey);
		
		
		if (themeKey.equals(ThemeResouceManager.THEME_PATH+themeFileName)) {
			ih.imageSelected.setImageResource(R.drawable.theme_using_flag);
		} else {
			ih.imageSelected.setImageDrawable(null);
		}

		return convertView;
	}

	private static class ViewHolder {
		private ImageView imageView;
		private ImageView imageSelected;
	}

}

