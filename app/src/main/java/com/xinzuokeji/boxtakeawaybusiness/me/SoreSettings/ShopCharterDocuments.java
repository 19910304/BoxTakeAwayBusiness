package com.xinzuokeji.boxtakeawaybusiness.me.SoreSettings;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

import java.util.Calendar;

//特许证件
public class ShopCharterDocuments extends BaseActivity {
    private ImageButton header_back;
    private LinearLayout ll_selector_time;
    private TextView tv_selector_time;
    Calendar calendar = Calendar.getInstance();

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_shop_charter_documents);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("特许证件");
        ll_selector_time = findViewById(R.id.ll_selector_time);//选择时间
        tv_selector_time = findViewById(R.id.tv_selector_time);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_selector_time.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_selector_time://选择年月日
                showDatePickerDialog(this, 3, tv_selector_time, calendar);
                break;
            default:
                break;
        }
    }

//    public static String showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
//        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
//        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
//            // 绑定监听器(How the parent is notified that the date is set.)
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                // 此处得到选择的时间，可以进行你想要的操作
//                tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//
//            }
//        }
//                // 设置初始日期
//                , calendar.get(Calendar.YEAR)
//                , calendar.get(Calendar.MONTH)
//                , calendar.get(Calendar.DAY_OF_MONTH)).show();
//        return tv.getText().toString();
//    }

}
