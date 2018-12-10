package com.xinzuokeji.boxtakeawaybusiness.me;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.MainActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.AllShop;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

//门店切换
public class StoreSetting extends BaseActivity {
    private ImageButton header_back;
    ListViewAdapter<AllShop> mAdapter;
    ArrayList<AllShop> list = new ArrayList<AllShop>();
    ListView listView;
    NetService netService;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_store_setting);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);

        TextView textView = findViewById(R.id.header_title);
        textView.setText("切换门店");
        listView = findViewById(R.id.lv_store_setting);
        mAdapter = new ListViewAdapter<AllShop>(this, R.layout.item_lanyadayin, list) {
            @Override
            public void convert(ViewHolder holder, AllShop allShop, int id, int selectedPosition) {

                TextView tv_lanya = holder.getView(R.id.tv_lanya);
                tv_lanya.setText(allShop.shop_name);
                ImageView im_xuanzhong_lanya = holder.getView(R.id.im_xuanzhong_lanya);
                if (selectedPosition == id) {
                    im_xuanzhong_lanya.setVisibility(View.VISIBLE);
                } else {
                    im_xuanzhong_lanya.setVisibility(View.GONE);
                }
                if (String.valueOf(GetstoreId()).equals(allShop.id)) {
                    im_xuanzhong_lanya.setVisibility(View.VISIBLE);
                } else {
                    im_xuanzhong_lanya.setVisibility(View.GONE);
                }
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  切换门店
                SetstoreId(Integer.parseInt(list.get(position).id));
                MainActivity.mActivity.finish();
                try {
                    HashMap<String, String> extras = new HashMap<>();
                    extras.put("class_name", "StoreSetting");
                    gotoActivityAndFinish(MainActivity.class.getName(), extras);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                mAdapter.clearSelection(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        netService.allShop(getuserinfoId(), allShop);
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

    @SuppressLint("HandlerLeak")
    Handler allShop = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        list.clear();
                        list.addAll((Collection<? extends AllShop>) msg.obj);
                        mAdapter.notifyDataSetChanged();
                    }

                    break;
                case 2001:

                    break;
                case 1001:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
