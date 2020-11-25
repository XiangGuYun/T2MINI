package com.yp.baselib.ex

import android.text.Html
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.yp.baselib.Holder
import com.yp.baselib.utils.RVUtils
import com.yp.baselib.view.rv.YxdRVAdapter
import com.yp.baselib.view.rv.YxdRVHolder

/**
 * RecyclerView扩展类
 * @property wrap RVUtils
 */
interface RvEx : StringEx {

    /**
     * 生成并设置适配器
     * @param data 数据集合
     * @param bindItemWithData 绑定数据
     * @param getLayoutIndex 决定列表项与列表项布局id的对应关系，为null是默认均采用第一个布局id
     * @param itemLayoutId 传入可变长度的布局id数组
     */
    fun <T, R : List<T>> RVUtils.generate(
        data: R,
        bindItemWithData: (h: YxdRVHolder, i: Int, it: T) -> Unit,
        getLayoutIndex: ((i: Int) -> Int)? = null,
        vararg itemLayoutId: Int
    ) {
        if (rv.layoutManager == null) {
            rv.layoutManager = LinearLayoutManager(context)
        }
        rv.adapter = object : YxdRVAdapter<T>(context, data, *itemLayoutId) {
            override fun onBindData(
                viewHolder: YxdRVHolder,
                position: Int,
                item: T
            ) {
                bindItemWithData.invoke(viewHolder, position, item)
            }

            override fun getLayoutIndex(layoutIndex: Int, item: T): Int {
                return getLayoutIndex?.invoke(layoutIndex) ?: 0
            }
        }
    }

    /**
     * 设置带有HeaderView的适配器
     * @receiver RVUtils
     * @param data List<T>
     * @param headerViewId Int
     * @param handleHeaderView (holder:Holder)->Unit
     * @param handleNormalView (holder:Holder,pos:Int)->Unit
     * @param handleNormalLayoutIndex (pos:Int)->Int
     * @param itemId IntArray
     */
    fun <T, R : List<T>> RVUtils.generateH(
        data: R,
        headerViewId: Int,
        handleHeaderView: (holder: Holder) -> Unit,
        handleNormalView: (holder: Holder, pos: Int, item: T) -> Unit,
        handleNormalLayoutIndex: (pos: Int) -> Int,
        vararg itemId: Int
    ) {
        needHeader = true
        generate(data, { holder, pos, item ->
            when (pos) {
                0 -> {
                    handleHeaderView.invoke(holder)
                }
                else -> {
                    handleNormalView.invoke(holder, pos, item)
                }
            }
        }, {
            when (it) {
                0 -> 0
                else -> handleNormalLayoutIndex.invoke(it) + 1
            }
        }, headerViewId, *itemId)
    }

    /**
     * 设置带有HeaderView和FooterView的适配器
     * @receiver RVUtils
     * @param data List<T>
     * @param headerViewId Int
     * @param handleHeaderView (holder:Holder)->Unit
     * @param handleNormalView (holder:Holder,pos:Int)->Unit
     * @param handleNormalLayoutIndex (pos:Int)->Int
     * @param itemId IntArray
     */
    fun <T, R : List<T>> RVUtils.generateHF(
        data: R,
        headerViewId: Int,
        handleHeaderView: (headerHolder: Holder) -> Unit,
        footerViewId: Int,
        handleFooterView: (footerHolder: Holder) -> Unit,
        handleNormalView: (normalHolder: Holder, pos: Int, item: T) -> Unit,
        handleNormalLayoutIndex: (pos: Int) -> Int,
        vararg itemId: Int
    ): RVUtils {
        needHeader = true
        needFooter = true
        generate(data, { holder, pos, item ->
            when (pos) {
                0 -> {
                    handleHeaderView.invoke(holder)
                }
                data.lastIndex -> {
                    handleFooterView.invoke(holder)
                }
                else -> {
                    handleNormalView.invoke(holder, pos, item)
                }
            }
        }, {
            when (it) {
                0 -> 0
                data.lastIndex -> 1
                else -> handleNormalLayoutIndex.invoke(it) + 2
            }
        }, headerViewId, footerViewId, *itemId)
        return this
    }

    /**
     * 遍历RecyclerView的子视图
     * @receiver RecyclerView
     * @param fun1 (i:Int,it:View)->Unit
     */
    fun RecyclerView.foreachIndexed(fun1: (i: Int, it: View) -> Unit) {
        for (i in 0 until childCount) {
            fun1.invoke(i, getChildAt(i))
        }
    }

    fun RecyclerView.foreach(fun1: (it: View) -> Unit) {
        for (i in 0 until childCount) {
            fun1.invoke(getChildAt(i))
        }
    }

    /**
     * 简化ViewHolder的view获取，无需指定泛型
     */
    fun Holder.v(id: Int): View {
        return getView(id)
    }

    fun Holder.vNull(id: Int): View? {
        return getView(id)
    }

    /**
     * 简化ViewHolder的view获取，需要指定泛型
     */
    fun <T : View> Holder.view(id: Int): T {
        return getView(id)
    }

    /**
     * 简化ViewHolder的ImageView获取
     */
    fun Holder.iv(id: Int): ImageView {
        return getView(id)
    }

    /**
     * 简化ViewHolder的TextView获取
     */
    fun Holder.tv(id: Int): TextView {
        return getView(id)
    }

    fun Holder.tvNull(id: Int): TextView? {
        return getView(id)
    }

    /**
     * 简化ViewHolder的RecyclerView获取
     */
    fun Holder.rv(id: Int): RecyclerView {
        return getView(id)
    }

    /**
     * 简化ViewHolder的EditText获取
     */
    fun Holder.et(id: Int): EditText {
        return getView(id)
    }

    /**
     *  简化setImageResource，且返回Holder
     */
    fun Holder.ir(ivId: Int, imgId: Int): Holder {
        setImageResource(ivId, imgId)
        return this
    }

    /**
     *  简化setText，且返回Holder
     */
    fun Holder.txt(id: Int, text: String?): Holder {
        if (text.isNullOrEmpty()) {
            setText(id, "")
        } else {
            setText(id, text)
        }

        return this
    }

    /**
     *  简化setTextColor，且返回Holder
     */
    fun Holder.color(id: Int, color: Int): Holder {
        setTextColor(id, color)
        return this
    }

    /**
     *  简化setOnClickListener，且返回Holder
     */
    fun Holder.click(id: Int, onClick: (View) -> Unit): Holder {
        v(id).setOnClickListener(onClick)
        return this
    }

    /**
     *  简化setOnItemViewClickListener，且返回Holder
     */
    fun Holder.itemClick(click: (view: View) -> Unit): Holder {
        setOnItemViewClickListener(click)
        return this
    }

    /**
     *  简化ItemView的setOnLongClickListener，且返回Holder
     */
    fun Holder.itemLongClick(click: (view: View) -> Unit): Holder {
        itemView.setOnLongClickListener {
            click.invoke(it)
            return@setOnLongClickListener true
        }
        return this
    }

    /**
     *  简化设置Html格式文本，且返回Holder
     */
    fun Holder.htmlText(id: Int, html: String): Holder {
        getView<TextView>(id).text = Html.fromHtml(html)
        return this
    }

    /**
     * 添加分割线
     */
    fun RVUtils.decorate(drawableId: Int, isVertical: Boolean = true): RVUtils {
        val divider = DividerItemDecoration(
            context,
            if (isVertical) DividerItemDecoration.VERTICAL else DividerItemDecoration.HORIZONTAL
        )
        divider.setDrawable(context.resources.getDrawable(drawableId))
        rv.addItemDecoration(divider)
        return this
    }

    fun RVUtils.decorate(isVertical: Boolean = true): RVUtils {
        rv.addItemDecoration(
            DividerItemDecoration(
                context,
                if (isVertical) DividerItemDecoration.VERTICAL else DividerItemDecoration.HORIZONTAL
            )
        )
        return this
    }

    fun RVUtils.decorate(decoration: RecyclerView.ItemDecoration): RVUtils {
        rv.addItemDecoration(decoration)
        return this
    }

    /**
     * 设置线性吸附
     */
    fun RVUtils.snapLinear(): RVUtils {
        val helper = LinearSnapHelper()
        helper.attachToRecyclerView(rv)
        return this
    }

    /**
     * 设置页面吸附
     */
    fun RVUtils.snapPager(): RVUtils {
        pagerHelper = PagerSnapHelper()
        pagerHelper.attachToRecyclerView(rv)
        return this
    }

    /**
     * 设置自定义吸附
     */
    fun RVUtils.customSnap(set: (rv: RecyclerView) -> Unit): RVUtils {
        set.invoke(rv)
        return this
    }

    fun RVUtils.customSnap(snapHelper: SnapHelper): RVUtils {
        snapHelper.attachToRecyclerView(rv)
        return this
    }

    /**
     * 设置Item增删动画
     *
    Cool
    LandingAnimator

    Scale
    ScaleInAnimator, ScaleInTopAnimator, ScaleInBottomAnimator
    ScaleInLeftAnimator, ScaleInRightAnimator

    Fade
    FadeInAnimator, FadeInDownAnimator, FadeInUpAnimator
    FadeInLeftAnimator, FadeInRightAnimator

    Flip
    FlipInTopXAnimator, FlipInBottomXAnimator
    FlipInLeftYAnimator, FlipInRightYAnimator

    Slide
    SlideInLeftAnimator, SlideInRightAnimator, OvershootInLeftAnimator, OvershootInRightAnimator
    SlideInUpAnimator, SlideInDownAnimator
     */
    fun <T : RecyclerView.ItemAnimator> RVUtils.anim(anim: T?): RVUtils {
        if (anim == null) {
            rv.itemAnimator = DefaultItemAnimator()
        } else {
            rv.itemAnimator = anim
        }
        return this
    }

    /**
     * 滚动到指定位置，指定位置会完整地出现在屏幕的最下方
     */
    fun <T> RecyclerView.scrollTo(position: Int, list: List<T>) {
        if (position >= 0 && position <= list.size - 1) {
            val firstItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val lastItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            when {
                position <= firstItem -> {
                    scrollToPosition(position)
                }
                position <= lastItem -> {
                    val top = getChildAt(position - firstItem).top
                    scrollBy(0, top)
                }
                else -> {
                    scrollToPosition(position)
                }
            }
        }
    }

    /**
     * 删除动画
     */
    fun <T> RecyclerView.deleteAnim(pos: Int, list: MutableList<T>) {
        list.removeAt(pos)
        adapter?.notifyItemRemoved(pos)
        adapter?.notifyItemRangeChanged(pos, list.size - pos)
    }

    /**
     * 获取RV的布局管理器
     * @receiver RecyclerView
     * @return T
     */
    fun <T : RecyclerView.LayoutManager> RecyclerView.lm(): T {
        return layoutManager as T
    }

    /**
     * 监听RV的滚动事件
     * @receiver RecyclerView
     * @param callback Function2<[@kotlin.ParameterName] Int, [@kotlin.ParameterName] Int, Unit>
     */
    fun RecyclerView.onScroll(callback: (dx: Int, dy: Int) -> Unit) {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                callback.invoke(dx, dy)
            }
        })
    }

    val RecyclerView?.wrap: RVUtils get() = RVUtils(this)

    /**
     * 刷新RecyclerView
     * @receiver RecyclerView
     */
    fun RecyclerView.update() {
        this.adapter?.notifyDataSetChanged()
    }

}