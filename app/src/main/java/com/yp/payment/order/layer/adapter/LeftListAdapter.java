package com.yp.payment.order.layer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yp.payment.R;
import com.yp.payment.order.layer.model.AllCaiModel;
import com.yp.payment.order.layer.utils.MoneyUtils;

import java.util.ArrayList;
import java.util.List;

import static com.yp.payment.ui.OrderDishActivity.selectedIndex;

/**
 * Created by Administrator on 2020/6/16.
 */

public class LeftListAdapter extends BaseAdapter {
    private List<AllCaiModel.DataBean.ResultsBean> data = new ArrayList<AllCaiModel.DataBean.ResultsBean>();
    private Context mContext;
    private LayoutInflater mInflater;
    private LeftListDelInterFace leftListDelInterFace;

    public LeftListAdapter(Context context, List<AllCaiModel.DataBean.ResultsBean> data,LeftListDelInterFace leftListDelInterFace) {
        mContext = context;
        this.data = data;
        mInflater = LayoutInflater.from(mContext);
        this.leftListDelInterFace = leftListDelInterFace;
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
        LeftListAdapter.ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.left_list_item,null);
            vh = new LeftListAdapter.ViewHolder();
            vh.tv = (TextView) convertView.findViewById(R.id.text1);
            vh.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            vh.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(vh);
        }else{
            vh = (LeftListAdapter.ViewHolder)convertView.getTag();
        }
        vh.tv.setText(data.get(position).getName());
        vh.tv_count.setText("x"+data.get(position).getCount()*(selectedIndex+1));
        vh.tv_price.setText("¥"+ MoneyUtils.changeF2Y((selectedIndex+1)*data.get(position).getCount()*data.get(position).getPrice()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftListDelInterFace.del(position);
            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView tv;
        TextView tv_count;
        TextView tv_price;
    }
}