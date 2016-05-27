package com.zhhtao.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zhhtao.testad.R;

import butterknife.ButterKnife;
import butterknife.Bind;

public class SwipeRefreshLayoutTestActivity extends AppCompatActivity {

    @Bind(R.id.tv_random_num)
    TextView tvRandomNum;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh_layout_test);
        ButterKnife.bind(this);

        swipeContainer.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 停止刷新
                        swipeContainer.setRefreshing(false);
                        int num = (int) (Math.random() * 100);
                        tvRandomNum.setText("随机数字："+num);
                    }
                },3000); // 5秒后发送消息，停止刷新
            }
        });


        /*
        * 有时候调用swipeContainer.setRefreshing(true);无效
        * 则可使用如下代码：
        * */
//        swipeContainer.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeContainer.setRefreshing(true);
//            }
//        });

    }
}
