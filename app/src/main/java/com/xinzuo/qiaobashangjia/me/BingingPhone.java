package com.xinzuo.qiaobashangjia.me;

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

import com.xinzuo.qiaobashangjia.BaseActivity;
import com.xinzuo.qiaobashangjia.R;
import com.xinzuo.qiaobashangjia.netService.NetService;
import com.xinzuo.qiaobashangjia.util.CountDownUtil;
import com.xinzuo.qiaobashangjia.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

//修改绑定手机号
public class BingingPhone extends BaseActivity {
    private ImageButton header_back;
    //    EditText et_password_new;
    EditText et_phone_number, et_code;

    Button btn_reset_confirm;
    TextView bt_code;
    String phone_number = "", code = "";
    private NetService netService;
    CountDownUtil countDownUtil;
    private TextView tv_tip;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_binging_phone);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("修改绑定手机号");
        et_phone_number = findViewById(R.id.et_phone_number);
        et_code = findViewById(R.id.et_code);
        bt_code = findViewById(R.id.bt_code);
        btn_reset_confirm = findViewById(R.id.btn_reset_confirm);
        //提示
        tv_tip = findViewById(R.id.tv_tip);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_code.setOnClickListener(this);
        btn_reset_confirm.setOnClickListener(this);
        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phone_number = et_phone_number.getText().toString().trim();
            }
        });
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code = et_code.getText().toString().trim();
            }
        });


    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.bt_code:
                if (Valid.isPhone(phone_number)) {
                    netService.getSendsms(phone_number, String.valueOf(3), getSendsms);
                    countDownUtil = new CountDownUtil(bt_code);
                    countDownUtil.setCountDownMillis(60_000L);//倒计时60000ms
                    countDownUtil.setCountDownColor(android.R.color.holo_blue_light, android.R.color.darker_gray);//不同状态字体颜色
                    countDownUtil.start();
                    countDownUtil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            netService.getSendsms(phone_number, String.valueOf(3), getSendsms);
                            Log.i("codeactivity", "发送成功");


                        }
                    });
                } else {
                    Toast.makeText(BingingPhone.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btn_reset_confirm://先确认旧手机再绑定新手机
                if (Valid.isPhone(phone_number) && Valid.isNotNullOrEmpty(code)) {
                    if (btn_reset_confirm.getText().toString().equals("下一步")) {
                        netService.checkOldPhone(getuserinfoId(), phone_number, Integer.parseInt(code), hander_checkOldPhone);
                    }
                    if (btn_reset_confirm.getText().toString().equals("确认修改")) {
                        netService.checkNewPhone(getuserinfoId(), phone_number, Integer.parseInt(code), hander_checkNewPhone);
                    }
                } else {
                    showTip("请正确输入验证码", Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("bind_phone", phone_number);
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
            finish();
        }

    };
    String message = "";
    @SuppressLint("HandlerLeak")
    Handler getSendsms = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(msg.obj.toString());
                        String data = jsonObject.getString("data");
                        message = jsonObject.getJSONObject("status").getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showTip(message, Toast.LENGTH_SHORT);
                    break;
                case 2001:
                    showTip(message, Toast.LENGTH_SHORT);
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
    Handler hander_checkOldPhone = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    et_phone_number.setText("");
                    et_phone_number.setHint("请输入新手机号码");
                    et_code.setText("");
                    tv_tip.setVisibility(View.GONE);
                    btn_reset_confirm.setText("确认修改");
                    countDownUtil.reset();
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
    @SuppressLint("HandlerLeak")
    Handler hander_checkNewPhone = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    JSONObject object = null;
                    try {
                        object = new JSONObject(msg.obj.toString());
                        String data = object.getString("data");

                        phone_number = data;
                        JSONObject status = object.getJSONObject("status");
                        showTip(status.getString("message"), Toast.LENGTH_SHORT);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    handler.sendEmptyMessage(0);

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
