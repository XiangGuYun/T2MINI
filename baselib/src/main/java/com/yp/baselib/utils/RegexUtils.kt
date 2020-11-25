package com.yp.baselib.utils

import java.util.regex.Pattern

/**
 * 格式校验工具类
 */
object RegexUtils {

    /***
     * 手机号码检测
     */
    fun checkPhoneNum(num: String): Boolean {
        val p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$")
        val m = p.matcher(num)
        return m.matches()
    }

    /**
     * 身份证号码检测
     */
    fun checkIdNum(num: String): Boolean {
        return IDCardCheck.check(num)
    }

    /**
     * 字符串是否符合正则表达式的规则
     *
     * @param text   匹配文本
     * @param format 匹配规则
     * @return true 匹配成功 flase 匹配失败
     */
    fun isMatches(text: String?, format: String?): Boolean {
        val pattern = Pattern.compile(format)
        val m = pattern.matcher(text)
        return m.matches()
    }

    /**
     * 验证输入的是否是正数小数或整数
     *
     * @return
     */
    fun isPositiveDemical(str: String?): Boolean {
        val format = "[0-9.]{0,3}"
        return isMatches(str, format)
    }

    /**
     * 匹配帐号类型是否正确（只能输入大小写字母和数字，最大不超过20个字符）
     *
     * @param str 帐号
     * @return true= 符合 false=不符合
     */
    fun isAccount(str: String?): Boolean {
        val format = "[a-zA-Z0-9]{0,20}"
        return isMatches(str, format)
    }

    /**
     * 匹配金额是否符合要求（99999999.99）
     *
     * @param money 金额字符串
     * @return true= 符合 false=不符合
     */
    fun isMoney(money: String?): Boolean {
        val regex = "(^[1-9][0-9]{0,7}(\\.[0-9]{0,2})?)|(^0(\\.[0-9]{0,2})?)"
        return isMatches(money, regex)
    }

}