package com.yp.payment.order.layer;

import com.alibaba.fastjson.JSONObject;
import com.android.tu.loadingdialog.LoadingDailog;
import com.google.gson.Gson;
import com.yp.payment.jc_internet.ApkConstant;
import com.yp.payment.jc_internet.HttpCallBack;
import com.yp.payment.jc_internet.NetWorkUtils;
import com.yp.payment.jc_internet.OrderCountModel;
import com.yp.payment.order.layer.model.AllCaiModel;
import com.yp.payment.order.layer.model.AllCaiTypeModel;
import com.yp.payment.order.layer.model.RequestCaiModel;
import com.yp.payment.order.layer.utils.ToastUtils;
import com.yp.payment.update.SPHelper;

import org.xutils.http.RequestParams;

import java.util.List;

/**
 * 管理所有的网络请求
 */
public class Req {

    /**
     * 请求所有菜品类型
     * @param isShowDialog
     * @param callback
     * @param callbackReqDishes
     */
    public static void reqDishesTypes(boolean isShowDialog, CallbackReqDishesTypes callback, CallbackReqDishes callbackReqDishes){
        RequestCaiModel requestCaiModel = new RequestCaiModel();
        requestCaiModel.setPage_num(1);
        requestCaiModel.setPage_size(100);
        requestCaiModel.setSql_id("T2MINI-COOKPLAN-002");
        RequestCaiModel.ParamsBean paramsBean = new RequestCaiModel.ParamsBean();
        paramsBean.setShopId(SPHelper.getShopId()+"");
        paramsBean.setCashierDeskId(SPHelper.getCashierDeskId()+"");
        paramsBean.setBranchId(SPHelper.getBranchId());
        requestCaiModel.setParams(paramsBean);

        RequestParams params = new RequestParams(ApkConstant.ISQLQUERY);
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(requestCaiModel));
        NetWorkUtils.postHttpRequest(isShowDialog, params, new HttpCallBack.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                AllCaiTypeModel allCaiTypeModel = JSONObject.parseObject(result,AllCaiTypeModel.class);
                if (allCaiTypeModel.getCode() == 200){
                    List<AllCaiTypeModel.DataBean.ResultsBean> resultsBeanList = allCaiTypeModel.getData().getResults();
//                    bindAllCaiTypeData(resultsBeanList);
                    callback.callback(resultsBeanList);
                }else{
                    ToastUtils.toast(allCaiTypeModel.getMessage());
                }
                Req.reqDishes(callbackReqDishes);
            }

            @Override
            public void onCancelled(String msg) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 请求所有的菜品数据
     */
    public static void reqDishes(CallbackReqDishes callback) {
        RequestCaiModel requestCaiModel = new RequestCaiModel();
        requestCaiModel.setPage_num(1);
        requestCaiModel.setPage_size(100);
        requestCaiModel.setSql_id("T2MINI-COOKPLAN-001");
        RequestCaiModel.ParamsBean paramsBean = new RequestCaiModel.ParamsBean();
        paramsBean.setShopId(SPHelper.getShopId()+"");
        paramsBean.setCashierDeskId(SPHelper.getCashierDeskId()+"");
        paramsBean.setBranchId(SPHelper.getBranchId());
        requestCaiModel.setParams(paramsBean);

        RequestParams params = new RequestParams(ApkConstant.ISQLQUERY);
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(requestCaiModel));
        System.out.println("菜谱："+new Gson().toJson(requestCaiModel));
        NetWorkUtils.postHttpRequest(true, params, new HttpCallBack.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                System.out.println("res:"+result);
                AllCaiModel allCaiModel = JSONObject.parseObject(result,AllCaiModel.class);
                if (allCaiModel.getCode() == 200){
                    List<AllCaiModel.DataBean.ResultsBean> resultsBeanList = allCaiModel.getData().getResults();
                    System.out.println("resultsBeanList:"+resultsBeanList);
                    if (resultsBeanList == null){
                        return;
                    }
                    callback.callback(resultsBeanList);
                }else{
                    ToastUtils.toast(allCaiModel.getMessage());
                }
            }

            @Override
            public void onCancelled(String msg) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 请求订单数量
     */
    public static void reqOrderCount(LoadingDailog loadingDailog, CallbackReqOrderCount callbackReqOrderCount) {
        RequestParams params = new RequestParams(ApkConstant.GETSHANGMIORDER+ SPHelper.getCashierDeskId());
        params.setAsJsonContent(true);
        params.setBodyContent("");

        loadingDailog.show();
        NetWorkUtils.getHttpRequest(false,params, new HttpCallBack.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                OrderCountModel orderCountModel = JSONObject.parseObject(result, OrderCountModel.class);
                if (orderCountModel.getCode() == 200){
                    callbackReqOrderCount.callback(orderCountModel.getData());
                }else{
                    ToastUtils.toast(orderCountModel.getMessage());
                }
            }

            @Override
            public void onCancelled(String msg) {

            }

            @Override
            public void onError(String error) {

            }
            @Override
            public void onFinish() {
                if (loadingDailog != null){
                    if (loadingDailog.isShowing()){
                        loadingDailog.dismiss();
                    }
                }
            }
        });
    }

    public interface CallbackReqOrderCount{
        void callback(List<OrderCountModel.DataBean> list);
    }

    public interface CallbackReqDishes{
        void callback(List<AllCaiModel.DataBean.ResultsBean> list);
    }

    public interface CallbackReqDishesTypes{
        void callback(List<AllCaiTypeModel.DataBean.ResultsBean> list);
    }

}
