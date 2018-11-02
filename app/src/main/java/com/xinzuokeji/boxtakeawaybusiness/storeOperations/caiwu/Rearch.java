package com.xinzuokeji.boxtakeawaybusiness.storeOperations.caiwu;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.Constants;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;

//充值
public class Rearch extends BaseActivity {
    private ImageButton header_back;
    CheckBox ck_weixin, ck_zfb;
    Button bt_recharge;
    int temp;
    // 支付宝支付结果标识
    private static final int SDK_PAY_FLAG = 1;
    // 支付宝账号检查标识
    private static final int SDK_CHECK_FLAG = 2;
    private double rearchMoney;
    private NetService netService;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_rearch);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("充值");
        final EditText et_money = findViewById(R.id.et_money);
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //  因为精度问题需要转化成double
                if (Valid.isNotNullOrEmpty(et_money.getText().toString().trim())) {
                    rearchMoney = Double.parseDouble(et_money.getText().toString().trim());
                }
            }
        });
        bt_recharge = findViewById(R.id.bt_recharge);
        ck_weixin = findViewById(R.id.ck_weixin);
        ck_zfb = findViewById(R.id.ck_zfb);
        ck_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_weixin.setChecked(true);
                ck_zfb.setChecked(false);
            }
        });
        ck_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_weixin.setChecked(false);
                ck_zfb.setChecked(true);
            }
        });


    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_recharge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.bt_recharge://充值
                if (Valid.isNotNullOrEmpty(String.valueOf(rearchMoney))) {
                    if (ck_weixin.isChecked()) {
                        netService.wxPay(orderNumber(), (int) (rearchMoney * 100), getLocalIpAddress(), handlerServiceChargedeweiixn);
                    }
                    if (ck_zfb.isChecked()) {
//                    onPayClick("app_id=2017031306200829&method=alipay.trade.app.pay&format=JSON&charset=utf-8&sign_type=RSA2&version=1.0&return_url=&notify_url=&timestamp=2018-07-17+16%3A05%3A36&sign=q2a1fQij5Jso94QtiiVO0%2FlTWkQVutUeKdWcKBcL3UVnefNju7zW5gRrk7hn6rlXfVHa2Hb0wW4M%2Ff7GrdI6AokCKgoLYcqbwUiS4XeOmKxgPQBIqeb9gbBohUKOuV6sRCC0F3PCEgFjmr0YUDfPhTsDjyIUsapUzlGemP2Xha4%3D&biz_content=%7B%22out_trade_no%22%3A%221531814735053%22%2C%22total_fee%22%3Anull%2C%22body%22%3A%22%5Cu652f%5Cu4ed8%5Cu5b9d%5Cu5145%5Cu503c%22%2C%22spbill_create_ip%22%3Anull%2C%22notify_url%22%3A%22http%3A%5C%2F%5C%2Fwaimai.023wx.cn%5C%2Fapi%5C%2FPayment%5C%2FaliNotifyUrl%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D");
                        if (getLocalIpAddress() != null) {
                            netService.alipay(orderNumber(), (int) (rearchMoney * 100), "支付宝充值", getLocalIpAddress(), handleralipay);
                        }
                    }
                } else {
                    showTip("请输入充值金额", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    //获取随机数生成订单
    public String orderNumber() {
        long time = Calendar.getInstance().getTimeInMillis();
        int x = (int) (Math.random() * 100);
        return String.valueOf(time + x);
    }

    //获取手机ip
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("ex", ex.toString());
        }
        return null;
    }

    @SuppressLint("HandlerLeak")
    Handler handleralipay = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    Log.d("msd", msg.obj.toString() + "---");
                    onPayClick(msg.obj.toString());
                    break;
                case 2001:
                    Log.d("msd", "---22");
                    break;
                case 1001:
                    Log.d("msd", "---33");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handlerServiceChargedeweiixn = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    String appId = null;
                    String partnerId = null;
                    String prepayId = null;
                    String packageValue = null;
                    String nonceStr = null;
                    String timeStamp = null;
                    String sign = null;

                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        appId = object.getString("appid");
                        partnerId = object.getString("partnerid");
                        prepayId = object.getString("prepayid");
                        packageValue = object.getString("package");
                        nonceStr = object.getString("noncestr");
                        timeStamp = object.getString("timestamp");
                        sign = object.getString("sign");
//                        String code = jsonObject.getString("code");
//                        String msgs = jsonObject.getString("rmsg");
//                        if (code.equals("0")) {
//                            Toast.makeText(Rearch.this, msgs, Toast.LENGTH_SHORT).show();
//                        } else {
//                            JSONObject object = jsonObject.getJSONObject("data");
//                            appId = object.getString("appid");
//                            partnerId = object.getString("partnerid");
//                            prepayId = object.getString("prepayid");
//                            packageValue = object.getString("package");
//                            nonceStr = object.getString("noncestr");
//                            timeStamp = object.getString("timestamp");
//                            sign = object.getString("sign");
//                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    IWXAPI msgApi = WXAPIFactory.createWXAPI(Rearch.this, Constants.APP_ID, false);
                    // 将该app注册到微信
                    msgApi.registerApp(Constants.APP_ID);
                    PayReq request = new PayReq();
                    request.appId = Constants.APP_ID;
                    request.partnerId = partnerId;
                    request.prepayId = prepayId;
                    request.packageValue = packageValue;
                    request.nonceStr = nonceStr;
                    request.timeStamp = timeStamp;
                    request.sign = sign;
                    msgApi.sendReq(request);
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

    private void onPayClick(final String orderInfo) {
        //异步调用支付接口
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(Rearch.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(orderInfo, true);
                Log.d("pay", result + "----");
                android.os.Message msg = new android.os.Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //异步支付完成后接收支付结果
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    Log.i("pay", payResult.toString());
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(Rearch.this, "充值成功", Toast.LENGTH_SHORT).show();
//                        try {
//                            gotoActivityAndFinish(Rearch.class.getName(), null);
//                        } catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }
                        finish();
                        //Toast.makeText(Pay.this, R.string.mall_pay_succeed, Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(Rearch.this, "充值结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(Rearch.this, "充值失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Log.i("pay", "22");
                    Toast.makeText(Rearch.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }


    };
}
