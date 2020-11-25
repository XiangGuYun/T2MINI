package com.yp.payment.update

import com.yp.baselib.base.BaseApplication
import com.yp.baselib.utils.SPUtils

object SPHelper {
    @JvmStatic
    fun getIpAddress(): String {
        return SPUtils.get(BaseApplication.getInstance().context, "ipAddress", "https://binguoai.com").toString()
    }
    @JvmStatic
    fun putIpAddress(ipAddress: String) {
        SPUtils.put(BaseApplication.getInstance().context, "ipAddress", ipAddress)
    }
    @JvmStatic
    fun putShopId(shopId: Int) {
        SPUtils.put(BaseApplication.getInstance().context, "shopId", shopId)
    }
    @JvmStatic
    fun getShopId(): Int {
        return SPUtils.getInt(BaseApplication.getInstance().context, "shopId", 0)
    }
    @JvmStatic
    fun putCashierDeskId(cashierDeskId: Int) {
        SPUtils.put(BaseApplication.getInstance().context, "cashierDeskId", cashierDeskId)
    }
    @JvmStatic
    fun getCashierDeskId(): Int {
        return SPUtils.getInt(BaseApplication.getInstance().context, "cashierDeskId", 0)
    }
    @JvmStatic
    fun putCurUsername(curUsername: String) {
        SPUtils.put(BaseApplication.getInstance().context, "curUsername", curUsername)
    }

    @JvmStatic
    fun getCurUsername(): String {
        return SPUtils.getString(BaseApplication.getInstance().context, "curUsername", "")
    }

    @JvmStatic
    fun putShopName(shopName: String) {
        SPUtils.put(BaseApplication.getInstance().context, "shopName", shopName)
    }
    @JvmStatic
    fun getShopName(): String {
        return SPUtils.getString(BaseApplication.getInstance().context, "shopName", "")
    }

    @JvmStatic
    fun putMoney(money: String) {
        SPUtils.put(BaseApplication.getInstance().context, "money", money)
    }

    @JvmStatic
    fun getMoney(): String {
        return SPUtils.getString(BaseApplication.getInstance().context, "money", "")
    }



}