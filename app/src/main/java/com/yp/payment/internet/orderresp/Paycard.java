package com.yp.payment.internet.orderresp;



public class Paycard {

    private int id;
    private int customerid;
    private int shopid;
    private String cardno;
    private String name;
    private int accountbalance;
    private int wxAccountbalance;
    private int bzAccountbalance;
    private int yktAccountbalance;
    private int cardtype;
    private boolean ismaster;
    private boolean isnopasswordpay;
    private boolean deleted;
    private long created;
    private long updated;
    private String contractId;
    private String contractCode;
    private String payCode;
    private long payCodeTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccountbalance() {
        return accountbalance;
    }

    public void setAccountbalance(int accountbalance) {
        this.accountbalance = accountbalance;
    }

    public int getWxAccountbalance() {
        return wxAccountbalance;
    }

    public void setWxAccountbalance(int wxAccountbalance) {
        this.wxAccountbalance = wxAccountbalance;
    }

    public int getBzAccountbalance() {
        return bzAccountbalance;
    }

    public void setBzAccountbalance(int bzAccountbalance) {
        this.bzAccountbalance = bzAccountbalance;
    }

    public int getYktAccountbalance() {
        return yktAccountbalance;
    }

    public void setYktAccountbalance(int yktAccountbalance) {
        this.yktAccountbalance = yktAccountbalance;
    }

    public int getCardtype() {
        return cardtype;
    }

    public void setCardtype(int cardtype) {
        this.cardtype = cardtype;
    }

    public boolean isIsmaster() {
        return ismaster;
    }

    public void setIsmaster(boolean ismaster) {
        this.ismaster = ismaster;
    }

    public boolean isIsnopasswordpay() {
        return isnopasswordpay;
    }

    public void setIsnopasswordpay(boolean isnopasswordpay) {
        this.isnopasswordpay = isnopasswordpay;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public long getPayCodeTime() {
        return payCodeTime;
    }

    public void setPayCodeTime(long payCodeTime) {
        this.payCodeTime = payCodeTime;
    }
}