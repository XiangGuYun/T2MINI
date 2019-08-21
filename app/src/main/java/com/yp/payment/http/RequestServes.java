package com.yp.payment.http;



import com.yp.payment.entity.BaseEntity;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestServes {

    @POST("/dsmusic/api/user/binding")
    Call<BaseEntity> bindDev(@Header("userId") String userId, @Query("snCode") String snCode, @Query("equipTypeCode") String equipTypeCode);


}
