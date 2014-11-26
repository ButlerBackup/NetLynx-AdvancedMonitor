package com.netlynxtech.advancedmonitor.classes;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLFunctions {
	public static final String TAG = "SensorLynx[SQLi]";
	public static final String GLOBAL_ROWID = "_id";

	private static final String DATABASE_NAME = "sensorlynx";
	private static final String TABLE_MESSAGES = "messages";
	private static final String TABLE_MESSAGES_READ = "messagesRead";

	private static final int DATABASE_VERSION = 1;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_MESSAGES + " (" + GLOBAL_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Consts.MESSAGES_MESSAGE_ID + " TEXT NOT NULL, "
					+ Consts.MESSAGES_MESSAGE_EVENTID + " TEXT NOT NULL, " + Consts.MESSAGES_MESSAGE_TITLE + " TEXT NOT NULL, " + Consts.MESSAGES_MESSAGE_TIMESTAMP + " TEXT NOT NULL, "
					+ Consts.MESSAGES_MESSAGE_DEVICEID + " TEXT NOT NULL, " + Consts.MESSAGES_MESSAGE_MESSAGETYPE + " TEXT NOT NULL, " + Consts.MESSAGES_MESSAGE_MESSAGE + " TEXT NOT NULL, "
					+ Consts.MESSAGES_MESSAGE_ACKREQUIRED + " TEXT NOT NULL, " + Consts.MESSAGES_MESSAGE_ACKDONE + " TEXT NOT NULL, " + TABLE_MESSAGES_READ + " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
			onCreate(db);
		}

	}

	public SQLFunctions(Context c) {
		ourContext = c;
	}

	public SQLFunctions open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return null;
	}

	public void close() {
		if (ourHelper != null) {
			ourHelper.close();
		} else {
			Log.e(TAG, "You did not open your database. Null error");
		}
	}

	public long unixTime() {
		return System.currentTimeMillis() / 1000L;
	}

	public boolean longerThanTwoHours(String pTime) {
		int prevTime = Integer.parseInt(pTime);
		int currentTime = (int) (System.currentTimeMillis() / 1000L);
		int seconds = currentTime - prevTime;
		int how_many;
		if (seconds > 3600 && seconds < 86400) {
			how_many = (int) seconds / 3600;
			if (how_many >= 2) { // 2 hours
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public String getLastRowId() {
		String sql = "SELECT * FROM " + TABLE_MESSAGES + " ORDER BY " + GLOBAL_ROWID + " DESC LIMIT 1";
		Cursor cursor = ourDatabase.rawQuery(sql, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				String id = cursor.getString(cursor.getColumnIndex(GLOBAL_ROWID));
				cursor.close();
				Log.e("LATEST SQL ROW", id);
				return id;
			}
		}
		cursor.close();
		return "";
	}

	public boolean setMessageAckDone(String messageId) {
		String strFilter = Consts.MESSAGES_MESSAGE_ID + "='" + messageId + "'";
		ContentValues args = new ContentValues();
		args.put(Consts.MESSAGES_MESSAGE_ACKDONE, "1");
		if (ourDatabase.update(TABLE_MESSAGES, args, strFilter, null) > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<Message> loadMessages(String eventId) {
		ArrayList<Message> map = new ArrayList<Message>();
		Cursor cursor = ourDatabase.rawQuery("SELECT * FROM " + TABLE_MESSAGES + " WHERE " + Consts.MESSAGES_MESSAGE_EVENTID + "= '" + eventId + "'", null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false) {
					try {
						Message m = new Message();
						m.setMessageId(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_ID)));
						m.setEventId(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_EVENTID)));
						m.setTitle(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_TITLE)));
						m.setTimestamp(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_TIMESTAMP)));
						m.setDeviceId(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_DEVICEID)));
						m.setMessageType(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_MESSAGETYPE)));
						m.setMessage(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_MESSAGE)));
						m.setAckRequired(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_ACKREQUIRED)));
						m.setAckDone(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_ACKDONE)));
						map.add(m);
					} catch (Exception e) {
						e.printStackTrace();
					}
					cursor.moveToNext();
				}
			}
		}
		cursor.close();
		return map;
	}

	public ArrayList<Message> loadEventMessages() {
		ArrayList<Message> map = new ArrayList<Message>();
		Cursor cursor = ourDatabase.rawQuery("SELECT * FROM " + TABLE_MESSAGES + " GROUP BY " + Consts.MESSAGES_MESSAGE_EVENTID, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false) {
					try {
						Message m = new Message();
						m.setMessageId(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_ID)));
						m.setEventId(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_EVENTID)));
						m.setTitle(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_TITLE)));
						m.setTimestamp(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_TIMESTAMP)));
						m.setDeviceId(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_DEVICEID)));
						m.setMessageType(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_MESSAGETYPE)));
						m.setMessage(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_MESSAGE)));
						m.setAckRequired(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_ACKREQUIRED)));
						m.setAckDone(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_ACKDONE)));
						map.add(m);
					} catch (Exception e) {
						e.printStackTrace();
					}
					cursor.moveToNext();
				}
			}
		}
		cursor.close();
		return map;
	}

	public int getUnreadMessage(String eventId) {
		int count = 0;
		try {
			Cursor mCount = ourDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_MESSAGES + " WHERE " + Consts.MESSAGES_MESSAGE_EVENTID + " = '" + eventId + "'", null);
			mCount.moveToFirst();
			count = mCount.getInt(0);
			mCount.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public ArrayList<HashMap<String, String>> loadEventMessage() {
		ArrayList<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
		Cursor cursor = ourDatabase.rawQuery("SELECT * FROM " + TABLE_MESSAGES + " GROUP BY " + Consts.MESSAGES_MESSAGE_EVENTID, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false) {
					try {
						HashMap<String, String> h = new HashMap<String, String>();
						h.put("title", cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_TITLE)));
						h.put("message", cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_MESSAGE)));
						h.put("count", String.valueOf(getUnreadMessage(cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_EVENTID)))));
						h.put("eventId", cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_EVENTID)));
						map.add(h);
					} catch (Exception e) {
						e.printStackTrace();
					}
					cursor.moveToNext();
				}
			}
		}
		cursor.close();
		return map;
	}

	/*
	 * public HashMap<String, ArrayList<Message>> loadMessages() { HashMap<String, ArrayList<Message>> map = new HashMap<String, ArrayList<Message>>(); Cursor cursor =
	 * ourDatabase.rawQuery("SELECT * FROM " + TABLE_MESSAGES + " ORDER BY " + GLOBAL_ROWID + " DESC LIMIT " + new Utils(ourContext).getHousekeep(), null); if (cursor != null) { if
	 * (cursor.moveToFirst()) { while (cursor.isAfterLast() == false) { try { HashMap<String, String> hash = new HashMap<String, String>(); hash.put(GLOBAL_ROWID,
	 * cursor.getString(cursor.getColumnIndex(GLOBAL_ROWID))); hash.put(Consts.MESSAGES_MESSAGE_ID, cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_ID)));
	 * hash.put(Consts.MESSAGES_MESSAGE_EVENTID, cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_EVENTID))); hash.put(Consts.MESSAGES_MESSAGE_TITLE,
	 * cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_TITLE))); hash.put(Consts.MESSAGES_MESSAGE_TIMESTAMP, cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_TIMESTAMP)));
	 * hash.put(Consts.MESSAGES_MESSAGE_DEVICEID, cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_DEVICEID))); hash.put(Consts.MESSAGES_MESSAGE_MESSAGETYPE,
	 * cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_MESSAGETYPE))); hash.put(Consts.MESSAGES_MESSAGE_MESSAGE,
	 * cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_MESSAGE))); hash.put(Consts.MESSAGES_MESSAGE_ACKREQUIRED,
	 * cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_ACKREQUIRED))); hash.put(Consts.MESSAGES_MESSAGE_ACKDONE,
	 * cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_ACKDONE))); map.add(hash); } catch (Exception e) { e.printStackTrace(); } cursor.moveToNext(); } } } cursor.close(); return map; }
	 */

	public void insertMessage(Message m) {
		ContentValues cv = new ContentValues();
		String sql = "SELECT * FROM " + TABLE_MESSAGES + " WHERE " + Consts.MESSAGES_MESSAGE_ID + " = ?";
		Cursor cursor = ourDatabase.rawQuery(sql, new String[] { m.getMessageId() });
		if (cursor.moveToFirst()) {
			Log.e(TAG, "Message ID exist");
		} else {
			Log.e(TAG, "New Message");
			cv.put(Consts.MESSAGES_MESSAGE_ID, m.getMessageId());
			cv.put(Consts.MESSAGES_MESSAGE_EVENTID, m.getEventId());
			cv.put(Consts.MESSAGES_MESSAGE_TITLE, m.getTitle());
			cv.put(Consts.MESSAGES_MESSAGE_TIMESTAMP, m.getTimestamp());
			cv.put(Consts.MESSAGES_MESSAGE_DEVICEID, m.getDeviceId());
			cv.put(Consts.MESSAGES_MESSAGE_MESSAGETYPE, m.getMessageType());
			cv.put(Consts.MESSAGES_MESSAGE_MESSAGE, m.getMessage());
			cv.put(Consts.MESSAGES_MESSAGE_ACKREQUIRED, m.getAckRequired());
			cv.put(Consts.MESSAGES_MESSAGE_ACKDONE, "0");
			cv.put(TABLE_MESSAGES_READ, "0");

			try {
				ourDatabase.insert(TABLE_MESSAGES, null, cv);
			} catch (Exception e) {
				Log.e(TAG, "Error creating message entry", e);
			}
		}
		cursor.close();
	}
}