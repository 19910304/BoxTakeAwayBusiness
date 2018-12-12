package com.xinzuokeji.boxtakeawaybusiness.revice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.util.LogUtils;

import java.io.IOException;

import static com.alibaba.sdk.android.ams.common.global.AmsGlobalHolder.getPackageName;

/**
 * Created by Administrator on 2018/8/2.
 */

public class RingReceived extends BroadcastReceiver {
    private static MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.zking.risk_android_alarn_notification.RING".equals(intent.getAction())) {
            LogUtils.i("时间到了！");
//            Toast.makeText(context, "时间到了haha", Toast.LENGTH_SHORT).show();

        }
    }

    //1 初始化mediaplayer http://waimai.023wx.cn/public/music/421212.mp3
    private void startmusic(Context context) {
        final AudioManager audioMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // 获取最大音乐音量
        assert audioMgr != null;
        final int maxVolume = audioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer = new MediaPlayer();
        if (!mediaPlayer.isPlaying()) {
            //2 设置到播放的资源位置 path 可以是网络 路径 也可以是本地路径
            try {
                //  网络音频
                //  mediaPlayer.setDataSource(extraMap);
                //本地音频

                Uri setDataSourceuri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ordersound2);
                mediaPlayer.setDataSource(context, setDataSourceuri);
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
}
