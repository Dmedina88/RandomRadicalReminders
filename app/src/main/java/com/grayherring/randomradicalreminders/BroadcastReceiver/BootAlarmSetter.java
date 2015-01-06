package com.grayherring.randomradicalreminders.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootAlarmSetter extends BroadcastReceiver {
    AlarmReceiver alarmReceiver = new AlarmReceiver();

    public BootAlarmSetter() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            alarmReceiver.setAlarm(context);
        }
    }
}
