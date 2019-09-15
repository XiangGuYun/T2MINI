package com.yp.payment.internet;

/**
 * Created by 18682 on 2018/11/11.
 */

public class GetBalanceRequest {
    private String deviceId;
    private Integer shopId;
    private String cardNo;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
