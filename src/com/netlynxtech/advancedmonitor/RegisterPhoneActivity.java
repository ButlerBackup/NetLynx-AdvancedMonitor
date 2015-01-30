package com.netlynxtech.advancedmonitor;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.netlynxtech.advancedmonitor.classes.Consts;
import com.netlynxtech.advancedmonitor.classes.Device;
import com.netlynxtech.advancedmonitor.classes.ProgressGenerator;
import com.netlynxtech.advancedmonitor.classes.ProgressGenerator.OnCompleteListener;
import com.netlynxtech.advancedmonitor.classes.Utils;
import com.securepreferences.SecurePreferences;

public class RegisterPhoneActivity extends ActionBarActivity {
	SecurePreferences sp;
	EditText etPhoneNo, etName;
	ActionProcessButton bRequestPin;
	Bundle information;
	ArrayList<Device> devices;
	TextView tvError;
	boolean isTutorial = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = new SecurePreferences(RegisterPhoneActivity.this);
		information = getIntent().getExtras();
		if (getIntent().hasExtra("tutorialOnly")) {
			isTutorial = true;
		}
		devices = (ArrayList<Device>) getIntent().getSerializableExtra("devices");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		if (!sp.getString(Consts.PREFERENCES_PHONE_NO, "").equals("") && !sp.getString(Consts.PREFERENCES_GCMID, "").equals("")) {
			if (!isTutorial) {
				Bundle information = new Bundle();
				Intent i = new Intent(RegisterPhoneActivity.this, MemberAddNewActivity.class);
				information.putSerializable("devices", devices);
				i.putExtras(information);
				startActivity(i);
				finish();
			} else {
				Toast.makeText(RegisterPhoneActivity.this, "You're already signed up!", Toast.LENGTH_SHORT).show();
				finish();
			}
		} else {
			setContentView(R.layout.activity_register_phone);
			tvError = (TextView) findViewById(R.id.tvError);

			etPhoneNo = (EditText) findViewById(R.id.etPhoneNo);
			etName = (EditText) findViewById(R.id.etName);
			bRequestPin = (ActionProcessButton) findViewById(R.id.bRequestPin);
			bRequestPin.setMode(ActionProcessButton.Mode.ENDLESS);
			final ProgressGenerator progressGenerator = new ProgressGenerator(new OnCompleteListener() {

				@Override
				public void onComplete() {
					if (!bRequestPin.getText().toString().equals("success")) {
						tvError.setText(bRequestPin.getText().toString());
						etPhoneNo.setEnabled(true);
						bRequestPin.setEnabled(true);
						bRequestPin.setProgress(0);
						tvError.setVisibility(View.VISIBLE);
					} else {
						new Utils(RegisterPhoneActivity.this).setPhoneNumber((etPhoneNo.getText().toString().trim()));
						Bundle information = new Bundle();
						Intent i = new Intent(RegisterPhoneActivity.this, CheckPinActivity.class);
						information.putSerializable("devices", devices);
						i.putExtra("tutorialOnly", isTutorial);
						i.putExtras(information);
						startActivity(i);
						finish();
					}
				}
			});
			bRequestPin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (etPhoneNo.getText().toString().trim().length() > 0 && etName.getText().toString().trim().length() > 0) {
						etPhoneNo.setEnabled(false);
						etName.setEnabled(false);
						bRequestPin.setEnabled(false);
						progressGenerator.requestPin(bRequestPin, etPhoneNo.getText().toString().trim(), etName.getText().toString().trim(), RegisterPhoneActivity.this);
					} else {
						Toast.makeText(RegisterPhoneActivity.this, "Please input your phone number and name to register phone.", Toast.LENGTH_SHORT).show();
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
