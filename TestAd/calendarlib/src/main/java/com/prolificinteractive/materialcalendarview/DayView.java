package com.prolificinteractive.materialcalendarview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckedTextView;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView.ShowOtherDates;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;

import java.util.ArrayList;
import java.util.List;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.showDecoratedDisabled;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.showOtherMonths;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.showOutOfRange;

/**
 * Display one day of a {@linkplain MaterialCalendarView}
 */
@SuppressLint("ViewConstructor")
public class DayView extends CheckedTextView {

    private CalendarDay date;
    private int selectionColor = Color.GRAY;

    private final int fadeTime;
    private Drawable customBackground = null;
    private Drawable selectionDrawable;
    private DayFormatter formatter = DayFormatter.DEFAULT;

    private boolean isInRange = true;
    private boolean isInMonth = true;
    private boolean isDecoratedDisabled = false;
    @ShowOtherDates
    private int showOtherDates = MaterialCalendarView.SHOW_DEFAULTS;

    public DayView(Context context, CalendarDay day) {
        super(context);
        mContext = context;
        fadeTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        setSelectionColor(this.selectionColor);

        setGravity(Gravity.CENTER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setTextAlignment(TEXT_ALIGNMENT_CENTER);
        }

        setDay(day);
    }

    public void setDay(CalendarDay date) {
        this.date = date;
        setText(getLabel());
    }

    /**
     * Set the new label formatter and reformat the current label. This preserves current spans.
     *
     * @param formatter new label formatter
     */
    public void setDayFormatter(DayFormatter formatter) {
        this.formatter = formatter == null ? DayFormatter.DEFAULT : formatter;
        CharSequence currentLabel = getText();
        Object[] spans = null;
        if (currentLabel instanceof Spanned) {
            spans = ((Spanned) currentLabel).getSpans(0, currentLabel.length(), Object.class);
        }
        SpannableString newLabel = new SpannableString(getLabel());
        if (spans != null) {
            for (Object span : spans) {
                newLabel.setSpan(span, 0, newLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        setText(newLabel);
    }

    @NonNull
    public String getLabel() {
        return formatter.format(date);
    }

    public void setSelectionColor(int color) {
        this.selectionColor = color;
        regenerateBackground();
    }

    /**
     * @param drawable custom selection drawable
     */
    public void setSelectionDrawable(Drawable drawable) {
        if (drawable == null) {
            this.selectionDrawable = null;
        } else {
            this.selectionDrawable = drawable.getConstantState().newDrawable(getResources());
        }
        regenerateBackground();
    }

    /**
     * @param drawable background to draw behind everything else
     */
    public void setCustomBackground(Drawable drawable) {
        if (drawable == null) {
            this.customBackground = null;
        } else {
            this.customBackground = drawable.getConstantState().newDrawable(getResources());
        }
        invalidate();
    }

    public CalendarDay getDate() {
        return date;
    }

    private void setEnabled() {
        boolean enabled = isInMonth && isInRange && !isDecoratedDisabled;
        super.setEnabled(enabled);

        boolean showOtherMonths = showOtherMonths(showOtherDates);
        boolean showOutOfRange = showOutOfRange(showOtherDates) || showOtherMonths;
        boolean showDecoratedDisabled = showDecoratedDisabled(showOtherDates);

        boolean shouldBeVisible = enabled;

        if (!isInMonth && showOtherMonths) {
            shouldBeVisible = true;
        }

        if (!isInRange && showOutOfRange) {
            shouldBeVisible |= isInMonth;
        }

        if (isDecoratedDisabled && showDecoratedDisabled) {
            shouldBeVisible |= isInMonth && isInRange;
        }

        setVisibility(shouldBeVisible ? View.VISIBLE : View.INVISIBLE);
    }

    protected void setupSelection(@ShowOtherDates int showOtherDates, boolean inRange, boolean inMonth) {
        this.showOtherDates = showOtherDates;
        this.isInMonth = inMonth;
        this.isInRange = inRange;
        setEnabled();
    }



    private final Rect tempRect = new Rect();

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (customBackground != null) {
            canvas.getClipBounds(tempRect);
            customBackground.setBounds(tempRect);
            customBackground.setState(getDrawableState());
            customBackground.draw(canvas);
        }

        if (drawOrderView(canvas)) return;
        super.onDraw(canvas);
    }


    /**
     * Created by zhangHaiTao on 2016/5/17.
     */
    private boolean drawOrderView(@NonNull Canvas canvas) {

//        TextPaint defaultTextPaint = getPaint();
//        defaultTextPaint.setTextSize(sp2px(mContext, 17));

        if (markedDayList.contains(date)) {
            leftRightPadding = dip2px(mContext,4);
            TopBottomPadding = dip2px(mContext,3);
            rectRadius = dip2px(mContext,3);

            Paint boundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            boundPaint.setStyle(Paint.Style.STROKE);
            boundPaint.setColor(Color.parseColor("#553d82e5"));
            boundPaint.setStrokeWidth(dip2px(mContext, 2));

            cellWidth = getMeasuredWidth();
            cellHeight = getMeasuredHeight();

            RectF rectF = new RectF(0+leftRightPadding,
                    0+TopBottomPadding,
                    cellWidth-leftRightPadding,
                    cellHeight - TopBottomPadding);

            canvas.drawRoundRect(rectF, rectRadius, rectRadius, boundPaint);
            boundPaint.setStyle(Paint.Style.FILL);
            boundPaint.setColor(Color.parseColor("#103d82e5"));
            canvas.drawRect(rectF, boundPaint);



            Rect textBound = new Rect();
            Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setTextSize(sp2px(mContext, 17));
            textPaint.setColor(Color.parseColor("#3d82e5"));

            String day = date.getDay()+"";

            textPaint.getTextBounds(day, 0, day.length(), textBound);
            textWidth = textBound.width();
            textHeight = textBound.height();
            canvas.drawText(day + "", cellWidth / 2 - textWidth / 2,
                    cellHeight/2-dip2px(mContext,2), textPaint);

            textPaint.setTextSize(sp2px(mContext, 11));
            textPaint.setColor(Color.parseColor("#883d82e5"));

            String desc = "可预约";
            textPaint.getTextBounds(desc, 0, desc.length(), textBound);
            textWidth = textBound.width();
            textHeight = textBound.height();
            canvas.drawText(desc, cellWidth / 2 - textWidth / 2,
                    cellHeight/2+textHeight+dip2px(mContext,3), textPaint);
            return true;
        }
        return false;
    }

    static public List<CalendarDay> markedDayList = new ArrayList<>();
    private int leftRightPadding = 5;
    private int TopBottomPadding = 5;
    private int rectRadius = 5;
    private float cellWidth = 5;
    private float cellHeight = 5;
    private float textHeight = 5;
    private float textWidth = 5;

    Context mContext;
    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void regenerateBackground() {
        if (selectionDrawable != null) {
            setBackgroundDrawable(selectionDrawable);
        } else {
            setBackgroundDrawable(generateBackground(selectionColor, fadeTime));
        }
    }

    private static Drawable generateBackground(int color, int fadeTime) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.setExitFadeDuration(fadeTime);
        drawable.addState(new int[]{android.R.attr.state_checked}, generateCircleDrawable(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.addState(new int[]{android.R.attr.state_pressed}, generateRippleDrawable(color));
        } else {
            drawable.addState(new int[]{android.R.attr.state_pressed}, generateCircleDrawable(color));
        }

        drawable.addState(new int[]{}, generateCircleDrawable(Color.TRANSPARENT));

        return drawable;
    }

    private static Drawable generateCircleDrawable(final int color) {
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.setShaderFactory(new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                return new LinearGradient(0, 0, 0, 0, color, color, Shader.TileMode.REPEAT);
            }
        });
        return drawable;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable generateRippleDrawable(final int color) {
        ColorStateList list = ColorStateList.valueOf(color);
        Drawable mask = generateCircleDrawable(Color.WHITE);
        return new RippleDrawable(list, null, mask);
    }

    /**
     * @param facade apply the facade to us
     */
    void applyFacade(DayViewFacade facade) {
        this.isDecoratedDisabled = facade.areDaysDisabled();
        setEnabled();

        setCustomBackground(facade.getBackgroundDrawable());
        setSelectionDrawable(facade.getSelectionDrawable());

        // Facade has spans
        List<DayViewFacade.Span> spans = facade.getSpans();
        if (!spans.isEmpty()) {
            String label = getLabel();
            SpannableString formattedLabel = new SpannableString(getLabel());
            for (DayViewFacade.Span span : spans) {
                formattedLabel.setSpan(span.span, 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setText(formattedLabel);
        }
        // Reset in case it was customized previously
        else {
            setText(getLabel());
        }
    }
}
