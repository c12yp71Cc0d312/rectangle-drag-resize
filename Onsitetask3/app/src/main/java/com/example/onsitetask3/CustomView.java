package com.example.onsitetask3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {

    private static final String TAG = "CustomView";

    private Rect rect;
    private Paint blackStroke;
    private static double canvasWidth;
    private static double canvasHeight;
    private float top, bottom, left, right;
    private float xPosInitial, yPosInitial;
    private float xPosFinal, yPosFinal;
    private float delX, delY;
    private boolean firstTime;

    public CustomView(Context context) {
        super(context);

        blackStroke = new Paint();
        blackStroke.setColor(Color.BLACK);
        blackStroke.setStyle(Paint.Style.STROKE);
        blackStroke.setStrokeWidth(8);
        blackStroke.setAntiAlias(true);

        firstTime = true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        if(firstTime) {
            left = (float)canvasWidth/4;
            top = (float)canvasHeight/4;
            right = (float)(0.75*canvasWidth);
            bottom = (float)(0.75*canvasHeight);
            rect = new Rect((int)left, (int)top, (int)right, (int)bottom);
            firstTime = false;
        }

        canvas.drawRect(rect, blackStroke);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xPosInitial = event.getX();
                yPosInitial = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if(xPosInitial > left + 30 && xPosInitial < right - 30 && yPosInitial > top + 30 && yPosInitial < bottom - 30) {
                    xPosFinal = event.getX();
                    yPosFinal = event.getY();

                    delX = xPosFinal - xPosInitial;
                    delY = yPosFinal - yPosInitial;

                    left += delX;
                    right += delX;
                    top += delY;
                    bottom += delY;

                    xPosInitial = xPosFinal;
                    yPosInitial = yPosFinal;

                    invalidate();

                }
                resize(event);
                rect = new Rect((int)left, (int)top, (int)right, (int)bottom);
                break;

            default:
                super.onTouchEvent(event);
        }

        return true;

    }

    public void resize(MotionEvent event) {
        xPosFinal = event.getX();
        yPosFinal = event.getY();

        delX = xPosFinal - xPosInitial;
        delY = yPosFinal - yPosInitial;

        if(xPosInitial >= (left - 30) && xPosInitial <= (left + 30) && yPosInitial >= (top - 30) && yPosInitial <= (top + 30)) {
            left += delX;
            top += delY;
            Log.d(TAG, "resize: topleft");
        }
        else if(xPosInitial >= (left - 30) && xPosInitial <= (left + 30) && yPosInitial >= (bottom - 30) && yPosInitial <= (bottom + 30)) {
            left += delX;
            bottom += delY;
            Log.d(TAG, "resize: bottomleft");
        }
        else if(xPosInitial >= (right - 30) && xPosInitial <= (right + 30) && yPosInitial >= (top - 30) && yPosInitial <= (top + 30)) {
            right += delX;
            top += delY;
            Log.d(TAG, "resize: topright");
        }
        else if(xPosInitial >= (right - 30) && xPosInitial <= (right + 30) && yPosInitial >= (bottom - 30) && yPosInitial <= (bottom + 30)) {
            right += delX;
            bottom += delY;
            Log.d(TAG, "resize: bottomright");
        }

        xPosInitial = xPosFinal;
        yPosInitial = yPosFinal;

        invalidate();

    }

}
