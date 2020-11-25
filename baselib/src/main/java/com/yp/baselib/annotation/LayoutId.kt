package com.yp.baselib.annotation

/**
 * 布局Id
 * @property id Int
 * @constructor
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class LayoutId(val id: Int)