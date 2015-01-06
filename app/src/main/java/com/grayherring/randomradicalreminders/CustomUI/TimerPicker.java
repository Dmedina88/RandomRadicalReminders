package com.grayherring.randomradicalreminders.CustomUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.grayherring.randomradicalreminders.Util.Util;

/**
 * Created by David on 12/20/2014.
 */
public class TimerPicker extends DialogPreference {
    private int lastHour = 0;
    private int lastMinute = 0;
    private TimePicker picker = null;


    public TimerPicker(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");

    }


    @Override
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());


        return (picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            lastHour = picker.getCurrentHour();
            lastMinute = picker.getCurrentMinute();
            String time;
            if (lastMinute < 10) {
                time = String.valueOf(lastHour) + ":0" + String.valueOf(lastMinute);
            } else {
                time = String.valueOf(lastHour) + ":" + String.valueOf(lastMinute);
            }


            if (callChangeListener(time)) {
                persistString(time);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time = null;

        if (restoreValue) {
            if (defaultValue == null) {
                time = getPersistedString("00:00");
            } else {
                time = getPersistedString(defaultValue.toString());
            }
        } else {
            time = defaultValue.toString();
        }

        lastHour = Util.getHour(time);
        lastMinute = Util.getMinute(time);
    }


}