package com.zylbb.saveass;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

/**
 * Created by Administrator on 2015/3/12.
 * SaveAssCountDownTimer: a 3 minutes countdown timer which will be triggered when start toileting
 */
enum AttackMode {VIBRATE, CRACK_SCREEN, LOCK_SCREEN, SOUND}

public class SaveAssCountDownTimer extends CountDownTimer {

    int mTimeForToiletInMinute = 0;   //the initial time left for toilet in minutes, depends on to user's selection on preference
    int mLastTimeLeftInMinute = 0;    // for update notification, if current left minutes does not equal to last left minutes, then update notification
    int mTimeToAttack = 7;
    Activity mContextActivity;
    AttackMode mAttackMode = AttackMode.VIBRATE;  //the first attack would be VIBRATE
    //boolean mVibrateAttackOn;                      //vibrate attack is mandatory
    boolean mCrackScreenAttackOn;
    boolean mLockScreenAttackOn;
    boolean mSoundAttackOn;

    public SaveAssCountDownTimer(Activity contextActivity) {
//        super(Integer.valueOf(
//                PreferenceManager.
//                        getDefaultSharedPreferences(contextActivity).
//                        getString(SaveAssConstants.PREF_KEY_TIME_FOR_TOILET_LIST, null)
//                )*60*1000,
//               SaveAssConstants.COUNTDOWN_INTERVAL);

        super(60*1000, 1000);          //this line should be replaced by the above line
        mContextActivity = contextActivity;

        //load preferences
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(contextActivity);
        mTimeForToiletInMinute = Integer.valueOf(sp.getString(SaveAssConstants.PREF_KEY_TIME_FOR_TOILET_LIST, null));
        //mVibrateAttackOn = Boolean.valueOf(sp.getString(SaveAssConstants.PREF_KEY_VIBRATE_ATTACK_SWITCH, null));
        mCrackScreenAttackOn = sp.getBoolean(SaveAssConstants.PREF_KEY_CRACK_SCREEN_ATTACK_SWITCH, true);
        mLockScreenAttackOn = sp.getBoolean(SaveAssConstants.PREF_KEY_LOCK_SCREEN_ATTACK_SWITCH, true);
        mSoundAttackOn = sp.getBoolean(SaveAssConstants.PREF_KEY_SOUND_ATTACK_SWITCH, true);

        mLastTimeLeftInMinute = mTimeForToiletInMinute;
    }

    public SaveAssCountDownTimer(Activity contextActivity, int timeForToiletInMinute) {
        super(timeForToiletInMinute*60*1000, SaveAssConstants.COUNTDOWN_INTERVAL);
        mContextActivity = contextActivity;
        mTimeForToiletInMinute = mLastTimeLeftInMinute = timeForToiletInMinute;
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
        if(timeLeftInMinute!=mLastTimeLeftInMinute) {
            Intent resultIntent = new Intent(mContextActivity, MainActivity.class);
            resultIntent.putExtra(SaveAssConstants.EXTRA_IS_FROM_NOTIFICATION, true);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(mContextActivity, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            updateCountdownNotification(timeLeftInMinute, resultPendingIntent);
            mLastTimeLeftInMinute = timeLeftInMinute;
        }

        //if the timer is started by TimeUpActivity, try to attack the loser
        int timeLeftInSeconds = (int)(millisUntilFinished/1000);
        if(mContextActivity instanceof TimeUpActivity){
            if(timeLeftInSeconds%mTimeToAttack==0){
                attackLoser();
                mTimeToAttack = new Random().nextInt(10) + 10;  //in case mTimeToAttack=0, plus 10 here
            }
        }
    }

    private void attackLoser(){
        switch (mAttackMode){
            case CRACK_SCREEN:
                if (mCrackScreenAttackOn) {
                    attackLoserByCrackScreen();
                    mAttackMode = AttackMode.LOCK_SCREEN;
                    break;
                }
            case LOCK_SCREEN:
                if (mLockScreenAttackOn) {
                    attackLoserByLockScreen();
                    mAttackMode = AttackMode.SOUND;
                    break;
                }
            case SOUND:
                if (mSoundAttackOn) {
                    attackLoserBySound();
                    mAttackMode = AttackMode.VIBRATE;
                    break;
                }
            case VIBRATE:
                attackLoserByVibrate();
                mAttackMode = AttackMode.CRACK_SCREEN;
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
        DevicePolicyManager dpm;
        ComponentName componentName;

        dpm = (DevicePolicyManager) mContextActivity.getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(mContextActivity, LockScreenReceiver.class);

        boolean bActive = dpm.isAdminActive(componentName);
        if (bActive) {
            dpm.lockNow();
        }
        else{
            Intent intent = new Intent();
            intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            mContextActivity.startActivity(intent);
        }
    }

    private void attackLoserBySound(){

    }

    public void startCountDown(){
        Intent resultIntent = new Intent(mContextActivity, MainActivity.class);
        resultIntent.putExtra(SaveAssConstants.EXTRA_IS_FROM_NOTIFICATION, true);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContextActivity, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        updateCountdownNotification(mTimeForToiletInMinute, resultPendingIntent);
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
