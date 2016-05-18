package com.zhhtao.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by zhangHaiTao on 2016/5/11.
 */
public class CustomScrollView extends ViewGroup {

    GestureDetector mGestureDetector;
    Scroller mScroller;
    private int leftBound,rightBound = 0;
    private int viewGroupWidth;
    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new MyGestureListener());
        mScroller = new Scroller(context);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int counts = getChildCount();
        View child;
        int rb=0;
        for (int i=0; i<counts; i++) {
            child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            rb += child.getMeasuredWidth();
        }
        rightBound = rb;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int counts = getChildCount();
            int width = 0;
            //从左到右排列
            for (int i=0; i<counts; i++) {
                View child = getChildAt(i);
                child.layout(width,0,width + child.getMeasuredWidth(),
                        child.getMeasuredHeight());
                width += child.getMeasuredWidth();

            }

        }
        viewGroupWidth = getWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mScroller.forceFinished(true);
        }
        mGestureDetector.onTouchEvent(event);
        return true;
    }


    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollBy((int) distanceX, 0);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //最核心关键的代码在这里
            //第一个参数是getScrollX()，不是mScroller.getCurX()
            //第三个速度 一定要取反
            mScroller.fling(getScrollX(), getScrollY(), -(int) velocityX,
                    (int) velocityY, 0, rightBound-getWidth(), 0, 0);
            invalidate();
            return true;
        }
    }

    //控制滚动
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
