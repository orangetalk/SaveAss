package com.zylbb.saveass;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


public class MeteorActivity extends Activity implements ValueAnimator.AnimatorUpdateListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMeteor();
    }

    private void startMeteor() {
        ImageView imageView = (ImageView) findViewById(R.id.starImageView);
        ObjectAnimator anim = ObjectAnimator.ofInt(imageView, "top", 0, 1000);
        anim.setDuration(10000);
        anim.addUpdateListener(this);
        anim.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator){
        ImageView imageView = (ImageView) findViewById(R.id.starImageView);
        Log.d("ImageView:Height:", Integer.valueOf(imageView.getTop()).toString());
    }
}
