package com.yp.payment.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yp.payment.Constant;
import com.yp.payment.model.OrderDetail;
import com.yp.payment.utils.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    private DBHelper mHelper;
    private SQLiteDatabase mDatabase;

    public OrderDao(Context context) {
        mHelper = new DBHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertData(OrderDetail orderInfo) {
        ContentValues values = new ContentValues();

        values.put("shopId", orderInfo.getShopId());
        values.put("cashierDeskId", orderInfo.getCashierDeskId());
        values.put("orderNo", orderInfo.getOrderNo());
        values.put("dateTime", orderInfo.getDateTime());
        values.put("orderType", orderInfo.getOrderType());
        values.put("orderTypeStr", orderInfo.getOrderTypeStr());
        values.put("price", orderInfo.getPrice());
        values.put("realPrice", orderInfo.getRealPrice());
        values.put("discountPrice", orderInfo.getDiscountPrice() );

        mDatabase.insert("order_info", null, values);
    }

    public List<OrderDetail> queryOrderList() {
        String sql = "select * from order_info where shopId = '" + Constant.shopId + "' order by id desc limit 30";
        Cursor cursor = mDatabase.rawQuery(sql, null);

        if (cursor == null) {
            return null;
        }

        List<OrderDetail> orderInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            OrderDetail orderInfo = new OrderDetail();

            orderInfo.setShopId(cursor.getInt(1));
            orderInfo.setCashierDeskId(cursor.getInt(2));
            orderInfo.setOrderNo(cursor.getString(3));
            orderInfo.setDateTime(cursor.getString(4));
            orderInfo.setOrderType(cursor.getInt(5));
            orderInfo.setOrderTypeStr(cursor.getString(6));
            orderInfo.setPrice(cursor.getString(7));
            orderInfo.setRealPrice(cursor.getString(8));
            orderInfo.setDiscountPrice(cursor.getString(9));

            orderInfoList.add(orderInfo);
        }

        return orderInfoList;
    }


    public OrderDetail queryById(Integer id) {
        String sql = "select * from order_info where id = " + id;
        Cursor cursor = mDatabase.rawQuery(sql, null);

        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext()) {
            OrderDetail orderInfo = new OrderDetail();

            orderInfo.setShopId(cursor.getInt(1));
            orderInfo.setCashierDeskId(cursor.getInt(2));
            orderInfo.setOrderNo(cursor.getString(3));
            orderInfo.setDateTime(cursor.getString(4));
            orderInfo.setOrderType(cursor.getInt(5));
            orderInfo.setOrderTypeStr(cursor.getString(6));
            orderInfo.setPrice(cursor.getString(7));
            orderInfo.setRealPrice(cursor.getString(8));
            orderInfo.setDiscountPrice(cursor.getString(9));

            return orderInfo;
        }
        
        return null;
    }
}
