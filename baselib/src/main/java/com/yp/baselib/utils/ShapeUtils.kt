package com.yp.baselib.utils

import android.graphics.Color
import android.graphics.drawable.GradientDrawable

/**
 * 在代码中实现Shape绘制的drawable
 * 参考：https://www.cnblogs.com/guanxinjing/p/11142599.html
 * 结合DrawableEx可实现渐变色背景
 */
object ShapeUtils {

    /**
     * 获取直线或虚线drawable(虚线需要关闭硬件加速)
     */
    fun getLineDrawable(
        lineWidth: Int,
        lineColor: Int,
        dashWidth: Float = -1f,
        dashGap: Float = -1f
    ): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.LINE
        if (dashWidth == -1f || dashGap == -1f) {
            gradientDrawable.setStroke(lineWidth, lineColor)
        } else {
            gradientDrawable.setStroke(lineWidth, lineColor, dashWidth, dashGap)
        }
        return gradientDrawable
    }

    /**
     * 获取椭圆或圆形或圆环drawable
     */
    fun getOvalDrawable(
        ovalWidth: Int,
        ovalHeight: Int,
        ovalColor: Int,
        strokeWidth: Int = 0,
        strokeColor: Int = Color.TRANSPARENT,
        dashWidth: Float = -1f,
        dashGap: Float = -1f
    ): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.OVAL
        gradientDrawable.setColor(ovalColor)
        gradientDrawable.setSize(ovalWidth, ovalHeight)
        if (dashWidth != -1f && dashGap != -1f) {
            gradientDrawable.setStroke(strokeWidth, strokeColor)
        } else {
            gradientDrawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap)
        }
        return gradientDrawable
    }

    /**
     * 获取矩形或圆角矩形drawable
     */
    fun getRectangleDrawable(
        cornerRadius: Float = 0f,
        solidColor: Int = Color.TRANSPARENT,
        strokeWidth: Int = 0,
        strokeColor: Int = Color.TRANSPARENT,
        dashWidth: Float = -1f,
        dashGap: Float = -1f
    ): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setColor(solidColor)
        if (dashWidth != -1f && dashGap != -1f) {
            gradientDrawable.setStroke(strokeWidth, strokeColor)
        } else {
            gradientDrawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap)
        }
        gradientDrawable.cornerRadius = cornerRadius
        return gradientDrawable
    }

    /**
     * 获取矩形或圆角矩形drawable
     */
    fun getRectangleDrawable(
        cornerRadii: FloatArray = floatArrayOf(0f, 0f, 0f, 0f),
        solidColor: Int = Color.TRANSPARENT,
        strokeWidth: Int = 0,
        strokeColor: Int = Color.TRANSPARENT,
        dashWidth: Float = -1f,
        dashGap: Float = -1f
    ): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setColor(solidColor)
        if (dashWidth != -1f && dashGap != -1f) {
            gradientDrawable.setStroke(strokeWidth, strokeColor)
        } else {
            gradientDrawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap)
        }
        gradientDrawable.cornerRadii = cornerRadii
        return gradientDrawable
    }

    /**
     * 设置线性渐变色
     */
    fun setLineGradientColor(
        drawable: GradientDrawable,
        colors: IntArray,
        orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT
    ): GradientDrawable {
        drawable.colors = colors
        drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        drawable.orientation = orientation
        return drawable
    }


}