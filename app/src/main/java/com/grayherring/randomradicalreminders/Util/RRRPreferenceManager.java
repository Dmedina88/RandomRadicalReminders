package com.grayherring.randomradicalreminders.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;

import com.grayherring.randomradicalreminders.R;

/**
 * Created by David on 12/27/2014.
 */
public class RRRPreferenceManager {

    public static String getMessage(Context context) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();
        String result = prefs.getString(resources.getString(R.string.message_key), "NO MESSAGE FOUND!");
        return result;
    }

    public static boolean isAlarm(Context context) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();
        boolean result = prefs.getBoolean(resources.getString(R.string.alarm), false);
        return result;
    }

    public static String getStartTime(Context context) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();
        String result = prefs.getString(resources.getString(R.string.start_time), "00:00");
        return result;
    }

    public static String getEndTime(Context context) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();
        String result = prefs.getString(resources.getString(R.string.end_time), "00:00");
        return result;
    }

    public static boolean isUsingRecording(Context context) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();
        boolean result = prefs.getBoolean(resources.getString(R.string.use_recording), false);
        return result;
    }

    public static String getRingtone(Context context) {
        Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();
        String result = prefs.getString(resources.getString(R.string.reingtone_key), ringtone.toString());
        return result;
    }

    public static long getAlarmTime(Context context) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();
        long result = prefs.getLong(resources.getString(R.string.alarm_time_key), 0L);
        return result;
    }


    public static void setAlarmTime(Context context, long time) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(resources.getString(R.string.alarm_time_key), time);
        editor.commit();

    }

    public static void setAlarmStartMilis(Context context, long time) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(resources.getString(R.string.alarm_start_key), time);
        editor.commit();
    }

    public static void setAlarmEndMilis(Context context, long time) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(resources.getString(R.string.alarm_end_key), time);
        editor.commit();
    }


    public static long getAlarmStartMilis(Context context) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();
        long result = prefs.getLong(resources.getString(R.string.alarm_start_key), 0L);
        return result;
    }

    public static long getAlarmEndMilis(Context context) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();
        long result = prefs.getLong(resources.getString(R.string.alarm_end_key), 0L);
        return result;
    }


    public static void resetTimes(Context context, String startTime, String endTime) {
        RRRPreferenceManager.setAlarmStartMilis(context, Util.timeStringToMillis(startTime));
        RRRPreferenceManager.setAlarmEndMilis(context, Util.timeStringToMillis(endTime));
        RRRPreferenceManager.setAlarmTime(context, 0L);
    }

    public static void resetTimes(Context context) {
        resetTimes(context, getStartTime(context), getEndTime(context));

    }
}
