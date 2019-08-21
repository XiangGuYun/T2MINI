package com.yp.payment.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.yp.payment.BaseActivity;
import com.yp.payment.R;
import com.yp.payment.MoneyActivity;

/**
 * @author : cp
 * @email : ibsfiq@qq.com
 * @date : 2019/8/21 14:07
 * @description ：管理员登录页
 */
public class LoginActivity extends BaseActivity {

    EditText edit_user_account, edit_user_psw;

    @Override
    public int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        edit_user_account = findViewById(R.id.edit_user_account);
        edit_user_psw = findViewById(R.id.edit_user_psw);
        findViewById(R.id.btn_login_user).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loginAdmin();
        }
    };

    void loginAdmin() {
        String user_account = edit_user_account.getText().toString().trim();
        String user_psw = edit_user_psw.getText().toString().trim();
        if (TextUtils.isEmpty(user_account)) {
            showToast(getResString(R.string.invalid_account));
            return;
        } else if (TextUtils.isEmpty(user_psw)) {
            showToast(getResString(R.string.invalid_psw));
            return;
        }
        openActivity(MoneyActivity.class);
        finish();
    }

    @Override
    public void initData() {

    }
}
