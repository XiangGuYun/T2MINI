package com.yp.payment.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yp.baselib.helper.NetListenHelper;
import com.yp.baselib.utils.NetUtils;
import com.yp.payment.Constant;
import com.yp.payment.Consts;
import com.yp.payment.R;
import com.yp.payment.dao.ShopConfigDao;
import com.yp.payment.http.MyCallback;
import com.yp.payment.internet.LoginRequest;
import com.yp.payment.internet.LoginResponse;
import com.yp.payment.internet.LoginResponseV2;
import com.yp.payment.internet.MyRetrofit;
import com.yp.payment.model.ShopConfig;
import com.yp.payment.utils.GsonUtil;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;

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

    public static String deviceId;

    public ShopConfigDao shopConfigDao;
    private NetListenHelper netHelper;

    @Override
    public int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        edit_user_account = findViewById(R.id.edit_user_account);
        edit_user_psw = findViewById(R.id.edit_user_psw);
        findViewById(R.id.btn_login_user).setOnClickListener(v->loginAdmin());

        netHelper = new NetListenHelper();
        netHelper.register(this,
                integer -> {
                    edit_user_account.setVisibility(View.VISIBLE);
                    edit_user_psw.setVisibility(View.VISIBLE);
                    return null;
                },
                () -> {
                    edit_user_account.setVisibility(View.INVISIBLE);
                    edit_user_psw.setVisibility(View.INVISIBLE);
                    ((Button)findViewById(R.id.btn_login_user)).setText("离线登录");
                    return null;
                }
                );

        checkBluetoothPermission();

        deviceId = android.os.Build.SERIAL;
    }

    void loginAdmin() {
        String user_account = edit_user_account.getText().toString().trim();
        String user_psw = edit_user_psw.getText().toString().trim();
        /*if (TextUtils.isEmpty(user_account)) {
            showToast(getResString(R.string.invalid_account));
            return;
        } else if (TextUtils.isEmpty(user_psw)) {
            showToast(getResString(R.string.invalid_psw));
            return;
        }*/

        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDeviceId(LoginActivity.deviceId);
        loginRequest.setUsername(user_account);
        loginRequest.setPassword(user_psw);
//        loginRequest.setUsername("18312345678");
//        loginRequest.setPassword("000000");

        Log.d(TAG, "loginRequest==" + GsonUtil.GsonString(loginRequest));

        /*MyRetrofit.getApiService().init(loginRequest).enqueue(new MyCallback<LoginResponse>() {

            @Override
            public void onSuccess(LoginResponse loginResponse) {
                Log.d(TAG, "loginResponse==" + loginResponse);

                if (loginResponse.getCode() == 200) {
                    Constant.shopId = loginResponse.getData().getShopId();
                    Constant.cashierDeskId = loginResponse.getData().getCashierDeskId();
                    Constant.curUsername = loginRequest.getUsername();
                    Constant.shopName = loginResponse.getData().getShopName();

                    if (Constant.shopId == ) {
                    }

                    shopConfigDao.insertData(Constant.shopId, Constant.cashierDeskId,
                            Constant.shopName, loginRequest.getUsername());
                    openActivity(MoneyActivity.class);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                Log.d(TAG, "loginResponse onFailure==" + t.getMessage());
                super.onFailure(call, t);
            }
        });*/

        MyRetrofit.getApiService().initV2(loginRequest).enqueue(new MyCallback<LoginResponseV2>() {

            @Override
            public void onSuccess(LoginResponseV2 loginResponse) {
                Log.d(TAG, "loginResponse==" + loginResponse);

                if (loginResponse.getCode() == 200) {
                    Constant.shopId = loginResponse.getData().getShopId();
                    Constant.cashierDeskId = loginResponse.getData().getCashierDeskId();
                    Constant.curUsername = loginRequest.getUsername();
                    Constant.shopName = loginResponse.getData().getShopName();

                    if (loginResponse.getData().getCashAllow() != null &&
                            loginResponse.getData().getCashAllow().intValue() == 1) {
                        Consts.payModes = Consts.payModesWithCash;
                        Consts.payModeIcons = Consts.payModeIconsWithCash;
                    }else {
                        Consts.payModes = Consts.payModesWithNoCash;
                        Consts.payModeIcons = Consts.payModeIconsWithNoCash;
                    }

                    shopConfigDao.insertData(Constant.shopId, Constant.cashierDeskId,
                            Constant.shopName, loginRequest.getUsername());
                    openActivity(MoneyActivity.class);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseV2> call, Throwable t) {
                Log.d(TAG, "loginResponse onFailure==" + t.getMessage());
//                showToast("网络异常");
                super.onFailure(call, t);
            }
        });

//        finish();


    }

    @Override
    public void initData() {
        shopConfigDao = new ShopConfigDao(this);

        ShopConfig shopConfig = shopConfigDao.query();

        if (shopConfig != null && shopConfig.getAutoLogin() == 1 && NetUtils.INSTANCE.isNetConnected(this)) {
            Constant.shopId = shopConfig.getShopId();
            Constant.cashierDeskId = shopConfig.getCashierDeskId();
            Constant.curUsername = shopConfig.getUsername();
            Constant.shopName = shopConfig.getShopName();
            Consts.payModes = Consts.payModesWithNoCash;
            Consts.payModeIcons = Consts.payModeIconsWithNoCash;
            openActivity(MoneyActivity.class);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        netHelper.unregister(this);
    }
}
