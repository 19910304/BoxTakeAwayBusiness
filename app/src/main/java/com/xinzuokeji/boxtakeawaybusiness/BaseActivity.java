package com.xinzuokeji.boxtakeawaybusiness;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.util.AppUtil;
import com.xinzuokeji.boxtakeawaybusiness.util.Utility;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private GSApplication application;
    private BaseActivity oContext;
    private Bitmap bitmap;
    public static String RecordStype = "1";
    public static final String shopUpdate = "1";
    public static final String shopAdd = "3";

    public static String getRecordStype() {
        return RecordStype;
    }

    public static void setRecordStype(String recordStype) {
        RecordStype = recordStype;
    }


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
    public final static int REQUEST_READ_PHONE_STATE = 1;

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
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Utility.StatusBarLightMode(this);

            }
        }
        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 取消标题
        if (mAllowFullScreen) {
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        mCounent = this;
        addActivity();// 调用添加方法
        Log.i(this.getClass().getSimpleName(), "---------onCreate ");
        // 初始化视图
        initView();
        // 初始化事件监听
        initEvent();

    }

    //绑定view ，不在findviewbyid
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        AppUtil.bindViews(this, getWindow().getDecorView());
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        AppUtil.bindViews(this, getWindow().getDecorView());
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
//        Log.i("application", application + "==");
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
                if (dayOfMonth < 10 && monthOfYear + 1 < 10) {
                    tv.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth + "");
                }
                if (dayOfMonth >= 10 && monthOfYear + 1 >= 10) {
                    tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "");
                }
//                Log.i("monthOfYear", String.valueOf(monthOfYear));
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

    //、将字符串转为时间戳
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 例如：
//        cc_time = 1291778220;
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
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

    /**
     * 解决小米手机上获取图片路径为null的情况
     *
     * @param intent
     * @return
     */
    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
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
        int user_id = sp.getInt("user_id", 0);
        return user_id;
    }

    public void suerinfoId(int user_id) {
        SharedPreferences mSharedPreferences = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("user_id", user_id);
        editor.commit();
    }

    //店铺头像
    public String getstoreIcon() {
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //取得user_id
        String storeIcon = sp.getString("storeIcon", "");
        return storeIcon;
    }

    public void setstoreIcon(String storeIcon) {
        SharedPreferences mSharedPreferences = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("storeIcon", storeIcon);
        editor.commit();
    }

    //开店中店铺id
    public int getChangeStoreId() {
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //storeId
        int storeId = sp.getInt("storeChangeId", 0);
        return storeId;
    }

    public void setChangeStoreId(int storeId) {
        SharedPreferences mSharedPreferences = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("storeChangeId", storeId);
        editor.commit();
    }

    //店铺id
    public int GetstoreId() {
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //storeId
        int storeId = sp.getInt("storeId", 0);
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
        editor.apply();
    }

    //设配型号
    public String moble() {
        String model = android.os.Build.MODEL;
        return model;
    }

    public double doubleDigit(double number) {
        BigDecimal bg = new BigDecimal(number);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
    //设配唯一id
    public String getIMEI() {
        TelephonyManager TelephonyMgr = (TelephonyManager) this.getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
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

    public String getDate() {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        Calendar c = Calendar.getInstance(tz);
        return c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DAY_OF_MONTH);
    }

    //获取当前年月日
    public String year() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
//        time1.setText("Date获取当前日期时间" + simpleDateFormat.format(date));
        return time;
    }


    //生成uuid
    public String getIdentity() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String identity = preference.getString("identity", null);
        if (identity == null) {
            identity = java.util.UUID.randomUUID().toString();
            preference.edit().putString("identity", identity);
        }
        return identity;
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}
