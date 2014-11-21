package com.netlynxtech.advancedmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SetupSuccessfulFinalActivity extends ActionBarActivity {
	Button bFinishSetup;
	String deviceId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_successful_final);
		bFinishSetup = (Button) findViewById(R.id.bFinishSetup);
		bFinishSetup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(SetupSuccessfulFinalActivity.this, DeviceListActivity.class));
				finish();
			}
		});
	}

}
