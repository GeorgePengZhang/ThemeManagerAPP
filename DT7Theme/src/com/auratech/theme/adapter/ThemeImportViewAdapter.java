package com.auratech.theme.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.auratech.theme.R;

public class ThemeImportViewAdapter extends BaseAdapter {

	private ArrayList<String> mPaths;
	private LayoutInflater mInflater;
	private String mSelectedFile;
	private String[] mVolumePaths;

	public ThemeImportViewAdapter(Context context, List<String> paths) {
		mInflater = LayoutInflater.from(context);

		mPaths = (ArrayList<String>) paths;
	}

	public void setSelected(String path) {
		mSelectedFile = path;
	}

	public String getSelected() {
		return mSelectedFile;
	}
	
	public void setVolumePaths(String [] volumePaths) {
		mVolumePaths = volumePaths;
	}

	@Override
	public int getCount() {
		return mPaths.size();
	}

	@Override
	public String getItem(int position) {
		return mPaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.theme_import_item, null);
			convertView.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			viewHolder = new ViewHolder();
			viewHolder.mIconImage = (ImageView) convertView
					.findViewById(R.id.id_image);
			viewHolder.mTextView = (TextView) convertView
					.findViewById(R.id.id_msg);
			viewHolder.mSelectedImage = (ImageView) convertView
					.findViewById(R.id.id_selected);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String path = getItem(position);
		File file = new File(path);
		boolean directory = file.isDirectory();
		if (directory) {
			viewHolder.mIconImage.setImageResource(R.drawable.theme_listitem_file_logo);
			
			if (mVolumePaths != null) {
				for (int i = 0; i < mVolumePaths.length; i++) {
					if (mVolumePaths[i].equals(path)) {
						
						if (i == 0) {
							viewHolder.mIconImage.setImageResource(R.drawable.theme_path_internal_icon);
						} else if (i == 1) {
							viewHolder.mIconImage.setImageResource(R.drawable.theme_path_external_icon);
						} else if (i == 2) {
							viewHolder.mIconImage.setImageResource(R.drawable.theme_path_usb_icon);
						}
					} 
				}
			}
		} else {
			viewHolder.mIconImage
					.setImageResource(R.drawable.theme_listitem_theme_logo);
		}

		if (!directory) {
			viewHolder.mSelectedImage.setVisibility(View.VISIBLE);
			if (path.equals(mSelectedFile)) {
				viewHolder.mSelectedImage
						.setImageResource(R.drawable.theme_selected_yes);
			} else {
				viewHolder.mSelectedImage
						.setImageResource(R.drawable.theme_selected_no);
			}
		} else {
			viewHolder.mSelectedImage.setVisibility(View.INVISIBLE);
		}

		viewHolder.mTextView.setText(file.getName());

		return convertView;
	}

	private static class ViewHolder {
		public ImageView mIconImage;
		public TextView mTextView;
		public ImageView mSelectedImage;
	}

}
