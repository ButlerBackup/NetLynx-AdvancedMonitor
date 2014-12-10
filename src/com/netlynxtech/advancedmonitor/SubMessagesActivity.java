package com.netlynxtech.advancedmonitor;

import java.util.ArrayList;

import mehdi.sakout.dynamicbox.DynamicBox;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.netlynxtech.advancedmonitor.adapters.SubMessageAdapter;
import com.netlynxtech.advancedmonitor.classes.Message;
import com.netlynxtech.advancedmonitor.classes.SQLFunctions;

public class SubMessagesActivity extends ActionBarActivity {
	DynamicBox box;
	ListView lvMessage;
	getMessages mTask;
	String eventId;
	ArrayList<Message> data = new ArrayList<Message>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		eventId = getIntent().getStringExtra("eventId");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		lvMessage = (ListView) findViewById(R.id.lvMessages);
		box = new DynamicBox(SubMessagesActivity.this, lvMessage);
		mTask = null;
		mTask = new getMessages();
		mTask.execute();
	}

	private class getMessages extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			box.showLoadingLayout();
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			SubMessagesActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (data != null && data.size() > 0) {
						SubMessageAdapter adapter = new SubMessageAdapter(SubMessagesActivity.this, data);
						lvMessage.setAdapter(adapter);
						box.hideAll();
					} else {
						box.setExceptionMessageColor("#ff0040");
						box.setExceptionTitleColor("#ff0040");
						box.setOtherExceptionMessage("No messages");
						box.showExceptionLayout();
					}
				}
			});
		}

		@Override
		protected Void doInBackground(Void... params) {
			SQLFunctions sql = new SQLFunctions(SubMessagesActivity.this);
			sql.open();
			data = sql.loadMessages(eventId);
			sql.close();
			return null;
		}
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