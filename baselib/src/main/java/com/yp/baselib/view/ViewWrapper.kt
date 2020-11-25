package com.yp.baselib.view

import android.view.View

/**
 * 用于属性动画改变View的宽高
 */
class ViewWrapper(private val mTarget: View) {

    var width: Int
        get() = mTarget.layoutParams.width
        set(width) {
            mTarget.layoutParams.width = width
            mTarget.requestLayout()
        }

}