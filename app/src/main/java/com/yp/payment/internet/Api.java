package com.yp.payment.internet;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface Api {
    @POST("zjypg/shangmiInit")
    Call<LoginResponse> init(@Body LoginRequest entity);


    @POST("/api/order/createShangMi")
    Call<ShangMiOrderResponse> createShangMi(@Body ShangMiOrderRequest entity);


}