package com.yp.payment;

/**
 * @author : cp
 * @email : ibsfiq@qq.com
 * @date : 2019/8/21 15:08
 * @description ：
 */
public class Consts {
    public static String payModes[] = {"快速收银", "点餐模式"};//

//    public static String payModes[] = {"线上支付", "现金"};

    public static String payModesWithCash[] = {"线上支付", "现金"};
    public static String payModesWithNoCash[] = {"线上支付", "现金"};
    public static int payModeIconsWithCash[] = {R.drawable.icon_nfc , R.drawable.icon_cash};
    public static int payModeIconsWithNoCash[] = {R.drawable.icon_nfc, R.drawable.icon_cash};

    public static int payModeIcons[] = {R.drawable.icon_nfc , R.drawable.icon_cash};

    //    public static String payModes[] = {"现金", "线上支付", "支付宝", "微信支付", "储值支付"};
//    public static int payModeIcons[] = {R.drawable.icon_cash, R.drawable.icon_nfc, R.drawable.icon_alipay, R.drawable.icon_wechat, R.drawable.icon_card};
}
