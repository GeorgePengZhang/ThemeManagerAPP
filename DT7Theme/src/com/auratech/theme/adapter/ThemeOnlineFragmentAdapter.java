package com.auratech.theme.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.auratech.theme.R;
import com.auratech.theme.bean.ThemeModel;
import com.auratech.theme.executor.ServerConfig;
import com.auratech.theme.utils.CircleImageView;
import com.auratech.theme.utils.PreferencesManager;
import com.auratech.theme.utils.VolleyImageLoader;

public class ThemeOnlineFragmentAdapter extends BaseAdapter {

	private PreferencesManager mPreferencesManager;
	private List<ThemeModel> mList;
	private Context mContext;
	private VolleyImageLoader mVolleyImageLoader;
	public ThemeOnlineFragmentAdapter(Context context, List<ThemeModel> list) {
		mContext = context;
		mList = list;
		mPreferencesManager = PreferencesManager.getInstance(context);
		mVolleyImageLoader = VolleyImageLoader.getImageLoader(context);
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public ThemeModel getItem(int position) {
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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.theme_preview_item, null);
			convertView.setLayoutParams(new AbsListView.LayoutParams(
					AbsListView.LayoutParams.MATCH_PARENT, 344));

			CircleImageView imageView = (CircleImageView) convertView
					.findViewById(R.id.id_image);
			ImageView selected = (ImageView) convertView
					.findViewById(R.id.id_selected);

			ih = new ViewHolder();
			ih.imageView = (ImageView) imageView;

			ih.imageSelected = selected;

			convertView.setTag(ih);
		} else {
			ih = (ViewHolder) convertView.getTag();
		}

		ThemeModel bean = getItem(position);
		String urlStr = ServerConfig.SERVER_URL+"/"+bean.getThumbnails() ;
		mVolleyImageLoader.showImage(ih.imageView, urlStr);
//
//		if (themePath.equals(themeKey)) {
//			ih.imageSelected.setImageResource(R.drawable.theme_using_flag);
//		} else {
//			ih.imageSelected.setImageDrawable(null);
//		}

		return convertView;
	}

	private static class ViewHolder {
		private ImageView imageView;
		private ImageView imageSelected;
	}

}

