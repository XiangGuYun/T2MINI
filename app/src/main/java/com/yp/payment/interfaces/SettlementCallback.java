package com.yp.payment.interfaces;

/**
 * @author : cp
 * @email : ibsfiq@qq.com
 * @date : 2019/8/21 14:36
 * @description ：点击结算回调
 */
public interface SettlementCallback {
    void onSettlementClick(double price,double commPrice);
}
