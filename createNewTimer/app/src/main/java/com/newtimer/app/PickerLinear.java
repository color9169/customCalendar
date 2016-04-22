package com.newtimer.app;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by color on 16/4/21.
 */
public class PickerLinear extends LinearLayout {
    View view;
    MyGridView myGridView;
    List<String> dateList;
    Calendar calendar;
    MyPickerAdapter myPickerAdapter;
    TextView dateTxt;
    Context mContext;
    private int month;
    private GetDateStrInterface myInterface;

    public PickerLinear(Context context, int month) {
        super(context);
        mContext = context;
        this.month = month;
        view = LayoutInflater.from(context).inflate(R.layout.picker_layout, this, true);
        initView();
        init();
        setListener();
        setAdapter();
    }

    public void setInterface(GetDateStrInterface interfaceParams) {
        this.myInterface = interfaceParams;
    }

    public PickerLinear(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PickerLinear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        myGridView = (MyGridView) view.findViewById(R.id.picker_grid);
        myGridView.setNumColumns(7);
        dateTxt = (TextView) view.findViewById(R.id.date_txt);
    }

    private void init() {

        dateList = new ArrayList<>();
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        if (month > 12) {
            int someYear = month / 12;
            year += someYear;
            month = month - 12 * someYear;
            if (month == 0) {
                month = 12;
                year -= 1;
            }
        }

        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);


        int taday = calendar.get(Calendar.DAY_OF_WEEK);
        //如果不是每月第一天,则追加空白天数
        if (taday != 1) {
            for (int i = 0; i < taday - 1; i++) {
                dateList.add(" , ");
            }
        }
        for (int i = 1; i <= day; i++) {
            dateList.add(year + "-" + month + "-" + i + "," + i);
        }
        myPickerAdapter = new MyPickerAdapter(mContext);
        myPickerAdapter.setData(dateList);
        dateTxt.setText(year + "年" + month + "月");
    }


    public void setListener() {
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String str = (String) adapterView.getItemAtPosition(i);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = format.parse(str);
                    Calendar c = Calendar.getInstance();
                    String str_1 = str.split(",")[0];
                    String currentStr = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
                    Date currentDate = format.parse(currentStr);
                    if (date.getTime() >= currentDate.getTime() || currentStr.equals(str_1)) {  //判断只有今天之后包括今天的日期才可以点
                        if (((MainActivity) mContext).frist == null) {
                            ((MainActivity) mContext).frist = (TextView) view.findViewById(R.id.picker_txt);
                            ((MainActivity) mContext).frist.setBackgroundColor(getResources().getColor(R.color.hotel_intro_txt_color));
                            TextView tx = ((MainActivity) mContext).frist;
                            tx.setTextColor(getResources().getColor(R.color.white));
                            if (currentStr.equals(str_1)) {
                                tx.setText(tx.getText().toString().substring(0, tx.getText().toString().indexOf("\n")) + "\n入住");
                            } else {
                                tx.setText(tx.getText().toString() + "\n入住");
                            }
                            myInterface.getPreTime(str);
                        } else if (((MainActivity) mContext).last == null && ((MainActivity) mContext).frist != null && !str_1.trim().equals(((MainActivity) mContext).frist.getText().toString().trim())) {
                            String txTag = ((MainActivity) mContext).frist.getTag().toString();
                            Date fristDate = format.parse(txTag);
                            if (fristDate.getTime() < date.getTime()) {
                                ((MainActivity) mContext).last = (TextView) view.findViewById(R.id.picker_txt);
                                TextView tx = ((MainActivity) mContext).last;
                                tx.setText(tx.getText().toString() + "\n结束");
                                tx.setTextColor(getResources().getColor(R.color.white));
                                ((MainActivity) mContext).last.setBackgroundColor(getResources().getColor(R.color.hotel_intro_txt_color));
                                myInterface.getNextTime(str);
                            } else {
                                Toast.makeText(mContext, "不能选择之前的时间", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
//                        Toast.makeText(mContext, "不能选择过去的时间", Toast.LENGTH_SHORT).show();
                        //这个提示没什么卵用...只要不傻都知道
                    }
                } catch (ParseException exception) {
                    Toast.makeText(mContext, "报错了" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setAdapter() {
        myGridView.setAdapter(myPickerAdapter);
    }


    interface GetDateStrInterface { //回调接口,让前边儿页面可以获取到时间
        void getPreTime(String preStr);

        void getNextTime(String nextStr);
    }
}
