package com.yp.payment.order.layer.utils

import android.os.Environment
import java.io.File

object FileUtils {
    fun write(text:String){
        File(Environment.getExternalStorageDirectory().toString(), "bugs.txt").writeText(text)
    }
}