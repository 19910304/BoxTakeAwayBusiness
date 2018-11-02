package com.xinzuokeji.boxtakeawaybusiness.me.SoreSettings;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

//修改电话
public class StoreChangePhone extends BaseActivity {
    private LinearLayout ll_header_back;
    EditText et_change_phone;
    String changePhone = "";
    TextView tv_save;
    NetService netService;
    private String shopPhone;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_store_change_phone);
        ll_header_back = findViewById(R.id.ll_header_back);
        shopPhone = getIntent().getStringExtra("storePhone");
        tv_save = findViewById(R.id.tv_save);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("修改餐厅电话");
        et_change_phone = findViewById(R.id.et_change_phone);
        //设置电话号码
        if (Valid.isNotNullOrEmpty(shopPhone)) {
            et_change_phone.setText(shopPhone);
            changePhone = shopPhone;
        }
        et_change_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changePhone = et_change_phone.getText().toString().intern();
            }
        });
        netService = new NetService(this);
    }

    @Override
    public void initEvent() {
        super.initEvent();

        ll_header_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
    }

    // 监听返回按键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            //绑定返回按键和返回按钮
            LinearLayout header_back = findViewById(R.id.ll_header_back);
            if (header_back != null) {
                header_back.performClick();
            }

            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_header_back:
                finish();

                break;
            case R.id.tv_save://保存电话
                if (Valid.isPhone(changePhone)) {
                    netService.updShopphone(GetstoreId(), changePhone, updShopphone);
                } else {
                    Toast.makeText(this, "请正确输入电话号码", Toast.LENGTH_SHORT).show();
                }
                break;
            default:

                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler updShopphone = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String date = jsonObject.getString("data");
                        JSONObject status = new JSONObject(jsonObject.getString("status"));
                        String mesg = status.getString("message");
                        Toast.makeText(StoreChangePhone.this, mesg, Toast.LENGTH_SHORT).show();
                        if (date.equals("1")) {
                            suerphone(changePhone);
                            finish();
//                            try {
//                                gotoActivityAndFinish(StoreSettingNew.class.getName(), null);
//                            } catch (ClassNotFoundException e) {
//                                e.printStackTrace();
//                            }
                        } else {

                        }

                    } catch (JSONException e) {
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
