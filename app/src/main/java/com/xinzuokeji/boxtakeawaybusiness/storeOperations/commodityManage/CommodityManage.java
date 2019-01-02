package com.xinzuokeji.boxtakeawaybusiness.storeOperations.commodityManage;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.PluginListAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.GoodsData;
import com.xinzuokeji.boxtakeawaybusiness.entities.GoodsMenu;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.ACache;
import com.xinzuokeji.boxtakeawaybusiness.util.DragListView;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;
import com.xinzuokeji.boxtakeawaybusiness.util.ViewUtil;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

//商品管理
public class CommodityManage extends BaseActivity {
    private ImageButton header_back;
    ListView lv_caidan1;
    ListViewAdapter<GoodsMenu> listViewAdapter1, listViewAdapter2;
    List<GoodsMenu> mdateMenu = new ArrayList<>(), mDate2 = new ArrayList<>();
    Button bt_new_shangpin, bt_caidan_bianji, bt_setting_position;
    //可拖拽的listview
    private DragListView mListView;
    private PluginListAdapter mAdapter;
    ArrayList<GoodsData> addedItem = new ArrayList<GoodsData>();
    //定义一个变量，默认值是false
    private boolean isManager;
    public NetService netService;
    //    菜单id
    public int menuid;
    private TextView header_edit1;
    private TextView tv_menus_name, tv_menus_dis;
    private List<String> itemShopId = new ArrayList<>();
    private String selectpostion = "0";
    ACache aCache;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_commodity_manage);
        netService = new NetService(this);
        aCache = ACache.get(CommodityManage.this);
        //头信息
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        header_edit1 = findViewById(R.id.header_edit1);
        header_edit1.setTextColor(getResources().getColor(R.color.white));
//        header_edit1.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setText("商品管理");
        bt_new_shangpin = findViewById(R.id.bt_new_shangpin);
        bt_caidan_bianji = findViewById(R.id.bt_caidan_bianji);
        // 调整位置
        bt_setting_position = findViewById(R.id.bt_setting_position);
        // 菜单分类
        lv_caidan1 = findViewById(R.id.lv_caidan1);
        //可拖拽的菜单列表
        mListView = findViewById(R.id.lv_caidan2);
        //分类的名字和描述
        tv_menus_name = findViewById(R.id.tv_menus_name);
        tv_menus_dis = findViewById(R.id.tv_menus_dis);
        // 菜单分类
        listViewAdapter1 = new ListViewAdapter<GoodsMenu>(this, R.layout.item_caidan1, mdateMenu) {
            @Override
            public void convert(ViewHolder holder, GoodsMenu goodsMenu, int id, int selectedPosition) {
                LinearLayout ll_good_menu = holder.getView(R.id.ll_good_menu);
                // 商品分类名
                TextView tvgoodMenu = holder.getView(R.id.tv_good_menu);
                tvgoodMenu.setText(goodsMenu.menu_name);
                //  选中背景变色
                if (selectedPosition == id) {
                    ll_good_menu.setBackgroundResource(R.color.white);
                } else {
                    ll_good_menu.setBackgroundResource(R.color.background_new);
                }

            }
        };

        lv_caidan1.setSelection(0);
        lv_caidan1.setAdapter(listViewAdapter1);
        listViewAdapter1.notifyDataSetChanged();
        lv_caidan1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectpostion = String.valueOf(position);
                menuid = Integer.parseInt(mdateMenu.get(position).id);
                aCache.put("menu_position", selectpostion);
                aCache.put("menuid", mdateMenu.get(position).id);
                listViewAdapter1.clearSelection(position);
                listViewAdapter1.notifyDataSetChanged();
                // 分类的名字
                tv_menus_name.setText(mdateMenu.get(position).menu_name);
                //分类的描述
                tv_menus_dis.setText(mdateMenu.get(position).describe);
                netService.showGoodsData(GetstoreId(), Integer.parseInt(mdateMenu.get(position).id), showGoodsData);
            }
        });

        mListView.setDragItemListener(new DragListView.SimpleAnimationDragItemListener() {
            private Rect mFrame = new Rect();
            private boolean mIsSelected;

            @Override
            public boolean canDrag(View dragView, int x, int y) {
                // 获取可拖拽的图标
                View dragger = dragView.findViewById(R.id.dl_plugin_move);
                if (dragger == null || dragger.getVisibility() != View.VISIBLE) {
                    return false;
                }
                float tx = x - ViewUtil.getX(dragView);
                float ty = y - ViewUtil.getY(dragView);
                dragger.getHitRect(mFrame);
                if (mFrame.contains((int) tx, (int) ty)) { // 当点击拖拽图标才可进行拖拽
                    return true;
                }
                return false;
            }


            @Override
            public void beforeDrawingCache(View dragView) {
                mIsSelected = dragView.isSelected();
                View drag = dragView.findViewById(R.id.dl_plugin_move);
                dragView.setSelected(true);
                if (drag != null) {
                    drag.setSelected(true);
                }
            }

            @Override
            public Bitmap afterDrawingCache(View dragView, Bitmap bitmap) {
                dragView.setSelected(mIsSelected);
                View drag = dragView.findViewById(R.id.dl_plugin_move);
                if (drag != null) {
                    drag.setSelected(false);
                }
                return bitmap;
            }

            @Override
            public boolean canExchange(int srcPosition, int position) {
                boolean result = mAdapter.exchange(srcPosition, position);
                return result;
            }
        });
        //推拽菜单
        mAdapter = new PluginListAdapter(this, addedItem, null, null);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        netService.showMenu(GetstoreId(), showMenu);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_new_shangpin.setOnClickListener(this);
        bt_caidan_bianji.setOnClickListener(this);
        bt_setting_position.setOnClickListener(this);
        header_edit1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.header_edit1://保存拖拽菜单
                List<String> goodsNewArray = new ArrayList<>();
                for (int i = 0; i < addedItem.size(); i++) {
                    String goodid = addedItem.get(i).goods_id;
                    goodsNewArray.add(i, goodid);

                }
                // 不等于0才可以保存itemShopId（初始商品id）
                if (itemShopId.size() != 0) {
                    netService.updGoodsSort(itemShopId, goodsNewArray, updGoodsSort);
                }

                break;
            case R.id.bt_setting_position://点击可调整位置
                isManager = !isManager;
                bt_setting_position.setText(isManager ? "取消" : "调整位置");
                //为自定义方法--控制另外一个变量
                mAdapter.changetShowDelImage(isManager);
                if (bt_setting_position.getText().toString().equals("调整位置")) {
                    header_edit1.setVisibility(View.GONE);
                } else {
                    header_edit1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.bt_new_shangpin://新建商品
                aCache.put("menu_position", selectpostion);
                if (mdateMenu.size() != 0) {
                    HashMap<String, String> extras = new HashMap<>();
                    extras.put("menuid", String.valueOf(menuid));
                    extras.put("menu_position", selectpostion);
                    try {
                        gotoActivity(NewShangpin.class.getName(), extras);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    showTip("您还没有商品菜单哦", Toast.LENGTH_SHORT);
                }

                break;
            case R.id.bt_caidan_bianji://编辑菜单
                aCache.put("menu_position", selectpostion);
                Intent intent = new Intent(this, CaidanBianji.class);
                intent.putExtra("mdateMenu", (Serializable) mdateMenu);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String menu_position = aCache.getAsString("menu_position");
                String menuid = aCache.getAsString("menuid");
                if (Valid.isNotNullOrEmpty(menu_position) && Valid.isNotNullOrEmpty(menuid)) {
                    lv_caidan1.setSelection(Integer.parseInt(menu_position));
                    lv_caidan1.setAdapter(listViewAdapter1);
                    listViewAdapter1.notifyDataSetChanged();
                    tv_menus_name.setText(mdateMenu.get(Integer.parseInt(selectpostion)).menu_name);
                    tv_menus_dis.setText(mdateMenu.get(Integer.parseInt(selectpostion)).describe);
                    netService.showGoodsData(GetstoreId(), Integer.parseInt(menuid), showGoodsData);
                }

            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String caidan = aCache.getAsString("caidan");
                if (Valid.isNotNullOrEmpty(caidan)) {
                    netService.showMenu(GetstoreId(), showMenuRestart);
                }

            }
        });

    }

    //新建完商品刷新商品
    public static class MyMessageNewGoods extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ACache aCache = ACache.get(context);
            String menuid = aCache.getAsString("menuid");
            if (Valid.isNotNullOrEmpty(menuid)) {
                //   netService.showGoodsData(GetstoreId(), Integer.parseInt(menuid), showGoodsData);
            }
        }
    }

    // 调整商品顺序
    @SuppressLint("HandlerLeak")
    Handler updGoodsSort = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    mAdapter.changetShowDelImage(false);
                    header_edit1.setVisibility(View.GONE);
                    bt_setting_position.setText("调整位置");
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
                    showTip(getString(R.string.action_pick), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    // 显示对应菜单下的商品
    private final ShowGoodsData showGoodsData = new ShowGoodsData(this);

    private static class ShowGoodsData extends Handler {
        private final WeakReference<CommodityManage> mActivity;

        private ShowGoodsData(CommodityManage activity) {
            mActivity = new WeakReference<CommodityManage>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CommodityManage activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 2000:
                        if (msg.obj.toString().equals("[]")) {
                            activity.addedItem.clear();
                            // 重新选择菜单必须清空商品id
                            activity.itemShopId.clear();
                            activity.mAdapter.notifyDataSetChanged();
//                        Toast.makeText(CommodityManage.this, "暂无数据", Toast.LENGTH_SHORT).show();
                        } else {
                            activity.addedItem.clear();
                            activity.addedItem.addAll((Collection<? extends GoodsData>) msg.obj);
                            activity.mAdapter.notifyDataSetChanged();
                            activity.itemShopId.clear();
                            for (int i = 0; i < activity.addedItem.size(); i++) {
                                String goodid = activity.addedItem.get(i).goods_id;
                                activity.itemShopId.add(i, goodid);
                            }
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
        }
    }

    // 返回的菜单分类
    @SuppressLint("HandlerLeak")
    Handler showMenuRestart = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj.toString().equals("[]")) {
                        mdateMenu.clear();
                        listViewAdapter1.notifyDataSetChanged();
                    } else {
                        String menu_position = aCache.getAsString("menu_position");
                        String menuid = aCache.getAsString("menuid");
                        mdateMenu.clear();
                        mdateMenu.addAll((Collection<? extends GoodsMenu>) msg.obj);
                        // 刚进来显示菜单名字和描述
                        if (mdateMenu.size() != 0) {
                            if (Valid.isNotNullOrEmpty(menu_position)) {
                                tv_menus_name.setText(mdateMenu.get(Integer.parseInt(menu_position)).menu_name);
                                tv_menus_dis.setText(mdateMenu.get(Integer.parseInt(menu_position)).describe);
                            } else {
                                tv_menus_name.setText(mdateMenu.get(0).menu_name);
                                tv_menus_dis.setText(mdateMenu.get(0).describe);
                                // 删除分类之后默认选中第一项
                                listViewAdapter1.clearSelection(0);
                                netService.showGoodsData(GetstoreId(), Integer.parseInt(mdateMenu.get(0).id), showGoodsData);
                            }
                        }

                        if (Valid.isNotNullOrEmpty(menu_position) && Valid.isNotNullOrEmpty(menuid)) {
                            lv_caidan1.setSelection(Integer.parseInt(menu_position));
                            netService.showGoodsData(GetstoreId(), Integer.parseInt(menuid), showGoodsData);
                        }
                        lv_caidan1.setAdapter(listViewAdapter1);
                        listViewAdapter1.notifyDataSetChanged();
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
    // 菜单分类
    @SuppressLint("HandlerLeak")
    Handler showMenu = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj.toString().equals("[]")) {
                        mdateMenu.clear();
                        listViewAdapter1.notifyDataSetChanged();
//                        Toast.makeText(CommodityManage.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    } else {
                        mdateMenu.clear();
                        mdateMenu.addAll((Collection<? extends GoodsMenu>) msg.obj);
                        // 刚进来显示菜单名字和描述
                        if (mdateMenu.size() != 0) {
                            tv_menus_name.setText(mdateMenu.get(0).menu_name);
                            tv_menus_dis.setText(mdateMenu.get(0).describe);
                            //第一次进来设置默认第一条的menuid
                            aCache.put("menuid", mdateMenu.get(0).id);
                        }
                        listViewAdapter1.notifyDataSetChanged();
                        netService.showGoodsData(GetstoreId(), Integer.parseInt(mdateMenu.get(0).id), showGoodsData);
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


    /**
     * 点击已添加／未添加按钮
     */
    public class OnStateBtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(final View v) {
            GoodsData item = (GoodsData) v.getTag();
            if (item == null) {
                return;
            }
            if (mAdapter.isAdded(item)) { // 已添加
                mAdapter.removeItem(item);
            } else {
                mAdapter.addItem(item);
            }
        }
    }
}
