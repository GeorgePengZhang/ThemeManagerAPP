package com.auratech.theme;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.auratech.theme.adapter.ThemeImportViewAdapter;
import com.auratech.theme.utils.FileCopyManager;
import com.auratech.theme.utils.ThemeResouceManager;

public class ThemeImportActivity extends Activity {

	private LinearLayout mBack;
	private String[] mVolumePaths;
	private ListView mListView;
	private ArrayList<String> mListPath;
	private ThemeImportViewAdapter mAdapter;
	private String mRootPath;
	private String mCurrentpath;
	private TextView mTitle;
	private ViewGroup mHint;
	private ViewGroup mContent;
	private TextView mSure;
	private TextView mCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_import_layout);

		initTab();
		init();
		initBottom();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.d("TAG", "onDestroy ThemeImportActivity");
	}

	private void initBottom() {
		mSure = (TextView) findViewById(R.id.id_sure);
		mCancel = (TextView) findViewById(R.id.id_cancel);
		mSure.setBackgroundResource(R.drawable.theme_import_btn_disable);
		mSure.setTextColor(getResources().getColor(R.color.bottom_btn_disable));
		mSure.setEnabled(false);
		mSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String selected = mAdapter.getSelected();

				// 将选中arz文件 拷贝到theme/.data/目录下
				if (!TextUtils.isEmpty(selected)) {
					mSure.setEnabled(false);
					mCancel.setEnabled(false);
					
					
					final AlertDialog dialog = new AlertDialog.Builder(ThemeImportActivity.this).create();
					dialog.setCancelable(false);
					dialog.show();
					
					Window window = dialog.getWindow();
					window.setContentView(R.layout.theme_dialog_import_layout);
					WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
					layoutParams.gravity = Gravity.BOTTOM;
					layoutParams.width = 600;
					layoutParams.height = 150;
					dialog.getWindow().setAttributes(layoutParams);
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							File sourceFile = new File(selected);

							String fileName = sourceFile.getName();
							File dirPath = ThemeResouceManager.getInstance().getThemeDatePath();
							File destFile = new File(dirPath, fileName);

							try {
								FileCopyManager.copyFile(sourceFile, destFile);
								sourceFile.delete();
							} catch (IOException e) {
								e.printStackTrace();

								if (destFile.exists()) {
									destFile.delete();
								}
							}
							
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									dialog.dismiss();
									onBackPressed();
								}
							});
						}
					}).start();
				}
			}
		});
		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void init() {
		mHint = (ViewGroup) findViewById(R.id.id_ll_hint);
		mContent = (ViewGroup) findViewById(R.id.id_container);
		mListPath = new ArrayList<String>();
		mVolumePaths = getVolumePaths();

		File file = new File(mVolumePaths[0]);
		File parentFile = file.getParentFile();
		mRootPath = parentFile.getPath();
		mCurrentpath = mRootPath;

		for (int i = 0; i < mVolumePaths.length; i++) {
			mListPath.add(mVolumePaths[i]);
		}

		mListView = (ListView) findViewById(R.id.id_listview);
		mAdapter = new ThemeImportViewAdapter(this, mListPath);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentpath = mListPath.get(position);
				refreshListItems(mCurrentpath);
			}
		});
		
		mAdapter.setVolumePaths(mVolumePaths);
	}

	private void initTab() {
		mBack = (LinearLayout) findViewById(R.id.id_back);
		mTitle = (TextView) mBack.getChildAt(1);
		mTitle.setText(R.string.theme_importlocalthemes);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCurrentpath.equals(mRootPath)) {
					onBackPressed();
				} else {
					File file = new File(mCurrentpath);
					if (file.isDirectory()) {
						mCurrentpath = file.getParent();
					} else {
						mCurrentpath = file.getParentFile().getParent();
					}

					refreshListItems(mCurrentpath);
				}
			}
		});
	}

	/**
	 * 获取外部存储设备路径
	 * 
	 * @return
	 */
	private String[] getVolumePaths() {
		String paths[] = null;
		StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
		try {
			Class<?>[] paramClasses = {};
			Method getVolumePathsMethod = StorageManager.class.getMethod(
					"getVolumePaths", paramClasses);
			getVolumePathsMethod.setAccessible(true);
			Object[] params = {};
			paths = (String[]) getVolumePathsMethod.invoke(storageManager,
					params);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return paths;
	}

	/**
	 * 刷新当前的路径
	 * 
	 * @param currentPath
	 */
	private void refreshListItems(String currentPath) {
		File file = new File(currentPath);
		mAdapter.setSelected(null);
		mSure.setBackgroundResource(R.drawable.theme_import_btn_disable);
		mSure.setTextColor(getResources().getColor(R.color.bottom_btn_disable));
		mSure.setEnabled(false);
		if (currentPath.equals(mRootPath)) {
			mListPath.clear();
			for (int i = 0; i < mVolumePaths.length; i++) {
				mListPath.add(mVolumePaths[i]);
			}
			mAdapter.notifyDataSetChanged();
			mTitle.setText(R.string.theme_importlocalthemes);
			mHint.setVisibility(View.INVISIBLE);
			mContent.setVisibility(View.VISIBLE);
		} else {
			if (file.isDirectory()) {
				mListPath.clear();
				File[] listFiles = file.listFiles(new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						String name = pathname.getName();
						boolean startsWith = name.startsWith(".");

						// 只获取文件夹和arz文件
						if (pathname.isDirectory()) {
							return !startsWith;
						} else {
							return name.toLowerCase(Locale.getDefault()).endsWith(".arz");
						}
					}
				});

				if (listFiles != null && listFiles.length > 0) {
					for (int i = 0; i < listFiles.length; i++) {
						String path = listFiles[i].getPath();
						mListPath.add(path);
					}
					mHint.setVisibility(View.INVISIBLE);
					mContent.setVisibility(View.VISIBLE);
				} else {
					mHint.setVisibility(View.VISIBLE);
					mContent.setVisibility(View.INVISIBLE);
				}

				mAdapter.notifyDataSetChanged();
				mTitle.setText(currentPath);
			} else {
				mAdapter.setSelected(currentPath);
				mSure.setBackgroundResource(R.drawable.theme_import_btn);
				mSure.setTextColor(getResources().getColor(R.color.bottom_btn_focuse));
				mSure.setEnabled(true);
				mAdapter.notifyDataSetChanged();
			}
		}
	}
}
