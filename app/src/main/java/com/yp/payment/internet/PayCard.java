package com.yp.payment.internet;

import java.util.Date;

public class PayCard {
    private Integer id;

    /**
     * 客户ID
     */
    private Integer customerid;

    /**
     * 商户id
     */
    private Integer shopid;

    /**
     * 卡号
     */
    private String cardno;

    /**
     * 别名
     */
    private String name;

    /**
     * 账户余额
     */
    private Long accountbalance;

    /**
     * 微信余额
     */
    private Long wxAccountbalance;

    /**
     * 补助钱包
     */
    private Long bzAccountbalance;

    /**
     * 一卡通余额
     */
    private Long yktAccountbalance;

    /**
     * 卡类型。0：虚拟卡，1：实体卡，2：一卡通
     */
    private Byte cardtype;

    /**
     * 是否为主卡(0:否，1:是)
     */
    private Boolean ismaster;

    /**
     * 是否免密支付(0:否，1:是)
     */
    private Boolean isnopasswordpay;

    /**
     * 是否删除(0:否，1:是)
     */
    private Boolean deleted;

    private Date created;

    private Date updated;

    /**
     * 免密签约协议号—微信端返回
     */
    private String contractId;

    /**
     * 免密签约协议号—我方生成
     */
    private String contractCode;

    /**
     * 支付条形码
     */
    private String payCode;

    /**
     * 支付条形码有效时间
     */
    private Date payCodeTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public Integer getShopid() {
        return shopid;
    }

    public void setShopid(Integer shopid) {
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

    public Long getAccountbalance() {
        return accountbalance;
    }

    public void setAccountbalance(Long accountbalance) {
        this.accountbalance = accountbalance;
    }

    public Long getWxAccountbalance() {
        return wxAccountbalance;
    }

    public void setWxAccountbalance(Long wxAccountbalance) {
        this.wxAccountbalance = wxAccountbalance;
    }

    public Long getBzAccountbalance() {
        return bzAccountbalance;
    }

    public void setBzAccountbalance(Long bzAccountbalance) {
        this.bzAccountbalance = bzAccountbalance;
    }

    public Long getYktAccountbalance() {
        return yktAccountbalance;
    }

    public void setYktAccountbalance(Long yktAccountbalance) {
        this.yktAccountbalance = yktAccountbalance;
    }

    public Byte getCardtype() {
        return cardtype;
    }

    public void setCardtype(Byte cardtype) {
        this.cardtype = cardtype;
    }

    public Boolean getIsmaster() {
        return ismaster;
    }

    public void setIsmaster(Boolean ismaster) {
        this.ismaster = ismaster;
    }

    public Boolean getIsnopasswordpay() {
        return isnopasswordpay;
    }

    public void setIsnopasswordpay(Boolean isnopasswordpay) {
        this.isnopasswordpay = isnopasswordpay;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
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

    public Date getPayCodeTime() {
        return payCodeTime;
    }

    public void setPayCodeTime(Date payCodeTime) {
        this.payCodeTime = payCodeTime;
    }
}
