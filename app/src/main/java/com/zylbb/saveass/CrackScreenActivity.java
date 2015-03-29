package com.zylbb.saveass;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class CrackScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CrackScreenView crackScreenView = new CrackScreenView(this);
        setContentView(crackScreenView);

        new Thread(crackScreenView).start();
    }

    //forbid the user to quit the activity by pressing back
    @Override
    public void onBackPressed(){
        Log.d("Activity Lifecycle", "MainActivity onBackPressed");
    }
}
