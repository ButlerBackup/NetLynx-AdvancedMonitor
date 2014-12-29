package com.netlynxtech.advancedmonitor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.netlynxtech.advancedmonitor.classes.WebRequestAPI;

public class IndividualDeviceEditDetailsActivity extends ActionBarActivity {
	EditText etInput1, etInput2, etInput3, etInput4, etInput5, etInput6, etInput7, etInput8, etOutput1, etOutput2, etDescriptions;
	Button bUpdate;
	String deviceId = "", deviceDescription = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_edit_details);
		Intent i = getIntent();
		if (i.hasExtra("deviceId")) {
			deviceId = i.getStringExtra("deviceId");
			deviceDescription = i.getStringExtra("deviceDescription");
		} else {
			finish();
		}
		getSupportActionBar().setTitle(deviceDescription);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		etInput1 = (EditText) findViewById(R.id.etInput1);
		if (i.hasExtra("input1")) {
			etInput1.setText(i.getStringExtra("input1"));
		} else {
			etInput1.setEnabled(false);
		}
		etInput2 = (EditText) findViewById(R.id.etInput2);
		if (i.hasExtra("input2")) {
			etInput2.setText(i.getStringExtra("input2"));
		} else {
			etInput2.setEnabled(false);
		}
		etInput3 = (EditText) findViewById(R.id.etInput3);
		if (i.hasExtra("input3")) {
			etInput3.setText(i.getStringExtra("input3"));
		} else {
			etInput3.setEnabled(false);
		}
		etInput4 = (EditText) findViewById(R.id.etInput4);
		if (i.hasExtra("input4")) {
			etInput4.setText(i.getStringExtra("input4"));
		} else {
			etInput4.setEnabled(false);
		}
		etInput5 = (EditText) findViewById(R.id.etInput5);
		if (i.hasExtra("input5")) {
			etInput5.setText(i.getStringExtra("input5"));
		} else {
			etInput5.setEnabled(false);
		}
		etInput6 = (EditText) findViewById(R.id.etInput6);
		if (i.hasExtra("input6")) {
			etInput6.setText(i.getStringExtra("input6"));
		} else {
			etInput6.setEnabled(false);
		}
		etInput7 = (EditText) findViewById(R.id.etInput7);
		if (i.hasExtra("input7")) {
			etInput7.setText(i.getStringExtra("input7"));
		} else {
			etInput7.setEnabled(false);
		}
		etInput8 = (EditText) findViewById(R.id.etInput8);
		if (i.hasExtra("input8")) {
			etInput8.setText(i.getStringExtra("input8"));
		} else {
			etInput8.setEnabled(false);
		}
		etOutput1 = (EditText) findViewById(R.id.etOutput1);
		if (i.hasExtra("output1")) {
			etOutput1.setText(i.getStringExtra("output1"));
		} else {
			etOutput1.setEnabled(false);
		}
		etOutput2 = (EditText) findViewById(R.id.etOutput2);
		if (i.hasExtra("output2")) {
			etOutput2.setText(i.getStringExtra("output2"));
		} else {
			etOutput2.setEnabled(false);
		}
		etDescriptions = (EditText) findViewById(R.id.etDescriptions);
		etDescriptions.setText(deviceDescription);
		bUpdate = (Button) findViewById(R.id.bUpdate);
		bUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new updateDescriptions().execute(etDescriptions.getText().toString(), etInput1.getText().toString(), etInput2.getText().toString(), etInput3.getText().toString(), etInput4.getText()
						.toString(), etInput5.getText().toString(), etInput6.getText().toString(), etInput7.getText().toString(), etInput8.getText().toString(), etOutput1.getText().toString(),
						etOutput2.getText().toString());
			}
		});
	}

	private class updateDescriptions extends AsyncTask<String, Void, Void> {
		String data = "";

		@Override
		protected Void doInBackground(String... params) {
			// data = new WebRequestAPI(IndividualDeviceEditDetailsActivity.this).UpdateDescriptions("", params[0].toString(), params[1].toString(), params[2].toString(), params[3].toString(),
			// params[4].toString());
			data = new WebRequestAPI(IndividualDeviceEditDetailsActivity.this).UpdateDescriptions(deviceId, params[0].toString(), params[1].toString(), params[2].toString(), params[3].toString(),
					params[4].toString(), params[5].toString(), params[6].toString(), params[7].toString(), params[8].toString(), params[9].toString(), params[10].toString());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			IndividualDeviceEditDetailsActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (data.startsWith("success|")) {
						Toast.makeText(IndividualDeviceEditDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(IndividualDeviceEditDetailsActivity.this, data, Toast.LENGTH_SHORT).show();
					}
				}
			});
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
