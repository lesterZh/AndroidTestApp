package com.zhhtao.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhangHaiTao on 2016/5/17.
 */
public class DrawClock extends View {
    Context mContext;
    public DrawClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#fffff0"));
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dip2px(mContext, 2));
        canvas.drawCircle(width/2, height/2, width/2, paint);

        for (int i=0; i<60; i++) {
            if (i % 5 == 0) {
                paint.setStrokeWidth(dip2px(mContext, 2));
                paint.setColor(Color.BLUE);
                canvas.drawLine(0, height / 2, 30, height / 2, paint);

                paint.setStrokeWidth(2);
                paint.setTextSize(sp2px(mContext, 12));
                Rect rect = new Rect();
                paint.getTextBounds("1",0,1,rect);
                float textHeight = rect.bottom-rect.top;
                canvas.drawText((i/5+1)+"",35, height/2+textHeight/2,paint);

                canvas.rotate(6, width/2, height /2);
                continue;
            } else {
                paint.setStrokeWidth(dip2px(mContext, 1));
                paint.setColor(Color.CYAN);
                canvas.drawLine(0, height / 2, 20, height / 2, paint);
                canvas.rotate(6, width/2, height /2);
                continue;
            }
        }

        canvas.rotate(18, width/2, height/2);
        canvas.save();
        canvas.restore();
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
