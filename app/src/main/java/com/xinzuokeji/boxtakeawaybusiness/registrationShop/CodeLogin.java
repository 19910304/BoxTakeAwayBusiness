package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CommonCallback;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.MainActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.UserInfo;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.netutil.JSONDeserialize;
import com.xinzuokeji.boxtakeawaybusiness.util.AppUtil;
import com.xinzuokeji.boxtakeawaybusiness.util.CountDownUtil;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.xinzuokeji.boxtakeawaybusiness.GSApplication.pushService;
//忘记密码
public class CodeLogin extends BaseActivity {
    private ImageButton header_back;
    private EditText et_account_number, et_password;
    private String accountNumber = "", password = "";
    private Button btDeleteAccount, bt_login_sure;
    private TextView tv_psw_login, bt_get_code;
    private String name_type;
    private NetService netService;
    private List<UserInfo> mDate;
    //   注册
    private TextView register;
    AppUtil appUtil;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_code_login);
        appUtil = new AppUtil(CodeLogin.this);
        // 获取跳转类型
        name_type = getIntent().getStringExtra("name_type");
        mDate = new ArrayList<>();
        netService = new NetService(this);
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);
        TextView textView = findViewById(R.id.header_title);
        textView.setTextColor(getResources().getColor(R.color.white));
        //注册
        register = findViewById(R.id.header_edit1);
        if (name_type.equals("忘记密码")) {
            textView.setText("忘记密码");
        } else {
            textView.setText("商家登录");
            register.setVisibility(View.VISIBLE);
            register.setTextColor(getResources().getColor(R.color.white));
            register.setText("注册");
        }

        et_account_number = findViewById(R.id.et_account_number);//账户
        btDeleteAccount = findViewById(R.id.bt_delete);
        et_account_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                accountNumber = et_account_number.getText().toString().trim();


            }
        });

        et_password = findViewById(R.id.et_password);//密码
        bt_get_code = findViewById(R.id.bt_get_code);
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = et_password.getText().toString().trim();
                if (Valid.isNotNullOrEmpty(password)) {
                    btDeleteAccount.setVisibility(View.VISIBLE);
                } else {
                    btDeleteAccount.setVisibility(View.GONE);
                }
            }
        });
        bt_login_sure = findViewById(R.id.bt_login_sure);
        tv_psw_login = findViewById(R.id.tv_psw_login);
        if (name_type.equals("忘记密码")) {
            bt_login_sure.setText("下一步");
            bt_login_sure.setTextColor(getResources().getColor(R.color.white));
            bt_login_sure.setBackgroundResource(R.drawable.set_up_shop_selector);
            tv_psw_login.setVisibility(View.GONE);
        } else {
            bt_login_sure.setText("确认");
        }

    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        btDeleteAccount.setOnClickListener(this);
        bt_get_code.setOnClickListener(this);
        bt_login_sure.setOnClickListener(this);
        tv_psw_login.setOnClickListener(this);
        // 注册
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                try {
                    gotoActivityAndFinish(PassWordLogin.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_delete://删除账号
                et_password.setText("");
                break;
            case R.id.bt_get_code://获取验证码
                if (Valid.isPhone(accountNumber)) {
                    String type = "";
                    if (name_type.equals("忘记密码")) {
                        type = "3";
                    } else {
                        type = "2";
                    }
                    netService.getSendsms(accountNumber, type, getSendsms);
                    new CountDownUtil(bt_get_code)
                            .setCountDownMillis(60_000L)//倒计时60000ms
                            .setCountDownColor(android.R.color.holo_blue_light, android.R.color.darker_gray)//不同状态字体颜色
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .start();
                } else {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.bt_login_sure://下一步设置密码accountNumber(手机号)password（验证码）
                if (Valid.isPhone(accountNumber) && Valid.isNotNullOrEmpty(password)) {
                    if (name_type.equals("忘记密码")) {
                        // 验证手机号和验证码
                        netService.checkCodePhone(accountNumber, Integer.parseInt(password), hander_checkPhoneCode);

                    } else {
//                        netService.codeLogin(accountNumber, Integer.parseInt(password), moble(), getIMEI(), codeLogin);
                        // 检测验证码是否正确
                        netService.checkCode(password, hander_checkCode);

                    }

                } else {
                    Toast.makeText(this, "请正确输入账户和验证码", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_psw_login://密码登录
                try {
                    gotoActivityAndFinish(PassWordLogin.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.header_edit1://注册
                try {
                    gotoActivityAndFinish(RegisterOne.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public String getPhoneInfo() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
//                ActivityCompat#requestPermissions
//             here to request the missing permissions, and then overriding
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
//             to handle the case where the user grants the permission. See the documentation
//             for ActivityCompat#requestPermissions for more details.
            return "";
        }
        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
        sb.append("\nLine1Number = " + tm.getLine1Number());
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
        sb.append("\nNetworkType = " + tm.getNetworkType());
        sb.append("\nPhoneType = " + tm.getPhoneType());
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        sb.append("\nSimState = " + tm.getSimState());
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
        return sb.toString();
    }

    String message = "";
    @SuppressLint("HandlerLeak")
    Handler getSendsms = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    String data = "";
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(msg.obj.toString());
                        data = jsonObject.getString("data");
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
    Handler hander_checkCode = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    netService.codeLogin(accountNumber, Integer.parseInt(password), moble(), appUtil.getUniqueID(), codeLogin);
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
    @SuppressLint("HandlerLeak")
    Handler hander_checkPhoneCode = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    JSONObject status = null;
                    try {
                        status = new JSONObject(msg.obj.toString());
                        JSONObject jsonObject = status.getJSONObject("status");
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        showTip(message, Toast.LENGTH_SHORT);
                        if (code.equals("200")) {
                            HashMap<String, String> extras = new HashMap<>();
                            extras.put("phone", accountNumber);
                            //传入手机号设置密码需要
                            try {
                                gotoActivityAndFinish(SetNewPsw.class.getName(), extras);
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
                    showTip(getString(R.string.network_error), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler codeLogin = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    mDate.clear();
                    if (msg.obj.toString().equals("")) {
                        showTip("登录失败", Toast.LENGTH_SHORT);
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
                            String bid = jsonObject.getString("bid");
                            String account_name = jsonObject.getString("account_name");
                            String phone = jsonObject.getString("phone");
                            suerinfoId(Integer.parseInt(bid));
                            suerphone(phone);
                            String shop = jsonObject.getString("shop");
                            mDate.addAll(new JSONDeserialize<>(UserInfo.class, shop).toObjects());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (mDate.size() != 0) {
                            SetstoreId(Integer.parseInt(mDate.get(0).id));
                            //  阿里云绑定手机
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    bindAccount(getsuerphone());
                                    bindAccount(appUtil.getUniqueID());
                                }
                            });
                            showTip("登录成功", Toast.LENGTH_SHORT);
                            try {
                                HashMap<String, String> extras = new HashMap<>();
                                extras.put("class_name", "login");
                                gotoActivityAndFinish(MainActivity.class.getName(), extras);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }

                        } else {
                            try {
                                gotoActivityAndFinish(ApplicationRecord.class.getName(), null);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
//                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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

    private void bindAccount(final String user_phone) {
        if (Valid.isNotNullOrEmpty(user_phone)) {
            pushService.bindAccount(user_phone, new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    Log.i("eeee", s + user_phone + pushService.getDeviceId());
                }

                @Override
                public void onFailed(String s, String s1) {
                    Log.i("eeee", s + user_phone);
                    bindAccount(user_phone);
                }
            });
        }
    }

}
