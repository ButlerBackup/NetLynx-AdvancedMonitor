package com.netlynxtech.advancedmonitor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.netlynxtech.advancedmonitor.fragments.TutorialFragmentAdapter;
import com.securepreferences.SecurePreferences;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class TutorialActivity extends FragmentActivity {

	TutorialFragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SecurePreferences sp = new SecurePreferences(TutorialActivity.this);

		if (sp.getString("initial", "0").equals("1") && !getIntent().hasExtra("addNew")) {
			startActivity(new Intent(TutorialActivity.this, DeviceListActivity.class));
			finish();
		}
		setContentView(R.layout.tutorial_activity);
		mAdapter = new TutorialFragmentAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator = indicator;
		indicator.setViewPager(mPager);

		final float density = getResources().getDisplayMetrics().density;
		indicator.setBackgroundColor(0xFFCCCCCC);
		indicator.setRadius(5 * density);
		indicator.setPageColor(Color.TRANSPARENT);
		indicator.setFillColor(Color.GRAY);
		indicator.setStrokeColor(Color.BLACK);
		indicator.setStrokeWidth(density);
		indicator.setSnap(true);
	}

	@Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			super.onBackPressed();
		} else {
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}
}