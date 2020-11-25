package com.yp.payment;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.Camera;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.aidl.MSCardService;
import com.sunmi.aidl.callback;
import com.sunmi.reader.usbhid.SunmiReader;
import com.sunmi.scan.Config;
import com.sunmi.scan.Image;
import com.sunmi.scan.ImageScanner;
import com.sunmi.scan.Symbol;
import com.sunmi.scan.SymbolSet;
import com.yp.payment.adapter.KeyBoardAdapter;
import com.yp.payment.printer.BluetoothUtil;
import com.yp.payment.reader.ReaderUtils;
import com.yp.payment.scanner.FinderView;
import com.yp.payment.scanner.SoundUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sunmi.ds.DSKernel;
import sunmi.ds.callback.IConnectionCallback;
import sunmi.ds.callback.IReceiveCallback;
import sunmi.ds.data.DSData;
import sunmi.ds.data.DSFile;
import sunmi.ds.data.DSFiles;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final String TAG = "MainActivity123";
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION};

    /**
     * 扫码
     */
    TextView tv_scan_result;
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private SurfaceView surface_view;
    private ImageScanner scanner;//声明扫描器
    private Handler autoFocusHandler;
    private AsyncDecode asyncDecode;
    SoundUtils soundUtils;
    private boolean vibrate;
    public int decode_count = 0;
    final private int REQUEST_PERMISSION_CODE = 20001;
    private FinderView finder_view;


    /**
     * 打印机
     */
    private Button btn_printer;

    /**
     * 副屏
     */
    private DSKernel mDSKernel = null;
    private MyHandler myHandler;
    private IWoyouService woyouService;

    /**
     * 读卡
     *
     * @param savedInstanceState
     */
    public static SunmiReader sunmireader = new SunmiReader();
    TextView tv_nfc_result;
    NfcAdapter mNfcAdapter;
    PendingIntent pi;

    /**
     *
     */
    GridView gridview;
    TextView tv_money;
    KeyBoardAdapter keyboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBluetoothPermission();
        myHandler = new MyHandler(this);
        initSdk();
//        initLcd();
        init();
//        initCard();
        initReadCard();
    }

    private void initReadCard() {
//        int ret = sunmireader.open(this);
//        Log.d(TAG, "initReadCard:  ==  " + ret);
//        showToast("initReadCard:  ==  " + ret);
//        if (ret < -1) {
//
//        }

        //初始化NfcAdapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // 初始化PendingIntent，当有NFC设备连接上的时候，就交给当前Activity处理
        pi = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

    }

    private void initSdk() {
        mDSKernel = DSKernel.newInstance();
        mDSKernel.init(this, mIConnectionCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback2);
        mDSKernel.removeReceiveCallback(mIReceiveCallback);
        mDSKernel.removeReceiveCallback(mIReceiveCallback2);
    }

    private void initLcd() {
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scanner != null) {
            scanner.destroy();
        }
        if (soundUtils != null) {
            soundUtils.release();
        }
    }

    private void init() {
        tv_nfc_result = findViewById(R.id.tv_nfc_result);
        surface_view = findViewById(R.id.surface_view);
        finder_view = findViewById(R.id.finder_view);
        tv_scan_result = findViewById(R.id.tv_pay_result);
        gridview = findViewById(R.id.gridview);
        tv_money = findViewById(R.id.tv_money);

//        keyboardAdapter = new KeyBoardAdapter(this, tv_money, new InputKeyboardCallback() {
//            @Override
//            public void onInputTextResult(String result) {
//                String moneyStr = tv_money.getText().toString();
//            }
//        });
        gridview.setAdapter(keyboardAdapter);
        mHolder = surface_view.getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);

        scanner = new ImageScanner();//创建扫描器
        scanner.setConfig(0, Config.X_DENSITY, 2);//行扫描间隔
        scanner.setConfig(0, Config.Y_DENSITY, 2);//列扫描间隔
        scanner.setConfig(0, Config.ENABLE_MULTILESYMS, 0);//是否开启同一幅图一次解多个条码,0表示只解一个，1为多个
        scanner.setConfig(0, Config.ENABLE_INVERSE, 0);//是否解反色的条码
        scanner.setConfig(Symbol.PDF417, Config.ENABLE, 0);//是否禁止PDF417码，默认开启

        autoFocusHandler = new Handler();
        asyncDecode = new AsyncDecode();
        decode_count = 0;
        //打印
        findViewById(R.id.btn_printer).setOnClickListener(onClickListener);
        findViewById(R.id.btn_screen).setOnClickListener(onCreenClickListener);
        findViewById(R.id.btn_clear_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_nfc_result.setText("");
            }
        });

    }


    View.OnClickListener onCreenClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            lcdTwoLine();
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
// 1: Get BluetoothAdapter
            BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
            if (btAdapter == null) {
                Toast.makeText(getBaseContext(), "Please Open Bluetooth!", Toast.LENGTH_LONG).show();
                return;
            }
            if (!btAdapter.isEnabled()) {
                btAdapter.enable();
                showToast("正在打开蓝牙");
            }
            // 2: Get Sunmi's InnerPrinter BluetoothDevice
            BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
            if (device == null) {
                Toast.makeText(getBaseContext(), "Please Make Sure Bluetooth have InnterPrinter!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            // 3: Generate a order data
//            byte[] data = ESCUtil.generateMockData();
            // 4: Using InnerPrinter print data
            BluetoothSocket socket = null;
            try {
                socket = BluetoothUtil.getSocket(device);
//                BluetoothUtil.sendData(data, socket);
            } catch (IOException e) {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }
        try {
            //摄像头预览分辨率设置和图像放大参数设置，非必须，根据实际解码效果可取舍
//			Camera.Parameters parameters = mCamera.getParameters();
//            parameters.setPreviewSize(800, 480);  //设置预览分辨率
            //     parameters.set("zoom", String.valueOf(27 / 10.0));//放大图像2.7倍
//            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(90);//竖屏显示
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(previewCallback);
            mCamera.startPreview();
            mCamera.autoFocus(autoFocusCallback);
            Log.d(TAG, "surfaceChanged: restart startPreview");
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    /**
     * 预览数据
     */
    Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (asyncDecode.isStoped()) {
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size size = parameters.getPreviewSize();//获取预览分辨率

                //创建解码图像，并转换为原始灰度数据，注意图片是被旋转了90度的
                Image source = new Image(size.width, size.height, "Y800");
                Rect scanImageRect = finder_view.getScanImageRect(size.height, size.width);
                //图片旋转了90度，将扫描框的TOP作为left裁剪
                source.setCrop(scanImageRect.top, scanImageRect.left, scanImageRect.height(), scanImageRect.width());
                source.setData(data);//填充数据
                asyncDecode = new AsyncDecode();
                asyncDecode.execute(source);//调用异步执行解码
            }
        }
    };

    private class AsyncDecode extends AsyncTask<Image, Void, String> {
        private boolean stoped = true;
        private String str = "";

        @Override
        protected String doInBackground(Image... params) {
            stoped = false;
            StringBuilder sb = new StringBuilder();
            Image src_data = params[0];//获取灰度数据

            long startTimeMillis = System.currentTimeMillis();

            //解码，返回值为0代表失败，>0表示成功
            int nsyms = scanner.scanImage(src_data);

            long endTimeMillis = System.currentTimeMillis();
            long cost_time = endTimeMillis - startTimeMillis;

            if (nsyms != 0) {
                playBeepSoundAndVibrate();//解码成功播放提示音

                decode_count++;
                sb.append("计数: " + String.valueOf(decode_count) + ", 耗时: " + String.valueOf(cost_time) + " ms \n");
                String qrCode = "";
                SymbolSet syms = scanner.getResults();//获取解码结果
                for (Symbol sym : syms) {
                    sb.append("[ " + sym.getSymbolName() + " ]: " + sym.getResult() + "\n");
                    qrCode = sym.getResult();
                }
                str = sb.toString();
                return qrCode;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (TextUtils.isEmpty(result)) {
                Log.d(TAG, "onPostExecute: 扫码失败");
                stoped = true;
            } else {

                //扫码成功，等待两秒后再去扫描
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        stoped = true;
                    }
                }).start();
                String m = tv_money.getText().toString();
                try {
                    double money = Double.parseDouble(m);
                    String cardNum = tv_nfc_result.getText().toString();
                    if (TextUtils.isEmpty(cardNum)) {
                        showToast("请输入会员卡");
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    showToast("金额输入有误！");
                    return;
                }
                tv_scan_result.setText(result);//显示解码结果
                Log.d(TAG, "onPostExecute: " + result);
                showToast("成功支付");
            }
            if (null == str || str.equals("")) {
//                Log.d(TAG, "onPostExecute: 没有识别");
            } else {
//                Log.d(TAG, "onPostExecute: 扫描结果 == " + str);
//                showToast(str);
//                tv_scan_result.setText(str);//显示解码结果
            }
        }

        public boolean isStoped() {
            return stoped;
        }
    }

    void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 自动对焦回调
     */
    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };


    //自动对焦
    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (null == mCamera || null == autoFocusCallback) {
                return;
            }
            mCamera.autoFocus(autoFocusCallback);
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = Camera.open(0);
        } catch (Exception e) {
            Log.d(TAG, "surfaceCreated: " + e.getMessage());
            mCamera = null;
            showToast("相机获取失败！");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private void initBeepSound() {
        if (soundUtils == null) {
            soundUtils = new SoundUtils(this, SoundUtils.RING_SOUND);
            soundUtils.putSound(0, R.raw.beep);
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initBeepSound();
        vibrate = false;
        if (mDSKernel != null) {
            mDSKernel.checkConnection();
        } else {
            initSdk();
        }
//        mNfcAdapter.enableForegroundDispatch(this, pi, null, null); //启动
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (soundUtils != null) {
            soundUtils.playSound(0, SoundUtils.SINGLE_PLAY);
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }


    private My m = new My();

    private MMy mm = new MMy();

    private MSCardService sendservice;

    private void initCard() {
        new Thread(sendable).start();
//        mButton.setEnabled(false);

    }

    Runnable sendable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                if (sendservice != null) {
                    sendservice.readRawMSCard(20000, m);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    class My extends callback.Stub {

        @Override
        public void MSCardInfo(final boolean isSuccess, final byte[] m1, final byte[] m2, final byte[] m3)
                throws RemoteException {
            runOnUiThread(new Runnable() {

                @SuppressWarnings("deprecation")
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "成功：" + isSuccess + "\n磁道1：" + new String(m1, "US-ASCII").split("\0")[0]
                                + "\n磁道2：" + new String(m2, "US-ASCII").split("\0")[0] + "\n磁道3："
                                + new String(m3, "US-ASCII").split("\0")[0]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    mButton.setEnabled(true);
                }
            });
        }

    }

    private class MMy implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sendservice = MSCardService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    int REQUEST_ENABLE_BT = 10001;

    /*
    校验蓝牙权限
   */
    private void checkBluetoothPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else {
                Log.d(TAG, "无需授权  ");
                startScan();
            }
        } else {//小于23版本直接使用
            Log.d(TAG, "checkBluetoothPermission: 小于23版本直接使用");
            startScan();
        }
    }

    //扫支付码
    public void startScan() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        startScan();
    }
//    public boolean checkBlueEnable(){
//        if (mBluetoothAdapter.isEnabled()){
//            return  true;
//        }else {
//            Toast.makeText(this,"蓝牙未打开",Toast.LENGTH_SHORT).show();
//            return  false;
//        }
//    }

    private IConnectionCallback mIConnectionCallback = new IConnectionCallback() {
        @Override
        public void onDisConnect() {
            Message message = new Message();
            message.what = 1;
            message.obj = "与远程服务连接中断";
            myHandler.sendMessage(message);
        }

        @Override
        public void onConnected(ConnState state) {
            Message message = new Message();
            message.what = 1;
            switch (state) {
                case AIDL_CONN:
                    message.obj = "与远程服务绑定成功";
                    break;
                case VICE_SERVICE_CONN:
                    message.obj = "与副屏服务通讯正常";
                    break;
                case VICE_APP_CONN:
                    message.obj = "与副屏app通讯正常";
                    break;
                default:
                    break;
            }
            myHandler.sendMessage(message);
        }
    };

    private IReceiveCallback mIReceiveCallback = new IReceiveCallback() {
        @Override
        public void onReceiveData(DSData data) {

        }

        @Override
        public void onReceiveFile(DSFile file) {

        }

        @Override
        public void onReceiveFiles(DSFiles files) {

        }

        @Override
        public void onReceiveCMD(DSData cmd) {

        }
    };
    private IReceiveCallback mIReceiveCallback2 = new IReceiveCallback() {
        @Override
        public void onReceiveData(DSData data) {

        }

        @Override
        public void onReceiveFile(DSFile file) {

        }

        @Override
        public void onReceiveFiles(DSFiles files) {

        }

        @Override
        public void onReceiveCMD(DSData cmd) {

        }
    };

    private static class MyHandler extends Handler {
        private WeakReference<Activity> mActivity;

        public MyHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() != null && !mActivity.get().isFinishing()) {
                switch (msg.what) {
                    case 1://消息提示用途
                        Toast.makeText(mActivity.get(), msg.obj + "", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }

    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
            readyLcd();
        }
    };

    /**
     * 副屏准备
     */
    private void readyLcd() {
        if (woyouService == null) {
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            woyouService.sendLCDCommand(1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清屏
     */
    public void clearLcd() {
        if (woyouService == null) {
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            woyouService.sendLCDCommand(4);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 两层显示
     */
    public void lcdTwoLine() {
        if (woyouService == null) {
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            woyouService.sendLCDDoubleString("Total:€999.99", "Change:¥999.99", null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void lcdShowText(String msg) {
        if (woyouService == null) {
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            woyouService.sendLCDString(msg, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
//        resolveIntent(intent);

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            processIntent(intent);
        }
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    private void processIntent(Intent intent) {
        //拿来装读取出来的数据，key代表扇区数，后面list存放四个块的内容
        Map<String, List<String>> map = new HashMap<>();
        //intent就是onNewIntent方法返回的那个intent
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        MifareClassic mfc = MifareClassic.get(tag);
        //如果当前IC卡不是这个格式的mfc就会为空
        if (null != mfc) {
            try {
                //链接NFC
                mfc.connect();
                //获取扇区数量
                int count = mfc.getSectorCount();
                //用于判断时候有内容读取出来
                boolean flag = false;
                byte[] bytes = {(byte) 0xff, (byte) 0xff, (byte) 0xff,
                        (byte) 0xff, (byte) 0xff, (byte) 0xff};
//                    //验证扇区密码，否则会报错（链接失败错误）
//                    //这里验证的是密码A，如果想验证密码B也行，将方法中的A换成B就行
                boolean isOpen = mfc.authenticateSectorWithKeyA(0, bytes);
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
                    tv_nfc_result.setText(ReaderUtils.byteToString(data));

                    flag = true;
                } else {
                    showToast("密码验证失败");
                    tv_nfc_result.setText("密码验证失败 ");

                }
            } catch (Exception e) {
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

    public static void main(String[] args) {
        String a = "123.4";
        System.out.println(a.lastIndexOf("."));
    }
}
