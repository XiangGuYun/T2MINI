package com.yp.baselib.utils

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * 对话框工具类
 */
object DialogUtils {

    /**
     * 获取底部弹窗BottomSheetDialog
     */
    fun createBottomSheetDialog(ctx: Context, viewId: Int): BottomSheetDialog {
        val dialog = BottomSheetDialog(ctx)
        val dialogView = LayoutInflater.from(ctx).inflate(viewId, null)
        dialog.setContentView(dialogView)
        dialog.delegate.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.setBackgroundColor(ctx.resources.getColor(android.R.color.transparent))
        return dialog
    }

    /**
     * 创建消息弹窗
     */
    @JvmStatic
    fun createMessageDialog(ctx: Context,
                            message: String,
                            title: String? = null,
                            yes: String? = null,
                            no: String? = null,
                            yesCallback: ((dialog: DialogInterface) -> Unit)? = null,
                            noCallback: ((dialog: DialogInterface) -> Unit)? = null
    ): AlertDialog {
        val builder = AlertDialog.Builder(ctx)
        builder.setMessage(message)
        if (title != null) {
            builder.setTitle(title)
        }
        if (yes != null) {
            builder.setPositiveButton(yes) { dialog, _ ->
                yesCallback?.invoke(dialog!!)
            }
        }
        if (no != null) {
            builder.setNegativeButton(no) { dialog, _ ->
                noCallback?.invoke(dialog!!)
            }
        }
        return builder.create()
    }

    fun createMessageBuilder(ctx: Context,
                            message: String,
                            title: String? = null,
                            yes: String? = null,
                            no: String? = null,
                            yesCallback: ((dialog: DialogInterface) -> Unit)? = null,
                            noCallback: ((dialog: DialogInterface) -> Unit)? = null
    ): AlertDialog.Builder {
        val builder = AlertDialog.Builder(ctx)
        builder.setMessage(message)
        if (title != null) {
            builder.setTitle(title)
        }
        if (yes != null) {
            builder.setPositiveButton(yes) { dialog, _ ->
                yesCallback?.invoke(dialog!!)
            }
        }
        if (no != null) {
            builder.setNegativeButton(no) { dialog, _ ->
                noCallback?.invoke(dialog!!)
            }
        }
        return builder
    }

    /**
     * 设置自定义视图弹窗
     */
    fun createCustomViewDialog(ctx: Context,
                               layoutId: Int,
                               title: String? = null,
                               yes: String? = null,
                               no: String? = null,
                               yesCallback: ((dialog: DialogInterface) -> Unit)? = null,
                               noCallback: ((dialog: DialogInterface) -> Unit)? = null): AlertDialog {
        val builder = AlertDialog.Builder(ctx)
        builder.setView(layoutId)
        if (title != null) {
            builder.setTitle(title)
        }
        if (yes != null) {
            builder.setPositiveButton(yes) { dialog, _ ->
                yesCallback?.invoke(dialog!!)
            }
        }
        if (no != null) {
            builder.setNegativeButton(no) { dialog, _ ->
                noCallback?.invoke(dialog!!)
            }
        }
        return builder.create()

    }

    fun createCustomViewDialog(ctx: Context,
                               viewId: View,
                               title: String? = null,
                               yes: String? = null,
                               no: String? = null,
                               yesCallback: ((dialog: DialogInterface) -> Unit)? = null,
                               noCallback: ((dialog: DialogInterface) -> Unit)? = null): AlertDialog {
        val builder = AlertDialog.Builder(ctx)
        builder.setView(viewId)
        if (title != null) {
            builder.setTitle(title)
        }
        if (yes != null) {
            builder.setPositiveButton(yes) { dialog, _ ->
                yesCallback?.invoke(dialog!!)
            }
        }
        if (no != null) {
            builder.setNegativeButton(no) { dialog, _ ->
                noCallback?.invoke(dialog!!)
            }
        }
        return builder.create()

    }


}