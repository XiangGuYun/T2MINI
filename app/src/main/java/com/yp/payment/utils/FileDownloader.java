package com.yp.payment.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static com.yp.payment.utils.AppInstallReceiver.installApp;


/**
 * Created by jay on 16/9/30.
 */
public class FileDownloader
{

    /*public static void main(String[] args) {
        downloadNet("http://popecash.com/apk001.apk", "/Users/jay/Downloads/yunpengNewSdk/app/build/outputs/logs/aaaa.apk");
    }*/
    public static void downloadNet(String imgUrl, String newFilename) {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        try {
            URL url = new URL(imgUrl);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            conn.setReadTimeout(50000000);//设置超时时间
            FileOutputStream fs = new FileOutputStream(new File(newFilename));

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }

            fs.close();

            System.out.println("app url newFilename============== " + newFilename);
            installApp(newFilename);
            System.out.println("installApp end============= " + newFilename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
