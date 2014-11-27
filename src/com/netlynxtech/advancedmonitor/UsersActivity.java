package com.netlynxtech.advancedmonitor;

import java.util.ArrayList;

import mehdi.sakout.dynamicbox.DynamicBox;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.netlynxtech.advancedmonitor.adapters.MembersAdapter;
import com.netlynxtech.advancedmonitor.classes.DeviceMembers;
import com.netlynxtech.advancedmonitor.classes.WebRequestAPI;

public class UsersActivity extends ActionBarActivity {
	ArrayList<DeviceMembers> data = new ArrayList<DeviceMembers>();
	String deviceId;
	DynamicBox box;
	ListView lvUsers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.activity_users));
		if (!getIntent().hasExtra("deviceId")) {
			finish();
		}
		deviceId = getIntent().getStringExtra("deviceId");
		setContentView(R.layout.activity_users);
		lvUsers = (ListView) findViewById(R.id.lvUsers);
		box = new DynamicBox(UsersActivity.this, lvUsers);
		new getMembers().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.menu_users, menu);
		return true;
	}

	private class getMembers extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			box.showLoadingLayout();
		}

		@Override
		protected Void doInBackground(Void... params) {
			data = new WebRequestAPI(UsersActivity.this).GetMembers(deviceId);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			UsersActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (data != null && data.size() > 0) {
						MembersAdapter adapter = new MembersAdapter(UsersActivity.this, data, deviceId);
						lvUsers.setAdapter(adapter);
						box.hideAll();
					} else {
						box.setOtherExceptionTitle("No users");
						box.setOtherExceptionMessage("No Users");
						box.showExceptionLayout();
					}
				}
			});
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_users:
			break;
		case android.R.id.home:
			finish();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}
}
