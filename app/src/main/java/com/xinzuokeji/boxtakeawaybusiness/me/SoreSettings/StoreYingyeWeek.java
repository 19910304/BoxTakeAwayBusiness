package com.xinzuokeji.boxtakeawaybusiness.me.SoreSettings;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//营业时间星期
public class StoreYingyeWeek extends BaseActivity {
    private ImageButton header_back;
    LinearLayout ll_sunday, ll_monday, ll_tuesday, ll_wednesday, ll_thursday, ll_friday, ll_saturday;
    ImageView img_sunday, img_monday, img_tuesday, img_wednesday, img_thursday, img_friday, img_saturday;
    List<String> YingyeWeek;

    //设置选中标记
    String flag7 = "0", flag1 = "0", flag2 = "0", flag3 = "0", flag4 = "0", flag5 = "0", flag6 = "0";
    String weekday = "", businessHours = "";

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_store_yingye_week);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        weekday = getIntent().getStringExtra("YingyeWeek");
        businessHours = getIntent().getStringExtra("business_hours");
        TextView textView = findViewById(R.id.header_title);
        textView.setText("重复");
        ll_sunday = findViewById(R.id.ll_sunday);
        ll_monday = findViewById(R.id.ll_monday);
        ll_tuesday = findViewById(R.id.ll_tuesday);
        ll_wednesday = findViewById(R.id.ll_wednesday);
        ll_thursday = findViewById(R.id.ll_thursday);
        ll_friday = findViewById(R.id.ll_friday);
        ll_saturday = findViewById(R.id.ll_saturday);
//"星期一, 星期三, 星期四, 星期天, 星期二, 星期五
        img_sunday = findViewById(R.id.img_sunday);
        img_monday = findViewById(R.id.img_monday);
        img_tuesday = findViewById(R.id.img_tuesday);
        img_wednesday = findViewById(R.id.img_wednesday);
        img_thursday = findViewById(R.id.img_thursday);
        img_friday = findViewById(R.id.img_friday);
        img_saturday = findViewById(R.id.img_saturday);
        //将string转化为集合
        Log.i("yinr", weekday + "==");

        stringToList(weekday.trim());
        // 当集合大小为0时，添加元素前面会多一个空元素
        YingyeWeek = new ArrayList<>(stringToList(weekday.trim()));
        Log.i("yinr", YingyeWeek + "--" + YingyeWeek.size());
        for (int i = 0; i < YingyeWeek.size(); i++) {
            if (YingyeWeek.get(i).equals("星期天")) {
                img_sunday.setVisibility(View.VISIBLE);
                flag7 = "1";
                Log.i("week", "7");
            }
            if (YingyeWeek.get(i).equals("星期一")) {
                img_monday.setVisibility(View.VISIBLE);
                flag1 = "1";
                Log.i("week", "1");
            }
            if (YingyeWeek.get(i).equals("星期二")) {
                img_tuesday.setVisibility(View.VISIBLE);
                flag2 = "1";
                Log.i("week", "2");
            }
            if (YingyeWeek.get(i).equals("星期三")) {
                img_wednesday.setVisibility(View.VISIBLE);
                flag3 = "1";
                Log.i("week", "3");

            }
            if (YingyeWeek.get(i).equals("星期四")) {
                img_thursday.setVisibility(View.VISIBLE);
                flag4 = "1";
                Log.i("week", "4");
            }
            if (YingyeWeek.get(i).equals("星期五")) {
                img_friday.setVisibility(View.VISIBLE);
                flag5 = "1";
                Log.i("week", "5");
            }
            if (YingyeWeek.get(i).equals("星期六")) {
                img_saturday.setVisibility(View.VISIBLE);
                flag6 = "1";
                Log.i("week", "6");
            }
        }
    }

    private List<String> stringToList(String strs) {
        String str[] = strs.split(",");
        return Arrays.asList(str);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_sunday.setOnClickListener(this);
        ll_monday.setOnClickListener(this);
        ll_tuesday.setOnClickListener(this);
        ll_wednesday.setOnClickListener(this);
        ll_thursday.setOnClickListener(this);
        ll_friday.setOnClickListener(this);
        ll_saturday.setOnClickListener(this);
    }

    //
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                HashMap<String, String> extras = new HashMap<>();
                extras.put("YingyeWeek", YingyeWeek.toString().replaceAll("[\\[\\]]", ""));
                //去掉中括号
                extras.put("business_hours", businessHours);
                try {
                    gotoActivityAndFinish(StoreYingyeTime.class.getName(), extras);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_sunday:
                if (flag7.equals("0")) {
                    flag7 = "1";
                    YingyeWeek.add("星期天");
                    img_sunday.setVisibility(View.VISIBLE);
                } else {
                    flag7 = "0";
                    remove(YingyeWeek, "星期天");
                    img_sunday.setVisibility(View.GONE);
                }

                break;
            case R.id.ll_monday:
                if (flag1.equals("0")) {
                    flag1 = "1";
                    YingyeWeek.add("星期一");
                    img_monday.setVisibility(View.VISIBLE);
                } else {
                    flag1 = "0";
                    remove(YingyeWeek, "星期一");
                    img_monday.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_tuesday:
                if (flag2.equals("0")) {
                    flag2 = "1";
                    YingyeWeek.add("星期二");
                    img_tuesday.setVisibility(View.VISIBLE);
                } else {
                    flag2 = "0";
                    remove(YingyeWeek, "星期二");
                    img_tuesday.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_wednesday:
                if (flag3.equals("0")) {
                    flag3 = "1";
                    YingyeWeek.add("星期三");
                    img_wednesday.setVisibility(View.VISIBLE);
                } else {
                    flag3 = "0";
                    remove(YingyeWeek, "星期三");
                    img_wednesday.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_thursday:
                if (flag4.equals("0")) {
                    flag4 = "1";
                    YingyeWeek.add("星期四");
                    img_thursday.setVisibility(View.VISIBLE);
                } else {
                    flag4 = "0";
                    remove(YingyeWeek, "星期四");
                    img_thursday.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_friday:
                if (flag5.equals("0")) {
                    flag5 = "1";
                    YingyeWeek.add("星期五");
                    img_friday.setVisibility(View.VISIBLE);
                } else {
                    flag5 = "0";
                    remove(YingyeWeek, "星期五");
                    img_friday.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_saturday:
                if (flag6.equals("0")) {
                    flag6 = "1";
                    YingyeWeek.add("星期六");
                    img_saturday.setVisibility(View.VISIBLE);
                } else {
                    flag6 = "0";
                    remove(YingyeWeek, "星期六");
                    img_saturday.setVisibility(View.GONE);
                }
                break;
            default:

                break;
        }
    }

    /*
     * 移除数组元素
     *
     */
    public void remove(List<String> list, String target) {
        for (int i = list.size() - 1; i >= 0; i--) {
            String item = list.get(i);
            if (target.equals(item)) {
                list.remove(item);
            }
            Log.i("list", list.size() + "-");
        }
        print(list);
    }

    private void print(List<String> list) {
        for (String item : list) {
            System.out.println("元素值：" + item);
        }
    }
}
