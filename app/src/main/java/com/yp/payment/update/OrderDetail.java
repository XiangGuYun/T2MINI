package com.yp.payment.update;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class OrderDetail {

    @Id
    private Long id;
    private int shopId;
    private int cashierDeskId;
    private String orderNo;
    private String dateTime;
    private int orderType;
    private String orderTypeStr;
    private String price;
    private String realPrice;
    private String discountPrice;
    private int branchId;

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    /**
     * 是否采用离线支付
     */
    private boolean isPayByOffline;

    /**
     * 卡号
     */
    private String cardNo;

    @Generated(hash = 1019234396)
    public OrderDetail(Long id, int shopId, int cashierDeskId, String orderNo,
            String dateTime, int orderType, String orderTypeStr, String price,
            String realPrice, String discountPrice, int branchId,
            boolean isPayByOffline, String cardNo) {
        this.id = id;
        this.shopId = shopId;
        this.cashierDeskId = cashierDeskId;
        this.orderNo = orderNo;
        this.dateTime = dateTime;
        this.orderType = orderType;
        this.orderTypeStr = orderTypeStr;
        this.price = price;
        this.realPrice = realPrice;
        this.discountPrice = discountPrice;
        this.branchId = branchId;
        this.isPayByOffline = isPayByOffline;
        this.cardNo = cardNo;
    }

    @Generated(hash = 268085433)
    public OrderDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getCashierDeskId() {
        return cashierDeskId;
    }

    public void setCashierDeskId(int cashierDeskId) {
        this.cashierDeskId = cashierDeskId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getOrderTypeStr() {
        return orderTypeStr;
    }

    public void setOrderTypeStr(String orderTypeStr) {
        this.orderTypeStr = orderTypeStr;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public boolean isPayByOffline() {
        return isPayByOffline;
    }

    public void setPayByOffline(boolean payByOffline) {
        isPayByOffline = payByOffline;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public boolean getIsPayByOffline() {
        return this.isPayByOffline;
    }

    public void setIsPayByOffline(boolean isPayByOffline) {
        this.isPayByOffline = isPayByOffline;
    }
}
