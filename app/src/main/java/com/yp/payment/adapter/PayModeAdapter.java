package com.yp.payment.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yp.payment.Consts;
import com.yp.payment.R;
import com.yp.payment.interfaces.PayModeCallback;

public class PayModeAdapter extends RecyclerView.Adapter<PayModeAdapter.ViewHolder> {
    private static final String TAG = "PayModeAdapter";
    Context mContext;
    PayModeCallback payModeCallback;

    public PayModeAdapter(Context mContext, PayModeCallback payModeCallback) {
        this.mContext = mContext;
        this.payModeCallback = payModeCallback;
    }

    int selectedMode = 0;
    int lastSelectedMode = 0;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pay_mode, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Log.d(TAG, "onBindViewHolder,i===" + i);
        viewHolder.item_iv_pay_mode_icon.setImageResource(Consts.payModeIcons[i]);
        viewHolder.item_tv_pay_mode_name.setText(Consts.payModes[i]);

        viewHolder.item_tv_pay_mode_name.setWidth(500);
        if (selectedMode == i) {
            viewHolder.itemiv_pay_mode_bg_layout.setBackgroundResource(R.color.unselect_pay_mode);
        } else {
            viewHolder.itemiv_pay_mode_bg_layout.setBackgroundResource(R.color.white);
        }
        viewHolder.itemiv_pay_mode_bg_layout.setTag(i);
        viewHolder.itemiv_pay_mode_bg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                selectedMode = pos;
                payModeCallback.onSelectedMode(pos);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Consts.payModeIcons.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemiv_pay_mode_bg_layout;
        ImageView item_iv_pay_mode_icon;
        TextView item_tv_pay_mode_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemiv_pay_mode_bg_layout = itemView.findViewById(R.id.itemiv_pay_mode_bg_layout);
            item_iv_pay_mode_icon = itemView.findViewById(R.id.item_iv_pay_mode_icon);
            item_tv_pay_mode_name = itemView.findViewById(R.id.item_tv_pay_mode_name);
        }
    }
}
