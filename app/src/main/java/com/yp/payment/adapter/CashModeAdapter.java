package com.yp.payment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yp.payment.R;
import com.yp.payment.interfaces.RecommendPriceCallback;

import java.util.ArrayList;
import java.util.List;

public class CashModeAdapter extends RecyclerView.Adapter<CashModeAdapter.ViewHolder> {
    Context mContext;
    RecommendPriceCallback recommendPriceCallback;
    List<Integer> mlist;

    public CashModeAdapter(Context mContext, RecommendPriceCallback recommendPriceCallback) {
        this.mContext = mContext;
        this.recommendPriceCallback = recommendPriceCallback;
        mlist = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cash, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.item_tv_cash.setText("￥" + mlist.get(i));
        viewHolder.item_tv_cash.setTag(mlist.get(i));
        viewHolder.item_tv_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = (int) v.getTag();
                recommendPriceCallback.priceCallback(price);
            }
        });
    }

    public void setMlist(List<Integer> mList) {
        this.mlist.clear();
        mlist.addAll(mList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_tv_cash;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_tv_cash = itemView.findViewById(R.id.item_tv_cash);
        }
    }
}
