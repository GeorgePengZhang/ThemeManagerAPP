package com.auratech.theme.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.auratech.theme.R;

public class ThemeOnlineFragment extends Fragment {

	private TextView mHint;
	private LinearLayout mContent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.theme_online_layout, container, false);
		mHint = (TextView) view.findViewById(R.id.id_hint);
		mContent = (LinearLayout) view.findViewById(R.id.id_content);
		
		mHint.setText("在线 主题更换下载");
		
		return view;
	}
	
}
