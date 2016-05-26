package com.zhhtao.leancloud;

import android.os.Bundle;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.zhhtao.base.BaseActivty;
import com.zhhtao.testad.R;
import com.zhhtao.utils.LogUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeanCloudInitActivity extends BaseActivty {

    @BindView(R.id.btn_chat)
    Button btnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lean_cloud_init);
        ButterKnife.bind(this);

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
//        Intent intent = new Intent(mContext, AVChatActivity.class);
//        intent.putExtra(Constants.MEMBER_ID, "6");
//        startActivity(intent);
    }
}
