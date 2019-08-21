package com.yp.payment.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.yp.payment.R;
import com.yp.payment.adapter.CashModeAdapter;
import com.yp.payment.input.KeyBoardCallback;
import com.yp.payment.interfaces.RecommendPriceCallback;
import com.yp.payment.interfaces.SettlementCallback;
import com.yp.payment.view.VirtualKeyboardView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cp
 * @email : ibsfiq@qq.com
 * @date : 2019/8/21 14:27
 * @description ：
 */
public class KeyboardManage implements KeyBoardCallback, RecommendPriceCallback {
    private static final String TAG = "KeyboardManage";
    VirtualKeyboardView virtualKeyboardView;
    EditText tv_input_cash;
    SettlementCallback settlementCallback;
    RecyclerView cash_recycler_view;

    /**
     * 结算价钱推荐
     */
    CashModeAdapter cashModeAdapter;

    public KeyboardManage(Context context, View rootView, SettlementCallback settlementCallback) {
        this.settlementCallback = settlementCallback;
        cashModeAdapter = new CashModeAdapter(context, this);
        rootView.findViewById(R.id.tv_over).setOnClickListener(onClickListener);
        tv_input_cash = rootView.findViewById(R.id.tv_input_cash);
        virtualKeyboardView = rootView.findViewById(R.id.virtualKeyboardView);
        cash_recycler_view = rootView.findViewById(R.id.cash_recycler_view);
        cash_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        cash_recycler_view.setAdapter(cashModeAdapter);
        solveInputData();
    }

    /**
     * 结算
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                double price = Double.parseDouble(tv_input_cash.getText().toString());
                settlementCallback.onSettlementClick(price);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * 是否键盘输入
     */
    boolean autoSetting = true;

    public void solveInputData() {
        virtualKeyboardView.setKeyBoardCallback(this);
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
    public void priceCallback(int price) {
        tv_input_cash.setText(String.valueOf(price));
    }
}
