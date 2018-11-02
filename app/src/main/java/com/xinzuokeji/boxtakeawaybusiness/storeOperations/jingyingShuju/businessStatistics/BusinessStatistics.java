package com.xinzuokeji.boxtakeawaybusiness.storeOperations.jingyingShuju.businessStatistics;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

/**
 * Created by Administrator on 2018/6/4.
 */
//营业统计
public class BusinessStatistics extends BaseActivity {
    private ImageButton header_back;
    private LinearLayout ll_today_shuju, ll_huanbi_fenxi, ll_yesterday_duibi, ll_yesterday_wuxiao;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_business_statistics);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("营业统计");
        ll_today_shuju = findViewById(R.id.ll_today_shuju);
        ll_huanbi_fenxi = findViewById(R.id.ll_huanbi_fenxi);
        ll_yesterday_duibi = findViewById(R.id.ll_yesterday_duibi);
        ll_yesterday_wuxiao = findViewById(R.id.ll_yesterday_wuxiao);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_today_shuju.setOnClickListener(this);
        ll_huanbi_fenxi.setOnClickListener(this);
        ll_yesterday_duibi.setOnClickListener(this);
        ll_yesterday_wuxiao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_today_shuju://今日实时数据
                try {
                    gotoActivity(TodayData.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_huanbi_fenxi://环比分析

                break;
            case R.id.ll_yesterday_duibi://昨日商圈同行对比

                break;
            case R.id.ll_yesterday_wuxiao://昨日无效订单统计
                try {
                    gotoActivity(InvalidOrderYesterday.class.getName(),null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
