package com.xinzuo.qiaobashangjia.me;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuo.qiaobashangjia.BaseActivity;
import com.xinzuo.qiaobashangjia.R;
import com.xinzuo.qiaobashangjia.location.LocationSvc;

//自动接单设置
public class ZiDongOrderSetting extends BaseActivity {
    private ImageButton header_back;
    Switch sw_zidong_order;
    TextView tv_yingye_time;
    CheckBox ck_energy, ck_blue_tooth, ck_location, ck_low_energy;
    LinearLayout ll_lianjie_dayin;

    int level;


    @SuppressLint("NewApi")
    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activitty_zidong_order_setting);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("自动接单设置");
        sw_zidong_order = findViewById(R.id.sw_zidong_order);
        tv_yingye_time = findViewById(R.id.tv_yingye_time);
        // 是否自动接单条件
        ck_energy = findViewById(R.id.ck_energy);
        ck_blue_tooth = findViewById(R.id.ck_blue_tooth);
        ck_location = findViewById(R.id.ck_location);
        ck_low_energy = findViewById(R.id.ck_low_energy);
        // 判断电池状态
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        if (pm.isPowerSaveMode()) {
            ck_low_energy.setChecked(false);
        } else {
            ck_low_energy.setChecked(true);
        }

        //是否开启定位
        if (isLocationEnabled()) {
            ck_location.setChecked(true);
        } else {
            ck_location.setChecked(false);
        }
        // 是否开启蓝牙
        BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
        if (blueadapter != null) {
            if (blueadapter.isEnabled()) {
                ck_blue_tooth.setChecked(true);
            } else {
                ck_blue_tooth.setChecked(false);
            }
        }
        ll_lianjie_dayin = findViewById(R.id.ll_lianjie_dayin);

        // 注册一个BroadcastReceiver，作为访问电池计量之用
        //创建广播接受者对象

        Intent batteryInfoIntent = getApplicationContext()
                .registerReceiver(null,
                        new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        level = batteryInfoIntent.getIntExtra("level", 0);//电量（0-100）
        if (level > 40) {
            ck_energy.setChecked(true);
        } else {
            ck_energy.setChecked(false);
        }

    }


    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_lianjie_dayin.setOnClickListener(this);
        ck_energy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (level > 40) {
                    ck_energy.setChecked(true);
                }
                if (!isChecked) {
                    sw_zidong_order.setChecked(false);
                }
            }
        });

        ck_blue_tooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    sw_zidong_order.setChecked(false);
                }
            }
        });
        ck_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    sw_zidong_order.setChecked(false);
                }
                if (isChecked) {
                    // 启动服务
                    Intent intent = new Intent();
                    intent.setClass(ZiDongOrderSetting.this, LocationSvc.class);
                    startService(intent);
                    openGPS(ZiDongOrderSetting.this);
                }
            }
        });
        // 关闭低电量模式
        ck_low_energy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    sw_zidong_order.setChecked(false);
                }
                IntentFilter filter = new IntentFilter();
                filter.addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED);

            }
        });

        sw_zidong_order.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ck_blue_tooth.isChecked() && ck_energy.isChecked() && ck_location.isChecked() && ck_low_energy.isChecked()) {
                    if (isChecked) {
                        sw_zidong_order.setChecked(true);
                    } else {
                        sw_zidong_order.setChecked(false);
                    }
                } else {
                    if (isChecked) {
                        Toast.makeText(ZiDongOrderSetting.this, "此时不支持自动接单", Toast.LENGTH_SHORT).show();
                    }
                    sw_zidong_order.setChecked(false);

                }
            }
        });


    }

    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {

                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;

            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {

            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);

        }

    }

    private void addStopSaverAction(Notification.Builder nb) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED);
    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                // 取消注册，并关闭对话框
//                unregisterReceiver(batteryInfoIntent);

                break;
            case R.id.ll_lianjie_dayin:

                break;
            default:
                break;
        }
    }
}
