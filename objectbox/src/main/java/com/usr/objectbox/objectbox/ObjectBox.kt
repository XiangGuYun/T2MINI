package com.usr.objectbox.objectbox

import android.content.Context
import io.objectbox.Box
import io.objectbox.BoxStore

/**
 * MyObjectBox:基于您的实体类生成，MyObjectBox提供了一个生成器，用于为您的应用程序设置BoxStore。
 *
 */
object ObjectBox {

    /**
     * 使用ObjectBox的入口点。BoxStore是到数据库和管理Boxes的直接接口
     *
     * 基本操作
     * put：插入一个新的或用相同的ID更新一个已有的对象，支持放置多个对象
     *
     * get和getAll：给定对象的ID，将其从Box中读取回来。使用getAll来获取盒子中的所有对象。
     *
     * query：构建查询，以从框中返回符合特定条件的对象
     * userBox.query()
     *      .equal(User_.name, "Tim")
     *      .order(User_.name)
     *      .build()
     *      .find()
     *
     * remove和removeAll：将先前放置的对象从Box中移除(删除它)，remove还支持删除多个对象
     *
     * count：返回此Box中存储的对象的数量
     */
    @JvmStatic
    lateinit var boxStore: BoxStore
        private set

    /**
     * 在Application中初始化
     * @param context Context
     */
    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
    }

    /**
     * Box持续存在并查询实体。对于每个实体，都有一个Box(由BoxStore提供)
     * @return Box<T>?
     */
    @JvmStatic
    inline fun <reified T> getBox(): Box<T>? {
        return boxStore.boxFor(T::class.java)
    }
}