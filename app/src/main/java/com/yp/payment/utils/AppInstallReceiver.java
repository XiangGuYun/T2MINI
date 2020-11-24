package com.yp.payment.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yp.payment.ui.LoginActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class AppInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }

    /**
     * 执行命令
     */
    public static String execCmd(String cmd) {
        System.out.println(cmd);
        Process exeEcho;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            exeEcho = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    exeEcho.getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line);
            }
            System.out.println(stringBuffer.toString());
            exeEcho.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }


    public static boolean installApp(String apkPath) {
        if (apkPath == null || apkPath.length() < 4) {
            return false;
        }
        String type = apkPath.substring(apkPath.length() - 4, apkPath.length());
        if (!type.equals(".apk")) {
            return false;
        }
        File file = new File(apkPath);
        if (!file.exists()) {
            return false;
        }
        System.out.println("begin install apkPath============== " + apkPath);
        boolean isSuccess = install(apkPath);
        System.out.println("install result============== " + isSuccess);
        return isSuccess;
    }

    /**
     * 执行静默安装逻辑
     *
     * @param apkPath 要安装的apk文件的路径
     * @return 安装成功返回true，安装失败返回false。
     */
    public static boolean install(String apkPath) {
        boolean result = false;
//        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
//            Process process = new ProcessBuilder()
//                    .command("/system/xbin/su")
//                    .redirectErrorStream(true).start();

//            OutputStream out = mProcess.getOutputStream();
            // 申请su权限
//            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm install -i com.yp.payment –-user 0" + apkPath + "\n";
//            String command = "pm install -r " + apkPath + "\n";
            Process process = Runtime.getRuntime().exec(command);
//            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
//            dataOutputStream.flush();
//            dataOutputStream.writeBytes("exit\n");
//            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            Log.d("TAG", "===========install msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("TAG", "=========" + e.getMessage(), e);
        } finally {
            try {
//                if (dataOutputStream != null) {
//                    dataOutputStream.close();
//                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }
}
