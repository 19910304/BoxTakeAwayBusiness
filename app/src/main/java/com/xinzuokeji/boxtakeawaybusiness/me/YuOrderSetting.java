package com.xinzuokeji.boxtakeawaybusiness.me;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.ShowPreOrderSet;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;

//预订单设置
public class YuOrderSetting extends BaseActivity {
    private ImageButton header_back;
    LinearLayout ll_yu_order_time, ll_yu_order_warn_time;
    TextView tv_yuding_time, tv_yuding_warn_time;
    Switch sw_stop_yu_order;
    NetService netService;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_yu_order_setting);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("预订单设置");

        ll_yu_order_time = findViewById(R.id.ll_yu_order_time);
        ll_yu_order_warn_time = findViewById(R.id.ll_yu_order_warn_time);
        tv_yuding_time = findViewById(R.id.tv_yuding_time);
        tv_yuding_warn_time = findViewById(R.id.tv_yuding_warn_time);
        sw_stop_yu_order = findViewById(R.id.sw_stop_yu_order);
        // 获取订单设置信息
        netService.showPreOrderSet(GetstoreId(), Hander_showPreOrderSet);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_yu_order_time.setOnClickListener(this);
        ll_yu_order_warn_time.setOnClickListener(this);
        sw_stop_yu_order.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.sw_stop_yu_order://修改商家是否接收预订单
                netService.updatePreOrderSet(GetstoreId(), Hander_updatePreOrderSet);
                break;
            case R.id.ll_yu_order_time://预订单时间

                Intent intent = new Intent(this, YuOrderTime.class);
                intent.putExtra("yuding_time", tv_yuding_time.getText().toString());
                startActivityForResult(intent, 0);
                break;
            case R.id.ll_yu_order_warn_time://预订单提醒时间
                Intent intentWarnTime = new Intent(this, YuOrderWarnTime.class);
                intentWarnTime.putExtra("yuding_warn_time", tv_yuding_warn_time.getText().toString());
                startActivityForResult(intentWarnTime, 1);

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data != null) {
                    tv_yuding_time.setText(data.getStringExtra("yuding_time"));

                }
                break;
            case 1:
                if (data != null) {
                    tv_yuding_warn_time.setText(data.getStringExtra("yuding_warn_time"));

                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("HandlerLeak")
    Handler Hander_showPreOrderSet = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        //返回参数有问题，只有open_preorder；
                        ShowPreOrderSet preOrderSet = (ShowPreOrderSet) msg.obj;
                        tv_yuding_time.setText(preOrderSet.accept_date);//预定日趋
                        tv_yuding_warn_time.setText(preOrderSet.remind_time);//提醒时间
                        //   是否停止营业时接收预订单 1:接受 2:不接受
                        if (preOrderSet.open_preorder.equals("1")) {
                            sw_stop_yu_order.setChecked(true);
                        } else {
                            sw_stop_yu_order.setChecked(false);
                        }
                    }
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler Hander_updatePreOrderSet = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    // 1接受预订单2，不接受
                    if (msg.obj.toString().equals("1")) {
                        sw_stop_yu_order.setChecked(true);
                        showTip("接受预订单", Toast.LENGTH_SHORT);
                    }
                    if (msg.obj.toString().equals("2")) {
                        sw_stop_yu_order.setChecked(false);
                        showTip("不接受预订单", Toast.LENGTH_SHORT);
                    }

                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
