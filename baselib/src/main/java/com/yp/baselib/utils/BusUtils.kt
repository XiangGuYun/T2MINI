package com.yp.baselib.utils

import android.os.Message
import org.greenrobot.eventbus.EventBus

/**
 * EventBus工具类
 */
object BusUtils {

    fun post(what: Int) {
        val msg = Message.obtain()
        msg.what = what
        EventBus.getDefault().post(msg)
    }

    fun post(what: Int, obj: Any? = null) {
        val msg = Message.obtain()
        msg.what = what
        if (obj != null) msg.obj = obj
        EventBus.getDefault().post(msg)
    }

    /**
     * 发送普通消息
     */
    fun post(what: Int, obj: Any? = null, arg1: Int = 0, arg2: Int = 2) {
        val msg = Message.obtain()
        msg.what = what
        if (obj != null) msg.obj = obj
        msg.arg1 = arg1
        msg.arg2 = arg2
        EventBus.getDefault().post(msg)
    }

    /**
     * 发送粘性消息
     */
    fun postSticky(what: Int, obj: Any? = null, arg1: Int = 0, arg2: Int = 2) {
        val msg = Message.obtain()
        msg.what = what
        if (obj != null) msg.obj = obj
        msg.arg1 = arg1
        msg.arg2 = arg2
        EventBus.getDefault().postSticky(msg)
    }

}