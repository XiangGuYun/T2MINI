package com.yp.payment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yp.payment.adapter.OrderListAdapter;
import com.yp.payment.adapter.PayModeAdapter;
import com.yp.payment.interfaces.PayModeCallback;
import com.yp.payment.interfaces.SettlementCallback;
import com.yp.payment.utils.KeyboardManage;
import com.yp.payment.view.PayResultPop;
import com.yp.payment.view.VipLoginPop;

import java.text.DecimalFormat;

public class MoneyActivity extends BaseActivity implements View.OnClickListener, PayModeCallback, SettlementCallback {
    private static final String TAG = "MoneyActivity";

    RecyclerView pay_recycler_view;
    RecyclerView order_recyclerview;


    /**
     * 支付方式,默认现金
     */
    int payMode = 0;
    PayModeAdapter payModeAdapter;


    /**
     * 会员登录
     */
    VipLoginPop vipLoginPop;
    TextView tv_vip_num, tv_vip_phone;
    /**
     * 结算弹窗
     */
    PayResultPop payResultPop;

    @Override
    public int layoutId() {
        return R.layout.activity_pay_page;
    }

    @Override
    public void initView() {
        hidekeyboard();
        initOrderList();
        initKeyboard();
        initPayMode();
        vipLoginPop = new VipLoginPop(this);
        payResultPop = new PayResultPop(this);
        initVipModel();
    }

    void initVipModel() {
        findViewById(R.id.btn_vip_login).setOnClickListener(this);
        tv_vip_num = findViewById(R.id.tv_vip_num);
        tv_vip_phone = findViewById(R.id.tv_vip_phone);
    }

    @Override
    public void initData() {

    }

    void hidekeyboard() {
        Window _window = getWindow();
        WindowManager.LayoutParams params = _window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        _window.setAttributes(params);
    }

    /**
     * 历史订单列表
     */
    void initOrderList() {
        order_recyclerview = findViewById(R.id.order_recyclerview);
        order_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        OrderListAdapter orderListAdapter = new OrderListAdapter(this);
        order_recyclerview.setAdapter(orderListAdapter);
    }

    /**
     * 支付方式
     */
    void initPayMode() {
        pay_recycler_view = findViewById(R.id.pay_recycler_view);
        pay_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        payModeAdapter = new PayModeAdapter(this, this);
        pay_recycler_view.setAdapter(payModeAdapter);
    }

    /**
     * 键盘输入
     */
    void initKeyboard() {
        new KeyboardManage(this, findViewById(R.id.rootView), this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_vip_login:
                vipLoginPop.show();
                break;
            default:
                break;
        }
    }


    @Override
    public void onSelectedMode(int mode) {
        payMode = mode;
    }

    @Override
    public void onSettlementClick(double price) {
        DecimalFormat df = new DecimalFormat("#0.00");
        showToast("使用 " + Consts.payModes[payMode] + " 支付了 " +df.format(price) );
        payResultPop.show();
    }

    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("#0.00");
        System.out.println(df.format(.2));
    }
}
