package com.zylbb.saveass;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class TimeUpActivity extends ActionBarActivity {
    NotificationManager mNotificationManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_up);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_up, menu);
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

    //called when done button is clicked to start a countdown timer
    public void doneToilet(View view){
        mNotificationManager.cancel(SaveAssConstants.COUNTDOWN_NOTIFICATION_ID);
        System.exit(0);
    }

    //called when 3 more minutes button is clicked to start a countdown timer
    public void onRestartButtonClick(View view){
        startToilet();
    }

    private void startToilet(){
        SaveAssCountDownTimer saveAssCountDownTimer = new SaveAssCountDownTimer(this, SaveAssConstants.TIME_FOR_TOILET*60*1000, 1000);
        saveAssCountDownTimer.startCountDown();

        returnHomeScreen();
    }

    private void returnHomeScreen(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }
}
