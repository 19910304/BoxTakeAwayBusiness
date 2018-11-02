package com.xinzuokeji.boxtakeawaybusiness.storeOperations.advertisement;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

public class AvertisementDe extends BaseActivity {
    private ImageButton header_back;
    Button bt_pay_ad;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_avertisement_de);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.back_bl);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("首页轮播banner");//可变
        bt_pay_ad = findViewById(R.id.bt_pay_ad);

    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_pay_ad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.bt_pay_ad://跳转支付
                try {
                    gotoActivity(AvertisementPay.class.getName(),null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
