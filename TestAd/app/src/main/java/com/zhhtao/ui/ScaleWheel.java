package com.zhhtao.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

/**
 * Created by zhangHaiTao on 2016/5/22.
 */
public class ScaleWheel extends View {
    int longLineHeight = 100;
    int shortLineHeight = 50;
    float divideSpace = 30;
    int viewWidth;
    int viewHeight;
    int selectedCenterX;
    int leftBound;
    int rightBound;
    int startValue = 0;
    int endValue = 2000;


    Paint normalLinePaint;
    Paint selectedLinePaint;
    Paint valueTextPaint;
    Paint selectedValueTextPaint;
    int textHeight;
    int textTopMargin;
    Context mContext;

    float density;
    float touchSlop;
    Scroller mScroller;
    GestureDetector mGestureDetector;
    private boolean isFling = false;

    @TargetApi(Build.VERSION_CODES.M)
    public ScaleWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;


        normalLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        normalLinePaint.setStrokeWidth(dip2px(context, 1.0f));
        normalLinePaint.setColor(Color.GRAY);

        selectedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedLinePaint.setStrokeWidth(dip2px(context, 2.0f));
        selectedLinePaint.setColor(Color.BLUE);

        valueTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        valueTextPaint.setStrokeWidth(dip2px(context, 1.0f));
        valueTextPaint.setColor(Color.DKGRAY);
        valueTextPaint.setTextAlign(Paint.Align.CENTER);
        valueTextPaint.setTextSize(sp2px(mContext, 17));


        float b = valueTextPaint.getFontMetrics().bottom;
        float t = valueTextPaint.getFontMetrics().top;
        Rect rect = new Rect();
        valueTextPaint.getTextBounds("1", 0, 1, rect);
        textHeight = rect.height();
        textTopMargin = dip2px(mContext, 3);


        mScroller = new Scroller(mContext);
        mGestureDetector = new GestureDetector(mContext, new MyOnGestureListener());

        selectedValueTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedValueTextPaint.setStrokeWidth(dip2px(context, 1.0f));
        selectedValueTextPaint.setColor(Color.BLUE);
        selectedValueTextPaint.setTextAlign(Paint.Align.CENTER);
        selectedValueTextPaint.setTextSize(sp2px(mContext, 22));

        touchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        density = context.getResources().getDisplayMetrics().density;
        divideSpace = density * 10; //10dip


    }

    public void scrollToValue(int value) {
        int offset = (int) (value * divideSpace);
        scrollTo(offset, 0);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#ffffee"));

        drawSelectedValue(canvas);

        drawNormalLineAndText(canvas);
        drawSelectedLine(canvas);
    }

    private void drawSelectedValue(Canvas canvas) {
        float drawtTextX = (getScrollX()+viewWidth/2);
        int value = (int) (drawtTextX / divideSpace);
        canvas.drawText(value+"",
                drawtTextX, viewHeight/2-sp2px(mContext,10),
                selectedValueTextPaint);
    }

    private void drawSelectedLine(Canvas canvas) {
        int drawSelectedX = getScrollX() + selectedCenterX;
        canvas.drawLine(drawSelectedX, viewHeight / 2,
                drawSelectedX, viewHeight / 2 + longLineHeight, selectedLinePaint);
    }

    private void drawNormalLineAndText(Canvas canvas) {
        int drawX = getScrollX();


        for (int i = startValue; drawX < getScrollX() + viewWidth; i++) {
            drawX = (int) (i * divideSpace);

            if (i % 5 == 0) {
                canvas.drawText(i + "",
                        drawX, viewHeight / 2 + longLineHeight + textHeight + textTopMargin,
                        valueTextPaint);
                canvas.drawLine(drawX, viewHeight / 2,
                        drawX, viewHeight / 2 + longLineHeight, normalLinePaint);
            } else {
                canvas.drawLine(drawX, viewHeight / 2, drawX, viewHeight / 2 + shortLineHeight,
                        normalLinePaint);
            }
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        selectedCenterX = (int)((int) (viewWidth / divideSpace / 2) * divideSpace);

        leftBound = (int) (startValue * divideSpace) - selectedCenterX;
        rightBound = (int) (endValue * divideSpace) - selectedCenterX;
    }

    float curX = 0;
    float lastX = 0;
    float difX = 0;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                curX = event.getRawX();
                lastX = curX;

                mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                //这里滚动的效果比直接放在gesture的onscroll里效果好
                curX = event.getRawX();
                difX = lastX - curX;
                scrollBy((int) difX, 0);
                lastX = curX;

                break;

            case MotionEvent.ACTION_UP:
                int curScrollX = getScrollX();
                if (curScrollX <= leftBound) {
                    mScroller.startScroll(curScrollX, 0, 0 - curScrollX, 0);
                    invalidate();
//                    scrollTo(0,0);
                } else if (curScrollX >= rightBound) {
//                    scrollTo(rightBound, 0);
                    mScroller.startScroll(curScrollX, 0, rightBound - curScrollX, 0);
                    invalidate();
                }

                adjustToValue(curScrollX);

                break;
        }
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private void adjustToValue(int curScrollX) {
        int modValue = (curScrollX % (int) divideSpace);
        int leftIndex = (curScrollX / (int) divideSpace);
        if (modValue != 0) {
            if (modValue <= divideSpace / 2) {
                scrollTo((int) (leftIndex*divideSpace),0);
            } else {
                scrollTo((int) ((leftIndex+1)*divideSpace),0);
            }
        }
    }

    class MyOnGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            scrollBy((int) distanceX, 0);
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mScroller.fling(getScrollX(), 0, (int) -velocityX, 0,
                    leftBound, rightBound, 0, 0);
            isFling = true;

            invalidate();
            return false;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        } else if (isFling){
            adjustToValue(getScrollX());
            isFling = false;
        }
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
