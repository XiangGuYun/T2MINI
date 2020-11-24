package com.yp.payment.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


public class FileUtil {
    public static final String TAG = "FileUtil";
    public static final File parentPath = Environment.getExternalStorageDirectory();
    public static String storagePath = "";
    public static final String DST_FOLDER_NAME = "cloudwalk";

    public static String beforePath = Environment.getExternalStorageDirectory() + "/Face";
    public static String appDownloadPath = Environment.getExternalStorageDirectory() + "/YPApp";
    public static String cacheRootPath = Environment.getExternalStorageDirectory() + "/Cache";
    public static String cachePath = Environment.getExternalStorageDirectory() + "/Cache/temp.jpg";
    public static String cacheOnlineFacePath = Environment.getExternalStorageDirectory() + "/Cache/onlineFaceTemp.jpg";
    public static String cacheOnline640FacePath = Environment.getExternalStorageDirectory() + "/Cache/online640FaceTemp.jpg";
    public static String cacheIrPath = Environment.getExternalStorageDirectory() + "/Cache/IrTemp.jpg";


    public static byte[] file2byte(File file) throws IOException {
        byte[] bytes = null;
        if (file != null && file.exists()) {
            InputStream is = new FileInputStream(file);
            int length = (int) file.length();
            if (length > Integer.MAX_VALUE) {// 当文件的长度超过了int的最大值
                System.out.println("this file is max ");
                is.close();
                return null;
            }
            bytes = new byte[length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            is.close();
            // 如果得到的字节长度和file实际的长度不一致就可能出错了
            if (offset < bytes.length) {
                System.out.println("file length is error");
                return null;
            }
        }
        return bytes;
    }

    public static void mkParentDir(String path) {
        File file = new File(path);
        if (!file.exists())
            file.mkdir();
    }

    /**
     * 递归创建文件夹
     *
     * @param dirPath
     */
    public static void mkDir(String dirPath) {
        String[] dirArray = dirPath.split("/");
        String pathTemp = "";
        for (int i = 1; i < dirArray.length; i++) {
            pathTemp = pathTemp + "/" + dirArray[i];
            File newF = new File(dirArray[0] + pathTemp);
            if (!newF.exists()) {
                newF.mkdir();
            }
        }
    }

    public static void createModelFile(String modelDirPath, String modelFileName, AssetManager assetManager) {
        String content = null;
        String parentDir = "model";
        List<String> fileNameList = null;
        String[] files = null;

        String fileAbsPath = "";
        if (modelDirPath.endsWith("/")) {
            fileAbsPath = modelDirPath + modelFileName;
        } else {
            fileAbsPath = modelDirPath + "/" + modelFileName;
        }

        File modelFile = new File(fileAbsPath);
        if (modelFile.exists()) {
            return;
        }
        mkDir(modelDirPath);

        try {
            files = assetManager.list(parentDir);
            fileNameList = Arrays.asList(files);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        for (String fileName : fileNameList) {
            Log.e(TAG, fileName);
            content = readRawFileToString(parentDir + File.separator + fileName, assetManager);
            writeStringToFile(content, fileAbsPath);
        }
    }

    public static void createModelFileAll(String modelDirPath, AssetManager assetManager) {
        String parentDir = "model";
        List<String> fileNameList = null;
        String[] files = null;

        String fileAbsPath = "";
        if (modelDirPath.endsWith("/")) {
            fileAbsPath = modelDirPath;
        } else {
            fileAbsPath = modelDirPath + "/";
        }

        mkDir(modelDirPath);
        try {
            files = assetManager.list(parentDir);
            fileNameList = Arrays.asList(files);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        for (String fileName : fileNameList) {
            Log.e(TAG, fileName);
            File modelFile = new File(fileAbsPath + fileName);
            if (!modelFile.exists()) {
                copyRawFileToSdcard(parentDir + File.separator + fileName, assetManager, fileAbsPath + fileName);
            }
        }
    }

    public static String readRawFileToString(String rawFileName, AssetManager assetManager) {
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(rawFileName);
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        if (inputStream != null)
            return inputStreamToString(inputStream);
        return null;
    }

    public static byte[] readRawFileToByteArray(String rawFileName, AssetManager assetManager) {
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(rawFileName);
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        if (inputStream != null)
            return inputStreamToByteArray(inputStream);
        return null;
    }

    public static void copyRawFileToSdcard(String rawFileName, AssetManager assetManager, String outPutFileAbs) {
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(rawFileName);
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        if (inputStream != null)
            inputStreamToFile(inputStream, outPutFileAbs);
    }

    public static String inputStreamToString(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
        }
        return outputStream.toString();
    }

    public static byte[] inputStreamToByteArray(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
        }
        return outputStream.toByteArray();
    }

    public static void inputStreamToFile(InputStream inputStream, String absPath) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

            byte[] imgBytes = outputStream.toByteArray();
            FileOutputStream fos = new FileOutputStream(absPath, false);
            fos.write(imgBytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeStringToFile(String content, String file_name) {
        try {
            File file = new File(file_name);

            FileOutputStream fileW = new FileOutputStream(file.getCanonicalPath());
            fileW.write(content.getBytes());
            fileW.close();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void writeByteArrayToFile(byte[] content, String file_name) {
        try {

            File file = new File(file_name);

            FileOutputStream fileW = new FileOutputStream(file.getCanonicalPath());
            fileW.write(content);
            fileW.close();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void writeByteArrayToSD(byte[] content, String file_name) {
        try {

            File parentPath = Environment.getExternalStorageDirectory();
            String file_path = parentPath.getAbsolutePath() + ("/") + DST_FOLDER_NAME + ("/") + file_name;

            File file = new File(file_path);

            FileOutputStream fileW = new FileOutputStream(file.getCanonicalPath());
            fileW.write(content);
            fileW.close();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            //
        }
    }

    /**
     * 把batmap 转file
     *
     * @param bitmap
     * @param filepath
     */
    public static File saveBitmapFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);//将要保存图片的路径
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


}
