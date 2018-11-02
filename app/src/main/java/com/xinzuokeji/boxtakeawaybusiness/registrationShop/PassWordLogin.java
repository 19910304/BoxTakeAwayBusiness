package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.alibaba.sdk.android.push.CommonCallback;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.MainActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.UserInfo;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.netutil.JSONDeserialize;
import com.xinzuokeji.boxtakeawaybusiness.util.ACache;
import com.xinzuokeji.boxtakeawaybusiness.util.AppUtil;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.xinzuokeji.boxtakeawaybusiness.GSApplication.pushService;

public class PassWordLogin extends BaseActivity {
    private ImageButton header_back;
    private EditText et_account_number, et_password;
    String accountNumber = "", password = "";
    private Button btDeleteAccount, btDeletePassword, bt_login_sure;
    private TextView tv_forget_password, tv_code_login_new;
    NetService netService;
    //   注册
    private TextView register;
    private List<UserInfo> mDate;
    AppUtil appUtil;
    //缓存对象
    ACache aCache;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_password_login);
        aCache = ACache.get(this);
        mDate = new ArrayList<>();
        appUtil = new AppUtil(PassWordLogin.this);
        //  头信息
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.INVISIBLE);
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.header_title);
        //注册
        register = findViewById(R.id.header_edit1);
        register.setVisibility(View.VISIBLE);
        register.setTextColor(getResources().getColor(R.color.white));
        register.setText("注册");
        textView.setText("商家登录");
        textView.setTextColor(getResources().getColor(R.color.white));
        et_account_number = findViewById(R.id.et_account_number);//账户
        btDeleteAccount = findViewById(R.id.bt_delete);//清空
        // 从缓存获取用户名，没有则不显示
        String loginPhone = aCache.getAsString("loginPhone");
        if (Valid.isNotNullOrEmpty(loginPhone)) {
            et_account_number.setText(loginPhone);
            accountNumber = et_account_number.getText().toString().trim();
            // 清空按钮
            if (Valid.isNotNullOrEmpty(accountNumber)) {
                btDeleteAccount.setVisibility(View.VISIBLE);
            } else {
                btDeleteAccount.setVisibility(View.GONE);
            }
        }

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
                // 清空按钮
                if (Valid.isNotNullOrEmpty(accountNumber)) {
                    btDeleteAccount.setVisibility(View.VISIBLE);
                } else {
                    btDeleteAccount.setVisibility(View.GONE);
                }

            }
        });

        et_password = findViewById(R.id.et_password);//密码
        btDeletePassword = findViewById(R.id.bt_delete1);
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
                // 清空按钮
                if (Valid.isNotNullOrEmpty(password)) {
                    btDeletePassword.setVisibility(View.VISIBLE);
                } else {
                    btDeletePassword.setVisibility(View.GONE);
                }
            }
        });
        bt_login_sure = findViewById(R.id.bt_login_sure);
        //忘记密码和验证码登录
        tv_forget_password = findViewById(R.id.tv_forget_password);
        tv_code_login_new = findViewById(R.id.tv_code_login_new);
        netService = new NetService(this);
        getpermission();
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        btDeleteAccount.setOnClickListener(this);
        btDeletePassword.setOnClickListener(this);
        bt_login_sure.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        tv_code_login_new.setOnClickListener(this);
        // 注册
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
//                try {
//                    gotoActivityAndFinish(RegisterOne.class.getName(), null);
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
                break;
            case R.id.bt_delete://删除账户
                et_account_number.setText("");
                break;
            case R.id.bt_delete1://删除密码
                et_password.setText("");
                break;
            case R.id.bt_login_sure://登录
//                netService.updPwd(getuserinfoId(), "0", "123456", login_handler);
                if (Valid.isPhone(accountNumber) && Valid.isNotNullOrEmpty(password)) {
                    bt_login_sure.setClickable(false);
                    netService.Login(accountNumber, password, moble(), appUtil.getUniqueID(), loginHandler);
                } else {
                    Toast.makeText(this, "请正确输入账户和密码", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_forget_password://忘记密码
                HashMap<String, String> extras = new HashMap<>();
                extras.put("name_type", "忘记密码");
                try {
                    gotoActivityAndFinish(CodeLogin.class.getName(), extras);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_code_login_new://验证码登录
                HashMap<String, String> extrasCode = new HashMap<>();
                extrasCode.put("name_type", "验证码登录");
                try {
                    gotoActivityAndFinish(CodeLogin.class.getName(), extrasCode);
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

    private void bindAccount(final String user_phone) {
        if (Valid.isNotNullOrEmpty(user_phone)) {
            pushService.bindAccount(user_phone, new CommonCallback() {
                @Override
                public void onSuccess(String s) {
//                    Log.i("eeee", s + user_phone + pushService.getDeviceId());
                }

                @Override
                public void onFailed(String s, String s1) {
//                    bindAccount(appUtil.getUniqueID());
                }
            });
        }
    }

    public void getpermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PassWordLogin.this,
                    Manifest.permission.READ_CONTACTS)) {
                //在这里提醒用户你要使用权限意图
                showTip("打开手机相关权限", Toast.LENGTH_SHORT);
            } else {
                //请求权限
                ActivityCompat.requestPermissions(PassWordLogin.this, new String[]{
                                Manifest.permission.READ_CONTACTS}
                        , 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length > 0) {
            switch (requestCode) {
                case 1:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //权限被允许
                        // 小米手机添加
                        int checkOp = 0;
                        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (appOpsManager != null) {
                                checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_WRITE_CONTACTS,
                                        Process.myUid(), getPackageName());
                            }
                        }
                        if (checkOp == AppOpsManager.MODE_IGNORED) {
                            //在这里提醒用户你要使用权限意图
                            showTip("打开手机相关权限", Toast.LENGTH_SHORT);
                        }

                    } else {
                        // 权限被拒绝
                        Toast.makeText(PassWordLogin.this, "权限被拒绝！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private final LoginHandler loginHandler = new LoginHandler(this);

    private static class LoginHandler extends Handler {
        private final WeakReference<PassWordLogin> mActivity;

        public LoginHandler(PassWordLogin activity) {
            mActivity = new WeakReference<PassWordLogin>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final PassWordLogin activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 2000:
                        activity.mDate.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
                            String data = jsonObject.getString("data");
                            JSONObject object = jsonObject.getJSONObject("status");
                            String code = object.getString("code");
                            String message = object.getString("message");
                            if (data.equals("")) {
                                activity.showTip(message, Toast.LENGTH_SHORT);
                            } else {
                                if (code.equals("200")) {
                                    //将账户缓存本地
                                    activity.aCache.put("loginPhone", activity.accountNumber);
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(data);
                                        String bid = jsonObject1.getString("bid");
                                        String account_name = jsonObject1.getString("account_name");
                                        String phone = jsonObject1.getString("phone");
                                        activity.suerinfoId(Integer.parseInt(bid));
                                        activity.suerphone(phone);
                                        String shop = jsonObject1.getString("shop");
                                        activity.mDate.addAll(new JSONDeserialize<>(UserInfo.class, shop).toObjects());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (activity.mDate.size() != 0) {
                                        //  date大小不为0时跳转主页，否则跳转申请记录页面
                                        activity.SetstoreId(Integer.parseInt(activity.mDate.get(0).id));
                                        //  阿里云绑定设备号
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                // 35259E736145532612CED678C8256851
//                                            Log.i("uid", appUtil.getUniqueID());
                                                activity.bindAccount(activity.appUtil.getUniqueID());
                                            }
                                        });
                                        activity.showTip(message, Toast.LENGTH_SHORT);
                                        try {
                                            HashMap<String, String> extras = new HashMap<>();
                                            extras.put("class_name", "login");
                                            activity.gotoActivityAndFinish(MainActivity.class.getName(), extras);
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        try {
                                            activity.gotoActivityAndFinish(ApplicationRecord.class.getName(), null);
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 2001:
                        activity.bt_login_sure.setClickable(true);
                        activity.showTip(msg.toString(), Toast.LENGTH_SHORT);
                        break;
                    case 1001:
                        activity.bt_login_sure.setClickable(true);
                        activity.showTip(activity.getString(R.string.network_error), Toast.LENGTH_SHORT);
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);

            }
        }
    }

    //    String objectdateNew = objectdate.replaceAll("[\\[\\]]", "");//去掉中括号
    @SuppressLint("HandlerLeak")
    Handler login_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    mDate.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String data = jsonObject.getString("data");
                        JSONObject object = jsonObject.getJSONObject("status");
                        String code = object.getString("code");
                        String message = object.getString("message");
                        if (data.equals("")) {
                            showTip(message, Toast.LENGTH_SHORT);
                        } else {
                            if (code.equals("200")) {
                                //将账户缓存本地
                                aCache.put("loginPhone", accountNumber);
                                try {
                                    JSONObject jsonObject1 = new JSONObject(data);
                                    String bid = jsonObject1.getString("bid");
                                    String account_name = jsonObject1.getString("account_name");
                                    String phone = jsonObject1.getString("phone");
                                    suerinfoId(Integer.parseInt(bid));
                                    suerphone(phone);
                                    String shop = jsonObject1.getString("shop");
                                    mDate.addAll(new JSONDeserialize<>(UserInfo.class, shop).toObjects());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (mDate.size() != 0) {
                                    //  date大小不为0时跳转主页，否则跳转申请记录页面
                                    SetstoreId(Integer.parseInt(mDate.get(0).id));
                                    //  阿里云绑定设备号
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // 35259E736145532612CED678C8256851
//                                            Log.i("uid", appUtil.getUniqueID());
                                            bindAccount(appUtil.getUniqueID());
                                        }
                                    });
                                    showTip(message, Toast.LENGTH_SHORT);
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
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
                case 2001:
                    showTip(msg.toString(), Toast.LENGTH_SHORT);
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
