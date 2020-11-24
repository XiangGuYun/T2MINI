package com.yp.payment.new

data class PayResult(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val cashierDeskId: Int,
        val customerId: Int,
        val customerName: String,
        val customerPhone: String,
        val id: Int,
        val itemCount: Int,
        val note: Any,
        val nutritionList: Any,
        val orderItems: List<OrderItem>,
        val orderNo: String,
        val payCard: PayCard,
        val payStatus: Int,
        val payTime: Long,
        val payType: Int,
        val productOrderUrl: Any,
        val qrCode: Any,
        val realFee: Int,
        val refundFee: Int,
        val refundStatus: Int,
        val refundTime: Long,
        val serial: Int,
        val serialNumber: String,
        val shopId: Int,
        val shopName: String,
        val smkaccountBalance: Any,
        val totalFee: Int
    ) {
        data class OrderItem(
            val calorie: Any,
            val hasInfo: Boolean,
            val mealPrice: Any,
            val price: Int,
            val productId: Int,
            val productName: String,
            val quantity: Int,
            val realPrice: String,
            val warmPrice: Any
        )

        data class PayCard(
            val accountbalance: Int,
            val bzAccountbalance: Int,
            val cardno: String,
            val cardtype: Int,
            val contractCode: Any,
            val contractId: Any,
            val created: Long,
            val customerid: Int,
            val deleted: Boolean,
            val id: Int,
            val ismaster: Boolean,
            val isnopasswordpay: Boolean,
            val name: String,
            val payCode: String,
            val payCodeTime: Long,
            val shopid: Int,
            val updated: Long,
            val wxAccountbalance: Int,
            val yktAccountbalance: Int
        )
    }
}