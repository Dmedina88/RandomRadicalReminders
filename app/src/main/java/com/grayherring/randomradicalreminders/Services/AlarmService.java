package com.grayherring.randomradicalreminders.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.grayherring.randomradicalreminders.BroadcastReceiver.AlarmReceiver;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AlarmService extends IntentService {

    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";
    private IntentFilter matcher;

    public AlarmService() {
        super("AlarmService");
        matcher = new IntentFilter();
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (matcher.matchAction(action)) {
                execute(action);
            }
        }
    }

    private void execute(String action) {
        //  AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);

        this.sendBroadcast(i);
        Log.d("test", "execute alarm servis ) ");
        // am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 10000, pi);
        Log.d("test", "execute ");
        if (CREATE.equals(action)) {

            Log.d("test", "execute CREATE.equals(action) ");
        } else if (CANCEL.equals(action)) {
            //   am.cancel(pi);

            Log.d("test", "execute CANCEL.equals(action)) ");
        }
    }

}
