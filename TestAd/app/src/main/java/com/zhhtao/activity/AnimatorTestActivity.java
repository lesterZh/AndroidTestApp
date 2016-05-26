package com.zhhtao.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zhhtao.base.BaseActivty;
import com.zhhtao.testad.R;
import com.zhhtao.utils.UIUtils;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class AnimatorTestActivity extends BaseActivty {

    @BindView(R.id.iv_girl)
    ImageView ivGirl;
    @BindView(R.id.btn_scale)
    Button btnShow;
    @BindView(R.id.btn_hide)
    Button btnHide;
    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.btn_down_dismiss)
    Button btnDownDismiss;
    @BindView(R.id.ll_down)
    LinearLayout llDown;
    @BindView(R.id.btn_add_window)
    Button btnAddWindow;
    @BindView(R.id.btn_move)
    Button btnMove;
    @BindView(R.id.iv_progress)
    ImageView ivProgress;
    @BindView(R.id.ll_change_bg)
    LinearLayout llChangeBg;
    @BindView(R.id.btn_progress)
    Button btnProgress;
    @BindView(R.id.btn_pop)
    Button btnPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator_test);
        ButterKnife.bind(this);

        llDown.setVisibility(View.INVISIBLE);//消除第一次动画因gone属性产生的bug
    }

    @OnClick(R.id.btn_scale)
    public void btnShow() {
        //视图动画
//        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0,
//                Animation.RELATIVE_TO_SELF, 0);
//        scaleAnimation.setDuration(1000);
//        ivGirl.startAnimation(scaleAnimation);
//        ivGirl.setVisibility(View.VISIBLE);

        //属性动画
        ObjectAnimator ob = ObjectAnimator.ofFloat(ivGirl, "scaleY", 0, 1);
        ivGirl.setPivotY(0);
        ivGirl.setVisibility(View.VISIBLE);
        ob.setDuration(2000);
        ob.start();
    }

    @OnClick(R.id.btn_hide)
    public void btnHide() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1, 1, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        scaleAnimation.setDuration(1000);
        ivGirl.startAnimation(scaleAnimation);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivGirl.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    @OnClick({R.id.btn_down, R.id.btn_down_dismiss})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_down:

                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0);
                scaleAnimation.setDuration(1000);
                //使用补间动画
//                translateAnimation.setFillAfter(true);
//                llDown.startAnimation(scaleAnimation);

//                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
//                        Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,-1,Animation.RELATIVE_TO_SELF,0);
//                translateAnimation.setDuration(1000);
//                llDown.startAnimation(translateAnimation);
//                llDown.setVisibility(View.VISIBLE);

//                llDown.setAlpha(0);
                llDown.setVisibility(View.VISIBLE);
                //这里llDown的初始界面布局为invisible
                // 如果为gone则第一次动画异常

                ObjectAnimator showAnim = ObjectAnimator.ofFloat(llDown, "translationY",
                        -llDown.getHeight()
                        , 0);
                ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(llDown, "alpha", 0, 1);

                AnimatorSet set = new AnimatorSet();

                set.play(showAnim).with(alphaAnim);
                set.start();
                break;
            case R.id.btn_down_dismiss:
//                llDown.setVisibility(View.GONE);
                ObjectAnimator dismissAnim = ObjectAnimator.ofFloat(llDown, "translationY", 0, -llDown.getHeight());
                dismissAnim.setDuration(300);
                dismissAnim.start();
                UIUtils.showToast(mContext, "dismiss");
                break;
        }
    }

    ImageView ivRocket;
    WindowManager wm;
    WindowManager.LayoutParams params;

    //使用windowManager来添加和移动View
    @OnClick({R.id.btn_add_window, R.id.btn_move, R.id.btn_remove})
    public void btn_add_window(View view) {
        switch (view.getId()) {
            case R.id.btn_add_window:
                wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                params = new WindowManager.LayoutParams();
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                params.format = PixelFormat.TRANSLUCENT;
                params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        //这个属性有点变态，会一直点亮屏幕
                        //如果不加下面的特性  则屏幕的其他部分无法相应点击,且返回键无效
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

                        //设置背景的不透明效果
                        | WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                params.dimAmount = 0.4f;

                //显示在屏幕最上面，可移动
//                params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;

                ivRocket = new ImageView(mContext);
                ivRocket.setOnTouchListener(new View.OnTouchListener() {
                    int startX;
                    int startY;

                    //注意这里获取的getRawX和getRawY是不能直接赋值给param的x和y的
                    //只能通过偏移量来实现移动
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                startX = (int) event.getRawX();
                                startY = (int) event.getRawY();

                                break;

                            case MotionEvent.ACTION_MOVE:
                                int newX = (int) event.getRawX();
                                int newY = (int) event.getRawY();
                                int dx = newX - startX;
                                int dy = newY - startY;
                                params.x += dx;
                                params.y += dy;

                                startX = (int) event.getRawX();
                                startY = (int) event.getRawY();
                                wm.updateViewLayout(ivRocket, params);
                                break;
                        }

                        return true;
                    }
                });
                ivRocket.setImageResource(R.drawable.desktop_rocket_launch_1);

                wm.addView(ivRocket, params);

                break;
            case R.id.btn_move:
                params.x += 10;
                params.y += 20;
                wm.updateViewLayout(ivRocket, params);
                break;
            case R.id.btn_remove:
                wm.removeViewImmediate(ivRocket);
                break;
        }
    }

    @OnClick(R.id.btn_progress)
    public void btnProgress() {
        llChangeBg.setBackgroundColor(Color.BLUE);

        ivProgress.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivProgress, "translationX", -ivProgress.getWidth(), ivProgress.getWidth());
        animator.setDuration(1200);
        animator.setRepeatCount(3);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animation.start();
            }
        });
        animator.start();

    }


    PopupWindow popupWindow ;
    @OnClick(R.id.btn_pop)
    public void btnPop() {
        popupWindow = new PopupWindow(mContext);
        View view = View.inflate(mContext, R.layout.layout_pop_window, null);
        view.findViewById(R.id.btn_pop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(view);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x22000000));
        popupWindow.showAsDropDown(btnPop);

    }
}

