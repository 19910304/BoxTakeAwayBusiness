package com.xinzuokeji.boxtakeawaybusiness.me;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.LoginInfo;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;

public class MeAccount extends BaseActivity {
    private ImageButton header_back;
    LinearLayout ll_change_password, ll_banding_phone;
    TextView tv_account_name, tv_band_phone, tv_phone_model;
    NetService netService;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_me_account);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("我的账户");
        ll_change_password = findViewById(R.id.ll_change_password);
        ll_banding_phone = findViewById(R.id.ll_banding_phone);
        tv_account_name = findViewById(R.id.tv_account_name);
        tv_band_phone = findViewById(R.id.tv_band_phone);
        tv_phone_model = findViewById(R.id.tv_phone_model);
//        账户名
        tv_phone_model.setText(moble());
        netService.showMyAccount(getuserinfoId(), showMyAccount);
    }

    @Override
    public void initEvent() {
        super.initEvent();

        header_back.setOnClickListener(this);
        ll_banding_phone.setOnClickListener(this);
        ll_change_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_change_password:
                try {
                    gotoActivity(ChangePassword.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_banding_phone:
                Intent intent = new Intent(this, BingingPhone.class);
                startActivityForResult(intent, 0);

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
                    tv_band_phone.setText(data.getStringExtra("bind_phone"));
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("HandlerLeak")
    Handler showMyAccount = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        com.xinzuokeji.boxtakeawaybusiness.entities.LoginInfo loginInfo = (LoginInfo) msg.obj;
                        tv_account_name.setText(loginInfo.account_name);
                        tv_band_phone.setText(loginInfo.phone);

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
