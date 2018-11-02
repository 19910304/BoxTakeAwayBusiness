package com.xinzuokeji.boxtakeawaybusiness.storeOperations.advertisement;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

public class AvertisementPay extends BaseActivity {
    private ImageButton header_back;
    TextView tv_pay_money_ad;
    CheckBox ck_weixin, ck_zfb, ck_balance;
    Button bt_pay_ad;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_avertisement_pay);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.back_bl);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("支付");
        tv_pay_money_ad = findViewById(R.id.tv_pay_money_ad);
        tv_pay_money_ad.setText("33");//支付金额
        ck_weixin = findViewById(R.id.ck_weixin);
        ck_weixin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        ck_zfb = findViewById(R.id.ck_zfb);
        ck_zfb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        ck_balance = findViewById(R.id.ck_balance);
        ck_balance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
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
            case R.id.bt_pay_ad:
                //  支付
                break;
            default:
                break;
        }
    }
}
