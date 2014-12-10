package com.netlynxtech.advancedmonitor;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutUsActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(getResources().getString(R.string.about_us_title));
		setContentView(R.layout.about_us_activity);
		String v = "1";
		try {
			v = getPackageManager().getPackageInfo(getPackageName(), 0).versionName; // get version of this package (in manifest)
			((TextView) findViewById(R.id.tvVersion)).setText(AboutUsActivity.this.getResources().getString(R.string.version) + " " + v);
		} catch (Exception e) {
			((TextView) findViewById(R.id.tvVersion)).setText(AboutUsActivity.this.getResources().getString(R.string.version) + " 1.0.0"); // else v1.0.0
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}
}
