package com.yp.payment.internet;

public class ShangMiOrderRequest {
    private String cardNo;

    private Long accountBalance;

    private String authCode;

    private int payType;

    private String faceIMG64;

    private Integer shopID;

    private Integer cashierDeskID;

    public String getCardNo() {
        return cardNo;
    }

    public int isOffline;

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Long getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Long accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getFaceIMG64() {
        return faceIMG64;
    }

    public void setFaceIMG64(String faceIMG64) {
        this.faceIMG64 = faceIMG64;
    }

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    public Integer getCashierDeskID() {
        return cashierDeskID;
    }

    public void setCashierDeskID(Integer cashierDeskID) {
        this.cashierDeskID = cashierDeskID;
    }
}
