package com.zhhtao.viewtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zhhtao.testad.R;
import com.zhhtao.utils.LogUtil;

/**
 * Created by zhangHaiTao on 2016/5/8.
 */
public class DeletListView extends ListView {

    GestureDetector mGestureDetector;
    boolean isShowDeleteBtn;
    Context mContext;
    int selectedItemIndex;
    int xDown,yDown;

    ViewGroup selectedItemView;
    View deleteButton;

    public DeletListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mGestureDetector = new GestureDetector(context, new MyGestureDetector());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                mGestureDetector.onTouchEvent(event);

                return false;
            }
        });
    }

    OnDeleteListener mOnDeleteListener;
    public interface OnDeleteListener {
        void onDelete(int position);
    }
    public void setOnDeleteListener(OnDeleteListener l) {
        mOnDeleteListener = l;
    }

    float startX=0, endX = 0;
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LogUtil.i("distanceX:"+distanceX);
            LogUtil.i("e1X:"+e1.getX()+" e2 x:"+e2.getX());
            endX = e2.getX();

            if (e1.getX()-e2.getX() > 100) {//向右滑动50px
                if (isShowDeleteBtn == false) {
                    selectedItemView = (ViewGroup)
                            getChildAt(selectedItemIndex -getFirstVisiblePosition());

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

                    deleteButton = View.inflate(mContext, R.layout.delete_button, null);

                    deleteButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = selectedItemIndex;
                            mOnDeleteListener.onDelete(position);
                            selectedItemView.removeView(deleteButton);
                            deleteButton = null;
                            isShowDeleteBtn = false;
                        }
                    });

                    selectedItemView.addView(deleteButton, layoutParams);

                    isShowDeleteBtn = true;
                }
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            LogUtil.i("velocityX:"+velocityX+" velocityY:"+velocityY);
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                //之前的按钮添加逻辑在这里，对滑动事件响应效果不理想
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {

            if (isShowDeleteBtn) {
                selectedItemView.removeView(deleteButton);
                deleteButton = null;
                isShowDeleteBtn = false;
            }

            xDown = (int) e.getX();
            yDown = (int) e.getY();
            selectedItemIndex = pointToPosition(xDown, yDown);
            LogUtil.w("child counts " + getChildCount());
            LogUtil.w("first child  " + getFirstVisiblePosition());
            return false;
        }


    }


}
