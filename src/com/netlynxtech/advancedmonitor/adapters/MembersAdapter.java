package com.netlynxtech.advancedmonitor.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netlynxtech.advancedmonitor.R;
import com.netlynxtech.advancedmonitor.classes.DeviceMembers;
import com.netlynxtech.advancedmonitor.classes.Utils;
import com.netlynxtech.advancedmonitor.classes.WebRequestAPI;

public class MembersAdapter extends BaseAdapter {
	Context context;
	ArrayList<DeviceMembers> data;
	private static LayoutInflater inflater = null;
	String deviceId;
	ProgressDialog pd;

	public MembersAdapter(Context context, ArrayList<DeviceMembers> data, String deviceId) {
		this.context = context;
		this.data = data;
		this.deviceId = deviceId;
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
		TextView tvName;
		TextView tvTimestamp;
		TextView tvRequestStatus;
		TextView tvRole;
		ImageView ivDeleteUser;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_users_item, parent, false);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
			holder.tvRequestStatus = (TextView) convertView.findViewById(R.id.tvRequestStatus);
			holder.tvRole = (TextView) convertView.findViewById(R.id.tvRole);
			holder.ivDeleteUser = (ImageView) convertView.findViewById(R.id.ivDeleteUser);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		DeviceMembers d = data.get(position);
		// String udid = d.getUdid();
		final String memberUdid = d.getUdid();
		holder.tvName.setText(d.getName());
		if (d.getRequestStatus().equals("0")) {
			holder.tvRequestStatus.setText("Pending");
			holder.tvRequestStatus.setTextColor(Color.YELLOW);
		} else if (d.getRequestStatus().equals("1")) {
			holder.tvRequestStatus.setText("Accepted");
			holder.tvRequestStatus.setTextColor(Color.GREEN);
		} else {
			holder.tvRequestStatus.setText("Unknown");
			holder.tvRequestStatus.setTextColor(Color.RED);
		}

		holder.tvTimestamp.setText(Html.fromHtml("<b><i>" + Utils.parseTime(d.getUpdateTimestamp()) + "</b></i>"));
		holder.tvTimestamp.setTextColor(Color.parseColor("#A4A4A4"));
		if (d.getRole().equals("9")) {
			holder.tvRole.setText("Administrator");
			holder.tvRole.setTextColor(Color.RED);
		} else if (d.getRole().equals("2")) {
			holder.tvRole.setText("Controller");
			holder.tvRole.setTextColor(Color.YELLOW);
		} else if (d.getRole().equals("1")) {
			holder.tvRole.setText("Viewer");
			holder.tvRole.setTextColor(Color.GREEN);
		} else {
			holder.tvRole.setText("No Access");
		}
		holder.ivDeleteUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							new removeMember().execute(memberUdid, String.valueOf(position));
							break;
						case DialogInterface.BUTTON_NEGATIVE:
							break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Are you sure you want to remove this person from the device?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
			}
		});
		return convertView;
	}

	private class removeMember extends AsyncTask<String, Void, Void> {
		String res = "";
		int position;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = null;
			pd = new ProgressDialog(context);
			pd.setMessage("Removing member..");
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			Log.e("RemoveMember", deviceId + "|" + params[0]);
			position = Integer.valueOf(params[1]);
			res = new WebRequestAPI(context).RemoveMemberFromDevice(deviceId, params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			super.onPostExecute(result);
			((Activity) context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (!res.equals("success")) {
						Toast.makeText(context, res, Toast.LENGTH_LONG).show();
					} else {
						data.remove(position);
						notifyDataSetChanged();
					}
				}
			});

		}

	}

}
