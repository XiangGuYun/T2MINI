package com.yp.baselib.utils

import com.google.gson.Gson
import com.yp.baselib.other.HttpsHelper
import com.yp.baselib.utils.http.OkHttpUtils
import com.yp.baselib.utils.http.callback.StringCallback
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLSession

/**
 * OKHTTP工具类
 */
object ProjectOK {

    fun initInApplication(){
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .sslSocketFactory(HttpsHelper.createSSLSocketFactory())
//                .hostnameVerifier { hostname: String?, session: SSLSession? -> true }
                .retryOnConnectionFailure(true) //其他配置
                .build()
        OkHttpUtils.initClient(okHttpClient)
    }

    /**
     * 表示所传参数被忽略
     */
    const val OPTIONAL = "optional"

    const val TAG = "OK_Result"

    const val MEDIA_TYPE = "application/json; charset=utf-8"

    /**
     * post请求
     */
    inline fun <reified T> postJson(
            url: String, //URL
            crossinline onSuccess: (data: T) -> Unit, //成功回调
            vararg pairs: Pair<String, String>//参数
    ) {
        LogUtils.d(TAG, "url is $url");
        val mapJson = Gson().toJson(HashMap(pairs.toMap()).filterValues { it != OPTIONAL })
        val builder = OkHttpUtils
                .postString()
                .url(url)
                .content(mapJson)
                .mediaType(MEDIA_TYPE.toMediaTypeOrNull())

        LogUtils.d(TAG, "mapJson is $mapJson");

        builder.build()
                .connTimeOut(6000)
                .readTimeOut(6000)
                .writeTimeOut(6000)
                .execute(object : StringCallback() {

                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        LogUtils.d(TAG, "error is ${e?.localizedMessage}");
                        call?.cancel()
                    }

                    override fun onResponse(response: String?, id: Int) {
                        LogUtils.d(TAG, response!!)
                        onSuccess.invoke(Gson().fromJson(response, T::class.java))
                    }
                })

    }

    /**
     * get请求
     */
    inline fun <reified T> get(
            url: String,
            crossinline onSuccess: (data: T) -> Unit,
            vararg params: Pair<String, String>) {
        val req = OkHttpUtils
                .get()
                .url(url)

        params.forEach {
            req.addParams(it.first, it.second)
        }

        req.build()
                .execute(object : StringCallback() {
                    override fun onError(call: Call?, e: java.lang.Exception?, id: Int) {
                        LogUtils.d(TAG, "error is ${e?.localizedMessage}");
                    }

                    override fun onResponse(response: String?, id: Int) {
                        LogUtils.d(TAG, response!!)
                        onSuccess.invoke(Gson().fromJson(response, T::class.java))
                    }
                })
    }

}