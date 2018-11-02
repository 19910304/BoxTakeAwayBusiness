package com.xinzuokeji.boxtakeawaybusiness.orderManagement;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

/**
 * Created by Administrator on 2018/8/9.
 */
//退款成功
public class RefundSuccess extends BaseActivity {
    private ImageButton header_back;
    Button bt_refund_success;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_refund_succsee);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("退款成功");
        bt_refund_success = findViewById(R.id.bt_refund_success);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_refund_success.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.bt_refund_success://完成后应该到已取消的订单状态
                finish();
                break;
            default:
                break;
        }
    }
}
