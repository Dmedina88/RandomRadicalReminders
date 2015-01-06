package com.grayherring.randomradicalreminders.Activitys;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.grayherring.randomradicalreminders.BroadcastReceiver.AlarmReceiver;
import com.grayherring.randomradicalreminders.R;
import com.grayherring.randomradicalreminders.Util.Constants;
import com.grayherring.randomradicalreminders.Util.RRRPreferenceManager;
import com.grayherring.randomradicalreminders.Util.Util;

import java.io.IOException;
import java.util.ArrayList;

public class AlarmActivity extends BaseActivity {

    private TextView mMessageTextView;
    private MediaPlayer mPlayer = null;
    private AlarmReceiver mAlarmReceiver = new AlarmReceiver();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        setUpAd();
        mMessageTextView = (TextView) findViewById(R.id.message);



        String msg = RRRPreferenceManager.getMessage(this);

        mMessageTextView.setText(msg);
        boolean useRecording = RRRPreferenceManager.isUsingRecording(this);



        if (useRecording) {
            String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FILE_NAME;
            startPlaying(fileName);
        } else {
            String alarm = RRRPreferenceManager.getRingtone(this);
            startPlaying(alarm);
        }

        Integer red = getResources().getColor(R.color.red);
        Integer blue = getResources().getColor(R.color.blue);
        Integer orange = getResources().getColor(R.color.orange);
        Integer yellow = getResources().getColor(R.color.yellow);
        Integer purple = getResources().getColor(R.color.purple);
        Integer cool = getResources().getColor(R.color.cool);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(red);
        colors.add(blue);
        colors.add(orange);
        colors.add(yellow);
        colors.add(purple);
        colors.add(cool);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colors.toArray());
        colorAnimation.setDuration(300L);
        colorAnimation.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimation.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mMessageTextView.setTextColor((Integer) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();

    }

    @Override
    protected void onDestroy() {
        stopPlaying();
        mAlarmReceiver.setAlarmNextCycle(this);

        super.onDestroy();
    }


    private void startPlaying(String fileName) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.setLooping(true);
            mPlayer.start();
        } catch (IOException e) {
            Log.e(Util.LOG, e.getMessage());
        }
    }

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    // next cycal
    public void setNextCycle(View v) {
        Toast.makeText(this, "Alarm will  play again next cycle. ", Toast.LENGTH_LONG).show();
        mAlarmReceiver.setAlarmNextCycle(this);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Alarm will try to play again this cycle. ", Toast.LENGTH_LONG).show();
        mAlarmReceiver.setAlarm(this);
        super.onBackPressed();
    }

    public void setSameCycal(View v) {
        Toast.makeText(this, "Alarm will try to play again this cycle.", Toast.LENGTH_LONG).show();
        mAlarmReceiver.setAlarm(this);
        this.finish();
    }
}
