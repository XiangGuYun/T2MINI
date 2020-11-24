package com.yp.payment.order.layer.model;

import java.util.List;

/**
 * Created by Administrator on 2020/6/16.
 */

public class AllCaiTypeModel {

    /**
     * code : 200
     * message : SUCCESS
     * data : {"results":[{"deleted":0,"name":"热菜","id":32,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"素菜","id":33,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"主食","id":205,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"默认值","id":206,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"123","id":216,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"荤菜","id":321,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"111","id":322,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"333","id":426,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"555","id":427,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"666","id":428,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"888","id":429,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"777","id":430,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"主食","id":432,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"111","id":434,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"1111","id":440,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"111","id":457,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":467,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":468,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":469,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":470,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":471,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":472,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":473,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":474,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"11","id":475,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"凉菜","id":476,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"凉菜","id":477,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"凉菜","id":478,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"凉菜","id":481,"shopId":23,"type":1,"parentId":0}],"page_num":1,"page_size":100,"all_page_num":1,"record_count":29,"sql_name":"点餐-获取菜类","sql_id":"D2MINI-COOKPLAN-002"}
     */

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
        /**
         * results : [{"deleted":0,"name":"热菜","id":32,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"素菜","id":33,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"主食","id":205,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"默认值","id":206,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"123","id":216,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"荤菜","id":321,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"111","id":322,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"333","id":426,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"555","id":427,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"666","id":428,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"888","id":429,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"777","id":430,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"主食","id":432,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"111","id":434,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"1111","id":440,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"111","id":457,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":467,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":468,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":469,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":470,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":471,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":472,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":473,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"凉菜","id":474,"shopId":23,"type":1,"parentId":0},{"deleted":1,"name":"11","id":475,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"凉菜","id":476,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"凉菜","id":477,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"凉菜","id":478,"shopId":23,"type":1,"parentId":0},{"deleted":0,"name":"凉菜","id":481,"shopId":23,"type":1,"parentId":0}]
         * page_num : 1
         * page_size : 100
         * all_page_num : 1
         * record_count : 29
         * sql_name : 点餐-获取菜类
         * sql_id : D2MINI-COOKPLAN-002
         */

        private int page_num;
        private int page_size;
        private int all_page_num;
        private int record_count;
        private String sql_name;
        private String sql_id;
        private List<ResultsBean> results;

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

        public int getAll_page_num() {
            return all_page_num;
        }

        public void setAll_page_num(int all_page_num) {
            this.all_page_num = all_page_num;
        }

        public int getRecord_count() {
            return record_count;
        }

        public void setRecord_count(int record_count) {
            this.record_count = record_count;
        }

        public String getSql_name() {
            return sql_name;
        }

        public void setSql_name(String sql_name) {
            this.sql_name = sql_name;
        }

        public String getSql_id() {
            return sql_id;
        }

        public void setSql_id(String sql_id) {
            this.sql_id = sql_id;
        }

        public List<ResultsBean> getResults() {
            return results;
        }

        public void setResults(List<ResultsBean> results) {
            this.results = results;
        }

        public static class ResultsBean {
            /**
             * deleted : 0
             * name : 热菜
             * id : 32
             * shopId : 23
             * type : 1
             * parentId : 0
             */

            private int deleted;
            private String name;
            private int id;
            private int shopId;
            private int type;
            private int parentId;
            private List<AllCaiModel.DataBean.ResultsBean> mList;

            public List<AllCaiModel.DataBean.ResultsBean> getmList() {
                return mList;
            }

            public void setmList(List<AllCaiModel.DataBean.ResultsBean> mList) {
                this.mList = mList;
            }

            public int getDeleted() {
                return deleted;
            }

            public void setDeleted(int deleted) {
                this.deleted = deleted;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getShopId() {
                return shopId;
            }

            public void setShopId(int shopId) {
                this.shopId = shopId;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }
        }
    }
}
