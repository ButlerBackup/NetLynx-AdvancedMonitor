package com.netlynxtech.advancedmonitor.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.netlynxtech.advancedmonitor.R;
import com.netlynxtech.advancedmonitor.classes.DeviceRequest;
import com.netlynxtech.advancedmonitor.classes.Utils;
import com.netlynxtech.advancedmonitor.classes.WebRequestAPI;

public class ListRequestAdapter extends BaseAdapter {
	Context context;
	ArrayList<DeviceRequest> data;
	private static LayoutInflater inflater = null;
	HashMap<String, String> rolesValueArray;

	public ListRequestAdapter(Context context, ArrayList<DeviceRequest> data, HashMap<String, String> rolesValueArray) {
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.rolesValueArray = rolesValueArray;
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
		TextView tvDeviceName;
		TextView tvRequestorTimestamp;
		TextView tvRole;
		TextView tvRequestorName;
		Button bRequestAccept;
		Button bRequestDecline;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_received_member_permission_item, parent, false);
			holder = new ViewHolder();
			holder.tvDeviceId = (TextView) convertView.findViewById(R.id.tvDeviceId);
			holder.tvDeviceName = (TextView) convertView.findViewById(R.id.tvDeviceName);
			holder.tvRequestorTimestamp = (TextView) convertView.findViewById(R.id.tvRequestorTimestamp);
			holder.tvRole = (TextView) convertView.findViewById(R.id.tvRole);
			holder.tvRequestorName = (TextView) convertView.findViewById(R.id.tvRequestorName);
			holder.bRequestAccept = (Button) convertView.findViewById(R.id.bRequestAccept);
			holder.bRequestDecline = (Button) convertView.findViewById(R.id.bRequestDecline);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final DeviceRequest d = data.get(position);
		holder.tvDeviceId.setText(d.getDeviceId());
		holder.tvDeviceName.setText(d.getDeviceName());
		holder.tvRequestorName.setText(d.getRequestorName());
		holder.tvRequestorTimestamp.setText(Html.fromHtml("<b>" + Utils.parseTime(d.getTimestamp()) + "</b>"));
		holder.tvRole.setText(rolesValueArray.get(d.getRole()));
		if (d.getRole().equals("9")) {
			holder.tvRole.setTextColor(Color.RED);
		} else if (d.getRole().equals("2")) {
			holder.tvRole.setTextColor(Color.YELLOW);
		} else if (d.getRole().equals("1")) {
			holder.tvRole.setTextColor(Color.GREEN);
		}
		holder.bRequestAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Void>() {
					String res;

					@Override
					protected Void doInBackground(Void... params) {
						res = new WebRequestAPI(context).RespondToMemberRequest(d.getDeviceId(), "1", "");
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						if (res.equals("success")) {
							data.remove(position);
							notifyDataSetChanged();
						}
					}

				}.execute();
			}
		});
		holder.bRequestDecline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Void>() {
					String res;

					@Override
					protected Void doInBackground(Void... params) {
						res = new WebRequestAPI(context).RespondToMemberRequest(d.getDeviceId(), "0", "");
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						if (res.equals("success")) {
							data.remove(position);
							notifyDataSetChanged();
						}
					}

				}.execute();
			}
		});
		return convertView;
	}

}
