package com.zhhtao.base;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.utils.ThirdPartUserUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zhhtao.leancloud.CustomUserProvider;
import com.zhhtao.utils.LogUtil;

/**
 * Created by zhangHaiTao on 2016/5/26.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initLeanCloud();

        AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler());
    }

    /**
     * 初始化leanCloud实时通信
     */
    private void initLeanCloud() {
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "I4AiOzuTXH58hlwzmavFRFuY-gzGzoHsz", "VNNdwm14AProYqwFyd9DkRb6");
//        ChatManager.setDebugEnabled(true);// tag leanchatlib
//        AVOSCloud.setDebugLogEnabled(true);  // set false when release
//        initImageLoader(this);
//        ChatManager.getInstance().init(this);
//        ThirdPartUserUtils.setThirdPartUserProvider(new CustomUserProvider());
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                        //.memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static class CustomMessageHandler extends AVIMMessageHandler {
        //接收到消息后的处理逻辑
        @Override
        public void onMessage(AVIMMessage message,AVIMConversation conversation,AVIMClient client){
            if(message instanceof AVIMTextMessage){
                AVIMTextMessage textMessage = (AVIMTextMessage) message;
                LogUtil.w(textMessage.getText());
            }
        }

        public void onMessageReceipt(AVIMMessage message,AVIMConversation conversation,AVIMClient client){

        }
    }

}
