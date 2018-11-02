package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

//商家注册
public class RegisterOne extends BaseActivity {
    private ImageButton header_back;
    EditText et_phone;
    String phone = "";
    Button bt_next_step, bt_delete;
    TextView tv_account_login;
    private NetService netService;
    public static Activity mActivity;

    @Override
    public void initView() {
        super.initView();
        mActivity = this;
        setContentView(R.layout.activity_register_one);
        netService = new NetService(this);
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("商家注册");
        textView.setTextColor(getResources().getColor(R.color.white));
        et_phone = findViewById(R.id.et_phone);

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                if (Valid.isNotNullOrEmpty(et_phone.getText().toString())) {
                    bt_delete.setVisibility(View.VISIBLE);
                } else {
                    bt_delete.setVisibility(View.GONE);
                }
                phone = et_phone.getText().toString().trim();

            }
        });
        bt_next_step = findViewById(R.id.bt_next_step);
        bt_delete = findViewById(R.id.bt_delete);
        tv_account_login = findViewById(R.id.tv_account_login);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_next_step.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
        tv_account_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                // 到密码登录
                try {
                    gotoActivityAndFinish(PassWordLogin.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_next_step://下一步
                if (Valid.isPhone(phone) && Valid.isNotNullOrEmpty(phone)) {
                    //获取验证码
                    netService.getSendsms(phone, "1", getSendsms);

                } else {
                    Toast.makeText(this, "请正确输入手机号", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.bt_delete://清空手机号
                et_phone.setText("");
                break;
            case R.id.tv_account_login://
                try {
                    gotoActivityAndFinish(PassWordLogin.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    String message = "";
    @SuppressLint("HandlerLeak")
    Handler getSendsms = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    String data = "";
                    JSONObject object = new JSONObject();
                    // 将手机存到本地
                    suerphone(phone);
                    try {
                        try {
                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
                            data = jsonObject.getString("data");
                            object = jsonObject.getJSONObject("status");
                            message = jsonObject.getJSONObject("status").getString("message");
                            showTip(message, Toast.LENGTH_SHORT);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (data.equals("1")) {
                            //data为1已经注册，不发短信，不跳转
                        } else {
                            if (object.getString("code").equals("200")) {
                                gotoActivityAndFinish(RegisterTwo.class.getName(), null);
                            }
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2001:
                    showTip(message, Toast.LENGTH_SHORT);
                    break;
                case 1001:
                    showTip(getString(R.string.network_error), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
