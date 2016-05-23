package com.zhhtao.custominterface;

import com.zhhtao.bean.PhoneResultBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by ZhangHaiTao on 2016/5/23.
 */
public interface PhoneService {
    @GET("/apistore/mobilenumber/mobilenumber")
    Call<PhoneResultBean> getResult(@Header("apikey") String apikey, @Query("phone") String phone);
}
