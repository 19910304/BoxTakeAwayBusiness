package com.xinzuokeji.boxtakeawaybusiness;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.xinzuokeji.boxtakeawaybusiness.util.LogUtils;

import java.util.ArrayList;
import java.util.List;


//全局变量  禾籽
public class GSApplication extends Application {
    private List<Activity> oList;//用于存放所有启动的Activity的集合
    //自己的位置
    private double Latitude;
    private double Longitude;

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    private static GSApplication instance;
    private static final String TAG = "Init";
    String user_phone = "";
    public OSS oss;
    public OSS ossNew;
    public static final String pictures = "waimai_img";
    // 禾籽
    public final String xiaomiId = "2882303761517865835";
    public final String xiaomiKey = "5901786519835";
    public static String mHost = "http://waimai.023wx.cn/";
    //俏吧
//    public final String xiaomiId = "2882303761517888346";
//    public final String xiaomiKey = "5141788836346";
//    public static String mHost = "http://www.qiaoba.ren/";
    // 快吖
//    public final String xiaomiId = "2882303761517880543";
//    public final String xiaomiKey = "5731788046543";
//    public static String mHost = "http://www.kvaiya.cn/";
    //设置全局的常量
    public static final int REQUESTCODE = 1;
    public static final int REQUEST_SETTING_NOTIFICATION = 2;
    public CloudPushService pushService;
    //
    //app  的更新地址
    public static String pathApp = mHost;

    public static GSApplication getInstance() {
        return instance;
    }

    //测试
    public static String ceshiphone = "0";

    @Override
    public void onCreate() {
        super.onCreate();
        switchIcon(2);
        oList = new ArrayList<Activity>();
        instance = this;
//        AMapNavi.setApiKey(this, "你的KEY");
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        ImageLoader.getInstance().init(configuration);
        SharedPreferences sp = this.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //取得user_id
        user_phone = sp.getString("user_phone", "");
        // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
        MiPushRegister.register(this, xiaomiId, xiaomiKey);
        // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
        HuaWeiRegister.register(this);
        //GCM/FCM辅助通道注册
//        GcmRegister.register(this, sendId, applicationId); //sendId/applicationId为步骤获得
        initCloudChannel(this);
        initNotificationChannel();
        //初始化打印工具类，发布时关掉
        LogUtils.isDebug(true);
    }

    //获取应用包名
    public String getPackage() {
        return getPackageName();
    }

    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
// 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
        //判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }

//            activity.finish();
        }
    }

    //8.0配置
    public void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "heziwaimai";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "notification channel";
            // 用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            mChannel.setShowBadge(true);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    //实例化阿里云
    public void initOss(String ACCESS_ID, String AccessKeySecret, String SecurityToken, String OSS_ENDPOINT) {
        //实例化阿里云
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ACCESS_ID, AccessKeySecret, SecurityToken);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(12); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        // oss为全局变量，OSS_ENDPOINT是一个OSS区域地址
        ossNew = new OSSClient(getApplicationContext(), OSS_ENDPOINT, credentialProvider);
    }

    // 创建服务用于捕获崩溃异常
    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {

        }
    };

    //   防止 在4.x上运行报错。红米（4.2.1）、三星（4.3）、酷派（4.4.4）。
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        pushService = PushServiceFactory.getCloudPushService();
        //动态设置推送配置
//        pushService.setAppkey("24954024");
//        pushService.setAppSecret("9dbb9c47065304b59b04010025214802");
//        pushService.setNotificationSmallIcon(R.mipmap.app_icon);
//        pushService.setNotificationSoundFilePath("android.resource://" + getPackageName() + "/" + R.raw.ordersound2);
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "init cloudchannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
//        Log.i("push", pushService.getUTDeviceId() + "---" + pushService.getDeviceId());
        // 阿里云获取的设备 id
        // 4a63e368e5d6418487ae102afa6e2c1f
    }

    //加载图片显示的配置项
    public DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();

    //   加载头像配置
    public DisplayImageOptions optionshead = new DisplayImageOptions.Builder().cacheInMemory(true)//使用内存缓存
            .
                    cacheOnDisk(true)//使用磁盘缓存
            .
                    showImageForEmptyUri(R.mipmap.circleimag)//url为空或请求的资源不存在时
            .
                    showImageOnFail(R.mipmap.circleimag)//下载失败时显示的图片
            .
                    bitmapConfig(Bitmap.Config.RGB_565)//设置图片格式
            .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
//            .
//                    displayer(new RoundedBitmapDisplayer(20))//设置圆角图片
            .
                    build();
    //    骑手默认图片
    public DisplayImageOptions optionsRider = new DisplayImageOptions.Builder().cacheInMemory(true)//使用内存缓存
            .
                    cacheOnDisk(true)//使用磁盘缓存
            .
                    showImageForEmptyUri(R.mipmap.rider_icon)//url为空或请求的资源不存在时
            .
                    showImageOnFail(R.mipmap.rider_icon)//下载失败时显示的图片
            .
                    bitmapConfig(Bitmap.Config.RGB_565)//设置图片格式
            .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
//            .
//                    displayer(new RoundedBitmapDisplayer(20))//设置圆角图片
            .
                    build();

    /**
     * @param useCode 1、为活动图标 2 为用普通图标 3、不启用判断
     */
    private void switchIcon(int useCode) {

        try {
            //要跟manifest的activity-alias 的name保持一致
            String icon_tag = getPackage() + ".icon_tag";
            String icon_tag_1212 = getPackage() + ".icon_tag_1212";

            if (useCode != 3) {

                PackageManager pm = getPackageManager();

                ComponentName normalComponentName = new ComponentName(
                        getBaseContext(),
                        icon_tag);
                //正常图标新状态，此处使用用来修改清单文件中activity-alias下的android:enable的值
                int normalNewState = useCode == 2 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                //新状态跟当前状态不一样才执行
                if (pm.getComponentEnabledSetting(normalComponentName) != normalNewState) {
                    //PackageManager.DONT_KILL_APP表示执行此方法时不杀死当前的APP进程
                    pm.setComponentEnabledSetting(
                            normalComponentName,
                            normalNewState,
                            PackageManager.DONT_KILL_APP);
                }

                ComponentName actComponentName = new ComponentName(
                        getBaseContext(),
                        icon_tag_1212);
                //活动图标新状态
                int actNewState = useCode == 1 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                //新状态跟当前状态不一样才执行
                if (pm.getComponentEnabledSetting(actComponentName) != actNewState) {
                    pm.setComponentEnabledSetting(
                            actComponentName,
                            actNewState,
                            PackageManager.DONT_KILL_APP);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
