package com.yp.baselib.listener

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.yp.baselib.utils.LogUtils

interface OnTextWatcher : TextWatcher {
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        LogUtils.d("OnTextWatcher", "s is $s, slength is ${s?.length}, start is $start, before is $before, count is $count")
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        LogUtils.d("beforeTextChanged", "s is $s, slength is ${s?.length}, start is $start, after is $after, count is $count")
    }
}