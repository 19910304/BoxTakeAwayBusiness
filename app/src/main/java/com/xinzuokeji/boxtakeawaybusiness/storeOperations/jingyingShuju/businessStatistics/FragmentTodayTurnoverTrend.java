package com.xinzuokeji.boxtakeawaybusiness.storeOperations.jingyingShuju.businessStatistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.xinzuokeji.boxtakeawaybusiness.R;

/**
 * Created by Administrator on 2018/8/7.
 */

public class FragmentTodayTurnoverTrend extends Fragment implements OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 加载布局
        View view = inflater.inflate(R.layout.fragment_today_turnover_trend, container, false);
        initView(view, inflater, container);
        initEvent(view);
        return view;
    }

    private void initView(View view, LayoutInflater inflater, ViewGroup container) {

    }

    private void initEvent(View view) {

    }

    @Override
    public void onClick(View view) {

    }
}
