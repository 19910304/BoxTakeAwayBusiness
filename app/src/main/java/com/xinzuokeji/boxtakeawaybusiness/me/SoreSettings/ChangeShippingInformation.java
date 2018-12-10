package com.xinzuokeji.boxtakeawaybusiness.me.SoreSettings;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.DisList;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/8/8.
 */
//修改配送范围
public class ChangeShippingInformation extends BaseActivity {
    private ImageButton header_back;
    private EditText et_minimum_delivery_amount, et_shipping_fee;
    private TextView tv_start_time, tv_end_time;
    Calendar calendar = Calendar.getInstance();
    private Button bt_save_Shipping_time;
    private LinearLayout ll_delivery_area;
    private DisList disList;
    private NetService netService;
    private String start_price, dis_money;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_change_shipping_information);
        netService = new NetService(this);
//        接受列表传来的对象
        String addTime = getIntent().getStringExtra("addTime");
        if (addTime.equals("0")) {
            disList = (DisList) getIntent().getSerializableExtra("disList");
        }

        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("修改配送范围");
        et_minimum_delivery_amount = findViewById(R.id.et_minimum_delivery_amount);
        et_minimum_delivery_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                start_price = et_minimum_delivery_amount.getText().toString().trim();
            }
        });
        et_shipping_fee = findViewById(R.id.et_shipping_fee);
        et_shipping_fee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dis_money = et_shipping_fee.getText().toString().trim();
            }
        });
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        bt_save_Shipping_time = findViewById(R.id.bt_save_Shipping_time);
        ll_delivery_area = findViewById(R.id.ll_delivery_area);
        LinearLayout ll_star_time = findViewById(R.id.ll_star_time);
        LinearLayout ll_end_time = findViewById(R.id.ll_end_time);
        //常规时段不能选择时间
        if (disList != null) {
//            if (disList.time_type.equals("1")) {
//                ll_star_time.setVisibility(View.GONE);
//                ll_end_time.setVisibility(View.GONE);
//            } else {
//                ll_star_time.setVisibility(View.VISIBLE);
//                ll_end_time.setVisibility(View.VISIBLE);
//            }
            //显示配送信息
            et_minimum_delivery_amount.setText(disList.start_price);
            et_shipping_fee.setText(disList.dis_money);
            tv_start_time.setText(disList.open);
            tv_end_time.setText(disList.close);
        }

    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        tv_start_time.setOnClickListener(this);
        tv_end_time.setOnClickListener(this);
        bt_save_Shipping_time.setOnClickListener(this);
        ll_delivery_area.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_delivery_area://电子围栏
//                Intent intent = new Intent(this, GeoFence_Polygon_Activity.class);
//                if (Valid.isNotNullOrEmpty(disList.id) && disList != null) {
//                    intent.putExtra("id", disList.id);
//                    intent.putExtra("point_set", (Serializable) disList.point_set);
//                } else {
//                    intent.putExtra("id", 1);
//                    intent.putExtra("point_set", "");
//                }
//                startActivityForResult(intent, REQUESTCODE);
                break;
            case R.id.tv_start_time://开始时间
                showTimePickerDialog(this, 3, tv_start_time, calendar);
                break;
            case R.id.tv_end_time://结束时间
                showTimePickerDialog(this, 3, tv_end_time, calendar);
                break;
            case R.id.bt_save_Shipping_time://保存价格信息
                if (Valid.isNotNullOrEmpty(start_price) && Valid.isNotNullOrEmpty(dis_money)) {
                    netService.updDisPrice(Integer.parseInt(disList.id), Integer.parseInt(disList.shop_id), dis_money, start_price, tv_start_time.getText().toString(), tv_end_time.getText().toString(), Hander_updDisPrice);
                } else {
                    showTip("请将信息填写完整", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler Hander_updDisPrice = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    finish();
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
