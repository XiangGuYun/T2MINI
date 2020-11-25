package com.yp.baselib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.content.FileProvider;

import java.io.File;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * App工具类
 *
 * @author YeXuDong
 */
public class AppUtils {

    /**
     * 获取设备的IMEI值
     * 注意：需要添加READ_PRIVILEGED_PHONE_STATE权限
     *
     * @param ctx
     * @return
     */
    public static String getIMEI(Context ctx) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            TelephonyManager TelephonyMgr = (TelephonyManager) ctx.getSystemService(TELEPHONY_SERVICE);
            String szImei = TelephonyMgr.getDeviceId();
            return szImei;
        } else {
            return Settings.System.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = "";
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 安装APK
     *
     * @param ctx
     * @param providerAuthorities 提供者证书
     * @param filePath            APK文件路径
     */
    public static void install(Activity ctx, String providerAuthorities, String filePath) {
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //是否获取安装未知应用权限
            boolean hasInstallPermission = ctx.getPackageManager().canRequestPackageInstalls();
            if (hasInstallPermission) {
                //安装应用的逻辑
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(ctx, providerAuthorities, apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                //当为允许安装未知来源应用时
                ToastUtils.toast("请允许安装未知来源应用");
                //跳转至“安装未知应用”权限界面，引导用户开启权限，可以在onActivityResult中接收权限的开启结果
                Uri packageURI = Uri.parse("package:" + ctx.getPackageName());
                Intent intent1 = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivityForResult(intent1, REQUEST_CODE_UNKNOWN_APP);
                return;
            }
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        ctx.startActivity(intent);
        ctx.finish();
    }

    public static final int REQUEST_CODE_UNKNOWN_APP = 12;

    /**
     * 当允许安装位置来源APK之后的回调
     *
     * @param ctx
     * @param providerAuthorities
     * @param filePath
     * @param requestCode
     */
    public static void doInOnActivityResult(Context ctx, String providerAuthorities, String filePath, int requestCode) {
        if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
            File apkFile = new File(filePath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(ctx, providerAuthorities, apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            ctx.startActivity(intent);
        }
    }

}
