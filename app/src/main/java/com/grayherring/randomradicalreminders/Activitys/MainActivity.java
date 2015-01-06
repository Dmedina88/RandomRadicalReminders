package com.grayherring.randomradicalreminders.Activitys;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.grayherring.randomradicalreminders.BroadcastReceiver.AlarmReceiver;
import com.grayherring.randomradicalreminders.Fragments.SettingsFragment;
import com.grayherring.randomradicalreminders.R;


public class MainActivity extends BaseActivity {
    AlarmReceiver alarm = new AlarmReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpAd();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new SettingsFragment())
                    .commit();
        }
        setTitle(getResources().getString(R.string.title_activity_main));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {

            String string = getResources().getString(R.string.help);
            new AlertDialog.Builder(this).setTitle("Help").setMessage(string).setNeutralButton("Okey",null).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
