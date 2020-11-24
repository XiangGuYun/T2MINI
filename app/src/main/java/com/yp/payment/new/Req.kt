package com.yp.payment.new

import com.yp.payment.internet.ShangMiOrderRequest


/**
 * 管理所有接口请求
 */
object Req  {

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