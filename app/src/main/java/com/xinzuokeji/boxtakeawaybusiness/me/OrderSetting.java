package com.xinzuokeji.boxtakeawaybusiness.me;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

//订单设置
public class OrderSetting extends BaseActivity {
    private ImageButton header_back;
    LinearLayout ll_yu_order_setting, ll_zidong_order_setting;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_order_setting);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("订单设置");
        ll_yu_order_setting = findViewById(R.id.ll_yu_order_setting);
        ll_zidong_order_setting = findViewById(R.id.ll_zidong_order_setting);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_yu_order_setting.setOnClickListener(this);
        ll_zidong_order_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_yu_order_setting://预订单设置
                try {
                    gotoActivity(YuOrderSetting.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_zidong_order_setting://自动接单设置
                try {
                    gotoActivity(ZiDongOrderSetting.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
