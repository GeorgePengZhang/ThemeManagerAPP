package com.auratech.theme.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.auratech.theme.R;
import com.auratech.theme.ThemeDetailActivity;
import com.auratech.theme.ThemeOnlineDetailActivity;
import com.auratech.theme.adapter.ThemeOnlineFragmentAdapter;
import com.auratech.theme.bean.ThemeModel;
import com.auratech.theme.executor.RequestCallBackEx;
import com.auratech.theme.executor.ThemeControl;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

/**
 * 在线主题处理类
 * 
 * @Description TODO
 * @author Robin
 * @date
 * @Copyright:
 */
public class ThemeOnlineFragment extends Fragment {
	public static final String THEME_ONLINE = "theme_online"; // 在線主題標籤
	private TextView mHint;
	private LinearLayout mContent;
	private ArrayList<ThemeModel> listData;
	private GridView mGridView;
	private ThemeOnlineFragmentAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listData = new ArrayList<ThemeModel>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.theme_online_layout, container, false);
		mHint = (TextView) view.findViewById(R.id.id_hint);
		mContent = (LinearLayout) view.findViewById(R.id.id_content);
		mGridView = (GridView) view
				.findViewById(R.id.id_gridview_theme_preview);
		mAdapter = new ThemeOnlineFragmentAdapter(getActivity(), listData);
		mGridView.setAdapter(mAdapter);

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						ThemeOnlineDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(THEME_ONLINE, listData.get(position));
				intent.putExtra(THEME_ONLINE, bundle);
				startActivity(intent);
			}

		});
		obtainTheme();
		return view;
	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		obtainTheme() ;
	}

	/**
	 * 
	 * 获取所有的主题信息
	 * 
	 * @Description TODO
	 * @author Robin
	 * @date
	 * @Copyright:
	 */
	private void obtainTheme() {
		ThemeControl.getThemeData(new RequestCallBackEx<String>() {

			@Override
			public void onSuccessEx(ResponseInfo<String> responseInfo) {
				int stutas = ThemeControl.parseStutas(responseInfo.result);
				if (stutas == 0) { // 成功
					ArrayList<ThemeModel> listDatas = ThemeControl
							.parseQueryThemeList(responseInfo.result);
					if (listDatas.size() > 0) { // 有数据 显示 view
						listData.clear();
						mHint.setVisibility(View.GONE);
						mContent.setVisibility(View.VISIBLE);
						listData.addAll(listDatas);
					} else { // 没有数据 隐藏 view
						mContent.setVisibility(View.GONE);
						mHint.setVisibility(View.VISIBLE);
						mHint.setText(getString(R.string.theme_no));
					}

					mAdapter.notifyDataSetChanged();
				} else { // 失败
					Log.e("tagNo", ""+responseInfo.result);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.e("tagNo", ""+msg);
				mContent.setVisibility(View.GONE);
				mHint.setVisibility(View.VISIBLE);
				mHint.setText(getString(R.string.theme_no));
			}

		});

	}

}
