package com.xinzuokeji.boxtakeawaybusiness.orderManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.RatingBarNew;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

/**
 * Created by Administrator on 2018/8/2.
 */
//评价骑手
public class EvaluateRider extends BaseActivity {
    private ImageButton header_back;
    private com.xinzuokeji.boxtakeawaybusiness.util.RatingBarNew star;
    private EditText ed_content;
    private Button bt_evaluation_rider;
    private NetService netService;
    private int fint;
    private String order_main_id, rid;
    String date;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_evaluae_rider);
        //  获取订单id和骑手id
        order_main_id = getIntent().getStringExtra("order_main_id");
        rid = getIntent().getStringExtra("rid");
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("提交评价");
        star = findViewById(R.id.star);
        star.setClickable(true);
        //  点击获取星数
        star.setOnRatingChangeListener(new RatingBarNew.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                Log.i("star", String.valueOf(ratingCount));
                fint = (int) ratingCount;
            }
        });
        ed_content = findViewById(R.id.ed_content);
        ed_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ed_content.getText().toString().trim();
            }
        });
//        提交
        bt_evaluation_rider = findViewById(R.id.bt_evaluation_rider);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_evaluation_rider.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.bt_evaluation_rider://提交评论
                if (Valid.isNotNullOrEmpty(ed_content.getText().toString().trim()) && fint != 0) {
                    netService.JudgeRider(GetstoreId(), Integer.valueOf(rid), Integer.valueOf(order_main_id), fint, ed_content.getText().toString().trim(), showJudgeRider);
                } else {
                    showTip("请评价骑手", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            Log.i("date", date + "---");
            bundle.putSerializable("JudgeRider", date);
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
            finish();
        }

    };
    @SuppressLint("HandlerLeak")
    Handler showJudgeRider = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    date = msg.obj.toString();
                    handler.sendEmptyMessage(0);
                    showTip("评价成功", Toast.LENGTH_SHORT);
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
