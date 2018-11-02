package com.xinzuokeji.boxtakeawaybusiness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class VolumeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {


        }
    }
}
