package com.yp.payment.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by cp on 2017/8/17.
 */

public class RetrofitConfig<T> {
    public static RetrofitConfig instance;
    public T service;

    public RetrofitConfig(String baseUrl, Class<T> requestServesClass) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(requestServesClass);
    }

    public static RetrofitConfig creatInstance(String baseUrl, Class requestServesClass) {
        synchronized (RetrofitConfig.class) {
            instance = new RetrofitConfig(baseUrl, requestServesClass);
        }
        return instance;
    }

    public static RetrofitConfig getInstance() {
        return instance;
    }

}
