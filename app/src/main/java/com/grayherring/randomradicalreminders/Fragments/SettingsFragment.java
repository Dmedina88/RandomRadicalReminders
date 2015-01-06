package com.grayherring.randomradicalreminders.Fragments;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.util.Log;
import android.widget.Toast;

import com.grayherring.randomradicalreminders.BroadcastReceiver.AlarmReceiver;
import com.grayherring.randomradicalreminders.CustomUI.TimerPicker;
import com.grayherring.randomradicalreminders.R;
import com.grayherring.randomradicalreminders.Util.Constants;
import com.grayherring.randomradicalreminders.Util.RRRPreferenceManager;
import com.grayherring.randomradicalreminders.Util.Util;


public class SettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    AlarmReceiver mAlarm = new AlarmReceiver();

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        initSummaries(getPreferenceScreen());
        TimerPicker timerPicker = (TimerPicker) findPreference(getString(R.string.start_time));
        timerPicker.setOnPreferenceChangeListener(this);
        timerPicker = (TimerPicker) findPreference(getString(R.string.end_time));
        timerPicker.setOnPreferenceChangeListener(this);
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(getString(R.string.alarm));
        checkBoxPreference.setOnPreferenceChangeListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        RingtonePreference pref = (RingtonePreference) findPreference(getString(R.string.reingtone_key));
        pref.setOnPreferenceChangeListener(this);


    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        RingtonePreference pref = (RingtonePreference) findPreference(getString(R.string.reingtone_key));

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.ALARM_CHANGED, true);
        Preference pref = findPreference(key);
        setSummary(pref);

    }

    private void setSummary(Preference pref) {


        if (pref instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) pref;
            if (pref.getTitle().toString().contains("Message")) {
                pref.setSummary(editTextPref.getText());
                return;
            }
        }
       /* if (pref instanceof MultiSelectListPreference) {
            MultiSelectListPreference multipref = (MultiSelectListPreference) pref;
            if (pref.getTitle().toString().contains("Set Days")) {
                StringBuilder stringBuilder = new StringBuilder();

                //Set<String> fullset = multipref.getValues();
                //fullset.removeAll(Arrays.asList(multipref.getEntries()));

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Set<String> fullset = sharedPrefs.getStringSet(resources.getString(R.string.day_key), null);

                Object[] objects = fullset.toArray();
                List<String> selections = Arrays.asList(Arrays.copyOf(objects, objects.length, String[].class));
                selections.removeAll(Arrays.asList("", null));
                if (selections.size() > 0) {
                    stringBuilder.append(selections.get(0));
                }
                for (int i = 1; i < selections.size(); i++) {
                    stringBuilder.append(", ");
                    stringBuilder.append(selections.get(i));
                }
                pref.setSummary(stringBuilder.toString());
                return;
            }
        }*/
        if (pref instanceof RingtonePreference) {

            String strRingtonePreference = RRRPreferenceManager.getRingtone(getActivity());
            RingtonePreference ringtonePreference = (RingtonePreference) pref;
            Uri ringtoneUri = Uri.parse(strRingtonePreference);
            Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), ringtoneUri);
            String name = ringtone.getTitle(getActivity());
            ringtonePreference.setSummary(name);
        }
        if (pref instanceof TimerPicker) {

            TimerPicker timerPicker = (TimerPicker) pref;
            if (timerPicker.getKey().equals(getResources().getString(R.string.start_time))) {
                timerPicker.setSummary(RRRPreferenceManager.getStartTime(getActivity()));
            } else {
                timerPicker.setSummary(RRRPreferenceManager.getEndTime(getActivity()));
            }

        }


    }

    private void initSummaries(PreferenceGroup pg) {
        for (int i = 0; i < pg.getPreferenceCount(); ++i) {
            Preference p = pg.getPreference(i);
            if (p instanceof PreferenceGroup)
                this.initSummaries((PreferenceGroup) p); // recursion
            else
                this.setSummary(p);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference instanceof CheckBoxPreference) {
            if (preference.getKey().equals(getResources().getString(R.string.alarm))) {
                if ((boolean) newValue) {
                    Toast.makeText(getActivity(), "Alarm Updated", Toast.LENGTH_LONG).show();
                    RRRPreferenceManager.resetTimes(getActivity());
                    mAlarm.setAlarmUnChecked(getActivity());
                } else {
                    Log.i(Util.LOG, "not clicked");
                    mAlarm.cancelAlarm(getActivity());
                }
            }
        }


        if (preference instanceof RingtonePreference) {
            updateRingtoneSummary((RingtonePreference) preference, Uri.parse((String) newValue));
        }


        if (preference instanceof TimerPicker) {
            TimerPicker timerPicker = (TimerPicker) preference;
            String time = (String) newValue;
            timerPicker.setSummary(time);
            //check to see if value changed
            if (timerPicker.getKey().equals(getResources().getString(R.string.start_time))) {
                if (!RRRPreferenceManager.getStartTime(getActivity()).equals(newValue)) {
                    updateAlarm(time, RRRPreferenceManager.getEndTime(getActivity()));
                }
            } else if (timerPicker.getKey().equals(getResources().getString(R.string.end_time))) {
                if (!RRRPreferenceManager.getEndTime(getActivity()).equals(newValue)) {
                    updateAlarm(RRRPreferenceManager.getStartTime(getActivity()), time);
                }
            }
        }
        return true;
    }

    private void updateAlarm(String startTime, String endTime) {
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(getResources().getString(R.string.alarm));
        if (checkBoxPreference.isChecked()) {
            Toast.makeText(getActivity(), "Alarm Updated", Toast.LENGTH_LONG).show();
            RRRPreferenceManager.resetTimes(getActivity(), startTime, endTime);
            mAlarm.setAlarm(getActivity());
        }
    }

    private void updateRingtoneSummary(RingtonePreference preference, Uri ringtoneUri) {
        Ringtone ringtone = RingtoneManager.getRingtone(this.getActivity(), ringtoneUri);
        if (ringtone != null)
            preference.setSummary(ringtone.getTitle(this.getActivity()));
        else
            preference.setSummary("Default");
    }


}
