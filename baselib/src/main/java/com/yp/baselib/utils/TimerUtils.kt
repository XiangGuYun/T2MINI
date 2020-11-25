package com.yp.baselib.utils

import android.os.CountDownTimer
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * 定时任务工具类，注意需要在界面退出时关闭任务执行
 * @author YeXuDong
 */
object TimerUtils {

    /**
     * 周期性执行非耗时任务，可延迟执行
     * @param delay Long
     * @param period Long
     * @param callback Function0<Unit>
     * @return Timer
     */
    fun schedule(delay: Long, period: Long, callback: (Timer) -> Unit): Timer {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                callback.invoke(timer)
            }
        }, delay, period)
        return timer
    }

    /**
     * 延迟执行某项任务
     * @param delay Long
     * @param callback Function0<Unit>
     * @return Timer
     */
    fun schedule(delay: Long, callback: () -> Unit): Timer {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                callback.invoke()
            }
        }, delay)
        return timer
    }

    /**
     * 在规定日期执行某项任务
     * @param date Date
     * @param callback Function0<Unit>
     * @return Timer
     */
    fun schedule(date: Date, callback: () -> Unit): Timer {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                callback.invoke()
            }
        }, date)
        return timer
    }

    /**
     * 在规定日期周期性执行某项费耗时任务
     * @param date Date
     * @param period Long
     * @param callback Function0<Unit>
     * @return Timer
     */
    fun schedule(date: Date, period: Long, callback: () -> Unit): Timer {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                callback.invoke()
            }
        }, date, period)
        return timer
    }

    /**
     * 执行倒计时任务
     * @param millisInFuture Long
     * @param countDownInterval Long
     * @param onTickCallback Function1<[@kotlin.ParameterName] Long, Unit>?
     * @param onFinishCallback Function0<Unit>?
     * @return CountDownTimer
     */
    fun countdown(millisInFuture: Long,
                  countDownInterval: Long,
                  onTickCallback: ((millisUntilFinished: Long) -> Unit)? = null,
                  onFinishCallback: (() -> Unit)? = null): CountDownTimer {
        return object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onFinish() {
                onFinishCallback?.invoke()
            }

            override fun onTick(millisUntilFinished: Long) {
                onTickCallback?.invoke(millisUntilFinished)
            }
        }
    }

    /**
     * 周期性执行耗时任务，每个任务在单独的子线程中执行
     * @param corePoolSize Int
     * @param runnable Runnable
     * @param initialDelay Long
     * @param period Long
     * @param unit TimeUnit
     * @return ScheduledExecutorService
     */
    fun scheduleTimeConsumingTask(corePoolSize: Int,
                                  runnable: Runnable,
                                  initialDelay: Long,
                                  period: Long,
                                  unit: TimeUnit): ScheduledExecutorService {
        val service = Executors.newScheduledThreadPool(corePoolSize)
        service.scheduleAtFixedRate(runnable, initialDelay, period, unit)
        return service
    }

}