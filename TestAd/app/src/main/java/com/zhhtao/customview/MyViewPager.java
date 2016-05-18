package com.zhhtao.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by zhangHaiTao on 2016/5/1.
 */
public class MyViewPager extends ViewGroup{
    private static final String TAG = "ZHT";
    Context mContext;
    float xDown,xMove,xLastMove;
    int leftBorder,rightBorder;
    int mTouchSlop;//判断为拖动的最小像素
    Scroller mScroller;
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i=0; i<getChildCount(); i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            for (int i=0; i<getChildCount(); i++) {
                View child = getChildAt(i);
                child.layout(i*getMeasuredWidth(), 0, (i+1)*getMeasuredWidth(), getMeasuredHeight());
            }

            leftBorder = getChildAt(0).getLeft();
            rightBorder = getChildAt(getChildCount()-1).getRight();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN :
                Log.w(TAG, " viewPager onInterceptTouchEvent xDown " + xDown );

                xDown = ev.getRawX();
                xLastMove = xDown;
                break;
            case MotionEvent.ACTION_MOVE :
                xMove = ev.getRawX();
                float dif = xMove - xDown;
                xLastMove = xMove;
                //判断是否滑动  是则拦截事件，进入onTouchEvent
                //也可以直接在此函数中使用 return true,拦截所有事件
                if (Math.abs(dif) > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                xDown = event.getRawX();
                xLastMove = xDown;
                Log.w(TAG, " viewPager onTouchEvent ACTION_DOWN" + xDown );
                break;
            case MotionEvent.ACTION_MOVE :
                Log.w(TAG, " viewPager onTouchEvent ACTION_MOVE" + xDown );
                xMove = event.getRawX();
                float dx = xLastMove - xMove;

                if (getScrollX()+dx < leftBorder) {
                    scrollTo(leftBorder,0);
                } else if (getScrollX()+getMeasuredWidth()+dx > rightBorder) {
                    scrollTo(rightBorder-getMeasuredWidth(),0);
                } else {
                    scrollBy((int)dx, 0);
                    xLastMove = xMove;
                }
                return false;
//                break;
            case MotionEvent.ACTION_UP :
                Log.w(TAG, " viewPager onTouchEvent ACTION_UP" + xDown );

                int index = getScrollX() / getMeasuredWidth();
                index += (getScrollX() % getMeasuredWidth() > (getMeasuredWidth()/2) ? 1 : 0);
//                scrollTo(index*getMeasuredWidth(),0);//瞬时移动
                int scrollerDx = index*getMeasuredWidth() - getScrollX();
                mScroller.startScroll(getScrollX(), 0, scrollerDx, 0);
                invalidate();
                break;
        }
        return true;
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
