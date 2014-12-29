package com.netlynxtech.advancedmonitor.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netlynxtech.advancedmonitor.IndividualDeviceActivity;
import com.netlynxtech.advancedmonitor.R;
import com.netlynxtech.advancedmonitor.classes.Device;
import com.netlynxtech.advancedmonitor.classes.Utils;
import com.netlynxtech.advancedmonitor.classes.WebRequestAPI;

import de.ankri.views.Switch;

public class DevicesAdapter extends BaseAdapter {
	Context context;
	ArrayList<Device> data;
	private static LayoutInflater inflater = null;

	public DevicesAdapter(Context context, ArrayList<Device> data) {
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView tvDeviceId;
		TextView tvDeviceDescription;
		TextView tvDeviceTemperature;
		TextView tvDeviceHumidity;
		TextView tvDeviceVoltage;
		TextView tvDeviceTimestamp;
		TextView tvInputOneDescription;
		TextView tvInputTwoDescription;
		TextView tvOutputOneDescription;
		TextView tvOutputTwoDescription;
		ImageView ivInputOne;
		ImageView ivInputTwo;
		ImageView ivInputThree;
		ImageView ivInputFour;
		ImageView ivInputFive;
		ImageView ivInputSix;
		ImageView ivInputSeven;
		ImageView ivInputEight;
		Switch sOutputOne;
		Switch sOutputTwo;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.device_list_item, parent, false);
			holder = new ViewHolder();
			holder.tvDeviceId = (TextView) convertView.findViewById(R.id.tvDeviceId);
			holder.tvDeviceDescription = (TextView) convertView.findViewById(R.id.tvDeviceDescription);
			holder.tvDeviceTemperature = (TextView) convertView.findViewById(R.id.tvDeviceTemperature);
			holder.tvDeviceHumidity = (TextView) convertView.findViewById(R.id.tvDeviceHumidity);
			holder.tvDeviceVoltage = (TextView) convertView.findViewById(R.id.tvDeviceVoltage);
			holder.tvInputOneDescription = (TextView) convertView.findViewById(R.id.tvInputOneDescription);
			holder.tvInputTwoDescription = (TextView) convertView.findViewById(R.id.tvInputTwoDescription);
			holder.tvOutputOneDescription = (TextView) convertView.findViewById(R.id.tvOutputOneDescription);
			holder.tvOutputTwoDescription = (TextView) convertView.findViewById(R.id.tvOutputTwoDescription);
			holder.tvDeviceTimestamp = (TextView) convertView.findViewById(R.id.tvDeviceTimestamp);

			holder.ivInputOne = (ImageView) convertView.findViewById(R.id.ivInputOne);
			holder.ivInputTwo = (ImageView) convertView.findViewById(R.id.ivInputTwo);
			holder.ivInputThree = (ImageView) convertView.findViewById(R.id.ivInputThree);
			holder.ivInputFour = (ImageView) convertView.findViewById(R.id.ivInputFour);
			holder.ivInputFive = (ImageView) convertView.findViewById(R.id.ivInputFive);
			holder.ivInputSix = (ImageView) convertView.findViewById(R.id.ivInputSix);
			holder.ivInputSeven = (ImageView) convertView.findViewById(R.id.ivInputSeven);
			holder.ivInputEight = (ImageView) convertView.findViewById(R.id.ivInputEight);
			holder.sOutputOne = (Switch) convertView.findViewById(R.id.sOutputOne);
			holder.sOutputTwo = (Switch) convertView.findViewById(R.id.sOutputTwo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Device item = data.get(position);
		holder.tvDeviceTimestamp.setText(Html.fromHtml("<b>" + Utils.parseTime(item.getTimestamp()) + "</b>"));
		holder.tvDeviceId.setText(item.getDeviceID());
		holder.tvDeviceDescription.setText(item.getDescription());
		holder.tvInputOneDescription.setText(item.getDescriptionInput1());
		holder.tvInputTwoDescription.setText(item.getDescriptionInput2());

		holder.tvDeviceTemperature.setText(Html.fromHtml("Temperature<br>" + "<b><font color='#4CAF50'>" + item.getTemperature() + " " + (char) 0x00B0 + "C" + "</b></font>"));
		float temperatureCurrent = Float.parseFloat(item.getTemperature());
		float temperatureHi = Float.parseFloat(item.getTemperatureHi());
		float temperatureLo = Float.parseFloat(item.getTemperatureLo());
		if (temperatureCurrent > temperatureHi) {
			holder.tvDeviceTemperature.setText(Html.fromHtml("Temperature<br>" + "<b><font color='#FF1744'>" + item.getTemperature() + " " + (char) 0x00B0 + "C" + "</b></font>"));
		}
		if (temperatureCurrent < temperatureLo) {
			holder.tvDeviceTemperature.setText(Html.fromHtml("Temperature<br>" + "<b><font color='#2196F3'>" + item.getTemperature() + " " + (char) 0x00B0 + "C" + "</b></font>"));
		}
		holder.tvDeviceHumidity.setText(Html.fromHtml("Humidity<br>" + "<b><font color='#4CAF50'>" + item.getHumidity() + " %" + "</b></font>"));
		float humidityCurrent = Float.parseFloat(item.getHumidity());
		float humidityHi = Float.parseFloat(item.getHumidityHi());
		float humidityLo = Float.parseFloat(item.getHumidityLo());
		if (humidityCurrent > humidityHi) {
			holder.tvDeviceHumidity.setText(Html.fromHtml("Humidity<br>" + "<b><font color='#FF1744'>" + item.getHumidity() + " %" + "</b></font>"));
		}
		if (humidityCurrent < humidityLo) {
			holder.tvDeviceHumidity.setText(Html.fromHtml("Humidity<br>" + "<b><font color='#2196F3'>" + item.getHumidity() + " %" + "</b></font>"));
		}
		holder.tvDeviceVoltage.setText(Html.fromHtml("Voltage<br>" + "<b><font color='#4CAF50'>" + item.getVoltage() + " V" + "</b></font>"));
		if (item.getEnableInput1().equals("1")) {
			holder.ivInputOne.setVisibility(View.VISIBLE);
			if (item.getInput1().equals("1")) {
				holder.ivInputOne.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_greendot));
			} else {
				holder.ivInputOne.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_reddot));
			}
		} else {
			holder.tvInputOneDescription.setVisibility(View.INVISIBLE);
			holder.ivInputOne.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nodot));
		}

		if (item.getEnableInput2().equals("1")) {
			holder.ivInputTwo.setVisibility(View.VISIBLE);
			if (item.getInput2().equals("1")) {
				holder.ivInputTwo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_greendot));
			} else {
				holder.ivInputTwo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_reddot));
			}
		} else {
			holder.tvInputTwoDescription.setVisibility(View.INVISIBLE);
			holder.ivInputTwo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nodot));
		}
		if (item.getEnableInput3().equals("1")) {
			holder.ivInputThree.setVisibility(View.VISIBLE);
			if (item.getInput3().equals("1")) {
				holder.ivInputThree.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_greendot));
			} else {
				holder.ivInputThree.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_reddot));
			}
		} else {
			holder.ivInputThree.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nodot));
		}
		if (item.getEnableInput4().equals("1")) {
			holder.ivInputFour.setVisibility(View.VISIBLE);
			if (item.getInput4().equals("1")) {
				holder.ivInputFour.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_greendot));
			} else {
				holder.ivInputFour.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_reddot));
			}
		} else {
			holder.ivInputFour.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nodot));
		}
		if (item.getEnableInput5().equals("1")) {
			holder.ivInputFive.setVisibility(View.VISIBLE);
			if (item.getInput5().equals("1")) {
				holder.ivInputFive.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_greendot));
			} else {
				holder.ivInputFive.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_reddot));
			}
		} else {
			holder.ivInputFive.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nodot));
		}
		if (item.getEnableInput6().equals("1")) {
			holder.ivInputSix.setVisibility(View.VISIBLE);
			if (item.getInput6().equals("1")) {
				holder.ivInputSix.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_greendot));
			} else {
				holder.ivInputSix.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_reddot));
			}
		} else {
			holder.ivInputSix.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nodot));
		}
		if (item.getEnableInput7().equals("1")) {
			holder.ivInputSeven.setVisibility(View.VISIBLE);
			if (item.getInput7().equals("1")) {
				holder.ivInputSeven.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_greendot));
			} else {
				holder.ivInputSeven.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_reddot));
			}
		} else {
			holder.ivInputSeven.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nodot));
		}
		if (item.getEnableInput8().equals("1")) {
			holder.ivInputEight.setVisibility(View.VISIBLE);
			if (item.getInput8().equals("1")) {
				holder.ivInputEight.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_greendot));
			} else {
				holder.ivInputEight.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_reddot));
			}
		} else {
			holder.ivInputEight.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nodot));
		}

		if (item.getEnableOutput1().equals("1")) {
			holder.sOutputOne.setVisibility(View.VISIBLE);
			holder.tvOutputOneDescription.setVisibility(View.VISIBLE);
			holder.sOutputOne.setEnabled(true);
			holder.tvOutputOneDescription.setText(item.getDescriptionOutput1().trim());
			Log.e("OUTPUT1", "INSIDE 1");
			if (item.getOutput1().equals("1")) {
				holder.sOutputOne.setChecked(true);
			} else {
				holder.sOutputOne.setChecked(false);
			}
		} else {
			holder.sOutputOne.setVisibility(View.INVISIBLE);
			holder.tvOutputOneDescription.setVisibility(View.INVISIBLE);
		}

		holder.sOutputOne.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				holder.sOutputOne.setEnabled(false);
				new AsyncTask<String, Void, Void>() {
					String finalStatus, data;

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						if (item.getOutput1().equals("1")) {
							finalStatus = "0";
						} else {
							finalStatus = "1";
						}
					}

					@Override
					protected Void doInBackground(String... params) {
						data = new WebRequestAPI(context).SetOutput(item.getDeviceID(), "1", finalStatus);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						holder.sOutputOne.setEnabled(true);
						if (data.startsWith("success|")) {
							if (finalStatus.equals("1")) {
								holder.sOutputOne.setChecked(true);
								item.setOutput1("1");
							} else if (finalStatus.equals("0")) {
								holder.sOutputOne.setChecked(false);
								item.setOutput1("0");
							}
						} else {
							if (finalStatus.equals("1")) {
								holder.sOutputOne.setChecked(false);
							} else if (finalStatus.equals("0")) {
								holder.sOutputOne.setChecked(true);
							}
							Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
						}
					}
				}.execute();
			}
		});

		if (item.getEnableOutput2().equals("1")) {
			holder.sOutputTwo.setVisibility(View.VISIBLE);
			holder.tvOutputTwoDescription.setVisibility(View.VISIBLE);
			holder.sOutputTwo.setEnabled(true);
			holder.tvOutputTwoDescription.setText(item.getDescriptionOutput2().trim());
			Log.e("OUTPUT2", "INSIDE 2");
			if (item.getOutput2().equals("1")) {
				holder.sOutputTwo.setChecked(true);
			} else {
				holder.sOutputTwo.setChecked(false);
			}
		} else {
			holder.sOutputTwo.setVisibility(View.INVISIBLE);
			holder.tvOutputTwoDescription.setVisibility(View.INVISIBLE);
		}
		holder.sOutputTwo.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				holder.sOutputTwo.setEnabled(false);
				new AsyncTask<String, Void, Void>() {
					String finalStatus, data;

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						if (item.getOutput2().equals("1")) {
							finalStatus = "0";
						} else {
							finalStatus = "1";
						}
					}

					@Override
					protected Void doInBackground(String... params) {
						data = new WebRequestAPI(context).SetOutput(item.getDeviceID(), "2", finalStatus);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						holder.sOutputTwo.setEnabled(false);
						if (data.startsWith("success|")) {
							if (finalStatus.equals("1")) {
								holder.sOutputTwo.setChecked(true);
								item.setOutput2("1");
							} else if (finalStatus.equals("0")) {
								holder.sOutputTwo.setChecked(false);
								item.setOutput2("0");
							}
						} else {
							if (finalStatus.equals("1")) {
								holder.sOutputTwo.setChecked(false);
							} else if (finalStatus.equals("0")) {
								holder.sOutputTwo.setChecked(true);
							}
							Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
						}
					}
				}.execute();
			}
		});
		if (!item.getRole().equals("9") && !item.getRole().equals("2")) {
			holder.sOutputOne.setEnabled(false);
			holder.sOutputTwo.setEnabled(false);
		}
		if (!item.getEnableOutput1().equals("1") && !item.getEnableInput1().equals("1") && !item.getEnableInput2().equals("1") && !item.getEnableInput3().equals("1")
				&& !item.getEnableInput4().equals("1")) {
			holder.tvInputOneDescription.setVisibility(View.GONE);
			holder.ivInputOne.setVisibility(View.GONE);
			holder.ivInputTwo.setVisibility(View.GONE);
			holder.ivInputThree.setVisibility(View.GONE);
			holder.ivInputFour.setVisibility(View.GONE);
			holder.tvOutputOneDescription.setVisibility(View.GONE);
			holder.sOutputOne.setVisibility(View.GONE);
		}
		if (!item.getEnableOutput2().equals("1") && !item.getEnableInput5().equals("1") && !item.getEnableInput6().equals("1") && !item.getEnableInput7().equals("1")
				&& !item.getEnableInput8().equals("1")) {
			holder.tvInputTwoDescription.setVisibility(View.GONE);
			holder.ivInputFive.setVisibility(View.GONE);
			holder.ivInputSix.setVisibility(View.GONE);
			holder.ivInputSeven.setVisibility(View.GONE);
			holder.ivInputEight.setVisibility(View.GONE);
			holder.tvOutputTwoDescription.setVisibility(View.GONE);
			holder.sOutputTwo.setVisibility(View.GONE);
		}
		holder.sOutputOne.setEnabled(false);
		holder.sOutputTwo.setEnabled(false);
		if (!item.getEnableOutput2().equals("1")) {
			holder.tvInputTwoDescription.setVisibility(View.GONE);
			holder.tvOutputTwoDescription.setVisibility(View.GONE);
			holder.sOutputTwo.setVisibility(View.GONE);
		}
		return convertView;
	}
}
