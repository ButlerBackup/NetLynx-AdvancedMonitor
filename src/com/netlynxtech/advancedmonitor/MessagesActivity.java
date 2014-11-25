package com.netlynxtech.advancedmonitor;

import java.util.ArrayList;

import mehdi.sakout.dynamicbox.DynamicBox;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;
import com.netlynxtech.advancedmonitor.adapters.MessageAdapter;
import com.netlynxtech.advancedmonitor.classes.Message;
import com.netlynxtech.advancedmonitor.classes.WebRequestAPI;

public class MessagesActivity extends ActionBarActivity {
	ArrayList<Message> data;
	DynamicBox box;
	ListView lvMessage;
	getMessages mTask;
	RefreshActionItem mRefreshActionItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Messages");
		lvMessage = (ListView) findViewById(R.id.lvMessages);
		lvMessage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Message m = data.get(position);
				startActivity(new Intent(MessagesActivity.this, SubMessagesActivity.class).putExtra("message", m));
			}
		});
		box = new DynamicBox(MessagesActivity.this, lvMessage);
		box.setClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					mTask = null;
					mTask = new getMessages();
					mTask.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private class getMessages extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			box.showLoadingLayout();
			mRefreshActionItem.showProgress(true);
		}

		@Override
		protected void onPostExecute(Void result) {
			mRefreshActionItem.showProgress(false);
			super.onPostExecute(result);
			MessagesActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (data != null && data.size() > 0) {
						MessageAdapter adapter = new MessageAdapter(MessagesActivity.this, data);
						lvMessage.setAdapter(adapter);
						box.hideAll();
					} else {
						box.setOtherExceptionMessage("No messages");
						box.showExceptionLayout();
					}
				}
			});
		}

		@Override
		protected Void doInBackground(Void... params) {
			data = new WebRequestAPI(MessagesActivity.this).GetMessages();
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_messages, menu);
		MenuItem item = menu.findItem(R.id.menu_individual_refresh);
		mRefreshActionItem = (RefreshActionItem) MenuItemCompat.getActionView(item);
		mRefreshActionItem.setMenuItem(item);
		mRefreshActionItem.setProgressIndicatorType(ProgressIndicatorType.INDETERMINATE);
		mRefreshActionItem.setRefreshActionListener(new RefreshActionListener() {

			@Override
			public void onRefreshButtonClick(RefreshActionItem sender) {
				try {
					mTask = null;
					mTask = new getMessages();
					mTask.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			mTask = null;
			mTask = new getMessages();
			mTask.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
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
}
