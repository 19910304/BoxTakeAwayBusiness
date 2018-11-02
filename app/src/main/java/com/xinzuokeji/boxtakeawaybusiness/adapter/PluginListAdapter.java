package com.xinzuokeji.boxtakeawaybusiness.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.GoodsData;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.storeOperations.commodityManage.CMCompile;
import com.xinzuokeji.boxtakeawaybusiness.storeOperations.commodityManage.CommodityManage;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by huangziwei on 16-6-29.
 */
public class PluginListAdapter extends BaseAdapter {

    public static final int TYPE_TAG_ADDED = 0;
    public static final int TYPE_TAG_NOT_ADDED = 1;
    public static final int TYPE_PLUGIN_ADDED = 2;
    public static final int TYPE_PLUGIN_NOT_ADDED = 3;
    //是否显示拖拽按钮
    private boolean isShow;
    private Context mContext;
    private ArrayList<GoodsData> mAddedPlugins; // 已添加
    //    private ArrayList<CommodityManage.PluginItem> mNotAddedPlugins; // 未添加
//    private CommodityManage.OnStateBtnClickListener mListener;
    private Button bt_shaxiajia;
    private TextView tv_shangxiajia;
    private NetService netService;
    private String isLower = "";

    public PluginListAdapter(Context context, ArrayList<GoodsData> added, ArrayList<GoodsData> notAdded, CommodityManage.OnStateBtnClickListener listener) {
        mContext = context;
        mAddedPlugins = added;
//        mNotAddedPlugins = notAdded;
//        mListener = listener;
        netService = new NetService(mContext);
    }

    private GoodsData getPlugin(int position) {
//        if (position == 0) { // 跳过条目名称（已添加）
//            return null;
//        } else if (position < mAddedPlugins.size() + 1) {
//            return mAddedPlugins.get(position - 1);
//        } else if (position == mAddedPlugins.size() + 1) { // 跳过条目名称（未添加）
//            return null;
//        } else {
////            return mNotAddedPlugins.get(position - mAddedPlugins.size() - 2);
//            return mAddedPlugins.get(position);
//        }
        return mAddedPlugins.get(position);
    }

    public boolean isAdded(GoodsData item) {
        return mAddedPlugins.contains(item);
    }


    //改变显示拖拽的imageview，通过定义变量isShow去接收变量isManager
    public void changetShowDelImage(boolean isShow) {
        this.isShow = isShow;
        notifyDataSetChanged();
    }

    /**
     * 交换数据的位置
     *
     * @param src
     * @param dst
     * @return
     */
    public boolean exchange(int src, int dst) {
        boolean success = false;
        GoodsData srcItem = getPlugin(src);
        GoodsData dstItem = getPlugin(dst);
        int srcIndex = mAddedPlugins.indexOf(srcItem);
        int dstIndex = mAddedPlugins.indexOf(dstItem);
        if (srcIndex != -1 && dstIndex != -1) {
            Collections.swap(mAddedPlugins, srcIndex, dstIndex);
            success = true;
        }
        // 处理处理的数据在两个不同的集合内的情况
        if (!success) {
            if (srcIndex == -1 && dstIndex != -1) {
//                srcIndex = mNotAddedPlugins.indexOf(srcItem);
                if (srcIndex != -1 && dstIndex != -1) {
//                    mNotAddedPlugins.remove(srcIndex);
//                    mNotAddedPlugins.add(srcIndex, dstItem);

                    mAddedPlugins.remove(dstIndex);
                    mAddedPlugins.add(dstIndex, srcItem);
                    success = true;
                }
            } else if (srcIndex != -1 && dstIndex == -1) {
//                dstIndex = mNotAddedPlugins.indexOf(dstItem);
                if (srcIndex != -1 && dstIndex != -1) {
//                    mNotAddedPlugins.remove(dstIndex);
//                    mNotAddedPlugins.add(dstIndex, srcItem);

                    mAddedPlugins.remove(srcIndex);
                    mAddedPlugins.add(srcIndex, dstItem);
                    success = true;
                }
            } else {
//                srcIndex = mNotAddedPlugins.indexOf(srcItem);
//                dstIndex = mNotAddedPlugins.indexOf(dstItem);
                if (srcIndex != -1 && dstIndex != -1) {
//                    Collections.swap(mNotAddedPlugins, srcIndex, dstIndex);
                    success = true;
                }
            }
        }
        if (success) {
            notifyDataSetChanged();
        }
        return success;
    }

    public void addItem(GoodsData item) {
//        mNotAddedPlugins.remove(item);
        mAddedPlugins.remove(item);
        mAddedPlugins.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(GoodsData item) {
//        mNotAddedPlugins.remove(item);
        mAddedPlugins.remove(item);

        // 排在最前
//        mNotAddedPlugins.add(0, item);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mAddedPlugins.size();
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TAG_ADDED;
        } else if (position == mAddedPlugins.size() + 1) {
            return TYPE_TAG_NOT_ADDED;
        } else if (position <= mAddedPlugins.size()) {
            return TYPE_PLUGIN_ADDED;
        } else {
            return TYPE_PLUGIN_NOT_ADDED;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_caidan2, null);
            viewHolder = new ViewHolder();
            //商品图片
            viewHolder.img_goods_img = convertView.findViewById(R.id.img_goods_img);
            //
            viewHolder.mMoveIcon = convertView.findViewById(R.id.dl_plugin_move);
            //商品名字
            viewHolder.tv_goods_name = convertView.findViewById(R.id.tv_goods_name);
            //商品单价
            viewHolder.tv_goods_price = convertView.findViewById(R.id.tv_goods_price);
            //销售量
            viewHolder.tv_num = convertView.findViewById(R.id.tv_num);
            // 点击编辑
            viewHolder.bt_bianji = convertView.findViewById(R.id.bt_bianji);
            // 上下架
            viewHolder.tv_caipin_stutas = convertView.findViewById(R.id.tv_caipin_stutas);
            viewHolder.tv_shangxia_jia = convertView.findViewById(R.id.tv_shangxia_jia);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final GoodsData item = getPlugin(position);
        if (isShow) {
            viewHolder.mMoveIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mMoveIcon.setVisibility(View.GONE);
        }
        if (item != null) {
            ImageLoader.getInstance().displayImage(item.goods_img, viewHolder.img_goods_img, GSApplication.getInstance().imageOptions);
            viewHolder.tv_goods_name.setText(item.goods_name);
            viewHolder.tv_goods_price.setText("￥" + item.goods_price);
            viewHolder.tv_num.setText("月售：" + item.goods_num);
//           是否下架 1 下架 0 未下架
            if (item.is_lower.equals("1")) {
                viewHolder.tv_shangxia_jia.setText("已下架");
                viewHolder.tv_caipin_stutas.setText("上架");
//                isLower = "0";
            } else {
                viewHolder.tv_shangxia_jia.setText("已上架");
                viewHolder.tv_caipin_stutas.setText("下架");
//                isLower = "1";
            }
            //编辑
            viewHolder.bt_bianji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CMCompile.class);
                    intent.putExtra("good_id", item.goods_id);
                    mContext.startActivity(intent);
                }
            });
            // 上下架
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.tv_caipin_stutas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bt_shaxiajia = finalViewHolder.tv_caipin_stutas;
                    tv_shangxiajia = finalViewHolder.tv_shangxia_jia;
                    //  商品上下架状态 0:上架 1:下架
                    int type = 0;
                    Log.i("type", isLower);
                    if (bt_shaxiajia.getText().toString().equals("上架")) {
                        type = 0;
                    }
                    if (bt_shaxiajia.getText().toString().equals("下架")) {
                        type = 1;
                    }
                    netService.showNorms(Integer.parseInt(item.goods_id), type, Hander_showNorms);

                }
            });
        }

        ((ViewGroup) convertView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        return convertView;
    }

    /**
     *
     */
    private class ViewHolder {
        public ImageView img_goods_img;
        public TextView tv_goods_name, tv_num, tv_goods_price, tv_shangxia_jia;
        public Button bt_bianji, tv_caipin_stutas;
        public ImageView mMoveIcon;
    }

    @SuppressLint("HandlerLeak")
    public Handler Hander_showNorms = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj.toString().equals("1")) {
                        bt_shaxiajia.setText("上架");
                        tv_shangxiajia.setText("已下架");

                    }
                    if (msg.obj.toString().equals("0")) {
                        bt_shaxiajia.setText("下架");
                        tv_shangxiajia.setText("已上架");

                    }
//                    Toast.makeText(mContext, msg.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2001:
                    Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 1001:
                    Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
