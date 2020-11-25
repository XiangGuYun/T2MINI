package com.yp.baselib.base

import android.os.Message
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.kotlinlib.common.ResUtils
import com.yp.baselib.utils.BusUtils
import org.greenrobot.eventbus.Subscribe

/**
 * 方便显示和隐藏进度条的Activity
 */
abstract class ProgressBarActivity : BaseActivity(){

    private lateinit var progressBar: ProgressBar

    companion object{
        private val START_PROGRESS get() = 0x1255//开启加载动画
        private val STOP_PROGRESS get() = 0x1256//停止加载动画

        /**
         * 显示进度条
         */
        fun startProgress(){
            BusUtils.post(START_PROGRESS)
        }

        /**
         * 隐藏进度条
         */
        fun stopProgress(){
            BusUtils.post(STOP_PROGRESS)
        }

    }

    @Subscribe
    fun handleProgressBar(msg: Message) {
        when (msg.what) {
            START_PROGRESS -> startProgressBar()
            STOP_PROGRESS -> stopProgressBar()
        }
    }

    override fun beforeInit() {
        addProgressBar()
        doBackArrow()
    }

    /**
     *  如果存在返回键，那就进行统一回退处理
     */
    private fun doBackArrow() {
        findViewById<View>(ResUtils.getId(this, "ivBack"))?.click {
            closeKeyboard()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopProgressBar()
    }

    /**
     *  添加Loading
     */
    private fun addProgressBar() {
        val winContent = findViewById<ViewGroup>(android.R.id.content)
        progressBar = ProgressBar(this)
        progressBar.layoutParams = FrameLayout.LayoutParams(50.dp, 50.dp).apply { gravity = Gravity.CENTER }
        winContent.addView(progressBar)
        progressBar.hide()
    }

    fun startProgressBar() {
        progressBar.show()
    }

    fun stopProgressBar() {
        progressBar.gone()
    }

}