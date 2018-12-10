package com.xinzuokeji.boxtakeawaybusiness.storeOperations.jingyingShuju;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

/**
 * Created by Administrator on 2018/8/7.
 */
//流量统计
public class MobileCounter extends BaseActivity {
    private ImageButton header_back;
    private RadioGroup rdg_tenement_bottom;
    private RadioButton rdb_tenement_yesterday, rdb_tenement_seven_day, rdb_tenement_month;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_mobile_counter);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("流量统计");
        //选择天数
        rdg_tenement_bottom = findViewById(R.id.rdg_tenement_bottom);
        rdb_tenement_yesterday = findViewById(R.id.rdb_tenement_yesterday);
        rdb_tenement_seven_day = findViewById(R.id.rdb_tenement_seven_day);
        rdb_tenement_month = findViewById(R.id.rdb_tenement_month);
        rdg_tenement_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_tenement_yesterday:
//                        tv_sales_ranking.setText("昨天销售额排行");
//                        tv_consider.setText("昨天销量排行");
//                        tv_time.setText("");
                        break;
                    case R.id.rdb_tenement_seven_day:
//                        tv_sales_ranking.setText("本周销售额排行");
//                        tv_consider.setText("本周销量排行");
//                        tv_time.setText("");
                        break;
                    case R.id.rdb_tenement_month:
//                        tv_sales_ranking.setText("本月销售额排行");
//                        tv_consider.setText("本月销量排行");
//                        tv_time.setText("");
                        break;
                }
            }
        });
    }

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
}
