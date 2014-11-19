package com.netlynxtech.advancedmonitor.adapters;

import info.hoang8f.widget.FButton;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netlynxtech.advancedmonitor.R;
import com.netlynxtech.advancedmonitor.classes.Message;
import com.netlynxtech.advancedmonitor.classes.SQLFunctions;
import com.netlynxtech.advancedmonitor.classes.Utils;
import com.netlynxtech.advancedmonitor.classes.WebRequestAPI;

public class MessageAdapter extends BaseAdapter {
	Context context;
	ArrayList<Message> data;
	private static LayoutInflater inflater = null;

	public MessageAdapter(Context context, ArrayList<Message> data) {
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
		TextView tvTitle;
		TextView tvTimestamp;
		TextView tvMessage;
		FButton bAck;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_message_item, parent, false);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
			holder.tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);
			holder.bAck = (FButton) convertView.findViewById(R.id.bAck);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Message d = data.get(position);
		final String messageId = d.getMessageId();
		final String eventId = d.getEventId();
		// String udid = d.getUdid();
		holder.tvTitle.setText(d.getTitle());
		holder.tvTimestamp.setText(Html.fromHtml("<b><i>" + Utils.parseTime(d.getTimestamp()) + "</b></i>"));
		holder.tvTimestamp.setTextColor(Color.parseColor("#A4A4A4"));
		holder.tvMessage.setText(d.getMessage());
		if (d.getAckRequired().equals("1") && d.getAckDone().equals("0")) {
			holder.bAck.setVisibility(View.VISIBLE);
			holder.bAck.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder alert = new AlertDialog.Builder(context);

					alert.setTitle("Acknowledgement");
					alert.setMessage("Please acknowledge this alert with your message");

					final EditText input = new EditText(context);
					alert.setView(input);

					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							final String value = input.getText().toString();
							new AsyncTask<Void, Void, Void>() {
								String res = "";

								@Override
								protected Void doInBackground(Void... params) {
									res = new WebRequestAPI(context).AckMessage(eventId, value);
									return null;
								}

								@Override
								protected void onPostExecute(Void result) {
									super.onPostExecute(result);
									((Activity) context).runOnUiThread(new Runnable() {
										@Override
										public void run() {
											if (!res.equals("success")) {
												SQLFunctions sql = new SQLFunctions(context);
												sql.open();
												sql.setMessageAckDone(messageId);
												sql.close();
												holder.bAck.setVisibility(View.GONE);
											} else {
												Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
											}
										}
									});
								}

							}.execute();
						}
					});

					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

						}
					});

					alert.show();
				}
			});
		} else {
			holder.bAck.setVisibility(View.GONE);
		}
		return convertView;
	}
}
