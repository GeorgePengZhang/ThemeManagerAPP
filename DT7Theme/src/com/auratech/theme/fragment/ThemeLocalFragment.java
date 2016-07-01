package com.auratech.theme.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.auratech.theme.R;
import com.auratech.theme.ThemeDetailActivity;
import com.auratech.theme.ThemeImportActivity;
import com.auratech.theme.adapter.ThemePreviewViewAdapter;
import com.auratech.theme.bean.DescriptionBean;
import com.auratech.theme.utils.CircleImageView;
import com.auratech.theme.utils.ThemeImageLoader;
import com.auratech.theme.utils.ThemeImageLoader.ThemeImageOptions;
import com.auratech.theme.utils.ThemeResouceManager;

public class ThemeLocalFragment extends Fragment {

	private static final String TAG = "ThemeLocalFragment";
	
	
	private GridView mGridView;
	private ArrayList<DescriptionBean> mList;
	private ThemePreviewViewAdapter mAdapter;

	private TextView mImport;
	private boolean mFirstLoaded;
	private int mfirstVisibleItem;
	private int mVisibleItemCount;
	private LoadedDescription mTask; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mList = new ArrayList<DescriptionBean>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.theme_local_layout, container, false);
		mGridView = (GridView) view.findViewById(R.id.id_gridview_theme_preview);
		mAdapter = new ThemePreviewViewAdapter(getActivity(), mList);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					showImageView(view, mfirstVisibleItem, mVisibleItemCount);
				} else {
					ThemeImageLoader.getInstance().cancel();
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				mfirstVisibleItem = firstVisibleItem;
				mVisibleItemCount = visibleItemCount;
				
				if (mFirstLoaded && visibleItemCount > 0) {
					showImageView(view, firstVisibleItem, visibleItemCount);
					mFirstLoaded = false;
				}
			}
		});
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), ThemeDetailActivity.class);
				intent.putExtra(ThemeDetailActivity.THEME_DETAIL, mList.get(position));
				startActivity(intent);
			}
			
		});
		
		mImport = (TextView) view.findViewById(R.id.id_import);
		mImport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ThemeImportActivity.class);
				startActivity(intent);
			}
		});
		
		return view;
	}
		
	@Override
	public void onResume() {
		super.onResume();
		mTask = new LoadedDescription(mList, mAdapter);
		mTask.execute();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (mTask != null) {
			mTask.cancel(false);
			mTask = null;
		}
	}
	
	public class LoadedDescription extends AsyncTask<Void, Void, Void> {

		private ArrayList<DescriptionBean> list = new ArrayList<DescriptionBean>();
		private ArrayList<DescriptionBean> mMainList;
		private ThemePreviewViewAdapter mMainAdapter;
		
		
		public LoadedDescription(ArrayList<DescriptionBean> list, ThemePreviewViewAdapter adapter) {
			mMainList = list;
			mMainAdapter = adapter;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			String[] themeTotal = ThemeResouceManager.getInstance().getAllLocalTheme();
			list.clear();
			DescriptionBean dbn = ThemeResouceManager.getInstance().getThemeDescriptionBean(ThemeResouceManager.THEME_DEAFULT_ABSOLUTE_PATH);
			if (dbn != null) {
				list.add(dbn);
			}
			
			if (themeTotal != null) {
				for (String theme:themeTotal) {
					DescriptionBean bean = ThemeResouceManager.getInstance().parseDescription(theme);
					if (bean != null) {
						list.add(bean);
					}
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mMainList.clear();
			mMainList.addAll(list);
			mFirstLoaded = true;
			mMainAdapter.notifyDataSetChanged();
		}
	};
	

	private void showImageView(AbsListView view, int firstVisibleItem,
			int visibleItemCount) {
		for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
			DescriptionBean bean = mAdapter.getItem(i);
			String theme = bean.getTheme();
			String themePath = bean.getPath()+theme;
			String themeResource = ThemeResouceManager.THEME_TYPE_PREVIEW+bean.getThumbnails();
			CircleImageView imageView = (CircleImageView) view.findViewWithTag(themePath+"_"+themeResource);
			
			ThemeImageOptions options = new ThemeImageOptions();
			options.width = 200;
			options.height = 200;
			options.imageView = imageView;
					
			ThemeImageLoader.getInstance().loadImage(themePath, themeResource, options);
		}
	}
	
//	private static DescriptionBean getDefaultThemeDescription() {
//		return ThemeResouceManager.getInstance().parseDescription(ThemeResouceManager.THEME_DEAFULT_NAME, ThemeResouceManager.THEME_DEFAULT_PATH);
//	}
}
