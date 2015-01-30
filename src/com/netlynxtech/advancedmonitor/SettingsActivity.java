package com.netlynxtech.advancedmonitor;

import net.margaritov.preference.colorpicker.ColorPickerPreference;
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
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.netlynxtech.advancedmonitor.classes.Utils;
import com.securepreferences.SecurePreferences;

public class SettingsActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_activity);
		findPreference("pref_tutorial_restart").setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				startActivity(new Intent(SettingsActivity.this, TutorialChooseActivity.class).putExtra("addNew", true));
				return true;
			}
		});
		findPreference("pref_cp_graph_values_text").setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				new Utils(SettingsActivity.this).storeSecurePreference("pref_cp_graph_values_text", ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue))));
				return true;
			}

		});
		findPreference("pref_cp_graph_text").setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				new Utils(SettingsActivity.this).storeSecurePreference("pref_cp_graph_text", ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue))));
				return true;
			}

		});
		((ColorPickerPreference) findPreference("pref_cp_temperature_graph_color")).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				new Utils(SettingsActivity.this).storeSecurePreference("pref_cp_temperature_graph_color", ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue))));
				return true;
			}

		});
		((ColorPickerPreference) findPreference("pref_cp_humidity_graph_color")).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				new Utils(SettingsActivity.this).storeSecurePreference("pref_cp_humidity_graph_color", ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue))));
				return true;
			}

		});
		((ColorPickerPreference) findPreference("pref_cp_graph_line")).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				new Utils(SettingsActivity.this).storeSecurePreference("pref_cp_graph_line", ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue))));
				return true;
			}

		});
		((ColorPickerPreference) findPreference("pref_cp_graph_threshold_max")).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				new Utils(SettingsActivity.this).storeSecurePreference("pref_cp_graph_threshold_max", ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue))));
				return true;
			}

		});
		((ColorPickerPreference) findPreference("pref_cp_graph_threshold_min")).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Log.e("VALUE", ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue))));
				new Utils(SettingsActivity.this).storeSecurePreference("pref_cp_graph_threshold_min", ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue))));
				return true;
			}

		});
		final ListPreference pref_graph_history_amount = (ListPreference) findPreference("pref_graph_history_amount");
		pref_graph_history_amount.setSummary(SettingsActivity.this.getResources().getString(R.string.pref_housekeep_summary).toString()
				.replace(" X ", " " + new Utils(SettingsActivity.this).getHousekeep() + " "));
		pref_graph_history_amount.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if (newValue.equals("1337")) {
					AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
					alert.setTitle("Graph History Entries Amount");
					alert.setMessage("Input how many history entries you would like to see?");

					final EditText input = new EditText(SettingsActivity.this);
					input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
					input.setText(new Utils(SettingsActivity.this).getHousekeep());

					alert.setView(input);

					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String value = input.getText().toString().replaceAll("[^\\d]", "");
							if (value.length() < 0) {
								value = "6";
							}
							pref_graph_history_amount.setSummary(SettingsActivity.this.getResources().getString(R.string.pref_housekeep_summary).toString().replace(" X ", " " + value + " "));
							SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
							preferences.edit().putString("pref_graph_history_amount", value).commit();
						}
					});
					alert.show();
				} else {
					pref_graph_history_amount.setSummary(SettingsActivity.this.getResources().getString(R.string.pref_housekeep_summary).toString().replace(" X ", " " + newValue + " "));
				}
				return true;
			}
		});
		Preference pref_about = (Preference) findPreference("pref_about");
		pref_about.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				startActivity(new Intent(SettingsActivity.this, AboutUsActivity.class));
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
