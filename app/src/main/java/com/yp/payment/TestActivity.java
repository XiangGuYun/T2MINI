package com.yp.payment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.yp.payment.adapter.CashModeAdapter;
import com.yp.payment.adapter.OrderListAdapter;
import com.yp.payment.adapter.PayModeAdapter;
import com.yp.payment.adapter.PopVipLoginAdapter;
import com.yp.payment.input.KeyBoardCallback;
import com.yp.payment.interfaces.PayModeCallback;
import com.yp.payment.interfaces.RecommendPriceCallback;
import com.yp.payment.view.VipLoginPop;
import com.yp.payment.view.VirtualKeyboardView;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity implements KeyBoardCallback, View.OnClickListener, RecommendPriceCallback, PayModeCallback {
    private static final String TAG = "TestActivity";

    RecyclerView pay_recycler_view;
    RecyclerView cash_recycler_view;
    RecyclerView order_recyclerview;
    EditText tv_input_cash;
    private VirtualKeyboardView virtualKeyboardView;
    /**
     * 是否键盘输入
     */
    boolean autoSetting = true;
    /**
     * 支付方式,默认现金
     */
    int payMode = 0;
    PayModeAdapter payModeAdapter;

    /**
     * 结算价钱推荐
     */
    CashModeAdapter cashModeAdapter;

    /**
     * 会员登录
     */
    VipLoginPop vipLoginPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_page);
        Window _window = getWindow();
        WindowManager.LayoutParams params = _window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        _window.setAttributes(params);


        vipLoginPop = new VipLoginPop(this);

        tv_input_cash = findViewById(R.id.tv_input_cash);
        virtualKeyboardView = findViewById(R.id.virtualKeyboardView);
        virtualKeyboardView.setKeyBoardCallback(this);

        findViewById(R.id.tv_over).setOnClickListener(this);
        findViewById(R.id.btn_vip_login).setOnClickListener(this);

        order_recyclerview = findViewById(R.id.order_recyclerview);
        order_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        OrderListAdapter orderListAdapter = new OrderListAdapter(this);
        order_recyclerview.setAdapter(orderListAdapter);


        pay_recycler_view = findViewById(R.id.pay_recycler_view);
        pay_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        payModeAdapter = new PayModeAdapter(this, this);
        pay_recycler_view.setAdapter(payModeAdapter);

        cash_recycler_view = findViewById(R.id.cash_recycler_view);
        cash_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        cashModeAdapter = new CashModeAdapter(this, this);
        cash_recycler_view.setAdapter(cashModeAdapter);

        virtualKeyboardView = findViewById(R.id.virtualKeyboardView);

        tv_input_cash.setShowSoftInputOnFocus(false);
        tv_input_cash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!autoSetting) {
                    autoSetting = true;
                    return;
                }
                String str = tv_input_cash.getText().toString();
                Log.d(TAG, "onTextChanged: " + str);
                if (TextUtils.isEmpty(str)) {
                    autoSetting = false;

                    tv_input_cash.setText("0");
                    tv_input_cash.setSelection(tv_input_cash.getText().length());
                } else if (str.startsWith("00")) {
                    autoSetting = false;

                    tv_input_cash.setText("0");
                    tv_input_cash.setSelection(tv_input_cash.getText().length());
                } else if (str.startsWith("0") && str.length() > 1 && !str.startsWith("0.")) {
                    autoSetting = false;
                    tv_input_cash.setText(str.substring(1, str.length()));
                    tv_input_cash.setSelection(tv_input_cash.getText().length());
                } else if (str.startsWith(".")) {
                    autoSetting = false;

                    tv_input_cash.setText("0.");
                    tv_input_cash.setSelection(tv_input_cash.getText().length());
                } else if (str.endsWith(".")) {
                    String tempStr = str.substring(0, str.lastIndexOf("."));
                    Log.d(TAG, "onTextChanged:  tempStr =  " + tempStr);
                    if (tempStr.contains(".")) {
                        autoSetting = false;

                        tv_input_cash.setText(tempStr);
                        tv_input_cash.setSelection(tv_input_cash.getText().length());
                    }
                } else if (str.startsWith("0.")) {

                    if (str.lastIndexOf(".") < str.length() - 3) {
                        String tempStr = str.substring(0, str.lastIndexOf(".") + 3);
                        autoSetting = false;
                        tv_input_cash.setText(tempStr);
                        tv_input_cash.setSelection(tv_input_cash.getText().length());
                    }
                } else if (str.contains(".")) {
                    if (str.lastIndexOf(".") < str.length() - 3) {
                        autoSetting = false;

                        String tempStr = str.substring(0, str.lastIndexOf(".") + 3);
                        tv_input_cash.setText(tempStr);
                        tv_input_cash.setSelection(tv_input_cash.getText().length());
                    }
                }

                Log.d(TAG, "onTextChanged: " + str);
                recommend();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    public void recommend() {
        String str = tv_input_cash.getText().toString();
        int priceInt = 0;
        List<Integer> prices = null;
        try {
            if (str.contains(".")) {
                double priceDouble = Double.parseDouble(str);
                priceInt = (int) priceDouble;
//                Log.d(TAG, "recommend:  toInt  =  " + priceInt);
                if (priceDouble > 0 && priceInt == 0) {
                    //零点几块钱
                    prices = compayList(1);
                    cashModeAdapter.setMlist(prices);
                    return;
                }
            } else {
                priceInt = Integer.parseInt(str);
            }
            prices = compayList(priceInt);
            cashModeAdapter.setMlist(prices);
//            Log.d(TAG, "recommend: ======================   ");
//            Log.d(TAG, "recommend: 当前价钱 = " + str);
//            for (int i = 0; i < prices.size(); i++) {
//                Log.d(TAG, "recommend: 推荐的价钱是 = " + prices.get(i));
//            }
//            Log.d(TAG, "recommend: ======================   ");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public int rmbPrices[] = {5, 10, 20, 50, 100};

    public List<Integer> compayList(int price) {
        List<Integer> recommendList = new ArrayList<>();
        for (int i = 0; i < rmbPrices.length; i++) {
            int a = price / rmbPrices[i];
            int b = price % rmbPrices[i];
            if (b != 0) {
                int recPrice = (a + 1) * rmbPrices[i];
                if (!recommendList.contains(recPrice)) {
                    recommendList.add(recPrice);
                }
            }
        }
        return recommendList;
    }

    @Override
    public void OnKeyCallback() {
        if (tv_input_cash.hasFocus()) {
            tv_input_cash.setSelection(tv_input_cash.getText().length());
        } else {
            tv_input_cash.requestFocus();//获取焦点 光标出现
            tv_input_cash.setSelection(tv_input_cash.getText().length());
        }
    }

    @Override
    public void clearAllData() {
        tv_input_cash.setText("0");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_over:
                showToast("结算");
                break;
            case R.id.btn_vip_login:
                vipLoginPop.show();
                break;
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void priceCallback(int price) {
        tv_input_cash.setText(String.valueOf(price));
    }

    @Override
    public void onSelectedMode(int mode) {
        payMode = mode;
    }
}
