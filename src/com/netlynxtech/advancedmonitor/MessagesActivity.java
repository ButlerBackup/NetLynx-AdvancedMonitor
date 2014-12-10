package com.netlynxtech.advancedmonitor;

import java.util.ArrayList;
import java.util.HashMap;

import mehdi.sakout.dynamicbox.DynamicBox;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;
import com.netlynxtech.advancedmonitor.adapters.MessageAdapter;
import com.netlynxtech.advancedmonitor.classes.Consts;
import com.netlynxtech.advancedmonitor.classes.SQLFunctions;
import com.netlynxtech.advancedmonitor.classes.WebRequestAPI;
import com.securepreferences.SecurePreferences;

public class MessagesActivity extends ActionBarActivity {
	ArrayList<HashMap<String, String>> data;
	DynamicBox box;
	ListView lvMessage;
	getMessages mTask;
	RefreshActionItem mRefreshActionItem;
	messagesMarkRead mTaskRead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Messages");
		lvMessage = (ListView) findViewById(R.id.lvMessages);
		lvMessage.setFastScrollEnabled(true);
		lvMessage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				HashMap<String, String> map = data.get(position);
				String eventId = map.get("eventId");
				// Message m = data.get(position);
				startActivity(new Intent(MessagesActivity.this, SubMessagesActivity.class).putExtra("eventId", eventId));
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
					if (!isCancelled()) {
						if (data != null && data.size() > 0) {
							MessageAdapter adapter = new MessageAdapter(MessagesActivity.this, data);
							lvMessage.setAdapter(adapter);
							box.hideAll();
						} else {
							box.setExceptionMessageColor("#ff0040");
							box.setExceptionTitleColor("#ff0040");
							box.setOtherExceptionTitle("No messages");
							box.setOtherExceptionMessage("No messages");
							box.showExceptionLayout();
						}
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
		case R.id.menu_mark_all_read:
			Log.e("WUT", "WUT");
			showMarkAllReadDialog();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	private void showMarkAllReadDialog() {
		final SecurePreferences sp = new SecurePreferences(MessagesActivity.this);
		AlertDialog.Builder adb = new AlertDialog.Builder(MessagesActivity.this);
		LayoutInflater adbInflater = LayoutInflater.from(MessagesActivity.this);
		View eulaLayout = adbInflater.inflate(R.layout.message_dialog_mark_all_read, null);
		final CheckBox dontShowAgain = (CheckBox) eulaLayout.findViewById(R.id.skip);
		adb.setView(eulaLayout);
		adb.setTitle("Mark All As Read");
		adb.setMessage("Mark all messages as read?\nNote: Messages that needs acknowledgement will not be marked as read. You have to acknowledge the messages individually");
		adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (dontShowAgain.isChecked()) {
					sp.edit().putBoolean(Consts.PREF_MESSAGES_MARK_READ, true).commit();
				}
				try {
					mTaskRead = null;
					mTaskRead = new messagesMarkRead();
					mTaskRead.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		});

		adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (dontShowAgain.isChecked()) {
					sp.edit().putBoolean(Consts.PREF_MESSAGES_MARK_READ, true).commit();
				}
				return;
			}
		});

		if (!sp.getBoolean(Consts.PREF_MESSAGES_MARK_READ, false)) {
			adb.show();
		} else {
			try {
				mTaskRead = null;
				mTaskRead = new messagesMarkRead();
				mTaskRead.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class messagesMarkRead extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(MessagesActivity.this);
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.setMessage("Please hold..");
			pd.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			MessagesActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
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

		@Override
		protected Void doInBackground(Void... params) {
			SQLFunctions sql = new SQLFunctions(MessagesActivity.this);
			sql.open();
			sql.markAllAsRead();
			sql.close();
			return null;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		supportInvalidateOptionsMenu();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING) {
			mTask.cancel(true);
			mTask = null;
		}
		if (mTaskRead != null && mTaskRead.getStatus() == AsyncTask.Status.RUNNING) {
			mTaskRead.cancel(true);
			mTaskRead = null;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING) {
			mTask.cancel(true);
			mTask = null;
		}
		if (mTaskRead != null && mTaskRead.getStatus() == AsyncTask.Status.RUNNING) {
			mTaskRead.cancel(true);
			mTaskRead = null;
		}
	}

}
