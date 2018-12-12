package com.xinzuokeji.boxtakeawaybusiness.me;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.revice.RingReceived;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/1.
 */
//预订单提醒时间
public class YuOrderWarnTime extends BaseActivity {
    private ImageButton header_back;
    private ListView lv_warn_time;
    ArrayList<String> mDate = new ArrayList<>();
    ListViewAdapter<String> listViewAdapter;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Handler mHandler;
    private NetService netService;
    private String yuding_warn_time;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_yu_order_warn_time);
        netService = new NetService(this);
        yuding_warn_time = getIntent().getStringExtra("yuding_warn_time");

//        获取闹钟管理者
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("预订单提醒时间");
        lv_warn_time = findViewById(R.id.lv_warn_time);
        mDate.add(0, "30分钟");
        mDate.add(1, "45分钟");
        mDate.add(2, "60分钟");
        mDate.add(3, "90分钟");
        mDate.add(4, "105分钟");
        mDate.add(5, "120分钟");
        listViewAdapter = new ListViewAdapter<String>(this, R.layout.item_yu_order_time, mDate) {
            @Override
            public void convert(ViewHolder holder, String s, int id, int selectedPosition) {
                CheckBox ckDays = holder.getView(R.id.ck_days);
                ckDays.setVisibility(View.GONE);
                holder.setText(R.id.tv_days, s);
                ImageView imageViewTime = holder.getView(R.id.im_xuanzhong_time);
                if (id == selectedPosition) {
                    imageViewTime.setVisibility(View.VISIBLE);

                } else {
                    imageViewTime.setVisibility(View.GONE);
                }
                // 获取的时间与选中的时间相同显示选中状态
                if (getWarnTime().equals(s)) {
                    imageViewTime.setVisibility(View.VISIBLE);
                } else {
                    imageViewTime.setVisibility(View.GONE);
                }
            }
        };
        lv_warn_time.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listViewAdapter.clearSelection(i);
                listViewAdapter.notifyDataSetChanged();
                setWarnTime(mDate.get(i));
                //添加/修改商家预定单提醒时间
                netService.addShopReminTime(GetstoreId(), mDate.get(i), Hander_addShopReminTime);

            }
        });
        lv_warn_time.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), RingReceived.class);
                intent.setAction("com.zking.risk_android_alarn_notification.RING");
                int requestCode = 0;
                PendingIntent pendIntent = PendingIntent.getBroadcast(getApplicationContext(),
                        requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                // 5秒后发送广播，然后每个10秒重复发广播。广播都是直接发到AlarmReceiver的
                int triggerAtTime = (int) (SystemClock.elapsedRealtime() + 3 * 600 * 1000);
                int interval = 1000 * 60 * 60 * 24;
                if (alarmMgr != null) {

                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendIntent);
                }
            }
        }, 0);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("yuding_warn_time", getWarnTime());
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
            finish();
        }

    };

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler Hander_addShopReminTime = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    handler.sendEmptyMessage(1);
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
