package com.yp.baselib.utils

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView

/**
 * 自定义字体工具类
 */
object FontsUtils {

    /**
     * 为TextView设置字体
     * @param tv TextView
     * @param assetsPath String
     */
    fun setFont(tv: TextView, assetsPath: String) {
        val typeface = Typeface.createFromAsset(tv.context.assets, assetsPath)
        tv.typeface = typeface
    }

    /**
     * 设置全局字体，一般在Application中调用
     * @param context
     * @param fontAssetName           替换后的字体样式
     * @param staticTypefaceFieldName 需要替换的系统字体样式
     */
    fun setDefaultFont(
            context: Context,
            fontAssetName: String?,
            staticTypefaceFieldName: String = "DEFAULT",
    ) {
        // 根据路径得到Typeface
        val regular = Typeface.createFromAsset(context.assets, fontAssetName)
        // 设置全局字体样式
        replaceFont(staticTypefaceFieldName, regular)
    }

    private fun replaceFont(staticTypefaceFieldName: String, newTypeface: Typeface) {
        try {
            val staticField = Typeface::class.java.getDeclaredField(staticTypefaceFieldName)
            staticField.isAccessible = true
            //替换系统字体样式
            staticField[null] = newTypeface
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}