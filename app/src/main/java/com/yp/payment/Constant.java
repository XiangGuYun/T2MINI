package com.yp.payment;

import android.os.Handler;

import com.yp.payment.model.OrderDetail;

import java.util.List;

public class Constant {

    public static boolean startPay = false;
    public static boolean updating = false;
    public static Integer curOrderSeq = 0;
    public static Integer shopId = null;
    public static Integer cashierDeskId = null;
    public static String curUsername = null;
    public static String curPrice = null;
    public static String shopName = "xxx";
    public static String payUser = "";
    public static String payBanlance = "";
    public static Handler keyboardHandler = null;

    public static List<OrderDetail> curOrderList;
}
