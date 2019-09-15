package com.yp.payment.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.yp.payment.Constant;
import com.yp.payment.http.MyCallback;
import com.yp.payment.internet.MyRetrofit;
import com.yp.payment.internet.SyncDataRequest;
import com.yp.payment.model.SyncDataEntity;
import com.yp.payment.ui.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import retrofit2.Call;

public class MsgSynchTask implements Runnable {


    private static final String TAG = "MsgSynchTask";
    Handler handler;


    public MsgSynchTask(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;

        Log.d(TAG, "new MsgSynchTask====");
    }

    Context context;

    static SyncDataRequest syncDataRequest;
    static {
        syncDataRequest = new SyncDataRequest();
        syncDataRequest.setDeviceId(LoginActivity.deviceId);
    }

    @Override
    public void run() {

        Log.d(TAG, "syncDataRequest====" + GsonUtil.GsonString(syncDataRequest));
        MyRetrofit.getApiService().syncData(syncDataRequest).enqueue(new MyCallback<SyncDataEntity>() {
            @Override
            public void onSuccess(SyncDataEntity syncDataEntity) {
                Log.d(TAG,"syncDataEntity request success===== " + GsonUtil.GsonString(syncDataEntity));

                if (syncDataEntity.getData() != null && syncDataEntity.getData().size() > 0) {
                    List<SyncDataEntity.DataBean> dataBeanList = syncDataEntity.getData();
                    /**
                     * 2: app update
                     * 1: customer update
                     * 3: shop config update
                     */
                    for (SyncDataEntity.DataBean dataBean : dataBeanList) {
                        Log.d(TAG,"SyncDataEntity.DataBean============== " + GsonUtil.GsonString(dataBean));
                        if (dataBean.getMsgType().intValue() == 2) {//app update

                            final String content = dataBean.getMsgContent();
                            handler.sendEmptyMessage(17);//提示正在更新

                            Constant.updating = true;
                            //content为下载地址

                            Log.d(TAG,"app url ============== " + content);
                            new Thread(new Runnable(){
                                @Override
                                public void run() {
                                    String newFilename = dowloadApp(content);

                                }
                            }).start();



                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SyncDataEntity> call, Throwable t) {
                super.onFailure(call, t);
            }
        });


    }

    /**
     * @param url
     */
    private String dowloadApp(String url) {
        String fileName = System.currentTimeMillis() + ".apk";
        File mFile = new File(FileUtil.appDownloadPath);
        if (!mFile.exists()){
            mFile.mkdir();
        }

        String newFilename = FileUtil.appDownloadPath + "/" + fileName;
        FileDownloader.downloadNet(url, newFilename);

        return newFilename;
    }


}
