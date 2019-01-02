package com.xinzuokeji.boxtakeawaybusiness.me;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.xinzuokeji.boxtakeawaybusiness.GSApplication.REQUEST_SETTING_NOTIFICATION;

public class MessageSetting extends BaseActivity {
    private ImageButton header_back;
    Switch sw_on_off;
    SeekBar seek_volume;
    private Vibrator mVibrator;  //声明一个振动器对象
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    VolumeReceiver receiver;
    ContentObserver mVoiceObserver;
    private LinearLayout ll_notification;
    private TextView tv_notification;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_message_setting);
        receiver = new VolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(receiver, filter);
        //获取手机震动服务
        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("消息设置");
        sw_on_off = findViewById(R.id.sw_on_off);
        seek_volume = findViewById(R.id.pro_volume);
        //通知显示
        tv_notification = findViewById(R.id.tv_notification);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //获取系统最大音量
        int maxVolume = 0;
        if (audioManager != null) {
            maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        seek_volume.setMax(maxVolume);
        //获取当前音量
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seek_volume.setProgress(currentVolume);
        //通知开启
        ll_notification = findViewById(R.id.ll_notification);

    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_notification.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isNotificationEnabled(this)) {
                tv_notification.setText("开启");
            } else {
                tv_notification.setText("关闭");
            }
        }
        //获取振动开启状态
        if (getVibrator()) {
            sw_on_off.setChecked(true);
        } else {
            sw_on_off.setChecked(false);
        }

        sw_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_on_off.isChecked()) {
                    sw_on_off.setChecked(true);
                    mVibrator.vibrate(new long[]{100, 100, 100, 1000}, -1);
                    setVibrator(true);
                } else {
                    sw_on_off.setChecked(false);
                    mVibrator.cancel();
                    setVibrator(false);
                }
            }
        });
        seek_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //设置系统音量
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                            && !notificationManager.isNotificationPolicyAccessGranted()) {
                        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        startActivity(intent);
                    } else {
                        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, 0);
                        audioManager.setStreamVolume(3, progress, 0);// 3 代表 AudioManager.STREAM_MUSIC
                    }
//                    int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                    seekBar.setProgress(currentVolume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mVoiceObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
//                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                assert audioManager != null;
                seek_volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
                //或者你也可以用媒体音量来监听改变，效果都是一样的。
//                seek_volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            }
        };


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_notification:
                // 跳转开启通知
                gotoNotificationSetting(this);
                break;
            default:
                break;
        }
    }

    //跳转到应用权限设置界面
    public void gotoNotificationSetting(Activity activity) {
        ApplicationInfo appInfo = activity.getApplicationInfo();
        String pkg = activity.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", pkg);
                intent.putExtra("app_uid", uid);
                activity.startActivityForResult(intent, REQUEST_SETTING_NOTIFICATION);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                activity.startActivityForResult(intent, REQUEST_SETTING_NOTIFICATION);
            } else {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivityForResult(intent, REQUEST_SETTING_NOTIFICATION);
            }
        } catch (Exception e) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivityForResult(intent, REQUEST_SETTING_NOTIFICATION);

        }
    }

    //开启通知回调

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTING_NOTIFICATION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isNotificationEnabled(this)) {
                    tv_notification.setText("开启");
                } else {
                    tv_notification.setText("关闭");
                }
            }
        }
    }

    //android开发通知栏权限是否开启
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isNotificationEnabled(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //8.0手机以上
            if (((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).getImportance() == NotificationManager.IMPORTANCE_NONE) {
                return false;
            }
        }

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;

        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private class VolumeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                seek_volume.setProgress(currentVolume);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
