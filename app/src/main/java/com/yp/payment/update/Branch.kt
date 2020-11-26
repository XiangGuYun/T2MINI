package com.yp.payment.update

data class Branch(
    val code: Int,
    val `data`: List<Data>,
    val message: String
) {
    data class Data(
        val aliMchId: Any,
        val branchId: Int,
        val branchName: String,
        val branchPricetype: Int,
        val shopId: Int,
        val subAppid: Any,
        val subMchId: Any,
        val webBranchId: Any
    )
}