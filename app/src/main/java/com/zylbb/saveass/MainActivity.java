package com.zylbb.saveass;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {
    public static final int COUNTDOWN_NOTIFICATION_ID = 1;
    public static final int TIME_FOR_TOILET = 3;

    NotificationManager mNotificationManager = null;
    NotificationCompat.Builder mNotifyBuilder = null;

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

    //called when start button is clicked to start a countdown timer
    public void startCountdown(View view){
        addCountdownNotification();

        SaveAssCountDownTimer saveAssCountDownTimer = new SaveAssCountDownTimer(TIME_FOR_TOILET*60*1000, 1000);
        saveAssCountDownTimer.start();

        returnHome();
    }

    private void addCountdownNotification(){
        updateCountdownNotification(TIME_FOR_TOILET);
    }

    //update the notification as counting down in minutes
    private void updateCountdownNotification(int timeLeft){
        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(timeLeft + " " + getString(R.string.notification_content))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(getString(R.string.notification_ticker))
                .setOngoing(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(COUNTDOWN_NOTIFICATION_ID, mNotifyBuilder.build());
    }

    private void returnHome(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    class SaveAssCountDownTimer extends CountDownTimer {
        int lastTimeLeftInMinute = TIME_FOR_TOILET;

        public SaveAssCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onTick(long millisUntilFinished) {
            int timeLeftInMinute = (int)(millisUntilFinished/1000/60);
            if(timeLeftInMinute!=lastTimeLeftInMinute) {
                updateCountdownNotification(timeLeftInMinute);
                lastTimeLeftInMinute = timeLeftInMinute;
            }
        }
    }
}
