package com.yp.payment.jc_internet;

/**
 * Created by 20191024 on 2019/10/28.
 */

public class HttpCallBack {
    public static abstract class HttpCallback {
        public abstract void onSuccess(String result);
        public abstract void onCancelled(String msg);
        public abstract void onError(String error);
        public abstract void onFinish();
    }
}
