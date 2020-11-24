package com.yp.payment.new

import android.app.Activity
import com.yp.baselib.utils.BusUtils
import com.yp.baselib.utils.TimerUtils
import com.yp.payment.Constant
import com.yp.payment.internet.ShangMiOrderRequest
import com.yp.payment.model.OrderDetail
import io.objectbox.Box
import java.math.BigDecimal

/**
 * 离线上传帮助类
 */
class OfflineUploadHelper private constructor() {

    companion object {
        private var helper: OfflineUploadHelper? = null

        fun getInstance(): OfflineUploadHelper {
            if (helper == null) {
                helper = OfflineUploadHelper()
            }
            return helper!!
        }
    }

    var isPaying = false

    fun pay(activity: Activity) {
        val cardPayBox = ObjectBox.boxStore.boxFor(OrderDetail::class.java)
        TimerUtils.schedule(1000, 1000 * 60 * 5) {
           synchronized(activity){
               val list = cardPayBox.all
               if (!list.any { it.isPayByOffline }) {
                   it.cancel()
               }
               list.forEach { order ->
                   if (order.isPayByOffline) {
                       doPay(cardPayBox, ShangMiOrderRequest().apply {
                           cardNo = order.cardNo
                           payType = 2
                           shopID = Constant.shopId
                           isOffline = 1
                           cashierDeskID = Constant.cashierDeskId
                           accountBalance = BigDecimal(order.price).multiply(BigDecimal(100)).toLong()
                       }, order)
                       Thread.sleep(500)
                   }
               }
           }
        }
    }

    /**
     * 支付操作
     * @param req ShangMiOrderRequest
     */
    private fun doPay(cardPayBox: Box<OrderDetail>, req: ShangMiOrderRequest, cardPayBean: OrderDetail) {
        Req.pay(req, { username, orderNo, payType ->
            // 支付成功
            // 更新数据库
            cardPayBean.isPayByOffline = false
            cardPayBox.put(cardPayBean)
            BusUtils.post(0x001)
        }, {
            // 支付失败接着等待支付

        })
    }
}