package com.netlynxtech.advancedmonitor;

import java.util.ArrayList;
import java.util.HashMap;

import mehdi.sakout.dynamicbox.DynamicBox;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendForm;
import com.github.mikephil.charting.utils.LimitLine;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;
import com.netlynxtech.advancedmonitor.classes.Consts;
import com.netlynxtech.advancedmonitor.classes.Device;
import com.netlynxtech.advancedmonitor.classes.MyMarkerView;
import com.netlynxtech.advancedmonitor.classes.Utils;
import com.netlynxtech.advancedmonitor.classes.WebRequestAPI;

import de.ankri.views.Switch;

public class IndividualDeviceActivity extends ActionBarActivity {
	String deviceId = "", deviceDescription = "";
	RefreshActionItem mRefreshActionItem;
	Device device = new Device();
	AsyncTask<Void, Void, Void> task = new loadData();
	DynamicBox box;
	TextView tvDeviceId, tvDeviceDescription, tvDeviceTemperature, tvDeviceHumidity, tvDeviceVoltage, tvDeviceTimestamp, tvInputOneDescription, tvInputTwoDescription, tvOutputOneDescription,
			tvOutputTwoDescription, tvPastHistoryTime, tvPastHistoryTemperature, tvPastHistoryHumidity;
	ImageView ivInputOne, ivInputTwo, ivTemperature, ivHumidity, ivVoltage;
	Switch sOutputOne, sOutputTwo;
	boolean isProcessing = false, loadedBefore = false;
	deleteDevice mDeleteDevice;
	private LineChart[] mCharts = new LineChart[4];
	SharedPreferences prefs;
	loadGraphData mGraphTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(IndividualDeviceActivity.this);
		Intent i = getIntent();
		deviceId = i.getStringExtra("deviceId");
		Log.e("DeviceId", deviceId);
		deviceDescription = i.getStringExtra("deviceDescription");
		if (deviceId.length() < 1) {
			finish();
		}
		device = (Device) i.getSerializableExtra("device");
		Log.e("Individual", deviceId);
		getSupportActionBar().setTitle(deviceDescription);
		setContentView(R.layout.activity_individual_device);

		RelativeLayout rlIndividualDevice = (RelativeLayout) findViewById(R.id.rlIndividualDevice);
		box = new DynamicBox(IndividualDeviceActivity.this, rlIndividualDevice);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		tvDeviceId = (TextView) findViewById(R.id.tvDeviceId);
		tvDeviceDescription = (TextView) findViewById(R.id.tvDeviceDescription);
		tvDeviceTemperature = (TextView) findViewById(R.id.tvDeviceTemperature);
		tvDeviceHumidity = (TextView) findViewById(R.id.tvDeviceHumidity);
		tvDeviceVoltage = (TextView) findViewById(R.id.tvDeviceVoltage);
		tvInputOneDescription = (TextView) findViewById(R.id.tvInputOneDescription);
		tvInputTwoDescription = (TextView) findViewById(R.id.tvInputTwoDescription);
		tvOutputOneDescription = (TextView) findViewById(R.id.tvOutputOneDescription);
		tvOutputTwoDescription = (TextView) findViewById(R.id.tvOutputTwoDescription);
		tvDeviceTimestamp = (TextView) findViewById(R.id.tvDeviceTimestamp);
		tvPastHistoryTime = (TextView) findViewById(R.id.tvPastHistoryTime);
		tvPastHistoryTemperature = (TextView) findViewById(R.id.tvPastHistoryTemperature);
		tvPastHistoryHumidity = (TextView) findViewById(R.id.tvPastHistoryHumidity);

		ivInputOne = (ImageView) findViewById(R.id.ivInputOne);
		ivInputTwo = (ImageView) findViewById(R.id.ivInputTwo);
		ivTemperature = (ImageView) findViewById(R.id.imageView1);
		ivHumidity = (ImageView) findViewById(R.id.imageView2);
		ivVoltage = (ImageView) findViewById(R.id.imageView3);
		sOutputOne = (Switch) findViewById(R.id.sOutputOne);
		sOutputTwo = (Switch) findViewById(R.id.sOutputTwo);
		setData();
		tvDeviceTemperature.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(IndividualDeviceActivity.this);
				dialog.setCancelable(false);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setContentView(R.layout.dialog_temperature_threshold);
				dialog.setTitle("Set Threshold (Temperature)");
				final EditText etMinTempThreshold = (EditText) dialog.findViewById(R.id.etMinTempThreshold);
				etMinTempThreshold.setText(device.getTemperatureLo());
				final EditText etMaxTempThreshold = (EditText) dialog.findViewById(R.id.etMaxTempThreshold);
				etMaxTempThreshold.setText(device.getTemperatureHi());
				TextView tvIndicatorMin = (TextView) dialog.findViewById(R.id.tvIndicatorMin);
				tvIndicatorMin.setText((char) 0x00B0 + "c");
				TextView tvIndicatorMax = (TextView) dialog.findViewById(R.id.tvIndicatorMax);
				tvIndicatorMax.setText((char) 0x00B0 + "c");
				Button bCancel = (Button) dialog.findViewById(R.id.bCancel);
				bCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				Button bUpdate = (Button) dialog.findViewById(R.id.bUpdate);
				bUpdate.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (etMaxTempThreshold.getText().toString().length() > 0 && etMinTempThreshold.getText().toString().length() > 0) {
							if (Double.valueOf(etMaxTempThreshold.getText().toString()) > Double.valueOf(etMinTempThreshold.getText().toString())) {

								final String maxTemp = etMaxTempThreshold.getText().toString();
								String minTemp = etMinTempThreshold.getText().toString();
								new AsyncTask<String, Void, Void>() {
									String res = "", maxTemp, minTemp;
									ProgressDialog pd;

									@Override
									protected void onPreExecute() {
										super.onPreExecute();
										pd = new ProgressDialog(IndividualDeviceActivity.this);
										pd.setCancelable(false);
										pd.setCanceledOnTouchOutside(false);
										pd.setMessage("Updating threshold..");
										pd.setIndeterminate(true);
										pd.show();
									}

									@Override
									protected Void doInBackground(String... params) {
										maxTemp = params[0];
										minTemp = params[1];
										res = new WebRequestAPI(IndividualDeviceActivity.this).UpdateTemperatureThreshold(deviceId, maxTemp, minTemp);
										return null;
									}

									@Override
									protected void onPostExecute(Void result) {
										super.onPostExecute(result);
										if (pd != null && pd.isShowing()) {
											pd.dismiss();
										}
										IndividualDeviceActivity.this.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												if (res.equals("success")) {
													device.setTemperatureHi(maxTemp);
													device.setTemperatureLo(minTemp);
													Toast.makeText(IndividualDeviceActivity.this, "Successfully updated threshold", Toast.LENGTH_SHORT).show();
													task = null;
													task = new loadData();
													task.execute();
													dialog.dismiss();
												} else {
													Toast.makeText(IndividualDeviceActivity.this, "Failed to update threshold", Toast.LENGTH_SHORT).show();
												}
											}
										});
									}

								}.execute(maxTemp, minTemp);
							} else {
								Toast.makeText(IndividualDeviceActivity.this, "Minimum must be higher than the maximum", Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
				dialog.show();
			}
		});
		ivTemperature.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(IndividualDeviceActivity.this);
				dialog.setCancelable(false);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setContentView(R.layout.dialog_temperature_threshold);
				dialog.setTitle("Set Threshold (Temperature)");
				final EditText etMinTempThreshold = (EditText) dialog.findViewById(R.id.etMinTempThreshold);
				etMinTempThreshold.setText(device.getTemperatureLo());
				final EditText etMaxTempThreshold = (EditText) dialog.findViewById(R.id.etMaxTempThreshold);
				etMaxTempThreshold.setText(device.getTemperatureHi());
				TextView tvIndicatorMin = (TextView) dialog.findViewById(R.id.tvIndicatorMin);
				tvIndicatorMin.setText((char) 0x00B0 + "c");
				TextView tvIndicatorMax = (TextView) dialog.findViewById(R.id.tvIndicatorMax);
				tvIndicatorMax.setText((char) 0x00B0 + "c");
				Button bCancel = (Button) dialog.findViewById(R.id.bCancel);
				bCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				Button bUpdate = (Button) dialog.findViewById(R.id.bUpdate);
				bUpdate.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (etMaxTempThreshold.getText().toString().length() > 0 && etMinTempThreshold.getText().toString().length() > 0) {
							if (Double.valueOf(etMaxTempThreshold.getText().toString()) > Double.valueOf(etMinTempThreshold.getText().toString())) {

								final String maxTemp = etMaxTempThreshold.getText().toString();
								String minTemp = etMinTempThreshold.getText().toString();
								new AsyncTask<String, Void, Void>() {
									String res = "", maxTemp, minTemp;
									ProgressDialog pd;

									@Override
									protected void onPreExecute() {
										super.onPreExecute();
										pd = new ProgressDialog(IndividualDeviceActivity.this);
										pd.setCancelable(false);
										pd.setCanceledOnTouchOutside(false);
										pd.setMessage("Updating threshold..");
										pd.setIndeterminate(true);
										pd.show();
									}

									@Override
									protected Void doInBackground(String... params) {
										maxTemp = params[0];
										minTemp = params[1];
										res = new WebRequestAPI(IndividualDeviceActivity.this).UpdateTemperatureThreshold(deviceId, maxTemp, minTemp);
										return null;
									}

									@Override
									protected void onPostExecute(Void result) {
										super.onPostExecute(result);
										if (pd != null && pd.isShowing()) {
											pd.dismiss();
										}
										IndividualDeviceActivity.this.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												if (res.equals("success")) {
													device.setTemperatureHi(maxTemp);
													device.setTemperatureLo(minTemp);
													Toast.makeText(IndividualDeviceActivity.this, "Successfully updated threshold", Toast.LENGTH_SHORT).show();
													task = null;
													task = new loadData();
													task.execute();
													dialog.dismiss();
												} else {
													Toast.makeText(IndividualDeviceActivity.this, "Failed to update threshold", Toast.LENGTH_SHORT).show();
												}
											}
										});
									}

								}.execute(maxTemp, minTemp);
							} else {
								Toast.makeText(IndividualDeviceActivity.this, "Minimum must be higher than the maximum", Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
				dialog.show();
			}
		});
		tvDeviceHumidity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(IndividualDeviceActivity.this);
				dialog.setCancelable(false);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setContentView(R.layout.dialog_temperature_threshold);
				dialog.setTitle("Set Threshold (Humidity)");
				final EditText etMinTempThreshold = (EditText) dialog.findViewById(R.id.etMinTempThreshold);
				etMinTempThreshold.setText(device.getHumidityLo());
				final EditText etMaxTempThreshold = (EditText) dialog.findViewById(R.id.etMaxTempThreshold);
				etMaxTempThreshold.setText(device.getHumidityHi());
				TextView tvIndicatorMin = (TextView) dialog.findViewById(R.id.tvIndicatorMin);
				tvIndicatorMin.setText("%");
				TextView tvIndicatorMax = (TextView) dialog.findViewById(R.id.tvIndicatorMax);
				tvIndicatorMax.setText("%");

				Button bCancel = (Button) dialog.findViewById(R.id.bCancel);
				bCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				Button bUpdate = (Button) dialog.findViewById(R.id.bUpdate);
				bUpdate.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (etMaxTempThreshold.getText().toString().length() > 0 && etMinTempThreshold.getText().toString().length() > 0) {
							if (Double.valueOf(etMaxTempThreshold.getText().toString()) > Double.valueOf(etMinTempThreshold.getText().toString())) {
								final String maxTemp = etMaxTempThreshold.getText().toString();
								String minTemp = etMinTempThreshold.getText().toString();
								new AsyncTask<String, Void, Void>() {
									String res = "", maxTemp, minTemp;
									ProgressDialog pd;

									@Override
									protected void onPreExecute() {
										super.onPreExecute();
										pd = new ProgressDialog(IndividualDeviceActivity.this);
										pd.setCancelable(false);
										pd.setCanceledOnTouchOutside(false);
										pd.setMessage("Updating threshold..");
										pd.setIndeterminate(true);
										pd.show();
									}

									@Override
									protected Void doInBackground(String... params) {
										maxTemp = params[0];
										minTemp = params[1];
										res = new WebRequestAPI(IndividualDeviceActivity.this).UpdateHumidityThreshold(deviceId, maxTemp, minTemp);
										return null;
									}

									@Override
									protected void onPostExecute(Void result) {
										super.onPostExecute(result);
										if (pd != null && pd.isShowing()) {
											pd.dismiss();
										}
										IndividualDeviceActivity.this.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												if (res.equals("success")) {
													device.setHumidityHi(maxTemp);
													device.setHumidityLo(minTemp);
													Toast.makeText(IndividualDeviceActivity.this, "Successfully updated threshold", Toast.LENGTH_SHORT).show();
													task = null;
													task = new loadData();
													task.execute();
													dialog.dismiss();
												} else {
													Toast.makeText(IndividualDeviceActivity.this, "Failed to update threshold", Toast.LENGTH_SHORT).show();
												}
											}
										});
									}

								}.execute(maxTemp, minTemp);
							} else {
								Toast.makeText(IndividualDeviceActivity.this, "Minimum must be higher than the maximum", Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
				dialog.show();
			}
		});
		ivHumidity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(IndividualDeviceActivity.this);
				dialog.setCancelable(false);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setContentView(R.layout.dialog_temperature_threshold);
				dialog.setTitle("Set Threshold (Humidity)");
				final EditText etMinTempThreshold = (EditText) dialog.findViewById(R.id.etMinTempThreshold);
				etMinTempThreshold.setText(device.getHumidityLo());
				final EditText etMaxTempThreshold = (EditText) dialog.findViewById(R.id.etMaxTempThreshold);
				etMaxTempThreshold.setText(device.getHumidityHi());
				TextView tvIndicatorMin = (TextView) dialog.findViewById(R.id.tvIndicatorMin);
				tvIndicatorMin.setText("%");
				TextView tvIndicatorMax = (TextView) dialog.findViewById(R.id.tvIndicatorMax);
				tvIndicatorMax.setText("%");
				Button bCancel = (Button) dialog.findViewById(R.id.bCancel);
				bCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				Button bUpdate = (Button) dialog.findViewById(R.id.bUpdate);
				bUpdate.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (etMaxTempThreshold.getText().toString().length() > 0 && etMinTempThreshold.getText().toString().length() > 0) {
							if (Double.valueOf(etMaxTempThreshold.getText().toString()) > Double.valueOf(etMinTempThreshold.getText().toString())) {
								final String maxTemp = etMaxTempThreshold.getText().toString();
								String minTemp = etMinTempThreshold.getText().toString();
								new AsyncTask<String, Void, Void>() {
									String res = "", maxTemp, minTemp;
									ProgressDialog pd;

									@Override
									protected void onPreExecute() {
										super.onPreExecute();
										pd = new ProgressDialog(IndividualDeviceActivity.this);
										pd.setCancelable(false);
										pd.setCanceledOnTouchOutside(false);
										pd.setMessage("Updating threshold..");
										pd.setIndeterminate(true);
										pd.show();
									}

									@Override
									protected Void doInBackground(String... params) {
										maxTemp = params[0];
										minTemp = params[1];
										res = new WebRequestAPI(IndividualDeviceActivity.this).UpdateHumidityThreshold(deviceId, maxTemp, minTemp);
										return null;
									}

									@Override
									protected void onPostExecute(Void result) {
										super.onPostExecute(result);
										if (pd != null && pd.isShowing()) {
											pd.dismiss();
										}
										IndividualDeviceActivity.this.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												if (res.equals("success")) {
													device.setHumidityHi(maxTemp);
													device.setHumidityLo(minTemp);
													Toast.makeText(IndividualDeviceActivity.this, "Successfully updated threshold", Toast.LENGTH_SHORT).show();
													task = null;
													task = new loadData();
													task.execute();
													dialog.dismiss();
												} else {
													Toast.makeText(IndividualDeviceActivity.this, "Failed to update threshold", Toast.LENGTH_SHORT).show();
												}
											}
										});
									}

								}.execute(maxTemp, minTemp);
							} else {
								Toast.makeText(IndividualDeviceActivity.this, "Minimum must be higher than the maximum", Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
				dialog.show();
			}
		});
		box.setClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					task = null;
					task = new loadData();
					task.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mGraphTask = null;
		mGraphTask = new loadGraphData();
		mGraphTask.execute();
		processData();
	}

	private void setData() {
		isProcessing = true;
		tvDeviceTimestamp.setText(Html.fromHtml("<b>" + Utils.parseTime(device.getTimestamp()) + "</b>"));
		tvDeviceId.setText(device.getDeviceID());
		tvDeviceDescription.setText(device.getDescription());
		tvInputOneDescription.setText(device.getDescriptionInput1());
		tvInputTwoDescription.setText(device.getDescriptionInput2());
		tvDeviceTemperature.setText(Html.fromHtml("Temperature<br>" + "<b><font color='#4CAF50'>" + device.getTemperature() + " " + (char) 0x00B0 + "C" + "</b></font>"));
		float temperatureCurrent = Float.parseFloat(device.getTemperature());
		float temperatureHi = Float.parseFloat(device.getTemperatureHi());
		float temperatureLo = Float.parseFloat(device.getTemperatureLo());
		if (temperatureCurrent > temperatureHi) {
			tvDeviceTemperature.setText(Html.fromHtml("Temperature<br>" + "<b><font color='#FF1744'>" + device.getTemperature() + " " + (char) 0x00B0 + "C" + "</b></font>"));
		}
		if (temperatureCurrent < temperatureLo) {
			tvDeviceTemperature.setText(Html.fromHtml("Temperature<br>" + "<b><font color='#2196F3'>" + device.getTemperature() + " " + (char) 0x00B0 + "C" + "</b></font>"));
		}

		tvDeviceHumidity.setText(Html.fromHtml("Humidity<br>" + "<b><font color='#4CAF50'>" + device.getHumidity() + " %" + "</b></font>"));
		float humidityCurrent = Float.parseFloat(device.getHumidity());
		float humidityHi = Float.parseFloat(device.getHumidityHi());
		float humidityLo = Float.parseFloat(device.getHumidityLo());
		if (humidityCurrent > humidityHi) {
			tvDeviceHumidity.setText(Html.fromHtml("Humidity<br>" + "<b><font color='#FF1744'>" + device.getHumidity() + " %" + "</b></font>"));
		}
		if (humidityCurrent < humidityLo) {
			tvDeviceHumidity.setText(Html.fromHtml("Humidity<br>" + "<b><font color='#2196F3'>" + device.getHumidity() + " %" + "</b></font>"));
		}

		tvDeviceVoltage.setText(Html.fromHtml("Voltage<br>" + "<b><font color='#4CAF50'>" + device.getVoltage() + " V" + "</b></font>"));

		if (device.getEnableInput1().equals("1")) {
			if (device.getInput1().equals("1")) {
				ivInputOne.setImageDrawable(IndividualDeviceActivity.this.getResources().getDrawable(R.drawable.ic_greendot));
			} else {
				ivInputOne.setImageDrawable(IndividualDeviceActivity.this.getResources().getDrawable(R.drawable.ic_reddot));
			}
		} else {
			tvInputOneDescription.setVisibility(View.INVISIBLE);
			ivInputOne.setVisibility(View.INVISIBLE);
		}

		if (device.getEnableInput2().equals("1")) {
			if (device.getInput2().equals("1")) {
				ivInputTwo.setImageDrawable(IndividualDeviceActivity.this.getResources().getDrawable(R.drawable.ic_greendot));
			} else {
				ivInputTwo.setImageDrawable(IndividualDeviceActivity.this.getResources().getDrawable(R.drawable.ic_reddot));
			}
		} else {
			tvInputTwoDescription.setVisibility(View.INVISIBLE);
			ivInputTwo.setVisibility(View.INVISIBLE);
		}

		if (device.getEnableOutput1().equals("1")) {
			sOutputOne.setEnabled(true);
			tvOutputOneDescription.setText(device.getDescriptionOutput1().trim());
			Log.e("OUTPUT1", "INSIDE 1");
			if (device.getOutput1().equals("1")) {
				sOutputOne.setChecked(true);
			} else {
				sOutputOne.setChecked(false);
			}
		} else {
			sOutputOne.setVisibility(View.INVISIBLE);
			tvOutputOneDescription.setVisibility(View.INVISIBLE);
		}
		sOutputOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncTask<String, Void, Void>() {
					String finalStatus, data;

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						if (device.getOutput1().equals("1")) {
							finalStatus = "0";
						} else {
							finalStatus = "1";
						}
					}

					@Override
					protected Void doInBackground(String... params) {
						data = new WebRequestAPI(IndividualDeviceActivity.this).SetOutput(device.getDeviceID(), "1", finalStatus);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						if (data.startsWith("success|")) {
							if (finalStatus.equals("1")) {
								sOutputOne.setChecked(true);
								device.setOutput1("1");
							} else if (finalStatus.equals("0")) {
								sOutputOne.setChecked(false);
								device.setOutput1("0");
							}
						} else {
							if (finalStatus.equals("1")) {
								sOutputOne.setChecked(false);
							} else if (finalStatus.equals("0")) {
								sOutputOne.setChecked(true);
							}
							Toast.makeText(IndividualDeviceActivity.this, data, Toast.LENGTH_SHORT).show();
						}
					}
				}.execute();
			}
		});

		if (device.getEnableOutput2().equals("1")) {
			sOutputTwo.setEnabled(true);
			tvOutputTwoDescription.setText(device.getDescriptionOutput2().trim());
			Log.e("OUTPUT2", "INSIDE 2");
			if (device.getOutput2().equals("1")) {
				sOutputTwo.setChecked(true);
			} else {
				sOutputTwo.setChecked(false);
			}
		} else {
			sOutputTwo.setVisibility(View.INVISIBLE);
			tvOutputTwoDescription.setVisibility(View.INVISIBLE);
		}

		sOutputTwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncTask<String, Void, Void>() {
					String finalStatus, data;

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						if (device.getOutput2().equals("1")) {
							finalStatus = "0";
						} else {
							finalStatus = "1";
						}
					}

					@Override
					protected Void doInBackground(String... params) {
						data = new WebRequestAPI(IndividualDeviceActivity.this).SetOutput(device.getDeviceID(), "2", finalStatus);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						if (data.startsWith("success|")) {
							if (finalStatus.equals("1")) {
								sOutputTwo.setChecked(true);
								device.setOutput2("1");
							} else if (finalStatus.equals("0")) {
								sOutputTwo.setChecked(false);
								device.setOutput2("0");
							}
						} else {
							if (finalStatus.equals("1")) {
								sOutputTwo.setChecked(false);
							} else if (finalStatus.equals("0")) {
								sOutputTwo.setChecked(true);
							}
							Toast.makeText(IndividualDeviceActivity.this, data, Toast.LENGTH_SHORT).show();
						}
					}
				}.execute();
			}
		});
		Log.e("ROLE", device.getRole());
		if (!device.getRole().equals("9") && !device.getRole().equals("2")) {
			sOutputOne.setEnabled(false);
			sOutputTwo.setEnabled(false);
		}
		if (!device.getRole().equals("9")) {
			tvDeviceTemperature.setEnabled(false);
			tvDeviceHumidity.setEnabled(false);
			ivTemperature.setEnabled(false);
			ivHumidity.setEnabled(false);
			tvDeviceVoltage.setEnabled(false);
			ivVoltage.setEnabled(false);
		}
		if (!device.getEnableOutput1().equals("1") && !device.getEnableInput1().equals("1")) {
			tvInputOneDescription.setVisibility(View.GONE);
			ivInputOne.setVisibility(View.GONE);
			tvOutputOneDescription.setVisibility(View.GONE);
			tvOutputOneDescription.setVisibility(View.GONE);
		}
		if (!device.getEnableOutput2().equals("1") && !device.getEnableInput2().equals("1")) {
			tvInputTwoDescription.setVisibility(View.GONE);
			ivInputTwo.setVisibility(View.GONE);
			tvOutputTwoDescription.setVisibility(View.GONE);
			tvOutputTwoDescription.setVisibility(View.GONE);
		}
		isProcessing = false;
		loadedBefore = true;
		mGraphTask = null;
		mGraphTask = new loadGraphData();
		mGraphTask.execute();
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
										task = new loadData();
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

	private LineData getData(ArrayList<Double> xValues, ArrayList<String> yValues, String type, String maxThreshold, String minThreshold) {
		ArrayList<String> xVals = yValues;

		ArrayList<Entry> yVals = new ArrayList<Entry>();

		for (int i = 0; i < xVals.size(); i++) {
			double val = xValues.get(i);
			yVals.add(new Entry((float) val, i));
		}

		// create a dataset and give it a type
		LineDataSet set1 = new LineDataSet(yVals, type);
		// set1.setFillAlpha(110);

		set1.setLineWidth(1.75f);
		set1.setCircleSize(3f);
		int gridColor = Color.parseColor(new Utils(IndividualDeviceActivity.this).getGraphLineColor());

		set1.setColor(gridColor);
		set1.setCircleColor(gridColor);
		set1.setHighLightColor(gridColor);
		set1.setFillColor(gridColor);

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set1); // add the datasets

		// create a data object with the datasets

		LineData data = new LineData(xVals, dataSets);
		LimitLine ll = new LimitLine(Float.parseFloat(maxThreshold));
		ll.setLineColor(Color.parseColor(new Utils(IndividualDeviceActivity.this).getGraphMaximumThreshold()));
		ll.setLineWidth(1f);
		data.addLimitLine(ll);
		LimitLine ll2 = new LimitLine(Float.parseFloat(minThreshold));
		ll2.setLineColor(Color.parseColor(new Utils(IndividualDeviceActivity.this).getGraphMinimumThreshold()));
		ll2.setLineWidth(1f);
		data.addLimitLine(ll2);
		return data;
	}

	private void setupChart(LineChart chart, LineData data, int color) {
		chart.setHighlightEnabled(true);
		MyMarkerView mv = new MyMarkerView(IndividualDeviceActivity.this, R.layout.custom_marker_view);
		mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());
		chart.setMarkerView(mv);
		chart.setHighlightIndicatorEnabled(false);

		// if enabled, the chart will always start at zero on the y-axis
		chart.setStartAtZero(false);

		// enable the drawing of values into the chart
		chart.setDrawYValues(true);

		chart.setDrawBorder(false);

		// no description text
		chart.setDescription("");
		chart.setNoDataTextDescription("You need to provide data for the chart.");

		// enable / disable grid lines
		chart.setDrawVerticalGrid(false);
		// mChart.setDrawHorizontalGrid(false);
		//
		// enable / disable grid background
		chart.setDrawGridBackground(false);
		int gridColor = Color.parseColor(new Utils(IndividualDeviceActivity.this).getGraphLineColor());
		chart.setGridColor(gridColor & 0x70FFFFFF);
		chart.setGridWidth(1.25f);
		chart.setValueTextColor(Color.parseColor(new Utils(IndividualDeviceActivity.this).getGraphValuesTextColor()));
		chart.setValueTextSize(12f);
		// enable touch gestures
		chart.setTouchEnabled(true);

		// enable scaling and dragging
		chart.setDragEnabled(true);
		chart.setScaleEnabled(true);

		// if disabled, scaling can be done on x- and y-axis separately
		chart.setPinchZoom(true);

		chart.setBackgroundColor(color);

		// chart.setValueTypeface(mTf);

		// add data
		chart.setData(data);

		// get the legend (only possible after setting data)
		Legend l = chart.getLegend();
		// modify the legend ...
		// l.setPosition(LegendPosition.LEFT_OF_CHART);
		l.setForm(LegendForm.CIRCLE);
		l.setFormSize(6f);
		String cpTextColor = new Utils(IndividualDeviceActivity.this).getGraphTextColor();
		l.setTextColor(Color.parseColor(cpTextColor));

		YLabels y = chart.getYLabels();
		y.setTextColor(Color.parseColor(cpTextColor));
		y.setLabelCount(4);

		XLabels x = chart.getXLabels();
		x.setTextColor(Color.parseColor(cpTextColor));

		if (new Utils(IndividualDeviceActivity.this).getGraphAnimate()) {
			chart.animateX(2500);
		}
		chart.invalidate();
	}

	private class loadGraphData extends AsyncTask<Void, Void, Void> {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		ArrayList<Double> temperature = new ArrayList<Double>();
		ArrayList<Double> humidity = new ArrayList<Double>();
		ArrayList<String> timing = new ArrayList<String>();

		LineData data1, data2;

		@Override
		protected Void doInBackground(Void... params) {

			data = new WebRequestAPI(IndividualDeviceActivity.this).GetChartData(deviceId, Utils.getCurrentDateTime(), Utils.getCustomDateTime(), 12);
			if (data.size() > 0) {
				for (HashMap<String, String> d : data) {
					temperature.add(Double.parseDouble(d.get(Consts.GETDEVICES_TEMPERATURE)));
					humidity.add(Double.parseDouble(d.get(Consts.GETDEVICES_HUMIDITY)));
					timing.add(d.get(Consts.GETDEVICES_DATATIMESTAMP));
				}
			}
			mCharts[0] = (LineChart) findViewById(R.id.chart1);
			mCharts[1] = (LineChart) findViewById(R.id.chart2);

			data1 = getData(temperature, timing, "Temperature", device.getTemperatureHi(), device.getTemperatureLo());
			data2 = getData(humidity, timing, "Humidity", device.getHumidityHi(), device.getHumidityLo());

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			IndividualDeviceActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (!isCancelled()) {
						if (data.size() > 0) {
							setupChart(mCharts[0], data1, Color.parseColor(new Utils(IndividualDeviceActivity.this).getGraphTemperatureColor()));
							setupChart(mCharts[1], data2, Color.parseColor(new Utils(IndividualDeviceActivity.this).getGraphHumidityColor()));
							String pastTime = "<b><u>Time</u></b><br>", pastTemp = "<b><u>Temperature</u></b><br>", pastHumidity = "<b><u>Humidity</u></b><br>";
							int pastHistoryAmount = Integer.parseInt(new Utils(IndividualDeviceActivity.this).getHousekeep());
							for (int i = pastHistoryAmount; --i >= 0;) {
								HashMap<String, String> d = data.get(i);
								pastTime += d.get(Consts.GETDEVICES_DATATIMESTAMP) + "<br>";
								pastTemp += d.get(Consts.GETDEVICES_TEMPERATURE) + (char) 0x00B0 + "c<br>";
								pastHumidity += d.get(Consts.GETDEVICES_HUMIDITY) + "%<br>";
							}
							tvPastHistoryTime.setText(Html.fromHtml(pastTime));
							tvPastHistoryTemperature.setText(Html.fromHtml(pastTemp));
							tvPastHistoryHumidity.setText(Html.fromHtml(pastHumidity));
							Log.e("Humidity", pastHumidity);
						} else {
							Toast.makeText(IndividualDeviceActivity.this, "Unable to load graph", Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
		}

	}

	private class loadData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isProcessing = true;
			mRefreshActionItem.showProgress(true);
			if (!loadedBefore) {
				box.showLoadingLayout();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			device = new WebRequestAPI(IndividualDeviceActivity.this).GetDevice(deviceId);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			IndividualDeviceActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (!loadedBefore) {
						box.hideAll();
						loadedBefore = true;
					}
					getSupportActionBar().setTitle(device.getDescription());
					mRefreshActionItem.showProgress(false);
					isProcessing = false;
					if (device != null && device.getDescription() != null && device.getDescription().length() > 0) {
						setData();
					}
					processData();
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_individual_device, menu);
		MenuItem item = menu.findItem(R.id.menu_individual_refresh);
		mRefreshActionItem = (RefreshActionItem) MenuItemCompat.getActionView(item);
		mRefreshActionItem.setMenuItem(item);
		mRefreshActionItem.setProgressIndicatorType(ProgressIndicatorType.INDETERMINATE);
		mRefreshActionItem.setRefreshActionListener(new RefreshActionListener() {

			@Override
			public void onRefreshButtonClick(RefreshActionItem sender) {
				if (task != null) {
					task = null;
					task = new loadData();
					task.execute();
				}
			}
		});
		task = null;
		task = new loadData();
		task.execute();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_individual_map:
			if (device.getRole().equals("9")) {
				startActivity(new Intent(IndividualDeviceActivity.this, MapsActivity.class).putExtra("device", device));
			} else {
				Toast.makeText(IndividualDeviceActivity.this, "You are not an Admin", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.menu_individual_users:
			if (device.getRole().equals("9")) {
				startActivity(new Intent(IndividualDeviceActivity.this, UsersActivity.class).putExtra("deviceId", device.getDeviceID()));
			} else {
				Toast.makeText(IndividualDeviceActivity.this, "You are not an Admin", Toast.LENGTH_SHORT).show();
			}
			break;
		case android.R.id.home:
			finish();
			break;
		case R.id.menu_edit_device:
			if (device.getRole().equals("9")) {

				Intent i = new Intent(IndividualDeviceActivity.this, IndividualDeviceEditDetailsActivity.class);
				i.putExtra("deviceId", deviceId);
				i.putExtra("deviceDescription", device.getDescription());
				if (device.getEnableInput1().equals("1")) {
					i.putExtra("input1", device.getDescriptionInput1());
				}
				if (device.getEnableInput2().equals("1")) {
					i.putExtra("input2", device.getDescriptionInput2());
				}
				if (device.getEnableOutput1().equals("1")) {
					i.putExtra("output1", device.getDescriptionOutput1());
				}
				if (device.getEnableOutput2().equals("1")) {
					i.putExtra("output2", device.getDescriptionOutput2());
				}
				startActivity(i);
			} else {
				Toast.makeText(IndividualDeviceActivity.this, "You are not an Admin", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.menu_delete_device:
			if (device.getRole().equals("9")) {
				showDeleteDialog();
			}
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	private void showDeleteDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(IndividualDeviceActivity.this);
		builder.setMessage("Delete device?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mDeleteDevice = null;
				mDeleteDevice = new deleteDevice();
				mDeleteDevice.execute();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		}).show();
	}

	private class deleteDevice extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;
		String res = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(IndividualDeviceActivity.this);
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.setMessage("Deleting device..");
			pd.show();
			mRefreshActionItem.showProgress(true);

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			IndividualDeviceActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mRefreshActionItem.showProgress(false);
					if (pd != null && pd.isShowing()) {
						pd.cancel();
					}
					if (res.equals("success")) {
						finish();
					} else {
						Toast.makeText(IndividualDeviceActivity.this, res, Toast.LENGTH_LONG).show();
					}
				}
			});
		}

		@Override
		protected Void doInBackground(Void... params) {
			res = new WebRequestAPI(IndividualDeviceActivity.this).DeleteDevice(deviceId);
			return null;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e("RESUME", "RESUME");
		try {
			if (task != null) {
				task.execute();
			} else {
				task = new loadData();
				task.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
			task = null;
		}

		if (mGraphTask != null && mGraphTask.getStatus() == AsyncTask.Status.RUNNING) {
			mGraphTask.cancel(true);
			mGraphTask = null;
		}
	}

}
