package com.yp.baselib.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * 网络状态工具类
 */
object NetUtils {

    /**
     * 网络类型
     */
    enum class NetType {
        WIFI, CMNET, CMWAP, NONE
    }

    /**
     * 判断是否有网络连接
     * 需要添加ACCESS_NETWORK_STATE权限
     */
    @JvmStatic
    fun isNetConnected(ctx:Context): Boolean {
        val mConnectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable
        }
        return false
    }

    /**
     * 获取当前的网络状态
     * 需要添加ACCESS_NETWORK_STATE权限
     */
    fun getNetType(ctx:Context): NetType {
        val connMgr = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo ?: return NetType.NONE
        val nType = networkInfo.type

        if (nType == ConnectivityManager.TYPE_MOBILE) {
            return if (networkInfo.extraInfo.toLowerCase() == "cmnet") {
                NetType.CMNET
            } else {
                NetType.CMWAP
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI
        }
        return NetType.NONE

    }


}