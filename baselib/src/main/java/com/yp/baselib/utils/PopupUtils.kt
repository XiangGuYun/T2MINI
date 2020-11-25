package com.yp.baselib.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow

/**
 * 弹出窗口工具类
 */
class PopupUtils(
        ctx: Context,
        layoutId: Int,
        metric: Pair<Int, Int>,
        bgColor: String = "#00ffffff",
        getFocus: Boolean = true,
        isOutsideTouchable: Boolean = false,
        onDismiss: (() -> Unit)? = null
) {

    var window: PopupWindow

    var windowView: View = LayoutInflater.from(ctx).inflate(layoutId, null)

    init {
        window = PopupWindow(windowView, metric.first, metric.second)
        window.isFocusable = getFocus
        window.setBackgroundDrawable(ColorDrawable(Color.parseColor(bgColor)))
        window.isOutsideTouchable = isOutsideTouchable
        window.update()
        window.setOnDismissListener(onDismiss)
    }

    fun dismiss() {
        window.dismiss()
    }


}
