package com.newtimer.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by color on 16/4/21.
 */
public class MyPickerAdapter extends BaseAdapter {
    private List<String> dateList;
    private Context mContext;

    public MyPickerAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<String> data) {
        this.dateList = data;
    }

    @Override
    public int getCount() {
        if (dateList != null && dateList.size() > 0) {
            return dateList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return dateList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            vh = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.picker_item, null, false);
            vh.tx = (TextView) view.findViewById(R.id.picker_txt);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        String[] strArr = dateList.get(i).toString().split(",");
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        if (strArr[0].equals(" ")) {
            vh.tx.setText(strArr[0]);
        } else {
            int now_month = Integer.parseInt(strArr[0].split("-")[1]);
            int day = Integer.parseInt(strArr[1].trim());
            if ((i + 1) % 7 == 1) {  //计算为周日时显示的颜色
                vh.tx.setTextColor(mContext.getResources().getColor(R.color.hotel_intro_txt_color));
            } else if ((i + 1) % 7 == 0) {//计算为周六时显示的颜色
                vh.tx.setTextColor(mContext.getResources().getColor(R.color.hotel_intro_txt_color));
            } else {
                vh.tx.setTextColor(mContext.getResources().getColor(R.color.hotel_name_txt_color));
            }
            StringBuffer dateValue = new StringBuffer();
            dateValue.append(strArr[1]);
            if (day == calendar.get(Calendar.DAY_OF_MONTH) && month == now_month) {
                dateValue.append("\n今天");
            }
            vh.tx.setText(dateValue);
            vh.tx.setTag(strArr[0]);

        }
        return view;
    }

    class ViewHolder {
        TextView tx;
    }
}
