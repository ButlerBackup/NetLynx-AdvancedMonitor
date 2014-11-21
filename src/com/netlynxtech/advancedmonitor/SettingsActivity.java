package com.netlynxtech.advancedmonitor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;

import com.netlynxtech.advancedmonitor.classes.Utils;
import com.securepreferences.SecurePreferences;

public class SettingsActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// getSupportActionBar().setHomeButtonEnabled(true);
		addPreferencesFromResource(R.xml.settings_activity);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

		Preference pref_about = (Preference) findPreference("pref_about");
		pref_about.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// startActivity(new Intent(SettingsActivity.this, AboutUsActivity.class));
				return true;
			}
		});

		final ListPreference pref_housekeep = (ListPreference) findPreference("pref_housekeep");
		pref_housekeep.setSummary(SettingsActivity.this.getResources().getString(R.string.pref_housekeep_summary).toString()
				.replace(" X ", " " + new Utils(SettingsActivity.this).getHousekeep() + " "));
		pref_housekeep.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if (newValue.equals("60")) {
					AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
					alert.setTitle("Housekeep Amount");
					alert.setMessage("Input how many alerts you would like to show");

					final EditText input = new EditText(SettingsActivity.this);
					input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
					input.setText(new Utils(SettingsActivity.this).getHousekeep());

					alert.setView(input);

					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String value = input.getText().toString().replaceAll("[^\\d]", "");
							if (value.length() < 0) {
								value = "20";
							}
							pref_housekeep.setSummary(SettingsActivity.this.getResources().getString(R.string.pref_housekeep_summary).toString().replace(" X ", " " + value + " "));
							SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
							preferences.edit().putString("pref_housekeep", value).commit();
						}
					});
					alert.show();
				} else {
					pref_housekeep.setSummary(SettingsActivity.this.getResources().getString(R.string.pref_housekeep_summary).toString().replace(" X ", " " + newValue + " "));
				}
				return true;
			}
		});

		Preference pref_reset = (Preference) findPreference("pref_reset");
		pref_reset.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
				builder.setMessage(SettingsActivity.this.getResources().getString(R.string.pref_reset_summary)).setPositiveButton("Yes", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
						sp.edit().clear().commit();
						SecurePreferences scp = new SecurePreferences(SettingsActivity.this);
						scp.edit().clear().commit();
						Intent i = SettingsActivity.this.getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
						finish();
					}
				}).setNegativeButton("No", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();

				return true;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
