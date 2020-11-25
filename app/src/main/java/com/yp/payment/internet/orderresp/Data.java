package com.yp.payment.internet.orderresp;

import java.util.List;

public class Data {
    private String orderNo;
    private String productOrderUrl;
    private String serialNumber;
    private int serial;
    private int shopId;
    private String shopName;
    private int cashierDeskId;
    private int customerId;
    private String customerName;
    private String customerPhone;
    private int payType;
    private int totalFee;
    private int realFee;
    private int refundFee;
    private int payStatus;
    private int refundStatus;
    private String note;
    private long payTime;
    private long refundTime;
    private int itemCount;
    private List<Orderitems> orderItems;
    private String qrCode;
    private Paycard payCard;
    private String isSuccee;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductOrderUrl() {
        return productOrderUrl;
    }

    public void setProductOrderUrl(String productOrderUrl) {
        this.productOrderUrl = productOrderUrl;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getCashierDeskId() {
        return cashierDeskId;
    }

    public void setCashierDeskId(int cashierDeskId) {
        this.cashierDeskId = cashierDeskId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public int getRealFee() {
        return realFee;
    }

    public void setRealFee(int realFee) {
        this.realFee = realFee;
    }

    public int getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(int refundFee) {
        this.refundFee = refundFee;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public long getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(long refundTime) {
        this.refundTime = refundTime;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public List<Orderitems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<Orderitems> orderItems) {
        this.orderItems = orderItems;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Paycard getPayCard() {
        return payCard;
    }

    public void setPayCard(Paycard payCard) {
        this.payCard = payCard;
    }

    public String getIsSuccee() {
        return isSuccee;
    }

    public void setIsSuccee(String isSuccee) {
        this.isSuccee = isSuccee;
    }
}
