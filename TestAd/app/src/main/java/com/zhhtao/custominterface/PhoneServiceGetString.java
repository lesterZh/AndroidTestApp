package com.zhhtao.custominterface;

import com.zhhtao.bean.PhoneResultBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by ZhangHaiTao on 2016/5/23.
 */
public interface PhoneServiceGetString {
    @GET("/apistore/mobilenumber/mobilenumber")
    Call<ResponseBody> getResult(@Header("apikey") String apikey, @Query("phone") String phone);
}
