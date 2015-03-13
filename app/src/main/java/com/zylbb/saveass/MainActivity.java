package com.zylbb.saveass;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {
    NotificationManager mNotificationManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //called when start_done button is clicked to start or stop a countdown timer
    public void start_doneCountdown(View view){
        Button button = (Button)view;
        if(button.getText().equals(getString(R.string.button_start))) {
            button.setText(getString(R.string.button_done));
            startCountdown();
        }
        else
            doneToilet();
    }

    private void startCountdown(){
        SaveAssCountDownTimer saveAssCountDownTimer = new SaveAssCountDownTimer(this, SaveAssConstants.TIME_FOR_TOILET*60*1000, 1000);
        saveAssCountDownTimer.startSaveAss();
        saveAssCountDownTimer.start();
    }

    private void doneToilet(){
        mNotificationManager.cancel(SaveAssConstants.COUNTDOWN_NOTIFICATION_ID);
        System.exit(0);
    }
}
