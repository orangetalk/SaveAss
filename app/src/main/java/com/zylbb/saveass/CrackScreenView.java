package com.zylbb.saveass;

/**
 * Created by Administrator on 2015/3/18.
 *
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

public class CrackScreenView extends View implements Runnable{
    private Bitmap mBitmap;
    private ArrayList<Integer> mXPointList;
    private ArrayList<Integer> mYPointList;
    private int mPaintLength = 0; //How many cracked holes to paint

    public CrackScreenView(Context context) {
        super(context);
        this.mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cracked_screen);
        generateXYPoints(context);
    }

    private void generateXYPoints(Context context){
        mXPointList = new ArrayList<>();
        mYPointList = new ArrayList<>();

        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        for(int i=0;i<SaveAssConstants.NUMBER_OF_CRACKED_HOLES;i++){
            mXPointList.add(new Random().nextInt(size.x));
            mYPointList.add(new Random().nextInt(size.y));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i=0;i<mPaintLength;i++)
            canvas.drawBitmap(mBitmap, mXPointList.get(i)-mBitmap.getWidth()/2, mYPointList.get(i)-mBitmap.getHeight()/2, null);

    }

    @Override
    public void run() {
        while (mPaintLength < SaveAssConstants.NUMBER_OF_CRACKED_HOLES){
            mPaintLength++;
            try{
                Thread.sleep(1000);
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
            postInvalidate();
        }
    }
}