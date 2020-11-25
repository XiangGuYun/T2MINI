package com.yp.baselib.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 */
public class MD5Utils {

    private static String md5_32;
    private static String md5_16;

    public static String md5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            md5_32 = result;
            md5_16 = buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            LogUtils.d("System.out", e.getLocalizedMessage());
        }

        return md5_32;
    }

    public String get16() {
        return md5_16;
    }

    public String get32() {
        return md5_32;
    }

}