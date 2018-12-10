package com.xinzuokeji.boxtakeawaybusiness.pending;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;


//退款原因
public class RefundReason extends BaseActivity {
    private ImageButton header_back;
    ListView lv_refund_reason;
    ListViewAdapter<String> mAdapter;
    List<String> mDate;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_refund_reason);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        header_back.setBackgroundResource(R.mipmap.back_bl);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("选择退款原因");
        lv_refund_reason = findViewById(R.id.lv_refund_reason);
        mDate = new ArrayList<>();
        mDate.add(0, "");
        mDate.add(1, "");
        mDate.add(2, "");
        mAdapter = new ListViewAdapter<String>(this, R.layout.item_refund_reason, mDate) {
            @Override
            public void convert(ViewHolder holder, String s, int id, int selectedPosition) {
                TextView tv_refund_reason = holder.getView(R.id.tv_refund_reason);

            }
        };
        lv_refund_reason.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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
