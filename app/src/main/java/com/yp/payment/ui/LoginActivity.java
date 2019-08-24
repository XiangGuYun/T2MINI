package com.yp.payment.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.yp.payment.BaseActivity;
import com.yp.payment.MainActivity;
import com.yp.payment.R;
import com.yp.payment.MoneyActivity;

/**
 * @author : cp
 * @email : ibsfiq@qq.com
 * @date : 2019/8/21 14:07
 * @description ：管理员登录页
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    EditText edit_user_account, edit_user_psw;

    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION};
    final private int REQUEST_PERMISSION_CODE = 20001;

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
        checkBluetoothPermission();
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

    /*
   校验蓝牙权限
  */
    private void checkBluetoothPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else if (ContextCompat.checkSelfPermission(LoginActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else {
                Log.d(TAG, "无需授权  ");
            }
        } else {//小于23版本直接使用
            Log.d(TAG, "checkBluetoothPermission: 小于23版本直接使用");
        }
    }

}
