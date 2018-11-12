package com.xinzuo.qiaobashangjia.me.SoreSettings;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xinzuo.qiaobashangjia.BaseActivity;
import com.xinzuo.qiaobashangjia.R;

//商家协议
public class ShopAgreement extends BaseActivity {
    private ImageButton header_back;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_shop_agreement);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("已签署协议");

    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;

            default:

                break;
        }
    }
}
