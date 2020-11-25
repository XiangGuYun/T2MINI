package com.yp.baselib.ex

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.LAYER_TYPE_SOFTWARE
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jakewharton.rxbinding2.view.RxView
import com.yp.baselib.listener.OnSeekBarChange
import com.yp.baselib.utils.DensityUtils.Companion.dip2px
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * View的工具类
 */
interface ViewEx {

    /**
     * 简化写法
     */
    val MP: Int get() = ViewGroup.LayoutParams.MATCH_PARENT

    /**
     * 简化写法
     */
    val WC: Int get() = ViewGroup.LayoutParams.WRAP_CONTENT

    /**
     * 禁止硬件加速
     */
    fun View.banGPU() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    /**
     * 获取子View，需要指定泛型
     */
    fun <T : View> View.view(id: Int): T {
        return findViewById(id)
    }

    /**
     * 获取子View，无需指定泛型
     */
    fun View.v(id: Int): View {
        return findViewById(id)
    }

    /**
     * 获取一个可为null的View
     */
    fun View.vNull(id: Int): View? {
        return findViewById(id)
    }

    /**
     * 返回一个有可能为空的泛型View
     */
    fun <T : View> View.viewNull(id: Int): T? {
        return findViewById(id)
    }

    /**
     * 设置View为不可见
     */
    fun <T : View> T.hide(): T {
        this.visibility = View.INVISIBLE
        return this
    }

    /**
     * 设置View为不可见
     */
    fun <T : View> T.gone(): T {
        this.visibility = View.GONE
        return this
    }

    /**
     * 如果View不可见，则设置View为可见
     */
    fun <T : View> T?.showIfNot() {
        this?.let {
            if (visibility != View.VISIBLE)
                visibility = View.VISIBLE
        }
    }

    /**
     * 设置View为可见
     */
    fun <T : View> T.show(): T {
        this.visibility = View.VISIBLE
        return this
    }

    /**
     * 判断View是否可见
     */
    fun <T : View> T.canSee(): Boolean {
        return visibility == View.VISIBLE
    }

    /**
     * 显示1~N个View
     * @param views Array<out T>
     */
    fun <T : View> show(vararg views: T) {
        views.forEach {
            it.show()
        }
    }

    /**
     * 隐藏1~N个View
     * @param views Array<out T>
     */
    fun <T : View> goneViews(vararg views: T) {
        views.forEach {
            it.gone()
        }
    }

    /**
     * 隐藏1~N个View
     * @param views Array<out T>
     */
    fun <T : View> hide(vararg views: T) {
        views.forEach {
            it.hide()
        }
    }

    /**
     * 隐藏1~N个View
     * @param views Array<out T>
     */
    fun <T : View> gone(vararg views: T) {
        views.forEach {
            it.gone()
        }
    }

    /**
     * 显示然后隐藏
     * @param act Activity
     * @param delay Long 显示时间
     * @return T
     */
    fun <T : View> T.showAndHide(act: Activity, delay: Long = 1200): T {
        show()
        val view = this
        Thread {
            Thread.sleep(delay)
            act.runOnUiThread { view.hide() }
        }.start()
        return this
    }

    /**
     * 设置点击事件
     * @param limitSecond 限制N秒内只允许触发一次点击时间，如果是0，则不限制
     */
    fun <T : View> T.click(limitSecond: Int = 0, func: (View) -> Unit): T {
        if (limitSecond == 0) {
            setOnClickListener(func)
        } else {
            preventRepeatedClick(View.OnClickListener{
                func.invoke(it)
            }, 1)
        }
        return this
    }

    /**
     * 设置点击事件
     */
//    infix fun <T : View> T.click(c: View.OnClickListener): T {
//        setOnClickListener(c)
//        return this
//    }

    /**
     * 设置点击事件
     */
//    fun <T : View> T.click1(func: () -> Unit): T {
//        setOnClickListener {
//            func.invoke()
//        }
//        return this
//    }

    /**
     * 取消点击事件
     */
    fun <T : View> T.clickNull(): T {
        setOnClickListener(null)
        return this
    }

    /**
     * 设置透明度
     */
    fun <T : View> T.alpha(a: Float): T {
        alpha = a
        return this
    }

    /**
     * 设置背景
     * @receiver T
     * @param drawableId Int drawable的Id
     * @return T
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    infix fun <T : View> T.bg(drawableId: Int): T {
        background = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getDrawable(drawableId)
        } else {
            context.resources.getDrawable(drawableId)
        }
        return this
    }

    /**
     * 设置背景色
     * @receiver T
     * @param color Int
     * @return T
     */
    infix fun <T : View> T.bgColor(color: Int): T {
        setBackgroundColor(color)
        return this
    }

    /**
     * 设置背景色
     * @receiver T
     * @param color String
     * @return T
     */
    infix fun <T : View> T.bgColor(color: String): T {
        setBackgroundColor(Color.parseColor(color))
        return this
    }


    /**
     * 寻找子ViewGroup
     */
    fun <T : View> T.vg(childId: Int): ViewGroup {
        return findViewById(childId)
    }

    /**
     * 获取子控件
     */
    fun <T : ViewGroup> T.child(index: Int): View? {
        return getChildAt(index)
    }

    /**
     * 反转可见性2(VISIBLE & GONE)，并设置对应事件
     * @receiver View
     * @param onVis Function0<Unit> 设置View为VISIBLE之后的回调
     * @param onGone Function0<Unit> 设置View为GONE之后的回调
     * @return View
     */
    fun View.revertVisible(onVis: (() -> Unit)? = null, onGone: (() -> Unit)? = null): View {
        if (visibility == View.GONE) {
            visibility = View.VISIBLE
            onVis?.invoke()
        } else {
            visibility = View.GONE
            onGone?.invoke()
        }
        return this
    }

    /**
     * 防止在2秒内重复点击
     * 扩展: https://blog.csdn.net/hust_twj/article/details/78742453
     */
    fun View.limitClick(click: (v: View) -> Unit) {
        preventRepeatedClick(View.OnClickListener{
            click.invoke(it)
        })
    }

    /**
     * 防止重复点击
     * @receiver View
     * @param click (v:View)->Unit 点击事件
     * @param time Long 间隔时间，默认是2秒
     */
    fun View.limitClickByTime(click: (v: View) -> Unit, time: Long = 2) {
        preventRepeatedClick(View.OnClickListener{
            click.invoke(it)
        }, time)
    }

    /**
     * 防止重复点击
     * @receiver View
     * @param listener OnClickListener 点击事件
     * @param time Long 间隔时间，默认是2秒
     */
    private fun View.preventRepeatedClick(listener: View.OnClickListener, time: Long = 2) {
        val target = this
        RxView.clicks(this).throttleFirst(time, TimeUnit.SECONDS).subscribe(object : Observer<Any> {
            override fun onError(e: Throwable) {}
            override fun onSubscribe(d: Disposable) {}
            override fun onComplete() {}
            override fun onNext(t: Any) {
                listener.onClick(target)
            }
        })
    }

    /**
     * 监听连续点击
     */
    @SuppressLint("CheckResult")
    fun View.multiClick(timeout: Long = 500, callback: (MutableList<Any>) -> Unit) {
        val observable = RxView.clicks(this).share()
        observable.buffer(observable.debounce(timeout, TimeUnit.MILLISECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback.invoke(it)
                }
    }

    /**
     * 获取子TextView
     */
    infix fun View.tv(id: Int): TextView {
        return findViewById(id)
    }

    /**
     * 获取子TextView，可能为null
     * @receiver View
     * @param id Int
     * @return TextView?
     */
    infix fun View.tvNull(id: Int): TextView? {
        return findViewById(id)
    }

    /**
     * 获取子EditText
     */
    fun View.et(id: Int): EditText {
        return findViewById(id)
    }

    /**
     * 获取子WebView
     */
    fun View.wv(id: Int): WebView {
        return findViewById(id)
    }

    /**
     * 获取子ImageView
     */
    fun View.iv(id: Int): ImageView {
        return findViewById(id)
    }

    /**
     * 获取子RecyclerView
     */
    fun View.rv(id: Int): RecyclerView {
        return findViewById(id)
    }

    /**
     * 若条件成立则显示View，反之则隐藏View
     */
    fun <T : View> T.showOrGone(result: Boolean): T {
        if (result) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
        return this
    }

    fun <T : View> T.showOrHide(result: Boolean): T {
        if (result) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.INVISIBLE
        }
        return this
    }

    /**
     * 设置View是否可点击
     * @receiver View
     * @param bool Boolean
     * @return View
     */
    infix fun View.clickable(bool: Boolean): View {
        isClickable = bool
        return this
    }

    /**
     * 修改布局
     */
    fun <T : ViewGroup.LayoutParams> View.doLP(callback: (t: T) -> Unit): View {
        layoutParams = (layoutParams as T).apply {
            callback.invoke(this)
        }
        return this
    }

    /**
     * 设置布局
     */
    fun <T : ViewGroup.LayoutParams> View.lp(lp: T): View {
        layoutParams = lp
        return this
    }

    /**
     * 添加子View
     * @receiver ViewGroup
     * @param resId Int 布局Id
     */
    infix fun ViewGroup.add(resId: Int): ViewGroup {
        addView(LayoutInflater.from(context).inflate(resId, null))
        return this
    }

    /**
     * 添加子View
     * @receiver ViewGroup
     * @param view View 子View
     */
    infix fun ViewGroup.add(view: View) {
        addView(view)
    }

    /**
     * 添加子View
     * @receiver ViewGroup
     * @param resId Int 资源ID
     * @param pos Int 位置
     * @return ViewGroup
     */
    fun ViewGroup.add(resId: Int, pos: Int): ViewGroup {
        addView(LayoutInflater.from(context).inflate(resId, null), pos)
        return this
    }

    /**
     * View转bitmap
     * 如果使用DrawingCache，则对要截图的View有一个要求：View本身已经显示在界面上。
     * 如果View没有添加到界面上或者没有显示（绘制）过，则buildDrawingCache会失败。
     * 这种方式比较适合对应用界面或者某一部分的截图
     */
    fun View.view2Bitmap(callback: (Bitmap) -> Unit) {
        post {
            setDrawingCacheEnabled(true)
            buildDrawingCache()  //启用DrawingCache并创建位图
            val bitmap = Bitmap.createBitmap(getDrawingCache()) //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
            setDrawingCacheEnabled(false)  //禁用DrawingCache否则会影响性能
            callback.invoke(bitmap)
        }
    }

    /**
     * 从View中加载Bitmap
     * @receiver View
     * @param color Int
     * @param callback Function1<Bitmap, Unit>
     */
    fun View.loadBitmapFromView(color: Int = Color.WHITE, callback: (Bitmap) -> Unit) {
        post {
            val w = this.width
            val h = this.height
            val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val c = Canvas(bmp)
            c.drawColor(color)
            /** 如果不设置canvas画布为白色，则生成透明  */
            this.layout(0, 0, w, h)
            this.draw(c)
            callback.invoke(bmp)
        }
    }

    //---------------------------------------------

    /**
     * 简化setImageResource
     */
    infix fun ImageView.sIR(id: Int): ImageView {
        setImageResource(id)
        return this
    }

    fun sIR(vararg pair: Pair<ImageView, Int>) {
        pair.forEach {
            it.first.sIR(it.second)
        }
    }

    /**
     * 简化setImageBitmap
     */
    infix fun ImageView.sIB(bmp: Bitmap): ImageView {
        setImageBitmap(bmp)
        return this
    }

    /**
     * 简化setImageURI
     */
    infix fun ImageView.sIU(uri: Uri): ImageView {
        setImageURI(uri)
        return this
    }

    /**
     * 获取ImageView中的Bitmap
     * @receiver ImageView
     * @return Bitmap?
     */
    fun ImageView.getSrc(): Bitmap? {
        return (this.drawable as BitmapDrawable).bitmap
    }

    /**
     * 禁用RadioGroup
     * @receiver RadioGroup
     */
    fun RadioGroup.disable() {
        for (i in 0 until this.childCount) {
            this.getChildAt(i).isEnabled = false
        }
    }

    /**
     * 启用RadioGroup
     * @receiver RadioGroup
     */
    fun RadioGroup.enable() {
        for (i in 0 until this.childCount) {
            this.getChildAt(i).isEnabled = true
        }
    }

    /**
     * 监听拖动条改变事件
     * @receiver SeekBar
     * @param callback Function3<[@kotlin.ParameterName] SeekBar?, [@kotlin.ParameterName] Int, [@kotlin.ParameterName] Boolean, Unit>
     */
    fun SeekBar.change(callback: (seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit) {
        setOnSeekBarChangeListener(object : OnSeekBarChange {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                callback.invoke(seekBar, progress, fromUser)
            }
        })
    }

    /**
     * 为Tab的ImageView设置资源图片
     * @receiver TabLayout.Tab
     * @param ivId Int ImageView的Id
     * @param resId Int 图片资源Id
     */
    fun TabLayout.Tab.sIR(ivId: Int, resId: Int) {
        customView?.iv(ivId)?.sIR(resId)
    }

    /**
     * 为Tab设置自定义布局
     * @receiver TabLayout.Tab
     * @param resId Int
     * @return View?
     */
    infix fun TabLayout.Tab.cv(resId: Int): View {
        customView = LayoutInflater.from(parent!!.context).inflate(resId, null)
        return customView!!
    }

    /**
     * 获取Tab的自定义View
     */
    val TabLayout.Tab.cv: View get() = this.customView!!

    /**
     * 获取给定索引位的Tab
     */
    infix fun TabLayout.tab(pos: Int): TabLayout.Tab {
        return getTabAt(pos)!!
    }

    /**
     * 设置TabLayout的Tab宽度为某个view的宽度
     */
    fun TabLayout.setTabWidthBy(fieldName: String) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        post {
            try {
                //拿到tabLayout的mTabStrip属性
                val mTabStripField = javaClass.getDeclaredField("mTabStrip")
                mTabStripField.setAccessible(true)

                val mTabStrip = mTabStripField.get(this) as LinearLayout

                val dp10 = dip2px(context, 0f)

                for (i in 0 until mTabStrip.childCount) {
                    val tabView = mTabStrip.getChildAt(i)

                    //拿到tabView的mView属性
                    val mViewField = tabView.javaClass.getDeclaredField(fieldName)
                    mViewField.isAccessible = true

                    val mView = mViewField.get(tabView) as View

                    tabView.setPadding(0, 0, 0, 0)

                    //因为我想要的效果是   字多宽线就多宽，所以测量mView的宽度
                    mView.measure(0, 0)
                    var width = mView.measuredWidth
                    //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                    val params = tabView.layoutParams as LinearLayout.LayoutParams
                    params.width = width
                    params.leftMargin = dp10
                    params.rightMargin = dp10
                    tabView.layoutParams = params

                    tabView.invalidate()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 设置内边距
     */
    fun <T : View> T.pad(left: Int, top: Int, right: Int, bottom: Int): T {
        setPadding(dip2px(context, left.toFloat()), dip2px(context, top.toFloat()), dip2px(context, right.toFloat()), dip2px(context, bottom.toFloat()))
        return this
    }

    /**
     * 勾选某个RadioButton
     * @receiver RadioGroup
     * @param getId Function1<[@kotlin.ParameterName] Int, Unit>
     */
    fun RadioGroup.check(getId: (id: Int) -> Unit) {
        setOnCheckedChangeListener { group, checkedId ->
            getId.invoke(checkedId)
        }
    }

    /**
     * 设置ViewPager适配器
     */
    fun ViewPager.setViewAdapter(count: Int, getPageView: (pos: Int) -> View) {
        val pagerAdapter: PagerAdapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return count
            }

            override fun isViewFromObject(p0: View, p1: Any): Boolean {
                return p0 == p1
            }

            override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
                container.removeView(any as View)
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view = getPageView.invoke(position)
                container.addView(view)
                return view
            }
        }
        this.adapter = pagerAdapter
    }

    /**
     * 为ViewPager绑定TabLayout
     */
    fun ViewPager.bindTabLayout(tabLayout: TabLayout,
                                tabCount: Int,
                                isScroll: Boolean,
                                listener: (TabLayout.Tab?, Int) -> Unit) {
        if (isScroll) {
            tabLayout.tabMode = TabLayout.MODE_SCROLLABLE //设置滑动Tab模式
        } else {
            tabLayout.tabMode = TabLayout.MODE_FIXED //设置固定Tab模式
        }
        for (i in 0 until tabCount) {
            val tab: TabLayout.Tab = tabLayout.newTab()
            tabLayout.addTab(tab)
        }
        //将TabLayout和ViewPager关联起来
        tabLayout.setupWithViewPager(this, true)
        //Tab属性必须在关联ViewPager之后设置
        for (i in 0 until tabCount) {
            listener.invoke(tabLayout.getTabAt(i), i)
        }
    }

}