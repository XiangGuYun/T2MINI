package com.yp.baselib.ex

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.drawable.Drawable
import android.os.Environment
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.yp.baselib.base.BaseActivity
import java.io.Serializable
import java.util.*


/**
 * Context扩展接口
 */
interface ContextEx {

    /** 布局填充器 */
    val Context.inflater: LayoutInflater get() = LayoutInflater.from(this)

    /** SDCard路径 */
    val Context.SDCARD: String get() = Environment.getExternalStorageDirectory().toString()

    /** 内置缓存路径 */
    val Context.CACHE: String get() = "/data/data/${this.packageName}/cache"

    /**
     * 获取屏幕宽度
     */
    val Context.srnWidth get() = this.resources.displayMetrics.widthPixels

    /**
     * 获取屏幕高度
     */
    val Context.srnHeight get() = this.resources.displayMetrics.heightPixels


    /**
     * 获取一个Drawable
     */
    fun Context.drawable(id: Int): Drawable? {
        return resources.getDrawable(id)
    }

    /**
     * 延迟打开键盘，不依赖EditText
     * Q: 软键盘把某些布局挤上去的情况
     * A: Activity属性windowSoftInputMode
     * adjustPan:不会把底部的布局给挤上去
     * adjustResize:自适应的，会把底部的挤上去
     */
    fun BaseActivity.openKeyboardDelay(time: Long) {
        doDelayTask(time){
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
        }
    }

    /**
     * 延迟弹出键盘，依赖EditText
     */
    fun Context.showKeyboardDelay(et: EditText, delayedTime: Long = 1000) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                val inputManager = et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(et, 0)
            }
        }, delayedTime)
    }

    /**
     * 延迟打开键盘，依赖EditText
     */
    fun Activity.openKeyboardDelay(et: EditText, time: Long) {
        Thread {
            Thread.sleep(time)
            runOnUiThread {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
                et.requestFocus()
            }
        }.start()
    }


    /**
     * 自动弹出键盘
     */
    fun Context.showKeyboard(et: EditText) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.showSoftInput(et, InputMethodManager.SHOW_FORCED)//0
    }

    /**
     * 切换键盘开关状态
     */
    fun Context.toggleKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 关闭键盘1
     */
    fun Activity.closeKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && currentFocus != null) {
            if (currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    /**
     * 关闭键盘2
     */
    fun Activity.closeKeyboard1() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = window.peekDecorView()
        if (null != v) {
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    /**
     * 复制文字到剪贴板
     */
    fun Context.copyStringToChipboard(text: String) {
        val myClipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip: ClipData = ClipData.newPlainText("text", text)
        myClipboard.setPrimaryClip(myClip)
    }

    /**
     * 获取剪贴板的文字信息
     */
    fun Context.getClipBoardText(): String {
        val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val data = cm.primaryClip
        val item = data!!.getItemAt(0)
        return item.text.toString()
    }

    /**
     * 获取Bundle字符串，若为null则返回空字符串
     */
    fun Activity.extraStr(name: String): String {
        return intent.getStringExtra(name) ?: ""
    }

    /**
     * 获取Bundle可空字符串
     */
    fun Activity.extraStrNull(name: String): String? {
        return intent.getStringExtra(name)
    }

    /**
     * 获取Bundle整型
     */
    fun Activity.extraInt(tag:String, defValue:Int): Int {
        return intent.getIntExtra(tag, defValue)
    }

    /**
     * 获取Bundle布尔值
     */
    fun Activity.extraBool(tag:String, defValue:Boolean): Boolean {
        return intent.getBooleanExtra(tag, defValue)
    }

    /**
     * 获取Bundle序列化1
     * 注意内部类也必须实现序列化，否则无法收到
     */
    fun Activity.extraSerial(name: String): Serializable? {
        return intent.getSerializableExtra(name)
    }

    /**
     * 获取Bundle序列化2
     */
    fun Activity.extraParcel(name: String): Parcelable? {
        return intent.getParcelableExtra(name)
    }

}