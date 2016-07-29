package com.auratech.theme;

import java.util.ArrayList;

import android.R.color;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.auratech.theme.fragment.ThemeLocalFragment;
import com.auratech.theme.fragment.ThemeOnlineFragment;
import com.auratech.theme.utils.ThemeImageLoader;

public class ThemePreviewActivity extends FragmentActivity {
	
	private ViewPager mViewPager;
	private FragmentPagerAdapter mPagerAdapter;
	private ArrayList<Fragment> mFragmentList;
//	private int mScreenW1_2;
//	private View mTabline;
	private TextView mTVLocal;
	private TextView mTVOnline;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_preview_layout);

//		initTabline();
		initViews();
		
		checkUpdateApp();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		ThemeImageLoader.getInstance().cancel();
		Log.d("TAG", "onDestroy ThemePreviewActivity");
	}
	
//	private void initTabline() {
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		int screenW = dm.widthPixels;
//		mScreenW1_2 = screenW / 2;
//		mTabline = findViewById(R.id.id_tabline);
//		LayoutParams lp = mTabline.getLayoutParams();
//		lp.width = mScreenW1_2;
//		mTabline.setLayoutParams(lp);
//	}

	private void initViews() {
		mTVLocal = (TextView) findViewById(R.id.id_tv_local);
		mTVOnline = (TextView) findViewById(R.id.id_tv_online);
		mTVLocal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(0);
			}
		});
		mTVOnline.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(1);
			}
		});
		
		mViewPager = (ViewPager) findViewById(R.id.id_preview_viewpager);
		mFragmentList = new ArrayList<Fragment>();
		
		ThemeLocalFragment localFrag = new ThemeLocalFragment();
		ThemeOnlineFragment onlineFrag = new ThemeOnlineFragment();
		
		mFragmentList.add(localFrag);
		mFragmentList.add(onlineFrag);
		
		mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				return mFragmentList.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				return mFragmentList.get(arg0);
			}
		};
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				resetTabstatus();
				
//				int focuse = getResources().getColor(R.color.tab_text_focuse);
				
				switch (position) {
				case 0:
//					mTVLocal.setTextColor(focuse);
					mTVLocal.setBackgroundResource(R.drawable.theme_top_preview_selected);
					break;
					
				case 1:
//					mTVOnline.setTextColor(focuse);
					mTVOnline.setBackgroundResource(R.drawable.theme_top_preview_selected);
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int position, float offset, int px) {
//				float offsetX = (position+offset) * mScreenW1_2;
//				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabline.getLayoutParams();
//				lp.leftMargin = (int) offsetX;
//				mTabline.setLayoutParams(lp);
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		mViewPager.setAdapter(mPagerAdapter);
	}
	
	private void resetTabstatus() {
//		int normal = getResources().getColor(R.color.tab_text_normal);
		
//		mTVLocal.setTextColor(normal);
//		mTVOnline.setTextColor(normal);
		
		mTVLocal.setBackgroundColor(color.transparent);
		mTVOnline.setBackgroundColor(color.transparent);
	}
	
	
	private void checkUpdateApp() {
        try {
            String packageName = getPackageName();
            //因为这个是google的网址，所以请求的时候需要翻墙在能正常请求到数据(注：这个请求要保证本app已经上传到google play)
            String url = "https://play.google.com/store/apps/details?id="+packageName;

            PackageInfo info = getPackageManager().getPackageInfo(packageName, 0);

            new CheckVersionAsyncTask(ThemePreviewActivity.this, info.versionName).executeOnExecutor(CheckVersionAsyncTask.THREAD_POOL_EXECUTOR, url);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
