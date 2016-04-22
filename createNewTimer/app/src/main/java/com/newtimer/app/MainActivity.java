package com.newtimer.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PickerLinear.GetDateStrInterface {

    PickerLinear pickerLinear;
    Calendar calendar;
    ListView list_view;
    List<PickerLinear> pickerLinears;
    TextView tx1, tx2;
    Button btn_reset;
    public TextView frist;
    public TextView last;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_view = (ListView) findViewById(R.id.list_view);
        tx1 = (TextView) findViewById(R.id.txt1);
        tx2 = (TextView) findViewById(R.id.txt2);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        init();
        setListener();
    }

    private void init() {
        pickerLinears = new ArrayList<>();
        calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        for (int i = 0; i < 12; i++) {
            pickerLinear = new PickerLinear(this, month + i);
            pickerLinear.setInterface(this);
            pickerLinears.add(pickerLinear);
        }
        listAdapter = new ListAdapter();
        list_view.setAdapter(listAdapter);
    }

    private void setListener() {
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (frist != null) {
                    String fristStr = frist.getText().toString().substring(0, frist.getText().toString().indexOf("\n")).trim();
                    int fristDate = Integer.parseInt(fristStr);
                    if (fristDate % 7 == 1) {
                        frist.setTextColor(getResources().getColor(R.color.hotel_intro_txt_color));
                    } else if (fristDate % 7 == 0) {
                        frist.setTextColor(getResources().getColor(R.color.hotel_intro_txt_color));
                    } else {
                        frist.setTextColor(getResources().getColor(R.color.hotel_name_txt_color));
                    }
                    frist.setBackgroundColor(getResources().getColor(R.color.back_color));
                    if (fristStr.equals(calendar.get(Calendar.DAY_OF_MONTH) + "")) {
                        frist.setText(fristStr + "\n今天");
                    } else {
                        frist.setText(fristStr);
                    }
                }
                if (last != null) {
                    String lastStr = last.getText().toString().substring(0, last.getText().toString().indexOf("\n")).trim();
                    int lastDate = Integer.parseInt(lastStr);
                    if (lastDate % 7 == 1) {
                        last.setTextColor(getResources().getColor(R.color.hotel_intro_txt_color));
                    } else if (lastDate % 7 == 0) {
                        last.setTextColor(getResources().getColor(R.color.hotel_intro_txt_color));
                    } else {
                        last.setTextColor(getResources().getColor(R.color.hotel_name_txt_color));
                    }
                    last.setBackgroundColor(getResources().getColor(R.color.back_color));
                    last.setText(lastStr);
                }
                frist = null;
                last = null;
                tx1.setText("入住时间");
                tx2.setText("离店时间");
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void getPreTime(String preStr) {
        Toast.makeText(this, preStr, Toast.LENGTH_SHORT).show();
        String date = preStr.split(",")[0];
        String[] dateArr = date.split("-");
        tx1.setText("入住时间\n" + dateArr[0] + "年" + dateArr[1] + "月" + dateArr[2] + "日");
    }

    @Override
    public void getNextTime(String nextStr) {
        Toast.makeText(this, nextStr, Toast.LENGTH_SHORT).show();
        String date = nextStr.split(",")[0];
        String[] dateArr = date.split("-");
        tx2.setText("离店时间\n" + dateArr[0] + "年" + dateArr[1] + "月" + dateArr[2] + "日");
    }


    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pickerLinears.size();
        }

        @Override
        public Object getItem(int i) {
            return pickerLinears.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            return pickerLinears.get(i);
        }
    }

}
