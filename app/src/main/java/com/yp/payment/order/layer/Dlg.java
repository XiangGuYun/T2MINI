package com.yp.payment.order.layer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.yp.payment.R;
import com.yp.payment.adapter.MylistviewAdapter;
import com.yp.payment.jc_internet.OrderCountModel;
import com.yp.payment.order.layer.utils.ContextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理所有对话框的创建
 */
public class Dlg {

    public static LoadingDailog getLoadingDialog(Context ctx) {
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(ctx)
                .setMessage("加载中...")
                .setCancelable(true)
                .setCancelOutside(true);
        return loadBuilder.create();
    }

    public static class OrderStatDialog extends Dialog {

        public MylistviewAdapter adapter;

        public List<OrderCountModel.DataBean> list = new ArrayList<>();

        public OrderStatDialog(@NonNull Context context, int themeResId) {
            super(context, themeResId);
        }

        /**
         * 更新列表数据并显示
         * @param data
         */
        public void updateAndShow(List<OrderCountModel.DataBean> data) {
            list.clear();
            list.addAll(data);
            adapter.notifyDataSetChanged();
            show();
        }
    }

    public static OrderStatDialog getOrderStatDialog(Context ctx) {
        OrderStatDialog mPreviewDialog = new OrderStatDialog(ctx, R.style.list_dialog_theme);
        mPreviewDialog.setCancelable(true);
        mPreviewDialog.setCanceledOnTouchOutside(true);
        View alertReimderView = View.inflate(ctx, R.layout.item_popview1, null);
        View tv_cancle = alertReimderView.findViewById(R.id.tv_cancle);
        ListView listview = alertReimderView.findViewById(R.id.listview);
        mPreviewDialog.adapter = new MylistviewAdapter(ctx, mPreviewDialog.list);
        listview.setAdapter(mPreviewDialog.adapter);
        tv_cancle.setOnClickListener(v -> {
            if (mPreviewDialog.isShowing()) {
                mPreviewDialog.dismiss();
                ContextUtils.INSTANCE.hideNavigation((Activity) ctx);
            }
        });
        mPreviewDialog.setContentView(alertReimderView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPreviewDialog.setCancelable(false);
        return mPreviewDialog;
    }

}
