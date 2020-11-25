package com.yp.payment.adapter;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yp.payment.R;
import com.yp.payment.input.KeyBoardCallback;

import java.util.ArrayList;
import java.util.Map;

/**
 * 九宫格键盘适配器
 */
public class KeyBoardAdapter extends BaseAdapter {
    private static final String TAG = "KeyBoardAdapter";
    int keys[] = {KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4,
            KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_8, KeyEvent.KEYCODE_9, KeyEvent.KEYCODE_0,
            KeyEvent.KEYCODE_PERIOD, KeyEvent.KEYCODE_DEL};

    private Context mContext;
    private ArrayList<Map<String, String>> valueList;
    KeyBoardCallback mKeyBoardCallback;

    public KeyBoardAdapter(Context mContext, ArrayList<Map<String, String>> valueList) {
        this.mContext = mContext;
        this.valueList = valueList;
    }

    public void setKeyBoardCallback(KeyBoardCallback keyBoardCallback) {
        mKeyBoardCallback = keyBoardCallback;
    }

    @Override
    public int getCount() {
        return valueList.size();
    }

    @Override
    public Object getItem(int position) {
        return valueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.grid_item_virtual_keyboard, null);
            viewHolder = new ViewHolder();
            viewHolder.btnKey = (TextView) convertView.findViewById(R.id.btn_keys);
            viewHolder.imgDelete = (RelativeLayout) convertView.findViewById(R.id.imgDelete);
            viewHolder.rl_layout = (RelativeLayout) convertView.findViewById(R.id.rl_layout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 10) {
            viewHolder.imgDelete.setVisibility(View.INVISIBLE);
            viewHolder.btnKey.setVisibility(View.VISIBLE);
            viewHolder.btnKey.setText(valueList.get(position).get("name"));
//            viewHolder.btnKey.setBackgroundColor(Color.parseColor("#e0e0e0"));
        } else if (position == 11) {
            viewHolder.btnKey.setBackgroundResource(R.drawable.icon_delete);
            viewHolder.imgDelete.setVisibility(View.VISIBLE);
            viewHolder.btnKey.setVisibility(View.INVISIBLE);

        } else {
            viewHolder.imgDelete.setVisibility(View.INVISIBLE);
            viewHolder.btnKey.setVisibility(View.VISIBLE);
            viewHolder.btnKey.setText(valueList.get(position).get("name"));
        }
        viewHolder.rl_layout.setTag(position);
        viewHolder.rl_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int pos = (int) view.getTag();
                if (pos == 11 && mKeyBoardCallback != null) {
                    mKeyBoardCallback.clearAllData();
                }
                return false;
            }
        });
        viewHolder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mKeyBoardCallback != null) {
                    mKeyBoardCallback.OnKeyCallback();
                }
                int pos = (int) view.getTag();
                Log.d(TAG, "onClick: " + pos);
                typeIn(keys[pos]);
            }
        });
        return convertView;
    }

    public void typeIn(final int KeyCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                    Log.e("sendKeyDownUpSync", e.toString());
                }
            }
        }).start();
    }

    /**
     * 存放控件
     */
    public final class ViewHolder {
        public RelativeLayout rl_layout;
        public TextView btnKey;
        public RelativeLayout imgDelete;
    }
}
