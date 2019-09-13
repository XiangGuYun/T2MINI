package com.yp.payment;

import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Rect;
import android.hardware.Camera;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.reader.usbhid.SunmiReader;
import com.sunmi.scan.Config;
import com.sunmi.scan.Image;
import com.sunmi.scan.ImageScanner;
import com.sunmi.scan.Symbol;
import com.sunmi.scan.SymbolSet;
import com.yp.payment.adapter.OrderListAdapter;
import com.yp.payment.adapter.PayModeAdapter;
import com.yp.payment.entity.DepositorInfo;
import com.yp.payment.interfaces.PayModeCallback;
import com.yp.payment.interfaces.SettlementCallback;
import com.yp.payment.printer.BluetoothUtil;
import com.yp.payment.printer.ESCUtil;
import com.yp.payment.reader.ReaderUtils;
import com.yp.payment.scanner.FinderView;
import com.yp.payment.scanner.SoundUtils;
import com.yp.payment.ui.LoginActivity;
import com.yp.payment.utils.KeyboardManage;
import com.yp.payment.view.PayResultPop;
import com.yp.payment.view.VipLoginPop;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
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

public class MoneyActivity extends BaseActivity implements View.OnClickListener, PayModeCallback, SettlementCallback, SurfaceHolder.Callback {
    private static final String TAG = "MoneyActivity";

    RecyclerView pay_recycler_view;
    RecyclerView order_recyclerview;

    /**
     * 支付方式,默认线上支付
     */
    int payMode = 0;
    PayModeAdapter payModeAdapter;
    /**
     * 选中推荐价钱
     */
    double commPrice;
    /**
     * 会员登录
     */
    VipLoginPop vipLoginPop;
    TextView tv_vip_num, tv_vip_phone;
    /**
     * 结算弹窗
     */
    PayResultPop payResultPop;
    /**
     * 储户信息
     */
    DepositorInfo depositorInfo;
    TextView btn_vip_login;
    /**
     * 扫码
     */
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private SurfaceView surface_view;
    private ImageScanner scanner;//声明扫描器
    private Handler autoFocusHandler;
    private AsyncDecode asyncDecode;
    private FinderView finder_view;
    /**
     * 扫码提示音
     */
    SoundUtils soundUtils;
    private static final long VIBRATE_DURATION = 200L;
    private boolean vibrate;

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
    NfcAdapter mNfcAdapter;
    PendingIntent pi;
    String packageName;

    @Override
    public int layoutId() {
        return R.layout.activity_pay_page;
    }

    @Override
    public void initView() {
        myHandler = new MyHandler(this);
        findViewById(R.id.iv_exit).setOnClickListener(this);
        findViewById(R.id.btn_print).setOnClickListener(this);
        hideNavigation();
        initOrderList();
        initKeyboard();
        initPayMode();
        vipLoginPop = new VipLoginPop(this);
        packageName = getPackageName();
        payResultPop = new PayResultPop(this, packageName);
        initVipModel();
        initReadCard();
        initCamera();

    }

    private void initCamera() {
        surface_view = findViewById(R.id.surface_view);
        finder_view = findViewById(R.id.finder_view);
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
    }

    private void initReadCard() {
        //初始化NfcAdapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // 初始化PendingIntent，当有NFC设备连接上的时候，就交给当前Activity处理
        pi = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

    }

    void initVipModel() {
        btn_vip_login = findViewById(R.id.btn_vip_login);
        btn_vip_login.setOnClickListener(this);
        tv_vip_num = findViewById(R.id.tv_vip_num);
        tv_vip_phone = findViewById(R.id.tv_vip_phone);
    }

    @Override
    public void initData() {

    }

    /**
     * 隐藏navigation
     */
    void hideNavigation() {
        Window _window = getWindow();
        WindowManager.LayoutParams params = _window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        _window.setAttributes(params);
    }

    /**
     * 历史订单列表
     */
    void initOrderList() {
        order_recyclerview = findViewById(R.id.order_recyclerview);
        order_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        OrderListAdapter orderListAdapter = new OrderListAdapter(this);
        order_recyclerview.setAdapter(orderListAdapter);
    }

    /**
     * 支付方式
     */
    void initPayMode() {
        pay_recycler_view = findViewById(R.id.pay_recycler_view);
        pay_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        payModeAdapter = new PayModeAdapter(this, this);
        pay_recycler_view.setAdapter(payModeAdapter);
    }

    /**
     * 初始化sdk
     */
    private void initSdk() {
        mDSKernel = DSKernel.newInstance();
        mDSKernel.init(this, mIConnectionCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback);
        mDSKernel.addReceiveCallback(mIReceiveCallback2);
        mDSKernel.removeReceiveCallback(mIReceiveCallback);
        mDSKernel.removeReceiveCallback(mIReceiveCallback2);
    }

    private void printOrder() {
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
        byte[] data = ESCUtil.generateMockData();
        // 4: Using InnerPrinter print data
        BluetoothSocket socket = null;
        try {
            socket = BluetoothUtil.getSocket(device);
            BluetoothUtil.sendData(data, socket);
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

    /**
     * 键盘输入
     */
    void initKeyboard() {
        new KeyboardManage(this, findViewById(R.id.rootView), this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_exit:
                openActivity(LoginActivity.class);
                finish();
                break;
            case R.id.btn_print:
                printOrder();
                break;
            case R.id.btn_vip_login:
                if (depositorInfo == null) {
                    vipLoginPop.show();
                } else {
                    btn_vip_login.setText("会员登录");
                    depositorInfo = null;
                }
                break;
            default:
                break;
        }
    }


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


    @Override
    public void onSelectedMode(int mode) {
        payMode = mode;
    }

    @Override
    public void onSettlementClick(double price, double commPrice) {
        Log.d(TAG, "price==" + price + "; commPrice====" + commPrice + "; payMode==" + payMode);
        if (payMode == 4 && depositorInfo == null) {
            showToast("会员登录");
            return;
        }
        DecimalFormat df = new DecimalFormat("#0.00");
//        showToast("使用 " + Consts.payModes[payMode] + " 支付了 " + df.format(price));
        if (commPrice > 0) {
            payResultPop.showPop(payMode, commPrice, payMode);
        } else {
            payResultPop.showPop(payMode, price, payMode);
        }

    }


    private class AsyncDecode extends AsyncTask<Image, Void, String> {
        private boolean stoped = true;
        private String str = "";

        @Override
        protected String doInBackground(Image... params) {
            stoped = false;
            Image src_data = params[0];//获取灰度数据

            long startTimeMillis = System.currentTimeMillis();

            //解码，返回值为0代表失败，>0表示成功
            int nsyms = scanner.scanImage(src_data);

            long endTimeMillis = System.currentTimeMillis();
            long cost_time = endTimeMillis - startTimeMillis;

            if (nsyms != 0) {
                playBeepSoundAndVibrate();//解码成功播放提示音

                String qrCode = "";
                SymbolSet syms = scanner.getResults();//获取解码结果
                for (Symbol sym : syms) {
                    qrCode = sym.getResult();
                }
                return qrCode;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (TextUtils.isEmpty(result)) {
                Log.d(TAG, "onPostExecute: 扫码失败===");
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
                waitCustomerQrCodePay(result);
                Log.d(TAG, "onPostExecute===: " + result);
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

    private void playBeepSoundAndVibrate() {
        if (soundUtils != null) {
            soundUtils.playSound(0, SoundUtils.SINGLE_PLAY);
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * 初始化提示音
     */
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
        mNfcAdapter.enableForegroundDispatch(this, pi, null, null); //启动
    }

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
        Log.d(TAG, "tag=== ： " + tag);
        //如果当前IC卡不是这个格式的mfc就会为空
        if (null != mfc) {
            try {
                //链接NFC
                mfc.connect();
                //获取扇区数量
                int count = mfc.getSectorCount();
                Log.d(TAG, "count=== ： " + count);
                //用于判断时候有内容读取出来
                boolean flag = false;
                byte[] bytes = {(byte) 0x94, (byte) 0x04, (byte) 0x30,
                        (byte) 0xDe, (byte) 0xf3, (byte) 0x06};

//                bytes = "940430Def306".getBytes();
//                    //验证扇区密码，否则会报错（链接失败错误）
//                    //这里验证的是密码A，如果想验证密码B也行，将方法中的A换成B就行
//                boolean isOpen = mfc.authenticateSectorWithKeyB(0, bytes);
                boolean isOpen = mfc.authenticateSectorWithKeyA(0, bytes);

                Log.d(TAG, "isOpen=== ： " + isOpen);
                if (true) {
//                        //获取扇区里面块的数量
                    int bCount = mfc.getBlockCountInSector(0);
                    Log.d(TAG, "bCount=== ： " + bCount);
                    //获取扇区第一个块对应芯片存储器的位置
                    //（我是这样理解的，因为第0扇区的这个值是4而不是0）
                    int bIndex = mfc.sectorToBlock(0);
                    Log.d(TAG, "bIndex=== ： " + bIndex);
                    //读取数据，这里是循环读取全部的数据
                    //如果要读取特定扇区的特定块，将i，j换为固定值就行
                    byte[] data = mfc.readBlock(bIndex + 0);
                    Log.d(TAG, "data=== ： " + data);
//                    showToast("读取扇区 == " + ReaderUtils.byteToString(data));
                    String cardNo = ReaderUtils.byteToString(data);
                    Log.d(TAG, "读取卡号 ===： " + cardNo);
                    waitCustomerNfcPay(cardNo);
                    flag = true;
                } else {
                    showToast("密码验证失败");
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

    /**
     * 等待客户刷nfc支付
     */
    public void waitCustomerNfcPay(String nfc) {
//        if (payMode == 1) {
            if (payResultPop != null && !TextUtils.isEmpty(nfc)) {
                if (payResultPop.isShowing()) {
                    payResultPop.setNfcResult(nfc);
                }
            }
//        }
    }

    /**
     * 等待客户刷码支付
     */
    public void waitCustomerQrCodePay(String payCode) {
        //如果是nfc支付
//        if (payMode == 2 || payMode == 3) {
            if (payResultPop != null && !TextUtils.isEmpty(payCode)) {
                if (payResultPop.isShowing()) {
                    payResultPop.setQrcodeResult(payCode);

                }
            }
//        }
    }

}
