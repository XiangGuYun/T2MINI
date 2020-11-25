package com.yp.payment.update

import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {

    /**
     * 保存订单数据到本地数据库
     * @param ordertype
     * @param money
     * @param orderno
     */
    @JvmStatic
    fun addOrder(ordertype: String?, money: String?, orderno: String?, cardNo:String = "", isOffline:Boolean = false) {
        val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss") // HH:mm:ss
        val date = Date(System.currentTimeMillis())
        val orderDetail = OrderDetail()
        orderDetail.orderNo = orderno
        orderDetail.orderTypeStr = ""
        orderDetail.orderType = Integer.valueOf(ordertype!!)
        orderDetail.price = money
        orderDetail.realPrice = money
        orderDetail.discountPrice = ""
        orderDetail.shopId = SPHelper.getShopId()
        orderDetail.dateTime = simpleDateFormat.format(date)
        orderDetail.cardNo = cardNo
        orderDetail.isPayByOffline = isOffline
//        val orderDetailBox = ObjectBox.boxStore.boxFor(OrderDetail::class.java)
//        orderDetailBox.put(orderDetail)
    }


}