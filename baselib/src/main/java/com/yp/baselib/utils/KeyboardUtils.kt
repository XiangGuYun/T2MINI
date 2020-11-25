package com.yp.baselib.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.yp.baselib.base.BaseActivity

/**
 * 键盘工具类
 */
object KeyboardUtils {

    /**
     * 延迟打开键盘，不依赖EditText
     * Q: 软键盘把某些布局挤上去的情况
     * A: Activity属性windowSoftInputMode
     * adjustPan:不会把底部的布局给挤上去
     * adjustResize:自适应的，会把底部的挤上去
     */
    fun openKeyboardDelay(activity: BaseActivity, time: Long) {
        activity.doDelayTask(time) {
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
        }
    }

    /**
     * 延迟弹出键盘，依赖EditText
     */
    fun showKeyboardDelay(activity: BaseActivity, et: EditText, delayedTime: Long = 1000) {
        activity.doDelayTask(delayedTime) {
            val inputManager = et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(et, 0)
        }
    }

    /**
     * 延迟打开键盘，依赖EditText
     */
    fun openKeyboardDelay(activity: BaseActivity, et: EditText, time: Long) {
        activity.doDelayTask(time) {
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
            et.requestFocus()
        }
    }

    /**
     * 自动弹出键盘
     */
    fun showKeyboard(activity: BaseActivity, et: EditText) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.showSoftInput(et, InputMethodManager.SHOW_FORCED)
    }

    /**
     * 切换键盘开关状态
     */
    fun toggleKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 关闭键盘1
     */
    fun closeKeyboard(activity: BaseActivity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && activity.currentFocus != null) {
            if (activity.currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    /**
     * 关闭键盘2
     */
    fun closeKeyboard1(activity: BaseActivity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = activity.window.peekDecorView()
        if (null != v) {
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }


}