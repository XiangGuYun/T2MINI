package com.yp.payment.http;


public class HttpCenter {
    private static final String TAG = "HttpCenter";

    static {
        RetrofitConfig.creatInstance("http://", RequestServes.class);
    }


}
