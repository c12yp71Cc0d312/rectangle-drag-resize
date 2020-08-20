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
        blackStroke.setStrokeWidth(30);
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
                drag(event);
                resizeCorner(event);
                resizeEdge(event);
                rect = new Rect((int)left, (int)top, (int)right, (int)bottom);
                Log.d(TAG, "resizeCorner: top " + top + " - left: " + left + "- right: " + right + "- bottom:" + bottom);
                break;

            case MotionEvent.ACTION_UP:
                updateVertices();
                Log.d(TAG, "resizeCorner: top " + top + " - left: " + left + "- right: " + right + "- bottom:" + bottom);

            default:
                super.onTouchEvent(event);
        }

        return true;

    }

    public void drag(MotionEvent event) {
        if(xPosInitial > left + 30 && xPosInitial < right - 30 && yPosInitial > top + 30 && yPosInitial < bottom - 30) {
            updatePosition(event);

            left += delX;
            right += delX;
            top += delY;
            bottom += delY;

            xPosInitial = xPosFinal;
            yPosInitial = yPosFinal;

            invalidate();

        }
    }

    public void resizeCorner(MotionEvent event) {
        
        boolean cornerResized = false;

        if(xPosInitial >= (left - 30) && xPosInitial <= (left + 30) && yPosInitial >= (top - 30) && yPosInitial <= (top + 30)) {
            updatePosition(event);
            left += delX;
            top += delY;
            cornerResized = true;
        }
        else if(xPosInitial >= (left - 30) && xPosInitial <= (left + 30) && yPosInitial >= (bottom - 30) && yPosInitial <= (bottom + 30)) {
            updatePosition(event);
            left += delX;
            bottom += delY;
            cornerResized = true;
        }
        else if(xPosInitial >= (right - 30) && xPosInitial <= (right + 30) && yPosInitial >= (top - 30) && yPosInitial <= (top + 30)) {
            updatePosition(event);
            right += delX;
            top += delY;
            cornerResized = true;
        }
        else if(xPosInitial >= (right - 30) && xPosInitial <= (right + 30) && yPosInitial >= (bottom - 30) && yPosInitial <= (bottom + 30)) {
            updatePosition(event);
            right += delX;
            bottom += delY;
            cornerResized = true;
        }

        if(cornerResized) {
            xPosInitial = xPosFinal;
            yPosInitial = yPosFinal;
        }

        invalidate();

    }

    public void resizeEdge(MotionEvent event) {

        boolean edgeResized = false;

        if(xPosInitial >= (left - 30) && xPosInitial <= (left + 30) && yPosInitial > (top + 30) && yPosInitial < (bottom - 30)) {
            updatePosition(event);
            left += delX;
            edgeResized = true;
        }
        else if(xPosInitial >= (right - 30) && xPosInitial <= (right + 30) && yPosInitial > (top + 30) && yPosInitial < (bottom - 30)) {
            updatePosition(event);
            right += delX;
            edgeResized = true;
        }
        else if(yPosInitial >= (top - 30) && yPosInitial <= (top + 30) && xPosInitial > (left + 30) && xPosInitial < (right - 30)) {
            updatePosition(event);
            top += delY;
            edgeResized = true;
        }
        else if(yPosInitial >= (bottom - 30) && yPosInitial <= (bottom + 30) && xPosInitial > (left + 30) && xPosInitial < (right - 30)) {
            updatePosition(event);
            bottom += delY;
            edgeResized = true;
        }

        if(edgeResized) {
            xPosInitial = xPosFinal;
            yPosInitial = yPosFinal;
        }

        invalidate();

    }

    public void updatePosition(MotionEvent event) {
        xPosFinal = event.getX();
        yPosFinal = event.getY();

        delX = xPosFinal - xPosInitial;
        delY = yPosFinal - yPosInitial;
    }

    public void updateVertices() {
        float temp = 0;

        if(left >= right) {
            temp = left;
            left = right;
            right = temp;
        }
        else if(top >= bottom) {
            temp = top;
            top = bottom;
            bottom = temp;
        }
    }

}
