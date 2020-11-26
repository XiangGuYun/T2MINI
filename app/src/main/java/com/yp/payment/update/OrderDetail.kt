//package com.yp.payment.update
//
//import org.greenrobot.greendao.annotation.Entity
//import org.greenrobot.greendao.annotation.Id
//import org.greenrobot.greendao.annotation.Index
//
//@Entity(indexes = { @Index(value = "text, date DESC", unique = true) })
//class OrderDetail{
//    @Id
//    var id: Long = 0
//    var shopId: Int? = null
//    var cashierDeskId: Int? = null
//    var orderNo: String? = null
//    var dateTime: String? = null
//    var orderType: Int? = null
//    var orderTypeStr: String? = null
//    var price: String? = null
//    var realPrice: String? = null
//    var discountPrice: String? = null
//
//    /**
//     * 是否采用离线支付
//     */
//    var isPayByOffline:Boolean = false
//
//    /**
//     * 卡号
//     */
//    var cardNo:String = ""
//}