package com.zhhtao.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhhtao.utils.UIUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangHaiTao on 2016/5/17.
 */
public class DrawClock extends View {
    Context mContext;
    private float minuteRotate = 0;//分针的选中角度
    private float secondRotate = 0;

    boolean isRun = false;
    Timer timer = new Timer();
    TimerTask timerTask;
    public DrawClock(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isRun) {
                    secondRotate += 6;
                    minuteRotate += 1;//模拟分针是秒针速度的 1/6
                    postInvalidate();
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRun) {
                    isRun = false;
                    UIUtils.showToast((Activity)mContext, "stop");
                } else {
                    UIUtils.showToast((Activity)mContext, "run");
                    isRun = true;
                }
            }
        });
    }

    int padding = 5;//修正画的圆最外侧不完整
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#fffff0"));
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dip2px(mContext, 2));
//        canvas.drawCircle(width / 2, height / 2, width / 2 - padding, paint);//后画圆

        for (int i=0; i<60; i++) {
            if (i % 5 == 0) {
                paint.setStrokeWidth(dip2px(mContext, 2));
                paint.setColor(Color.GRAY);
//                canvas.drawLine(0, height / 2, 30, height / 2, paint);
                canvas.drawLine(width/2, padding, width/2, padding+30, paint);

                paint.setStrokeWidth(2);
                paint.setTextSize(sp2px(mContext, 12));
                paint.setColor(Color.BLUE);
                Rect rect = new Rect();
                paint.getTextBounds("1",0,1,rect);
                float textHeight = rect.bottom-rect.top;
                float textWidth = rect.right-rect.left;
//                canvas.drawText((i/5+1)+"",35, height/2+textHeight/2,paint);
                canvas.drawText((i/5+1)+"",width/2-textWidth, padding+35+textHeight,paint);

                canvas.rotate(6, width/2, height /2);
                continue;
            } else {
                paint.setStrokeWidth(dip2px(mContext, 1));
                paint.setColor(Color.GRAY);
//                canvas.drawLine(0, height / 2, 20, height / 2, paint);
                canvas.drawLine(width/2, padding, width/2, padding+20, paint);
                canvas.rotate(6, width/2, height /2);
                continue;
            }
        }

        paint.setStrokeWidth(dip2px(mContext, 2));
        canvas.drawCircle(width / 2, height / 2, width / 2 - padding, paint);

        canvas.save();
        canvas.translate(width / 2, height / 2);


        canvas.rotate(secondRotate);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(dip2px(mContext, 1.5f));
        canvas.drawLine(0, 0, 100, 100, paint);

        //抵消秒针的旋转
        canvas.rotate(minuteRotate-secondRotate);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(dip2px(mContext, 3));
        canvas.drawLine(0, 0, 60, -80, paint);

        canvas.drawCircle(0, 0, dip2px(mContext, 3), new Paint());
    }





    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
