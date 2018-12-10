package com.xinzuokeji.boxtakeawaybusiness.storeOperations.jingyingShuju;

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
 * Created by Administrator on 2018/8/7.
 */
//商品分析
public class CommercialAnalysis extends BaseActivity {
    private ImageButton header_back;
    private TextView tv_sales_ranking, tv_consider, tv_time;
    private ListView lv_sales_ranking, lv_consider;
    private ListViewAdapter<String> listViewAdapterSR, listViewAdapterCd;
    List<String> mDateSR, mDateCd;
    private RadioGroup rdg_tenement_bottom;
    private RadioButton rdb_tenement_yesterday, rdb_tenement_seven_day, rdb_tenement_month;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_commercial_analysis);
        header_back = findViewById(R.id.header_back);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("商品分析");
        //选择天数
        rdg_tenement_bottom = findViewById(R.id.rdg_tenement_bottom);
        rdb_tenement_yesterday = findViewById(R.id.rdb_tenement_yesterday);
        rdb_tenement_seven_day = findViewById(R.id.rdb_tenement_seven_day);
        rdb_tenement_month = findViewById(R.id.rdb_tenement_month);
        // 销售额排行
        tv_sales_ranking = findViewById(R.id.tv_sales_ranking);
        lv_sales_ranking = findViewById(R.id.lv_sales_ranking);
        // 销量排行
        tv_consider = findViewById(R.id.tv_consider);
        lv_consider = findViewById(R.id.lv_consider);
        //数据趋势
        tv_time = findViewById(R.id.tv_time);
        rdg_tenement_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_tenement_yesterday:
                        tv_sales_ranking.setText("昨天销售额排行");
                        tv_consider.setText("昨天销量排行");
                        tv_time.setText("");
                        break;
                    case R.id.rdb_tenement_seven_day:
                        tv_sales_ranking.setText("本周销售额排行");
                        tv_consider.setText("本周销量排行");
                        tv_time.setText("");
                        break;
                    case R.id.rdb_tenement_month:
                        tv_sales_ranking.setText("本月销售额排行");
                        tv_consider.setText("本月销量排行");
                        tv_time.setText("");
                        break;
                }
            }
        });

        // 销售额排行
        mDateSR = new ArrayList<>();
        mDateSR.add(0, "");
        mDateSR.add(1, "");
        mDateSR.add(2, "");
        listViewAdapterSR = new ListViewAdapter<String>(this, R.layout.item_commercial_analysis, mDateSR) {
            @Override
            public void convert(ViewHolder holder, String s, int id, int selectedPosition) {

            }
        };
        lv_sales_ranking.setAdapter(listViewAdapterSR);
        setPullLvHeight(lv_sales_ranking);
        listViewAdapterSR.notifyDataSetChanged();
        // 销量排行
        mDateCd = new ArrayList<>();
        mDateCd.add(0, "");
        mDateCd.add(1, "");
        mDateCd.add(2, "");
        listViewAdapterCd = new ListViewAdapter<String>(this, R.layout.item_commercial_analysis, mDateCd) {
            @Override
            public void convert(ViewHolder holder, String s, int id, int selectedPosition) {

            }
        };
        lv_consider.setAdapter(listViewAdapterCd);
        setPullLvHeight(lv_consider);
        listViewAdapterCd.notifyDataSetChanged();

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
