package com.zhhtao.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhhtao.testad.R;
import com.zhhtao.customview.ZhtCustomIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhangHaiTao on 2016/5/13.
 */
public class IndicatorTestActivity extends BaseActivty {
    @InjectView(R.id.indicator)
    ZhtCustomIndicator indicator;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;

    List<View> viewList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator_test);
        ButterKnife.inject(this);

        for (int i = 0; i < 14; i++) {
            Button button = new Button(mContext);
            button.setText("page" + (i + 1));
            viewList.add(button);
        }
        indicator.setTitles(new String[]{"天使", "上帝", "亚当","苹果","西瓜","香蕉","菠萝"
                ,"天使", "上帝", "亚当","苹果","西瓜","香蕉","菠萝"});
        viewPager.setAdapter(new MyViewPagerAdapter());
        indicator.bindViewPager(viewPager);
    }

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
