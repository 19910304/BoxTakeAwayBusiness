package com.xinzuokeji.boxtakeawaybusiness.storeOperations.commodityManage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.GoodsMenu;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2018/7/24.
 */
//选择菜单
public class ChoiceMenus extends BaseActivity {
    private ImageButton header_back;
    private List<GoodsMenu> mdateMenu;
    private ListViewAdapter<GoodsMenu> listViewAdapter;
    ListView lv_menu;
    NetService netService;
    String menu_name, menu_id;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_changeguige_list);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("选择菜单");
        lv_menu = findViewById(R.id.lv_guige);
        mdateMenu = new ArrayList<>();
        listViewAdapter = new ListViewAdapter<GoodsMenu>(this, R.layout.item_guige, mdateMenu) {
            @Override
            public void convert(ViewHolder holder, GoodsMenu goodsMenu, int id, int selectedPosition) {
                holder.setText(R.id.tv_guige_name, goodsMenu.menu_name);
            }
        };
        // 选择菜单
        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                menu_name = mdateMenu.get(i).menu_name;
                menu_id = mdateMenu.get(i).id;
                handler.sendEmptyMessage(2);

            }
        });
        lv_menu.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
        netService.showMenu(GetstoreId(), showMenu);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("menu_name", menu_name);
            bundle.putSerializable("menu_id", menu_id);
            data.putExtras(bundle);
            setResult(2, data);
            finish();
        }

    };


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

    // 菜单分类
    @SuppressLint("HandlerLeak")
    Handler showMenu = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj.toString().equals("[]")) {
                        mdateMenu.clear();
                        listViewAdapter.notifyDataSetChanged();
//                        Toast.makeText(CommodityManage.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    } else {
                        mdateMenu.clear();
                        mdateMenu.addAll((Collection<? extends GoodsMenu>) msg.obj);
                        listViewAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2001:
//                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
