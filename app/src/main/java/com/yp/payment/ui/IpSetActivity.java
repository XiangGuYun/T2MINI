package com.yp.payment.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yp.payment.R;
import com.yp.payment.jc_internet.ApkConstant;
import com.yp.payment.update.SPHelper;

/**
 * 设置IP地址
 */
public class IpSetActivity extends AppCompatActivity {
    private EditText et_ip_address;
    private TextView tv_set;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ip_set);
        init();
    }

    private void init() {
        initView();
        bindDefault();
        registerClick();
    }

    private void registerClick() {
        tv_set.setOnClickListener(view -> {
            String ipAddress = et_ip_address.getText().toString().trim();
            if(TextUtils.isEmpty(ipAddress)){
                Toast.makeText(IpSetActivity.this,"请输入ip地址",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!ipAddress.contains("http")){
                Toast.makeText(IpSetActivity.this,"请输入正确ip地址",Toast.LENGTH_SHORT).show();
                return;
            }
            ApkConstant.ONLINE_IP = ipAddress;
            SPHelper.putIpAddress(ipAddress);
            Toast.makeText(IpSetActivity.this,"IP地址设置成功,准备重启中...",Toast.LENGTH_SHORT).show();
            reStartApp();
        });
    }

    private void reStartApp() {
        new Handler().postDelayed(() -> {
            Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
            PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
            System.exit(0);
        },1500);
    }

    private void bindDefault() {
        et_ip_address.setText(SPHelper.getIpAddress());
        et_ip_address.setSelection(et_ip_address.getText().toString().length());
    }

    private void initView() {
        et_ip_address = findViewById(R.id.et_ip_address);
        tv_set = findViewById(R.id.tv_set);
    }
}
