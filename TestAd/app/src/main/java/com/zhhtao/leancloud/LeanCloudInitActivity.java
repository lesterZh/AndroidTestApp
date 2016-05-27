package com.zhhtao.leancloud;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avoscloud.leanchatlib.activity.AVChatActivity;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.utils.Constants;
import com.zhhtao.base.BaseActivty;
import com.zhhtao.testad.R;
import com.zhhtao.utils.LogUtil;
import com.zhhtao.utils.UIUtils;
import com.zhhtao.utils.ZhtUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeanCloudInitActivity extends BaseActivty {

    @Bind(R.id.btn_chat)
    Button btnChat;
    @Bind(R.id.btn_save_data)
    Button btnSaveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lean_cloud_init);
        ButterKnife.bind(this);
//        testSDK();
    }

    private void testSDK() {
        // 测试 SDK 是否正常工作的代码
        AVObject testObject = new AVObject("TestObject");
        testObject.put("words", "Hello World!" + new Date());
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    LogUtil.w("success!");
                }
            }
        });
    }

    @OnClick(R.id.btn_chat)
    public void btnChat() {
        ChatManager.getInstance().openClient(this, "0", new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null == e) {
                    Intent intent = new Intent(mContext, AVChatActivity.class);
                    intent.putExtra(Constants.MEMBER_ID, "bb");
                    startActivity(intent);
                } else {
                    UIUtils.showToast(mContext, e.toString());
                }
            }
        });
    }

    @OnClick(R.id.btn_save_data)
    public void btnSaveData() {
        ZhtUtils.gotoIntent(mContext, SaveDataActivity.class);
    }
}
