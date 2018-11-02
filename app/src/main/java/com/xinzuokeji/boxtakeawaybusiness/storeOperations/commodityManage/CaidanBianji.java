package com.xinzuokeji.boxtakeawaybusiness.storeOperations.commodityManage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.GoodsMenu;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.ACache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//管理菜单
public class CaidanBianji extends BaseActivity {
    private ImageButton header_back;
    ListView listView;
    ListViewAdapter<GoodsMenu> listViewAdapter;
    List<GoodsMenu> mDate;
    //新增
    TextView addMenu;
    public static Activity mActivity;
    NetService netService;

    @Override

    public void initView() {
        super.initView();
        mActivity = this;
        netService = new NetService(this);
        setContentView(R.layout.activity_caidan_bianji);
//        mDate = (List<GoodsMenu>) getIntent().getSerializableExtra("mdateMenu");
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("管理菜单");
        // 新增商品
        addMenu = findViewById(R.id.header_edit1);
        addMenu.setVisibility(View.VISIBLE);
        addMenu.setText("新增");
        listView = findViewById(R.id.lv_caidan_bianji);
        mDate = new ArrayList<>();
        listViewAdapter = new ListViewAdapter<GoodsMenu>(this, R.layout.item_bianjicaidan, mDate) {
            @Override
            public void convert(ViewHolder holder, final GoodsMenu goodsMenu, final int id, int selectedPosition) {
                TextView tv_caipin = holder.getView(R.id.tv_caipin);
                tv_caipin.setText(goodsMenu.menu_name);
                TextView tv_shangpin_number = holder.getView(R.id.tv_shangpin_number);
                tv_shangpin_number.setText(goodsMenu.goods_count + "个商品");
                Button bt_bianji_caidan = holder.getView(R.id.bt_bianji_caidan);
                // 跳转到编辑菜单页面
                bt_bianji_caidan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CaidanBianji.this, CaidanBiande.class);
//                        intent.putExtra("mdateMenu", (Serializable) mDate);
                        intent.putExtra("menu_id", goodsMenu.id);
                        intent.putExtra("menu_name", goodsMenu.menu_name);
                        intent.putExtra("describe", goodsMenu.describe);
                        startActivity(intent);

                    }
                });
                //隐藏
                Button bt_new_shpin = holder.getView(R.id.bt_new_shpin);
                bt_new_shpin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };
        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();

        netService.showMenu(GetstoreId(), showMenu);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        addMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                ACache aCache = ACache.get(this);
                aCache.put("caidan", "caidan");
                finish();
                break;
            case R.id.header_edit1://新增菜单
                Intent intent = new Intent(this, AddMenu.class);
                intent.putExtra("mdateMenu", (Serializable) mDate);
                startActivity(intent);
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
                        mDate.clear();
                        listViewAdapter.notifyDataSetChanged();
                    } else {
                        mDate.clear();
                        mDate.addAll((Collection<? extends GoodsMenu>) msg.obj);
                        listViewAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
