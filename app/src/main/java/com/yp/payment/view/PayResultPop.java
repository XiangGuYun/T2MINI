package com.yp.payment.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yp.payment.R;

import java.text.DecimalFormat;

/**
 * @author : cp
 * @email : ibsfiq@qq.com
 * @date : 2019/8/21 15:17
 * @description ：
 */
public class PayResultPop extends Dialog implements View.OnClickListener {
    public PayResultPop(Context context) {
        super(context);
    }

    TextView tv_title;
    TextView tv_used_price_title, tv_used_price;
    TextView tv_balance_title, tv_balance;
    TextView tv_small_change_title, tv_small_change;
    DecimalFormat df = new DecimalFormat("#0.00");
    /**
     * 支付方式
     */
    int payMode;
    /**
     * 应付价钱
     */
    double price;

    /**
     * 现金：实际给的；会员卡：卡内余额
     */
    double commPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_pay_result);
        tv_used_price_title = findViewById(R.id.tv_used_price_title);
        tv_used_price = findViewById(R.id.tv_used_price);
        tv_balance_title = findViewById(R.id.tv_balance_title);
        tv_balance = findViewById(R.id.tv_balance);
        tv_small_change_title = findViewById(R.id.tv_small_change_title);
        tv_small_change = findViewById(R.id.tv_small_change);
        tv_title = findViewById(R.id.tv_title);

        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_sure_pay).setOnClickListener(this);
        setPriceData();
    }

    public void showPop(int payMode, double price, double commPrice) {
        this.payMode = payMode;
        this.price = price;
        this.commPrice = commPrice;
        show();
        setPriceData();
    }

    public void setPriceData() {
        if (tv_title == null) return;
        switch (payMode) {
            case 0:
                tv_title.setText("现金支付");
                tv_used_price_title.setText("应付：");
                tv_balance_title.setText("实付：");
                tv_small_change_title.setText("找零：");
                tv_used_price.setText(df.format(price));
                tv_balance.setText(df.format(commPrice));
                tv_small_change.setText(df.format(commPrice - price));
                tv_balance_title.setVisibility(View.VISIBLE);
                tv_balance.setVisibility(View.VISIBLE);
                tv_small_change.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_title.setText("NFC卡支付");
                tv_used_price_title.setText("应付：");
                tv_used_price.setText(df.format(price));
                tv_small_change_title.setText("正等待客户刷卡…");

                tv_balance_title.setVisibility(View.GONE);
                tv_balance.setVisibility(View.GONE);
                tv_small_change.setVisibility(View.GONE);
                break;
            case 2:
                tv_title.setText("支付宝支付");
                tv_small_change_title.setText("正等待客户扫码…");
                tv_used_price.setText(df.format(price));


                tv_balance_title.setVisibility(View.GONE);
                tv_balance.setVisibility(View.GONE);
                tv_small_change.setVisibility(View.GONE);
                break;
            case 3:
                tv_title.setText("微信支付");
                tv_small_change_title.setText("正等待客户扫码…");
                tv_used_price.setText(df.format(price));


                tv_balance_title.setVisibility(View.GONE);
                tv_balance.setVisibility(View.GONE);
                tv_small_change.setVisibility(View.GONE);
                break;
            case 4:
                tv_title.setText("储值支付");
                tv_used_price_title.setText("应付：");
                tv_balance_title.setText("卡内余额：");
                tv_small_change_title.setText("支付后余额：");
                tv_used_price.setText(df.format(price));
                tv_balance.setText(df.format(commPrice));
                tv_small_change.setText(df.format(commPrice - price));

                tv_balance_title.setVisibility(View.VISIBLE);
                tv_balance.setVisibility(View.VISIBLE);
                tv_small_change.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                break;
            case R.id.btn_sure_pay:
                break;
        }
    }
}
