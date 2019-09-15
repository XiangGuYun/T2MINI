package com.yp.payment.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.yp.payment.ui.LoginActivity;

/**
 * Created by totopamimi on 17/3/26.
 */

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("boot_flag", Context.MODE_PRIVATE);
        // 每次开机都运行测试程序
        if (sp.getBoolean("first_run", true) || true) {
            sp.edit().putBoolean("first_run", false).apply();
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                Intent start = new Intent(context, LoginActivity.class);
                start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(start);
            }
        }
    }
}
