package com.zhhtao.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhhtao.base.BaseActivty;
import com.zhhtao.bean.LoginResultBean;
import com.zhhtao.bean.PhoneResultBean;
import com.zhhtao.bean.SmsVerificationCodeBean;
import com.zhhtao.net.NetApiServiceInterface;
import com.zhhtao.net.NetUtil;
import com.zhhtao.net.PhoneService;
import com.zhhtao.net.PhoneServiceGetString;
import com.zhhtao.testad.R;
import com.zhhtao.utils.LogUtil;
import com.zhhtao.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangHaiTao on 2016/5/23.
 */
public class RetrofitTestActivity extends BaseActivty {

    @Bind(R.id.et_input)
    EditText etInput;
    @Bind(R.id.btn_query)
    Button btnQuery;
    @Bind(R.id.tv_res)
    TextView tvRes;
    @Bind(R.id.btn_get_string)
    Button btnGetString;
    @Bind(R.id.btn_verification)
    Button btnVerification;
    @Bind(R.id.et_smsCode)
    EditText etSmsCode;
    @Bind(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_test);
        ButterKnife.bind(this);
        tvRes.setText("查询结果：");
        etInput.setText("17729849372");
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

    /**
     * 发送验证码
     */
    void getVerificationCode() {
        String baseUrl = "http://192.168.2.2:8080/gaia/member/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NetApiServiceInterface service = retrofit.create(NetApiServiceInterface.class);

        String input = etInput.getText().toString();

        //构造post请求体
        JSONStringer jsonStringer = null;
        try {
            jsonStringer = new JSONStringer().object()
                    .key("phone").value(input)
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = jsonStringer.toString();

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
        Call<SmsVerificationCodeBean> call = service.getVerificationCode(body);
        call.enqueue(new Callback<SmsVerificationCodeBean>() {
            @Override
            public void onResponse(Call<SmsVerificationCodeBean> call, Response<SmsVerificationCodeBean> response) {
                if (response.isSuccessful()) {
                    SmsVerificationCodeBean smsVerificationCodeBean = response.body();
                    LogUtil.w(smsVerificationCodeBean.toString());
                    UIUtils.showText(mContext, smsVerificationCodeBean.toString());
                } else {
                    LogUtil.w("failed");
                }
            }

            @Override
            public void onFailure(Call<SmsVerificationCodeBean> call, Throwable t) {
                LogUtil.w("failed");
            }
        });
    }

    /**
     * 登录
     */
    public void loginWithPhoneAndVerfication() {
        String baseUrl = "http://192.168.2.2:8080/gaia/member/";

        String phone = etInput.getText().toString();
        String smsCode = etSmsCode.getText().toString();
        JSONStringer jsonStringer = null;
        try {
            jsonStringer = new JSONStringer().object()
                    .key("phone").value(phone)
                    .key("smsCode").value(smsCode)
                    .key("ulon").value(99)
                    .key("ulat").value(99)
                    .key("geohashLength").value(1)
                    .key("limitKm").value(1)
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonStr = jsonStringer.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);


//        NetUtil.getInstance().getNetApiServiceInterface(baseUrl)
//                .postLogin(body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<LoginResultBean>() {
//                    @Override
//                    public void call(LoginResultBean loginResultBean) {
//                        if (loginResultBean != null) {
//                            LogUtil.w(loginResultBean.toString());
//                        } else {
//                            LogUtil.w("failed");
//                        }
//                    }
//                });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NetApiServiceInterface service = retrofit.create(NetApiServiceInterface.class);
        Call<LoginResultBean> call = service.postLogin2(body);
        call.enqueue(new Callback<LoginResultBean>() {
            @Override
            public void onResponse(Call<LoginResultBean> call, Response<LoginResultBean> response) {
                if (response.isSuccessful()) {
                    LoginResultBean loginResultBean = response.body();
                    UIUtils.showText(mContext, loginResultBean.toString());
                } else {
                    UIUtils.showText(mContext, "login fail");
                }
            }

            @Override
            public void onFailure(Call<LoginResultBean> call, Throwable t) {
                UIUtils.showText(mContext, "onFailure");
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

    @OnClick(R.id.btn_verification)
    public void btnVerification() {
        getVerificationCode();
    }

    @OnClick(R.id.btn_login)
    public void btnLogin() {
        loginWithPhoneAndVerfication();
    }
}
