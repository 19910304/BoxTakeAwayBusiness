package com.xinzuokeji.boxtakeawaybusiness.storeOperations.activeConfiguration;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

public class ActiveMeDe extends BaseActivity {
    private ImageButton header_back;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_active_mede);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("减满活动");//可变
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
