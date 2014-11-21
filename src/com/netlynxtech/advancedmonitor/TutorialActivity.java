package com.netlynxtech.advancedmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.netlynxtech.advancedmonitor.fragments.TutorialOneFragment;
import com.netlynxtech.advancedmonitor.fragments.TutorialThreeFragment;
import com.netlynxtech.advancedmonitor.fragments.TutorialTwoFragment;
import com.securepreferences.SecurePreferences;

public class TutorialActivity extends ActionBarActivity {
	private static final int NUM_PAGES = 3;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SecurePreferences sp = new SecurePreferences(TutorialActivity.this);

		if (sp.getString("initial", "0").equals("1") && !getIntent().hasExtra("addNew")) {
			startActivity(new Intent(TutorialActivity.this, DeviceListActivity.class));
			finish();
		}
		setContentView(R.layout.tutorial_activity);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		getSupportActionBar().setTitle(R.string.activity_tutorial);
	}

	@Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			super.onBackPressed();
		} else {
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment f = new Fragment();
			switch (position) {
			case 0:
				f = new TutorialOneFragment();
				break;
			case 1:
				f = new TutorialTwoFragment();
				break;
			case 2:
				f = new TutorialThreeFragment();
				break;
			}
			return f;
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}
}