package com.yp.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yp.payment.R;
import com.yp.payment.jc_internet.OrderCountModel;
import com.yp.payment.utils.Jc_Utils;

import java.util.Date;
import java.util.List;

/**
 * Created by 20191024 on 2020/1/6.
 */

public class MylistviewAdapter extends BaseAdapter {
    private Context context;
    private List<OrderCountModel.DataBean> modelList;

    public MylistviewAdapter(Context context, List<OrderCountModel.DataBean> modelList){
        this.context = context;
        this.modelList = modelList;
    }
    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_listview, parent, false);
            viewHolder.tv_type0 = (TextView) convertView.findViewById(R.id.tv_type0);
            viewHolder.tv_moneyStr0 = (TextView) convertView.findViewById(R.id.tv_moneyStr0);
            viewHolder.tv_count0 = (TextView) convertView.findViewById(R.id.tv_count0);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderCountModel.DataBean model = modelList.get(position);
        int type = model.getType();
        String typeStr = "";
        if (type == 1){
            typeStr = "当日";
        }
        if (type == 2){
            typeStr = "昨日";
        }
        if (type == 3){
            typeStr = "本周";
        }
        if (type == 4){
            typeStr = "本月";
        }
        if (type == 5){
            typeStr = Jc_Utils.getPrevMonthDate(new Date(),1);
        }
        if (type == 6){
            typeStr = Jc_Utils.getPrevMonthDate(new Date(),2);
        }
        viewHolder.tv_type0.setText(typeStr);
        viewHolder.tv_moneyStr0.setText("¥"+model.getMoneyStr().trim());
        viewHolder.tv_count0.setText(String.valueOf(model.getCount())+"单");
        return convertView;
    }

    class ViewHolder{
        TextView tv_type0;
        TextView tv_moneyStr0;
        TextView tv_count0;
    }
}
