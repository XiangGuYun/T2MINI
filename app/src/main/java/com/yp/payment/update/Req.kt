package com.yp.payment.update

import android.app.Activity
import android.os.Build
import com.yp.baselib.utils.ToastUtils
import com.yp.payment.Constant
import com.yp.payment.internet.LoginRequest
import com.yp.payment.internet.LoginResponseV2
import com.yp.payment.internet.ShangMiOrderRequest


/**
 * 管理所有接口请求
 */
object Req  {

    /**
     * 三角收银机 - 登录
     */
    fun login(activity: Activity,
              username: String?,
              password: String?,
              onSuccess: () -> Unit,
              onFailure: () -> Unit) {
        val loginRequest = LoginRequest().apply {
            deviceId = Build.SERIAL
            this.username = username
            this.password = password
//            deviceType = 13
        }

        com.yp.baselib.utils.OK.post<LoginResponseV2>("${URL.BASE_URL}${URL.SM_INIT_V2}", loginRequest) { loginResponse ->
            if (loginResponse.code == 200) {
                //保存基本信息
                Constant.shopId = loginResponse.data.shopId
                Constant.cashierDeskId = loginResponse.data.cashierDeskId
                Constant.curUsername = loginRequest.username
                SPHelper.putCurUsername(loginRequest.username)
                Constant.shopName = loginResponse.data.shopName
                SPHelper.putShopName(loginResponse.data.shopName)
//                Constant.employeeId = loginResponse.data.employeeId
                //保存信息用于下次自动登陆
//                SharedUtil.getInstatnce().getShared(activity).edit()
//                        .putString("username", username)
//                        .putString("password", password).apply()
                onSuccess.invoke()
            } else {
                onFailure.invoke()
                ToastUtils.toast("登录失败 ${loginResponse.message}")
            }
        }
    }

    /**
     * 快速收银 - 支付
     */
    fun pay(req: ShangMiOrderRequest,
            onSuccess: (username: String, orderNo: String, payType: String) -> Unit,
            onFailure: (message: String) -> Unit) {
        //music:支付中
        OK.post<PayResult>("${URL.BASE_URL}${URL.CREATE_SM_ORDER}", req) {
            if (it.code == 200) {
                // music:支付成功
                onSuccess.invoke(
                        it.data.customerName,
                        it.data.orderNo,
                        it.data.payType.toString())
            } else {
                // 支付失败接着等待支付
                onFailure.invoke(it.message)
            }
        }
    }

}