package com.zhhtao.testad;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.zhhtao.activity.AnimatorTestActivity;
import com.zhhtao.activity.CalendarTestActivity;
import com.zhhtao.activity.DrawTestActivity;
import com.zhhtao.activity.GreenDaoTestActivity;
import com.zhhtao.activity.IndicatorAdvanceActivity;
import com.zhhtao.activity.IndicatorTestActivity;
import com.zhhtao.activity.RetrofitTestActivity;
import com.zhhtao.activity.RxJavaActivity;
import com.zhhtao.activity.ScrollTestActivity;
import com.zhhtao.activity.SwipeRefreshLayoutTestActivity;
import com.zhhtao.leancloud.LeanCloudInitActivity;
import com.zhhtao.utils.ZhtUtils;

import butterknife.Bind;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    Activity mContext;

    @Bind(R.id.btn_indicator)
    Button btnIndicator;
    @Bind(R.id.btn_animator)
    Button btnAnimator;
    @Bind(R.id.btn_new_indicator)
    Button btnNewIndicator;
    @Bind(R.id.btn_scroll_test)
    Button btnScrollTest;
    @Bind(R.id.btn_draw_test)
    Button btnDrawTest;
    @Bind(R.id.btn_calendar)
    Button btnCalendar;
    @Bind(R.id.btn_net)
    Button btnNet;
    @Bind(R.id.btn_green_dao)
    Button btnGreenDao;
    @Bind(R.id.btn_rx_java)
    Button btnRxJava;
    @Bind(R.id.btn_refresh)
    Button btnRefresh;
    @Bind(R.id.btn_lean_cloud)
    Button btnLeanCloud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;

    }


    @OnClick(R.id.btn_indicator)
    public void onClickIndicator() {
        ZhtUtils.gotoIntent(mContext, IndicatorTestActivity.class);
    }

    @OnClick(R.id.btn_animator)
    public void btnAnimator() {
        ZhtUtils.gotoIntent(mContext, AnimatorTestActivity.class);
    }

    @OnClick(R.id.btn_new_indicator)
    public void btnNewIndicator() {
        ZhtUtils.gotoIntent(mContext, IndicatorAdvanceActivity.class);
    }

    @OnClick(R.id.btn_scroll_test)
    public void btnScrollTest() {
        ZhtUtils.gotoIntent(mContext, ScrollTestActivity.class);
    }

    @OnClick(R.id.btn_draw_test)
    public void btnDrawTest() {
        ZhtUtils.gotoIntent(mContext, DrawTestActivity.class);
    }

    @OnClick(R.id.btn_calendar)
    public void btnCalendar() {
        ZhtUtils.gotoIntent(mContext, CalendarTestActivity.class);
    }

    @OnClick(R.id.btn_net)
    public void btnNet() {
        ZhtUtils.gotoIntent(mContext, RetrofitTestActivity.class);
    }


    @OnClick(R.id.btn_green_dao)
    public void btnGreenDao() {
        ZhtUtils.gotoIntent(mContext, GreenDaoTestActivity.class);
    }

    @OnClick(R.id.btn_rx_java)
    public void btnRxJava() {
        ZhtUtils.gotoIntent(mContext, RxJavaActivity.class);
    }

    @OnClick(R.id.btn_refresh)
    public void btnRefresh() {
        ZhtUtils.gotoIntent(mContext, SwipeRefreshLayoutTestActivity.class);
    }

    @OnClick(R.id.btn_lean_cloud)
    public void btnLeanCloud() {
        ZhtUtils.gotoIntent(mContext, LeanCloudInitActivity.class);
    }
}
