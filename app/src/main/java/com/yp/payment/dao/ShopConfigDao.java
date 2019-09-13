package com.yp.payment.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yp.payment.model.ShopConfig;
import com.yp.payment.utils.DBHelper;

public class ShopConfigDao {

    private DBHelper mHelper;
    private SQLiteDatabase mDatabase;

    public ShopConfigDao(Context context) {
        mHelper = new DBHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertData(Integer shopId, Integer cashierDeskId) {
        delete();

        ContentValues values = new ContentValues();
        values.put("shopId", shopId);
        values.put("cashierDeskId", cashierDeskId);
        mDatabase.insert("shopConfig", null, values);
    }

    public void delete() {
        mDatabase.delete("shopConfig", "id > 0", new String[]{});
    }

    public ShopConfig query() {
        String sql = "select * from shopConfig";
        Cursor cursor = mDatabase.rawQuery(sql, null);

        if (cursor == null) {
            return null;
        }

        while (cursor.moveToNext()) {
            ShopConfig shopConfig = new ShopConfig();
            shopConfig.setShopId(cursor.getInt(1));
            shopConfig.setCashierDeskId(cursor.getInt(2));
            return shopConfig;
        }

        return null;
    }
}
