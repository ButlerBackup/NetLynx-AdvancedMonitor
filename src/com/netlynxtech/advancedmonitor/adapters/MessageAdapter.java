package com.netlynxtech.advancedmonitor.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manuelpeinado.multichoiceadapter.extras.actionbarcompat.MultiChoiceBaseAdapter;
import com.netlynxtech.advancedmonitor.R;
import com.netlynxtech.advancedmonitor.classes.SQLFunctions;

public class MessageAdapter extends MultiChoiceBaseAdapter {
	Context context;
	ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public MessageAdapter(Bundle savedInstanceState, Context context, ArrayList<HashMap<String, String>> data) {
		super(savedInstanceState);
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public HashMap<String, String> getItem(int position) {
		return data.get(position);
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
	public View getViewImpl(final int position, View convertView, ViewGroup parent) {
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
		if (map.get("read").equals("0")) {
			holder.tvMessage.setTypeface(null, Typeface.BOLD);
			holder.tvTitle.setTypeface(null, Typeface.BOLD);
		} else {
			holder.tvMessage.setTypeface(null, Typeface.NORMAL);
			holder.tvTitle.setTypeface(null, Typeface.NORMAL);
		}
		return convertView;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.menu_action_mode, menu);
		return true;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		if (item.getItemId() == R.id.menu_discard) {
			showDeleteDialog();
			return true;
		}
		return false;
	}

	private void discardSelectedItems() {
		Set<Long> selection = getCheckedItems();
		ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		for (long position : selection) {
			items.add(getItem((int) position));
		}
		for (HashMap<String, String> item : items) {
			String eventId = item.get("eventId");
			deleteEventMessages(eventId);
			data.remove(item);
		}
		finishActionMode();
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	private void showDeleteDialog() {
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		adb.setTitle("Delete Messages?");
		adb.setMessage("Delete selected event's messages?");
		adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				discardSelectedItems();
				return;
			}
		});

		adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		adb.show();
	}

	private void deleteEventMessages(final String eventId) {
		SQLFunctions sql = new SQLFunctions(context);
		sql.open();
		sql.deleteEvent(eventId);
		sql.close();
	}
}
