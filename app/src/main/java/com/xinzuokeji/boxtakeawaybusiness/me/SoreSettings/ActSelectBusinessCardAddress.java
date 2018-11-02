package com.xinzuokeji.boxtakeawaybusiness.me.SoreSettings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CommonCallback;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.MainActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.registrationShop.GspLocation;
import com.xinzuokeji.boxtakeawaybusiness.registrationShop.PassWordLogin;
import com.xinzuokeji.boxtakeawaybusiness.util.IosAlertDialog;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

import static com.xinzuokeji.boxtakeawaybusiness.GSApplication.REQUESTCODE;
import static com.xinzuokeji.boxtakeawaybusiness.GSApplication.pushService;


//选择的地址

public class ActSelectBusinessCardAddress extends BaseActivity {
    private LinearLayout ll_header_back;
    TextView tv_save;
    //    请选择 地图 地址
    private TextView text_ditu_xinxi_sad;

    //    确定 的 按钮
    private LinearLayout text_queding_yes;
    //    门牌号
    private EditText text_ditu_xuanze_shuru;
    String latitude_new;
    String longitude_new;
    String city;
    //    选择地址
    private TextView text_daddress;
    private String sheng, shi, qu, jiedao, cityCode, adCode;
    NetService netService;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_store_change_address);
        sheng = getIntent().getStringExtra("sheng");
        shi = getIntent().getStringExtra("shi");
        qu = getIntent().getStringExtra("qu");
        jiedao = getIntent().getStringExtra("address");
        latitude_new = getIntent().getStringExtra("dimension");
        longitude_new = getIntent().getStringExtra("longitude");
        adCode = getIntent().getStringExtra("code");
        netService = new NetService(this);
        ll_header_back = findViewById(R.id.ll_header_back);
        tv_save = findViewById(R.id.tv_save);
        tv_save.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("修改餐厅地址");
        text_ditu_xinxi_sad = (TextView) findViewById(R.id.text_ditu_xinxi_sad);
        //  详细地址
        text_ditu_xuanze_shuru = (EditText) findViewById(R.id.text_ditu_xuanze_shuru);
        text_ditu_xuanze_shuru.setText(jiedao);
        //选择 地址
        text_daddress = (TextView) findViewById(R.id.text_daddress);
        text_daddress.setText(sheng + shi + qu);
        // 确认按钮
        text_queding_yes = (LinearLayout) findViewById(R.id.text_queding_yes);
    }

    @Override
    public void initEvent() {
        text_ditu_xinxi_sad.setOnClickListener(this);
        text_queding_yes.setOnClickListener(this);
        ll_header_back.setOnClickListener(this);
        text_ditu_xuanze_shuru.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                jiedao = text_ditu_xuanze_shuru.getText().toString();
            }
        });
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
        switch (v.getId()) {
            case R.id.ll_header_back:
                finish();
                break;
            case R.id.text_ditu_xinxi_sad:
                Intent intent = new Intent(this, GspLocation.class);
                // 执行跳转
                startActivityForResult(intent, REQUESTCODE);
                //这里采用startActivityForResult来做跳转，
                // 此处的0为一个依据，可以写其他的值，但一定要>=0
                break;
//            确定  执行
            case R.id.text_queding_yes:
                if (getOperatingState().equals("未营业")) {

                    IosAlertDialog alertDialog = new IosAlertDialog(this);
                    alertDialog.builder();
                    alertDialog.setCancelable(true);
                    alertDialog.setMsg("修改后店铺将重新审核");
                    alertDialog.setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    alertDialog.setPositiveButton("", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            netService.updShopaddress(GetstoreId(), sheng, shi, qu, text_ditu_xuanze_shuru.getText().toString(), longitude_new, latitude_new, adCode, Handle_updShopaddress);
                        }
                    });
                    alertDialog.show();
//                    Logout();
                } else {
                    showTip("当前店铺处于营业状态，如需修改请将店铺进行停业", Toast.LENGTH_SHORT);
                }

                break;
            default:
                break;


        }
    }

//    private void Logout() {
//        final AletDialog dialog = new AletDialog(this, "修改店铺地址", "修改后店铺将重新审核", getString(R.string.cancel), getString(R.string.confirm), new AletDialog.OnAlertDialogListener() {
//            @Override
//            public void Confirm() {
//                netService.updShopaddress(GetstoreId(), sheng, shi, qu, text_ditu_xuanze_shuru.getText().toString(), longitude_new, latitude_new, adCode, Handle_updShopaddress);
//            }
//        });
//        dialog.show();
//
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUESTCODE) {
            // 从地图返回
            sheng = data.getStringExtra("sheng");
            shi = data.getStringExtra("shi");
            qu = data.getStringExtra("qu");
            cityCode = data.getStringExtra("CityCode");
            latitude_new = data.getStringExtra("lat");
            longitude_new = data.getStringExtra("lgt");
            adCode = data.getStringExtra("AdCode");
            jiedao = data.getStringExtra("jiedao");
            text_daddress.setText(sheng + shi + qu);
            text_ditu_xuanze_shuru.setText(data.getStringExtra("jiedao"));
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("sheng", sheng);
            bundle.putSerializable("shi", shi);
            bundle.putSerializable("qu", qu);
            bundle.putSerializable("jiedao", jiedao);
            bundle.putSerializable("dimension", latitude_new);
            bundle.putSerializable("longitude", longitude_new);
            bundle.putSerializable("code", adCode);
            data.putExtras(bundle);
            setResult(10, data);
            finish();
        }

    };
    @SuppressLint("HandlerLeak")
    Handler Handle_updShopaddress = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String data = jsonObject.getString("data");
                        JSONObject object = jsonObject.getJSONObject("status");
                        String code = object.getString("code");
                        String message = object.getString("message");
                        showTip(message, Toast.LENGTH_SHORT);
                        //修改成功data为1
                        if (Valid.isNotNullOrEmpty(data)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    netService.signOut(GetstoreId(), Hander_logout);
                                }
                            });
                            // 清除数据
                            SharedPreferences sp = GSApplication.getInstance().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.clear();
                            editor.apply();
                            pushService.unbindAccount(new CommonCallback() {
                                @Override
                                public void onSuccess(String s) {
                                    Log.i("unphone", "unphone");
                                }

                                @Override
                                public void onFailed(String s, String s1) {
                                    Log.i("unphone", "unphone111");
                                }
                            });
                            //销毁baseActivity下的所有activity
                            MainActivity.mActivity.finish();
                            GSApplication.getInstance().removeALLActivity_();
                            Intent intent1 = new Intent(ActSelectBusinessCardAddress.this, PassWordLogin.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent1);
//                            handler.sendEmptyMessage(10);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2001:
                    showTip(msg.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
                    showTip(msg.toString(), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler Hander_logout = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
//                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2001:
//                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
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
