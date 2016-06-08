package com.zhhtao.net;

import com.zhhtao.bean.LoginResultBean;
import com.zhhtao.bean.PhoneResultBean;
import com.zhhtao.bean.SmsVerificationCodeBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Android客户端组-ZhangHaiTao
 * @version V1.0
 * @Title: NetApiServiceInterface
 * @Package com.zhhtao.custominterface
 * @Description: TODO
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/6/8 11:09
 */
public interface NetApiServiceInterface {

    @GET("/apistore/mobilenumber/mobilenumber")
    Call<PhoneResultBean> getResult(@Header("apikey") String apikey, @Query("phone") String phone);

    /**
     * Get请求方式
     *
     * xyzBean是一个自定义的实体类
     * key 是请求时,参数对应的key
     * 类型 是这个参数value的类型
     */
    @GET("name")
    Call<PhoneResultBean> getTest(@Query("key") String value);

    /**
     * Post 请求方式
     *
     * TestBean 是自己定义的一个实体类
     * @param requestBody 请求体: 通过RequestBody.create(MediaType,String)来创建
     * @return
     */
    @POST("api/auth/login")
    Call<PhoneResultBean> postTest(@Body RequestBody requestBody);


    @POST("api/sms/get")
    Call<SmsVerificationCodeBean> getVerificationCode(@Body RequestBody requestBody);

    @POST("auth/login/sms")
    Observable<LoginResultBean> postLogin(@Body RequestBody requestBody);

    @POST("auth/login/sms")
    Call<LoginResultBean> postLogin2(@Body RequestBody requestBody);
}
