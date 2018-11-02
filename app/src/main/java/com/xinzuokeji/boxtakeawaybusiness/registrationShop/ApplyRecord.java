package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.FragmentViewPagerAdapter;
import com.xinzuokeji.boxtakeawaybusiness.me.Me;
import com.xinzuokeji.boxtakeawaybusiness.orderManagement.OrderManagement;
import com.xinzuokeji.boxtakeawaybusiness.pending.Pending;
import com.xinzuokeji.boxtakeawaybusiness.storeOperations.StoreOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/30.
 */
//暂时不用
public class ApplyRecord extends FragmentActivity {
    // ViewPager
    private ViewPager m_viewPager;
    // FragmentPagerAdapter
    private FragmentViewPagerAdapter m_adapter;
    // Fragment
    private List<Fragment> m_fragments;
    private Fragment m_fragment_communication;
    private Fragment m_fragment_record;
    private Fragment m_fragment_services;
    private Fragment m_fragment_me;

    // RadioGroup
    private RadioGroup rdg_m_bottom;

    //两次Back退出
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvnet();

    }

    // 初始化事件
    private void initEvnet() {
        // RadioGroup 状态切换事件
        rdg_m_bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            // 选择事件处理
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_main_communication:
                        m_viewPager.setCurrentItem(0);

                        break;
                    case R.id.rdb_main_record:
                        m_viewPager.setCurrentItem(1);

                        break;
                    case R.id.rdb_main_services:
                        m_viewPager.setCurrentItem(2);
                        break;
                    case R.id.rdb_main_me:
                        m_viewPager.setCurrentItem(3);

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

    // 联动事件处理
    private void pageSelect(int index) {
        // 重置按钮状态
        for (int i = 0; i < rdg_m_bottom.getChildCount(); i++) {
            RadioButton radButton = (RadioButton) rdg_m_bottom.getChildAt(i);
            radButton.setChecked(false);
            radButton.setTextColor(getResources()
                    .getColor(R.color.gray_dark));
        }

        // 选中RadioButton的状态
        if (rdg_m_bottom.getCheckedRadioButtonId() != index) {
            RadioButton radButton = (RadioButton) rdg_m_bottom
                    .getChildAt(index);
            radButton.setChecked(true);
            radButton.setTextColor(getResources().getColor(R.color.red));
        }
    }

    // 初始化视图
    private void initView() {
        // ViewPager
        m_viewPager = (ViewPager) findViewById(R.id.vp_main_viewPager);

        // 初始化ViewPager数据
        m_fragment_communication = new Pending();
        m_fragment_record = new OrderManagement();
        m_fragment_services = new StoreOperations();
        m_fragment_me = new Me();
        m_fragments = new ArrayList<>();
        m_fragments.add(m_fragment_communication);
        m_fragments.add(m_fragment_record);
        m_fragments.add(m_fragment_services);
        m_fragments.add(m_fragment_me);

        m_adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), m_viewPager, m_fragments);
        m_viewPager.setAdapter(m_adapter);

        // RadioGroup
        rdg_m_bottom = findViewById(R.id.rdg_main_vp);

        // 默认状态
        m_viewPager.setCurrentItem(0);
        pageSelect(0);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //调用FragmentMe中的方法
        m_fragment_me.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
