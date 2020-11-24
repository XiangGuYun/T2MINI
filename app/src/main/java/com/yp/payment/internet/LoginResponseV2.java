package com.yp.payment.internet;

import java.util.List;

public class LoginResponseV2 {
    private int code;
    private int pageCount;
    private int pageSize;
    private String message;

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        private Integer shopId;
        private Integer cashierDeskId;
        private Integer cashAllow;
        private Integer branchId;
        private List<BranchVO> branchList;
        private String shopName;

        public Integer getShopId() {
            return shopId;
        }

        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }

        public Integer getCashierDeskId() {
            return cashierDeskId;
        }

        public void setCashierDeskId(Integer cashierDeskId) {
            this.cashierDeskId = cashierDeskId;
        }

        public Integer getCashAllow() {
            return cashAllow;
        }

        public void setCashAllow(Integer cashAllow) {
            this.cashAllow = cashAllow;
        }

        public Integer getBranchId() {
            return branchId;
        }

        public void setBranchId(Integer branchId) {
            this.branchId = branchId;
        }

        public List<BranchVO> getBranchList() {
            return branchList;
        }

        public void setBranchList(List<BranchVO> branchList) {
            this.branchList = branchList;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "shopId=" + shopId +
                    ", cashierDeskId=" + cashierDeskId +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "LoginResponse{" +
                "code=" + code +
                ", pageCount=" + pageCount +
                ", pageSize=" + pageSize +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
