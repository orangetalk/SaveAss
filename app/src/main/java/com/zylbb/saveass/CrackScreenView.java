package com.zylbb.saveass;

/**
 * Created by Administrator on 2015/3/18.
 *
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

public class CrackScreenView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private ArrayList<Integer> mXPointList;
    private ArrayList<Integer> mYPointList;
    private int mLength = 3;// 绘制总数

    public CrackScreenView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        // mPaint.setAlpha(127);
        mPaint.setStrokeWidth(2.0f);
        this.setKeepScreenOn(true);
        this.setFocusable(true);
        this.setLongClickable(true);
        this.mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cracked_screen);
        generateXYPoints(context);
    }

    private void generateXYPoints(Context context){
        mXPointList = new ArrayList<Integer>();
        mYPointList = new ArrayList<Integer>();

        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        for(int i=0;i<mLength;i++){
            mXPointList.add(new Random().nextInt(size.x));
            mYPointList.add(new Random().nextInt(size.y));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mXPointList.size(); ++i) {
            canvas.drawBitmap(mBitmap, mXPointList.get(i) - mBitmap.getWidth()
                    / 2, mYPointList.get(i) - mBitmap.getHeight() / 2, null);
        }
    }
}