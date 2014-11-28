package com.netlynxtech.advancedmonitor;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.netlynxtech.advancedmonitor.classes.Device;
import com.netlynxtech.advancedmonitor.classes.WebRequestAPI;

public class MemberAddNewActivity extends ActionBarActivity {
	ArrayList<Device> devices;
	Spinner sRoles, sDevices;
	Button bSendRequest;
	EditText etPhoneNumber;
	ArrayList<String> deviceIDs, deviceNames, rolesValueArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_add_new);
		sRoles = (Spinner) findViewById(R.id.sRoles);
		sDevices = (Spinner) findViewById(R.id.sDevices);
		bSendRequest = (Button) findViewById(R.id.bSendRequest);
		etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		devices = (ArrayList<Device>) getIntent().getSerializableExtra("devices");
		if (devices.size() < 1) {
			Toast.makeText(MemberAddNewActivity.this, "You have no devices", Toast.LENGTH_SHORT).show();
			finish();
		}
		deviceIDs = new ArrayList<String>();
		deviceNames = new ArrayList<String>();
		for (Device d : devices) {
			deviceIDs.add(d.getDeviceID());
			deviceNames.add(d.getDescription());
		}

		ArrayAdapter<String> deviceNamesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, deviceNames);
		sDevices.setAdapter(deviceNamesAdapter);

		String[] rolesText = getResources().getStringArray(R.array.roles_array_text_string);
		ArrayList<String> rolesTextArray = new ArrayList<String>(Arrays.asList(rolesText));
		ArrayAdapter<String> rolesTextAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, rolesTextArray);
		sRoles.setAdapter(rolesTextAdapter);

		String[] rolesValue = getResources().getStringArray(R.array.roles_array_value);
		rolesValueArray = new ArrayList<String>(Arrays.asList(rolesValue));
		for (String s : rolesValue) {
			Log.e("ROLES", s);
		}
		bSendRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etPhoneNumber.getText().toString().trim().length() > 0) {
					String deviceIDToSend = deviceIDs.get(sDevices.getSelectedItemPosition());
					String deviceRoleToSend = rolesValueArray.get(sRoles.getSelectedItemPosition());
					Log.e("Assign", deviceIDToSend + "|" + deviceRoleToSend);
					bSendRequest.setEnabled(false);
					new assignMember().execute(deviceIDToSend, deviceRoleToSend);
				} else {
					Toast.makeText(MemberAddNewActivity.this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private class assignMember extends AsyncTask<String, Void, Void> {
		String res = "";

		@Override
		protected Void doInBackground(String... params) {
			res = new WebRequestAPI(MemberAddNewActivity.this).AssignMemberToDevice(etPhoneNumber.getText().toString().trim(), params[0], params[1]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			MemberAddNewActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					bSendRequest.setEnabled(true);
					if (res.equals("success")) {
						Toast.makeText(MemberAddNewActivity.this, "Successfully sent invite", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MemberAddNewActivity.this, res, Toast.LENGTH_SHORT).show();
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
