package com.yp.payment.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yp.payment.update.OrderDetail;
import com.yp.payment.Constant;
import com.yp.payment.R;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private static final String TAG = "OrderListAdapter";
    Context mContext;

    int selectedMode = 0;

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

        OrderDetail orderDetail = Constant.curOrderList.get(i);

        viewHolder.item_order_list_bg.setTag(i);

        viewHolder.item_tv_pay_date.setText(orderDetail.getDateTime());

        if (orderDetail.getOrderType() == 2) {//支付类型 0：二维码支付，1：人脸支付，2：实体卡支付，3：其他支付,  4:商户扫码支付
            viewHolder.item_tv_mpde_tag.setBackgroundResource(R.drawable.item_list_wecha);
            viewHolder.item_tv_mpde_tag.setTextColor(mContext.getResources().getColor(R.color.color_wechat));
            viewHolder.item_tv_mpde_tag.setText("刷卡");
        } else if (orderDetail.getOrderType() == 3) {
            viewHolder.item_tv_mpde_tag.setBackgroundResource(R.drawable.item_list_cash);
            viewHolder.item_tv_mpde_tag.setText("现金");
            viewHolder.item_tv_mpde_tag.setTextColor(mContext.getResources().getColor(R.color.color_cash));
        } else if (orderDetail.getOrderType() == 4) {
            viewHolder.item_tv_mpde_tag.setBackgroundResource(R.drawable.item_list_ali);
            viewHolder.item_tv_mpde_tag.setText("二维码");
            viewHolder.item_tv_mpde_tag.setTextColor(mContext.getResources().getColor(R.color.color_ali));
            viewHolder.item_order_list_bg.setBackgroundColor(mContext.getResources().getColor(R.color.unselect_pay_mode));
        }

        viewHolder.item_tv_pay_realprice.setText("实收: ￥" + orderDetail.getRealPrice());
        viewHolder.item_tv_pay_price.setText("应收: ￥" + orderDetail.getPrice());

        if (selectedMode == i) {
            viewHolder.item_order_list_bg.setBackgroundResource(R.color.unselect_pay_mode);
        } else {
            viewHolder.item_order_list_bg.setBackgroundResource(R.color.white);
        }

        viewHolder.item_order_list_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "v.getTag()====: " + v.getTag());

                int pos = (int) v.getTag();
                Constant.curOrderSeq = pos;

                selectedMode = pos;

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Constant.curOrderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_order_list_bg;
        TextView item_tv_pay_date;
        TextView item_tv_mpde_tag;
        TextView item_tv_pay_realprice;
        TextView item_tv_pay_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_order_list_bg = itemView.findViewById(R.id.item_order_list_bg);
            item_tv_pay_date = itemView.findViewById(R.id.item_tv_pay_date);
            item_tv_mpde_tag = itemView.findViewById(R.id.item_tv_mpde_tag);
            item_tv_pay_realprice = itemView.findViewById(R.id.item_tv_pay_realprice);
            item_tv_pay_price = itemView.findViewById(R.id.item_tv_pay_price);
        }
    }
}
