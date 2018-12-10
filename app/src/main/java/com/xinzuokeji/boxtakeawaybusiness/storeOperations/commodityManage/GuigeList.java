package com.xinzuokeji.boxtakeawaybusiness.storeOperations.commodityManage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.GuiGe;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.IosAlertDialog;
import com.xinzuokeji.boxtakeawaybusiness.util.Utility;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/7/23.
 */
//规格列表
public class GuigeList extends BaseActivity {
    private ImageButton header_back;
    private TextView add_guige;
    private List<GuiGe> mDate;
    private ListViewAdapter<GuiGe> listViewAdapter;
    SwipeMenuListView lv_guige;
    NetService netService;
    String goodid = "0";
    private String guigeId;
    private String typeChange = "";
    private String msg;
    // 删除的position
    int delPosition;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_changeguige_list);
        goodid = getIntent().getStringExtra("good_id");
        if (!Valid.isNotNullOrEmpty(goodid)) {
            goodid = "0";
        }
        typeChange = getIntent().getStringExtra("list_type");
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        if (typeChange.equals("GuigeList")) {
            textView.setText("商品规格");
            msg = "最多增加10个规格";
        } else {
            textView.setText("商品口味");
            msg = "最多增加10个口味";
        }

        add_guige = findViewById(R.id.header_edit1);
        add_guige.setVisibility(View.VISIBLE);
        add_guige.setText("新增");
        lv_guige = findViewById(R.id.lv_guige);
        // 添加删除布局
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
//                // set item width
//                openItem.setWidth(Utility.dip2px(GuigeList.this, 90));
//                // set item title
//                openItem.setTitle("Open");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(Utility.dip2px(GuigeList.this, 90));
                // set a icon
//                deleteItem.setIcon(R.mipmap.ic_launcher);
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
//                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        lv_guige.setMenuCreator(creator);
        mDate = new ArrayList<>();
        listViewAdapter = new ListViewAdapter<GuiGe>(this, R.layout.item_guige, mDate) {
            @Override
            public void convert(ViewHolder holder, GuiGe guiGe, int id, int selectedPosition) {
                if (typeChange.equals("GuigeList")) {
                    holder.setText(R.id.tv_guige_name, guiGe.norms);
                } else {
                    holder.setText(R.id.tv_guige_name, guiGe.name);
                }

            }
        };
        // 修改规格
        lv_guige.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (typeChange.equals("GuigeList")) {
                    Intent intent = new Intent(GuigeList.this, ChangeGuige.class);
                    intent.putExtra("guige", mDate.get(i));
                    intent.putExtra("good_id", goodid);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(GuigeList.this, ChangeTaste.class);
                    intent.putExtra("guige", mDate.get(i));
                    intent.putExtra("good_id", goodid);
                    intent.putExtra("attribute_id", mDate.get(i).id);
                    startActivity(intent);
                }


            }
        });
        //长按删除
        lv_guige.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                IosAlertDialog alertDialog = new IosAlertDialog(GuigeList.this);
                alertDialog.builder();
                alertDialog.setCancelable(true);
                alertDialog.setMsg("确定删除");
                alertDialog.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                alertDialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delPosition = position;
                        if (typeChange.equals("GuigeList")) {
                            //删除规格
                            netService.deleteNorms(Integer.parseInt(goodid), Integer.parseInt(mDate.get(position).id), Hander_deleteAttribute);
                        } else {
                            netService.deleteAttribute(Integer.parseInt(mDate.get(position).id), Hander_deleteAttribute);
                        }

                    }
                });
                alertDialog.show();
                return true;
            }
        });
        // 侧滑显示删除
        lv_guige.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
//                    case 1:
//                        // open
//                        break;
                    case 0:
                        // delete
                        delPosition = position;
                        if (typeChange.equals("GuigeList")) {
                            //删除规格
                            netService.deleteNorms(Integer.parseInt(goodid), Integer.parseInt(mDate.get(position).id), Hander_deleteAttribute);
                        } else {
                            netService.deleteAttribute(Integer.parseInt(mDate.get(position).id), Hander_deleteAttribute);
                        }
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        // Right
        lv_guige.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        // Left
        lv_guige.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        lv_guige.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        HandlerThread mHandlerThread = new HandlerThread("handler-thread");
        mHandlerThread.start();
        Handler mHandler = new Handler(mHandlerThread.getLooper()) {
            public void handleMessage(Message msg) {
                if (typeChange.equals("GuigeList")) {
                    netService.showNormsNew(Integer.parseInt(goodid), showNormsNew);
                } else {
                    netService.showAttribute(Integer.parseInt(goodid), showNormsNew);
                }


            }
        };
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        add_guige.setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("guige_id", guigeId);
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
            finish();
        }

    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                if (typeChange.equals("GuigeList")) {
                    //   规格个数不等于0拼接规格id
                    if (mDate != null && mDate.size() != 0) {
                        for (int i = 0; i < mDate.size(); i++) {
                            if (Valid.isNotNullOrEmpty(guigeId)) {
                                guigeId = guigeId + "," + mDate.get(i).id;
                            } else {
                                guigeId = mDate.get(i).id;
                            }
                        }
                    }
                    handler.sendEmptyMessage(0);
                } else {
                    finish();
                }


                break;
            case R.id.header_edit1:
                if (mDate.size() >= 10) {
                    showTip(msg, Toast.LENGTH_SHORT);
                } else {
                    if (typeChange.equals("GuigeList")) {
                        //新增规格
                        HashMap<String, String> extras = new HashMap<>();
                        extras.put("good_id", String.valueOf(goodid));
                        try {
                            gotoActivity(ChangeGuige.class.getName(), extras);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //新增口味
                        HashMap<String, String> extras = new HashMap<>();
                        extras.put("good_id", String.valueOf(goodid));
                        extras.put("attribute_id", "0");
                        try {
                            gotoActivity(ChangeTaste.class.getName(), extras);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                }

                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler showNormsNew = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj.toString().equals("[]")) {
                        mDate.clear();
                        listViewAdapter.notifyDataSetChanged();
                    } else {
                        mDate.clear();
                        mDate.addAll((Collection<? extends GuiGe>) msg.obj);
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
    @SuppressLint("HandlerLeak")
    Handler Hander_deleteAttribute = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    // 删除成功刷新列表
                    mDate.remove(delPosition);
                    lv_guige.setAdapter(listViewAdapter);
                    listViewAdapter.notifyDataSetChanged();
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
