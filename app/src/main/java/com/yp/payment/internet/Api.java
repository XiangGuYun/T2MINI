package com.yp.payment.internet;


import com.yp.payment.internet.orderresp.JsonsRootBean;
import com.yp.payment.model.GetBalanceResponse;
import com.yp.payment.model.SyncDataEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {
    @POST("zjypg/shangmiInit")
    Call<LoginResponse> init(@Body LoginRequest entity);

    @POST("zjypg/shangmiInitV2")
    Call<LoginResponseV2> initV2(@Body LoginRequest entity);


    @POST("/api/order/createShangMi")
    Call<JsonsRootBean> createShangMi(@Body ShangMiOrderRequest entity);


    @POST("zjypg/syncData")
    Call<SyncDataEntity> syncData(@Body SyncDataRequest entity);

    @POST("zjypg/getBanlance")
    Call<GetBalanceResponse> getBanlance(@Body GetBalanceRequest entity);

}