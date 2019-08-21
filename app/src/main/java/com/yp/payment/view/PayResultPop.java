package com.yp.payment.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.yp.payment.R;

/**
 * @author : cp
 * @email : ibsfiq@qq.com
 * @date : 2019/8/21 15:17
 * @description ：
 */
public class PayResultPop extends Dialog {
    public PayResultPop(Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_pay_result);
    }
}
