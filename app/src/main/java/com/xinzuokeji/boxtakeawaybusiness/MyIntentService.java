package com.xinzuokeji.boxtakeawaybusiness;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CommonCallback;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import static com.xinzuokeji.boxtakeawaybusiness.GSApplication.pushService;

/**
 * Created by lenovo on 2018/3/19.
 */


//接收 通知  的 东西

public class MyIntentService extends Service implements TextToSpeech.OnInitListener {
    private PopupWindow popWnd;
    private String data;
    private String music;
    MediaPlayer mediaPlayer;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("onStartCommand", "onCreate");
    }

    public MyIntentService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //extraMap 值等于logout退出登录否则播放语音
        String extraMap = intent.getStringExtra("extraMap");
        if (extraMap.equals("logout")) {

        } else {
            //  Log.i("extraMap", extraMap + "");
            //1 初始化mediaplayer http://waimai.023wx.cn/public/music/421212.mp3
            final AudioManager audioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            // 获取最大音乐音量
            assert audioMgr != null;
            final int maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            //2 设置到播放的资源位置 path 可以是网络 路径 也可以是本地路径
            try {
                mediaPlayer.setDataSource(extraMap);
                //3 准备播放
                mediaPlayer.prepareAsync();
                //3.1 设置一个准备完成的监听
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // 4 开始播放
                        mediaPlayer.start();
                        //调整音量 curVolume为音量值
                        audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume,
                                AudioManager.FLAG_PLAY_SOUND);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent1 = new Intent(this, MainActivity.class);
            intent1.putExtra("class_name", "");
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
        }
        return super.onStartCommand(intent, flags, startId);

    }

    /**
     *
     */
    private void showNoticeDialog() {
        Log.i("sssss", "2222");
//        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("账号异地登录");
//        builder.setMessage("");
        // 更新
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
//                startActivity(intent);
//                Intent intent = new Intent(getApplicationContext(), Actlogin.class);
//                MainActiv
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        noticeDialog.setCancelable(false);
        noticeDialog.show();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //执行耗时操作
            Log.e("bm", "runnable线程： " + Thread.currentThread().getId() + " name:" + Thread.currentThread().getName());
            if (true) {
                //判断  是否  处于  前台
//                boolean is = isBackground(getApplicationContext());
//                showNoticeDialog();
                Toast.makeText(getApplicationContext(), "账号异地登录!", Toast.LENGTH_SHORT).show();
//                SharedPreferences mSharedPreferences = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = mSharedPreferences.edit();
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
                SharedPreferences read = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
                read.edit().clear().commit();

            }
            Log.e("bm", "执行完耗时操作了~");
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("onStartCommand", "IBinder");
        return null;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    @Override
    public void onInit(int status) {
        Log.i("onStartCommand", status + "");

    }


    //将其下线
//    public void post_Cardasdasceshi() {
//        final String url = "http://ht.yhbapp.com/api/login/checkEquipment?" + "equipment="
////                + getDeviceId(getApplicationContext())
//
//                +getSerialNumber()
//
//                + "id";
//        Request.Builder requestBuilder = new Request.Builder()
//                .url(url);
//        //可以省略，默认是GET请求
//        requestBuilder.method("GET", null);
//        Request request = requestBuilder.build();
//
//        mcall.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String str = response.body().string();
//
//                Log.i("duihuanjuan", url + "---"+getSerialNumber()+"++---" + str);
//
//
//
//            }
//        });
//    }


    // Mac地址
    private static String getLocalMac(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    // Android Id
    private static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }


//    final Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            //执行耗时操作
//
//            Log.e("bm", "runnable线程： " + Thread.currentThread().getId() + " name:" + Thread.currentThread().getName());
//
//
////            if (status.getCode() == 200) {
//
//
////判断  是否  处于  前台
//            boolean is = isBackground(getApplicationContext());
//
//
////                if (is==true){
//
//
//            Toast.makeText(getApplicationContext(), "强制退出登录!", Toast.LENGTH_SHORT).show();
//
//
////                pushService.removeAlias(phone_me, new CommonCallback() {
////                    @Override
////                    public void onSuccess(String s) {
////                        Log.i("sada",s);
////                    }
////
////                    @Override
////                    public void onFailed(String s, String s1) {
////                        Log.i("sadax",s+"---"+s1);
////                    }
////                });
//
//
////                SharedPreferences read = getSharedPreferences(PRIVATE_SHAREPREFERENCE_KEY_DATA, MODE_MULTI_PROCESS);
////                read.edit().clear().commit();
//
////            }
//            Log.e("bm", "执行完耗时操作了~");
//        }
//    };

    /**
     * 判断整个app项目是否在用户操作界面
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    private String getSerialNumber() {

        String serial = null;

        try {

            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get = c.getMethod("get", String.class);

            serial = (String) get.invoke(c, "ro.serialno");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return serial;

    }


}
