package com.xinzuokeji.boxtakeawaybusiness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.xinzuokeji.boxtakeawaybusiness.registrationShop.PassWordLogin;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.util.Map;

import static com.xinzuokeji.boxtakeawaybusiness.GSApplication.pushService;

public class MyMessageReceiver extends MessageReceiver {
    //    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";
    Context mContext;
    AlertDialog alertDialog = null;

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        this.mContext = context;
        // TODO 处理推送通知
        Log.e("MyMessageReceiver", "onNotification, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        //        {order=neworder, _ALIYUN_NOTIFICATION_ID_=291605, music=http://waimai.023wx.cn/public/music/421212.mp3}
        if (Valid.isNotNullOrEmpty(extraMap.get("music"))) {
            Intent intentserve = new Intent(context, MyIntentService.class);
            intentserve.putExtra("extraMap", extraMap.get("music"));
            context.startService(intentserve);
        }
    }

    @Override
    public void onMessage(final Context context, CPushMessage cPushMessage) {
        Log.e("MyMessageReceiver", "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
        this.mContext = context;
        // 获得广播发送的数据
        if (cPushMessage.getTitle().equals("logout")) {
            // 收到广播解绑设配号，清空数据库
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
            SharedPreferences read = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
            read.edit().clear().commit();
            Intent intent = new Intent(context, LogoutActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    //退出登录弹框
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
}
