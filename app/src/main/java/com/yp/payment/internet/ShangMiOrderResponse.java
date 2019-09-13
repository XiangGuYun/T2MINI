package com.yp.payment.internet;

import java.util.List;

public class ShangMiOrderResponse {

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String orderNo;
        private String productOrderUrl;
        private String serialNumber;
        private Integer serial;
        private Integer shopId;
        private String shopName;
        private Integer cashierDeskId;
        private Integer customerId;
        private String customerName;
        private String customerPhone;
        private Byte payType;
        private Long totalFee;
        private Long realFee;
        private Long refundFee;
        private Byte payStatus;
        private Byte refundStatus;
        private String note;
        private Long payTime;
        private Long refundTime;
        private Integer itemCount;
        private List<OrderItemVO> orderItems;
        private String qrCode;
        private PayCard payCard;
        private Integer isSuccee;

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

        public Integer getSerial() {
            return serial;
        }

        public void setSerial(Integer serial) {
            this.serial = serial;
        }

        public Integer getShopId() {
            return shopId;
        }

        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public Integer getCashierDeskId() {
            return cashierDeskId;
        }

        public void setCashierDeskId(Integer cashierDeskId) {
            this.cashierDeskId = cashierDeskId;
        }

        public Integer getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Integer customerId) {
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

        public Byte getPayType() {
            return payType;
        }

        public void setPayType(Byte payType) {
            this.payType = payType;
        }

        public Long getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(Long totalFee) {
            this.totalFee = totalFee;
        }

        public Long getRealFee() {
            return realFee;
        }

        public void setRealFee(Long realFee) {
            this.realFee = realFee;
        }

        public Long getRefundFee() {
            return refundFee;
        }

        public void setRefundFee(Long refundFee) {
            this.refundFee = refundFee;
        }

        public Byte getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(Byte payStatus) {
            this.payStatus = payStatus;
        }

        public Byte getRefundStatus() {
            return refundStatus;
        }

        public void setRefundStatus(Byte refundStatus) {
            this.refundStatus = refundStatus;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public Long getPayTime() {
            return payTime;
        }

        public void setPayTime(Long payTime) {
            this.payTime = payTime;
        }

        public Long getRefundTime() {
            return refundTime;
        }

        public void setRefundTime(Long refundTime) {
            this.refundTime = refundTime;
        }

        public Integer getItemCount() {
            return itemCount;
        }

        public void setItemCount(Integer itemCount) {
            this.itemCount = itemCount;
        }

        public List<OrderItemVO> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<OrderItemVO> orderItems) {
            this.orderItems = orderItems;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public PayCard getPayCard() {
            return payCard;
        }

        public void setPayCard(PayCard payCard) {
            this.payCard = payCard;
        }

        public Integer getIsSuccee() {
            return isSuccee;
        }

        public void setIsSuccee(Integer isSuccee) {
            this.isSuccee = isSuccee;
        }
    }
}
