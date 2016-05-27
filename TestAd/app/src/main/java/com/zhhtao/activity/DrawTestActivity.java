package com.zhhtao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhhtao.base.BaseActivty;
import com.zhhtao.customview.QuickIndexBar;
import com.zhhtao.testad.R;
import com.zhhtao.ui.ScaleWheel;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Created by zhangHaiTao on 2016/5/17.
 */
public class DrawTestActivity extends BaseActivty {

    @Bind(R.id.scale_view)
    ScaleWheel scaleView;
    @Bind(R.id.index_bar)
    QuickIndexBar indexBar;
    @Bind(R.id.tv_show_quick_index)
    TextView tvShowQuickIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_test);
        ButterKnife.bind(this);

        scaleView.scrollToValue(100);

        tvShowQuickIndex.setVisibility(View.INVISIBLE);

        indexBar.setOnSelectChangeListener(new QuickIndexBar.OnSelectChangeListener() {
            @Override
            public void onChange(String cur) {
                if (cur.equals("")) {
                    tvShowQuickIndex.setVisibility(View.INVISIBLE);
                } else {
                    tvShowQuickIndex.setText(cur);
                    tvShowQuickIndex.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
