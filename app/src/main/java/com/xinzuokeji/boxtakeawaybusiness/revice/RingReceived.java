package com.xinzuokeji.boxtakeawaybusiness.revice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2018/8/2.
 */

public class RingReceived extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.zking.risk_android_alarn_notification.RING".equals(intent.getAction())) {
            //跳转到Activity
//            Intent intent1 = new Intent(context, RingActivity.class);      //给Intent设置标志位Flag
//            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            // Intent的组成部分：
//            // ComponementName extra Action Data Categlay(分组) Flag
//            context.startActivity(intent1);
        }
    }
}
