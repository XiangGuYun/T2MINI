package com.yp.payment.new

import com.yp.baselib.base.BaseApplication
import com.yp.payment.pay.SPUtils

object SPHelper {

    fun getIpAddress(): String {
        return SPUtils.getSP(BaseApplication.getInstance().context, "ipAddress", "").toString()
    }

    fun putIpAddress(ipAddress: String) {
        SPUtils.putSP(BaseApplication.getInstance().context, "ipAddress", ipAddress)
    }


}