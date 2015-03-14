package com.zylbb.saveass;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

/**
 * Created by Administrator on 2015/3/12.
 * SaveAssCountDownTimer: a 3 minutes countdown timer which will be triggered when start toileting
 */
class SaveAssCountDownTimer extends CountDownTimer {
    int lastTimeLeftInMinute = SaveAssConstants.TIME_FOR_TOILET;
    int timeToAttack = 7;
    Activity contextActivity = null;

    public SaveAssCountDownTimer(Activity contextActivity, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.contextActivity = contextActivity;
    }

    @Override
    public void onFinish() {
        startTimeUpActivity();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int timeLeftInMinute = (int)(millisUntilFinished/1000/60);
        if(timeLeftInMinute!=lastTimeLeftInMinute) {
            updateCountdownNotification(timeLeftInMinute);
            lastTimeLeftInMinute = timeLeftInMinute;
        }

        int timeLeftInSeconds = (int)(millisUntilFinished/1000);
        if(contextActivity instanceof TimeUpActivity){
            if(timeLeftInSeconds%timeToAttack==0){
                Vibrator vibrator = (Vibrator) contextActivity.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(SaveAssConstants.VIBRATE_DURATION);
                timeToAttack = new Random().nextInt(20);
            }
        }
    }

    public void startSaveAss(){
        updateCountdownNotification(SaveAssConstants.TIME_FOR_TOILET);
        returnHome();
    }

    //update the notification as counting down in minutes
    private void updateCountdownNotification(int timeLeft){
        NotificationManager mNotificationManager;
        NotificationCompat.Builder mNotifyBuilder;

        mNotificationManager = (NotificationManager) contextActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyBuilder = new NotificationCompat.Builder(contextActivity)
                .setContentTitle(contextActivity.getString(R.string.app_name))
                .setContentText(timeLeft + " " + contextActivity.getString(R.string.notification_content))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(contextActivity.getString(R.string.notification_ticker))
                .setOngoing(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(contextActivity, MainActivity.class);
        resultIntent.putExtra(SaveAssConstants.EXTRA_IS_FROM_NOTIFICATION, true);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(contextActivity, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(SaveAssConstants.COUNTDOWN_NOTIFICATION_ID, mNotifyBuilder.build());
    }

    private void returnHome(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        contextActivity.startActivity(home);
    }

    private void startTimeUpActivity(){
        Intent intent = new Intent(contextActivity, TimeUpActivity.class);
        contextActivity.startActivity(intent);
    }
}
