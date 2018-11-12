package com.xinzuo.qiaobashangjia.me;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuo.qiaobashangjia.BaseActivity;
import com.xinzuo.qiaobashangjia.R;
import com.xinzuo.qiaobashangjia.netService.NetService;
import com.xinzuo.qiaobashangjia.util.Valid;

//修改密码
public class ChangePassword extends BaseActivity {
    private ImageButton header_back;
    EditText et_password_new;
    EditText et_password_old, et_password_sec;
    EditText et_reset_code;
    Button btn_reset_code;
    Button btn_reset_confirm;
    String password_new = "", password_old = "", password_sec = "";
    String password_confirm;
    String code;
    NetService netService;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_change_password);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("修改密码");
        et_password_old = findViewById(R.id.et_password_old);
        et_password_new = findViewById(R.id.et_password_new);
        et_password_sec = findViewById(R.id.et_password_sec);
        btn_reset_confirm = findViewById(R.id.btn_reset_confirm);
        netService = new NetService(this);

    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
//        btn_reset_code.setOnClickListener(this);
        btn_reset_confirm.setOnClickListener(this);
        et_password_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password_new = et_password_new.getText().toString().trim();
            }
        });
        et_password_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password_old = et_password_old.getText().toString().trim();
            }
        });
        et_password_sec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password_sec = et_password_sec.getText().toString().trim();
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

            case R.id.btn_reset_confirm:
                // 确认密码
                if (Valid.validPassword(password_new) && Valid.validPassword(password_sec)) {
                    if (password_new.equals(password_sec)) {
                        netService.updPwd(getuserinfoId(), password_old, password_new, updPwd);
                    } else {
                        showTip("两次密码不一致哦", Toast.LENGTH_SHORT);
                    }
                } else {
                    showTip("密码必须为数字或字母，长度6-22位", Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler updPwd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    }
                    finish();
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
