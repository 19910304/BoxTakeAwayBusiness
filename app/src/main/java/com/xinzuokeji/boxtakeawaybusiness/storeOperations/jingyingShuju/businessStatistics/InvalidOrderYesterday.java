package com.xinzuokeji.boxtakeawaybusiness.storeOperations.jingyingShuju.businessStatistics;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/8.
 */
//昨天无效订单
public class InvalidOrderYesterday extends BaseActivity {
    private ImageButton header_back;
    private RadioGroup rdg_data;
    private RadioButton rdb_today_data, rdb_week_data, rdb_month_data;
    private ListView lv_order_Invalid_history;
    private List<String> mDate;
    private ListViewAdapter<String> lvAdapterOIH;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_invalid_order_yesterday);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("昨日无效订单");
        // RadioGroup
        rdg_data = findViewById(R.id.rdg_today_data_vp);
        rdb_today_data = findViewById(R.id.rdb_today_data);
        rdb_week_data = findViewById(R.id.rdb_week_data);
        rdb_month_data = findViewById(R.id.rdb_month_data);
        // 历史取消订单
        lv_order_Invalid_history = findViewById(R.id.lv_order_Invalid_history);
        mDate = new ArrayList<>();
        mDate.add(0, "");
        mDate.add(1, "");
        mDate.add(2, "");
        lvAdapterOIH = new ListViewAdapter<String>(this, R.layout.item_order_invalid_history, mDate) {
            @Override
            public void convert(ViewHolder holder, String s, int id, int selectedPosition) {

            }
        };
        lv_order_Invalid_history.setAdapter(lvAdapterOIH);
        setPullLvHeight(lv_order_Invalid_history);
        lvAdapterOIH.notifyDataSetChanged();
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        rdg_data.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedid) {
                switch (checkedid) {
                    case R.id.rdb_today_data:

                        break;
                    case R.id.rdb_week_data:

                        break;
                    case R.id.rdb_month_data:

                        break;
                    default:
                        break;
                }
            }
        });
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
}
