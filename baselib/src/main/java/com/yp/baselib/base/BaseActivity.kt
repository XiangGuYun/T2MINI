package com.yp.baselib.base

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
import android.widget.Toast
import com.githang.statusbar.StatusBarCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yp.baselib.annotation.*
import com.yp.baselib.ex.BaseEx
import com.yp.baselib.utils.DensityUtils
import com.yp.baselib.utils.LogUtils
import com.yp.baselib.utils.TimerUtils
import com.yp.baselib.utils.*
import me.yokeyword.fragmentation.SupportActivity
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.ArrayList


/**
 * 最基类Activity
 */
abstract class BaseActivity : SupportActivity(), BaseEx {

    val ACTIVITY_NAME = "ac_name"
    var startEventBus = false//是否启用EventBus
    var viewInject: LayoutId? = null//布局ID注解
    var colorInject: StatusBarColor? = null//状态栏颜色注解
    var orientationInject: Orientation? = null//是否是竖直方向
    var notFitSystemWindowInject: NotFitSystemWindow? = null
    var noReqOrientation: NoReqOrientation? = null
    var permission:Permission?=null
    var notFitSystemWindow = false
    var dont_reqest_orientation = false
    var immersion = false
    var topBarId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        //初始化注解
        initAnnotation()

        //设置屏幕方向
        if (!dont_reqest_orientation) {
            try {
                requestedOrientation = if (orientationInject == null) {
                    //默认是竖直方向
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                } else {
                    if (orientationInject?.isVertical!!)
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    else
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
            } catch (e: IllegalStateException) {

            }
        }

        //加载布局
        if (viewInject != null) {
            setContentView(viewInject!!.id)
        }

        if (colorInject != null) {
            StatusBarCompat.setStatusBarColor(this, Color.parseColor(colorInject?.color))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarCompat.setStatusBarColor(this, Color.WHITE)
        }

        val winContent = findViewById<ViewGroup>(android.R.id.content)
        winContent.setBackgroundColor(APP_BG_COLOR)

        beforeInit()

        init(savedInstanceState)

        if (startEventBus) {
            EventBus.getDefault().register(this)
        }

        actList.add(this)
    }


    private fun initAnnotation() {


        val annotations = this::class.annotations
        annotations.forEachIndexed { _, it ->
            when (it.annotationClass) {
                LayoutId::class -> {
                    viewInject = it as LayoutId
                }
                StatusBarColor::class -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) colorInject =
                        it as StatusBarColor
                }
                Orientation::class -> {
                    orientationInject = it as Orientation
                }
                Permission::class -> {
                    permission = it as Permission
                    val permissions = permission?.permissions
                    permissions?.let {
                        PermissionUtils.req(this, {
                            granted()
                        },{
                            shouldShowRequestPermissionRationale()
                        },{
                            needGoToSettingsPage()
                        }, *it )
                    }
                }
                Bus::class -> {
                    startEventBus = true
                }
                StatusBarBlackText::class -> {
                    setStatusBarTextBlack(true)
                }
                FullScreen::class -> {
                    fullscreen(true)
                }
                NotFitSystemWindow::class -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) notFitSystemWindow = true
                }
                NoReqOrientation::class -> {
                    dont_reqest_orientation = true
                }
                Immersion::class -> {
                    immersion = true
                    topBarId = (it as Immersion).topBarId
                }
            }
        }
    }

    open fun shouldShowRequestPermissionRationale(){}

    open fun needGoToSettingsPage() {}

    open fun granted() {}

    protected open fun beforeInit() {}

    override fun onResume() {
        super.onResume()
        currAct = javaClass.simpleName
    }

    protected abstract fun init(bundle: Bundle?)

    companion object {
        /**
         * 全局背景色
         */
        const val APP_BG_COLOR = Color.WHITE

        var gson = Gson()//所有的Activity共享一个gson
        var actList = ArrayList<BaseActivity>()//用于储存所有的Activity实例
        var currAct: String = ""

        /**
         * 判断在Activity栈中是否包含某个Activity，传入简名即可
         */
        fun containActivity(simpleName: String): Boolean {
            return actList.find { it.javaClass.simpleName == simpleName } != null
        }

        /**
         * 根据Activity的SimpleName来关闭它
         * @param actName String
         */
        fun finishActivityByName(actName: String) {
            for (activity in actList) {
                if (activity.javaClass.simpleName == actName) {
                    activity.finish()
                    //return
                }
            }
        }

        /**
         *  根据1~N个Activity的SimpleName来关闭它（们）
         * @param actName Array<out String>
         */
        fun finishActivityByName(vararg actName: String) {
            for (activity in actList) {
                if (activity.javaClass.simpleName in actName) {
                    activity.finish()
                }
            }
        }

        /**
         * 关闭所有的Activity
         */
        fun finishAllActivities() {
            LogUtils.d("ActivityName", "size is " + actList.size)
            for (activity in actList) {
                LogUtils.d("ActivityName", activity.javaClass.simpleName)
                activity.finish()
            }
        }

        /**
         * 关闭所有的Activity除了指定的Activity
         */
        fun finishAllActivitiesExcept(vararg actName: String) {
            val list = actName.toMutableList()
            for (activity in actList) {
                if (!list.contains(activity.javaClass.simpleName)) {
                    activity.finish()
                }
            }
        }

        /**
         * 关闭栈中最上一层Activity
         */
        fun finishLast() {
            actList.last().finish()
        }

        /**
         * 根据类名来在栈中查找Activity，可能为空，可替代EventBus来直接调用要查找的Activity的方法
         */
        fun <T : BaseActivity> findActivity(simpleName: String): T? {
            return actList.find { it.javaClass.simpleName == simpleName } as T
        }

    }

    /**
     * 设置状态栏颜色为黑色，仅对6.0以上版本有效
     * @param isDark Boolean
     */
    fun setStatusBarTextBlack(isDark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            if (isDark) {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)//设置绘画模式
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//设置半透明模式
                }
                decor.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)//清除绘画模式
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//清除半透明模式
                }
                decor.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        }
    }

    /**
     * 隐藏或显示状态栏
     * @param enable Boolean
     */
    protected fun fullscreen(enable: Boolean) {
        if (enable) { //显示状态栏
            val lp = window.attributes
            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
            window.attributes = lp
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        } else { //隐藏状态栏
            val lp = window.attributes
            lp.flags = lp.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
            window.attributes = lp
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    /**
     * 方便在Activity中弹出Toast
     */
    fun Any.toast(isLong: Boolean = false) {
        Toast.makeText(
            this@BaseActivity, "$this",
            if (!isLong) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        ).apply {
            setGravity(Gravity.CENTER, 0, 0)
        }.show()
    }

    /**
     * 方便在Activity中直接使用相关单位的数字
     */
    val Number.dp get() = DensityUtils.dip2px(this@BaseActivity, this.toFloat())
    val Number.sp get() = DensityUtils.sp2px(this@BaseActivity, this.toFloat())
    fun Number.px2dp(): Int = DensityUtils.px2dip(this@BaseActivity, this.toFloat())
    fun Number.px2sp(): Int = DensityUtils.px2sp(this@BaseActivity, this.toFloat())

    /**
     * 启动Activity
     */
    inline fun <reified T : Activity> goTo(needFinish: Boolean = false) {
        startActivity(Intent(this, T::class.java))
        if (needFinish) finish()
    }

    /**
     * 启动Activity，可带参数和跳转动画
     */
    inline fun <reified T : Activity> goTo(
        vararg pairs: Pair<String, Any>,
        anims: Pair<Int, Int> = 0 to 0,
        needFinish: Boolean = false
    ) {
        val intent = Intent(this, T::class.java)
        pairs.forEach {
            when (it.second) {
                is String -> {
                    intent.putExtra(it.first, it.second.toString())
                }
                is Int -> {
                    intent.putExtra(it.first, it.second as Int)
                }
                is Boolean -> {
                    intent.putExtra(it.first, it.second as Boolean)
                }
                is Double -> {
                    intent.putExtra(it.first, it.second as Double)
                }
                is java.io.Serializable -> intent.putExtra(
                    it.first,
                    it.second as java.io.Serializable
                )
                is Parcelable -> intent.putExtra(it.first, it.second as Parcelable)
            }
            LogUtils.d("T_BUNDLE", ("${it.first} ${it.second}"))
        }
        startActivity(intent)
        if (anims != 0 to 0) overridePendingTransition(anims.first, anims.second)
        if (needFinish) finish()
    }

    /**
     * 全屏并且隐藏状态栏
     */
    fun hideStatusBar() {
        val attrs = window.attributes
        attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.attributes = attrs
    }

    /**
     * 全屏并且状态栏透明显示
     */
    fun showStatusBar() {
        val attrs = window.attributes
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        window.attributes = attrs
    }

    /**
     * 设置底部导航栏是是否显示
     */
    fun setNavigationBar(visible: Boolean) {
        if (!visible) {
            window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_HIDE_NAVIGATION
        } else {
            window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    /**
     * 设置背景变灰
     * @param alpha Float
     */
    fun setWindowAlpha(alpha: Float = 0.4f) {
        val attr = window.attributes
        attr.alpha = alpha
        window.attributes = attr
    }

    fun inflate(id: Int): View {
        return LayoutInflater.from(this).inflate(id, null)
    }

    private var handler: Handler? = null

    private var isRunTask = false

    /**
     * 执行延迟任务（使用Handler实现）
     * @param delay Long
     * @param callback Function0<Unit>
     */
    fun doDelayTask(delay: Long, callback: () -> Unit) {
        if (handler == null) {
            handler = Handler()
        }
        handler?.postDelayed({
            callback.invoke()
        }, delay)
    }

    /**
     * 执行延迟任务(使用Timer实现)
     * @param delay Long
     * @param callback Function0<Unit>
     */
    fun doDelayTask1(delay: Long, callback: () -> Unit) {
        var countDownTimer: Timer? = null
        countDownTimer = TimerUtils.schedule(delay) {
            runOnUiThread {
                callback.invoke()
                countDownTimer?.cancel()
            }
        }
    }

    override fun onDestroy() {
        LogUtils.d("ActivityName", "移除了 " + javaClass.simpleName)
        actList.remove(this)
        if (startEventBus) {
            EventBus.getDefault().unregister(this)
        }
        handler?.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    var exitTime = 0L

    /**
     * 退出应用验证
     * @param msg String
     * @param time Long
     */
    fun doExitVerify(msg: String = "再按一次退出应用", time:Long = 2000) {
        if ((System.currentTimeMillis() - exitTime) > time) {
            msg.toast()
            exitTime = System.currentTimeMillis()
        } else {
            finishAllActivities()
        }
    }

}