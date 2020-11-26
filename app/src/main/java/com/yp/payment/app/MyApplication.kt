package com.yp.payment.app

import android.content.Context
import com.yp.baselib.base.BaseApplication
import com.yp.baselib.helper.ExceptionHelper
import com.yp.baselib.utils.http.OkHttpUtils
import com.yp.payment.order.layer.CrashHandler
import com.yp.payment.update.DaoMaster
import com.yp.payment.update.DaoMaster.DevOpenHelper
import com.yp.payment.update.DaoSession
import okhttp3.OkHttpClient
import org.xutils.x
import java.util.concurrent.TimeUnit


/**
 * Created by 20191024 on 2020/1/6.
 */
class MyApplication : BaseApplication() {

    private lateinit var daoSession: DaoSession

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        application = this
        x.Ext.init(this)
        appContext = this.applicationContext
        val handler = CrashHandler.getInstance()
        Thread.setDefaultUncaughtExceptionHandler(handler)
        ExceptionHelper.getInstance().init()
//        ObjectBox.init(this)
        initOKHttp()
        initGreenDAO()
    }

    private fun initGreenDAO() {
        val helper = DevOpenHelper(this, "order_detail-db")
        val db = helper.writableDb
        daoSession = DaoMaster(db).newSession()
    }

    companion object {
        @JvmStatic
        var application: MyApplication? = null
            private set
        @JvmStatic
        var appContext: Context? = null
            private set
    }

    private fun initOKHttp() {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .sslSocketFactory(HttpsHelp.createSSLSocketFactory())
//                .hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true })
                .retryOnConnectionFailure(true) //其他配置
                .build()
        OkHttpUtils.initClient(okHttpClient)
    }

}