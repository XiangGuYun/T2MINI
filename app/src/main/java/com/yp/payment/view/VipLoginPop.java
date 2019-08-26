package com.yp.payment.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private static final String TAG = "VipLoginPop";
    Context context;

    public VipLoginPop(Context context) {
        super(context);
        this.context = context;
    }

    int curIndex = 0;
    TextView btn_login_phone, btn_login_qrcode, btn_login_card;
    ConstraintLayout pop_card_layout, pop_phone_layout, pop_qrcode_layout;
    EditText pop_intpu_edit_phone, pop_intpu_edit_card, pop_intpu_edit_qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window _window = getWindow();
        WindowManager.LayoutParams params = _window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        _window.setAttributes(params);
        setContentView(R.layout.pop_vip_login);
        btn_login_phone = findViewById(R.id.btn_login_phone);
        btn_login_qrcode = findViewById(R.id.btn_login_qrcode);
        btn_login_card = findViewById(R.id.btn_login_card);
        pop_phone_layout = findViewById(R.id.pop_phone_layout);
        pop_card_layout = findViewById(R.id.pop_card_layout);
        pop_qrcode_layout = findViewById(R.id.pop_qrcode_layout);
        pop_intpu_edit_phone = findViewById(R.id.pop_intpu_edit_phone);
        pop_intpu_edit_card = findViewById(R.id.pop_intpu_edit_card);
        pop_intpu_edit_qrcode = findViewById(R.id.pop_intpu_edit_qrcode);
        btn_login_phone.setOnClickListener(this);
        btn_login_qrcode.setOnClickListener(this);
        btn_login_card.setOnClickListener(this);
        findViewById(R.id.btn_sure_login).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);

        setCancelable(true);//点击外部取消
        setCanceledOnTouchOutside(true);
        showFramgent(curIndex);
    }

    @Override
    public void show() {
        if (btn_login_phone!=null){
            showFramgent(0);
        }
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
            case R.id.btn_sure_login:
                login();
                break;
            case R.id.btn_cancel:
                hide();
                break;
            default:
                break;
        }
    }

    void login() {
        if (curIndex == 0) {
            String phone = pop_intpu_edit_phone.getText().toString();
            Log.d(TAG, "login:   phone = " + phone);
        } else if (curIndex == 1) {
            String cardCode = pop_intpu_edit_card.getText().toString();
            Log.d(TAG, "login:   cardCode = " + cardCode);
        } else if (curIndex == 2) {
            String qrCode = pop_intpu_edit_qrcode.getText().toString();
            Log.d(TAG, "login:   qrCode = " + qrCode);
        }
    }

    public void setNfcCode(String nfcStr) {
        if (curIndex == 1) {
            pop_intpu_edit_card.setText(nfcStr);
        }
    }

    public void setQrCode(String qrCodeStr) {
        if (curIndex == 2) {
            pop_intpu_edit_qrcode.setText(qrCodeStr);
        }
    }

    public void clearText() {
        if (pop_intpu_edit_card!=null){
            pop_intpu_edit_card.setText("");
            pop_intpu_edit_phone.setText("");
            pop_intpu_edit_qrcode.setText("");
        }

    }

    public void showFramgent(int index) {
        curIndex = index;
        clearText();
        switch (index) {
            case 0:
                pop_phone_layout.setVisibility(View.VISIBLE);
                pop_card_layout.setVisibility(View.GONE);
                pop_qrcode_layout.setVisibility(View.GONE);

                break;
            case 1:
                pop_phone_layout.setVisibility(View.GONE);
                pop_card_layout.setVisibility(View.VISIBLE);
                pop_qrcode_layout.setVisibility(View.GONE);
                break;
            case 2:
                pop_phone_layout.setVisibility(View.GONE);
                pop_card_layout.setVisibility(View.GONE);
                pop_qrcode_layout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
