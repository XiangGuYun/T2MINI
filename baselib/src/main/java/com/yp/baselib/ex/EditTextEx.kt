package com.yp.baselib.ex

import android.annotation.SuppressLint
import android.text.*
import android.text.style.AbsoluteSizeSpan
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.yp.baselib.listener.TextChanged
import com.yp.baselib.utils.RegexUtils
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException
import kotlin.jvm.Throws

/**
 * 输入框扩展类
 */
interface EditTextEx : TextViewEx {

    /**
     * 设置文本
     */
    fun <T : EditText> T.txt(text: String?): T {
        setText(text ?: "")
        return this
    }

    /**
     * 设置EditText输入的最大长度
     * @param max Int
     */
    fun EditText.setMaxLength(max: Int) {
        filters = filters.plus(InputFilter.LengthFilter(max))
    }

    /**
     * 设置正则表达式来限制输入的字符类型
     */
    fun EditText.setInputType(regex: String) {
        val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            if (RegexUtils.isMatches(source.toString(), regex)) {
                null//如果符合要求则不过滤
            } else {
                ""//如果不符合要求则进行过滤
            }
        }
        filters = filters.plus(inputFilter)
    }

    /**
     * 监听EdiText文本输入事件
     * @receiver EditText
     * @param func Function1<CharSequence?, Unit>
     */
    fun EditText.onChanged(func: (CharSequence?) -> Unit) {
        addTextChangedListener(TextChanged {
            func.invoke(it)
        })
    }

    /**
     * 监听搜索键
     * 注意EditText一定要有如下属性
     * android:inputType="text"
     * android:imeOptions="actionSearch"
     */
    fun EditText.onPressSearch(callback: () -> Unit) {
        setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_SEARCH) {
                callback.invoke()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    /**
     * 监听打字数量
     * @param
     * et
     * @param tvWords
     * @param maxWords
     */
    fun EditText.listenWordInput(tvWords: TextView, maxWords: Int) {
        var wordNum: CharSequence = ""
        var selectionStart = 0
        var selectionEnd = 0
        addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                wordNum = s//实时记录输入的字数
            }

            override fun afterTextChanged(s: Editable) {
                val editable = text.toString()
                //String str = editable;
                val str = editable//stringFilter(editable)
                if (editable != str) {
                    //setText(str)
                    //设置新的光标所在位置
                    setSelection(str.length)
                } else {
                    //TextView显示剩余字数
                    tvWords.text = "${s.length}/${maxWords}个字"
                    selectionStart = selectionStart
                    selectionEnd = selectionEnd
                    if (wordNum.length > maxWords) {
                        //删除多余输入的字（不会显示出来）
                        s.delete(selectionStart - 1, selectionEnd)
                        val tempSelection = selectionEnd
                        text = s
                        setSelection(tempSelection)//设置光标在最后
                    }
                }
            }
        })
    }

    /**
     * 匹配正则表达式
     */
    @Throws(PatternSyntaxException::class)
    private fun stringFilter(str: String): String {
        // 只允许字母、数字和汉字
        val regEx = "[^a-zA-Z0-9\u4E00-\u9FA5.,。，]"
        //String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        return m.replaceAll("").trim { it <= ' ' }
    }

    /**
     * 限制输入的数字大小
     * @param edit
     * @param MIN_MARK
     * @param MAX_MARK
     */
    fun EditText.setNumberRegion(MIN_MARK: Double, MAX_MARK: Double) {
        addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var s = s
                if (start > 1) {
                    if (MIN_MARK != -1.0 && MAX_MARK != -1.0) {
                        val num = java.lang.Double.parseDouble(s.toString())
                        if (num > MAX_MARK) {
                            s = MAX_MARK.toString()
                            setText(s)
                        } else if (num < MIN_MARK) {
                            s = MIN_MARK.toString()
                            setText(s)
                        }
                        setSelection(s.length)
                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    if (MIN_MARK != -1.0 && MAX_MARK != -1.0) {
                        var markVal = 0.0
                        try {
                            markVal = java.lang.Double.parseDouble(s.toString())
                        } catch (e: NumberFormatException) {
                            markVal = 0.0
                        }

                        if (markVal > MAX_MARK) {
                            setText(MAX_MARK.toString())
                            setSelection(MAX_MARK.toString().length)
                        }
                        return
                    }
                }
            }
        })
    }

    /**
     * 设置提示文字大小
     */
    fun EditText.setHintSize(hintSize: Int): EditText {
        val ss = SpannableString(getHint())
        val ass = AbsoluteSizeSpan(hintSize, true)
        ss.setSpan(ass, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        hint = SpannedString(ss)
        return this
    }

    /**
     * 搜索联想
     * @param debounceTime 设置触发搜索联想的时间间隔
     * @param getResult 对搜索结果进行处理
     */
    @SuppressLint("CheckResult")
    fun EditText.rxSearch(debounceTime: Long = 1000, getResult: (keyWord: String) -> Unit) {
        RxTextView.textChanges(this)//设置作用的EditText
                .debounce(debounceTime, TimeUnit.MILLISECONDS)//只要debounceTime内没有输入，就发送事件
                .map {
                    //获取用户当前输入的关键词
                    it.toString()
                }
                .subscribe { keyWord ->
                    //对关键词进行处理
//                    if(keyWord.isNotEmpty())
                    getResult.invoke(keyWord)
                }
    }


}