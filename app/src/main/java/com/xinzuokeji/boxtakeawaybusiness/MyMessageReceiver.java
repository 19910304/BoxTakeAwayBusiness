package com.xinzuokeji.boxtakeawaybusiness;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.xinzuokeji.boxtakeawaybusiness.registrationShop.PassWordLogin;

import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.alibaba.sdk.android.ams.common.global.AmsGlobalHolder.getPackageName;

public class MyMessageReceiver extends MessageReceiver {
    //    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";
    Context mContext;
    AlertDialog alertDialog = null;
    MediaPlayer mediaPlayer;

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        this.mContext = context;
        // TODO 处理推送通知
        Log.e("MyMessageReceiver", "onNotification, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        //        {order=neworder, _ALIYUN_NOTIFICATION_ID_=291605, music=http://waimai.023wx.cn/public/music/421212.mp3}
        if (extraMap.get("music") != null && extraMap.get("filename") != null) {
            Intent intentserve = new Intent(context, MyIntentService.class);
            intentserve.putExtra("extraMap", extraMap.get("music"));
            intentserve.putExtra("filename", extraMap.get("filename"));
            context.startService(intentserve);
        }
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.putExtra("class_name", "");
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent1);
    }

    @Override
    public void onMessage(final Context context, CPushMessage cPushMessage) {
        Log.e("MyMessageReceiver", "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
        this.mContext = context;
        // 获得广播发送的数据
        if (cPushMessage.getTitle().equals("logout")) {
            // 收到广播解绑设配号，清空数据库
            GSApplication.getInstance().pushService.unbindAccount(new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    Log.i("unphone", "unphone");
                }

                @Override
                public void onFailed(String s, String s1) {
                    Log.i("unphone", "unphone111");
                }
            });
            SharedPreferences read = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
            read.edit().clear().apply();
            Intent intent = new Intent(context, LogoutActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
//            buildNotification(context, cPushMessage);
        }
    }

    //退出登录弹框 ,此方法需要悬浮窗权限，弃用
    private void dialogout(final Context context) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.pop_push_xiaxian, null);
        TextView text_xiaxian_yes = view.findViewById(R.id.text_xiaxian_yes);
        text_xiaxian_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                //销毁baseActivity下的所有activity
                GSApplication.getInstance().removeALLActivity_();
                MainActivity.mActivity.finish();
                Intent intent1 = new Intent(context, PassWordLogin.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent1);
            }
        });
        dialogBuilder.setView(view);
        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder.create();
        //8.0系统加强后台管理，禁止在其他应用和窗口弹提醒弹窗，如果要弹，必须使用TYPE_APPLICATION_OVERLAY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alertDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY));
        } else {
            alertDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        }

        alertDialog.show();


    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        this.mContext = context;
//        Log.i("extraMap", extraMap);
        Intent intentserveOpened = new Intent(mContext, MyIntentService.class);
        intentserveOpened.putExtra("extraMap", extraMap);
        mContext.startService(intentserveOpened);


    }

//    @Override
//    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
//        Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
//    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.e("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e("MyMessageReceiver", "onNotificationRemoved" + context);
    }

    /**
     * 接受到对应消息后，消息的弹出处理 透传信息使用
     */
    public void buildNotification(Context context, CPushMessage message) {
        Log.i("build", "sss");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_logout);
//        remoteViews.setImageViewResource(R.id.custom_icon, R.mipmap.ic_launcher);
//        remoteViews.setTextViewText(R.id.tv_custom_title, message.getTitle());
//        remoteViews.setTextViewText(R.id.tv_custom_content, message.getContent());
//        remoteViews.setTextViewText(R.id.tv_custom_time, new SimpleDateFormat("HH:mm").format(new Date()));
        Notification notification = new NotificationCompat.Builder(context)
                .setContent(remoteViews)
                .setContentTitle("ss")
                .setContentText("ss")
                .setSmallIcon(R.mipmap.app_icon)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ordersound2))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .build();
        notification.contentIntent = buildClickContent(context, message);
        notification.deleteIntent = buildDeleteContent(context, message);
        if (notificationManager != null) {
            notificationManager.notify(message.hashCode(), notification);
        }
    }


    public PendingIntent buildClickContent(Context context, CPushMessage message) {
        Intent clickIntent = new Intent();
        clickIntent.setAction("your notification click action");
        //添加其他数据
        clickIntent.putExtra("message key", message);//将message放入intent中，方便通知自建通知的点击事件
        return PendingIntent.getService(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public PendingIntent buildDeleteContent(Context context, CPushMessage message) {
        Intent deleteIntent = new Intent();
        deleteIntent.setAction("your notification click action");
        //添加其他数据
        deleteIntent.putExtra("message key", message);//将message放入intent中，方便通知自建通知的点击事件
        return PendingIntent.getService(context, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
