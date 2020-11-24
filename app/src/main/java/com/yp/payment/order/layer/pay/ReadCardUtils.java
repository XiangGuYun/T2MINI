package com.yp.payment.order.layer.pay;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.text.TextUtils;
import android.util.Log;

import com.yp.payment.Constant;
import com.yp.payment.http.MyCallback;
import com.yp.payment.internet.GetBalanceRequest;
import com.yp.payment.internet.MyRetrofit;
import com.yp.payment.model.GetBalanceResponse;
import com.yp.payment.reader.ReaderUtils;
import com.yp.payment.ui.LoginActivity;
import com.yp.payment.ui.OrderDishActivity;
import com.yp.payment.utils.GsonUtil;
import com.yp.payment.utils.PriceUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

public class ReadCardUtils {

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    public static void processIntent(OrderDishActivity activity, Intent intent) {
        //拿来装读取出来的数据，key代表扇区数，后面list存放四个块的内容
        Map<String, List<String>> map = new HashMap<>();
        //intent就是onNewIntent方法返回的那个intent
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        MifareClassic mfc = MifareClassic.get(tag);
        //如果当前IC卡不是这个格式的mfc就会为空
        Log.d("Test", "tag=== ： " + tag);
        if (null != mfc) {
            try {
                //链接NFC
                mfc.connect();
                //获取扇区数量
                int count = mfc.getSectorCount();
                boolean flag = false;
                byte[] bytes = {(byte) 0x94, (byte) 0x04, (byte) 0x30,
                        (byte) 0xDe, (byte) 0xf3, (byte) 0x06};

//                bytes = "940430Def306".getBytes();
//                    //验证扇区密码，否则会报错（链接失败错误）
//                    //这里验证的是密码A，如果想验证密码B也行，将方法中的A换成B就行
//                boolean isOpen = mfc.authenticateSectorWithKeyB(0, bytes);
                boolean isOpen = mfc.authenticateSectorWithKeyA(0, bytes);


                if (isOpen == false) {
                    byte[] bytesNew = {(byte) 0xff, (byte) 0xff, (byte) 0xff,
                            (byte) 0xff, (byte) 0xff, (byte) 0xff};

                    isOpen = mfc.authenticateSectorWithKeyA(0, bytesNew);
                }
                if (isOpen) {
//                        //获取扇区里面块的数量
                    int bCount = mfc.getBlockCountInSector(0);
                    //获取扇区第一个块对应芯片存储器的位置
                    //（我是这样理解的，因为第0扇区的这个值是4而不是0）
                    int bIndex = mfc.sectorToBlock(0);
                    //读取数据，这里是循环读取全部的数据
                    //如果要读取特定扇区的特定块，将i，j换为固定值就行
                    byte[] data = mfc.readBlock(bIndex + 0);
//                    showToast("读取扇区 == " + ReaderUtils.byteToString(data));
                    String cardNo = ReaderUtils.byteToString(data);
                    waitCustomerNfcPay(activity, cardNo);
                    flag = true;
                } else {
                    ToastUtils.toast("无法识别卡");
                    activity.lcdShowText("卡无效");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            activity.lcdShowText("云澎智能");
                        }
                    }, 2000);
                }
            } catch (Exception e) {
                ToastUtils.toast("mfc连接失败："+e.getLocalizedMessage());
                e.printStackTrace();
            } finally {
                try {
                    mfc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void waitCustomerNfcPay(OrderDishActivity activity, String nfc) {
        if(!Constant.startPay){
            GetBalanceRequest getBalanceRequest = new GetBalanceRequest();
            getBalanceRequest.setCardNo(nfc);
            getBalanceRequest.setShopId(Constant.shopId);
            getBalanceRequest.setDeviceId(LoginActivity.deviceId);
            MyRetrofit.getApiService().getBanlance(getBalanceRequest).enqueue(new MyCallback<GetBalanceResponse>() {
                @Override
                public void onSuccess(GetBalanceResponse getBalanceResponse) {

                    Constant.startPay = false;
                    if (getBalanceResponse.getCode() == 200) {
                        Constant.payUser = getBalanceResponse.getData().getUsername();
                        Constant.payBanlance = PriceUtil.changeF2Y(Long.valueOf(getBalanceResponse.getData().getBalance())) + "元";
                        activity.lcdTwoLine();
                    } else {
                        activity.lcdShowText(getBalanceResponse.getMessage());
                    }

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
//                            activity.handler.sendEmptyMessage(22);
                        }
                    }, 4000);
                }

                @Override
                public void onFailure(Call<GetBalanceResponse> call, Throwable t) {
                    super.onFailure(call, t);
                }
            });
            return;
        }
//        if (payMode == 1) {
        if (activity.payResultPop != null && !TextUtils.isEmpty(nfc)) {
            if (activity.payResultPop.isShowing()) {
                activity.payResultPop.setNfcResult(nfc);
            }
        }
//        }
    }

}
