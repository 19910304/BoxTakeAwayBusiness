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

//注册完成
public class RegisterTwo extends BaseActivity {
    private ImageButton header_back;
    EditText et_phone_code;
    String phone_code = "";
    Button bt_sure, bt_delete;
    private NetService netService;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_register_two);
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
        bt_sure = findViewById(R.id.bt_sure);
        bt_delete = findViewById(R.id.bt_delete);
        et_phone_code = findViewById(R.id.et_phone_code);
        et_phone_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Valid.isNotNullOrEmpty(et_phone_code.getText().toString())) {
                    bt_delete.setVisibility(View.VISIBLE);
                } else {
                    bt_delete.setVisibility(View.GONE);
                }
                phone_code = et_phone_code.getText().toString().trim();
//                if (Valid.isPhone(phone_code)) {
//                    bt_sure.setBackgroundResource(R.drawable.set_up_shop_selector);
//                    bt_sure.setTextColor(getResources().getColor(R.color.white));
//                } else {
//                    bt_sure.setBackgroundResource(R.drawable.register_selector);
//                    bt_sure.setTextColor(getResources().getColor(R.color.gray_text));
//                }
            }
        });

    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_sure.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
//                finish();
                try {
                    gotoActivityAndFinish(RegisterOne.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_sure:
                if (Valid.isNotNullOrEmpty(phone_code)) {
                    // 需要验证code是否是正确的
                    netService.checkCode(phone_code, checkCode);

                } else {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_delete:
                et_phone_code.setText("");
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler checkCode = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        gotoActivityAndFinish(SetPassWord.class.getName(), null);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2001:

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
