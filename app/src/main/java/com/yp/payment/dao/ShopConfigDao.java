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

    public void insertData(Integer shopId, Integer cashierDeskId,
                           String shopName, String username) {
        delete();

        ContentValues values = new ContentValues();
        values.put("shopId", shopId);
        values.put("cashierDeskId", cashierDeskId);
        values.put("shopName", shopName);
        values.put("username", username);
        values.put("autoLogin", 1);
        values.put("autoPrint", 0);
        mDatabase.insert("shopConfig", null, values);
    }

    public void updateExit(){
        ContentValues values = new ContentValues();
        values.put("autoLogin", "0");
        mDatabase.update("shopConfig", values, " id > 0 ", null);
    }


    public void updatePrintState(int state){
        ContentValues values = new ContentValues();
        values.put("autoPrint", state);
        mDatabase.update("shopConfig", values, " id > 0 ", null);
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
            shopConfig.setShopName(cursor.getString(3));
            shopConfig.setUsername(cursor.getString(4));
            shopConfig.setAutoLogin(cursor.getInt(5));
            shopConfig.setAutoPrint(cursor.getInt(6));
            return shopConfig;
        }

        return null;
    }
}
