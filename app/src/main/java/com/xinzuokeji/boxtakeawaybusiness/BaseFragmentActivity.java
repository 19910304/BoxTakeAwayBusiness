package com.xinzuokeji.boxtakeawaybusiness;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/7.
 */

public class BaseFragmentActivity extends FragmentActivity implements View.OnClickListener {
    private Bitmap bitmap;
    private GSApplication application;
    // 请求成功
    public static Integer DATA_SUCCESS = 2000;
    // 请求失败
    public static Integer DATA_FAILURE = 2001;
    /***********************************************************************
     * 公共部分功能封装
     ***********************************************************************/
    // 是否允许全屏
    private boolean mAllowFullScreen = true;
    public Activity mCounent;

    // 初始化事件监听,
    public void initEvent() {
    }

    // 初始化视图，setContent，初始化控件写在该方法中
    public void initView() {
    }

    // 是否全屏
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.mAllowFullScreen = allowFullScreen;
    }

    // 实现接口OnClickListener,什么也没做!!!
    @Override
    public void onClick(View v) {
    }

    //获取当前系统时间
    public String time() {
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(day));
        return df.format(day);
    }

    public static String getSHA256StrJava(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    // 页面跳转方法
    public void gotoActivity(String className, HashMap<String, String> extras) throws ClassNotFoundException {

        // 定义 intent
        Intent intent = new Intent(this, Class.forName(className));

        // 添加参数
        if (extras != null) {
            Iterator<Map.Entry<String, String>> iterator = extras.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                @SuppressWarnings("rawtypes")
                Map.Entry entry = (Map.Entry) iterator.next();
                intent.putExtra(entry.getKey().toString(), entry.getValue()
                        .toString());
            }
        }

        // 跳转
        startActivity(intent);
    }

    // 页面跳转方法
    public void gotoActivityAndFinish(String className, HashMap<String, String> extras) throws ClassNotFoundException {
        // 跳转到新的Activity
        gotoActivity(className, extras);
        // 结束当前Activity
        this.finish();
    }

    /***************************************************************************
     * 打印Activity生命周期
     ***************************************************************************/

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 取消标题
        if (mAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        Log.i(this.getClass().getSimpleName(), "---------onCreate ");
        mCounent = this;//把当前的上下文对象赋值给BaseActivity
//        if (application == null) {
//            // 得到Application对象
//            application = GSApplication.getInstance();
//        }
        addActivity();// 调用添加方法
        // 初始化视图
        initView();
        // 初始化事件监听
        initEvent();
    }

    // 添加Activity方法
    public void addActivity() {
        GSApplication.getInstance().addActivity_(mCounent);// 调用myApplication的添加Activity方法
    }

    //销毁当个Activity方法
    public void removeActivity() {
        GSApplication.getInstance().removeActivity_(mCounent);// 调用myApplication的销毁单个Activity方法
    }

    //销毁所有Activity方法
    public void removeALLActivity() {
        GSApplication.getInstance().removeALLActivity_();// 调用myApplication的销毁所有Activity方法
    }

    //
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(this.getClass().getSimpleName(), "---------onStart ");
    }

    //
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(this.getClass().getSimpleName(), "---------onResume ");
    }

    //
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(this.getClass().getSimpleName(), "---------onStop ");
    }

    //
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(this.getClass().getSimpleName(), "---------onPause ");
    }

    //
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(this.getClass().getSimpleName(), "---------onRestart ");
    }

    //
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(this.getClass().getSimpleName(), "---------onDestroy ");
        this.finish();
    }

    public Bitmap returnBitMap(final String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return bitmap;
    }

    /**
     * 日期选择activity这是日期选择，
     *
     * @param //里面的themeresid表示的是效果的背景色，设置为2就是上面的效果图，当点击确认后就会触发上面的onDateSet方法，在这里就可以做操作，同理，时间选择如下：，同样很简单，还有一些简单的操作可以自行百度。
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static String showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作

//                tv.setText("您选择了：" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                if (monthOfYear + 1 < 10) {
                    tv.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth + "");
                }
                if (dayOfMonth < 10) {
                    tv.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth + "");
                }
                if (dayOfMonth < 10 && monthOfYear < 10) {
                    tv.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth + "");
                }
                if (dayOfMonth >= 10 && monthOfYear >= 10) {
                    tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "");
                }
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
        return tv.getText().toString();
    }

    /**
     * 时间选择
     *
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static String showTimePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog(activity, themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay < 10) {
                            tv.setText("0" + hourOfDay + ":" + minute + "");
                        }
                        if (minute < 10) {
                            tv.setText(hourOfDay + ":" + "0" + minute);
                        }
                        if (minute < 10 && hourOfDay < 10) {
                            tv.setText("0" + hourOfDay + ":" + "0" + minute);
                        }
                        if (minute >= 10 && hourOfDay >= 10) {
                            tv.setText(hourOfDay + ":" + minute);
                        }

                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
        return tv.getText().toString();
    }

    // 监听返回按键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            //绑定返回按键和返回按钮
            ImageButton header_back = findViewById(R.id.header_back);
            if (header_back != null) {
                header_back.performClick();
            }

            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void showTip(String message, Integer duration) {
        Toast.makeText(this, message, duration).show();
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 检查当前网络是否可用
     *
     * @param
     * @return
     */

    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //重新计算listview高度
    public void setPullLvHeight(ListView pull) {
        int totalHeight = 0;
        ListAdapter adapter = pull.getAdapter();
        for (int i = 0, len = adapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, pull);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = pull.getLayoutParams();
        params.height = totalHeight + (pull.getDividerHeight() * (pull.getCount() - 1));
        pull.setLayoutParams(params);
    }

    //商家id
    public int getuserinfoId() {
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //取得user_id
        int user_id = sp.getInt("user_id", 1);
        return user_id;
    }

    public void suerinfoId(int user_id) {
        SharedPreferences mSharedPreferences = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("user_id", user_id);
        editor.commit();
    }

    //店铺id
    public int GetstoreId() {
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //storeId
        int storeId = sp.getInt("storeId", 1);
        return storeId;
    }

    public void SetstoreId(int storeId) {
        SharedPreferences mSharedPreferences = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("storeId", storeId);
        editor.commit();
    }

    public void suerphone(String user_phone) {
        SharedPreferences mSharedPreferences = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("user_phone", user_phone);
//
        editor.commit();
    }

    public String getsuerphone() {
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //取得user_phone
        String user_phone = sp.getString("user_phone", "");
        return user_phone;
    }

    public void suerName(String user_phone) {
        SharedPreferences mSharedPreferences = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("user_name", user_phone);
//
        editor.commit();
    }

    public String getsuerName() {
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //取得user_name
        String user_name = sp.getString("user_name", "");
        return user_name;
    }

    //设置营业状态
    public void setOperatingState(String OperatingState) {
        SharedPreferences mSharedPreferences = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("OperatingState", OperatingState);
        editor.commit();
    }

    //获取营业状态
    public String getOperatingState() {
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //取得user_name
        String OperatingState = sp.getString("OperatingState", "");
        return OperatingState;
    }

    //保存城市
    public void setCity(String city) {
        SharedPreferences mSharedPreferences = getSharedPreferences("city", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("city", city);
        editor.commit();
    }

    public String getCity() {
        SharedPreferences sp = getSharedPreferences("city", Context.MODE_PRIVATE);
        //取得user_name
        String user_name = sp.getString("city", "");
        return user_name;
    }

    //保存预订单提醒时间
    public void setWarnTime(String WarnTime) {
        SharedPreferences mSharedPreferences = getSharedPreferences("WarnTime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("WarnTime", WarnTime);
        editor.commit();
    }

    //获取预订单提醒时间
    public String getWarnTime() {
        SharedPreferences sp = getSharedPreferences("WarnTime", Context.MODE_PRIVATE);
        //取得WarnTime
        String WarnTime = sp.getString("WarnTime", "30分钟");
        return WarnTime;
    }

    //清除用户信息
    public void ClearloginUser() {
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    //设配型号
    public String moble() {
        String model = android.os.Build.MODEL;
        return model;
    }

    //设配唯一id
    public String getIMEI() {
        TelephonyManager TelephonyMgr = (TelephonyManager) this.getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        assert TelephonyMgr != null;
        @SuppressLint("HardwareIds") String szImei = TelephonyMgr.getDeviceId();
        if (Valid.isNotNullOrEmpty(szImei)) {
            return szImei;
        } else {
            return "";
        }
//        return szImei;
    }

    //显示红点提示
    public void showBadgeView(View view, Boolean isShow) {
        //显示红点

//        BadgeView badgeView = new BadgeView(this);
//        badgeView.setWidth(Utility.dip2px(this, 8));
//        badgeView.setHeight(Utility.dip2px(this, 8));
////        badgeView.setBackground(R.drawable.round_tip, Color.RED);
//        if (isShow) {
//            badgeView.setVisibility(View.VISIBLE);
//        } else {
//            badgeView.setVisibility(View.GONE);
//        }
//        badgeView.setTextColor(Color.RED);
//        badgeView.setTargetView(view);
    }

    public void showNumberBadgeView(View view, int textColor, int number, float setWidth, float setHeight) {
//        BadgeView badgeView = new BadgeView(this);
//        badgeView.setWidth(Utility.dip2px(this, setWidth));
//        badgeView.setHeight(Utility.dip2px(this, setHeight));
////        badgeView.setBackground(R.drawable.round_tip, Color.RED);
//        badgeView.setVisibility(View.VISIBLE);
//        badgeView.setTextColor(textColor);
//        badgeView.setBadgeCount(number);
//        badgeView.setTargetView(view);
    }
}
