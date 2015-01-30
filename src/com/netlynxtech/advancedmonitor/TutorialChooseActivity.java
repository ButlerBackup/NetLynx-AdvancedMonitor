package com.netlynxtech.advancedmonitor;

import com.securepreferences.SecurePreferences;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class TutorialChooseActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial_choose);
		final SecurePreferences sp = new SecurePreferences(TutorialChooseActivity.this);

		if (sp.getString("initial", "0").equals("1") && !getIntent().hasExtra("addNew")) {
			startActivity(new Intent(TutorialChooseActivity.this, DeviceListActivity.class));
			finish();
		}
		findViewById(R.id.bJoinCommunity).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sp.edit().putString("initial", "1").commit();
				startActivity(new Intent(TutorialChooseActivity.this, TutorialCommunityActivity.class));
				finish();
			}
		});
		findViewById(R.id.bAddOwnDevice).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sp.edit().putString("initial", "1").commit();
				startActivity(new Intent(TutorialChooseActivity.this, TutorialActivity.class));
				finish();
			}
		});
	}
}
