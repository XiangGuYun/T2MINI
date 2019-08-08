package com.yp.payment.input;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yp.payment.R;

public class KeyboardAdapter extends BaseAdapter {
    private Context context;
    TextView textView;

    InputKeyboardCallback inputKeyboardCallback;

    public KeyboardAdapter(Context context, TextView textView, InputKeyboardCallback inputKeyboardCallback) {
        this.context = context;
        this.textView = textView;
        this.inputKeyboardCallback = inputKeyboardCallback;
    }

    public KeyboardAdapter(Context context, InputKeyboardCallback inputKeyboardCallback) {
        this.context = context;
        this.inputKeyboardCallback = inputKeyboardCallback;
    }

    @Override
    public int getCount() {
        return 16;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_keyboard, null);
            viewHolder = new ViewHolder();
            viewHolder.tvKey = convertView.findViewById(R.id.tvKey);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == 3) {
            viewHolder.tvKey.setText("<-");
            viewHolder.tvKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String result = textView.getText().toString();
                    if (!TextUtils.isEmpty(result)) {
                        textView.setText(result.substring(0, result.length() - 1));
                    }
                }
            });
        } else if (position == 7) {
            viewHolder.tvKey.setText("清除");
            viewHolder.tvKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView.setText("");
                }
            });

        } else if (position == 11) {
            viewHolder.tvKey.setText("全额");
            viewHolder.tvKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    textView.setText("");
                }
            });
        } else if (position == 15) {
            viewHolder.tvKey.setText("返回");
            viewHolder.tvKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView.setText("");
                }
            });
        } else {
            switch (position) {
                case 0:
                    viewHolder.tvKey.setText("7");
                    break;
                case 1:
                    viewHolder.tvKey.setText("8");
                    break;
                case 2:
                    viewHolder.tvKey.setText("9");
                    break;
                case 4:
                    viewHolder.tvKey.setText("4");
                    break;
                case 5:
                    viewHolder.tvKey.setText("5");
                    break;
                case 6:
                    viewHolder.tvKey.setText("6");
                    break;
                case 8:
                    viewHolder.tvKey.setText("1");
                    break;
                case 9:
                    viewHolder.tvKey.setText("2");
                    break;
                case 10:
                    viewHolder.tvKey.setText("3");
                    break;
                case 12:
                    viewHolder.tvKey.setText("0");
                    break;
                case 13:
                    viewHolder.tvKey.setText("00");
                    break;
                case 14:
                    viewHolder.tvKey.setText(".");
                    break;


            }
            viewHolder.tvKey.setTag(viewHolder.tvKey.getText());
            viewHolder.tvKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tvKey = (String) v.getTag();
                    String result = textView.getText().toString();
                    if (result.contains(".")) {
                        if (TextUtils.equals(tvKey, ".")) {

                        } else {
//                            int index = result.lastIndexOf(".");
                            textView.setText(result + tvKey);
                        }
                    } else if (TextUtils.isEmpty(result)) {
                        if (TextUtils.equals(tvKey, ".")) {
                            textView.setText("0.");
                        } else {
                            textView.setText(result + tvKey);
                        }
                    } else {
                        textView.setText(result + tvKey);
                    }
                }
            });
        }


        return convertView;
    }

    class ViewHolder {
        TextView tvKey;

    }
}
