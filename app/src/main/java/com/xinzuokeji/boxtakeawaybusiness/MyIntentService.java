package com.xinzuokeji.boxtakeawaybusiness;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.PopupWindow;

import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * Created by lenovo on 2018/3/19.
 */


//接收 通知  的 东西

public class MyIntentService extends Service implements TextToSpeech.OnInitListener {
    private PopupWindow popWnd;
    private String data;
    private String music;
    private static MediaPlayer mediaPlayer;
    private Context mContext;
    private static Ringtone mRingtone;
    NetService netService;
    int storeId;
    @Override
    public void onCreate() {
        super.onCreate();
        netService = new NetService(this);
        SharedPreferences sp = GSApplication.getInstance().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //storeId
        storeId = sp.getInt("storeId", 0);
        Log.i("onStartCommand", "onCreate");
    }

    public MyIntentService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //extraMap 值等于logout退出登录否则播放语音
        String extraMap = intent.getStringExtra("extraMap");
        String filename = intent.getStringExtra("filename");
        if (filename.equals("ordersound2.mp3")) {
            netService.showUntreated(storeId, showUntreated);
        }
        if (extraMap.equals("logout")) {

        } else {

            //1 初始化mediaplayer http://waimai.023wx.cn/public/music/421212.mp3
            final AudioManager audioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            // 获取最大音乐音量
            assert audioMgr != null;
            final int maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            if (!mediaPlayer.isPlaying()) {
                //2 设置到播放的资源位置 path 可以是网络 路径 也可以是本地路径
                try {
                    //  网络音频
                    //  mediaPlayer.setDataSource(extraMap);
                    //本地音频
                    if (Valid.isNotNullOrEmpty(filename)) {
                        if (filename.equals("ordersound2.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ordersound2);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else if (filename.equals("bt_device_connect.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bt_device_connect);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else if (filename.equals("push_to_grab.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.push_to_grab);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else if (filename.equals("push_to_grab_book.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.push_to_grab_book);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else if (filename.equals("push_to_transfer.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.push_to_transfer);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else if (filename.equals("refundsound2.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.refundsound2);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else if (filename.equals("remindersound2.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.remindersound2);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else if (filename.equals("auto_accept_fail.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.auto_accept_fail);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else if (filename.equals("autocancelordersound2.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.autocancelordersound2);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else if (filename.equals("autoordersound2.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.autoordersound2);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else if (filename.equals("bt_device_disconnect.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bt_device_disconnect);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else if (filename.equals("logistic_abnormal.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logistic_abnormal);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else if (filename.equals("net_disconnect.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.net_disconnect);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else if (filename.equals("pre_order.mp3")) {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pre_order);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        } else {
                            Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ordersound2);
                            mediaPlayer.setDataSource(this, setDataSourceuri);
                        }

                    }
                    if (Valid.isNotNullOrEmpty(extraMap)) {
                        mediaPlayer.setDataSource(extraMap);
                    }
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

            }
        }
        return super.onStartCommand(intent, flags, startId);

    }
    //    订单信息
    private final SetshopPutForwardHandler showUntreated = new SetshopPutForwardHandler(this);

    private static class SetshopPutForwardHandler extends Handler {
        private final WeakReference<MyIntentService> mActivity;

        private SetshopPutForwardHandler(MyIntentService activity) {
            mActivity = new WeakReference<MyIntentService>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyIntentService activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 2000:
                        Intent intentnew = new Intent();
                        if (msg.obj != null) {
                            intentnew.putExtra("new_order", (Serializable) msg.obj);
                        }
                        intentnew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentnew.setAction("com.xinzuokeji.boxtakeawaybusiness.pending");
                        activity.sendBroadcast(intentnew);
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
    public synchronized void playSound(Context context) {
//        if (!allowMusic) {
//            return;
//        }
        if (mRingtone == null) {
//            LogUtil.log("----------初始化铃声----------");
            String uri = "android.resource://" + context.getPackageName() + "/" + R.raw.ordersound2;
            Uri no = Uri.parse(uri);
            mRingtone = RingtoneManager.getRingtone(context.getApplicationContext(), no);
        }
        if (!mRingtone.isPlaying()) {
//            LogUtil.log("--------------播放铃声---------------" + mRingtone.isPlaying());
            mRingtone.play();
        }
    }


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


}
