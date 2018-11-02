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
import com.xinzuokeji.boxtakeawaybusiness.entities.BusinessNotice;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

//店铺公告
public class ShopAnnouncement extends BaseActivity {
    private LinearLayout ll_header_back;

    String shop_announcement = "";
    EditText et_shop_announcement;
    NetService netService;
    TextView tv_save;
    private String shop_type;
    //    进度显示
    LoadingDialog.Builder loadBuilder;
    LoadingDialog dialog = null;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_shop_announcement);
//        接受类型
        shop_type = getIntent().getStringExtra("shop_type");
        ll_header_back = findViewById(R.id.ll_header_back);
        tv_save = findViewById(R.id.tv_save);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        et_shop_announcement = findViewById(R.id.et_shop_announcement);
        if (shop_type.equals("店铺公告")) {
            textView.setText("店铺公告");
            et_shop_announcement.setHint("请输入公告信息");
        }
        if (shop_type.equals("店铺简介")) {
            textView.setText("店铺简介");
            et_shop_announcement.setHint("请输入简介信息");
        }


        et_shop_announcement.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                shop_announcement = et_shop_announcement.getText().toString().intern();
            }
        });
        netService = new NetService(this);
        if (shop_type.equals("店铺公告")) {
            netService.showBusinessNotice(GetstoreId(), hander_showBusinessNotice);
        }
        if (shop_type.equals("店铺简介")) {
            netService.showShopinfo(GetstoreId(), hander_showShopinfo);
        }

    }

    @Override
    public void initEvent() {
        super.initEvent();
        ll_header_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_header_back:
                finish();
                break;
            case R.id.tv_save:
                loadBuilder = new LoadingDialog.Builder(this)
                        .setMessage("")
                        .setCancelable(true)
                        .setCancelOutside(true);
                dialog = loadBuilder.create();
                dialog.show();
                if (shop_type.equals("店铺公告")) {
                    netService.updBusinessNotice(GetstoreId(), shop_announcement, updBusinessNotice);//调用公告信息
                }
                if (shop_type.equals("店铺简介")) {
                    netService.updShopinfo(GetstoreId(), shop_announcement, updBusinessNotice);//调用简介信息
                }
                break;
            default:

                break;
        }
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

    //店铺公告
    @SuppressLint("HandlerLeak")
    Handler hander_showBusinessNotice = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    BusinessNotice businessNotice = (BusinessNotice) msg.obj;
//                    店铺公告
                    et_shop_announcement.setText(businessNotice.notice);
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
    // 店铺简介
    @SuppressLint("HandlerLeak")
    Handler hander_showShopinfo = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    BusinessNotice businessNotice = (BusinessNotice) msg.obj;
                    //  店铺简介
                    et_shop_announcement.setText(businessNotice.shop_info);
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
    String mesg;
    @SuppressLint("HandlerLeak")
    Handler updBusinessNotice = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String date = jsonObject.getString("data");
                        JSONObject status = new JSONObject(jsonObject.getString("status"));
                        mesg = status.getString("message");
                        String code = status.getString("code");
                        if (code.equals("200")) {
                            finish();
                        }
                        Toast.makeText(ShopAnnouncement.this, mesg, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2001:
                    Toast.makeText(ShopAnnouncement.this, mesg, Toast.LENGTH_SHORT).show();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
                case 1001:
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
