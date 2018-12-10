package com.xinzuokeji.boxtakeawaybusiness.ceshi;

import android.os.Bundle;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

/**
 * Created by Administrator on 2018/7/6.
 */
//测试类
public class Ceshi extends BaseActivity {
    TextView tv_ceshi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ceshi);
        tv_ceshi.setText("");
    }
}
