package com.zylbb.saveass;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if(intent.getBooleanExtra(SaveAssConstants.EXTRA_IS_FROM_NOTIFICATION, false)){
            Button button = (Button)findViewById(R.id.start_done_button);
            button.setText(getString(R.string.button_done));
            Log.d("Activity Lifecycle", "MainActivity onCreate from notification");
        }

        //Set preference default values
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        Log.d("Activity Lifecycle", "MainActivity onCreate");
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
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //called when start_done button is clicked to start or stop a countdown timer
    public void onStartDoneButtonClick(View view){
        Button button = (Button)view;
        if(button.getText().equals(getString(R.string.button_start))) {
            button.setText(getString(R.string.button_done));
            startToilet();
        }
        else
            doneToilet();
    }

    private void startToilet(){
        SaveAssCountDownTimer saveAssCountDownTimer = new SaveAssCountDownTimer(this, SaveAssConstants.TIME_FOR_TOILET*60*1000, 1000);
        saveAssCountDownTimer.startCountDown();

        finish();
    }

    private void returnHomeScreen(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    private void doneToilet(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(SaveAssConstants.COUNTDOWN_NOTIFICATION_ID);
        System.exit(0);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        Log.d("Activity Lifecycle", "MainActivity onRestart");
    }

    @Override
    public void onStart(){
        super.onStart();

        Log.d("Activity Lifecycle", "MainActivity onStart");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("Activity Lifecycle", "MainActivity onStop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("Activity Lifecycle", "MainActivity onDestroy");
    }
}
