package com.xinzuokeji.boxtakeawaybusiness;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.navi.AMapNavi;
import com.xinzuokeji.boxtakeawaybusiness.adapter.FragmentViewPagerAdapter;
import com.xinzuokeji.boxtakeawaybusiness.entities.TodayOrder;
import com.xinzuokeji.boxtakeawaybusiness.me.Me;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.orderManagement.OrderManagement;
import com.xinzuokeji.boxtakeawaybusiness.pending.Pending;
import com.xinzuokeji.boxtakeawaybusiness.revice.OnePixelReceiver;
import com.xinzuokeji.boxtakeawaybusiness.storeOperations.StoreOperations;
import com.xinzuokeji.boxtakeawaybusiness.util.ACache;
import com.xinzuokeji.boxtakeawaybusiness.util.UpdateManager;
import com.xinzuokeji.boxtakeawaybusiness.util.Utility;
import com.yanzhenjie.permission.AndPermission;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.amap.api.location.AMapLocationClientOption.setLocationProtocol;

public class MainActivity extends FragmentActivity implements AMapLocationListener, View.OnClickListener {
    // ViewPager
    private ViewPager m_viewPager;
    // FragmentPagerAdapter
    private FragmentViewPagerAdapter m_adapter;
    // Fragment
    private List<Fragment> m_fragments;
    private Fragment m_fragment_communication;
    private Fragment m_fragment_record;
    private Fragment m_fragment_services;
    private Fragment m_fragment_me;

    // RadioGroup
    private RadioGroup rdg_m_bottom;

    //两次Back退出
    private long exitTime = 0;

    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    @SuppressLint("StaticFieldLeak")
    public static Activity mActivity;
    private String className;
    private TextView tv_quanxian;
    //版本更新
    UpdateManager update = new UpdateManager(this);
    private Handler mHandler;
    private NetService netService;
    // 店铺id
    private int storeId;
    public String user_phone;
    ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        netService = new NetService(this);
        aCache = ACache.get(MainActivity.this);
        startPermission();
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //storeId
        storeId = sp.getInt("storeId", 1);
        user_phone = sp.getString("user_phone", "");
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Utility.StatusBarLightMode(this);
                String manufacturer = Build.MANUFACTURER;
                //华为
                if ("huawei".equalsIgnoreCase(manufacturer)) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.red));
                }
            }
        }
        mActivity = this;
        setContentView(R.layout.activity_main);
        className = getIntent().getStringExtra("class_name");
        //检查版本更新
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                update.checkUpdate();
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200
                    );
                }

            }
        }, 0);
        location();
        initView();
        initEvnet();

    }


    // 初始化事件
    private void initEvnet() {
        tv_quanxian.setOnClickListener(this);
        // RadioGroup 状态切换事件
        rdg_m_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            // 选择事件处理
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_main_communication:
                        m_viewPager.setCurrentItem(0);
                        break;
                    case R.id.rdb_main_record:
                        m_viewPager.setCurrentItem(1);

                        break;
                    case R.id.rdb_main_services:
                        m_viewPager.setCurrentItem(2);
                        break;
                    case R.id.rdb_main_me:
                        m_viewPager.setCurrentItem(3);

                        break;

                    default:
                        break;
                }
            }
        });

        // 页面切换效果
        m_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                pageSelect(arg0);
                if (arg0 == 2) {
                    // 切换到门店运营刷新账户信息
                    netService.todayOrder(storeId, handle_todayOrder);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    // 联动事件处理
    private void pageSelect(int index) {
        // 重置按钮状态
        for (int i = 0; i < rdg_m_bottom.getChildCount(); i++) {
            RadioButton radButton = (RadioButton) rdg_m_bottom.getChildAt(i);
            radButton.setChecked(false);
            radButton.setTextColor(getResources()
                    .getColor(R.color.gray_dark));
        }
        // 选中RadioButton的状态
        if (rdg_m_bottom.getCheckedRadioButtonId() != index) {
            RadioButton radButton = (RadioButton) rdg_m_bottom
                    .getChildAt(index);
            radButton.setChecked(true);
            radButton.setTextColor(getResources().getColor(R.color.red));
        }
    }


    // 初始化视图
    private void initView() {
        // ViewPager
        m_viewPager = (ViewPager) findViewById(R.id.vp_main_viewPager);
        tv_quanxian = findViewById(R.id.tv_quanxian);
        // 初始化ViewPager数据
        m_fragment_communication = new Pending();
        m_fragment_record = new OrderManagement();
        m_fragment_services = new StoreOperations();
        m_fragment_me = new Me();
        m_fragments = new ArrayList<>();
        m_fragments.add(m_fragment_communication);
        m_fragments.add(m_fragment_record);
        m_fragments.add(m_fragment_services);
        m_fragments.add(m_fragment_me);
        m_adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), m_viewPager, m_fragments);
        m_viewPager.setAdapter(m_adapter);
        // RadioGroup
        rdg_m_bottom = findViewById(R.id.rdg_main_vp);
        if (className.equals("StoreSetting")) {
            m_viewPager.setCurrentItem(3);
            pageSelect(3);
        } else if (className.equals("RefundSuccess")) {
            aCache.put("RefundSuccess", "RefundSuccess");
            m_viewPager.setCurrentItem(1);
            pageSelect(1);
        } else {
            // 默认状态
            m_viewPager.setCurrentItem(0);
            pageSelect(0);
        }
        //注册监听屏幕的广播
//        pixelReceiver();

    }

    //注册监听屏幕的广播
    private void pixelReceiver() {
        //注册监听屏幕的广播
        OnePixelReceiver mOnepxReceiver = new OnePixelReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        registerReceiver(mOnepxReceiver, intentFilter);
    }

    //刷新今天订单信息
    private final Handle_todayOrder handle_todayOrder = new Handle_todayOrder(this);

    private static class Handle_todayOrder extends Handler {
        private final WeakReference<MainActivity> mActivity;

        private Handle_todayOrder(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 2000:
                        if (msg.obj != null) {
                            TodayOrder todayOrder = (TodayOrder) msg.obj;
                            activity.aCache.put("money", todayOrder.money);
                            activity.aCache.put("num", todayOrder.num);
                            // 发送广播通知StoreOperations 刷新收入情况
                            Intent intent = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction("com.scott.sayhi");
                            activity.sendBroadcast(intent);
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
        }
    }


    // 监听返回按键
    @SuppressLint("RestrictedApi")
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再次点击退出应用",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                //地图、导航相关资源清理
                AMapNavi navi = AMapNavi.getInstance(GSApplication.getInstance());
                if (navi != null) {
                    navi.destroy();
                }
                //应用退到后台
                moveTaskToBack(true);
                //结束应用
//                BaseActivity baseActivity = new BaseActivity();
//                baseActivity.removeALLActivity();
//                finish();
//                抛弃
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);

            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    //广播监听home键
    private static HomeWatcherReceiver mHomeKeyReceiver = null;

    private static void registerHomeKeyReceiver(Context context) {
        Log.i("", "registerHomeKeyReceiver");
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    private static void unregisterHomeKeyReceiver(Context context) {
        Log.i("", "unregisterHomeKeyReceiver");
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerHomeKeyReceiver(this);
    }

    @Override
    protected void onPause() {
        unregisterHomeKeyReceiver(this);
        super.onPause();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 修改网络协议为https；适配9.0
        setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTPS);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            if (isFloatWindowOpAllowed(this)) {//已经开启
//                switchActivity();
                tv_quanxian.setVisibility(View.GONE);
            } else {
                tv_quanxian.setVisibility(View.GONE);
                Toast.makeText(this, "开启悬浮窗失败", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 12) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(MainActivity.this)) {
                    Toast.makeText(this, "权限授予失败,无法开启悬浮窗", Toast.LENGTH_SHORT).show();
                    tv_quanxian.setVisibility(View.GONE);
                } else {
//                    switchActivity();
                    tv_quanxian.setVisibility(View.GONE);
                }
            }
        } else {

            //调用FragmentMe中的方法
            m_fragment_services.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                double la = aMapLocation.getLatitude();//获取纬度
                double lo = aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码
//                Log.i("city", android.os.Build.MODEL + aMapLocation.getCity() + "--" + aMapLocation.getDistrict() + "-" + aMapLocation.getStreet() + "-" + aMapLocation.getStreetNum());
                //发送广播（位置信息）
                Intent intentnew = new Intent();
                intentnew.putExtra("Location", android.os.Build.MODEL + aMapLocation.getCity() + "--" + aMapLocation.getDistrict() + "-" + aMapLocation.getStreet() + "-" + aMapLocation.getStreetNum() + lo + "," + la);
                intentnew.putExtra("user_phone", user_phone);
                intentnew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentnew.setAction("location");
                sendBroadcast(intentnew);
                if (aMapLocation.getCity() != null) {
                    setCity(aMapLocation.getCity());
                }

                //将坐标设置成全局
                GSApplication.getInstance().setLatitude(la);
                GSApplication.getInstance().setLongitude(lo);
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    //将地图移动到定位点
                    //点击定位按钮 能够将地图的中心移动到定位点
                    //添加图钉
                    //  aMap.addMarker(getMarkerOptions(amapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getCity() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getDistrict() + ""
                            + aMapLocation.getStreet() + ""
                            + aMapLocation.getStreetNum());
                    if (aMapLocation.getCity() != null) {
                        setCity(aMapLocation.getCity());
                    }
//                    Toast.makeText(getApplicationContext(), buffer.toString() + la + "*" + lo, Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
//                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();//停止定位
        mLocationClient.onDestroy();//销毁定位客户端。

    }

    //保存城市
    public void setCity(String city) {
        SharedPreferences mSharedPreferences = getSharedPreferences("city", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("city", city);
        editor.apply();
    }


    /**
     * 判断悬浮窗权限
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isFloatWindowOpAllowed(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return checkOp(context, 24);  // AppOpsManager.OP_SYSTEM_ALERT_WINDOW
        } else {
            if ((context.getApplicationInfo().flags & 1 << 27) == 1 << 27) {
                return true;
            } else {
                return false;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class<?> spClazz = Class.forName(manager.getClass().getName());
                Method method = manager.getClass().getDeclaredMethod("checkOp", int.class, int.class, String.class);
                int property = (Integer) method.invoke(manager, op,
                        Binder.getCallingUid(), context.getPackageName());
//                Log.e("399", " property: " + property);

                if (AppOpsManager.MODE_ALLOWED == property) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e("399", "Below API 19 cannot invoke!");
        }
        return false;
    }


    /**
     * 打开权限设置界面
     */
    public void openSetting() {
        try {
            Intent localIntent = new Intent(
                    "miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", getPackageName());
            startActivityForResult(localIntent, 11);
//            LogUtil.E("启动小米悬浮窗设置界面");
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent1.setData(uri);
            startActivityForResult(intent1, 11);
//            LogUtil.E("启动悬浮窗界面");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_quanxian:
                openSetting();
                break;
            default:
                break;
        }
    }

    //权限启动
    private void startPermission() {
        AndPermission.with(MainActivity.this).requestCode(88861)
                .permission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,//sd卡的权限
                        Manifest.permission.ACCESS_FINE_LOCATION,//定位权限
                        Manifest.permission.READ_PHONE_STATE,// 手机信息的权限
                        Manifest.permission.CAMERA
                ).send();
    }

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };


}
