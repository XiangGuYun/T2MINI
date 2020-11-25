package com.yp.baselib.ex

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.yp.baselib.utils.DensityUtils


/**
 * 文本工具类
 */
interface TextViewEx : StringEx {

    /**
     * 返回文字内容
     */
    val TextView.str: String get() = text.toString()

    /**
     * 返回文字内容长度
     */
    val TextView.len: Int get() = text.toString().length

    /**
     * 判断文本是否为空
     */
    val TextView.isEmpty: Boolean get() = text.toString().isEmpty()

    /**
     * 设置部分点击文本(仅限一部分)
     */
    fun TextView.setClickText(txt: String, start: Int, end: Int, callback: () -> Unit) {
        val ss = SpannableString(txt)
        ss.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                callback.invoke()
            }
        }, start, end, Spanned.SPAN_COMPOSING)
        text = ss
        movementMethod = LinkMovementMethod.getInstance()
    }

    /**
     * 设置文本
     */
    infix fun <T : TextView> T.txt(text: String?): T {
        this.text = text ?: ""
        return this
    }


    infix fun <T : EditText> T.txtPhone(phone: String?): T {
        val chars = (phone ?: "").toCharArray().toMutableList()
        chars.add(3, ' ')
        chars.add(8, ' ')
        val builder = StringBuilder()
        chars.forEach {
            builder.append(it.toString())
        }
        setText(builder.toString())
        return this
    }

    fun convertString2Phone(string: String?): String {
        if (string == null || string.length < 11) return string?:""
        val chars = string.toCharArray().toMutableList()
        chars.add(3, ' ')
        chars.add(8, ' ')
        val builder = StringBuilder()
        chars.forEach {
            builder.append(it.toString())
        }
        return builder.toString()
    }


    infix fun <T : EditText> T.select(pos: Int?): T {
        setSelection(pos?:0)
        return this
    }

    /**
     * 设置提示
     */
    infix fun <T : TextView> T.hint(text: String?): T {
        this.hint = text?:""
        return this
    }

    /**
     * 设置颜色
     */
    infix fun <T : TextView> T.color(color: Int?): T {
        setTextColor(color?:Color.WHITE)
        return this
    }

    /**
     * 设置颜色
     */
    infix fun <T : TextView> T.color(color: String): T {
        setTextColor(Color.parseColor(color))
        return this
    }

    /**
     * 设置字体大小
     */
    infix fun <T : TextView> T.size(size: Number): T {
        textSize = size.toFloat()
        return this
    }

    /**
     * 设置HTML格式文本
     */
    infix fun <T : TextView> T.html(text: String): T {
        setText(Html.fromHtml(text))
        return this
    }

    /**
     * 获取TextView的Drawable
     */
    fun Context.tvDrawable(id: Int): Drawable {
        val drawable = resources.getDrawable(id)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        return drawable
    }

    /**
     * 设置左边图片
     */
    fun TextView.setLeftTVDrawable(id: Int): TextView {
        this.setCompoundDrawables(context.tvDrawable(id), null, null, null)
        return this
    }

    /**
     * 设置右边图片
     */
    fun TextView.setRightTVDrawable(id: Int): TextView {
        this.setCompoundDrawables(null, null, context.tvDrawable(id), null)
        return this
    }

    /**
     * 设置上边图片
     */
    fun TextView.setTopTVDrawable(id: Int) {
        this.setCompoundDrawables(null, context.tvDrawable(id), null, null)
    }

    /**
     * 设置下边图片
     */
    fun TextView.setBtmTVDrawable(id: Int) {
        this.setCompoundDrawables(null, null, null, context.tvDrawable(id))
    }

    /**
     * 取消设置图片
     */
    fun TextView.setNullTVDrawable() {
        this.setCompoundDrawables(null, null, null, null)
    }

    /**
     * 获取TextView的文本宽度(带间隙)
     */
    fun TextView.getTextWidth(): Float {
        val paint = Paint()
        paint.textSize = this.textSize
        return paint.measureText(str, 0, len)
    }

    /**
     * 获取字符串的文本宽度(带间隙)
     */
    fun String.getTextWidth(textSize: Float): Float {
        val paint = Paint()
        paint.textSize = textSize
        return paint.measureText(this, 0, this.length)
    }

    /**
     * 获取文本宽度(不带间隙)
     */
    fun TextView.getPureTextWidth(): Int {
        val rect = Rect()
        val paint = Paint()
        paint.textSize = textSize
        paint.getTextBounds(str, 0, len, rect)
        return rect.width()
    }

    /**
     * 获取字符串的宽度(不带间隙)
     */
    fun String.getPureTextWidth(textSize: Float): Int {
        val rect = Rect()
        val paint = Paint()
        paint.textSize = textSize
        paint.getTextBounds(this, 0, this.length, rect)
        return rect.width()
    }

    /**
     * 设置显示的最大文本字数，不包含...，多出则用...代替
     * @receiver TextView
     * @param txt String
     * @param max Int
     */
    fun TextView.setLimitText(txt: String, max: Int = 6): TextView {
        if (txt.isNotEmpty()) {
            text = when {
                txt.length > max -> "${txt.substring(0, max)}..."
                else -> txt
            }
        } else {
            text = ""
        }
        return this
    }

    /**
     * 限制字符产的最大长度，包含...
     */
    fun String.limit(max: Int): String {
        return when {
            length > max -> "${substring(0, max - 1)}..."
            else -> this
        }
    }

    /**
     * 对一段文本设置不同大小的字体
     * @param brokenList 最后一个字符的索引
     * @param sizeList 字体尺寸大小
     */
    fun TextView.setDiffSizeText(text: String, brokenList: List<Int>, sizeList: List<Int>) {
        val textSpan = SpannableStringBuilder(text)
        sizeList.forEachIndexed { i, it ->
            when {
                i == 0 -> {
                    textSpan.setSpan(
                            AbsoluteSizeSpan(
                                    DensityUtils.sp2px(
                                            context,
                                            sizeList[i].toFloat()
                                    )
                            ), 0, brokenList[i] + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }
                i != sizeList.lastIndex -> textSpan.setSpan(
                        AbsoluteSizeSpan(
                                DensityUtils.sp2px(
                                        context,
                                        sizeList[i].toFloat()
                                )
                        ), brokenList[i - 1], brokenList[i] + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                else -> textSpan.setSpan(
                        AbsoluteSizeSpan(
                                DensityUtils.sp2px(
                                        context,
                                        sizeList.last().toFloat()
                                )
                        ), text.length - 1, text.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        }
        setText(textSpan)
    }

    /**
     * 设置自定义字体
     */
    fun TextView.setFont(assetsPath: String): TextView {
        val typeface = Typeface.createFromAsset(context.assets, assetsPath)
        this.typeface = typeface
        return this
    }

}