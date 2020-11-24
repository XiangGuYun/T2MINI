package com.yp.payment.order.layer.utils

import android.app.Activity
import android.view.View

object ContextUtils {

    /**
     * 隐藏navigation
     */
    fun hideNavigation(act:Activity) {
        val decorView = act.window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = uiOptions
    }

}