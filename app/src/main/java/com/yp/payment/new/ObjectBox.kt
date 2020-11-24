package com.yp.payment.new

import android.content.Context
import com.yp.payment.model.MyObjectBox
import io.objectbox.BoxStore

/**
 * MyObjectBox:基于您的实体类生成，MyObjectBox提供了一个生成器，
 * 用于为您的应用程序设置BoxStore。
 *
 * BoxStore:使用ObjectBox的入口点。BoxStore是到数据库和管理Boxes的直接接口。
 *
 * Box:Box持续存在并查询实体。对于每个实体，都有一个Box(由BoxStore提供)。
 *
 * 应用程序的BoxStore是使用生成的MyObjectBox类返回的builder来初始化的，
 * 例如在一个小助手类中，如下所示:
 */
object ObjectBox {

    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
    }
}