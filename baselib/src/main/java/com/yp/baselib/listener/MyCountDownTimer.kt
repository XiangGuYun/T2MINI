package com.yp.baselib.listener

import android.os.CountDownTimer

abstract class MyCountDownTimer(millisInFuture:Long, countDownInterval:Long) :
        CountDownTimer(millisInFuture, countDownInterval ) {
    override fun onTick(millisUntilFinished: Long) {
    }

    override fun onFinish() {

    }
}