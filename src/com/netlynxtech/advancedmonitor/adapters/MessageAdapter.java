package com.netlynxtech.advancedmonitor.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.netlynxtech.advancedmonitor.R;

public class MessageAdapter extends BaseAdapter {
	Context context;
	ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public MessageAdapter(Context context, ArrayList<HashMap<String, String>> data) {
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
		TextView tvCount;
		TextView tvMessage;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_message_item, parent, false);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvCount = (TextView) convertView.findViewById(R.id.tvCount);
			holder.tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HashMap<String, String> map = data.get(position);
		holder.tvTitle.setText(map.get("title"));
		holder.tvCount.setText("(" + map.get("count") + ")");
		holder.tvMessage.setText(map.get("message"));
		return convertView;
	}
}
