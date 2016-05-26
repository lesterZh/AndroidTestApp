package com.zhhtao.base;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by zhangHaiTao on 2016/5/26.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "I4AiOzuTXH58hlwzmavFRFuY-gzGzoHsz", "VNNdwm14AProYqwFyd9DkRb6");
    }
}
