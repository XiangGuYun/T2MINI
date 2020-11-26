package com.yp.payment.view;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yp.payment.update.OrderDetail;
import com.yp.payment.Constant;
import com.yp.payment.R;
import com.yp.payment.adapter.OrderListAdapter;
import com.yp.payment.dao.OrderDao;
import com.yp.payment.dao.ShopConfigDao;
import com.yp.payment.http.MyCallback;
import com.yp.payment.internet.MyRetrofit;
import com.yp.payment.internet.ShangMiOrderRequest;
import com.yp.payment.internet.orderresp.JsonsRootBean;
import com.yp.payment.order.layer.utils.BusUtils;
import com.yp.payment.ui.MoneyActivity;
import com.yp.payment.ui.OrderDishActivity;
import com.yp.payment.update.SPHelper;
import com.yp.payment.utils.GsonUtil;
import com.yp.payment.utils.PriceUtil;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

/**
 * @author : cp
 * @email : ibsfiq@qq.com
 * @date : 2019/8/21 15:17
 * @description ：
 */
public class PayResultPop extends Dialog implements View.OnClickListener {
    String packageName;
    Context context;
    OrderDao orderDao;
    ShopConfigDao shopConfigDao;
    OrderListAdapter orderListAdapter;

    Handler superHandler;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 18) {
                hide();
                return;
            }

        }
    };

    public PayResultPop(Context context, String packageName, OrderDao orderDao,
                        OrderListAdapter orderListAdapter, ShopConfigDao shopConfigDao, Handler handler
    ) {
        super(context);
        this.context = context;
        this.packageName = packageName;
        this.orderDao = orderDao;
        this.orderListAdapter = orderListAdapter;
        this.shopConfigDao = shopConfigDao;
        this.superHandler = handler;
    }

    private static final String TAG = "PayResultPop";
    TextView tv_title;
    TextView tv_used_price_title, tv_used_price;
    TextView tv_balance_title, tv_balance;
    TextView tv_small_change_title, tv_small_change;
    TextView tv_cancle;
    DecimalFormat df = new DecimalFormat("#0.00");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    boolean orderRequesting = false;
    /**
     * 支付方式
     */
    int payMode;
    /**
     * 应付价钱
     */
    double price;

    /**
     * 现金：实际给的；会员卡：卡内余额
     */
    double commPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_pay_result);
        tv_used_price_title = findViewById(R.id.tv_used_price_title);
        tv_used_price = findViewById(R.id.tv_used_price);
        tv_balance_title = findViewById(R.id.tv_balance_title);
        tv_balance = findViewById(R.id.tv_balance);
        tv_small_change_title = findViewById(R.id.tv_small_change_title);
        tv_small_change = findViewById(R.id.tv_small_change);
        tv_title = findViewById(R.id.tv_title);

        tv_cancle = findViewById(R.id.btn_cancel);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderRequesting = false;
                hide();
                Constant.startPay = false;
                Constant.keyboardHandler.sendEmptyMessage(1);
                Message msg = Message.obtain();
                msg.what = 0x123;
                EventBus.getDefault().post(msg);
            }
        });

//        findViewById(R.id.btn_sure_pay).setOnClickListener(this);
        setPriceData();
    }

    public void showPop(int payMode, double price, double commPrice) {
        Constant.startPay = true;

        this.payMode = payMode;
        this.price = price;
        this.commPrice = commPrice;
        show();
        setPriceData();

        if (this.payMode == 1) {//1现金支付
            ShangMiOrderRequest shangMiOrderRequest = new ShangMiOrderRequest();
            shangMiOrderRequest.setPayType(3);//支付类型 0：二维码支付，1：人脸支付，2：实体卡支付，3：其他支付,  4:商户扫码支付
            shangMiOrderRequest.setShopID(SPHelper.getShopId());
            shangMiOrderRequest.setCashierDeskID(SPHelper.getCashierDeskId());

            BigDecimal priceInCent = new BigDecimal(String.valueOf(price)).multiply(new BigDecimal(100));
            if(Constant.IS_ORDER_DISH){
                shangMiOrderRequest.setAccountBalance(priceInCent.longValue()*(OrderDishActivity.selectedIndex+1));
            } else {
                shangMiOrderRequest.setAccountBalance(priceInCent.longValue());
            }
            doPay(shangMiOrderRequest);
        }else {
            playMusic(5);
        }
    }

    public void setNfcResult(String nfcStr) {
        Log.d(TAG, "setNfcResult: 接收到到 nfc===  ="+nfcStr);

        tv_small_change_title.setText("支付中……");

        ShangMiOrderRequest shangMiOrderRequest = new ShangMiOrderRequest();
        shangMiOrderRequest.setCardNo(nfcStr);
        shangMiOrderRequest.setPayType(2);
        shangMiOrderRequest.setShopID(SPHelper.getShopId());
        shangMiOrderRequest.setCashierDeskID(SPHelper.getCashierDeskId());
        BigDecimal priceInCent = new BigDecimal(String.valueOf(price)).multiply(new BigDecimal(100));
        shangMiOrderRequest.setAccountBalance(priceInCent.longValue());
        doPay(shangMiOrderRequest);
    }


    public void setQrcodeResult(String qrCode) {
        Log.d(TAG, "setQrcodeResult: 接收到到 qrcode===  ="+qrCode);
        tv_small_change_title.setText("支付中……");

        ShangMiOrderRequest shangMiOrderRequest = new ShangMiOrderRequest();
        shangMiOrderRequest.setAuthCode(qrCode);
        shangMiOrderRequest.setPayType(4);
        shangMiOrderRequest.setShopID(SPHelper.getShopId());
        shangMiOrderRequest.setCashierDeskID(SPHelper.getCashierDeskId());
        BigDecimal priceInCent = new BigDecimal(String.valueOf(price)).multiply(new BigDecimal(100));
        shangMiOrderRequest.setAccountBalance(priceInCent.longValue());
        doPay(shangMiOrderRequest);
    }

    public void setPriceData() {
        if (tv_title == null) return;
        switch (payMode) {

            case 1:
                tv_title.setText("现金支付");
                tv_used_price_title.setText("应付：");
                tv_small_change_title.setText("订单同步中……");
                if(Constant.IS_ORDER_DISH){
                    tv_used_price.setText(df.format(price*(OrderDishActivity.selectedIndex+1)));
                } else {
                    tv_used_price.setText(df.format(price));
                }
                tv_balance_title.setVisibility(View.GONE);
                tv_balance.setVisibility(View.GONE);
                tv_small_change.setVisibility(View.GONE);
                break;
            case 0:
                tv_title.setText("线上支付");
                tv_used_price_title.setText("应付：");
                if(Constant.IS_ORDER_DISH){
                    tv_used_price.setText(df.format(price*(OrderDishActivity.selectedIndex+1)));
                } else {
                    tv_used_price.setText(df.format(price));
                }
                tv_small_change_title.setText("正等待客户刷卡或扫码……");

                tv_balance_title.setVisibility(View.GONE);
                tv_balance.setVisibility(View.GONE);
                tv_small_change.setVisibility(View.GONE);
                break;
            /*case 2:
                tv_title.setText("支付宝支付");
                tv_small_change_title.setText("正等待客户扫码…");
                tv_used_price.setText(df.format(price));


                tv_balance_title.setVisibility(View.GONE);
                tv_balance.setVisibility(View.GONE);
                tv_small_change.setVisibility(View.GONE);
                break;
            case 3:
                tv_title.setText("微信支付");
                tv_small_change_title.setText("正等待客户扫码…");
                tv_used_price.setText(df.format(price));


                tv_balance_title.setVisibility(View.GONE);
                tv_balance.setVisibility(View.GONE);
                tv_small_change.setVisibility(View.GONE);
                break;
            case 4:
                tv_title.setText("储值支付");
                tv_used_price_title.setText("应付：");
                tv_balance_title.setText("卡内余额：");
                tv_small_change_title.setText("支付后余额：");
                tv_used_price.setText(df.format(price));
                tv_balance.setText(df.format(commPrice));
                tv_small_change.setText(df.format(commPrice - price));

                tv_balance_title.setVisibility(View.VISIBLE);
                tv_balance.setVisibility(View.VISIBLE);
                tv_small_change.setVisibility(View.VISIBLE);
                break;*/
            default:
        }
    }

    @Override
    public void show() {
        super.show();
        BusUtils.sendMsg(123);
    }

    @Override
    public void hide() {
        super.hide();
        Constant.startPay = false;
        BusUtils.sendMsg(124);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                break;
//            case R.id.btn_sure_pay:
//                break;
        }
    }


    public void doPay(final ShangMiOrderRequest shangMiOrderRequest) {

        Log.d(TAG, "orderRequest===: " + GsonUtil.GsonString(shangMiOrderRequest));

        tv_cancle.setVisibility(View.INVISIBLE);

        if (shangMiOrderRequest == null || orderRequesting) {
            Log.d(TAG, "orderRequest===: 订单支付中");
            return;
        }
        orderRequesting = true;
        playMusic(2);

        final OrderDetail orderDetail = new OrderDetail();
        orderDetail.setPrice(PriceUtil.changeF2Y(shangMiOrderRequest.getAccountBalance()));
        orderDetail.setOrderType(shangMiOrderRequest.getPayType());

        if (orderDetail.getOrderType() == 2) {//支付类型 0：二维码支付，1：人脸支付，2：实体卡支付，3：其他支付,  4:商户扫码支付
            orderDetail.setOrderTypeStr("刷卡");
        } else if (orderDetail.getOrderType() == 3) {
            orderDetail.setOrderTypeStr("现金");
        } else if (orderDetail.getOrderType() == 4) {
            orderDetail.setOrderTypeStr("刷码");
        }
        orderDetail.setDateTime(simpleDateFormat.format(new Date()));
        orderDetail.setShopId(shangMiOrderRequest.getShopID());
        orderDetail.setCashierDeskId(shangMiOrderRequest.getCashierDeskID());
        orderDetail.setBranchId(SPHelper.getBranchId());
        Log.d(TAG, "orderRequest===: " + GsonUtil.GsonString(shangMiOrderRequest));

        MyRetrofit.getApiService().createShangMi(shangMiOrderRequest).enqueue(new MyCallback<JsonsRootBean>() {
            @Override
            public void onSuccess(JsonsRootBean orderResponse) {
                String result =  GsonUtil.GsonString(orderResponse);
                Log.d(TAG, "orderResponse===: " +result);




                if (orderResponse.getCode() == 200) {
                    Constant.keyboardHandler.sendEmptyMessage(1);
                    Constant.startPay = false;
                    tv_small_change_title.setText("支付成功");
                    playMusic(1);

                    tv_cancle.setText("确定");
                    tv_cancle.setVisibility(View.VISIBLE);

                    orderDetail.setRealPrice(PriceUtil.changeF2Y(Long.valueOf(orderResponse.getData().getRealFee())));
                    orderDetail.setDiscountPrice(PriceUtil.changeF2Y(
                            Long.valueOf(
                                    orderResponse.getData().getTotalFee() -
                                            orderResponse.getData().getRealFee())));
                    orderDetail.setOrderNo(orderResponse.getData().getOrderNo());
                    if(!Constant.IS_ORDER_DISH){
                        orderDao.insertData(orderDetail);
                    }
/*
                    if (payMode == 1) {//1现金支付
                        hide();
                    }*/

                    if (orderDetail.getOrderType() == 2) {//支付类型 0：二维码支付，1：人脸支付，2：实体卡支付，3：其他支付,  4:商户扫码支付

                        if (orderResponse.getData().getPayCard() != null) {
                            Constant.payUser = orderResponse.getData().getCustomerName();
                            Constant.payBanlance =
                                    PriceUtil.changeF2Y(Long.valueOf(orderResponse.getData().getPayCard().getAccountbalance()));
                            superHandler.sendEmptyMessage(23);
                        }

                        if (!Constant.staticPay) {
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(18);
                                }
                            }, 2000);
                        }else {
                            Constant.startPay = true;
                            tv_small_change_title.setText("正等待客户刷卡或扫码……");

                            tv_cancle.setText("取消");
                            tv_cancle.setVisibility(View.VISIBLE);
                        }

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                superHandler.sendEmptyMessage(22);
                            }
                        }, 4000);
                    }else {
                        superHandler.sendEmptyMessage(21);

                        if (!Constant.staticPay) {
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(18);
                                }
                            }, 2000);
                        }else {
                            Constant.startPay = true;
                            tv_small_change_title.setText("正等待客户刷卡或扫码……");

                            tv_cancle.setText("取消");
                            tv_cancle.setVisibility(View.VISIBLE);
                        }

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                superHandler.sendEmptyMessage(22);
                            }
                        }, 2000);
                    }



                    if(!Constant.IS_ORDER_DISH){
                        Constant.curOrderList = orderDao.queryOrderList();
                    }

                    orderListAdapter.notifyDataSetChanged();

                    if (shopConfigDao.query().getAutoPrint() == 1) {
                        if(!Constant.IS_ORDER_DISH){
                            MoneyActivity moneyActivity = (MoneyActivity)context;
                            moneyActivity.printOrder(orderDetail);
                        } else {
                            OrderDishActivity orderDishActivity = (OrderDishActivity)context;
                            orderDishActivity.printOrder(orderDetail);
                        }
                    }
                } else {
                    Log.d(TAG, "orderResponse.getCode()== != 200: " + orderResponse);
                    tv_small_change_title.setText("支付失败");
                    playMusic(0);

                    tv_cancle.setVisibility(View.VISIBLE);
                }

                orderRequesting = false;



            }

            @Override
            public void onFailure(Call<JsonsRootBean> call, Throwable t) {
                super.onFailure(call, t);

                Log.d(TAG, "onFailure====: " + t.getMessage());
                playMusic(3);
                orderRequesting = false;
                tv_cancle.setVisibility(View.VISIBLE);
                Constant.startPay = false;
            }
        });
    }

    MediaPlayer mediaPlayer;

    void playMusic(int type) {

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "onPrepared: ");
                    mp.start();
                }
            });
        }
        mediaPlayer.reset();
        if (type == 1) {//  -
            try {
                mediaPlayer.setDataSource(context, Uri.parse("android.resource://" + packageName + "/" + R.raw.paysuccess));
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == 0) {//  -
            try {
                mediaPlayer.setDataSource(context, Uri.parse("android.resource://" + packageName + "/" + R.raw.payfail));
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == 2) {//  -
            try {
                mediaPlayer.setDataSource(context, Uri.parse("android.resource://" + packageName + "/" + R.raw.paying));
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == 3) {//  -
            try {
                mediaPlayer.setDataSource(context, Uri.parse("android.resource://" + packageName + "/" + R.raw.networkerror));
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == 5) {//  -
            try {
                mediaPlayer.setDataSource(context, Uri.parse("android.resource://" + packageName + "/" + R.raw.pleasepay));
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
