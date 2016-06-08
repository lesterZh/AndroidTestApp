package com.zhhtao.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Android客户端组-ZhangHaiTao
 * @version V1.0
 * @Title: NetUtil
 * @Package com.zhhtao.net
 * @Description: TODO
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/6/8 14:01
 */
public class NetUtil {
    private static NetUtil netUtil = new NetUtil();
    private NetUtil() {};

    public static NetUtil getInstance() {
        return netUtil;
    }

    public NetApiServiceInterface getNetApiServiceInterface(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return  retrofit.create(NetApiServiceInterface.class);
    }

}
