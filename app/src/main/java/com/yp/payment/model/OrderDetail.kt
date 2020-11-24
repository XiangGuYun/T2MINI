package com.yp.payment.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class OrderDetail{
    @Id
    var id: Long = 0
    var shopId: Int? = null
    var cashierDeskId: Int? = null
    var orderNo: String? = null
    var dateTime: String? = null
    var orderType: Int? = null
    var orderTypeStr: String? = null
    var price: String? = null
    var realPrice: String? = null
    var discountPrice: String? = null

    /**
     * 是否采用离线支付
     */
    var isPayByOffline:Boolean = false

    /**
     * 卡号
     */
    var cardNo:String = ""
}