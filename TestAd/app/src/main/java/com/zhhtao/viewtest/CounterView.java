package com.zhhtao.viewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.zhhtao.utils.DisplayUtil;

/**
 * Created by zhangHaiTao on 2016/5/8.
 */
public class CounterView extends View {
    Context context;
    Paint mPaint;
    Rect mBounds;
    int mCount = 0;
    public CounterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new Rect();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount++;
                invalidate();
            }
        });
    }
    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.BLUE);
//        canvas.drawColor(Color.BLUE); //绘制背景色
        canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);

        //绘制计数值
        String text = String.valueOf(mCount);
        mPaint.setColor(Color.YELLOW);
        mPaint.setTextSize(DisplayUtil.sp2px(context, 20));
        mPaint.getTextBounds(text, 0, text.length(), mBounds);
        int textWidth = mBounds.width();
        int textHeight = mBounds.height();

        //绘制文字，设置其开始绘制的左下角坐标
        canvas.drawText(text, getWidth()/2-textWidth/2,
                getHeight()/2+textHeight/2, mPaint);
    }
}
