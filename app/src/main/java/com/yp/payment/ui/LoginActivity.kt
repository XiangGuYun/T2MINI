package com.yp.payment.ui

import android.Manifest
import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseActivity
import com.yp.baselib.helper.NetListenHelper
import com.yp.baselib.utils.NetUtils.isNetConnected
import com.yp.baselib.utils.PermissionUtils
import com.yp.payment.Constant
import com.yp.payment.Consts
import com.yp.payment.MainActivity
import com.yp.payment.R
import com.yp.payment.dao.ShopConfigDao
import com.yp.payment.http.MyCallback
import com.yp.payment.internet.LoginRequest
import com.yp.payment.internet.LoginResponseV2
import com.yp.payment.internet.MyRetrofit
import com.yp.payment.update.SPHelper
import com.yp.payment.utils.GsonUtil
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call

/**
 * @author : cp
 * @email : ibsfiq@qq.com
 * @date : 2019/8/21 14:07
 * @description ：管理员登录页
 */
@LayoutId(R.layout.activity_login)
class LoginActivity : BaseActivity() {
    private val REQUEST_PERMISSION_CODE = 20001
    var shopConfigDao: ShopConfigDao? = null
    private var netHelper: NetListenHelper? = null


    fun loginAdmin() {
        val user_account = edit_user_account!!.text.toString().trim { it <= ' ' }
        val user_psw = edit_user_psw!!.text.toString().trim { it <= ' ' }
        val loginRequest = LoginRequest()
        loginRequest.deviceId = deviceId
        loginRequest.username = user_account
        loginRequest.password = user_psw
        Log.d(TAG, "loginRequest==" + GsonUtil.GsonString(loginRequest))
//        Req.login(this, user_account, user_psw, {
//            shopConfigDao!!.insertData(SPHelper.getShopId(), SPHelper.getCashierDeskId(),
//                    SPHelper.getShopName(), SPHelper.getCurUsername())
//            goTo<MoneyActivity>()
//        }, {
//            "失败".toast()
//        })
        val pd = ProgressDialog(this)
        pd.setMessage("正在登录...")
        pd.show()
        MyRetrofit.getApiService().initV2(loginRequest).enqueue(object : MyCallback<LoginResponseV2?>() {
            override fun onSuccess(loginResponse: LoginResponseV2?) {
                pd.dismiss()
                Log.d(TAG, "loginResponse==$loginResponse")
                if (loginResponse?.code == 200) {
                    SPHelper.putShopId(loginResponse.data.shopId)
                    SPHelper.putCashierDeskId(loginResponse.data.cashierDeskId)
                    SPHelper.putCurUsername(loginRequest.username)
                    SPHelper.putShopName(loginResponse.data.shopName)

                    Constant.shopId = loginResponse.data.shopId
                    Constant.cashierDeskId = loginResponse.data.cashierDeskId
                    Constant.curUsername = loginRequest.username
                    Constant.shopName = loginResponse.data.shopName

                    if (loginResponse.data.cashAllow != null &&
                            loginResponse.data.cashAllow.toInt() == 1) {
                        Consts.payModes = Consts.payModesWithCash
                        Consts.payModeIcons = Consts.payModeIconsWithCash
                    } else {
                        Consts.payModes = Consts.payModesWithNoCash
                        Consts.payModeIcons = Consts.payModeIconsWithNoCash
                    }

//                    ObjectBox.boxStore.boxFor(ShopConfig::class.java)?.let {
//                        it.put(ShopConfig().apply {
//                            shopId = loginResponse.data.shopId
//                            cashierDeskId = loginResponse.data.cashierDeskId
//                            shopName = loginResponse.data.shopName
//                            username = loginRequest.username
//                        })
//                        it.all.forEach {
//                            it.toString().logD("TEST123")
//                        }
//                    }

                    shopConfigDao!!.insertData(loginResponse.data.shopId, loginResponse.data.cashierDeskId,
                            loginResponse.data.shopName, loginRequest.username)
                    goTo<MoneyActivity>()
                }
            }

            override fun onFailure(call: Call<LoginResponseV2?>, t: Throwable) {
                super.onFailure(call, t)
                pd.dismiss()
                t.localizedMessage.toast()
                Log.d(TAG, "loginResponse onFailure==" + t.message)
            }
        })
    }

    fun initData() {
        shopConfigDao = ShopConfigDao(this)
        val shopConfig = shopConfigDao!!.query()
        if (shopConfig != null && shopConfig.autoLogin == 1 && isNetConnected(this)) {
            Consts.payModes = Consts.payModesWithNoCash
            Consts.payModeIcons = Consts.payModeIconsWithNoCash
            goTo<MoneyActivity>(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        netHelper!!.unregister(this)
    }

    override fun init(bundle: Bundle?) {
        netHelper = NetListenHelper()
        netHelper!!.register(this,
                {
                    show(edit_user_account, edit_user_psw)
                    btn_login_user.text = "登录"
                }
        ) {
            hide(edit_user_account, edit_user_psw)
            btn_login_user.text = "离线登录"
        }
        deviceId = Build.SERIAL
        initData()
        btn_login_user.click { loginAdmin() }
        PermissionUtils.req(this, null, null, null,
                *PERMISSIONS_STORAGE)

        tv_ip.click {
            goTo<IpSetActivity>()
        }

    }

    companion object {
        private const val TAG = "LoginActivity"
        private val PERMISSIONS_STORAGE = arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION)

        @JvmField
        var deviceId: String? = null
    }
}