package com.xinzuokeji.boxtakeawaybusiness.storeOperations.refund;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

//极速退款
public class SpeedRefund extends BaseActivity {
    private ImageButton header_back;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_speed_refund);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.back_bl);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("极速退款");
        Switch swrefund = findViewById(R.id.sw_on_off_refund);
        swrefund.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(SpeedRefund.this, "开启极速退款", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SpeedRefund.this, "关闭极速退款", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
