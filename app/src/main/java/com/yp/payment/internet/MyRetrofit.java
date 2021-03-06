package com.yp.payment.internet;


import android.util.Log;

import com.yp.payment.update.SPHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by 18682 on 2018/11/11.
 */

public class MyRetrofit {
    public static Api getApiService() {


        Retrofit retrofit = new Retrofit.Builder()
                //设置OKHttpClient,如果不设置会提供一个默认的
                .client(genericClient())
                .baseUrl(SPHelper.getIpAddress())
                .addConverterFactory(ScalarsConverterFactory.create())
                //添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.d("dasdas", SPHelper.getIpAddress());

        Api service = retrofit.create(Api.class);
        return service;
    }

    public static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).
                readTimeout(5, TimeUnit.SECONDS).
                writeTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {

                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .build();

                        return chain.proceed(request);
                    }

                })
                .build();

        return httpClient;
    }


}
