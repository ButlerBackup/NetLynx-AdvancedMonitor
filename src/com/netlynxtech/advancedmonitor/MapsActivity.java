package com.netlynxtech.advancedmonitor;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.netlynxtech.advancedmonitor.classes.Device;
import com.netlynxtech.advancedmonitor.classes.WebRequestAPI;

public class MapsActivity extends ActionBarActivity {
	private GoogleMap googleMap;
	Button bUpdateLocation;
	Device device;
	ArrayList<String> data = new ArrayList<String>();
	getLocation mTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_map);
		device = (Device) getIntent().getSerializableExtra("device");
		bUpdateLocation = (Button) findViewById(R.id.bGetCurrentLocation);
		initilizeMap();
		bUpdateLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String buttonText = bUpdateLocation.getText().toString();
				if (buttonText.equals(MapsActivity.this.getResources().getString(R.string.site_location_get_location))) { // get current location
					bUpdateLocation.setEnabled(false);
					bUpdateLocation.setText(MapsActivity.this.getResources().getString(R.string.site_location_loading));
					Location myLocation = googleMap.getMyLocation();
					if (myLocation != null) {
						LatLng sydney = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
						googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
						googleMap.addMarker(new MarkerOptions().title(device.getDescription()).position(sydney));
						bUpdateLocation.setText(MapsActivity.this.getResources().getString(R.string.site_location_show_text));
					} else {
						Toast.makeText(MapsActivity.this, "Unable to get your current location. Do make sure your GPS is turned on and/or click the compass on the top right of the maps",
								Toast.LENGTH_SHORT).show();
						bUpdateLocation.setEnabled(true);
						bUpdateLocation.setText(MapsActivity.this.getResources().getString(R.string.site_location_get_location));
					}
					bUpdateLocation.setEnabled(true);
				} else if (buttonText.equals(MapsActivity.this.getResources().getString(R.string.site_location_show_text))) {
					AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
					builder.setMessage("Are you sure you're on the right location? You can do so by turning on your GPS and clicking the compass icon on the top right corner of the map.")
							.setPositiveButton("Yes", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									try {
										Location loc = googleMap.getMyLocation();
										if (loc != null) {
											Log.e("Location", loc.getLatitude() + "|" + loc.getLongitude());
											// progressGenerator.updateLocation(bUpdateLocation, loc, deviceID, MapsActivity.this);
											new updateLocation().execute(String.valueOf(loc.getLatitude()), String.valueOf(loc.getLongitude()), device.getDeviceID());
										}
									} catch (Exception e) {
										e.printStackTrace();
										Toast.makeText(MapsActivity.this, "Unable to get your location. Make sure Google Map above is loaded and your network is stable.", Toast.LENGTH_LONG).show();
									}
								}
							}).setNegativeButton("No", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									bUpdateLocation.setEnabled(true);
								}
							}).show();
				}
			}
		});
		mTask = null;
		mTask = new getLocation();
		mTask.execute();
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

	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		}
		// double latitude = Double.parseDouble(data.get(0));
		// double longitude = Double.parseDouble(data.get(1));
		double latitude = 1.358364;
		double longitude = 103.833461;

		LatLng deviceLocation = new LatLng(latitude, longitude);
		googleMap.setMyLocationEnabled(true);
		MarkerOptions marker = new MarkerOptions().position(deviceLocation).title(device.getDescription());
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, 17));
		googleMap.addMarker(marker);
		if (googleMap == null) {
			Log.e("MapsActivity", "Error loading map");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// initilizeMap();
	}

	private class getLocation extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			data = new WebRequestAPI(MapsActivity.this).GetLocation(device.getDeviceID());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			MapsActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					try {
						initilizeMap();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	class updateLocation extends AsyncTask<String, Void, Void> {
		boolean error = false;
		String data = "";

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			MapsActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (data.equals("success")) {
						bUpdateLocation.setText("Success!");
						bUpdateLocation.setBackgroundColor(Color.parseColor("#009C12"));
						bUpdateLocation.setTextColor(Color.parseColor("#FFFFFF"));
					} else {
						bUpdateLocation.setEnabled(true);
						Toast.makeText(MapsActivity.this, "Unable to update location. Please contact admin", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}

		@Override
		protected Void doInBackground(String... params) {
			data = new WebRequestAPI(MapsActivity.this).UpdateLocation(params[2], params[0], params[1]);
			return null;
		}
	}
}
