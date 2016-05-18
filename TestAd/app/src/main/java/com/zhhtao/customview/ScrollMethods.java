package com.zhhtao.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.zhhtao.utils.LogUtil;

/**
 * Created by zhangHaiTao on 2016/5/17.
 */
public class ScrollMethods extends View {
    public ScrollMethods(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    float curX, curY, lastX, lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        method1(event);
//        method2(event);
//        method3(event);
//        method4(event);
//        method5(event);
        method6(event);
        return true;
    }

    private void method1(MotionEvent event) {
        curX = event.getX();
        curY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = curX;
                lastY = curY;
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.i("cx " + curX + " gl " + getLeft());
                int offX = (int) (curX - lastX);
                int offY = (int) (curY - lastY);
                LogUtil.i("ox " + offX + " lx " + lastX);
                layout(getLeft() + offX,
                        getTop() + offY,
                        getRight() + offX,
                        getBottom() + offY);
                break;
        }
    }

    private void method2(MotionEvent event) {
        curY = event.getRawY();
        curX = event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = curX;
                lastY = curY;
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.i("cx " + curX + " gl " + getLeft());
                int offX = (int) (curX - lastX);
                int offY = (int) (curY - lastY);
                LogUtil.i("ox " + offX + " lx " + lastX);
                layout(getLeft() + offX,
                        getTop() + offY,
                        getRight() + offX,
                        getBottom() + offY);

                //修正lastX lastY
                lastX = curX;
                lastY = curY;
                break;
        }
    }

    private void method3(MotionEvent event) {
        curY = event.getRawY();
        curX = event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = curX;
                lastY = curY;
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.i("cx " + curX + " gl " + getLeft());
                int offX = (int) (curX - lastX);
                int offY = (int) (curY - lastY);
                LogUtil.i("ox " + offX + " lx " + lastX);
                offsetLeftAndRight(offX);
                offsetTopAndBottom(offY);

                //修正lastX lastY
                lastX = curX;
                lastY = curY;
                break;
        }
    }

    private void method4(MotionEvent event) {
        curY = event.getY();
        curX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = curX;
                lastY = curY;
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.i("cx " + curX + " gl " + getLeft());
                int offX = (int) (curX - lastX);
                int offY = (int) (curY - lastY);
                LogUtil.i("ox " + offX + " lx " + lastX);

                //这个LayoutParams的类型要根据实际父布局的类型来设定
                RelativeLayout.LayoutParams layoutParams
                        = (RelativeLayout.LayoutParams) getLayoutParams();
                layoutParams.leftMargin += offX;
                layoutParams.topMargin += offY;

                //或者使用MarginLayoutParams 不用考虑父布局的类型
                ViewGroup.MarginLayoutParams marginLayoutParams
                        = (ViewGroup.MarginLayoutParams) getLayoutParams();
                marginLayoutParams.leftMargin += offX;
                marginLayoutParams.topMargin += offY;

//                setLayoutParams(layoutParams);
                setLayoutParams(marginLayoutParams);
                break;
        }
    }

    private void method5(MotionEvent event) {
        curY = event.getY();
        curX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = curX;
                lastY = curY;
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.i("cx " + curX + " gl " + getLeft());
                int offX = (int) (curX - lastX);
                int offY = (int) (curY - lastY);
                LogUtil.i("ox " + offX + " lx " + lastX);

                //对其父布局滑动
                ((View) getParent()).scrollBy(-offX, -offY);
                break;
        }
    }

    Scroller mScroller;
    private void method6(MotionEvent event) {
        curY = event.getY();
        curX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = curX;
                lastY = curY;
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.i("cx " + curX + " gl " + getLeft());
                int offX = (int) (curX - lastX);
                int offY = (int) (curY - lastY);
                LogUtil.i("ox " + offX + " lx " + lastX);

                ((View) getParent()).scrollBy(-offX, -offY);
                break;

            case MotionEvent.ACTION_UP:
                //恢复到初始位置，平滑移动
                View parent = (View) getParent();
                mScroller.startScroll(parent.getScrollX(),parent.getScrollY(),
                        -parent.getScrollX(), -parent.getScrollY(),2000);
                invalidate();
                break;
        }
    }

    //配合Method6
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            ((ViewGroup)getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
