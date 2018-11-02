package com.xinzuokeji.boxtakeawaybusiness.storeOperations.advertisement;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

//广告推送
public class Avertisement extends BaseActivity {
    private ImageButton header_back;
    ListView listView;
    List<String> mDate = new ArrayList<>();
    ListViewAdapter<String> mAdapter;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_avertisement);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("广告推送");
        listView = findViewById(R.id.lv_avertisement);
        mDate.add(0, "");
        mDate.add(1, "");
        mAdapter = new ListViewAdapter<String>(this, R.layout.item_avertisement, mDate) {
            @Override
            public void convert(ViewHolder holder, String s, int id, int selectedPosition) {

            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    gotoActivity(AvertisementDe.class.getName(),null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        listView.setAdapter(mAdapter);
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
