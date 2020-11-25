package com.yp.payment.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // 数据库文件名
    public static final String DB_NAME = "shangmi5.db";

    // 数据库表名
    // 数据库版本号
    public static final int DB_VERSION = 8;


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    // 当数据库文件创建时，执行初始化操作，并且只执行一次
    @Override
    public void onCreate(SQLiteDatabase db) {

        // 建表 商户配置表
        String sql3 = "create table shopConfig" +
                "(id integer primary key autoincrement, " +
                "shopId integer, " +
                "cashierDeskId integer ," +
                "shopName varchar ," +
                "username varchar, " +
                "autoLogin integer, " +
                "autoPrint integer "
                + ")";

        db.execSQL(sql3);

        // 建表 商户配置表
        String orderSql = "create table order_info" +
                "(id integer primary key autoincrement, " +
                "shopId integer, " +
                "cashierDeskId integer, " +
                "orderNo varchar, " +
                "dateTime varchar, " +
                "orderType integer, " +
                "orderTypeStr varchar, " +
                "price varchar ," +
                "realPrice varchar ," +
                "discountPrice varchar "
                + ")";

        db.execSQL(orderSql);

    }

    // 当数据库版本更新执行该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("oldVersion : " + oldVersion);
    }

}
