package com.yp.baselib.helper

import android.app.Activity
import android.content.IntentFilter
import android.net.ConnectivityManager

/**
 * 监听网络连接状态帮助类
 *
 * 注意需要添加以下权限：
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * @author YXD
 */
class NetListenHelper{

    private var isRegistered: Boolean = false
    private var netReceiver: NetStatusReceiver? = null

    /**
     * 注册监听网络状态的广播接收器
     * @param activity Activity
     * @param onConnected Function1<Int, Unit> 连接回调
     * @param onDisconnect Function0<Unit> 断开连接回调
     */
    fun register(activity: Activity, onConnected: (Int) -> Unit, onDisconnect: () -> Unit) {
        netReceiver = NetStatusReceiver()
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        isRegistered = true
        activity.registerReceiver(netReceiver, filter);
        netReceiver?.setNetStateListener { state ->
            if (state == NetStatusReceiver.NETSTATUS_INAVAILABLE) {
                // 网络未连接
                onDisconnect.invoke()
            } else {
                // 网络已经连接
                onConnected.invoke(state)
            }
        }
    }

    /**
     * 解除注册
     * @param activity Activity
     */
    fun unregister(activity: Activity) {
        if(isRegistered){
            isRegistered = false
            activity.unregisterReceiver(netReceiver)
        }
    }

}