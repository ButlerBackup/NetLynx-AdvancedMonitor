package com.netlynxtech.advancedmonitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.netlynxtech.advancedmonitor.classes.WebRequestAPI;
import com.securepreferences.SecurePreferences;

public class SetupSuccessfulActivity extends ActionBarActivity {
	Button bFinalSetup;
	String deviceId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_successful);
		Intent i = getIntent();
		if (i.hasExtra("deviceId")) {
			deviceId = i.getStringExtra("deviceId");
			Log.e("DEVICEID", deviceId);
		} else {
			finish();
		}
		bFinalSetup = (Button) findViewById(R.id.bFinalSetup);
		bFinalSetup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new finalSetup().execute();
			}
		});
	}

	private class finalSetup extends AsyncTask<Void, Void, Void> {
		String data = "";
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(SetupSuccessfulActivity.this);
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.setMessage("Finalizing registration.. Contacting server..");
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			data = new WebRequestAPI(SetupSuccessfulActivity.this).RegisterDevice(deviceId);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			SetupSuccessfulActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (data.equals("success")) {
						SecurePreferences sp = new SecurePreferences(SetupSuccessfulActivity.this);
						sp.edit().putString("initial", "1").commit();
						startActivity(new Intent(SetupSuccessfulActivity.this, SetupSuccessfulFinalActivity.class));
						finish();
					} else {
						Toast.makeText(SetupSuccessfulActivity.this, data, Toast.LENGTH_SHORT).show();
					}
				}
			});
		}

	}
}
