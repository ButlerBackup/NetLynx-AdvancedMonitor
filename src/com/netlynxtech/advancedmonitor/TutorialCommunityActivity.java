package com.netlynxtech.advancedmonitor;

import com.securepreferences.SecurePreferences;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class TutorialCommunityActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tutorial_community_fragment_two_layout);
		findViewById(R.id.bSetup).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new SecurePreferences(TutorialCommunityActivity.this).edit().putString("initial", "1").commit();
				startActivity(new Intent(TutorialCommunityActivity.this, RegisterPhoneActivity.class).putExtra("tutorialOnly", true));
				finish();
			}
		});
	}

}