package com.yp.baselib.annotation

/**
 * 设置屏幕方向，默认是竖向
 * @property isVertical Boolean
 * @constructor
 */
@Target(AnnotationTarget.CLASS)
annotation class Orientation(val isVertical: Boolean)