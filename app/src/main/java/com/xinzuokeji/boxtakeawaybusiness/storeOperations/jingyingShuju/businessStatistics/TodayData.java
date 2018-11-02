package com.xinzuokeji.boxtakeawaybusiness.storeOperations.jingyingShuju.businessStatistics;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseFragmentActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.FragmentViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/7.
 */
//今日实时数据
public class TodayData extends BaseFragmentActivity {
    private ImageButton header_back;
    // ViewPager
    private ViewPager m_viewPager;
    // FragmentPagerAdapter
    private FragmentViewPagerAdapter m_adapter;
    // Fragment
    private List<Fragment> m_fragments;
    private Fragment m_fragment_today_data_turnover_trend;
    private Fragment m_fragment_record;
    // RadioGroup
    private RadioGroup rdg_m_bottom;
    private RadioButton rdb_today_data_turnover_trend, rdb_today_data_effective_order;
    private ImageView img_turnover_trend, img_effective_order;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_today_data);
        header_back = findViewById(R.id.header_back);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("今日实时数据");
        // ViewPager
        m_viewPager = (ViewPager) findViewById(R.id.vp_today_viewPager);

        // 初始化ViewPager数据
        m_fragment_today_data_turnover_trend = new FragmentTodayTurnoverTrend();
        m_fragment_record = new FragmentTodayTurnoverTrend();

        m_fragments = new ArrayList<>();
        m_fragments.add(m_fragment_today_data_turnover_trend);
        m_fragments.add(m_fragment_record);
        m_adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), m_viewPager, m_fragments);
        m_viewPager.setAdapter(m_adapter);

        // RadioGroup
        rdg_m_bottom = findViewById(R.id.rdg_today_data_vp);
        rdb_today_data_turnover_trend = findViewById(R.id.rdb_today_data_turnover_trend);
        rdb_today_data_effective_order = findViewById(R.id.rdb_today_data_effective_order);
        img_effective_order = findViewById(R.id.img_effective_order);
        img_turnover_trend = findViewById(R.id.img_turnover_trend);
        // 默认状态
        m_viewPager.setCurrentItem(0);
        pageSelect(0);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        // RadioGroup 状态切换事件
        rdg_m_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            // 选择事件处理
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_today_data_turnover_trend:
                        m_viewPager.setCurrentItem(0);
                        img_effective_order.setVisibility(View.INVISIBLE);
                        img_turnover_trend.setVisibility(View.VISIBLE);

                        break;
                    case R.id.rdb_today_data_effective_order:
                        m_viewPager.setCurrentItem(1);
                        img_effective_order.setVisibility(View.VISIBLE);
                        img_turnover_trend.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });
        // 页面切换效果
        m_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                pageSelect(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
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


    // 联动事件处理
    private void pageSelect(int index) {
        // 重置按钮状态
        for (int i = 0; i < rdg_m_bottom.getChildCount(); i++) {
            RadioButton radButton = (RadioButton) rdg_m_bottom.getChildAt(i);
            radButton.setChecked(false);
            radButton.setTextColor(getResources()
                    .getColor(R.color.gray_dark));
            Log.i("index", index + "---");

        }

        // 选中RadioButton的状态
        if (rdg_m_bottom.getCheckedRadioButtonId() != index) {
            RadioButton radButton = (RadioButton) rdg_m_bottom
                    .getChildAt(index);
            radButton.setChecked(true);
            radButton.setTextColor(getResources().getColor(R.color.red));

        }

    }

}
