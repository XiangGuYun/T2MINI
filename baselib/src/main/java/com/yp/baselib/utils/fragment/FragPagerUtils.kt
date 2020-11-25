package com.yp.baselib.utils.fragment

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.*
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.yp.baselib.LLLP
import com.yp.baselib.base.BaseFragment
import com.yp.baselib.ex.ContextEx
import com.yp.baselib.ex.ViewEx
import com.yp.baselib.utils.DensityUtils
import com.yp.baselib.view.indicator.ViewPagerHelper
import com.yp.baselib.view.indicator.YxdIndicator
import com.yp.baselib.view.indicator.buildins.commonnavigator.CommonNavigator
import com.yp.baselib.view.indicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import com.yp.baselib.view.indicator.buildins.commonnavigator.abs.IPagerIndicator
import com.yp.baselib.view.indicator.buildins.commonnavigator.abs.IPagerTitleView
import com.yp.baselib.view.indicator.buildins.commonnavigator.indicators.LinePagerIndicator
import java.lang.reflect.Field

/**
 * Fragment搭配ViewPager以及TabLayout或YxdIndicator的工具类
 * 如果无需ViewPager的翻转效果，可用NoScrollViewPager
 */
class FragPagerUtils<T : Fragment?>(
    act: FragmentActivity,
    private val viewPager: ViewPager,
    fragments: List<T>,
    private val useStateAdapter: Boolean = false
) : ViewEx, ContextEx {

    var fragments: List<T>
    private lateinit var adapter: FragAdapter
    private lateinit var adapterState: FragStateAdapter

    init {
        viewPager.offscreenPageLimit = fragments.size
        this.fragments = fragments
        if (useStateAdapter) {
            adapterState = FragStateAdapter(act.supportFragmentManager, fragments)
            viewPager.adapter = adapterState
        } else {
            adapter = FragAdapter(act.supportFragmentManager, fragments)
            viewPager.adapter = adapter
        }
    }

    /**
     * 绑定TabLayout和ViewPager
     * @param tl TabLayout
     * @param isScroll Boolean
     * @param leftRightMargin Pair<Int, Int>
     * @param listener Function2<Tab, Int, Unit>
     */
    fun bindTL(
        tl: TabLayout,
        isScroll: Boolean = false,
        leftRightMargin: Pair<Int, Int> = 0 to 0,
        listener: (TabLayout.Tab, Int) -> Unit
    ) {
        if (isScroll) {
            tl.tabMode = TabLayout.MODE_SCROLLABLE //设置滑动Tab模式
        } else {
            tl.tabMode = TabLayout.MODE_FIXED //设置固定Tab模式
        }
        setIndicator(tl, leftRightMargin.first, leftRightMargin.second)
        for (i in fragments.indices) {
            val tab = tl.newTab()
            tl.addTab(tab)
        }
        //将TabLayout和ViewPager关联起来
        tl.setupWithViewPager(viewPager, true)
        //Tab属性必须在关联ViewPager之后设置
        for (i in fragments.indices) {
            listener.invoke(tl.getTabAt(i)!!, i)
        }
    }

    /**
     * 绑定YxdLayout和ViewPager
     * @param indicator
     * @param tabSize 标签数量
     * @param indicatorColor 指示器颜色
     * @param indicatorWidth 指示器宽度，如果是-1，则表示和标签的宽度一致
     * @param indicatorHeight 指示器高度
     * @param cornerRadius 指示器圆角半径
     * @param tabWidth 标签宽度，如果是-1，则表示根据标签数量来等分屏幕宽度
     * @param getTabView 返回标签的自定义View，该View必须实现IPagerTitleView接口
     */
    fun bindIndicator(
        indicator: YxdIndicator,
        tabSize: Int,
        indicatorColor: Int,
        indicatorWidth: Int = -1,
        indicatorHeight: Int = 2,
        cornerRadius: Int = 0,
        tabWidth: Int = -1,
        getTabView: (index: Int) -> View
    ) {
        indicator.navigator = CommonNavigator(indicator.context).apply {
            adapter = object : CommonNavigatorAdapter() {
                override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                    val tabView = getTabView.invoke(index)
                    if (tabView !is IPagerTitleView) {
                        throw ClassCastException("${tabView.javaClass.name}未实现IPagerTitleView接口")
                    }
                    tabView.setOnClickListener {
                        viewPager.currentItem = index
                    }
                    tabView.post {
                        tabView.doLP<LLLP> {
                            if (tabWidth == -1) {
                                it.width = context!!.srnWidth / tabSize
                            } else {
                                it.width = tabWidth
                            }
                        }
                    }
                    return tabView
                }

                override fun getCount(): Int {
                    return tabSize
                }

                override fun getIndicator(context: Context?): IPagerIndicator {
                    return LinePagerIndicator(context).apply {
                        mode = if (indicatorWidth == -1) {
                            LinePagerIndicator.MODE_MATCH_EDGE
                        } else {
                            LinePagerIndicator.MODE_EXACTLY
                        }
                        roundRadius =
                            DensityUtils.dip2px(context!!, cornerRadius.toFloat()).toFloat()
                        if (indicatorWidth != -1)
                            lineWidth =
                                DensityUtils.dip2px(context, indicatorWidth.toFloat()).toFloat()
                        lineHeight =
                            DensityUtils.dip2px(context, indicatorHeight.toFloat()).toFloat()
                        setColors(indicatorColor)
                    }
                }

            }
        }
        ViewPagerHelper.bind(indicator, viewPager)
    }

    /**
     * 设置TabLayout指示器的左右边距
     * @param tabs TabLayout
     * @param leftDip Int
     * @param rightDip Int
     */
    private fun setIndicator(tabs: TabLayout, leftDip: Int, rightDip: Int) {
        val tabLayout: Class<*> = tabs.javaClass
        var tabStrip: Field? = null
        try {
            tabStrip = tabLayout.getDeclaredField("slidingTabIndicator")
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        tabStrip!!.isAccessible = true
        var llTab: LinearLayout? = null
        try {
            llTab = tabStrip[tabs] as LinearLayout
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        val left = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            leftDip.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
        val right = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            rightDip.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
        for (i in 0 until llTab!!.childCount) {
            val child = llTab.getChildAt(i)
            child.setPadding(0, 0, 0, 0)
            val params =
                LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1f
                )
            params.leftMargin = left
            params.rightMargin = right
            child.layoutParams = params
            child.invalidate()
        }
    }

    fun getAdapter(): PagerAdapter {
        return if (useStateAdapter) adapterState else adapter
    }

    /**
     * 添加新的Fragment并刷新ViewPager
     * @param fragment BaseFragment
     */
    fun addAndUpdate(fragment: BaseFragment) {
        fragments.plus(fragment)
        getAdapter().notifyDataSetChanged()
//        for (i in fragments.indices) {
//            updateTab.invoke(tabLayout.getTabAt(i)!!)
//        }
//        tabLayout.getTabAt(fragments.size - 1)?.select()
    }

    inner class FragStateAdapter(
        fm: FragmentManager,
        private val fragments: List<T>
    ) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return fragments[position] as Fragment
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

    }

    inner class FragAdapter internal constructor(
        fm: FragmentManager,
        private val fragments: List<T>
    ) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return fragments[position] as Fragment
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

    }


}