package com.yp.payment.order.layer.model;

/**
 * Created by Administrator on 2020/6/16.
 */

public class RequestCaiModel {

    /**
     * sql_id : D2MINI-COOKPLAN-001
     * params : {"shopId":"23"}
     * page_num : 1
     * page_size : 100
     */

    private String sql_id;
    private ParamsBean params;
    private int page_num;
    private int page_size;
    public String getSql_id() {
        return sql_id;
    }

    public void setSql_id(String sql_id) {
        this.sql_id = sql_id;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public int getPage_num() {
        return page_num;
    }

    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public static class ParamsBean {
        /**
         * shopId : 23
         */

        private String shopId;
        private String cashierDeskId;

        private int branchId;

        public int getBranchId() {
            return branchId;
        }

        public void setBranchId(int branchId) {
            this.branchId = branchId;
        }

        public String getCashierDeskId() {
            return cashierDeskId;
        }

        public void setCashierDeskId(String cashierDeskId) {
            this.cashierDeskId = cashierDeskId;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }
    }
}
