package com.yp.baselib.view.scrollview

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

/**
 * 监听ScrollView的滑动距离
 * @author 86139
 */
class ListenScrollDistanceScrollView : ScrollView {

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    private var listener: OnScrollListener? = null

    fun setOnScrollListener(listener: OnScrollListener?) {
        this.listener = listener
    }

    /**
     * 设置接口
     */
    interface OnScrollListener {
        fun onScroll(scrollY: Int)
    }

    /**
     * 重写原生onScrollChanged方法，将参数传递给接口，由接口传递出去
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (listener != null) {
            //这里我只传了垂直滑动的距离
            listener!!.onScroll(t)
        }
    }
}