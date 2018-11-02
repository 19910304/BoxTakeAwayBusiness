package com.xinzuokeji.boxtakeawaybusiness;


//
//

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.registrationShop.PassWordLogin;

import java.util.HashMap;


//import android.os.Handler;
//import android.os.Message;

/******************************************************
 * 开始广告页
 ****************************************************/
public class Splash extends BaseActivity {
    public int user_id, storeId;
    public String user_phone;

//    //版本更新
//    UpdateManager update = new UpdateManager(Splash.this);

    @Override
    public void initView() {
        super.initView();
        NetService netService = new NetService(this);

        // 去掉信息栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 设置内容
        setContentView(R.layout.activity_splash);
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //取得user_id 没有默认id
        user_id = sp.getInt("user_id", 0);
        storeId = sp.getInt("storeId", 0);
        user_phone = sp.getString("user_phone", "");
        //有商家和店铺时进入主页
        if (user_id != 0 && storeId != 0) {
            try {
                HashMap<String, String> extras = new HashMap<>();
                extras.put("class_name", "splash");
                gotoActivityAndFinish(MainActivity.class.getName(), extras);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                gotoActivityAndFinish(PassWordLogin.class.getName(), null);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        //版本检查更新
//        if (!update.checkUpdate()) {
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//
//            }
//        }
//                .start();
//        }
    }


    //Handler处理消息
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
//                    Toast.makeText(getApplication(), "", Toast.LENGTH_LONG).show();
                    break;
                case 1002:
//                    try {
//                        gotoActivityAndFinish(MainActivity.class.getName(), null);
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
                    break;
                case 1003:

                    break;
                default:
                    break;
            }
        }
    };


    //判断当前是否有可用的网路链接

    private Boolean isNetworkAvailable() {
        Context context = getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        }
        return false;
    }
}
