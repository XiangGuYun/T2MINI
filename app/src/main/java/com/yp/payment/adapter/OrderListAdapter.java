package com.yp.payment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yp.payment.R;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    Context mContext;

    public OrderListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        switch (i) {
            case 0:

                viewHolder.item_tv_mpde_tag.setBackgroundResource(R.drawable.item_list_cash);
                viewHolder.item_tv_mpde_tag.setText("现金");
                viewHolder.item_tv_mpde_tag.setTextColor(mContext.getResources().getColor(R.color.color_cash));
                break;

            case 1:
                viewHolder.item_tv_mpde_tag.setBackgroundResource(R.drawable.item_list_ali);
                viewHolder.item_tv_mpde_tag.setText("支付宝");
                viewHolder.item_tv_mpde_tag.setTextColor(mContext.getResources().getColor(R.color.color_ali));
                viewHolder.item_order_list_bg.setBackgroundColor(mContext.getResources().getColor(R.color.unselect_pay_mode));
                break;
            case 2:
                viewHolder.item_tv_mpde_tag.setBackgroundResource(R.drawable.item_list_wecha);
                viewHolder.item_tv_mpde_tag.setTextColor(mContext.getResources().getColor(R.color.color_wechat));
                viewHolder.item_tv_mpde_tag.setText("微信支付");
                break;
            case 3:
                viewHolder.item_tv_mpde_tag.setBackgroundResource(R.drawable.item_list_cash);
                viewHolder.item_tv_mpde_tag.setText("现金");
                viewHolder.item_tv_mpde_tag.setBackgroundResource(R.drawable.item_list_cash);
                viewHolder.item_tv_mpde_tag.setText("现金");
                viewHolder.item_tv_mpde_tag.setTextColor(mContext.getResources().getColor(R.color.color_cash));
                break;
            case 4:
                viewHolder.item_tv_mpde_tag.setBackgroundResource(R.drawable.item_list_wecha);
                viewHolder.item_tv_mpde_tag.setTextColor(mContext.getResources().getColor(R.color.color_wechat));
                viewHolder.item_tv_mpde_tag.setText("微信支付");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout item_order_list_bg;
        TextView item_tv_pay_date;
        TextView item_tv_mpde_tag;
        TextView item_tv_pay_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_order_list_bg = itemView.findViewById(R.id.item_order_list_bg);
            item_tv_pay_date = itemView.findViewById(R.id.item_tv_pay_date);
            item_tv_mpde_tag = itemView.findViewById(R.id.item_tv_mpde_tag);
            item_tv_pay_price = itemView.findViewById(R.id.item_tv_pay_price);
        }
    }
}
