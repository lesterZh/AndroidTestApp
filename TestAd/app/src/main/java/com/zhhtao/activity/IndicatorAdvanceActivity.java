package com.zhhtao.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhhtao.customview.IndicatorAdvanced;
import com.zhhtao.testad.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class IndicatorAdvanceActivity extends BaseActivty {

    @InjectView(R.id.indicator)
    IndicatorAdvanced indicator;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;


    String[] titles = new String[15];
    int items = 15;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator_advance);
        ButterKnife.inject(this);


        for (int i = 0; i < items; i++) {
            Button button = new Button(mContext);
            button.setText("page" + (i + 1));
            viewList.add(button);
        }

        for (int i=0; i<items; i++) {
            titles[i] = "标题"+(i+1);
        }
        indicator.setTitles(titles);
        viewPager.setAdapter(new MyViewPagerAdapter());
        indicator.bindViewPager(viewPager);


        indicator.setIndex(4);
    }

    List<View> viewList = new ArrayList<>();

    class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }
}
