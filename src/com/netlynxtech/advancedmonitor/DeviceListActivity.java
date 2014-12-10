package com.netlynxtech.advancedmonitor;

import java.util.ArrayList;

import mehdi.sakout.dynamicbox.DynamicBox;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;
import com.netlynxtech.advancedmonitor.adapters.DevicesAdapter;
import com.netlynxtech.advancedmonitor.classes.Device;
import com.netlynxtech.advancedmonitor.classes.WebRequestAPI;
import com.securepreferences.SecurePreferences;

public class DeviceListActivity extends ActionBarActivity {
	DynamicBox box;
	ArrayList<Device> devices;
	DevicesAdapter adapter;
	ListView lvDevices;
	AsyncTask<Void, Void, Void> task = new getDevice();
	boolean isProcessing = false;
	int index = 0, top = 0;
	RefreshActionItem mRefreshActionItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_list);
		devices = new ArrayList<Device>();
		lvDevices = (ListView) findViewById(R.id.lvDevices);
		box = new DynamicBox(DeviceListActivity.this, lvDevices);
		lvDevices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
				TextView tvDeviceId = (TextView) view.findViewById(R.id.tvDeviceId);
				TextView tvDescription = (TextView) view.findViewById(R.id.tvDeviceDescription);
				Log.e("DEVICEID", tvDeviceId.getText().toString());
				if (devices.size() > 0) {
					Device d = devices.get(position);
					startActivity(new Intent(DeviceListActivity.this, IndividualDeviceActivity.class).putExtra("deviceId", tvDeviceId.getText().toString().trim())
							.putExtra("deviceDescription", tvDescription.getText().toString().trim()).putExtra("device", d));
				} else {
					Toast.makeText(DeviceListActivity.this, "Unable to get device. Make sure internet connection is turned on and refresh", Toast.LENGTH_LONG).show();
				}
			}
		});
		box.setClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					task = null;
					task = new getDevice();
					task.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void processData() {
		if (!isProcessing) {
			Log.e("PROCESSDATA", "PROCESSDATA");

			(new Thread(new Runnable() {

				@Override
				public void run() {
					if (!Thread.interrupted())
						try {
							Thread.sleep(15000);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if (task != null) {
										task = null;
										task = new getDevice();
										task.execute();
									}
								}
							});
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
			})).start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_device_list, menu);
		MenuItem item = menu.findItem(R.id.menu_individual_refresh);
		mRefreshActionItem = (RefreshActionItem) MenuItemCompat.getActionView(item);
		mRefreshActionItem.setMenuItem(item);
		mRefreshActionItem.setProgressIndicatorType(ProgressIndicatorType.INDETERMINATE);
		mRefreshActionItem.setRefreshActionListener(new RefreshActionListener() {

			@Override
			public void onRefreshButtonClick(RefreshActionItem sender) {
				try {
					task = null;
					task = new getDevice();
					task.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			task = null;
			task = new getDevice();
			task.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add_device:
			startActivity(new Intent(DeviceListActivity.this, TutorialActivity.class).putExtra("addNew", "1"));
			break;
		case R.id.menu_add_members:
			Bundle information = new Bundle();
			Intent i = new Intent(DeviceListActivity.this, RegisterPhoneActivity.class);
			information.putSerializable("devices", devices);
			i.putExtras(information);
			startActivity(i);
			break;
		case R.id.menu_show_requests:
			startActivity(new Intent(DeviceListActivity.this, ReceivedMemberPermissionActivity.class));
			break;
		case R.id.menu_show_messages:
			startActivity(new Intent(DeviceListActivity.this, MessagesActivity.class));

			break;
		case R.id.menu_settings:
			startActivity(new Intent(DeviceListActivity.this, SettingsActivity.class));

			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	private class getDevice extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Log.e("getDevice", "loading..");
			isProcessing = true;
			box.showLoadingLayout();
			mRefreshActionItem.showProgress(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			devices = new WebRequestAPI(DeviceListActivity.this).GetDevices();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			DeviceListActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mRefreshActionItem.showProgress(false);
					box.hideAll();
					if (!isCancelled()) {
						Log.e("DEVICE SIZE", devices.size() + "");
						if (devices.size() > 0) {
							adapter = new DevicesAdapter(DeviceListActivity.this, devices);
							lvDevices.setAdapter(adapter);
							// adapter = new DevicesAdapter(DeviceListActivity.this, devices);
							index = lvDevices.getFirstVisiblePosition();
							View v = lvDevices.getChildAt(0);
							top = (v == null) ? 0 : v.getTop();
							Log.e("Cancelled", "Not cancelled");
							box.hideAll();
							lvDevices.setSelectionFromTop(index, top);
						} else {
							box.setExceptionMessageColor("#ff0040");
							box.setExceptionTitleColor("#ff0040");
							box.setOtherExceptionTitle("No devices found");
							box.setOtherExceptionMessage("No devices found");
							box.showExceptionLayout();
						}
					}
					isProcessing = false;
					SecurePreferences sp = new SecurePreferences(DeviceListActivity.this);
					if (!sp.getString("initial", "").equals("")) {
						processData();
					}
				}
			});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
			task = null;
			isProcessing = false;
		}
	}

}
