package com.grayherring.randomradicalreminders.BroadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.grayherring.randomradicalreminders.Activitys.AlarmActivity;
import com.grayherring.randomradicalreminders.Services.AlarmService;
import com.grayherring.randomradicalreminders.Util.RRRPreferenceManager;
import com.grayherring.randomradicalreminders.Util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    PendingIntent mPendingIntent;
    AlarmManager mPlarmManager;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //  Intent i = new Intent(context,AlarmActivity.class);
        // context.startActivity(i);

        Intent service = new Intent(context, AlarmService.class);
        startWakefulService(context, service);
    }

    public void setAlarm(Context context) {
        Log.i(Util.LOG, "setAlarm");
        if (RRRPreferenceManager.isAlarm(context)) {
            setAlarmUnChecked(context, false);
        }
    }

    public void setAlarmNextCycle(Context context) {
        Log.i(Util.LOG, "setAlarmNextCycle");
        if (RRRPreferenceManager.isAlarm(context)) {
            setAlarmUnChecked(context, true);
        }
    }

    public void setAlarmUnChecked(Context context) {
        setAlarmUnChecked(context, false);
    }

    public void setAlarmUnChecked(Context context, boolean nextCycle) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        Date dateObj = new Date();
        long nowTime = Util.timeStringToMillis(df.format(dateObj));
        long alarmTime = RRRPreferenceManager.getAlarmTime(context);
        long startTime = RRRPreferenceManager.getAlarmStartMilis(context);
        long endTime = RRRPreferenceManager.getAlarmEndMilis(context);
        if (nextCycle) {
            startTime += Util.DAY;
            endTime += Util.DAY;
        }


        if (alarmTime < System.currentTimeMillis()) {
            // long ajustment;

            //endtime must be for the next day
            if (startTime > endTime) {
                endTime += Util.DAY;

            }

            if (startTime < nowTime && endTime < nowTime) {
                startTime = startTime + Util.DAY;
                endTime = endTime + Util.DAY;

            }

            //time can not match
            if (startTime == endTime) {
                endTime += (Util.SECOND * 10L);

            }
            if (startTime < nowTime && endTime > nowTime) {
                startTime = nowTime + (Util.SECOND * 10L);
                endTime = endTime + (Util.SECOND * 10L);
            }
            Random random = new Random();
            alarmTime = nextLong(random, (endTime - startTime)) + startTime;
            if (alarmTime > endTime || alarmTime < startTime) {
                Log.d(Util.LOG, "WTFFF");
            }

            if (nowTime > alarmTime) {
                alarmTime = nowTime - alarmTime;
            } else if (nowTime < alarmTime) {
                alarmTime = alarmTime - nowTime;
            }

            alarmTime = alarmTime + System.currentTimeMillis();

            RRRPreferenceManager.setAlarmStartMilis(context, startTime);
            RRRPreferenceManager.setAlarmEndMilis(context, endTime);
            RRRPreferenceManager.setAlarmTime(context, alarmTime);
        }
        Intent i = new Intent(context, AlarmActivity.class);
        mPendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        mPlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mPlarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, mPendingIntent);

        ComponentName receiver = new ComponentName(context, BootAlarmSetter.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context) {
        if (mPlarmManager != null) {
            mPlarmManager.cancel(mPendingIntent);
        }

        ComponentName receiver = new ComponentName(context, BootAlarmSetter.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    long nextLong(Random random, long n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be positive" + n);

        long bits, val;
        do {
            bits = (random.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits - val + (n - 1) < 0L);
        return val;
    }

}
