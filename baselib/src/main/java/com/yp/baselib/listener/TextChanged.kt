package com.yp.baselib.listener

import android.text.TextWatcher
import com.yp.baselib.listener.TextWatcherImpl

/**
 * 简化监听输入文本改变事件
 * @property func Function1<CharSequence?, Unit>
 * @constructor
 */
class TextChanged(val func: (CharSequence?) -> Unit) : TextWatcher by TextWatcherImpl() {

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        func.invoke(s)
    }

}