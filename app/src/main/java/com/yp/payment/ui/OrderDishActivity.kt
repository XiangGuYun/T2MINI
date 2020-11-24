package com.yp.payment.ui

import android.app.PendingIntent
import android.bluetooth.BluetoothSocket
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.Camera
import android.media.MediaPlayer
import android.net.Uri
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.android.tu.loadingdialog.LoadingDailog
import com.sunmi.scan.*
import com.yp.payment.Constant
import com.yp.payment.R
import com.yp.payment.adapter.OrderListAdapter
import com.yp.payment.dao.OrderDao
import com.yp.payment.dao.ShopConfigDao
import com.yp.payment.http.MyCallback
import com.yp.payment.internet.GetBalanceRequest
import com.yp.payment.internet.MyRetrofit
import com.yp.payment.jc_internet.OrderCountModel
import com.yp.payment.model.GetBalanceResponse
import com.yp.payment.model.OrderDetail
import com.yp.payment.order.layer.Dlg
import com.yp.payment.order.layer.RV
import com.yp.payment.order.layer.Req
import com.yp.payment.order.layer.TimerLayer
import com.yp.payment.order.layer.adapter.*
import com.yp.payment.order.layer.model.AllCaiModel
import com.yp.payment.order.layer.model.AllCaiTypeModel
import com.yp.payment.order.layer.utils.ContextUtils.hideNavigation
import com.yp.payment.order.layer.utils.MoneyUtils
import com.yp.payment.order.layer.utils.SPUtils
import com.yp.payment.order.layer.utils.ToastUtils
import com.yp.payment.printer.BluetoothUtil
import com.yp.payment.printer.ESCUtil
import com.yp.payment.reader.ReaderUtils
import com.yp.payment.scanner.FinderView
import com.yp.payment.utils.Jc_Utils
import com.yp.payment.utils.PriceUtil
import com.yp.payment.view.PayResultPop
import kotlinx.android.synthetic.main.act_diancan.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import sunmi.ds.DSKernel
import sunmi.ds.callback.IConnectionCallback
import sunmi.ds.callback.IConnectionCallback.ConnState
import sunmi.ds.callback.IReceiveCallback
import sunmi.ds.data.DSData
import sunmi.ds.data.DSFile
import sunmi.ds.data.DSFiles
import woyou.aidlservice.jiuiv5.IWoyouService
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.*

/**
 * 点餐
 */
class OrderDishActivity : BaseActivity(), View.OnClickListener {

    private var mCamera: Camera? = null
    private lateinit var asyncDecode: AsyncDecode
    private lateinit var scanner: ImageScanner
    private lateinit var pi: PendingIntent
    private lateinit var mNfcAdapter: NfcAdapter
    private var mDSKernel: DSKernel? = null

    /**
     * 菜品列表
     */
    private val listDishes = ArrayList<AllCaiModel.DataBean.ResultsBean>()

    /**
     * 菜品类别列表
     */
    private val listDishClass = ArrayList<AllCaiTypeModel.DataBean.ResultsBean>()

    /**
     * 已点餐品列表
     */
    private val listDishOrder = ArrayList<AllCaiModel.DataBean.ResultsBean>()

    /**
     * 加载中对话框
     */
    private var dialogLoading: LoadingDailog? = null

    /**
     * 订单统计对话框
     */
    private var dialogOrderStat: Dlg.OrderStatDialog? = null

    /**
     * 待支付价格
     */
    private var priceForPay = 0.0

    /**
     * 当前选中的菜品类别列表位置
     */
    private var selectedClassIndex = 0

    /**
     * 用于计时
     */
    private var timer: Timer? = null

    /**
     * 结算弹窗
     */
    lateinit var payResultPop: PayResultPop

    private var woyouService: IWoyouService? = null

    class MyHandler(private val act: OrderDishActivity) : Handler(){
        var wrActivity = WeakReference<OrderDishActivity>(act)

        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                17 -> {
                    ToastUtils.toast("系统升级中，请稍后操作")
                    return
                }
                19 -> {
                    wrActivity.get()?.playMusic(4)
                }
                20 -> {
                    wrActivity.get()?.lcdShowText(Constant.curPrice + "元")
                }
                21 -> {
                    wrActivity.get()?.lcdShowText("支付成功")
                    wrActivity.get()?.let {
                        it.listDishOrder.clear()
                        RV.update(it.listview_left)
                        it.tv_total.text = ""
                        it.tv_count.text = ""
                    }
                }
                22 -> {
                    wrActivity.get()?.lcdShowText("云澎科技")
                }
                23 -> {
                    wrActivity.get()?.lcdTwoLine()
                }
            }
        }
    }

    var handler: Handler = MyHandler(this)

    override fun layoutId(): Int {
        return R.layout.act_diancan
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        hideNavigation(this)
    }

    override fun initData() {
        priceForPay = intent.getDoubleExtra("priceForPay", 0.0)
        dialogLoading = Dlg.getLoadingDialog(this)
        dialogOrderStat = Dlg.getOrderStatDialog(this)
        initTimer()
        initReadCard()
        initDishClassListView()
        initDishesGridView()
        initOrderDishListView()
        tv_qie.setOnClickListener(this)
        tv_jiesuan.setOnClickListener(this)
        tv_baobiao.setOnClickListener(this)
        tv_refresh.setOnClickListener(this)
        RV.doXNumberList(findViewById(R.id.rvX))
        Req.reqDishesTypes(false,
                { list: List<AllCaiTypeModel.DataBean.ResultsBean> -> bindDishClassData(list) })
        { list: List<AllCaiModel.DataBean.ResultsBean> -> bindAllCaiData(list) }

        val intent = Intent()
        intent.setPackage("woyou.aidlservice.jiuiv5")
        intent.action = "woyou.aidlservice.jiuiv5.IWoyouService"
        val success = bindService(intent, connService, Context.BIND_AUTO_CREATE)
        payResultPop = PayResultPop(this, packageName, OrderDao(this), OrderListAdapter(this), ShopConfigDao(this), handler)
//        initCamera()
    }

    private fun initReadCard() {
        //初始化NfcAdapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        // 初始化PendingIntent，当有NFC设备连接上的时候，就交给当前Activity处理
        pi = PendingIntent.getActivity(this, 0, Intent(this, javaClass)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
    }

    private val connService = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            woyouService = IWoyouService.Stub.asInterface(service)
            readyLcd()
        }
    }

    /**
     * 副屏准备
     */
    private fun readyLcd() {
        if (woyouService == null) {
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            woyouService?.sendLCDCommand(1)
            lcdShowText("云澎科技")
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }


    /**
     * 两层显示
     */
    fun lcdTwoLine() {
        if (woyouService == null) {
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            woyouService?.sendLCDDoubleString(Constant.payUser, "余额:" + Constant.payBanlance, null)
            Constant.payUser = ""
            Constant.payBanlance = ""
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun lcdShowText(msg: String) {
        if (woyouService == null) {
            Toast.makeText(this, "Service not ready", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            woyouService?.sendLCDString(msg, null)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    var mediaPlayer: MediaPlayer? = null

    fun playMusic(type: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setOnPreparedListener { mp ->
                mp.start()
            }
        }
        mediaPlayer!!.reset()
        if (type == 4) { //  -
            try {
                mediaPlayer!!.setDataSource(this, Uri.parse("android.resource://" + packageName + "/" + R.raw.input))
                mediaPlayer!!.prepareAsync()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (type == 5) {
            try {
                mediaPlayer!!.setDataSource(this, Uri.parse("android.resource://" + packageName + "/" + R.raw.notstart))
                mediaPlayer!!.prepareAsync()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (type == 99) {
            try {
                mediaPlayer!!.setDataSource(this, Uri.parse("android.resource://" + packageName + "/" + R.raw.nospay))
                mediaPlayer!!.prepareAsync()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (type == 98) {
            try {
                mediaPlayer!!.setDataSource(this, Uri.parse("android.resource://" + packageName + "/" + R.raw.spay))
                mediaPlayer!!.prepareAsync()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Subscribe
    fun handleMessage(msg: Message) {
        when (msg.what) {
            0x1212 -> {
                listDishOrder.clear()
                RV.update(listview_left)
                calculateMoney()
            }
            0x1213 -> {
                listDishOrder.clear()
            }
            123 -> {
                lcdShowText(priceForPay.toString())
            }
            124 -> {
                lcdShowText("云澎科技")
                listDishOrder.clear()
                RV.update(listview_left)
                tv_total.text = "总计¥0"
                tv_count.text = "数量 0"
            }
        }
    }

    /**
     * 初始化定时器
     */
    private fun initTimer() {
        timer = TimerLayer.getCommonTimer(this) {
            tv_title.text = Constant.shopName
            tv_date.text = Jc_Utils.timeStamp2Date(System.currentTimeMillis())
            tv_version.text = "v${Jc_Utils.getAppVersionName(this@OrderDishActivity)}"
        }
    }

    /**
     * 初始化点餐列表视图
     */
    private fun initOrderDishListView() {
        val leftListAdapter = LeftListAdapter(this@OrderDishActivity, listDishOrder,
                LeftListDelInterFace { position: Int -> delSelectedOrderDish(position) })
        listview_left.adapter = leftListAdapter
    }

    /**
     * 删除选中的菜品
     */
    private fun delSelectedOrderDish(position: Int) {
        if (listDishOrder[position].count == 1) {
            listDishOrder.removeAt(position)
            RV.update(listview_left)
            calculateMoney()
            return
        }
        listDishOrder[position].count = listDishOrder[position].count
        RV.update(listview_left)
        calculateMoney()
    }

    /**
     * 初始化菜品网格视图
     */
    private fun initDishesGridView() {
        val rightGridAdapter = RightGridAdapter(this@OrderDishActivity, listDishes,
                RightGridviewInterface { position: Int -> addDishToOrderList(position) })
        gridView.adapter = rightGridAdapter
    }

    /**
     * 添加选中的菜品到订单列表中
     */
    private fun addDishToOrderList(position: Int) {
        if (isSame(position)) {
            for (i in listDishOrder.indices) {
                if (listDishOrder[i].id == listDishes[position].id) {
                    listDishOrder[i].count = listDishOrder[i].count + 1
                }
            }
        } else {
            listDishOrder.add(listDishes[position])
        }
        RV.update(listview_left)
        calculateMoney()
    }

    /**
     * 计算需要支付的金额
     */
    private fun calculateMoney() {
        var count = 0
        var totalMoney = 0
        if (listDishOrder.size == 0) {
            tv_total.text = "总计¥${MoneyUtils.changeF2Y(totalMoney * (selectedIndex + 1))}"
            tv_count.text = "数量 ${count * (selectedIndex + 1)}"
            priceForPay = MoneyUtils.changeF2Y(totalMoney).toDouble()
            return
        }
        for (i in listDishOrder.indices) {
            count += listDishOrder[i].count
            totalMoney += listDishOrder[i].count * listDishOrder[i].price
        }
        tv_total.text = "总计¥${MoneyUtils.changeF2Y(totalMoney * (selectedIndex + 1))}"
        priceForPay = MoneyUtils.changeF2Y(totalMoney).toDouble()
        tv_count.text = "数量 ${count * (selectedIndex + 1)}"
    }

    /**
     * 判断菜品是否已经位于已点餐品列表中
     */
    private fun isSame(position: Int): Boolean {
        if (listDishOrder.size == 0) return false
        if (listDishOrder.any { it.id == listDishes[position].id }) return true
        return false
    }

    /**
     * 初始化菜品类别列表视图
     */
    private fun initDishClassListView() {
        val adapter = MyListAdapter(this, listDishClass)
        listview_right.adapter = adapter
        listview_right.onItemClickListener = AdapterView.OnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            selectedClassIndex = position
            adapter.curPositon = position
            adapter.notifyDataSetChanged()
            listview_right.smoothScrollToPositionFromTop(position, (parent.height - view.height) / 2)
            if (listDishClass.size == 0) {
                return@OnItemClickListener
            }
            var childListBeanList = listDishClass[position].getmList()
            if (childListBeanList == null) {
                childListBeanList = ArrayList()
            }
            listDishes.clear()
            listDishes.addAll(childListBeanList)
            RV.update(gridView)
        }
    }

    /**
     * 绑定餐品类别数据
     */
    private fun bindDishClassData(resultsBeanList: List<AllCaiTypeModel.DataBean.ResultsBean>) {
        listDishClass.clear()
        listDishClass.addAll(resultsBeanList)
        RV.update(listview_right)
    }

    /**
     * 绑定餐品数据
     */
    private fun bindAllCaiData(resultsBeanList: List<AllCaiModel.DataBean.ResultsBean>) {
        listDishes.clear()
        listDishes.addAll(resultsBeanList)
        RV.update(gridView)
        for (i in listDishClass.indices) {
            val mList: MutableList<AllCaiModel.DataBean.ResultsBean> = ArrayList()
            for (j in listDishes.indices) {
                if (listDishClass[i].id == listDishes[j].minorCategoryId) {
                    mList.add(listDishes[j])
                }
            }
            listDishClass[i].setmList(mList)
        }
        listDishes.clear()
        listDishes.addAll(listDishClass[selectedClassIndex].getmList())
        RV.update(gridView)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        EventBus.getDefault().unregister(this)
        handler.removeCallbacksAndMessages(null)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_qie -> {
                // 快速收银
                mCamera?.stopPreview()
                mCamera?.setPreviewCallback(null)
                SPUtils.putSP(this,"isDiancan", false)
                Constant.IS_ORDER_DISH = false
                openActivity(MoneyActivity::class.java)
                finish()
            }
            R.id.tv_jiesuan -> {
                // 结算
                var payMoney = 0
                if (listDishOrder.size == 0) {
                    payMoney = 0
                } else {
                    var i = 0
                    while (i < listDishOrder.size) {
                        payMoney += listDishOrder[i].count * listDishOrder[i].price
                        i++
                    }
                }
                if (payMoney == 0) {
                    showToast("请添加菜")
                    return
                }
                val df = DecimalFormat("#0.00")
                payResultPop.showPop(0, priceForPay, priceForPay)
            }
            R.id.tv_baobiao ->                 // 订单统计
                Req.reqOrderCount(dialogLoading) { list: List<OrderCountModel.DataBean?>? ->
                    dialogOrderStat?.updateAndShow(list)
                }
            R.id.tv_refresh ->                 // 更新菜品
                Req.reqDishesTypes(true, { list: List<AllCaiTypeModel.DataBean.ResultsBean> -> bindDishClassData(list) })
                { list: List<AllCaiModel.DataBean.ResultsBean> -> bindAllCaiData(list) }
            else -> {
            }
        }
    }

    companion object {
        /**
         * 当前选中的XNumber列表索引位
         */
        @JvmField
        var selectedIndex = 0
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent!!.action) {
            processIntent(this, intent)
        }
    }

    fun processIntent(activity: OrderDishActivity, intent: Intent) {
        //拿来装读取出来的数据，key代表扇区数，后面list存放四个块的内容
        val map: Map<String, List<String>> = HashMap()
        //intent就是onNewIntent方法返回的那个intent
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        val mfc = MifareClassic.get(tag)
        //如果当前IC卡不是这个格式的mfc就会为空
        Log.d("Test", "tag=== ： $tag")
        if (null != mfc) {
            try {
                //链接NFC
                mfc.connect()
                //获取扇区数量
                val count = mfc.sectorCount
                var flag = false
                val bytes = byteArrayOf(0x94.toByte(), 0x04.toByte(), 0x30.toByte(),
                        0xDe.toByte(), 0xf3.toByte(), 0x06.toByte())

//                bytes = "940430Def306".getBytes();
//                    //验证扇区密码，否则会报错（链接失败错误）
//                    //这里验证的是密码A，如果想验证密码B也行，将方法中的A换成B就行
//                boolean isOpen = mfc.authenticateSectorWithKeyB(0, bytes);
                var isOpen = mfc.authenticateSectorWithKeyA(0, bytes)
                if (isOpen == false) {
                    val bytesNew = byteArrayOf(0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
                            0xff.toByte(), 0xff.toByte(), 0xff.toByte())
                    isOpen = mfc.authenticateSectorWithKeyA(0, bytesNew)
                }
                if (isOpen) {
//                        //获取扇区里面块的数量
                    val bCount = mfc.getBlockCountInSector(0)
                    //获取扇区第一个块对应芯片存储器的位置
                    //（我是这样理解的，因为第0扇区的这个值是4而不是0）
                    val bIndex = mfc.sectorToBlock(0)
                    //读取数据，这里是循环读取全部的数据
                    //如果要读取特定扇区的特定块，将i，j换为固定值就行
                    val data = mfc.readBlock(bIndex + 0)
                    //                    showToast("读取扇区 == " + ReaderUtils.byteToString(data));
                    val cardNo = ReaderUtils.byteToString(data)
                    waitCustomerNfcPay(activity, cardNo)
                    flag = true
                } else {
                    com.yp.payment.order.layer.pay.ToastUtils.toast("无法识别卡")
                    activity.lcdShowText("卡无效")
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            activity.lcdShowText("云澎智能")
                        }
                    }, 2000)
                }
            } catch (e: java.lang.Exception) {
                com.yp.payment.order.layer.pay.ToastUtils.toast("mfc连接失败：" + e.localizedMessage)
                e.printStackTrace()
            } finally {
                try {
                    mfc.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun waitCustomerNfcPay(activity: OrderDishActivity, nfc: String?) {
        if (!Constant.startPay) {
            val getBalanceRequest = GetBalanceRequest()
            getBalanceRequest.cardNo = nfc
            getBalanceRequest.shopId = Constant.shopId
            getBalanceRequest.deviceId = LoginActivity.deviceId
            MyRetrofit.getApiService().getBanlance(getBalanceRequest).enqueue(object : MyCallback<GetBalanceResponse>() {
                override fun onSuccess(response: GetBalanceResponse) {
                    Constant.startPay = false
                    if (response.code == 200) {
                        Constant.payUser = response.data.username
                        Constant.payBanlance = PriceUtil.changeF2Y(java.lang.Long.valueOf(response.data.balance)) + "元"
                        activity.lcdTwoLine()
                    } else {
                        activity.lcdShowText(response.message)
                    }
                    timer?.schedule(object : TimerTask() {
                        override fun run() {
                            runOnUiThread { lcdShowText("云澎科技") }
                        }
                    }, 2000)
                }
            })
            return
        }
        //        if (payMode == 1) {
        if (activity.payResultPop != null && !TextUtils.isEmpty(nfc)) {
            if (activity.payResultPop.isShowing) {
                activity.payResultPop.setNfcResult(nfc)
            }
        }
//        }
    }

    override fun onResume() {
        super.onResume()
        if (mDSKernel != null) {
            mDSKernel?.checkConnection()
        } else {
            initSdk()
        }
        mNfcAdapter.enableForegroundDispatch(this, pi, null, null) //启动

    }

    private fun initSdk() {
        mDSKernel = DSKernel.newInstance()
        mDSKernel?.init(this, mIConnectionCallback)
        mDSKernel?.addReceiveCallback(mIReceiveCallback)
        mDSKernel?.addReceiveCallback(mIReceiveCallback2)
        mDSKernel?.removeReceiveCallback(mIReceiveCallback)
        mDSKernel?.removeReceiveCallback(mIReceiveCallback2)
    }

    private val mIConnectionCallback = object : IConnectionCallback {
        override fun onDisConnect() {
            val message = Message()
            message.what = 1
            message.obj = "与远程服务连接中断"
           ToastUtils.toast("与远程服务连接中断")
        }

        override fun onConnected(state: ConnState) {
            val message = Message()
            message.what = 1
            when (state) {
                ConnState.AIDL_CONN -> message.obj = "与远程服务绑定成功"
                ConnState.VICE_SERVICE_CONN -> message.obj = "与副屏服务通讯正常"
                ConnState.VICE_APP_CONN -> message.obj = "与副屏app通讯正常"
                else -> {
                }
            }
            ToastUtils.toast(message.obj.toString())
        }
    }

    private val mIReceiveCallback: IReceiveCallback = object : IReceiveCallback {
        override fun onReceiveData(data: DSData) {}
        override fun onReceiveFile(file: DSFile) {}
        override fun onReceiveFiles(files: DSFiles) {}
        override fun onReceiveCMD(cmd: DSData) {}
    }

    private val mIReceiveCallback2: IReceiveCallback = object : IReceiveCallback {
        override fun onReceiveData(data: DSData) {}
        override fun onReceiveFile(file: DSFile) {}
        override fun onReceiveFiles(files: DSFiles) {}
        override fun onReceiveCMD(cmd: DSData) {}
    }
    
    lateinit var surface_view:SurfaceView
    lateinit var finder_view:FinderView
    lateinit var mHolder:SurfaceHolder
    lateinit var autoFocusHandler:Handler

    private fun initCamera() {
        surface_view = findViewById<SurfaceView>(R.id.surface_view)
        finder_view = findViewById<FinderView>(R.id.finder_view)
        mHolder = surface_view.holder
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        mHolder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                if (mHolder.surface == null) {
                    return
                }
                try {
                    mCamera?.stopPreview()
                } catch (e: java.lang.Exception) {
                }
                try {
                    //摄像头预览分辨率设置和图像放大参数设置，非必须，根据实际解码效果可取舍
//			Camera.Parameters parameters = mCamera.getParameters();
//            parameters.setPreviewSize(800, 480);  //设置预览分辨率
                    //     parameters.set("zoom", String.valueOf(27 / 10.0));//放大图像2.7倍
//            mCamera.setParameters(parameters);
                    mCamera?.setDisplayOrientation(90) //竖屏显示
                    mCamera?.setPreviewDisplay(mHolder)
                    mCamera?.setPreviewCallback(Camera.PreviewCallback { data, camera ->
                        if (asyncDecode.isStoped) {
                            val parameters = camera.parameters
                            val size = parameters.previewSize //获取预览分辨率

                            //创建解码图像，并转换为原始灰度数据，注意图片是被旋转了90度的
                            val source = Image(size.width, size.height, "Y800")
                            val scanImageRect = finder_view.getScanImageRect(size.height, size.width)
                            //图片旋转了90度，将扫描框的TOP作为left裁剪
                            source.setCrop(scanImageRect.top, scanImageRect.left, scanImageRect.height(), scanImageRect.width())
                            source.data = data //填充数据
                            asyncDecode = AsyncDecode()
                            asyncDecode.execute(source) //调用异步执行解码
                        }
                    })

                    mCamera?.startPreview()
                    mCamera?.autoFocus(autoFocusCallback)
                } catch (e: java.lang.Exception) {
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                if (mCamera != null) {
                    mCamera?.setPreviewCallback(null)
                    mCamera?.release()
                    mCamera = null
                }
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                try {
                    mCamera = Camera.open(0)
                } catch (e: java.lang.Exception) {
                    mCamera = null
                    showToast("相机获取失败！")
                }
            }

        })
        scanner = ImageScanner() //创建扫描器
        scanner.setConfig(0, Config.X_DENSITY, 2) //行扫描间隔
        scanner.setConfig(0, Config.Y_DENSITY, 2) //列扫描间隔
        scanner.setConfig(0, Config.ENABLE_MULTILESYMS, 0) //是否开启同一幅图一次解多个条码,0表示只解一个，1为多个
        scanner.setConfig(0, Config.ENABLE_INVERSE, 0) //是否解反色的条码
        scanner.setConfig(Symbol.PDF417, Config.ENABLE, 0) //是否禁止PDF417码，默认开启
        autoFocusHandler = Handler()
        asyncDecode = AsyncDecode()
    }

    val autoFocusCallback = object : Camera.AutoFocusCallback {
        override fun onAutoFocus(success: Boolean, camera: Camera?) {
            autoFocusHandler.postDelayed(Runnable {
                if (null == mCamera || null == this) {
                    return@Runnable
                }
                mCamera!!.autoFocus(this)
            }, 1000)
        }

    }

    inner class AsyncDecode : AsyncTask<Image?, Void?, String?>() {
        var isStoped = true
            private set
        private val str: String? = ""

        override fun doInBackground(vararg params: Image?): String? {
            isStoped = false
            val src_data = params[0] //获取灰度数据
            val startTimeMillis = System.currentTimeMillis()

            //解码，返回值为0代表失败，>0表示成功
            val nsyms: Int = scanner.scanImage(src_data)
            val endTimeMillis = System.currentTimeMillis()
            val cost_time = endTimeMillis - startTimeMillis
            if (nsyms != 0) {
//                playBeepSoundAndVibrate() //解码成功播放提示音
                var qrCode = ""
                val syms: SymbolSet = scanner.getResults() //获取解码结果
                for (sym in syms) {
                    qrCode = sym.result
                }
                return qrCode
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (TextUtils.isEmpty(result)) {
                isStoped = true
            } else {

                //扫码成功，等待两秒后再去扫描
                Thread(Runnable {
                    try {
                        Thread.sleep(2000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    isStoped = true
                }).start()
                waitCustomerQrCodePay(result)
                //                showToast("成功支付");
            }
            if (null == str || str == "") {
//                Log.d(TAG, "onPostExecute: 没有识别");
            } else {
//                Log.d(TAG, "onPostExecute: 扫描结果 == " + str);
//                showToast(str);
//                tv_scan_result.setText(str);//显示解码结果
            }
        }

    }

    /**
     * 等待客户刷码支付
     */
    fun waitCustomerQrCodePay(payCode: String?) {
        if (!Constant.startPay) {
            playMusic(5)
            return
        }
        //如果是nfc支付
//        if (payMode == 2 || payMode == 3) {
        if (payResultPop != null && !TextUtils.isEmpty(payCode)) {
            if (payResultPop.isShowing) {
                payResultPop.setQrcodeResult(payCode)
            }
        }
//        }
    }

    fun printOrder(orderDetail: OrderDetail?) {
        // 1: Get BluetoothAdapter
        val btAdapter = BluetoothUtil.getBTAdapter()
        if (btAdapter == null) {
            Toast.makeText(baseContext, "Please Open Bluetooth!", Toast.LENGTH_LONG).show()
            return
        }
        if (!btAdapter.isEnabled) {
            btAdapter.enable()
            showToast("正在打开蓝牙")
        }
        // 2: Get Sunmi's InnerPrinter BluetoothDevice
        val device = BluetoothUtil.getDevice(btAdapter)
        if (device == null) {
            Toast.makeText(baseContext, "Please Make Sure Bluetooth have InnterPrinter!",
                    Toast.LENGTH_LONG).show()
            return
        }

        // 3: Generate a order data
        val data = ESCUtil.generateMockData(orderDetail)
        // 4: Using InnerPrinter print data
        var socket: BluetoothSocket? = null
        try {
            socket = BluetoothUtil.getSocket(device)
            BluetoothUtil.sendData(data, socket)
        } catch (e: IOException) {
            if (socket != null) {
                try {
                    socket.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
        }
    }

    fun printOrder() {
        val orderDetail = Constant.curOrderList[Constant.curOrderSeq]
        printOrder(orderDetail)
    }


}