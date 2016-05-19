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
import com.auratech.theme.bean.DescriptionBean;
import com.auratech.theme.utils.CircleImageView;
import com.auratech.theme.utils.PreferencesManager;
import com.auratech.theme.utils.ThemeImageLoader;
import com.auratech.theme.utils.ThemeResouceManager;

public class ThemePreviewViewAdapter extends BaseAdapter {

	private PreferencesManager mPreferencesManager;
	private List<DescriptionBean> mList;
	private Context mContext;

	public ThemePreviewViewAdapter(Context context, List<DescriptionBean> list) {
		mContext = context;
		mList = list;
		mPreferencesManager = PreferencesManager.getInstance(context);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public DescriptionBean getItem(int position) {
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

		DescriptionBean bean = getItem(position);
		String theme = bean.getTheme();
		String themePath = bean.getPath() + theme;
		String themeResource = ThemeResouceManager.THEME_TYPE_PREVIEW
				+ bean.getThumbnails();
		String key = themePath + "_" + themeResource;

		ih.imageView.setTag(key);

		Bitmap bmp = ThemeImageLoader.getInstance().getBitmapFromCache(key);
		if (bmp != null) {
			ih.imageView.setImageBitmap(bmp);
		} else {
			ih.imageView.setImageResource(R.drawable.theme_preview_icon_default);
		}

		String themeKey = mPreferencesManager.getThemeKey();

		if (themePath.equals(themeKey)) {
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
