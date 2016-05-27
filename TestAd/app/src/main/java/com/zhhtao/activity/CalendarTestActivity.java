package com.zhhtao.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayView;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import com.zhhtao.base.BaseActivty;
import com.zhhtao.testad.R;
import com.zhhtao.utils.UIUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Created by zhangHaiTao on 2016/5/17.
 */
public class CalendarTestActivity extends BaseActivty {

    @Bind(R.id.calendar_view)
    MaterialCalendarView calendarView;

    List<CalendarDay> orderDayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_test);
        ButterKnife.bind(this);


        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        orderDayList.add(CalendarDay.from(mCalendar));
        mCalendar.add(Calendar.DAY_OF_MONTH, -7);
        orderDayList.add(CalendarDay.from(mCalendar));
        mCalendar.add(Calendar.DAY_OF_MONTH, 3);
        orderDayList.add(CalendarDay.from(mCalendar));
        mCalendar.add(Calendar.DAY_OF_MONTH, 6);
        orderDayList.add(CalendarDay.from(mCalendar));
        mCalendar.add(Calendar.DAY_OF_MONTH, 2);
        orderDayList.add(CalendarDay.from(mCalendar));
        mCalendar.add(Calendar.MONTH, 1);
        orderDayList.add(CalendarDay.from(mCalendar));
        mCalendar.add(Calendar.MONTH, -1);
        orderDayList.add(CalendarDay.from(mCalendar));
        mCalendar.add(Calendar.MONTH, 2);
        orderDayList.add(CalendarDay.from(mCalendar));

        DayView.markedDayList = orderDayList;

        calendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                return day.getYear() + "年" + day.getMonth() + "月";
            }
        });
        calendarView.setWeekDayLabels(new String[]{"日", "一", "二", "三", "四", "五", "六"});
        calendarView.setBackgroundColor(Color.WHITE);
        calendarView.setSelectionColor(Color.WHITE);
        calendarView.setWeekDayTextAppearance(R.style.WeekDayTextAppearance);
        calendarView.setHeaderTextAppearance(R.style.TiTleTextAppearance);
        calendarView.setDateTextAppearance(R.style.DateTextAppearance);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (orderDayList.contains(date)) {
                    UIUtils.showToast(mContext, "进入预约");
                } else {
                    UIUtils.showToast(mContext, "仅可选可预约日期");
                }

            }
        });


    }
}
