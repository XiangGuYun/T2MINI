package com.yp.baselib.utils;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

import android.content.Context;
import android.graphics.Canvas;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.yp.baselib.view.rv.YxdRVAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * RecyclerView工具类
 */
public class RVUtils {

    public RecyclerView rv;
    public Context context;
    public List<?> dataList;
    public int gridSpanCount = 1;
    public boolean needHeader = false;
    public boolean needFooter = false;
    @NotNull
    public PagerSnapHelper pagerHelper;

    public <T extends RecyclerView> RVUtils(T recyclerView) {
        rv = recyclerView;
        context = recyclerView.getContext();
    }

    /**
     * 是否禁止嵌套滑动
     * @param bool
     * @return
     */
    public RVUtils banNestedScroll(Boolean bool){
        rv.setNestedScrollingEnabled(!bool);
        return this;
    }

    /**
     * 设置网格列表
     * @param spanCount  列数
     * @param isVertical 是否垂直
     * @return
     */
    public RVUtils gridManager(int spanCount, boolean isVertical) {
        gridSpanCount = spanCount;
        if(isVertical){
            rv.setLayoutManager(new GridLayoutManager(context, spanCount));
        }else {
            rv.setLayoutManager(new GridLayoutManager(context, spanCount, LinearLayoutManager.HORIZONTAL, false));
        }
        return this;
    }

    public RVUtils gridManager(int spanCount) {
        gridSpanCount = spanCount;
        rv.setLayoutManager(new GridLayoutManager(context, spanCount));
        return this;
    }

    /**
     * 设置瀑布流列表
     */
    public RVUtils staggerManager(int spanCount, boolean isVertical){
        rv.setLayoutManager(new StaggeredGridLayoutManager(spanCount,
                isVertical?StaggeredGridLayoutManager.VERTICAL:StaggeredGridLayoutManager.HORIZONTAL));
        return this;
    }

    /**
     * 设置流式列表
     * @return
     */
    public RVUtils flexBoxManager(){
        //设置布局管理器
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
        //flexDirection 属性决定主轴的方向（即项目的排列方向）。类似 LinearLayout 的 vertical 和 horizontal。
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);//主轴为水平方向，起点在左端。
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列。
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);//按正常方向换行
        //justifyContent 属性定义了项目在主轴上的对齐方式。
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);//交叉轴的起点对齐。
        rv.setLayoutManager(flexboxLayoutManager);
        return this;
    }

    /**
     * 设置布局管理器
     */
    public RVUtils manager(RecyclerView.LayoutManager manager) {
        if (manager == null) {
            rv.setLayoutManager(new LinearLayoutManager(context));
        } else {
            rv.setLayoutManager(manager);
        }
        return this;
    }

    /**
     * 设置为横向布局
     */
    public RVUtils managerHorizontal(){
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        rv.setLayoutManager(manager);
        return this;
    }

    /**
     * 设置是否固定大小列表项
     */
    public RVUtils fixed(boolean b) {
        rv.setHasFixedSize(b);
        return this;
    }

    /**
     * 滑动到RV的指定位置
     */
    public void scrollToPosition(int position, List list) {
        if (position >= 0 && position <= list.size() - 1) {
            int firstItem = ((LinearLayoutManager) rv.getLayoutManager()).findFirstVisibleItemPosition();
            int lastItem = ((LinearLayoutManager) rv.getLayoutManager()).findLastVisibleItemPosition();
            if (position <= firstItem) {
                rv.scrollToPosition(position);
            } else if (position <= lastItem) {
                int top = rv.getChildAt(position - firstItem).getTop();
                rv.scrollBy(0, top);
            } else {
                rv.scrollToPosition(position);
            }
        }
    }

    /**
     * 获取滑动方向Y轴的距离，如果分割线是独立的，不要采用此方法
     * @return
     */
    public int getScrollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        android.view.View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisibleChildView.getHeight();
        return (position) * itemHeight - firstVisibleChildView.getTop();
    }

    /**
     * 获取滑动方向X轴的距离，如果分割线是独立的，不要采用此方法
     * @return
     */
    public int getScrollXDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        android.view.View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemWidth = firstVisibleChildView.getWidth();
        return (position) * itemWidth - firstVisibleChildView.getLeft();
    }

    /**
     * 支持滑动删除列表项
     * 需要注意数据集合不能是List，必须是ArrayList之类的有删除方法的
     * @param orientation 允许向哪些方向拖拽 仅限ItemTouchHelper.LEFT和ItemTouchHelper.RIGHT
     * @param onSelectBg 被选中的Item背景DrawableId，填入0表示无
     * @param onUnSelectBg 未被选中的Item背景DrawableId，填入0表示默认
     * @return
     */
    public RVUtils enableDragDeleteItem(final int orientation, final int onSelectBg, final int onUnSelectBg){
        //创建列表项触摸助手
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback(){
            /**
             * 设置滑动类型标记
             */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = 0;  // 禁止上下拖动
                int swipeFlags = orientation;  // 只允许从右向左滑动
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            /**
             * 滑动删除 Item 的操作
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                if (pos < 0 || pos > dataList.size()) {
                    return;
                }

                dataList.remove(pos);
                rv.getAdapter().notifyItemRemoved(pos);

                // 解决 RecyclerView 删除 Item 导致位置错乱的问题
                if (pos != dataList.size()) {
                    rv.getAdapter().notifyItemRangeChanged(pos, dataList.size() - pos);
                }
            }

            /**
             * 设置 Item 不支持长按拖动
             */
            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            /**
             * 设置 Item 支持滑动
             */
            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            /**
             * Item 被选中时候，改变 Item 的背景
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                // item 被选中的操作
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundResource(onSelectBg);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 移动过程中重新绘制 Item，随着滑动的距离，设置 Item 的透明度
             */
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                float x = Math.abs(dX) + 0.5f;
                float width = viewHolder.itemView.getWidth();
                float alpha = 1f - x / width;
                viewHolder.itemView.setAlpha(alpha);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            /**
             * 用户操作完毕或者动画完毕后调用，恢复 item 的背景和透明度
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 操作完毕后恢复颜色
                viewHolder.itemView.setBackgroundResource(onUnSelectBg);
                viewHolder.itemView.setAlpha(1.0f);
                super.clearView(recyclerView, viewHolder);
            }

        });
        mItemTouchHelper.attachToRecyclerView(rv);
        return this;
    }

    /**
     * 支持可拖拽列表项
     * @param dataList 列表项数据源
     * @param needDisableSome 是否需要禁用某些列表项的拖拽
     * @return
     */
    public RVUtils enableDraggableItem(final List<?> dataList, final boolean needDisableSome){
        //创建列表项触摸助手
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback(){
            /**
             * 必须实现的方法，设置是否滑动时间，以及拖拽的方向
             * @param recyclerView
             * @param viewHolder
             * @return
             */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //判断是否是网格布局，是则上下左右都可拖动，否则只能上下拖动
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager ||
                        recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            /**
             * 在拖动的时候不断回调的方法，需要将正在拖拽的item和集合的item进行交换元素，然后在通知适配器更新数据
             * @param recyclerView
             * @param viewHolder
             * @param target
             * @return
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(dataList, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(dataList, i, i - 1);
                    }
                }
                rv.getAdapter().notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            /**
             * onSwiped是替换后调用的方法，可以不用管。
             * @param viewHolder
             * @param direction
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            /**
             * 长按选中Item的时候开始调用
             * 在选中的时候设置高亮背景色，在完成的时候移除高亮背景色
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
//                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
//                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 手指松开的时候还原
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
//                viewHolder.itemView.setBackgroundColor(0);
            }

            /**
             * 重写拖拽不可用
             * @return
             */
            @Override
            public boolean isLongPressDragEnabled() {
                return !needDisableSome;
            }

        });
        mItemTouchHelper.attachToRecyclerView(rv);
        return this;
    }

    public void doGridInAttachToRV(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
        {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    if(needHeader&&!needFooter){
                        if(position==0){
                            return gridSpanCount;
                        }else {
                            return 1;
                        }
                    }else if(needHeader&&needFooter){
                        if(position==0||position==dataList.size()-1){
                            return gridSpanCount;
                        }else {
                            return 1;
                        }
                    }else if(!needHeader&&needFooter){
                        if(position==dataList.size()-1){
                            return gridSpanCount;
                        }else {
                            return 1;
                        }
                    }else {
                        return 1;
                    }

                }
            });
        }
    }


}