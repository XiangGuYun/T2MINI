package com.yp.payment.update

/**
 * 管理所有接口
 */
object URL {

    /**
     * 默认域名IP
     */
    private const val DEFAULT_BASE_URL = "https://binguoai.com"

    /**
     * 域名IP
     */
    @JvmField
    var BASE_URL = SPHelper.getIpAddress()

    /**
     * 快速收银+点餐模式 - 获取商米订单
     */
    const val GET_SM_ORDER = "/api/order/getShangMiOrder/"

    /**
     * 快速收银 - 支付(创建订单)
     */
    const val CREATE_SM_ORDER = "/api/order/createShangMi"

    /**
     * 快速收银 - 获取余额
     */
    const val GET_BALANCE = "/zjypg/getBalance"

    /**
     * 三角收银机 - 登录
     */
    const val SM_INIT_V2 = "/zjypg/shangmiInitV2"

    /**
     * 三角收银机 - 获取分店列表
     */
    const val BRANCH_LIST = "/api/branch/list"

    /**
     * 返回单个人脸数据
     */
    const val GET_FACE = "/zjypg/getCustomerList"

    /**
     * 返回所有人脸数据
     */
    const val GET_ALL_FACES = "/zjypg/getCustomerListNew"

    /**
     * 是否有更新变动的数据
     */
    const val SYNC_DATA = "/zjypg/syncData"

    /**
     * 得到广告图
     */
    const val GET_BANNER_IMAGES = "/zjypg/getShopConfig"

    /**
     * 获取百度密钥 授权码
     */
    const val GET_BAI_DU_SDK_SERIAL = "/api/wxapp/aiyouwei/getBaiduSDKSerial"

    const val ISQLQUERY = "/api/wxapp/openapi/iSqlquery"

}