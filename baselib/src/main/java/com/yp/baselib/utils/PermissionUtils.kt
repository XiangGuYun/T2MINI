package com.yp.baselib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * 权限申请工具类
 * READ_CALENDAR , WRITE_CALENDAR 读写日历权限
 * CAMERA 调用相机权限
 * READ_CONTACTS , WRITE_CONTACTS , GET_ACCOUNTS 通讯录权限
 * ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION 定位权限
 * RECORD_AUDIO 录音权限
 * READ_PHONE_STATE ,CALL_PHONE READ_CALL_LOG, WRITE_CALL_LOG ADD_VOICEMAIL 手机状态相关
 * BODY_SENSORS 传感器权限
 * SMS SEND_SMS ,RECEIVE_SMS ,READ_SMS, RECEIVE_WAP_PUSH, RECEIVE_MMS SMS 消息权限
 * READ_EXTERNAL_STORAGE ,WRITE_EXTERNAL_STORAGE 外部存储权限
 */
object PermissionUtils {

    /**
     * 主动申请权限
     * @param activity Activity
     * @param granted Function0<Unit>?
     * @param shouldShowRequestPermissionRationale Function0<Unit>?
     * @param needGoToSettingsPage Function0<Unit>?
     * @param permissions Array<out String>
     */
    @SuppressLint("CheckResult")
    fun req(activity: Activity,
            granted: (() -> Unit)? = null,
            shouldShowRequestPermissionRationale: (() -> Unit)? = null,
            needGoToSettingsPage: (() -> Unit)? = null,
            vararg permissions: String) {
        val rp = RxPermissions(activity)
        rp.requestEachCombined(*permissions)
                .subscribe { permission ->
                    when {
                        permission.granted -> {
                            // 所有权限被通过
                            granted?.invoke()
                        }
                        permission.shouldShowRequestPermissionRationale -> {
                            // 有权限不被通过
                            shouldShowRequestPermissionRationale?.invoke()
                        }
                        else -> {
                            // 有权限不被通过，且不再提醒
                            needGoToSettingsPage?.invoke()
                        }
                    }
                }
    }

    /**
     * 被动申请权限
     * @param activity Activity
     * @param targetView View
     * @param granted Function0<Unit>?
     * @param shouldShowRequestPermissionRationale Function0<Unit>?
     * @param needGoToSettingsPage Function0<Unit>?
     * @param permissions Array<out String>
     */
    @SuppressLint("CheckResult")
    fun reqOnClick(
            activity: Activity,
            targetView: View,
            granted: (() -> Unit)? = null,
            shouldShowRequestPermissionRationale: (() -> Unit)? = null,
            needGoToSettingsPage: (() -> Unit)? = null,
            vararg permissions: String) {
        RxView.clicks(targetView) // 设置触发权限申请的View
                .compose(RxPermissions(activity)
                        // 设置需要的所有权限
                        .ensureEachCombined(*permissions)
                ).subscribe {
                    when {
                        it.granted -> {
                            // 所有权限被通过
                            granted?.invoke()
                        }
                        it.shouldShowRequestPermissionRationale -> {
                            // 有权限不被通过
                            shouldShowRequestPermissionRationale?.invoke()
                        }
                        else -> {
                            // 有权限不被通过，且不再提醒
                            needGoToSettingsPage?.invoke()
                        }
                    }
                }
    }

    /**
     * 跳转应用设置页面
     * @return
     */
    fun goToAppSettingsPage(context: Context) {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        localIntent.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(localIntent)
    }

}