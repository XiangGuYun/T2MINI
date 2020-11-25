package com.yp.baselib.base

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * 开机自启动广播接收器
 *
 * 继承该广播接收器，并指定入口Activity
 *
 * 在manifest文件中添加如下
 *
 * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
 *
 * <receiver android:name=".广播接收器">
 *   <intent-filter>
 *       <action android:name="android.intent.action.BOOT_COMPLETED" />
 *       <category android:name="android.intent.category.LAUNCHER" />
 *   </intent-filter>
 * </receiver>
 *
 * 应用程序必须在Android中启动一次，下次才可以开机启动。
 */
abstract class BootBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == action_boot) {
            val bootMainIntent = Intent(context, getEntranceClass())
            // 这里必须为FLAG_ACTIVITY_NEW_TASK
            bootMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(bootMainIntent)
        }
    }

    abstract fun getEntranceClass():Class<out Activity>

    companion object {
        const val action_boot = "android.intent.action.BOOT_COMPLETED"
    }
}