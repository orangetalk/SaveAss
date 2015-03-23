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
enum AttackMode {VIBRATE, CRACK_SCREEN, LOCK_SCREEN}

public class SaveAssCountDownTimer extends CountDownTimer {
    int lastTimeLeftInMinute = SaveAssConstants.TIME_FOR_TOILET;
    int mTimeToAttack = 7;
    Activity mContextActivity = null;
    AttackMode mAttackMode = AttackMode.VIBRATE;

    public SaveAssCountDownTimer(Activity mContextActivity, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mContextActivity = mContextActivity;
    }

    @Override
    public void onFinish() {
        startTimeUpActivity();

        //update the notification with pending intent=TimeUpActivity. If the user click back/home/task button to hide the TimeUpActivity, he can resume it from notification
        Intent resultIntent = new Intent(mContextActivity, TimeUpActivity.class);
        resultIntent.putExtra(SaveAssConstants.EXTRA_IS_FROM_NOTIFICATION, true);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContextActivity, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        updateCountdownNotification(0, resultPendingIntent);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int timeLeftInMinute = (int)(millisUntilFinished/1000/60);
        if(timeLeftInMinute!=lastTimeLeftInMinute) {
            Intent resultIntent = new Intent(mContextActivity, MainActivity.class);
            resultIntent.putExtra(SaveAssConstants.EXTRA_IS_FROM_NOTIFICATION, true);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(mContextActivity, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            updateCountdownNotification(timeLeftInMinute, resultPendingIntent);
            lastTimeLeftInMinute = timeLeftInMinute;
        }

        //if the timer is started by TimeUpActivity, try to attack the loser
        int timeLeftInSeconds = (int)(millisUntilFinished/1000);
        if(mContextActivity instanceof TimeUpActivity){
            if(timeLeftInSeconds%mTimeToAttack==0){
                attackLoser();
                mTimeToAttack = new Random().nextInt(20) + 1;  //in case mTimeToAttack=0, plus 1 here
            }
        }
    }

    private void attackLoser(){
        switch (mAttackMode){
            case VIBRATE:
                attackLoserByVibrate();
                mAttackMode = AttackMode.CRACK_SCREEN;
                break;
            case CRACK_SCREEN:
                attackLoserByCrackScreen();
                mAttackMode = AttackMode.LOCK_SCREEN;
                break;
            case LOCK_SCREEN:
                attackLoserByLockScreen();
                mAttackMode = AttackMode.VIBRATE;
                break;
        }
    }

    private void attackLoserByVibrate(){
        Vibrator vibrator = (Vibrator) mContextActivity.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(SaveAssConstants.VIBRATE_DURATION);
    }

    private void attackLoserByCrackScreen(){
        Intent intent = new Intent(mContextActivity, CrackScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContextActivity.startActivity(intent);
    }

    private void attackLoserByLockScreen(){

    }

    public void startCountDown(){
        Intent resultIntent = new Intent(mContextActivity, MainActivity.class);
        resultIntent.putExtra(SaveAssConstants.EXTRA_IS_FROM_NOTIFICATION, true);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContextActivity, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        updateCountdownNotification(SaveAssConstants.TIME_FOR_TOILET, resultPendingIntent);
        start();
    }

    //update the notification as counting down in minutes
    private void updateCountdownNotification(int timeLeft, PendingIntent resultPendingIntent){
        NotificationManager notificationManager;
        NotificationCompat.Builder notifyBuilder;

        notificationManager = (NotificationManager) mContextActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyBuilder = new NotificationCompat.Builder(mContextActivity)
                .setContentTitle(mContextActivity.getString(R.string.app_name))
                .setContentText(timeLeft + " " + mContextActivity.getString(R.string.notification_content))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(mContextActivity.getString(R.string.notification_ticker))
                .setOngoing(true);
        notifyBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(SaveAssConstants.COUNTDOWN_NOTIFICATION_ID, notifyBuilder.build());
    }

    private void startTimeUpActivity(){
        Intent intent = new Intent(mContextActivity, TimeUpActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContextActivity.startActivity(intent);
    }
}
