package com.netlynxtech.advancedmonitor.classes;

import com.netlynxtech.advancedmonitor.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.securepreferences.SecurePreferences;

public class Utils {

	Context context;

	public Utils(Context con) {
		this.context = con;
	}

	public String getHousekeep() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString("pref_housekeep", "20");
	}

	public void showNotifications(String shortTitle, String title, String message, String messageType) {
		SecurePreferences sp = new SecurePreferences(context);
		long[] vibration;
		if (sp.getBoolean("pref_vibration", false)) {
			vibration = new long[] { 100, 250, 100, 500 };
		} else {
			vibration = new long[] { 0 };
		}
		SharedPreferences getAlarms = PreferenceManager.getDefaultSharedPreferences(context);
		String alarms = "";
		Intent myIntent = null;
		if (messageType.equals("1")) {
			alarms = getAlarms.getString("pref_notification_alert", "default ringtone");
			myIntent = new Intent(context, MessagesActivity.class);
		} else if (messageType.equals("2")) {
			alarms = getAlarms.getString("pref_notification_restore", "default ringtone");
		} else if (messageType.equals("3")) {
			alarms = getAlarms.getString("pref_notification_ack", "default ringtone");
		} else if (messageType.equals("4")) {
			alarms = getAlarms.getString("pref_notification_enrollment", "default ringtone");
			myIntent = new Intent(context, ReceivedMemberPermissionActivity.class);
		} else if (messageType.equals("5")) {
			alarms = getAlarms.getString("pref_notification_notification", "default ringtone");
		}
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		myIntent.putExtra("notification", "true");
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		NotificationCompat.Builder myNotification = new NotificationCompat.Builder(context);
		myNotification.setContentTitle(title).setContentText(message).setTicker(shortTitle).setWhen(System.currentTimeMillis()).setContentIntent(pendingIntent).setAutoCancel(true)
				.setSmallIcon(R.drawable.ic_launcher).setSound(Uri.parse(alarms)).setVibrate(vibration);

		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(999, myNotification.build());

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		if (settings.getBoolean("pref_force_sound", false)) {
			AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

			switch (am.getRingerMode()) {
			case AudioManager.RINGER_MODE_SILENT:
			case AudioManager.RINGER_MODE_VIBRATE:
				playNotificationSound(messageType);
				break;
			}
		}
	}

	private MediaPlayer mMediaPlayer;

	public void playNotificationSound(String messageType) {
		SharedPreferences getAlarms = PreferenceManager.getDefaultSharedPreferences(context);
		String alarms = "";
		if (messageType.equals("1")) {
			alarms = getAlarms.getString("pref_notification_ack", "default ringtone");
		} else {
			alarms = getAlarms.getString("pref_notification_normal", "default ringtone");
		}
		Uri uri = Uri.parse(alarms);
		playSound(context, uri);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				mMediaPlayer.stop();
			}
		}, 5000);
		//
	}

	private void playSound(Context context, Uri alert) {
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getRingtoneName(String path) {
		Uri uri = Uri.parse(path);
		Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
		return ringtone.getTitle(context);
	}

	public static String getTimeFromDateTime(String datetime) {
		String time = "";
		String[] d = datetime.split("");
		return d[1];
	}

	public static Calendar currentTime() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		c.setTimeInMillis(System.currentTimeMillis());

		// http://stackoverflow.com/a/11677405
		// http://www.objectdefinitions.com/odblog/2007/converting-java-dates-to-xml-schema-datetime-with-timezone/
		return c;
	}

	public static String getCurrentDateTime() {
		long unixTime = System.currentTimeMillis() / 1000L;
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.ENGLISH);
		df.setTimeZone(tz);
		String nowAsISO = df.format(new Date());
		Log.e("From", String.valueOf(unixTime));
		return String.valueOf(unixTime);
	}

	public static String getCustomDateTime() {
		String nowAsISO = null;
		return String.valueOf((System.currentTimeMillis() - 3600000) / 1000L);

	}

	public static String parseTimeToMinutesOnly(String datetime) {
		final String pattern = "yyyy-MM-dd'T'HH:mm:ss";
		final SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
		try {
			SimpleDateFormat outFormatter;
			outFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
			Date d = sdf.parse(datetime);
			return outFormatter.format(d).toString();
		} catch (Exception e) {
			return datetime;
		}
	}

	public static String parseTime(String datetime) {
		final String pattern = "yyyy-MM-dd'T'HH:mm:ss";
		final SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
		try {
			SimpleDateFormat outFormatter;
			outFormatter = new SimpleDateFormat("d MMMM yyyy HH:mm", Locale.getDefault()); // 24 hour
			Date d = sdf.parse(datetime);
			return outFormatter.format(d).toString();
		} catch (Exception e) {
			return datetime;
		}
	}

	public String getDeviceUniqueId() {
		SecurePreferences sp = new SecurePreferences(context);
		String id = sp.getString(Consts.PREFERENCES_UDID, "");
		if (id.equals("")) {
			id = getUnique();
			sp.edit().putString(Consts.PREFERENCES_UDID, id).commit();
		}
		Log.e("Unique ID", id);
		return id;
	}

	private String getUnique() {
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();
		Log.e("Unique ID", deviceId);
		return deviceId;
	}

	public String pregmatchIP(String text) {
		// String data = new UDPClass("255.255.255.255", "1025", Consts.UDP_BROADCAST_TODEVICE).run(); Pattern pattern4 =
		Pattern p = Pattern.compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]" + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
				+ "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}" + "|[1-9][0-9]|[0-9]))");
		Matcher matcher = p.matcher(text);
		if (matcher.find()) {
			return matcher.group(1).trim();
		}
		return "";
	}

	public void turnOnWifi() {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// boolean wifiEnabled = wifiManager.isWifiEnabled()
		wifiManager.setWifiEnabled(true);
	}

	public ArrayList<HashMap<String, String>> getWifiDevices() {
		final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiManager.startScan();
		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent intent) {
				final List<ScanResult> results = wifiManager.getScanResults();
				for (ScanResult n : results) {
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("name", n.SSID);
					item.put("capabilities", n.capabilities);
					Log.e(n.SSID, n.capabilities);
					data.add(item);
				}
			}
		}, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		return data;
	}

	public String parseDatetime(String datetime) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		// Log.e("DATE DATE", sp.getString("pref_timing", "1"));
		final String pattern = "yyyy-MM-dd'T'HH:mm:ss";
		final SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
		try {
			SimpleDateFormat outFormatter;
			if (sp.getString("pref_timing", "1").equals("1")) {
				outFormatter = new SimpleDateFormat("d MMMM yyyy HH:mm", Locale.getDefault()); // 24 hour
			} else {
				outFormatter = new SimpleDateFormat("d MMMM yyyy KK:mma", Locale.getDefault()); // 12 hour
			}
			Date d = sdf.parse(datetime);
			return outFormatter.format(d).toString();
		} catch (Exception e) {
			return datetime;
		}
	}

	public void setGCMID(String id) {
		SecurePreferences sp = new SecurePreferences(context);
		sp.edit().putString(Consts.PREFERENCES_GCMID, id).commit();
	}

	public void setPhoneNumber(String id) {
		SecurePreferences sp = new SecurePreferences(context);
		sp.edit().putString(Consts.PREFERENCES_PHONE_NO, id).commit();
	}

	public String getHandphoneNumber() {
		SecurePreferences sp = new SecurePreferences(context);
		String id = sp.getString(Consts.PREFERENCES_PHONE_NO, "");
		return id;
	}

	public String getGCMID() {
		SecurePreferences sp = new SecurePreferences(context);
		String id = sp.getString(Consts.PREFERENCES_GCMID, "");
		return id;
	}
}
