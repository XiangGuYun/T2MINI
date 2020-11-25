package com.yp.baselib.listener

import android.animation.Animator

interface AnimatorListener : Animator.AnimatorListener {

    override fun onAnimationCancel(animation: Animator?) {
        val thread = Thread{

        }
    }

    override fun onAnimationEnd(animation: Animator?) {

    }

    override fun onAnimationRepeat(animation: Animator?) {

    }

    override fun onAnimationStart(animation: Animator?) {

    }

}