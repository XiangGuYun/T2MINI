package com.yp.baselib.base;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

/**
 *
 */
public class BaseApplication extends Application {

    private static BaseApplication sInstance;

    public static BaseApplication getInstance() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public Context getContext(){
        return getApplicationContext();
    }

}