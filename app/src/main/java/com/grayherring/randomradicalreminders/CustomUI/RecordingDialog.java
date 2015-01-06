package com.grayherring.randomradicalreminders.CustomUI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.grayherring.randomradicalreminders.Util.Constants;
import com.grayherring.randomradicalreminders.Util.Util;

import java.io.IOException;

/**
 * Created by David on 12/21/2014.
 */
public class RecordingDialog extends DialogPreference {


    private static String mFileName = null;
    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;
    private PlayButton mPlayButton = null;
    private MediaPlayer mPlayer = null;


    public RecordingDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FILE_NAME;

        setPositiveButtonText("Done");


    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setNegativeButton(null, null);
    }

    @Override
    protected View onCreateDialogView() {

        return (createView());
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

    }


    private void onRecord(boolean start) {
        if (start) {
            startRecording();
            mPlayButton.setEnabled(false);
        } else {
            stopRecording();
            mPlayButton.setEnabled(true);
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
            mRecordButton.setEnabled(false);
        } else {
            stopPlaying();
            mRecordButton.setEnabled(true);
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("test", "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(Util.LOG, "prepare() failed" + e.getMessage());
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public View createView() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += Constants.FILE_NAME;
        LinearLayout ll = new LinearLayout(this.getContext());
        mRecordButton = new RecordButton(this.getContext());

        ll.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1));
        mPlayButton = new PlayButton(this.getContext());
        ll.addView(mPlayButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1));


        return ll;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mRecorder != null) {
            stopRecording();
        }
        if (mPlayer != null) {
            stopPlaying();
        }
        super.onDismiss(dialog);
    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }


}



