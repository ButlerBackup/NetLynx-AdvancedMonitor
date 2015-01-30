package com.netlynxtech.advancedmonitor;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.netlynxtech.advancedmonitor.classes.Consts;
import com.netlynxtech.advancedmonitor.classes.Device;
import com.netlynxtech.advancedmonitor.classes.ProgressGenerator;
import com.netlynxtech.advancedmonitor.classes.ProgressGenerator.OnCompleteListener;
import com.netlynxtech.advancedmonitor.classes.Utils;

public class CheckPinActivity extends ActionBarActivity {
	ArrayList<Device> devices;
	EditText etPinNo;
	ActionProcessButton bCheckPin;
	TextView tvError, tvStatusDesc, tvGCMID;
	Bundle information;
	getGCMID mTask;
	GoogleCloudMessaging gcm;
	String GCM_register_ID = "";
	boolean isTutorial = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_pin_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(CheckPinActivity.this.getResources().getString(R.string.check_pin_name));
		if (getIntent().hasExtra("tutorialOnly")) {
			isTutorial = true;
		}
		tvError = (TextView) findViewById(R.id.tvError);
		tvGCMID = (TextView) findViewById(R.id.tvGCMID);
		etPinNo = (EditText) findViewById(R.id.etPinNo);
		devices = (ArrayList<Device>) getIntent().getSerializableExtra("devices");
		tvStatusDesc = (TextView) findViewById(R.id.tvStatusDesc);
		bCheckPin = (ActionProcessButton) findViewById(R.id.bCheckPin);
		bCheckPin.setMode(ActionProcessButton.Mode.ENDLESS);
		etPinNo.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString().length() > 0) {
					bCheckPin.setEnabled(true);
				} else {
					bCheckPin.setEnabled(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		final ProgressGenerator progressGenerator = new ProgressGenerator(new OnCompleteListener() {

			@Override
			public void onComplete() {
				if (!bCheckPin.getText().toString().equals("success")) {
					tvError.setText(bCheckPin.getText().toString());
					etPinNo.setEnabled(true);
					bCheckPin.setEnabled(true);
					bCheckPin.setProgress(0);
					tvError.setVisibility(View.VISIBLE);
				} else {
					new Utils(CheckPinActivity.this).setGCMID(GCM_register_ID);
					if (!isTutorial) {
						Bundle information = new Bundle();
						Intent i = new Intent(CheckPinActivity.this, MemberAddNewActivity.class);
						information.putSerializable("devices", devices);
						i.putExtras(information);
						startActivity(i);
						finish();
					} else {
						Toast.makeText(CheckPinActivity.this, "Successfully signed up!", Toast.LENGTH_LONG).show();
						finish();
					}
				}
			}
		});

		bCheckPin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (etPinNo.getText().toString().length() > 0) {
					if (GCM_register_ID.length() > 0) {
						etPinNo.setEnabled(false);
						etPinNo.setEnabled(false);
						bCheckPin.setEnabled(false);
						progressGenerator.verifyPin(bCheckPin, etPinNo.getText().toString().trim(), GCM_register_ID, CheckPinActivity.this);
					} else {
						Toast.makeText(CheckPinActivity.this, "Unable to get Google Cloud Messging ID.. Please make sure you have a stable internet connection.", Toast.LENGTH_LONG).show();
					}
				} else {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText("PIN do not match");
				}
			}
		});

		mTask = null;
		mTask = new getGCMID();
		mTask.execute();
	}

	public void retryGetGCMID() {
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				if (mTask != null && mTask.getStatus() != Status.RUNNING) {
					mTask = null;
					mTask = new getGCMID();
					mTask.execute();
				}
			}
		}, 5000);
	}

	private class getGCMID extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
				}
				GCM_register_ID = gcm.register(Consts.PROJECT_NUMBER);
				Log.e("RegisteredID", GCM_register_ID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			CheckPinActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					tvError.setVisibility(View.VISIBLE);
					if (GCM_register_ID.length() > 0) {
						tvGCMID.setVisibility(View.GONE);
						tvError.setText(CheckPinActivity.this.getString(R.string.gcm_id_retrieved_successful));
						bCheckPin.setEnabled(true);
					} else {
						tvGCMID.setText(CheckPinActivity.this.getString(R.string.gcm_id_retrieved_retrying));
						tvError.setText(CheckPinActivity.this.getString(R.string.gcm_id_retrieved_failed));
						retryGetGCMID();
					}
				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mTask != null && mTask.getStatus() == Status.RUNNING) {
			mTask.cancel(true);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mTask != null && mTask.getStatus() == Status.RUNNING) {
			mTask.cancel(true);
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
