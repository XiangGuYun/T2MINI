package com.yp.baselib.utils

import android.util.Log
import com.yp.baselib.utils.LogUtils

/**
 * 日志工具类
 * @author 86139
 */
object LogUtils {

    /**
     * 不输出任何日志
     */
    const val LOG_LEVEL_NONE = 0

    /**
     * 仅输出DEBUG日志
     */
    const val LOG_LEVEL_DEBUG = 1

    /**
     * 仅输出INFO日志
     */
    const val LOG_LEVEL_INFO = 2

    /**
     * 仅输出WARN日志
     */
    const val LOG_LEVEL_WARN = 3

    /**
     * 仅输出ERROR日志
     */
    const val LOG_LEVEL_ERROR = 4

    /**
     * 输出所有日志
     */
    const val LOG_LEVEL_ALL = 5

    var logLevel = LOG_LEVEL_NONE

    /**
     * 以级别为 d 的形式输出LOG,输出debug调试信息
     */
    @JvmStatic
    fun d(tag: String?, msg: Any?) {
        if (logLevel >= LOG_LEVEL_DEBUG) {
            Log.d(tag, msg.toString())
        }
    }

    /**
     * 以级别为 i 的形式输出LOG,一般提示性的消息information
     */
    @JvmStatic
    fun i(tag: String?, msg: String?) {
        if (logLevel >= LOG_LEVEL_INFO) {
            Log.i(tag, msg)
        }
    }

    /**
     * 以级别为 w 的形式输出LOG,显示warning警告，一般是需要我们注意优化Android代码
     */
    @JvmStatic
    fun w(tag: String?, msg: String?) {
        if (logLevel >= LOG_LEVEL_WARN) {
            Log.w(tag, msg)
        }
    }

    /**
     * 以级别为 e 的形式输出LOG ，红色的错误信息，查看错误源的关键
     */
    @JvmStatic
    fun e(tag: String?, msg: String?) {
        if (logLevel >= LOG_LEVEL_ERROR) {
            Log.e(tag, msg)
        }
    }

    /**
     * 以级别为 v 的形式输出LOG ，verbose啰嗦的意思
     *
     */
    @JvmStatic
    fun v(tag: String?, msg: String?) {
        if (logLevel >= LOG_LEVEL_ALL) {
            Log.v(tag, msg)
        }
    }

    /**
     * 截断输出日志
     */
    @JvmStatic
    fun printLongLog(tag: String?, msg: String?) {
        var msg = msg
        if (tag == null || tag.isEmpty() || msg == null || msg.isEmpty()) {
            return
        }
        val segmentSize = 3 * 1024
        val length = msg.length.toLong()
        // 长度小于等于限制直接打印
        if (length <= segmentSize) {
            e(tag, msg)
        } else {
            // 循环分段打印日志
            while (msg!!.length > segmentSize) {
                val logContent = msg.substring(0, segmentSize)
                msg = msg.replace(logContent, "")
                e(tag, "-------------------$logContent")
            }
            // 打印剩余日志
            e(tag, "-------------------$msg")
        }
    }
}