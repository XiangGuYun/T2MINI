package com.yp.baselib.helper;

import com.yp.baselib.utils.EquipmentUtils;
import com.yp.baselib.utils.LogUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 异常处理帮助类
 */
public class ExceptionHelper implements Thread.UncaughtExceptionHandler {

    private static volatile ExceptionHelper INSTANCE;

    private ExceptionHelper() {
    }

    public static ExceptionHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (ExceptionHelper.class) {
                if (INSTANCE == null) {
                    synchronized (ExceptionHelper.class) {
                        INSTANCE = new ExceptionHelper();
                    }
                }
            }
        }
        return INSTANCE;
    }

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 初始化默认异常捕获
     */
    public void init() {
        // 获取默认异常处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将当前类设为默认异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (handleException(e)) {
            // 已经处理,APP重启

        } else {
            // 如果不处理,则调用系统默认处理异常,弹出系统强制关闭的对话框
            if (mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(t, e);
            }
        }
    }

    private boolean handleException(Throwable e) {
        if (e == null) {
            return false;
        }

        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        pw.close();
        String result = writer.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String currentTime = sdf.format(System.currentTimeMillis());
        // 打印出错误日志
        LogUtils.e("TEST_EX", "异常发生时间："+currentTime + "\n异常原因：" +result+"\n发生异常的设备信息：");
        LogUtils.e("TEST_EX", "手机厂商：" + EquipmentUtils.getDeviceBrand());
        LogUtils.e("TEST_EX", "手机型号：" + EquipmentUtils.getSystemModel());
        LogUtils.e("TEST_EX", "手机当前系统语言：" + EquipmentUtils.getSystemLanguage());
        LogUtils.e("TEST_EX", "Android系统版本号：" + EquipmentUtils.getSystemVersion());

        LogUtils.e("TEST_EX", "手机设备名：" + EquipmentUtils.getSystemDevice());
        LogUtils.e("TEST_EX", "主板名：" + EquipmentUtils.getDeviceBoard());
        LogUtils.e("TEST_EX", "手机厂商名：" + EquipmentUtils.getDeviceManufacturer());
        return true;
    }

}