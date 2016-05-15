package com.zhhtao.testad;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.zhhtao.activity.AnimatorTestActivity;
import com.zhhtao.activity.IndicatorTestActivity;
import com.zhhtao.utils.ZhtUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    Activity mContext;
    @InjectView(R.id.btn_indicator)
    Button btnIndicator;
    @InjectView(R.id.btn_animator)
    Button btnAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
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
}
