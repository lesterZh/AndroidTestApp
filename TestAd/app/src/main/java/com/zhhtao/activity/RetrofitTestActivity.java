package com.zhhtao.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhhtao.base.BaseActivty;
import com.zhhtao.bean.PhoneResultBean;
import com.zhhtao.custominterface.PhoneService;
import com.zhhtao.custominterface.PhoneServiceGetString;
import com.zhhtao.testad.R;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangHaiTao on 2016/5/23.
 */
public class RetrofitTestActivity extends BaseActivty {

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.btn_query)
    Button btnQuery;
    @BindView(R.id.tv_res)
    TextView tvRes;
    @BindView(R.id.btn_get_string)
    Button btnGetString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_test);
        ButterKnife.bind(this);
        tvRes.setText("查询结果：");
        etInput.setText("13312345678");
    }

    private static final String BASE_URL = "http://apis.baidu.com";
    private static final String API_KEY = "8e13586b86e4b7f3758ba3bd6c9c9135";

    private void qurey() {
        //1.创建retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //2.创建API访问请求
        PhoneService service = retrofit.create(PhoneService.class);
        String input = etInput.getText().toString();
        Call<PhoneResultBean> call = service.getResult(API_KEY, input);

        //3.发送请求
        call.enqueue(new Callback<PhoneResultBean>() {
            @Override
            public void onResponse(Call<PhoneResultBean> call, Response<PhoneResultBean> response) {
                if (response.isSuccessful()) {
                    final PhoneResultBean phoneResultBean = response.body();
                    if (phoneResultBean != null) {
//                        UIUtils.showToast(mContext, phoneResultBean.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvRes.setText("查询结果：" + phoneResultBean.toString());
                            }
                        });
                    }

                }
            }

            @Override
            public void onFailure(Call<PhoneResultBean> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvRes.setText("查询结果：失败");
                    }
                });
            }
        });
    }



    //http://www.jianshu.com/p/92bb85fc07e8 通过ResponseBody获取文件
    /*
    long fileSize = body.contentLength();
    long fileSizeDownloaded = 0;

    inputStream = body.byteStream();
    outputStream = new FileOutputStream(futureStudioIconFile);
    */

    //不加自动转换  返回string
    private void qureyForString() {
        //1.创建retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //2.创建API访问请求
        PhoneServiceGetString service = retrofit.create(PhoneServiceGetString.class);
        String input = etInput.getText().toString();
        Call<ResponseBody> call = service.getResult(API_KEY, input);

        //3.发送请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
//                        UIUtils.showToast(mContext, phoneResultBean.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    tvRes.setText("查询结果：" + response.body().string());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvRes.setText("查询结果：失败");
                    }
                });
            }
        });
    }

    @OnClick(R.id.btn_query)
    public void onClick() {
        qurey();
    }

    @OnClick(R.id.btn_get_string)
    public void btnGetString() {
        qureyForString();
    }
}
