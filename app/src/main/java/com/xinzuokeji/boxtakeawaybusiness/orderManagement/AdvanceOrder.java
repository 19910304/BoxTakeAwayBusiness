package com.xinzuokeji.boxtakeawaybusiness.orderManagement;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.RideRouteCalculateActivity;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.OrderInfo;
import com.xinzuokeji.boxtakeawaybusiness.entities.goods;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;
import com.xinzuokeji.boxtakeawaybusiness.util.listviewshangxia.XListView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//预订单
public class AdvanceOrder extends BaseActivity implements XListView.IXListViewListener {
    private ImageButton header_back;
    XListView xlv_advance_order;
    ListViewAdapter<OrderInfo> mAdapter;
    List<OrderInfo> mDate;
    private Handler mHandler;
    private NetService netService;


    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_advance_order);
        mHandler = new Handler();
        netService = new NetService(this);
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);

        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);

        TextView textView = findViewById(R.id.header_title);
        textView.setText("预订单");
        textView.setTextColor(getResources().getColor(R.color.white));
        // 订单的列表
        xlv_advance_order = findViewById(R.id.xlv_advance_order);
        // 设置xlistview判断是否出现“查看更多”
        xlv_advance_order.setPullLoadEnable(false);
        xlv_advance_order.setPullRefreshEnable(true);
        mDate = new ArrayList<>();
        mAdapter = new ListViewAdapter<OrderInfo>(this, R.layout.item_advance_order, mDate) {
            @Override
            public void convert(ViewHolder holder, final OrderInfo orderInfo, final int id, int selectedPosition) {
                //骑手信息 (预订单没有骑手信息)
                LinearLayout ll_nick_info = holder.getView(R.id.ll_nick_info);//看订单状态是否显示
                if (orderInfo.rider_id.equals("0")) {
                    ll_nick_info.setVisibility(View.GONE);
                } else {
                    ll_nick_info.setVisibility(View.VISIBLE);
                    ImageView cimg_nick_icon = holder.getView(R.id.cimg_nick_icon);
                    TextView tv_nickname = holder.getView(R.id.tv_nickname);
                    TextView tv_nick_phone = holder.getView(R.id.tv_nick_phone);
                    // 显示骑手信息
                    if (Valid.isNotNullOrEmpty(orderInfo.img)) {
                        ImageLoader.getInstance().displayImage(orderInfo.img, cimg_nick_icon, GSApplication.getInstance().optionsRider);
                    } else {
                        cimg_nick_icon.setBackgroundResource(R.mipmap.rider_icon);
                    }
//                    ImageLoader.getInstance().displayImage(orderInfo.img, cimg_nick_icon, GSApplication.getInstance().imageOptions);
                    tv_nickname.setText(orderInfo.rider_name);
                    tv_nick_phone.setText(orderInfo.riderphone);
                }
                //备注
                LinearLayout ll_remark = holder.getView(R.id.ll_remark);
                if (Valid.isNotNullOrEmpty(orderInfo.remarks)) {
                    ll_remark.setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_remark, orderInfo.remarks);
                } else {
                    ll_remark.setVisibility(View.GONE);
                }
                TextView tv_states = holder.getView(R.id.tv_states);//odd_numbers 今日单号
                TextView tv_pre_delivery_time = holder.getView(R.id.tv_pre_delivery_time);//预送达时间
                TextView tv_client_name = holder.getView(R.id.tv_client_name);//收货人名字
                TextView tv_client_phone = holder.getView(R.id.tv_client_phone);//收货人电话
                TextView tv_client_address = holder.getView(R.id.tv_client_address);//详细地址(收货人)
                TextView tv_service_charge = holder.getView(R.id.tv_service_charge);//平台服务费
                TextView tv_projected_income = holder.getView(R.id.tv_projected_income);//预计输入
                TextView tv_add_time = holder.getView(R.id.tv_add_time);//下单时间
                EditText tv_order_number = holder.getView(R.id.tv_order_number);//订单编号
                // 今日单号
                tv_states.setText("#" +orderInfo.odd_numbers);
                tv_pre_delivery_time.setText("预订单(建议" + orderInfo.expected_time + orderInfo.pre_delivery_time + "前送达)");//预送达时间
                tv_client_name.setText(orderInfo.name);//收货人名字
                tv_client_phone.setText(orderInfo.phone);//收货人电话
                tv_client_address.setText(orderInfo.address + orderInfo.building_card + "(" + orderInfo.mileage + "km)");//详细地址
                tv_service_charge.setText("￥" + orderInfo.service_price);//平台服务费
                tv_projected_income.setText("￥" + orderInfo.platform_fee);//预计输入
                TextView tv_peisong_fee = holder.getView(R.id.tv_peisong_fee);//配送费
                if (orderInfo.distribution_info.equals("0")) {
                    tv_peisong_fee.setText("+￥" + orderInfo.distribution_fee);
                } else {
                    tv_peisong_fee.setText("+￥" + orderInfo.distribution_fee);
                }
                TextView tv_canhe = holder.getView(R.id.tv_canhe);//餐盒费
                tv_canhe.setText("+￥" + orderInfo.lunch_box_fee);
                //总金额
                TextView tv_all_fee = holder.getView(R.id.tv_all_fee);
                tv_all_fee.setText("￥" + orderInfo.total_fee);
                tv_add_time.setText(orderInfo.add_time);//下单时间
                tv_order_number.setText(orderInfo.ordernum);//订单编号
                // 打开导航
                ImageView img_map = holder.getView(R.id.img_map);
                img_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String jd = orderInfo.jd;
                        String wd = orderInfo.wd;
                        String address = orderInfo.address;
                        // 打开高德app
                        toNavigation(AdvanceOrder.this, jd, wd, address);

                    }
                });
                //  长按复制订单
                tv_order_number.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(orderInfo.ordernum);
                        showTip("订单号已复制到剪切板，快去粘贴吧~", Toast.LENGTH_SHORT);
                        return false;
                    }
                });
                // 商品是否展示
                LinearLayout ll_good_is_unfold = holder.getView(R.id.ll_good_is_unfold);
                final TextView tv_good_is_unfold = holder.getView(R.id.tv_good_is_unfold);
                final ImageView img_is_expand = holder.getView(R.id.img_is_expand);
                final ListView lv_good = holder.getView(R.id.lv_good);
                ll_good_is_unfold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tv_good_is_unfold.getText().toString().equals("展开")) {
                            tv_good_is_unfold.setText("收起");
                            lv_good.setVisibility(View.VISIBLE);
                            img_is_expand.setBackgroundResource(R.mipmap.isexpand);
                        } else {
                            tv_good_is_unfold.setText("展开");
                            lv_good.setVisibility(View.GONE);
                            img_is_expand.setBackgroundResource(R.mipmap.expand);
                        }

                    }
                });
                // 商品的展示
                List<goods> dateGood = new ArrayList<>();

                dateGood = orderInfo.goods;
                ListViewAdapter<goods> listViewAdapter = new ListViewAdapter<goods>(AdvanceOrder.this, R.layout.item_good, dateGood) {
                    @Override
                    public void convert(ViewHolder holder, goods s, int id, int selectedPosition) {
                        TextView tv_dishes = holder.getView(R.id.tv_dishes);
                        TextView tv_dishes_number = holder.getView(R.id.tv_dishes_number);
                        TextView tv_dishes_prices = holder.getView(R.id.tv_dishes_prices);
                        tv_dishes.setText(s.goods_name);
                        tv_dishes_number.setText("×" + s.num);
                        tv_dishes_prices.setText("￥" + s.goods_price);
                    }
                };
                lv_good.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();
                setPullLvHeight(lv_good);
                //联系用户
                Button bt_cancel_order = holder.getView(R.id.bt_cancel_order);
                bt_cancel_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        diallPhone(orderInfo.phone);
                    }
                });
                //申请退款
                Button bt_rebates = holder.getView(R.id.bt_rebates);
                bt_rebates.setText("申请退款");
                bt_rebates.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdvanceOrder.this, Rebates.class);
                        intent.putExtra("phone", orderInfo.phone);
                        intent.putExtra("order_main_id", orderInfo.order_main_id);
                        intent.putExtra("oid", orderInfo.oid);
                        startActivity(intent);
                    }
                });
            }
        };
        xlv_advance_order.setAdapter(mAdapter);
        xlv_advance_order.setXListViewListener(this);
        mAdapter.notifyDataSetChanged();
        netService.showPreorder(GetstoreId(), showPreorder);
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

    private boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    private void toNavigation(Context context, String jd, String wd, String address) {
        //1.判断用户手机是否安装高德地图APP
        boolean isInstalled = isAvilible(context, "com.autonavi.minimap");
        //2.首选使用高德地图APP完成导航
        if (isInstalled)

        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("androidamap://navi?");
            try {
                //填写应用名称
                stringBuilder.append("sourceApplication=" + URLEncoder.encode("禾籽外卖", "utf-8"));
                //导航目的地
                stringBuilder.append("&poiname=" + URLEncoder.encode(address, "utf-8"));
                //目的地经纬度
                stringBuilder.append("&lat=" + wd);
                stringBuilder.append("&lon=" + jd);
                stringBuilder.append("&dev=1&style=2");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //调用高德地图APP
            Intent intent = new Intent();
            intent.setPackage("com.autonavi.minimap");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setAction(Intent.ACTION_VIEW);
            //传递组装的数据
            intent.setData(Uri.parse(stringBuilder.toString()));
            context.startActivity(intent);
        } else {
            //使用高德地图导航sdk完成导航
            Intent intent = new Intent(context, RideRouteCalculateActivity.class);
            intent.putExtra("point_jd", jd);
            intent.putExtra("point_wd", wd);
            context.startActivity(intent);
        }
    }

    //
    @SuppressLint("HandlerLeak")
    Handler showPreorder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj.toString().equals("[]")) {
                        mDate.clear();
                        Toast.makeText(AdvanceOrder.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    } else {
                        mDate.clear();
                        mDate.addAll((Collection<? extends OrderInfo>) msg.obj);

                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                case 2001:
//                    mDate.clear();
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

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                netService.showPreorder(GetstoreId(), showPreorder);
                if (mDate.size() >= 10) {
                    xlv_advance_order.setPullLoadEnable(true);
                }
                xlv_advance_order.stopLoadMore();
                xlv_advance_order.stopRefresh();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDate.size() >= 10) {
                    xlv_advance_order.setPullLoadEnable(true);
                }
                xlv_advance_order.stopLoadMore();
                xlv_advance_order.stopRefresh();
            }
        }, 2000);
    }
}
