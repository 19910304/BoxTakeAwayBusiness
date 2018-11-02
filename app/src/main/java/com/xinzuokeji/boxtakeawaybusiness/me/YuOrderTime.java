package com.xinzuokeji.boxtakeawaybusiness.me;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/1.
 */
//预订单日期
public class YuOrderTime extends BaseActivity {
    private ImageButton header_back;
    ArrayList<String> mDate = new ArrayList<>();
    private ListView lv_yudingdan_time;
    ListViewAdapter<String> listViewAdapter;
    NetService netService;
    String yudingTime;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_yu_order_time);
        yudingTime = getIntent().getStringExtra("yuding_time");
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("预订单日期");
        lv_yudingdan_time = findViewById(R.id.lv_yudingdan_time);
        mDate.add(0, "1天");
        mDate.add(1, "2天");
        mDate.add(2, "3天");
        mDate.add(3, "4天");
        mDate.add(4, "5天");
        mDate.add(5, "6天");
        mDate.add(6, "7天");
        listViewAdapter = new ListViewAdapter<String>(this, R.layout.item_yu_order_time, mDate) {
            @Override
            public void convert(ViewHolder holder, String s, int id, int selectedPosition) {
                CheckBox ckDays = holder.getView(R.id.ck_days);
                holder.setText(R.id.tv_days, s);
                if (id == selectedPosition) {
                    ckDays.setChecked(true);
                } else {
                    ckDays.setChecked(false);
                }
                // 获取时间相同显示选中
                if (yudingTime.equals(s)) {
                    ckDays.setChecked(true);
                } else {
                    ckDays.setChecked(false);
                }

            }
        };
        lv_yudingdan_time.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listViewAdapter.clearSelection(i);
                listViewAdapter.notifyDataSetChanged();
                netService.addShopPerDate(GetstoreId(), mDate.get(i), Hander_addShopPerDate);
                yudingTime = mDate.get(i);

            }
        });
        lv_yudingdan_time.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("yuding_time", yudingTime);
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
        }
    }

    @SuppressLint("HandlerLeak")
    Handler Hander_addShopPerDate = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    handler.sendEmptyMessage(0);
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
