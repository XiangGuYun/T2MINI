package com.yp.payment.jc_internet;

import com.yp.payment.update.SPHelper;

/**
 * Created by 20191024 on 2020/1/6.
 */

public class ApkConstant {
    public static final String OFFLINE_IP = "http://192.168.3.71:9010";
    public static String ONLINE_IP = SPHelper.getIpAddress();
    public static final String GETSHANGMIORDER = ONLINE_IP+"/api/order/getShangMiOrder/";

    public static String CUSTOMER = ONLINE_IP+"/api/customer";
    public static String CREATESHANGMI = ONLINE_IP+"/api/order/createShangMi";
    public static String GETBANLANCE = ONLINE_IP+"/zjypg/getBalance";
    public static String SHANGMIINITV2 = ONLINE_IP+"/zjypg/shangmiInitV2";
    public static String SYNCDATA = ONLINE_IP+"/zjypg/syncData";
    public static String ISQLQUERY = ONLINE_IP+"/api/wxapp/openapi/iSqlquery";
    public static String CREATE = ONLINE_IP+"/api/order/create";
    /** 获取门店配置 */
    public static String GETSHOPCONFIG = ONLINE_IP+"/api/shop/getShopConfig";

}
