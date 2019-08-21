package com.yp.payment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * @author : cp
 * @email : ibsfiq@qq.com
 * @date : 2019/8/21 14:02
 * @description ：
 */
public abstract class BaseActivity extends AppCompatActivity {
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        mContext = this;
        initView();
        initData();
    }

    public abstract int layoutId();

    public abstract void initView();

    public abstract void initData();

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public Drawable getDrawableResour(int id) {
        return getResources().getDrawable(id);
    }

    public int getResColor(int id) {
        return getResources().getColor(id);
    }

    public String getResString(int id) {
        return getResources().getString(id);
    }

    public void openActivity(Class<?> cls) {
        startActivity(new Intent(mContext, cls));
    }
}
