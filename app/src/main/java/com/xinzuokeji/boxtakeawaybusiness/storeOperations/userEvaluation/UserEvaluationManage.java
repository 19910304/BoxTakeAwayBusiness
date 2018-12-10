package com.xinzuokeji.boxtakeawaybusiness.storeOperations.userEvaluation;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/8/3.
 */
//评价管理
public class UserEvaluationManage extends BaseActivity {
    private ImageButton header_back;
    private LinearLayout ll_user_evaluation, ll_rider_evaluation;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_user_evaluation_manage);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("评价管理");
        ll_user_evaluation = findViewById(R.id.ll_user_evaluation);
        ll_rider_evaluation = findViewById(R.id.ll_rider_evaluation);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_user_evaluation.setOnClickListener(this);
        ll_rider_evaluation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_user_evaluation://用户评价
                HashMap<String, String> extrasUser = new HashMap<>();
                extrasUser.put("type", "user");
                try {
                    gotoActivity(UserEvaluation.class.getName(), extrasUser);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_rider_evaluation://骑手评价
                HashMap<String, String> extrasRider = new HashMap<>();
                extrasRider.put("type", "rider");
                try {
                    gotoActivity(UserEvaluation.class.getName(), extrasRider);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
