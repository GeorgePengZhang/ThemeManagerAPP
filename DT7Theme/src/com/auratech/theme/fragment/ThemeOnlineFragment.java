package com.auratech.theme.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.auratech.theme.ThemeOnlineDetailActivity;
import com.auratech.theme.adapter.ThemeOnlineFragmentAdapter;
import com.auratech.theme.bean.ThemeInfoBean;
import com.auratech.theme.utils.HttpConfig;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 在线主题处理类
 * 
 * @author Robin
 * @date
 * @Copyright:
 */
public class ThemeOnlineFragment extends Fragment {
	private TextView mHint;
	private LinearLayout mContent;
	private ArrayList<ThemeInfoBean> listData;
	private GridView mGridView;
	private ThemeOnlineFragmentAdapter mAdapter;
	private HttpUtils httpUtils;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listData = new ArrayList<ThemeInfoBean>();
		httpUtils = new HttpUtils();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.theme_online_layout, container, false);
		mHint = (TextView) view.findViewById(R.id.id_hint);
		mContent = (LinearLayout) view.findViewById(R.id.id_content);
		mGridView = (GridView) view.findViewById(R.id.id_gridview_theme_preview);
		mAdapter = new ThemeOnlineFragmentAdapter(getActivity(), listData);
		mGridView.setAdapter(mAdapter);

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),ThemeOnlineDetailActivity.class);
				intent.putExtra(ThemeOnlineDetailActivity.THEME_ONLINE, listData.get(position));
				startActivity(intent);
			}

		});
		return view;
	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		obtainTheme() ;
	}

	/**
	 * 
	 * 获取网络所有的主题信息
	 * 
	 * @author Robin
	 * @date
	 * @Copyright:
	 */
	private void obtainTheme() {
		httpUtils.send(HttpMethod.POST, HttpConfig.URL_QUERY_THEMES, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.d("TAG", "onFailure:"+msg);
				mContent.setVisibility(View.GONE);
				mHint.setVisibility(View.VISIBLE);
				mHint.setText(getResources().getString(R.string.theme_no));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
//				Log.d("TAG", "onSuccess:"+responseInfo.result);
				
				ArrayList<ThemeInfoBean> list = parseThemeInfo(responseInfo.result);
				if (list.size() > 0) { // 有数据 显示 view
					listData.clear();
					mHint.setVisibility(View.GONE);
					mContent.setVisibility(View.VISIBLE);
					listData.addAll(list);
				} else { // 没有数据 隐藏 view
					mContent.setVisibility(View.GONE);
					mHint.setVisibility(View.VISIBLE);
					mHint.setText(getString(R.string.theme_no));
				}

				mAdapter.notifyDataSetChanged();
			}
		} );
	}
	
	
	/**
	 * 获取网络主题信息
	 * @param result 返回网络主题的相关配置信息
	 * @return
	 */
	public ArrayList<ThemeInfoBean> parseThemeInfo(String result) {
		ArrayList<ThemeInfoBean> list = new ArrayList<ThemeInfoBean>();
		
		try {
			JSONObject jo = new JSONObject(result);
			int resultCode = jo.getInt(ThemeInfoBean.RESULTCODE);
			if (resultCode == 0) {
				JSONObject joResult = jo.getJSONObject(ThemeInfoBean.RESULT);
				JSONArray jaThemeInfo = joResult.getJSONArray(ThemeInfoBean.THEMEINFO);
				
				
				
				for (int i = 0; i < jaThemeInfo.length(); i++) {
					JSONObject joTI = jaThemeInfo.getJSONObject(i);
					ThemeInfoBean bean = new ThemeInfoBean();
					
					String name = joTI.getString(ThemeInfoBean.NAME);
					String thumbnails = joTI.getString(ThemeInfoBean.THUMBNAILS);
					String themefile = joTI.getString(ThemeInfoBean.THEMEFILE);
					String previewPath = joTI.getString(ThemeInfoBean.PREVIEWPATH);
					
					bean.setName(name);
					bean.setThumbnails(thumbnails);
					bean.setThemefile(themefile);
					bean.setPreviewPath(previewPath);
					
					list.add(bean);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace(); 
			list.clear();
		}
		
		return list;
	}
}
