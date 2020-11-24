package com.yp.payment.order.layer.utils;

import android.os.Message;

import org.greenrobot.eventbus.EventBus;

public class BusUtils {

    public static void sendMsg(int what){
        Message msg = Message.obtain();
        msg.what = what;
        EventBus.getDefault().post(msg);
    }

    public static void sendMsgWithObj(int what, Object obj){
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        EventBus.getDefault().post(msg);
    }

}
