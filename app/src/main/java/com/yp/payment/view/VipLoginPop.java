package com.yp.payment.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yp.payment.R;
import com.yp.payment.adapter.PopVipLoginAdapter;
import com.yp.payment.fragment.CardVipLoginFragment;
import com.yp.payment.fragment.PhoneVipLoginFragment;
import com.yp.payment.fragment.ScanVipLoginFragment;

public class VipLoginPop extends Dialog implements View.OnClickListener {
    Context context;

    public VipLoginPop(Context context) {
        super(context);
        this.context = context;
    }

    TextView btn_login_phone, btn_login_qrcode, btn_login_card;
    PopVipLoginAdapter popVipLoginAdapter;
    ViewPager pop_viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window _window = getWindow();
        WindowManager.LayoutParams params = _window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        _window.setAttributes(params);
        setContentView(R.layout.pop_vip_login);
        pop_viewpager = findViewById(R.id.pop_viewpager);
        btn_login_phone = findViewById(R.id.btn_login_phone);
        btn_login_qrcode = findViewById(R.id.btn_login_qrcode);
        btn_login_card = findViewById(R.id.btn_login_card);
        btn_login_phone.setOnClickListener(this);
        btn_login_qrcode.setOnClickListener(this);
        btn_login_card.setOnClickListener(this);

        setCancelable(true);//点击外部取消
        setCanceledOnTouchOutside(true);
//        pop_intpu_edit = findViewById(R.id.pop_intpu_edit);
//        pop_intpu_edit.setShowSoftInputOnFocus(false);
//        setOnDismissListener(onDismissListener);
//        pop_intpu_edit.requestFocus();

        popVipLoginAdapter = new PopVipLoginAdapter(((AppCompatActivity) context).getSupportFragmentManager());
//        pop_viewpager.setAdapter(popVipLoginAdapter);
        showFramgent(0);
    }

    OnDismissListener onDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
//            pop_intpu_edit.setFocusable(false);
        }
    };

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_phone:
                btn_login_phone.setTextColor(getContext().getResources().getColor(R.color.pop_select_text));
                btn_login_card.setTextColor(getContext().getResources().getColor(R.color.color_999));
                btn_login_qrcode.setTextColor(getContext().getResources().getColor(R.color.color_999));
                btn_login_phone.setBackgroundResource(R.color.pop_select_bg);
                btn_login_card.setBackgroundResource(R.color.transparent);
                btn_login_qrcode.setBackgroundResource(R.color.transparent);
                showFramgent(0);
                break;
            case R.id.btn_login_card:
                btn_login_phone.setTextColor(getContext().getResources().getColor(R.color.color_999));
                btn_login_card.setTextColor(getContext().getResources().getColor(R.color.pop_select_text));
                btn_login_qrcode.setTextColor(getContext().getResources().getColor(R.color.color_999));
                btn_login_phone.setBackgroundResource(R.color.transparent);
                btn_login_card.setBackgroundResource(R.color.pop_select_bg);
                btn_login_qrcode.setBackgroundResource(R.color.transparent);
                showFramgent(1);
                break;
            case R.id.btn_login_qrcode:
                btn_login_phone.setTextColor(getContext().getResources().getColor(R.color.color_999));
                btn_login_card.setTextColor(getContext().getResources().getColor(R.color.color_999));
                btn_login_qrcode.setTextColor(getContext().getResources().getColor(R.color.pop_select_text));
                btn_login_phone.setBackgroundResource(R.color.transparent);
                btn_login_card.setBackgroundResource(R.color.transparent);
                btn_login_qrcode.setBackgroundResource(R.color.pop_select_bg);
                showFramgent(2);
                break;
        }
    }

    int lastIndex = -1;

    public void showFramgent(int index) {
//        pop_viewpager.setCurrentItem(index);
//        FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
//        if (lastIndex == 0) {
//            fragmentTransaction.hide(phoneVipLoginFragment);
//        } else if (lastIndex == 1) {
//            fragmentTransaction.hide(cardVipLoginFragment);
//        } else if (lastIndex == 2) {
//            fragmentTransaction.hide(scanVipLoginFragment);
//        }
//        if (index == 0) {
//            if (phoneVipLoginFragment == null) {
//                phoneVipLoginFragment = new PhoneVipLoginFragment();
//                fragmentTransaction.add(R.id.pop_framelayout, phoneVipLoginFragment);
//            }
//            fragmentTransaction.show(phoneVipLoginFragment);
////            fragmentTransaction.replace(R.id.pop_framelayout, phoneVipLoginFragment);
//        } else if (index == 1) {
//            if (cardVipLoginFragment == null) {
//                cardVipLoginFragment = new CardVipLoginFragment();
//            }
//            fragmentTransaction.replace(R.id.pop_framelayout, cardVipLoginFragment);
//        } else if (index == 2) {
//            if (scanVipLoginFragment == null) {
//                scanVipLoginFragment = new ScanVipLoginFragment();
//            }
//            fragmentTransaction.replace(R.id.pop_framelayout, scanVipLoginFragment);
//        }
//        fragmentTransaction.commit();

    }
}
