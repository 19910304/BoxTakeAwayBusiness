package com.xinzuokeji.boxtakeawaybusiness.storeOperations.jingyingShuju;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.util.WaterWaveView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/4.
 */
//商家体检
public class MerchantsCheckUp extends BaseActivity {
    private ImageButton header_back;
    GridView gv_problem;
    ListViewAdapter<String> listAdapterProblem;
    List<String> mDateProblem;
    GridView gv_no_problem;
    ListViewAdapter<String> listAdapterNoProblem;
    List<String> mDateNoProblem;
    private WaterWaveView mWaterWaveView;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_merchants_check_up);

        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        // header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("商家体检");
        //流量图
        mWaterWaveView = (WaterWaveView) findViewById(R.id.wave_view);
        mWaterWaveView.setmWaterLevel(0.5F);//水波高度

        mWaterWaveView.startWave();

        gv_problem = findViewById(R.id.gv_problem);
        gv_no_problem = findViewById(R.id.gv_no_problem);
        mDateProblem = new ArrayList<>();
        mDateProblem.add(0, "");
        mDateProblem.add(1, "");
        mDateProblem.add(2, "");
        listAdapterProblem = new ListViewAdapter<String>(this, R.layout.item_problem, mDateProblem) {
            @Override
            public void convert(ViewHolder holder, String s, int id, int selectedPosition) {

            }
        };
        gv_problem.setAdapter(listAdapterProblem);
        listAdapterProblem.notifyDataSetChanged();
        mDateNoProblem = new ArrayList<>();
        mDateNoProblem.add(0, "");
        mDateNoProblem.add(1, "");
        mDateNoProblem.add(2, "");
        listAdapterNoProblem = new ListViewAdapter<String>(this, R.layout.item_problem, mDateNoProblem) {
            @Override
            public void convert(ViewHolder holder, String s, int id, int selectedPosition) {

            }
        };
        gv_no_problem.setAdapter(listAdapterNoProblem);
        listAdapterNoProblem.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        mWaterWaveView.stopWave();
        mWaterWaveView = null;
        super.onDestroy();
    }
}
