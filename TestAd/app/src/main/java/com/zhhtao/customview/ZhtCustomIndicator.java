package com.zhhtao.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.zhhtao.testad.R;
import com.zhhtao.utils.LogUtil;

/**
 * Created by zhangHaiTao on 2016/5/10.
 *
 * 对应的自定义attrs属性
 <resources>
 <declare-styleable name="ZhtCustomIndicator" >
 <attr name="unselect_text_color" format="color"/>
 <attr name="select_text_color" format="color"/>
 <attr name="text_size" format="dimension"/>
 <attr name="indicator_width" format="dimension"/>
 <attr name="indicator_height" format="dimension"/>
 <attr name="indicator_color" format="color"/>
 <attr name="first_select_index" format="integer"/>
 </declare-styleable>
 </resources>
 */
public class ZhtCustomIndicator extends View {

    /**
     * 控制字体
     */
    private Paint mTextPaint;

    /**
     * 控制指示条
     */
    private Paint mIndicatorPaint;

    /**
     * 整个ZhtCustomIndicator的宽高
     */
    private int viewHeight, viewWidth;

    /**
     * 根据标题个数N，将屏幕宽度平均分成N份，每份的宽度
     * cellWidth = viewWidth / titles.length
     */
    private float cellWidth;

    /**
     * indicator中文字所分得区域的高度，viewHeight=textZoneHeight+indicatorHeight
     */
    private float textZoneHeight;

    /**
     * 用来获取标题文字的宽高属性
     */
    private Rect mTextBound;

    /**
     * 设置要显示的标题
     */
    private String[] titles = {"语文", "数学", "英语"};

    /**
     * 标题文字的属性
     * 文字宽高
     */
    private int textWidth;
    private int textHeight;

    /**
     * 标题文字的属性
     * 文字的颜色 大小
     */
    private int unSelectedTextColor = Color.BLACK;
    private int selectedTextColor = Color.RED;
    private float textSize = 14;

    /**
     * 当前选择的标题下标
     */
    private int selectedIndex = -1;

    /**
     * 下发指示色条的属性
     * 宽高
     * 低部坐标
     * 顶部坐标
     * X轴中心点坐标，根据宽度求出左右边界坐标
     * 颜色
     */
    private float indicatorWidth;
    private float indicatorHeight;
    private float indicatorBottom;
    private float indicatorTop;
    private float indicatorCenterX;
    private int indicatorColor;

    /**
     * 设定和该Indicator绑定的ViewPager
     * 注意viewPager的页面个数要和Indicator的标题个数一致，否则绑定无效
     */
    private ViewPager mViewPager;


    /**
     * 控制indicator下方指示色条的动态滚动
     * 随着ViewPager滚动
     */
    private volatile float mPositionOffset;
    private int onPageScrolledPosition;

    private GestureDetector mGestureDetector;

    public ZhtCustomIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.
                obtainStyledAttributes(attrs, R.styleable.ZhtCustomIndicator);
        unSelectedTextColor = typedArray
                .getColor(R.styleable.ZhtCustomIndicator_unselect_text_color, Color.BLACK);
        selectedTextColor = typedArray
                .getColor(R.styleable.ZhtCustomIndicator_select_text_color, Color.RED);

        textSize = typedArray
                .getDimension(R.styleable.ZhtCustomIndicator_text_size, sp2px(context, 14));

        indicatorWidth = typedArray
                .getDimension(R.styleable.ZhtCustomIndicator_indicator_width, 0);
        indicatorHeight = typedArray
                .getDimension(R.styleable.ZhtCustomIndicator_indicator_height, dip2px(context, 5));
        LogUtil.w("indicatorW "+indicatorWidth);
        indicatorColor = typedArray
                .getColor(R.styleable.ZhtCustomIndicator_indicator_color, Color.RED);

        selectedIndex = typedArray.getInt(R.styleable.ZhtCustomIndicator_first_select_index, 0);
        typedArray.recycle();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(unSelectedTextColor);
        mTextPaint.setTextSize(textSize);

        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setColor(indicatorColor);
        
        mTextBound = new Rect();
        mGestureDetector = new GestureDetector(context, new MyGestureListener());
    }

    /**
     * 在这里获取一些关键的位置宽高属性
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = getWidth();
        viewHeight = getHeight();

        cellWidth = viewWidth / titles.length;
        textZoneHeight = viewHeight - indicatorHeight;
        indicatorBottom = viewHeight;
        indicatorTop = textZoneHeight;

        //如果没有指定指示色条的宽度，则为cellWidth
        if ((int)(indicatorWidth) == 0 || indicatorWidth > cellWidth) {
            indicatorWidth = cellWidth;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < titles.length; i++) {
            mTextPaint.getTextBounds(titles[i], 0, titles[i].length(), mTextBound);
            textWidth = mTextBound.width();
            textHeight = mTextBound.height();

            //绘制选中的文字和其对应的指示色条
            if (i == selectedIndex) {
                mTextPaint.setColor(selectedTextColor);
                canvas.drawText(titles[i], cellWidth / 2 - textWidth / 2 + i * cellWidth,
                        textZoneHeight / 2 + textHeight / 2, mTextPaint);
                mTextPaint.setColor(unSelectedTextColor);

                //注意 这里onPageSelected的position 和onPageScrolled的position是不同步的。
                //指示条的动态位置应参照onPageScrolled的position和positionOffset
                indicatorCenterX = cellWidth / 2 + onPageScrolledPosition * cellWidth
                        + cellWidth * mPositionOffset;
                drawIndicator(canvas, indicatorCenterX);

                //切换ViewPager
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(selectedIndex);
                }
                continue;
            }

            //绘制未选中的文字
            canvas.drawText(titles[i], cellWidth / 2 - textWidth / 2 + i * cellWidth,
                    textZoneHeight / 2 + textHeight / 2, mTextPaint);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            //计算当前选中的标题下标
//            selectedIndex = (int) (event.getX() / cellWidth);
//            invalidate();
//        }
        boolean res = mGestureDetector.onTouchEvent(event);
        return res;
    }

    /**
     * 绘制下方的指示色条
     * @param canvas
     * @param centerX
     */
    private void drawIndicator(Canvas canvas, float centerX) {
        canvas.drawRect(centerX - indicatorWidth / 2, indicatorTop,
                centerX + indicatorWidth / 2, indicatorBottom, mIndicatorPaint);
    }


    /**
     * 设置要显示的标题
     * @param titles
     */
    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    /**
     * 绑定对应的ViewPager的视图个数应该和标题数目一致
     * @param viewPager
     */
    public void bindViewPager(ViewPager viewPager) {

        if (viewPager.getAdapter().getCount() != titles.length) {
            Log.e(getClass().getSimpleName(), "绑定的ViewPager视图个数和标题数目不一致");
            return;
        }

        mViewPager = viewPager;

        //这里要设定ViewPager的初始index,否则初始进入可能出现色条滚动现象
        mViewPager.setCurrentItem(selectedIndex);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPositionOffset = positionOffset;
                onPageScrolledPosition = position;
                invalidate();
            }

            //注意 这里onPageSelected的position 和onPageScrolled的position是不同步的。
            //指示条的动态位置应参照onPageScrolled的position和positionOffset
            @Override
            public void onPageSelected(int position) {
                selectedIndex = position;
                invalidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            //改自定义view是不可clickable的，所以需要down事件返回true，才能响应后续事件
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //计算当前选中的标题下标
            selectedIndex = (int) (e.getX() / cellWidth);
            invalidate();
            return super.onSingleTapUp(e);
        }
    }
}
