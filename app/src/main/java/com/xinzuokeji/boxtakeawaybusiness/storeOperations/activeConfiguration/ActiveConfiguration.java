package com.xinzuokeji.boxtakeawaybusiness.storeOperations.activeConfiguration;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.util.Utility;

import java.util.ArrayList;
import java.util.List;

//活动配置
public class ActiveConfiguration extends BaseActivity {
    private ImageButton header_back;
    ListView lv_active, lv_me_active;
    ListViewAdapter<String> mAdpter;
    List<String> mDate = new ArrayList<>();
    //我的活动列表
    ListViewAdapter<String> mAdpterMe;
    List<String> mDateMe = new ArrayList<>();
    RadioGroup rdg_active;
    RadioButton rdb_new, rdb_me;

    RadioGroup rdg_active_me;
    RadioButton rdg_daikaishi, rdg_jingixng, rdg_jiehsu;
    LinearLayout ll_me;
    LinearLayout ll_header_back;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_active_configurrtion);
        header_back = findViewById(R.id.header_back);
        ll_header_back = findViewById(R.id.ll_header_back);

        rdg_active = findViewById(R.id.rdg_active);
        rdb_new = findViewById(R.id.rdb_new);
        rdb_me = findViewById(R.id.rdb_me);
        lv_active = findViewById(R.id.lv_active);
        lv_me_active = findViewById(R.id.lv_me_active);

        ll_me = findViewById(R.id.ll_me);
        rdg_active.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_new:
                        lv_active.setVisibility(View.VISIBLE);
                        lv_me_active.setVisibility(View.GONE);
                        ll_me.setVisibility(View.GONE);
                        break;
                    case R.id.rdb_me:
                        lv_active.setVisibility(View.GONE);
                        lv_me_active.setVisibility(View.VISIBLE);
                        ll_me.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
//        点击我的显示
        rdg_active_me = findViewById(R.id.rdg_active_me);
        rdg_daikaishi = findViewById(R.id.rdg_daikaishi);
        rdg_jingixng = findViewById(R.id.rdg_jingixng);
        rdg_jiehsu = findViewById(R.id.rdg_jiehsu);
        rdg_active_me.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdg_daikaishi:
//                        Toast.makeText(ActiveConfiguration.this, "33", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rdg_jingixng:
                        Toast.makeText(ActiveConfiguration.this, "我的", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rdg_jiehsu:
                        Toast.makeText(ActiveConfiguration.this, "新建", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
        mDate.add(0, "");
        mDate.add(1, "");

        mAdpter = new ListViewAdapter<String>(this, R.layout.item_active, mDate) {
            @Override
            public void convert(ViewHolder holder, String s, int id, int selectedPosition) {
                ImageView lv_active = holder.getView(R.id.lv_active);
                TextView tv_active = holder.getView(R.id.tv_active);
//                tv_active.setText("");
                lv_active.setImageBitmap(Utility.getBitmapByText(tv_active.getText().toString().trim().substring(0, 1), 24, 24, "#6C9CFF", "#FFFFFF"));


            }
        };
        lv_active.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    gotoActivity(ActiveConDe.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        lv_active.setAdapter(mAdpter);
        mAdpter.notifyDataSetChanged();

//        我的活动列表
        mDateMe.add(0, "");
        mDateMe.add(1, "");

        mAdpterMe = new ListViewAdapter<String>(this, R.layout.item_active_me, mDateMe) {
            @Override
            public void convert(ViewHolder holder, String s, int id, int selectedPosition) {
                TextView tv_event_details = holder.getView(R.id.tv_event_details);
                tv_event_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            gotoActivity(ActiveMeDe.class.getName(), null);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        };
        lv_me_active.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    gotoActivity(ActiveConDe.class.getName(), null);
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
            }
        });
        lv_me_active.setAdapter(mAdpterMe);
        mAdpterMe.notifyDataSetChanged();
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ll_header_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_header_back:
                finish();
                break;
            default:
                break;
        }
    }

    // 监听返回按键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            //绑定返回按键和返回按钮
            LinearLayout header_back = findViewById(R.id.ll_header_back);
            if (header_back != null) {
                header_back.performClick();
            }

            return true;
        }
        return super.dispatchKeyEvent(event);
    }

}
