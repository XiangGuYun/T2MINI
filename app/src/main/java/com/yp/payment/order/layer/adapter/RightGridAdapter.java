package com.yp.payment.order.layer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yp.payment.R;
import com.yp.payment.order.layer.model.AllCaiModel;
import com.yp.payment.order.layer.utils.MoneyUtils;
import com.yp.payment.order.layer.view.RoundImageView;

import java.util.List;

/**
 * Created by Administrator on 2020/6/16.
 */

public class   RightGridAdapter  extends BaseAdapter {
    private Context context;
    private List<AllCaiModel.DataBean.ResultsBean> data;
    private RightGridviewInterface rightGridviewInterface;
    public RightGridAdapter(Context context, List<AllCaiModel.DataBean.ResultsBean> data,RightGridviewInterface rightGridviewInterface) {
        this.context = context;
        this.data = data;
        this.rightGridviewInterface = rightGridviewInterface;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RightGridAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new RightGridAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_right_grid, parent, false);
            viewHolder.img =  convertView.findViewById(R.id.img);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.tv_realfee = (TextView) convertView.findViewById(R.id.tv_realfee);
            viewHolder.tv_oldfee = (TextView) convertView.findViewById(R.id.tv_oldfee);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RightGridAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(data.get(position).getName());
        viewHolder.tv_realfee.setText("¥"+ MoneyUtils.changeF2Y(data.get(position).getPrice()));
//        if (data.get(position).getVipPrice() == 0){
//            viewHolder.tv_oldfee.setVisibility(View.GONE);
//        }else{
//            viewHolder.tv_oldfee.setVisibility(View.VISIBLE);
//            viewHolder.tv_oldfee.setText("会员价 ¥"+MoneyUtils.changeF2Y(data.get(position).getVipPrice()));
//        }


        Glide.with(context).load(data.get(position).getProductPic()).into(viewHolder.img);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightGridviewInterface.add(position);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView title;
        private RoundImageView img;
        private TextView tv_realfee;
        private TextView tv_oldfee;
    }
}