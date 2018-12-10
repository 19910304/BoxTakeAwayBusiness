package com.xinzuokeji.boxtakeawaybusiness.orderManagement;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

/**
 * Created by Administrator on 2018/9/3.
 */
//订单搜索
public class SearchOrder extends BaseActivity {
    private ImageButton header_back;
    private TextView tvHeaderSearch;
    private EditText etOrderSearch;
    private String searchContent;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_search_order);
        header_back = findViewById(R.id.header_back);
        // 搜索按钮
        tvHeaderSearch = findViewById(R.id.tv_header_search);
        etOrderSearch = findViewById(R.id.et_order_search);
        etOrderSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //搜索结果
                searchContent = etOrderSearch.getText().toString();
            }
        });
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        tvHeaderSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.tv_header_search://后台获取结果
                showTip("没有该订单哦", Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }
    }
}
