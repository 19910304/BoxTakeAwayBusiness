package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.Shoptype;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.ACache;
import com.xinzuokeji.boxtakeawaybusiness.util.Utility;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2018/6/12.
 */
//选择门店分类
public class SelectorStoreType extends BaseActivity {
    private ImageButton header_back;
    private RecyclerView recyclerView;
    //
    private List<com.xinzuokeji.boxtakeawaybusiness.entities.Shoptype> mDateStoretype;
    NetService netService;
    ItemAdapter itemAdapter;

    //显示经营类型
    private ListViewAdapter<com.xinzuokeji.boxtakeawaybusiness.entities.Shoptype> ShoptypelistViewAdapter;
    private List<com.xinzuokeji.boxtakeawaybusiness.entities.Shoptype> mDateShoptype;
    ListView lv_manage_type;
    private Button bt_sure_store_type;
    String typeid;
    ACache aCache;
    String typename;//经营名字

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_selector_store_type);
        // 将经营id和经营名字缓存
        aCache = ACache.get(this);
        netService = new NetService(this);
        typename = getIntent().getStringExtra("typeName");
        typeid = getIntent().getStringExtra("manage_type_id");
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("选择门店分类");
        textView.setTextColor(getResources().getColor(R.color.white));
        // 确认
        bt_sure_store_type = findViewById(R.id.bt_sure_store_type);
        recyclerView = findViewById(R.id.rv_store_style);
        LinearLayoutManager ms = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(ms);//设置布局管理器，
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
        recyclerView.addItemDecoration(new SpaceItemDecoration(5));//设置子条目距离
        mDateStoretype = new ArrayList<>();
        itemAdapter = new ItemAdapter(mDateStoretype, this);//添加适配器，这里适配器刚刚装入了数据
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
        //获取店铺类型
        netService.showShoptype(Shoptype);
        //经营分类
        lv_manage_type = findViewById(R.id.lv_manage_type);
        mDateShoptype = new ArrayList<>();
        ShoptypelistViewAdapter = new ListViewAdapter<com.xinzuokeji.boxtakeawaybusiness.entities.Shoptype>(this, R.layout.item_shop_type, mDateShoptype) {
            @Override
            public void convert(ViewHolder holder, Shoptype shoptype, final int id, int selectedPosition) {
                //  分类标题
                TextView tv_shop_type = holder.getView(R.id.tv_shop_type);
                tv_shop_type.setText(shoptype.typename);
                //   选择的内容
                com.xinzuokeji.boxtakeawaybusiness.util.MyGridView gv_manage_type = holder.getView(R.id.gv_manage_type);
                List<com.xinzuokeji.boxtakeawaybusiness.entities.Sanji> mDatesan = new ArrayList<>();
                mDatesan = shoptype.Sanji;
                final List<com.xinzuokeji.boxtakeawaybusiness.entities.Sanji> finalMDatesan = mDatesan;
                ListViewAdapter<com.xinzuokeji.boxtakeawaybusiness.entities.Sanji> sanjiListViewAdapter = new ListViewAdapter<com.xinzuokeji.boxtakeawaybusiness.entities.Sanji>(SelectorStoreType.this, R.layout.item_manage_type, finalMDatesan) {
                    @Override
                    public void convert(ViewHolder holder, final com.xinzuokeji.boxtakeawaybusiness.entities.Sanji sanji, final int id, int selectedPosition) {
                        final Button bt_manage_type_name = holder.getView(R.id.bt_manage_type_name);
                        bt_manage_type_name.setText(sanji.typename);
                        //如果第一次和上页类型一样则显示红色
                        if (finalMDatesan.get(id).id.equals(aCache.getAsString("manage_type_id"))) {
                            bt_manage_type_name.setTextColor(getResources().getColor(R.color.red));
                        } else {
                            bt_manage_type_name.setTextColor(getResources().getColor(R.color.gray_dark));
                        }
                        bt_manage_type_name.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bt_manage_type_name.setTextColor(getResources().getColor(R.color.gray_dark));
                                // 将经营id和经营名字缓存
                                String manage_type_id = finalMDatesan.get(id).id;//经营类型id
                                String typename = finalMDatesan.get(id).typename;//经营类型名字
                                aCache.put("manage_type_id", manage_type_id);
                                aCache.put("typename", typename);
                                if (finalMDatesan.get(id).id.equals(aCache.getAsString("manage_type_id"))) {
                                    bt_manage_type_name.setTextColor(getResources().getColor(R.color.red));
//                                    Log.i("name", aCache.getAsString("manage_type_id") + "---");
                                } else {
                                    bt_manage_type_name.setTextColor(getResources().getColor(R.color.gray_dark));
                                }
                                // 刷新适配器，使选中的为红色
                                ShoptypelistViewAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                };
                gv_manage_type.setAdapter(sanjiListViewAdapter);
                sanjiListViewAdapter.notifyDataSetChanged();
                //  流式布局
//                final com.zhy.view.flowlayout.TagFlowLayout tagFlowLayout = holder.getView(R.id.id_flowlayout);
//                final LayoutInflater mInflater = LayoutInflater.from(SelectorStoreType.this);
//                final List<String> tagFlowDate = new ArrayList<>();
//                final List<String> tagFlowDateid = new ArrayList<>();
//                for (int i = 0; i < finalMDatesan.size(); i++) {
//                    tagFlowDate.add(finalMDatesan.get(i).typename);
//                }
//                for (int i = 0; i < finalMDatesan.size(); i++) {
//                    tagFlowDateid.add(finalMDatesan.get(i).id);
//                }
//                tagFlowLayout.setAdapter(new com.zhy.view.flowlayout.TagAdapter<String>(tagFlowDate) {
//                    @Override
//                    public View getView(com.zhy.view.flowlayout.FlowLayout parent, final int position, String shoptype) {
////                              将tv.xml文件填充到标签内.
//                        final TextView tv = (TextView) mInflater.inflate(R.layout.item_manage_type_tag,
//                                tagFlowLayout, false);
////                              为标签设置对应的内容
//                        //如果第一次和上页类型一样则显示红色
//                        if (tagFlowDateid.get(position).equals(aCache.getAsString("manage_type_id"))) {
//                            tv.setTextColor(getResources().getColor(R.color.red));
//                        } else {
//                            tv.setTextColor(getResources().getColor(R.color.gray_dark));
//                        }
//                        tv.setText(shoptype);
//                        tv.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                tv.setTextColor(getResources().getColor(R.color.gray_dark));
////                                // 将经营id和经营名字缓存
//                                String manage_type_id = tagFlowDateid.get(position);//经营类型id
//                                String typename = tagFlowDate.get(position);//经营类型名字
//                                aCache.put("manage_type_id", manage_type_id);
//                                aCache.put("typename", typename);
//                                if (tagFlowDateid.get(position).equals(aCache.getAsString("manage_type_id"))) {
//                                    tv.setTextColor(getResources().getColor(R.color.red));
////                                    Log.i("name", aCache.getAsString("manage_type_id") + "---");
//                                    Log.i("tas", "onClick: " + manage_type_id + typename);
//                                } else {
//                                    tv.setTextColor(getResources().getColor(R.color.gray_dark));
//                                }
//                                ShoptypelistViewAdapter.notifyDataSetChanged();
//                            }
//
//                        });
//                        return tv;
//                    }
//
//                    //      为标签设置预点击内容(就是一开始就处于点击状态的标签)
//                    @Override
//                    public boolean setSelected(int position, String s) {
//                        return s.equals(tagFlowDate.get(position));
//                    }
//                });

            }
        };
        lv_manage_type.setAdapter(ShoptypelistViewAdapter);
        ShoptypelistViewAdapter.notifyDataSetChanged();

    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        /**
         * @param space 传入的值，其单位视为dp
         */
        public SpaceItemDecoration(float space) {
            this.mSpace = Utility.dip2px(SelectorStoreType.this, space);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = itemAdapter.getItemCount();
            int pos = parent.getChildAdapterPosition(view);
//            Log.d(TAG, "itemCount>>" +itemCount + ";Position>>" + pos);

            outRect.left = 0;
            outRect.top = 0;
            outRect.bottom = 0;


            if (pos != (itemCount - 1)) {
                outRect.right = mSpace;
            } else {
                outRect.right = 0;
            }
        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

        List<com.xinzuokeji.boxtakeawaybusiness.entities.Shoptype> list;//存放数据
        Context context;
        int selectedPosition = 0;

        public ItemAdapter(List<com.xinzuokeji.boxtakeawaybusiness.entities.Shoptype> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_store_type, parent, false));
            return holder;
        }

        //在这里可以获得每个子项里面的控件的实例，比如这里的TextView,子项本身的实例是itemView，
// 在这里对获取对象进行操作
        //holder.itemView是子项视图的实例，holder.textView是子项内控件的实例
        //position是点击位置
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            //设置textView显示内容为list里的对应项R.mipmap.recharge
            holder.tv_type.setText(list.get(position).typename);

            if (selectedPosition == position) {
                holder.imageView.setBackgroundResource(R.mipmap.recharge1);
            } else {
                holder.imageView.setBackgroundResource(R.mipmap.recharge);
            }

            //子项的点击事件监听
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemAdapter.clearSelection(position);
                    itemAdapter.notifyDataSetChanged();
                    netService.show_businesstype(Integer.parseInt(mDateStoretype.get(position).id), businesstype);
                }
            });
        }

        //要显示的子项数量
        @Override
        public int getItemCount() {

            return list.size();
        }

        //这里定义的是子项的类，不要在这里直接对获取对象进行操作
        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv_type;
            ImageView imageView;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv_type = itemView.findViewById(R.id.tv_type);
                imageView = itemView.findViewById(R.id.img_type);
            }
        }

    /*之下的方法都是为了方便操作，并不是必须的*/

        //这句是把listview的点击position,传递过来
        public void clearSelection(int position) {
            selectedPosition = position;

        }

        //在指定位置插入，原位置的向后移动一格
        public boolean addItem(int position, Shoptype msg) {
            if (position < list.size() && position >= 0) {
                list.add(position, msg);
                notifyItemInserted(position);
                return true;
            }
            return false;
        }

        //去除指定位置的子项
        public boolean removeItem(int position) {
            if (position < list.size() && position >= 0) {
                list.remove(position);
                notifyItemRemoved(position);
                return true;
            }
            return false;
        }

        //清空显示数据
        public void clearAll() {
            list.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_sure_store_type.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                if (Valid.isNotNullOrEmpty(typeid) && Valid.isNotNullOrEmpty(typename)) {
                    aCache.put("manage_type_id", typeid);
                    aCache.put("typename", typename);
                }
                finish();
                break;
            case R.id.bt_sure_store_type:
                finish();
                break;
            default:
                break;
        }
    }

    //店铺分类
    @SuppressLint("HandlerLeak")
    Handler Shoptype = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        mDateStoretype.clear();
                        mDateStoretype.addAll((Collection<? extends Shoptype>) msg.obj);
                        itemAdapter.notifyDataSetChanged();
                        netService.show_businesstype(Integer.parseInt(mDateStoretype.get(0).id), businesstype);
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
    //    经营分类
    @SuppressLint("HandlerLeak")
    Handler businesstype = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        mDateShoptype.clear();
                        mDateShoptype.addAll((Collection<? extends Shoptype>) msg.obj);
                        ShoptypelistViewAdapter.notifyDataSetChanged();
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
