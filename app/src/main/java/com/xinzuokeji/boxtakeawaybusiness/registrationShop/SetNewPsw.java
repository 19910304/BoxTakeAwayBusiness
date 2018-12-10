package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.annotation.SuppressLint;
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

/**
 * Created by Administrator on 2018/6/30.
 */
//设置新密码
public class SetNewPsw extends BaseActivity {
    private ImageButton header_back;
    EditText et_password_new, et_password_sec;
    Button bt_login_sure, bt_delete_pw_new, bt_delete_pw_sec;
    String passwordNew, passwordSec;
    String phone;
    private NetService netService;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_set_new_psw);
        //接受手机号
        phone = getIntent().getStringExtra("phone");
        netService = new NetService(this);
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("重置密码");
        textView.setTextColor(getResources().getColor(R.color.white));
        et_password_new = findViewById(R.id.et_password_new);//密码
        et_password_sec = findViewById(R.id.et_password_sec);//确认密码
        bt_delete_pw_new = findViewById(R.id.bt_delete_pw_new);//删除密码
        bt_delete_pw_sec = findViewById(R.id.bt_delete_password_sec);//删除确认密码
        et_password_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                passwordNew = et_password_new.getText().toString().trim();
                if (Valid.isNotNullOrEmpty(passwordNew)) {
                    bt_delete_pw_new.setVisibility(View.VISIBLE);
                } else {
                    bt_delete_pw_new.setVisibility(View.GONE);
                }


            }
        });
        et_password_sec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                passwordSec = et_password_sec.getText().toString().trim();
                if (Valid.isNotNullOrEmpty(passwordSec)) {
                    bt_delete_pw_sec.setVisibility(View.VISIBLE);
                } else {
                    bt_delete_pw_sec.setVisibility(View.GONE);
                }
            }
        });
        bt_login_sure = findViewById(R.id.bt_login_sure);

    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_login_sure.setOnClickListener(this);
        bt_delete_pw_sec.setOnClickListener(this);
        bt_delete_pw_new.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.bt_delete_pw_new://清空新密码
                et_password_new.setText("");
                break;
            case R.id.bt_delete_password_sec:
                et_password_sec.setText("");//清空密码
                break;
            case R.id.bt_login_sure:

                if (Valid.validPassword(passwordSec) && Valid.validPassword(passwordNew)) {
                    if (passwordNew.equals(passwordSec)) {
                        netService.backPwd(phone, passwordNew, hander_backPwd);

                    } else {
                        showTip("两次密码输入不一致哦", Toast.LENGTH_SHORT);
                    }

                } else {
                    showTip("密码必须为数字或字母，长度6-22位", Toast.LENGTH_SHORT);
                }

                break;
            default:
                break;
        }
    }

    //修改密码结果
    @SuppressLint("HandlerLeak")
    Handler hander_backPwd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        JSONObject object = jsonObject.getJSONObject("status");
                        String code = object.getString("code");
                        String message = object.getString("message");
                        showTip(message, Toast.LENGTH_SHORT);
                        if (code.equals("200")) {
                            try {
                                gotoActivityAndFinish(PassWordLogin.class.getName(), null);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
