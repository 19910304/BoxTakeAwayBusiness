package com.xinzuokeji.boxtakeawaybusiness.me.SoreSettings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.ACache;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

//营业时间
public class StoreYingyeTime extends BaseActivity {
    private ImageButton header_back;
    LinearLayout ll_star_time, ll_stop_time, ll_yingye_time_week;
    TextView tv_star_time, tv_stop_time;
    Calendar c = Calendar.getInstance();
    NetService netService;
    private Button bt_sure_change_time;
    public String weekday, businessHours = "";

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_yingye_time);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        //接收工作日期
        businessHours = getIntent().getStringExtra("business_hours");
        Log.i("hours", businessHours + "--");
        weekday = getIntent().getStringExtra("YingyeWeek");
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("营业时间");
        ll_yingye_time_week = findViewById(R.id.ll_yingye_time_week);
        ll_star_time = findViewById(R.id.ll_star_time);
        ll_stop_time = findViewById(R.id.ll_stop_time);
        tv_star_time = findViewById(R.id.tv_star_time);//开始时间
        tv_stop_time = findViewById(R.id.tv_stop_time);//结束时间
        if (Valid.isNotNullOrEmpty(businessHours)) {
            String hoursStart = businessHours.substring(0, 5);
            tv_star_time.setText(hoursStart);
            String hoursEnd = businessHours.substring(6);
            tv_stop_time.setText(hoursEnd);
        } else {
            tv_star_time.setText("");
            tv_stop_time.setText("");
        }
        bt_sure_change_time = findViewById(R.id.bt_sure_change_time);//
        netService = new NetService(this);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_yingye_time_week.setOnClickListener(this);
        ll_star_time.setOnClickListener(this);
        ll_stop_time.setOnClickListener(this);
        bt_sure_change_time.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_yingye_time_week://星期replaceAll(" ", "")
                HashMap<String, String> extras = new HashMap<>();
                extras.put("YingyeWeek", weekday.replaceAll(" ", ""));
                if (Valid.isNotNullOrEmpty(businessHours)) {
                    extras.put("business_hours", tv_star_time.getText().toString() + "-" + tv_stop_time.getText().toString());
                } else {
                    extras.put("business_hours", "");
                }
                try {
                    gotoActivityAndFinish(StoreYingyeWeek.class.getName(), extras);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.ll_star_time:
                showTimePickerDialog(this, 3, tv_star_time, c);

                businessHours = tv_star_time.getText().toString() + "-" + tv_stop_time.getText().toString();
                break;
            case R.id.ll_stop_time:
                showTimePickerDialog(this, 3, tv_stop_time, c);
                businessHours = tv_star_time.getText().toString() + "-" + tv_stop_time.getText().toString();
                break;
            case R.id.bt_sure_change_time://修改工作时间
                long star_time = getStringToDate(tv_star_time.getText().toString(), "HH:mm");
                long stop_time = getStringToDate(tv_stop_time.getText().toString(), "HH:mm");
//                Log.i("sssss", tv_star_time.getText().toString() + "-" + tv_stop_time.getText().toString());
                if (Valid.isNotNullOrEmpty(tv_star_time.getText().toString()) && Valid.isNotNullOrEmpty(tv_stop_time.getText().toString())) {
                    if (star_time < stop_time) {
                        netService.updBusinesstime(GetstoreId(), weekday.replaceAll(" ", ""), tv_star_time.getText().toString() + "-" + tv_stop_time.getText().toString(), updBusinesstime);
                    } else {
                        showTip("营业开始时间不得晚于营业结束时间", Toast.LENGTH_SHORT);
                    }
                } else {
                    showTip("请选择营业时间", Toast.LENGTH_SHORT);
                }
                break;
            default:

                break;
        }
    }

    //、将字符串转为时间戳
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
//            Log.i("sssss", tv_star_time.getText().toString() + "-" + tv_stop_time.getText().toString());
            bundle.putSerializable("business_hours", tv_star_time.getText().toString() + "-" + tv_stop_time.getText().toString());
            bundle.putSerializable("YingyeWeek", weekday.replaceAll(" ", ""));
            // 将时间缓存起来，返回时拿到的是缓存数据
            ACache aCache = ACache.get(StoreYingyeTime.this);
            aCache.put("business_hours", tv_star_time.getText().toString() + "-" + tv_stop_time.getText().toString());
            aCache.put("YingyeWeek", weekday.replaceAll(" ", ""));
            data.putExtras(bundle);
            setResult(2, data);
            finish();
        }

    };
    @SuppressLint("HandlerLeak")
    Handler updBusinesstime = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String date = jsonObject.getString("data");
                        JSONObject status = new JSONObject(jsonObject.getString("status"));
                        String mesg = status.getString("message");
                        String code = status.getString("code");
                        Toast.makeText(StoreYingyeTime.this, mesg, Toast.LENGTH_SHORT).show();
                        if (code.equals("200")) {
                            if (Valid.isNotNullOrEmpty(date)) {
                                handler.sendEmptyMessage(2);
                            }
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2001:

                    break;
                case 1001:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 时间选择
     *
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static String showTimePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog(activity, themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay < 10) {
                            tv.setText("0" + hourOfDay + ":" + minute + "");
                        }
                        if (minute < 10) {
                            tv.setText(hourOfDay + ":" + "0" + minute);
                        }
                        if (minute < 10 && hourOfDay < 10) {
                            tv.setText("0" + hourOfDay + ":" + "0" + minute);
                        }
                        if (minute >= 10 && hourOfDay >= 10) {
                            tv.setText(hourOfDay + ":" + minute);
                        }
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
        return tv.getText().toString();
    }
}
