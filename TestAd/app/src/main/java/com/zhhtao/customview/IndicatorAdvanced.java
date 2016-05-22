package com.zhhtao.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.zhhtao.testad.R;
/*

使用示例：
布局引用：
<com.zhhtao.viewtest.IndicatorAdvanced
        android:id="@+id/indicator"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        zht:unselect_text_color="#666"
        zht:select_text_color="@android:color/holo_blue_dark"
        zht:text_size="16sp"
        zht:text_top_bottom_padding = "10sp"
        zht:text_left_right_padding = "10dp"
        zht:indicator_color="@android:color/holo_blue_dark"
        zht:indicator_height="2dp"
        zht:first_select_index="1"
</com.zhhtao.viewtest.IndicatorAdvanced>
    注意这里的android:layout_height="wrap_content"必须使用wrap_content的属性，整个View的高度
    由字体的大小和text_top_bottom_padding以及indicator_height来indicator_height组成。
    标题字体之间的水平间距由text_left_right_padding属性来设置

代码配置：
indicator.setTitles(new String[]{"天使", "上帝", "亚当"});
viewPager.setAdapter(new MyViewPagerAdapter());
indicator.bindViewPager(viewPager);

依赖自定义属性：attrs.xml
 <resources>
    <declare-styleable name="ZhtCustomIndicator" >
        <attr name="unselect_text_color" format="color"/>
        <attr name="select_text_color" format="color"/>
        <attr name="text_size" format="dimension"/>
        <attr name="text_top_bottom_padding" format="dimension"/>
        <attr name="text_left_right_padding" format="dimension"/>
        <attr name="indicator_width" format="dimension"/>
        <attr name="indicator_height" format="dimension"/>
        <attr name="indicator_color" format="color"/>
        <attr name="first_select_index" format="integer"/>
    </declare-styleable>
</resources>


*/
/**
 * Created by zhangHaiTao on 2016/5/15.
 */
public class IndicatorAdvanced extends LinearLayout {


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
    private String[] titles;

    /**
     * 标题文字的属性
     * 文字宽高
     */
    private int textWidth;
    private int textHeight;

    private int textTopBottomPadding;
    private int textLeftRightPadding;

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
    private int curSelectedIndex = 0;
    private int preSelectedIndex = -1;

    /**
     * 下发指示色条的属性
     * 宽高
     * 低部坐标
     * 顶部坐标
     * X轴中心点坐标，根据宽度求出左右边界坐标
     * 颜色
     */
    private float indicatorWidth;
    private float indicatorHeight = 5;
    private float indicatorBottom;
    private float indicatorTop;
    private float indicatorCenterX;
    private int indicatorColor = Color.CYAN;

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

    private HorizontalScrollView mScrollView;
    private LinearLayout mlinearLayout;
    private Scroller mScroller;

    private Context mContext;
    private float titleScrollOffSet = 0;

    public IndicatorAdvanced(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        this.setOrientation(VERTICAL);
        this.setBackgroundColor(Color.TRANSPARENT);//如果不添加背景不调用ondraw方法

        TypedArray typedArray = context.
                obtainStyledAttributes(attrs, R.styleable.ZhtCustomIndicator);
        unSelectedTextColor = typedArray
                .getColor(R.styleable.ZhtCustomIndicator_unselect_text_color, Color.BLACK);
        selectedTextColor = typedArray
                .getColor(R.styleable.ZhtCustomIndicator_select_text_color, Color.RED);

        textSize = typedArray
                .getDimension(R.styleable.ZhtCustomIndicator_text_size, 14);
        textTopBottomPadding = (int) typedArray
                .getDimension(R.styleable.ZhtCustomIndicator_text_top_bottom_padding, 10);
        textLeftRightPadding = (int) typedArray
                .getDimension(R.styleable.ZhtCustomIndicator_text_left_right_padding, 10);


        indicatorWidth = typedArray
                .getDimension(R.styleable.ZhtCustomIndicator_indicator_width, 0);
        indicatorHeight = typedArray
                .getDimension(R.styleable.ZhtCustomIndicator_indicator_height, 10);
        indicatorColor = typedArray
                .getColor(R.styleable.ZhtCustomIndicator_indicator_color, Color.RED);

        curSelectedIndex = typedArray.getInt(R.styleable.ZhtCustomIndicator_first_select_index, 0);
        typedArray.recycle();


        mScrollView = new HorizontalScrollView(context) {
            @Override
            protected void onScrollChanged(int l, int t, int oldl, int oldt) {
                super.onScrollChanged(l, t, oldl, oldt);
                //当标题滚动时同步滚动指示条
                titleScrollOffSet = l;
                postInvalidate();
            }
        };

        mScrollView.setHorizontalScrollBarEnabled(false);
        mScrollView.setFillViewport(true);
        mScrollView.setSaveEnabled(false);
        mScrollView.setSaveFromParentEnabled(false);

        mScrollView.setLayoutParams(
                new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        mlinearLayout = new LinearLayout(context);
        mlinearLayout.setOrientation(HORIZONTAL);

        mlinearLayout.setLayoutParams(
                new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        mScrollView.addView(mlinearLayout);
        addView(mScrollView);

        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setColor(indicatorColor);
        mScroller = new Scroller(context);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();
        indicatorTop = viewHeight;
        viewHeight += indicatorHeight;
        indicatorBottom = viewHeight;

        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellWidth = mlinearLayout.getChildAt(0).getMeasuredWidth();
        if (indicatorWidth == 0 || indicatorWidth > cellWidth) {
            indicatorWidth = cellWidth;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //注意 这里onPageSelected的position 和onPageScrolled的position是不同步的。
        //指示条的动态位置应参照onPageScrolled的position和positionOffset
        //titleScrollOffSet用于控制当标题滚动时同步滚动指示条
        indicatorCenterX = cellWidth / 2 + onPageScrolledPosition * cellWidth
                + cellWidth * mPositionOffset - titleScrollOffSet;
        drawIndicator(canvas, indicatorCenterX);
    }


    public void setTitles(String[] ts) {
        titles = ts;
        addTitles();
    }

    public void setSelected(int index) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(index);
        }
    }

    public void bindViewPager(ViewPager viewPager) {

        mViewPager = viewPager;

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
                curSelectedIndex = position;
                if (position < 0 || position >= titles.length) return;
                changeSelectedTextColor();
                View child = mlinearLayout.getChildAt(position);

                int curScrollX = mScrollView.getScrollX();
                int dx = 0;
                if (child.getLeft() < curScrollX) {
                    dx = child.getLeft() - curScrollX;
                    mScroller.startScroll(curScrollX, 0, dx, 0);
//                    mScrollView.postInvalidate();//也可以通过重绘mScrollView来实现平滑滚动
                } else if (curScrollX + viewWidth < child.getRight()) {
                    dx = child.getRight() - (curScrollX + viewWidth);
                    mScroller.startScroll(curScrollX, 0, dx, 0);
                }
                invalidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //这里要设定ViewPager的初始index
        mViewPager.setCurrentItem(curSelectedIndex);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mScrollView.scrollTo(mScroller.getCurrX(), 0);
        }
    }

    private void drawIndicator(Canvas canvas, float centerX) {
        canvas.drawRect(centerX - indicatorWidth / 2, indicatorTop, centerX + indicatorWidth / 2,
                indicatorBottom, mIndicatorPaint);
    }


    private void addTitles() {
        LinearLayout.LayoutParams params =
                new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        for (int i = 0; i < titles.length; i++) {
            final TextView addText = new TextView(mContext);
            addText.setText(titles[i]);
            if (i == curSelectedIndex) {
                addText.setTextColor(selectedTextColor);
            } else {
                addText.setTextColor(unSelectedTextColor);
            }
            addText.setTextSize(px2sp(mContext, textSize));
            addText.setGravity(Gravity.CENTER);

            addText.setPadding(textLeftRightPadding, textTopBottomPadding,
                    textLeftRightPadding, textTopBottomPadding);

            final int tempPosition = i;
            addText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    UIUtils.showToast((Activity)mContext, "pos "+tempPosition);
                    mViewPager.setCurrentItem(tempPosition);
                }
            });
            mlinearLayout.addView(addText, params);
        }

    }

    private void changeSelectedTextColor() {
        TextView cur = (TextView) mlinearLayout.getChildAt(curSelectedIndex);
        if (cur != null) {
            cur.setTextColor(selectedTextColor);
        }

        if (preSelectedIndex != -1) {
            TextView pre = (TextView) mlinearLayout.getChildAt(preSelectedIndex);
            if (pre != null)
                pre.setTextColor(unSelectedTextColor);
        }
        preSelectedIndex = curSelectedIndex;

    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
