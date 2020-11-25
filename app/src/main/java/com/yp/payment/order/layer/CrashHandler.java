package com.yp.payment.order.layer;

import com.yp.payment.order.layer.utils.FileUtils;

/**
 * 自定义的异常处理类，实现UncaughtExceptionHandler接口
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {


    private static CrashHandler INSTANCE = new CrashHandler();

    private CrashHandler() {

    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 当有未捕获异常发生，就会调用该函数，
     * 可以在该函数中对异常信息捕获并上传
     * @param t 发生异常的线程
     * @param e 异常
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // 处理异常,可以自定义弹框，可以上传异常信息
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < e.getStackTrace().length; i++) {
            builder.append(e.getStackTrace()[i]+"\n");
        }
        FileUtils.INSTANCE.write(e.toString()+"\n"+builder.toString());
        // 干掉当前的程序
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}