package com.yp.payment.order.layer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yp.payment.R;
import com.yp.payment.order.layer.model.AllCaiTypeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/6/16.
 */

public class MyListAdapter  extends BaseAdapter {
    private List<AllCaiTypeModel.DataBean.ResultsBean> data = new ArrayList<AllCaiTypeModel.DataBean.ResultsBean>();
    private Context mContext;
    private LayoutInflater mInflater;
    private int curPositon;

    public void setCurPositon(int curPositon) {
        this.curPositon = curPositon;
    }

    public int getCurPositon() {
        return curPositon;
    }

    public MyListAdapter(Context context, List<AllCaiTypeModel.DataBean.ResultsBean> data) {
        mContext = context;
        this.data = data;
        mInflater = LayoutInflater.from(mContext);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item,null);
            vh = new ViewHolder();
            vh.tv = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        vh.tv.setText(data.get(position).getName());

        if (position == curPositon) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.bg_activity_gray));
            vh.tv.setTextColor(mContext.getResources().getColor(R.color.ligth_bule));
        }else{
            vh.tv.setTextColor(mContext.getResources().getColor(R.color.text_gray));
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        return convertView;
    }
    class ViewHolder {
        TextView tv;
    }
}