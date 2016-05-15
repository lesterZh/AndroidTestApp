package com.zhhtao.viewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhhtao.utils.DisplayUtil;

/**
 * Created by zhangHaiTao on 2016/5/10.
 */
public class QuickIndexBar extends View {

    String[] words ={"↑","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S"
            ,"T","U","V","W","X","Y","Z","↓"};

    int viewHeight, viewWidth;
    float cellHeight;
    float textHeight,textWidth;

    private Paint mPaint;

    public QuickIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(DisplayUtil.sp2px(context, 13));
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //获取字体的高度，宽度是不一定的，在后面的for循环中获取
        textHeight = fontMetrics.descent - fontMetrics.ascent;

    }

    //获取View 的宽高
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = getHeight();
        viewWidth = getWidth();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        cellHeight = viewHeight / words.length;

        for (int i=0; i<words.length; i++) {
            String word = words[i];
            textWidth = mPaint.measureText(word);

            if (i == curIndex) {
                mPaint.setColor(Color.RED);
                canvas.drawText(word, viewWidth / 2 - textWidth / 2,
                        cellHeight / 2 + textHeight / 2 + i * cellHeight, mPaint);
                mPaint.setColor(Color.WHITE);
                continue;
            }

            canvas.drawText(word, viewWidth/2-textWidth/2,
                    cellHeight/2 + textHeight/2 + i*cellHeight, mPaint);
        }
    }

    public interface OnSelectChangeListener {
        void onChange(int curIndex);
    }
    OnSelectChangeListener mChangeListener;
    public void setOnSelectChangeListener(OnSelectChangeListener s) {
        mChangeListener = s;
    }


    float yDown;
    int curIndex = - 1;
    int preIndex = - 1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //滑出指示条的区域判断
        if (event.getX() < 0 || event.getX() > viewWidth) {
            curIndex = -1;
            invalidate();
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                yDown = event.getY();
                curIndex = (int) (yDown / cellHeight);
                invalidate();

                //过滤重复触发
                if (curIndex != preIndex)
                    mChangeListener.onChange(curIndex);
                preIndex = curIndex;
            break;

            case MotionEvent.ACTION_MOVE :
                yDown = event.getY();
                curIndex = (int) (yDown / cellHeight);
                invalidate();

                //过滤重复触发
                if (curIndex != preIndex)
                    mChangeListener.onChange(curIndex);
                preIndex = curIndex;            break;

            case MotionEvent.ACTION_UP :
                curIndex = -1;
                invalidate();

                mChangeListener.onChange(curIndex);

                break;
        }
        return true;
    }
}
