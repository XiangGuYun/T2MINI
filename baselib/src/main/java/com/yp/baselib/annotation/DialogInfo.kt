package com.yp.baselib.annotation

@Target(AnnotationTarget.CLASS)
annotation class DialogInfo(val width: Int, val height: Int, val layoutId: Int)