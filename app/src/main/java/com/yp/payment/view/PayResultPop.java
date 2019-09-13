package com.yp.payment.view;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yp.payment.Constant;
import com.yp.payment.R;
import com.yp.payment.http.MyCallback;
import com.yp.payment.internet.MyRetrofit;
import com.yp.payment.internet.ShangMiOrderRequest;
import com.yp.payment.internet.ShangMiOrderResponse;
import com.yp.payment.utils.GsonUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
    public PayResultPop(Context context, String packageName) {
        super(context);
        this.context = context;
        this.packageName = packageName;
    }

    private static final String TAG = "PayResultPop";
    TextView tv_title;
    TextView tv_used_price_title, tv_used_price;
    TextView tv_balance_title, tv_balance;
    TextView tv_small_change_title, tv_small_change;
    TextView tv_cancle;
    DecimalFormat df = new DecimalFormat("#0.00");

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
            }
        });
//        findViewById(R.id.btn_sure_pay).setOnClickListener(this);
        setPriceData();
    }

    public void showPop(int payMode, double price, double commPrice) {
        this.payMode = payMode;
        this.price = price;
        this.commPrice = commPrice;
        show();
        setPriceData();

        if (this.payMode == 1) {//1现金支付
            ShangMiOrderRequest shangMiOrderRequest = new ShangMiOrderRequest();
            shangMiOrderRequest.setPayType(3);//支付类型 0：二维码支付，1：人脸支付，2：实体卡支付，3：其他支付,  4:商户扫码支付
            shangMiOrderRequest.setShopID(Constant.shopId);
            shangMiOrderRequest.setCashierDeskID(Constant.cashierDeskId);

            BigDecimal priceInCent = new BigDecimal(price).multiply(new BigDecimal(100));
            shangMiOrderRequest.setAccountBalance(priceInCent.longValue());
            doPay(shangMiOrderRequest);
        }
    }

    public void setNfcResult(String nfcStr) {
        Log.d(TAG, "setNfcResult: 接收到到 nfc===  ="+nfcStr);

        tv_small_change_title.setText("支付中……");

        ShangMiOrderRequest shangMiOrderRequest = new ShangMiOrderRequest();
        shangMiOrderRequest.setCardNo(nfcStr);
        shangMiOrderRequest.setPayType(2);
        shangMiOrderRequest.setShopID(Constant.shopId);
        shangMiOrderRequest.setCashierDeskID(Constant.cashierDeskId);
        BigDecimal priceInCent = new BigDecimal(price).multiply(new BigDecimal(100));
        shangMiOrderRequest.setAccountBalance(priceInCent.longValue());
        doPay(shangMiOrderRequest);
    }


    public void setQrcodeResult(String qrCode) {
        Log.d(TAG, "setQrcodeResult: 接收到到 qrcode===  ="+qrCode);
        tv_small_change_title.setText("支付中……");

        ShangMiOrderRequest shangMiOrderRequest = new ShangMiOrderRequest();
        shangMiOrderRequest.setAuthCode(qrCode);
        shangMiOrderRequest.setPayType(4);
        shangMiOrderRequest.setShopID(Constant.shopId);
        shangMiOrderRequest.setCashierDeskID(Constant.cashierDeskId);
        BigDecimal priceInCent = new BigDecimal(price).multiply(new BigDecimal(100));
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
                tv_used_price.setText(df.format(price));
                tv_balance_title.setVisibility(View.GONE);
                tv_balance.setVisibility(View.GONE);
                tv_small_change.setVisibility(View.GONE);
                break;
            case 0:
                tv_title.setText("线上支付");
                tv_used_price_title.setText("应付：");
                tv_used_price.setText(df.format(price));
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
        }
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
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
        Log.d(TAG, "orderRequest===: " + GsonUtil.GsonString(shangMiOrderRequest));
        MyRetrofit.getApiService().createShangMi(shangMiOrderRequest).enqueue(new MyCallback<ShangMiOrderResponse>() {
            @Override
            public void onSuccess(ShangMiOrderResponse orderResponse) {
                Log.d(TAG, "orderResponse===: " + GsonUtil.GsonString(orderResponse));


                if (orderResponse.getCode() == 200) {
                    tv_small_change_title.setText("支付成功");
                    playMusic(1);

                    tv_cancle.setText("确定");
                    tv_cancle.setVisibility(View.VISIBLE);

                    if (payMode == 1) {//1现金支付
                        hide();
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
            public void onFailure(Call<ShangMiOrderResponse> call, Throwable t) {
                super.onFailure(call, t);

                playMusic(3);
                orderRequesting = false;
                tv_cancle.setVisibility(View.VISIBLE);
            }
        });
    }

    MediaPlayer mediaPlayer;

    void playMusic(int type) {

        if (true) {
            return;
        }
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
        }
    }
}
