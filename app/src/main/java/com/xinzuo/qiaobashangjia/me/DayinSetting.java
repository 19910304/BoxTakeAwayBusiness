package com.xinzuo.qiaobashangjia.me;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.xinzuo.qiaobashangjia.BaseActivity;
import com.xinzuo.qiaobashangjia.R;

//打印设置
public class DayinSetting extends BaseActivity {
    private ImageButton header_back;
    LinearLayout ll_lanya_dayin, ll_GPRS_dayin;
    Switch aSwitch;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_dayin);

        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("打印设置");
        ll_lanya_dayin = findViewById(R.id.ll_lanya_dayin);
        ll_GPRS_dayin = findViewById(R.id.ll_GPRS_dayin);
        aSwitch = findViewById(R.id.sw_xiaopiao);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_GPRS_dayin.setOnClickListener(this);
        ll_lanya_dayin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_lanya_dayin://蓝牙打印
                try {
                    gotoActivity(LanyaDayin.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_GPRS_dayin://GPRS打印
                try {
                    gotoActivity(GPRSDayin.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
