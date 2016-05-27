package com.zhhtao.leancloud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.zhhtao.base.BaseActivty;
import com.zhhtao.testad.R;
import com.zhhtao.utils.LogUtil;
import com.zhhtao.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaveDataActivity extends BaseActivty {

    @Bind(R.id.btn_add)
    Button btnAdd;
    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.et_num)
    EditText etNum;
    @Bind(R.id.btn_query)
    Button btnQuery;
    @Bind(R.id.tv_res)
    TextView tvRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data);
        ButterKnife.bind(this);
    }

    String saveId = "";

    @OnClick(R.id.btn_add)
    public void btnAdd() {
        final AVObject todo = new AVObject("Todo");
        todo.put("title", etTitle.getText().toString().trim());
        todo.put("content", etContent.getText().toString().trim());
        todo.put("location", "会议室");
        todo.put("num", Integer.parseInt(etNum.getText().toString().trim()));
        todo.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    //成功
                    LogUtil.i("ok");
                    saveId = todo.getObjectId();
                    LogUtil.i("id-" + saveId);
                    UIUtils.showToast(mContext, "ok");
                } else {
                    //失败
                    LogUtil.i("failed");
                    UIUtils.showToast(mContext, "failed");
                }
            }
        });

    }

    @OnClick(R.id.btn_query)
    public void btnQuery() {
        AVQuery<AVObject> query = new AVQuery<>("Todo");

        query.getInBackground(saveId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                String get = "title-"+avObject.getString("title");
                get += " content-" + avObject.getString("content")+" location-" +avObject.getString("location")
                        +" id-" +avObject.getObjectId()+" createdAt-" +  avObject.getCreatedAt();
                tvRes.setText(get);
            }
        });
    }
}
