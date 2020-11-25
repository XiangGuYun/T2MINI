package com.yp.payment.jc_internet;

import java.util.List;

/**
 * Created by 20191024 on 2020/1/6.
 */

public class OrderCountModel {

    /**
     * code : 200
     * message : SUCCESS
     * data : [{"realFee":null,"refundFee":null,"count":17,"moneyStr":"317.00","payTime":null,"type":4},{"realFee":null,"refundFee":null,"count":4,"moneyStr":"31.00","payTime":null,"type":1},{"realFee":null,"refundFee":null,"count":0,"moneyStr":"0.00","payTime":null,"type":2},{"realFee":null,"refundFee":null,"count":4,"moneyStr":"31.00","payTime":null,"type":3}]
     */

    private int code;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * realFee : null
         * refundFee : null
         * count : 17
         * moneyStr : 317.00
         * payTime : null
         * type : 4
         */

        private Object realFee;
        private Object refundFee;
        private int count;
        private String moneyStr;
        private Object payTime;
        private int type;

        public Object getRealFee() {
            return realFee;
        }

        public void setRealFee(Object realFee) {
            this.realFee = realFee;
        }

        public Object getRefundFee() {
            return refundFee;
        }

        public void setRefundFee(Object refundFee) {
            this.refundFee = refundFee;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getMoneyStr() {
            return moneyStr;
        }

        public void setMoneyStr(String moneyStr) {
            this.moneyStr = moneyStr;
        }

        public Object getPayTime() {
            return payTime;
        }

        public void setPayTime(Object payTime) {
            this.payTime = payTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
