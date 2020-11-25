package com.yp.baselib.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.yp.baselib.annotation.Bus
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.ex.BaseEx
import com.yp.baselib.utils.DensityUtils
import org.greenrobot.eventbus.EventBus

/**
 * DialogFragment的基类
 */
abstract class BaseDialogFragment : DialogFragment(), BaseEx {

    companion object {
        lateinit var gson: Gson
    }

    private var startEventBus = false
    private var viewInject: LayoutId? = null
    lateinit var fragView: View

    fun show(activity: FragmentActivity) {
        if (!isAdded) {
            show(activity.supportFragmentManager, javaClass.simpleName)
        } else {
            activity.supportFragmentManager.beginTransaction().remove(this).commitNow()
            show(activity.supportFragmentManager, javaClass.simpleName)
        }
    }

    /**
     * 获取托管Activity并指定具体类型
     */
    fun <T : BaseActivity> act(): T {
        return activity!! as T
    }

    /**
     * 获取托管Activity但不指定具体类型
     */
    fun getAct(): BaseActivity {
        return activity as BaseActivity
    }

    override fun onResume() {
        super.onResume()
        dialog?.let {
            it.window?.apply {
                setLayout(setDialogSize().first, setDialogSize().second)
                if (isTranslate()) setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            it.setCanceledOnTouchOutside(isCanceledOnTouchOutside())
            it.setOnShowListener { onShowListener()?.invoke() }
            it.setOnDismissListener { onDismissListener()?.invoke() }
        }
    }

    /**
     * 设置对话框的宽高
     */
    abstract fun setDialogSize(): Pair<Int, Int>

    protected fun isTranslate() = false

    fun isCanceledOnTouchOutside() = true

    protected fun onShowListener(): (() -> Unit)? = null

    protected fun onDismissListener(): (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val annotations = this::class.annotations
        annotations.forEachIndexed { _, it ->
            when (it.annotationClass) {
                LayoutId::class -> {
                    viewInject = it as LayoutId
                }
                Bus::class -> {
                    startEventBus = true
                    EventBus.getDefault().register(this)
                }
            }
        }
        fragView = if (viewInject != null) {
            inflater.inflate(viewInject!!.id, container, false)
        } else {
            inflater.inflate(getFragLayoutId(), container, false)
        }
        gson = BaseActivity.gson
        return fragView
    }

    /**
     * 如果不使用注解，可以重写此方法来返回布局
     */
    protected open fun getFragLayoutId(): Int {
        return 0
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //初始化代码需要放在onActivityCreated中，否则容易发生空指针异常
        init()
    }

    abstract fun init()

    /**
     * 获取对象JSON字符串
     * @param any Any
     * @return String
     */
    fun jsonStr(any: Any): String {
        return gson.toJson(any)
    }

    /**
     * 土司提示
     * @param isLong 是否显示更长时间
     */
    fun Any.toast(isLong: Boolean = false) {
        if (isLong)
            Toast.makeText(activity, this.toString(),
                    Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, 0, 0)
            }.show()
        else
            Toast.makeText(activity, this.toString(),
                    Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, 0, 0)
            }.show()
    }

    /**
     * 尺寸单位转换
     */
    fun Number.px2dp(): Int {
        return DensityUtils.px2dip(this@BaseDialogFragment.activity as Context, this.toFloat())
    }

    fun Number.dp2px(): Int {
        return DensityUtils.dip2px(this@BaseDialogFragment.activity as Context, this.toFloat())
    }

    val Number.dp get() = DensityUtils.dip2px(this@BaseDialogFragment.activity as Context, this.toFloat())

    val Number.sp get() = DensityUtils.sp2px(this@BaseDialogFragment.activity as Context, this.toFloat())

    override fun onDestroy() {
        super.onDestroy()
        if (startEventBus) {
            EventBus.getDefault().unregister(this)
        }
    }

}

/*
补充

设置DialogFragment从底部弹出，并且弹出动画为向上滑出，消失动画为向下滑出

WindowManager.LayoutParams params = getDialog().getWindow()
        .getAttributes();
params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
params.windowAnimations = R.style.bottomSheet_animation;
getDialog().getWindow().setAttributes(params);

 <style name="bottomSheet_animation" parent="@android:style/Animation">
        <item name="android:windowEnterAnimation">@anim/slide_in_bottom</item>
        <item name="android:windowExitAnimation">@anim/slide_out_bottom</item>
 </style>

 <?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:shareInterpolator="false">
    <translate
        android:duration="300"
        android:fromYDelta="100.0%p"
        android:toYDelta="0.0" />
</set>
 */