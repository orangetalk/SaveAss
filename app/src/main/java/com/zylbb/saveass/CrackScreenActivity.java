package com.zylbb.saveass;

import android.app.Activity;
import android.os.Bundle;

public class CrackScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CrackScreenView crackScreenView = new CrackScreenView(this);
        setContentView(crackScreenView);

        new Thread(crackScreenView).start();
    }
}
