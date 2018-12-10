package com.xinzuokeji.boxtakeawaybusiness.pending;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.MainActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.RideRouteCalculateActivity;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.OrderInfo;
import com.xinzuokeji.boxtakeawaybusiness.entities.goods;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.orderManagement.Rebates;
import com.xinzuokeji.boxtakeawaybusiness.util.LoadingDialog;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;
import com.xinzuokeji.boxtakeawaybusiness.util.listviewshangxia.XListView;

import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//待处理订单
public class Pending extends Fragment implements View.OnClickListener, XListView.IXListViewListener {
    private RadioGroup rdg_pending;
    private static RadioButton rdb_pending_new_order;
    private RadioButton rdb_pending_wait_distribution;
    private RadioButton rdb_pending_reminder;
    private RadioButton rdb_pending_refund;
    static XListView xlvOrder;
    static ListViewAdapter<OrderInfo> mAdapter;
    static List<OrderInfo> mDate;
    private int mLayoutId = R.layout.item_fragment_pending;
    private Handler mHandler;
    NetService netService;
    int storeId;
    //无数据显示
    static LinearLayout ll_order_no;
    static ImageView img_order_no;
    TextView tv_order_no;
    Button bt, btMealsWheels, btRepulseRefund, btAgreeRefund;
    LoadingDialog.Builder loadBuilder;
    public LoadingDialog dialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 加载布局
        View view = inflater.inflate(R.layout.fragment_pending, container, false);
        initView(view, inflater, container);
        initEvent(view);
        return view;
    }


    private void initView(View view, LayoutInflater inflater, ViewGroup container) {
        SharedPreferences sp = GSApplication.getInstance().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //storeId
        storeId = sp.getInt("storeId", 0);
        // 订单的列表
        xlvOrder = view.findViewById(R.id.xlv_order);
        // 设置xlistview判断是否出现“查看更多”
        xlvOrder.setPullLoadEnable(false);
        xlvOrder.setPullRefreshEnable(true);
        mHandler = new Handler();
        //无数据显示
        ll_order_no = view.findViewById(R.id.ll_order_no);
        img_order_no = view.findViewById(R.id.img_order_no);
        netService = new NetService(getActivity());
        //选中的三个状态
        rdg_pending = view.findViewById(R.id.rdg_pending);
        rdb_pending_new_order = view.findViewById(R.id.rdb_pending_new_order);
        rdb_pending_wait_distribution = view.findViewById(R.id.rdb_pending_wait_distribution);
        rdb_pending_reminder = view.findViewById(R.id.rdb_pending_reminder);
        rdb_pending_refund = view.findViewById(R.id.rdb_pending_refund);
        rdg_pending.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_pending_new_order://新订单
                        loading();
                        netService.showUntreated(storeId, showUntreated);
                        mAdapter = new ListViewAdapter<OrderInfo>(getActivity(), R.layout.item_fragment_pending, mDate) {
                            @Override
                            public void convert(ViewHolder holder, final OrderInfo orderInfo, int id, int selectedPosition) {
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
                                TextView tv_projected_income = holder.getView(R.id.tv_projected_income);//商家收入
                                TextView tv_add_time = holder.getView(R.id.tv_add_time);//下单时间
                                EditText et_order_number = holder.getView(R.id.et_order_number);//订单编号
                                TextView tv_peisong_fee = holder.getView(R.id.tv_peisong_fee);//配送费
                                if (orderInfo.distribution_info.equals("0")) {
                                    tv_peisong_fee.setText("+￥" + orderInfo.distribution_fee);
                                } else {
                                    tv_peisong_fee.setText("-￥" + orderInfo.distribution_fee);
                                }
                                TextView tv_canhe = holder.getView(R.id.tv_canhe);//餐盒费
                                tv_canhe.setText("+￥" + orderInfo.lunch_box_fee);
                                //总金额
                                TextView tv_all_fee = holder.getView(R.id.tv_all_fee);
                                tv_all_fee.setText("￥" + orderInfo.total_fee);
                                // 平台服务费
                                TextView tv_service_charge = holder.getView(R.id.tv_service_charge);
                                tv_service_charge.setText("-￥" + orderInfo.service_price);
//                                is_pro_order	int	是否为预订单 0:否 1:是
//                                dispatch_type	int	1.外送 2.自取
                                if (orderInfo.is_pro_order.equals("1")) {
                                    tv_states.setBackgroundResource(R.mipmap.chuli2);
                                    tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.blue));
                                    tv_pre_delivery_time.setText("预订单(要求" + orderInfo.expected_time + "前送达)");//预送达时间
                                } else {
                                    if (orderInfo.dispatch_type.equals("1")) {
                                        tv_states.setBackgroundResource(R.mipmap.order_tips);
                                        tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.red));
                                        tv_pre_delivery_time.setText("立即送达(建议" + orderInfo.pre_delivery_time + "前送达)");//预送达时间
                                    } else {
                                        tv_states.setBackgroundResource(R.mipmap.chuli3);
                                        tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.orange));
                                        tv_pre_delivery_time.setText("到店自取(建议" + orderInfo.pre_delivery_time + "前出餐)");//预送达时间
                                    }

                                }
                                // 今日单号
                                tv_states.setText("#" + orderInfo.odd_numbers);
                                tv_client_name.setText(orderInfo.name);//
                                tv_client_phone.setText(orderInfo.phone);//
                                tv_client_address.setText(orderInfo.address + orderInfo.building_card + "(" + orderInfo.mileage + "km)");//
                                tv_projected_income.setText("￥" + orderInfo.expected_income);//
                                tv_add_time.setText(orderInfo.add_time);//
                                et_order_number.setText(orderInfo.ordernum);//
                                // 打开导航
                                ImageView img_map = holder.getView(R.id.img_map);
                                img_map.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String jd = orderInfo.jd;
                                        String wd = orderInfo.wd;
                                        String address = orderInfo.address;
                                        // 打开高德app
                                        toNavigation(getActivity(), jd, wd, address);

                                    }
                                });
                                //  长按复制订单
                                et_order_number.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                        cm.setText(orderInfo.ordernum);
                                        Toast.makeText(getActivity(), "订单号已复制到剪切板，快去粘贴吧~", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });
                                // 商品是否展示
                                final TextView tv_good_is_unfold = holder.getView(R.id.tv_good_is_unfold);
                                final ImageView img_is_expand = holder.getView(R.id.img_is_expand);
                                final ListView lv_good = holder.getView(R.id.lv_good);
                                tv_good_is_unfold.setOnClickListener(new View.OnClickListener() {
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
//
                                ListViewAdapter<goods> listViewAdapter = new ListViewAdapter<goods>(getActivity(), R.layout.item_good, dateGood) {
                                    @Override
                                    public void convert(ViewHolder holder, goods s, int id, int selectedPosition) {
                                        TextView tv_dishes = holder.getView(R.id.tv_dishes);
                                        TextView tv_dishes_number = holder.getView(R.id.tv_dishes_number);
                                        TextView tv_dishes_prices = holder.getView(R.id.tv_dishes_prices);
                                        if (Valid.isNotNullOrEmpty(s.norms)) {
                                            tv_dishes.setText(s.goods_name + "(" + s.norms + ")");
                                        } else {
                                            tv_dishes.setText(s.goods_name);
                                        }
                                        tv_dishes_number.setText("×" + s.num);
                                        tv_dishes_prices.setText("￥" + s.total);
                                    }
                                };
                                lv_good.setAdapter(listViewAdapter);
                                listViewAdapter.notifyDataSetChanged();
                                setPullLvHeight(lv_good);
                                // 新订单

                                //  联系用户
                                final Button bt_cancel_order = holder.getView(R.id.bt_cancel_order);
                                // 接单
                                final Button bt_order_receiving = holder.getView(R.id.bt_order_receiving);
                                bt_cancel_order.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        diallPhone(orderInfo.phone);
                                    }
                                });

                                // 接单
                                bt_order_receiving.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bt = bt_order_receiving;
                                        //判断是否为预订单，不是预订单到待发配送，
                                        if (orderInfo.is_pro_order.equals("1")) {
                                            netService.manualReceipt(Integer.parseInt(orderInfo.oid), Hander_manualReceiptAdvance);
                                        } else {
                                            netService.manualReceipt(Integer.parseInt(orderInfo.oid), Hander_manualReceipt);
                                        }
                                        bt_cancel_order.setVisibility(View.GONE);
                                        bt_order_receiving.setClickable(false);
                                    }
                                });
                            }

                        };
                        xlvOrder.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                        break;
                    case R.id.rdb_pending_wait_distribution://待发配送
                        loading();
                        netService.waitDistribution(storeId, showUntreated);
                        mAdapter = new ListViewAdapter<OrderInfo>(getActivity(), R.layout.item_pending_wait_distribution, mDate) {
                            @Override
                            public void convert(ViewHolder holder, final OrderInfo orderInfo, int id, int selectedPosition) {
                                //备注
                                LinearLayout ll_remark = holder.getView(R.id.ll_remark);
                                if (Valid.isNotNullOrEmpty(orderInfo.remarks)) {
                                    ll_remark.setVisibility(View.VISIBLE);
                                    holder.setText(R.id.tv_remark, orderInfo.remarks);
                                } else {
                                    ll_remark.setVisibility(View.GONE);
                                }
                                //骑手信息
                                LinearLayout ll_nick_info = holder.getView(R.id.ll_rider_infor);
                                ImageView cimg_nick_icon = holder.getView(R.id.cimg_nick_icon);
                                TextView tv_nickname = holder.getView(R.id.tv_nickname);
                                TextView tv_nick_phone = holder.getView(R.id.tv_nick_phone);
                                TextView tv_nick_status = holder.getView(R.id.tv_nick_status);
                                //  骑手id等于0 则没有骑手信息
                                if (orderInfo.rider_id.equals("0")) {
                                    ll_nick_info.setVisibility(View.GONE);
                                } else {
                                    if (orderInfo.distribution_info.equals("1")) {
                                        ll_nick_info.setVisibility(View.VISIBLE);
                                        if (Valid.isNotNullOrEmpty(orderInfo.rider_img)) {
                                            ImageLoader.getInstance().displayImage(orderInfo.rider_img, cimg_nick_icon, GSApplication.getInstance().optionsRider);
                                        } else {
                                            cimg_nick_icon.setBackgroundResource(R.mipmap.rider_icon);
                                        }
                                        // 显示骑手信息
                                        tv_nickname.setText(orderInfo.rider_name);
                                        tv_nick_phone.setText(orderInfo.rider_phone);
                                        if (orderInfo.status.equals("4") || orderInfo.status.equals("13")) {
                                            tv_nick_status.setText("待取货");
                                        }
                                        if (orderInfo.status.equals("5")) {
                                            tv_nick_status.setText("配送中");
                                        }
                                        if (orderInfo.status.equals("6")) {
                                            tv_nick_status.setText("已送达");
                                        }
                                        if (orderInfo.status.equals("10")) {
                                            tv_nick_status.setText("接单中");
                                        }
                                        if (orderInfo.status.equals("11")) {
                                            tv_nick_status.setText("接单中");
                                        }
                                        if (orderInfo.status.equals("12")) {
                                            tv_nick_status.setText("骑手已到店");
                                        }
                                        if (orderInfo.status.equals("15")) {
                                            tv_nick_status.setText("转单中");
                                        }
                                    } else {
                                        ll_nick_info.setVisibility(View.GONE);
                                    }
                                }

                                TextView tv_pre_delivery_time = holder.getView(R.id.tv_pre_delivery_time);//预送达时间
                                TextView tv_client_name = holder.getView(R.id.tv_client_name);//收货人名字
                                TextView tv_client_phone = holder.getView(R.id.tv_client_phone);//收货人电话
                                TextView tv_client_address = holder.getView(R.id.tv_client_address);//详细地址(收货人)
                                TextView tv_projected_income = holder.getView(R.id.tv_projected_income);//
                                TextView tv_add_time = holder.getView(R.id.tv_add_time);//下单时间
                                EditText et_order_number = holder.getView(R.id.et_order_number);//订单编号
                                // 平台服务费
                                TextView tv_service_charge = holder.getView(R.id.tv_service_charge);
                                tv_service_charge.setText("-￥" + orderInfo.service_price);
                                TextView tv_peisong_fee = holder.getView(R.id.tv_peisong_fee);//配送费
                                if (orderInfo.distribution_info.equals("0")) {
                                    tv_peisong_fee.setText("+￥" + orderInfo.distribution_fee);
                                } else {
                                    tv_peisong_fee.setText("-￥" + orderInfo.distribution_fee);
                                }
                                TextView tv_canhe = holder.getView(R.id.tv_canhe);//餐盒费
                                tv_canhe.setText("+￥" + orderInfo.lunch_box_fee);
                                //总金额
                                TextView tv_all_fee = holder.getView(R.id.tv_all_fee);
                                tv_all_fee.setText("￥" + orderInfo.total_fee);

                                TextView tv_states = holder.getView(R.id.tv_states);//odd_numbers 今日单号
                                // 今日单号
                                tv_states.setText("#" + orderInfo.odd_numbers);
                                //代发配送没有预订单
                                if (orderInfo.is_pro_order.equals("1")) {
                                    tv_states.setBackgroundResource(R.mipmap.chuli2);
                                    tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.blue));
                                    tv_pre_delivery_time.setText("预订单(要求" + orderInfo.pre_delivery_time + "前送达)");//预送达时间
                                } else {
                                    if (orderInfo.dispatch_type.equals("1")) {
                                        tv_states.setBackgroundResource(R.mipmap.order_tips);
                                        tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.red));
                                        tv_pre_delivery_time.setText("立即送达(建议" + orderInfo.pre_delivery_time + "前送达)");//预送达时间
                                    } else {
                                        tv_states.setBackgroundResource(R.mipmap.chuli3);
                                        tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.orange));
                                        tv_pre_delivery_time.setText("到点自取(建议" + orderInfo.pre_delivery_time + "前出餐)");//预送达时间
                                    }

                                }
                                tv_client_name.setText(orderInfo.name);//
                                tv_client_phone.setText(orderInfo.phone);//
                                tv_client_address.setText(orderInfo.address + orderInfo.building_card + "(" + orderInfo.mileage + "km)");//
                                tv_projected_income.setText("￥" + orderInfo.expected_income);//
                                tv_add_time.setText(orderInfo.add_time);//
                                et_order_number.setText(orderInfo.ordernum);//
                                // 打开导航
                                ImageView img_map = holder.getView(R.id.img_map);
                                img_map.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String jd = orderInfo.jd;
                                        String wd = orderInfo.wd;
                                        String address = orderInfo.address;
                                        // 打开高德app
                                        toNavigation(getActivity(), jd, wd, address);

                                    }
                                });
                                //  长按复制订单
                                et_order_number.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                        cm.setText(orderInfo.ordernum);
                                        Toast.makeText(getActivity(), "订单号已复制到剪切板，快去粘贴吧~", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });
                                // 商品是否展示
                                final TextView tv_good_is_unfold = holder.getView(R.id.tv_good_is_unfold);
                                final ImageView img_is_expand = holder.getView(R.id.img_is_expand);
                                final ListView lv_good = holder.getView(R.id.lv_good);
                                tv_good_is_unfold.setOnClickListener(new View.OnClickListener() {
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
                                ListViewAdapter<goods> listViewAdapter = new ListViewAdapter<goods>(getActivity(), R.layout.item_good, dateGood) {
                                    @Override
                                    public void convert(ViewHolder holder, goods s, int id, int selectedPosition) {
                                        TextView tv_dishes = holder.getView(R.id.tv_dishes);
                                        TextView tv_dishes_number = holder.getView(R.id.tv_dishes_number);
                                        TextView tv_dishes_prices = holder.getView(R.id.tv_dishes_prices);
                                        if (Valid.isNotNullOrEmpty(s.norms)) {
                                            tv_dishes.setText(s.goods_name + "(" + s.norms + ")");
                                        } else {
                                            tv_dishes.setText(s.goods_name);
                                        }
                                        tv_dishes_number.setText("×" + s.num);
                                        tv_dishes_prices.setText("￥" + s.total);
                                    }
                                };
                                lv_good.setAdapter(listViewAdapter);
                                listViewAdapter.notifyDataSetChanged();
                                setPullLvHeight(lv_good);
                                //  联系用户
                                Button bt_cancel_order = holder.getView(R.id.bt_cancel_order);
                                bt_cancel_order.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        diallPhone(orderInfo.phone);
                                    }
                                });
                                //  退款
                                Button bt_rebates = holder.getView(R.id.bt_rebates);
                                bt_rebates.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), Rebates.class);
                                        intent.putExtra("phone", orderInfo.phone);
                                        intent.putExtra("order_main_id", orderInfo.order_main_id);
                                        intent.putExtra("oid", orderInfo.oid);
                                        startActivity(intent);
                                    }
                                });
                                // 送餐
                                final Button bt_meals_wheels = holder.getView(R.id.bt_meals_wheels);
                                //  0 未付款 1已取消
                                //  2已付款 等待商家接单
                                //  3 商家接单 正在备餐（等待骑手接单）
                                //    4 骑手待取货
                                //    5骑手配送中（带送达）
                                //    6 已送达（待评价）
                                //   7 催单   8 用户申请退款
                                //   9 商家主动退款
                                //     10系统派单（骑手待确认）
                                //  11手动派单(骑手待确认)
                                //     12骑手已到店
                                //     13商家出餐
                                //     14 已退单
                                //      15骑手申请转单
                                if ((orderInfo.status.equals("3") || orderInfo.status.equals("4") || orderInfo.status.equals("10") || orderInfo.status.equals("11") || orderInfo.status.equals("12") ||
                                        orderInfo.status.equals("15"))) {
                                    // 是否已出餐 0没出餐 1已出餐
                                    if (orderInfo.is_outmeal.equals("1")) {
                                        bt_meals_wheels.setVisibility(View.GONE);
                                    } else {
                                        bt_meals_wheels.setText("出餐");
                                        bt_meals_wheels.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    bt_meals_wheels.setVisibility(View.GONE);
                                }
                                if (orderInfo.distribution_info.equals("0") && orderInfo.status.equals("13")) {
                                    bt_meals_wheels.setVisibility(View.VISIBLE);
                                    bt_meals_wheels.setText("配送");
                                }
                                if (orderInfo.distribution_info.equals("1") && orderInfo.status.equals("13")) {
                                    bt_meals_wheels.setVisibility(View.GONE);
                                }
                                bt_meals_wheels.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (bt_meals_wheels.getText().toString().equals("出餐")) {
                                            btMealsWheels = bt_meals_wheels;
                                            netService.ShopOutFood(Integer.parseInt(orderInfo.oid), Hander_ShopOutFood);
                                            btMealsWheels.setClickable(false);
                                        }
                                        if (bt_meals_wheels.getText().toString().equals("配送")) {
                                            btMealsWheels = bt_meals_wheels;
                                            netService.foodDelivery(Integer.parseInt(orderInfo.oid), Hander_foodDelivery);
                                            btMealsWheels.setClickable(false);
                                        }
                                    }
                                });
                            }
                        };
                        xlvOrder.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case R.id.rdb_pending_reminder://催单
                        loading();
                        netService.showFinishOrder(storeId, 7, showUntreated);
                        mAdapter = new ListViewAdapter<OrderInfo>(getActivity(), R.layout.item_pending_refund, mDate) {
                            @Override
                            public void convert(ViewHolder holder, final OrderInfo orderInfo, int id, int selectedPosition) {
                                //备注
                                LinearLayout ll_remark = holder.getView(R.id.ll_remark);
                                if (Valid.isNotNullOrEmpty(orderInfo.remarks)) {
                                    ll_remark.setVisibility(View.VISIBLE);
                                    holder.setText(R.id.tv_remark, orderInfo.remarks);
                                } else {
                                    ll_remark.setVisibility(View.GONE);
                                }
                                LinearLayout ll_refund_fee = holder.getView(R.id.ll_refund_fee);//退款信息隐藏
                                ll_refund_fee.setVisibility(View.GONE);
                                LinearLayout ll_call_information = holder.getView(R.id.ll_call_information);//联系人信息
                                ll_call_information.setVisibility(View.VISIBLE);
                                TextView tv_pre_delivery_time = holder.getView(R.id.tv_pre_delivery_time);//预送达时间
                                TextView tv_client_name = holder.getView(R.id.tv_client_name);//收货人名字
                                TextView tv_client_phone = holder.getView(R.id.tv_client_phone);//收货人电话
                                TextView tv_client_address = holder.getView(R.id.tv_client_address);//详细地址(收货人)
                                TextView tv_projected_income = holder.getView(R.id.tv_projected_income);//
                                TextView tv_add_time = holder.getView(R.id.tv_add_time);//下单时间
                                EditText et_order_number = holder.getView(R.id.et_order_number);//订单编号
                                // 平台服务费
                                TextView tv_service_charge = holder.getView(R.id.tv_service_charge);
                                tv_service_charge.setText("-￥" + orderInfo.service_price);
                                TextView tv_peisong_fee = holder.getView(R.id.tv_peisong_fee);//配送费
                                if (orderInfo.distribution_info.equals("0")) {
                                    tv_peisong_fee.setText("+￥" + orderInfo.distribution_fee);
                                } else {
                                    tv_peisong_fee.setText("-￥" + orderInfo.distribution_fee);
                                }
                                TextView tv_canhe = holder.getView(R.id.tv_canhe);//餐盒费
                                tv_canhe.setText("+￥" + orderInfo.lunch_box_fee);
                                //总金额
                                TextView tv_all_fee = holder.getView(R.id.tv_all_fee);
                                tv_all_fee.setText("￥" + orderInfo.total_fee);
                                TextView tv_states = holder.getView(R.id.tv_states);//odd_numbers 今日单号
                                // 今日单号
                                tv_states.setText("#" + orderInfo.odd_numbers);
                                if (orderInfo.is_pro_order.equals("1")) {
                                    tv_states.setBackgroundResource(R.mipmap.chuli2);
                                    tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.blue));
                                    tv_pre_delivery_time.setText("预订单(要求" + orderInfo.pre_delivery_time + "前送达)");//预送达时间
                                } else {
                                    if (orderInfo.dispatch_type.equals("1")) {
                                        tv_states.setBackgroundResource(R.mipmap.order_tips);
                                        tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.red));
                                        tv_pre_delivery_time.setText("立即送达(建议" + orderInfo.pre_delivery_time + "前送达)");//预送达时间
                                    } else {
                                        tv_states.setBackgroundResource(R.mipmap.chuli3);
                                        tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.orange));
                                        tv_pre_delivery_time.setText("到点自取(建议" + orderInfo.pre_delivery_time + "前出餐)");//预送达时间
                                    }

                                }
                                //骑手信息
                                LinearLayout ll_nick_info = holder.getView(R.id.ll_rider_infor);
                                ImageView cimg_nick_icon = holder.getView(R.id.cimg_nick_icon);
                                TextView tv_nickname = holder.getView(R.id.tv_nickname);
                                TextView tv_nick_phone = holder.getView(R.id.tv_nick_phone);
                                TextView tv_nick_status = holder.getView(R.id.tv_nick_status);
                                //  骑手id等于0 则没有骑手信息
                                if (orderInfo.rider_id.equals("0")) {
                                    ll_nick_info.setVisibility(View.GONE);
                                } else {
                                    if (orderInfo.distribution_info.equals("1")) {
                                        ll_nick_info.setVisibility(View.VISIBLE);
                                        if (Valid.isNotNullOrEmpty(orderInfo.img)) {
                                            ImageLoader.getInstance().displayImage(orderInfo.img, cimg_nick_icon, GSApplication.getInstance().optionsRider);
                                        } else {
                                            cimg_nick_icon.setBackgroundResource(R.mipmap.rider_icon);
                                        }
                                        // 显示骑手信息
                                        tv_nickname.setText(orderInfo.rider_name);
                                        tv_nick_phone.setText(orderInfo.riderphone);
                                        if (orderInfo.status.equals("4") || orderInfo.status.equals("13")) {
                                            tv_nick_status.setText("待取货");
                                        }
                                        if (orderInfo.status.equals("5")) {
                                            tv_nick_status.setText("配送中");
                                        }
                                        if (orderInfo.status.equals("6")) {
                                            tv_nick_status.setText("已送达");
                                        }
                                        if (orderInfo.status.equals("10")) {
                                            tv_nick_status.setText("接单中");
                                        }
                                        if (orderInfo.status.equals("11")) {
                                            tv_nick_status.setText("接单中");
                                        }
                                        if (orderInfo.status.equals("12")) {
                                            tv_nick_status.setText("骑手已到店");
                                        }
                                        if (orderInfo.status.equals("15")) {
                                            tv_nick_status.setText("转单中");
                                        }
                                    } else {
                                        ll_nick_info.setVisibility(View.GONE);
                                    }
                                }
                                // 收货人名字
                                tv_client_name.setText(orderInfo.name);
                                //收货人名字
                                tv_client_phone.setText(orderInfo.phone);
                                //详细地址(收货人)
                                tv_client_address.setText(orderInfo.address + orderInfo.building_card + "(" + orderInfo.mileage + "km)");
                                //预计收入
                                tv_projected_income.setText("￥" + orderInfo.expected_income);
                                //下单时间
                                tv_add_time.setText(orderInfo.add_time);
                                //订单编号
                                et_order_number.setText(orderInfo.ordernum);
                                // 打开导航
                                ImageView img_map = holder.getView(R.id.img_map);
                                img_map.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String jd = orderInfo.jd;
                                        String wd = orderInfo.wd;
                                        String address = orderInfo.address;
                                        // 打开高德app
                                        toNavigation(getActivity(), jd, wd, address);

                                    }
                                });
                                //  长按复制订单
                                et_order_number.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                        cm.setText(orderInfo.ordernum);
                                        Toast.makeText(getActivity(), "订单号已复制到剪切板，快去粘贴吧~", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });
                                // 商品是否展示
                                final TextView tv_good_is_unfold = holder.getView(R.id.tv_good_is_unfold);
                                final ImageView img_is_expand = holder.getView(R.id.img_is_expand);
                                final ListView lv_good = holder.getView(R.id.lv_good);
                                tv_good_is_unfold.setOnClickListener(new View.OnClickListener() {
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
                                ListViewAdapter<goods> listViewAdapter = new ListViewAdapter<goods>(getActivity(), R.layout.item_good, dateGood) {
                                    @Override
                                    public void convert(ViewHolder holder, goods s, int id, int selectedPosition) {
                                        TextView tv_dishes = holder.getView(R.id.tv_dishes);
                                        TextView tv_dishes_number = holder.getView(R.id.tv_dishes_number);
                                        TextView tv_dishes_prices = holder.getView(R.id.tv_dishes_prices);
                                        if (Valid.isNotNullOrEmpty(s.norms)) {
                                            tv_dishes.setText(s.goods_name + "(" + s.norms + ")");
                                        } else {
                                            tv_dishes.setText(s.goods_name);
                                        }
                                        tv_dishes_number.setText("×" + s.num);
                                        tv_dishes_prices.setText("￥" + s.total);
                                    }
                                };
                                lv_good.setAdapter(listViewAdapter);
                                listViewAdapter.notifyDataSetChanged();
                                setPullLvHeight(lv_good);
                                // 退款

                                // 显示退款信息
                                final TextView tv_refund_expand = holder.getView(R.id.tv_refund_expand);
                                final ImageView img_refund_expand = holder.getView(R.id.img_refund_expand);
                                final LinearLayout ll_refund = holder.getView(R.id.ll_refund);
                                tv_refund_expand.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (tv_refund_expand.getText().toString().equals("收起")) {
                                            ll_refund.setVisibility(View.GONE);
                                            tv_refund_expand.setText("展开");
                                            img_refund_expand.setBackgroundResource(R.mipmap.isexpand);
                                        } else {
                                            tv_refund_expand.setText("收起");
                                            ll_refund.setVisibility(View.VISIBLE);
                                            img_refund_expand.setBackgroundResource(R.mipmap.expand);
                                        }

                                    }
                                });
                                // 联系用户
                                Button bt_call_client = holder.getView(R.id.bt_call_client);
                                bt_call_client.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        diallPhone(orderInfo.phone);
                                    }
                                });
                                // 联系骑手
                                Button bt_call_rider = holder.getView(R.id.bt_call_rider);
                                if (!orderInfo.rider_id.equals("0")) {
                                    if (orderInfo.distribution_info.equals("0")) {
                                        bt_call_rider.setVisibility(View.GONE);
                                    } else {
                                        bt_call_rider.setVisibility(View.VISIBLE);
                                    }
                                }

                                bt_call_rider.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        diallPhone(orderInfo.riderphone);
                                    }
                                });

                            }
                        };
                        xlvOrder.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case R.id.rdb_pending_refund://退款
                        loading();
                        netService.requestRefund(storeId, showUntreated);
                        mAdapter = new ListViewAdapter<OrderInfo>(getActivity(), R.layout.item_pending_refund, mDate) {
                            @Override
                            public void convert(ViewHolder holder, final OrderInfo orderInfo, int id, int selectedPosition) {
                                //备注
                                LinearLayout ll_remark = holder.getView(R.id.ll_remark);
                                if (Valid.isNotNullOrEmpty(orderInfo.remarks)) {
                                    ll_remark.setVisibility(View.VISIBLE);
                                    holder.setText(R.id.tv_remark, orderInfo.remarks);
                                } else {
                                    ll_remark.setVisibility(View.GONE);
                                }
                                LinearLayout ll_refund_fee = holder.getView(R.id.ll_refund_fee);//退款信息显示
                                ll_refund_fee.setVisibility(View.VISIBLE);
                                LinearLayout ll_call_information = holder.getView(R.id.ll_call_information);//联系人信息
                                ll_call_information.setVisibility(View.GONE);
                                TextView tv_pre_delivery_time = holder.getView(R.id.tv_pre_delivery_time);//预送达时间
                                TextView tv_client_name = holder.getView(R.id.tv_client_name);//收货人名字
                                TextView tv_client_phone = holder.getView(R.id.tv_client_phone);//收货人电话
                                TextView tv_client_address = holder.getView(R.id.tv_client_address);//详细地址(收货人)
                                TextView tv_projected_income = holder.getView(R.id.tv_projected_income);//预计收入
                                TextView tv_add_time = holder.getView(R.id.tv_add_time);//下单时间
                                EditText et_order_number = holder.getView(R.id.et_order_number);//订单编号
                                // 平台服务费
                                TextView tv_service_charge = holder.getView(R.id.tv_service_charge);
                                tv_service_charge.setText("-￥" + orderInfo.service_price);
                                TextView tv_peisong_fee = holder.getView(R.id.tv_peisong_fee);//配送费
                                if (orderInfo.distribution_info.equals("0")) {
                                    tv_peisong_fee.setText("+￥" + orderInfo.distribution_fee);
                                } else {
                                    tv_peisong_fee.setText("-￥" + orderInfo.distribution_fee);
                                }
                                TextView tv_canhe = holder.getView(R.id.tv_canhe);//餐盒费
                                tv_canhe.setText("+￥" + orderInfo.lunch_box_fee);
                                //总金额
                                TextView tv_all_fee = holder.getView(R.id.tv_all_fee);
                                tv_all_fee.setText("￥" + orderInfo.total_fee);
                                TextView tv_states = holder.getView(R.id.tv_states);//odd_numbers 今日单号
                                // 今日单号
                                tv_states.setText("#" + orderInfo.odd_numbers);
                                if (orderInfo.is_pro_order.equals("1")) {
                                    tv_states.setBackgroundResource(R.mipmap.chuli2);
                                    tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.blue));
                                    tv_pre_delivery_time.setText("预订单(要求" + orderInfo.pre_delivery_time + "前送达)");//预送达时间
                                } else {
                                    if (orderInfo.dispatch_type.equals("1")) {
                                        tv_states.setBackgroundResource(R.mipmap.order_tips);
                                        tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.red));
                                        tv_pre_delivery_time.setText("立即送达(建议" + orderInfo.pre_delivery_time + "前送达)");//预送达时间
                                    } else {
                                        tv_states.setBackgroundResource(R.mipmap.chuli3);
                                        tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.orange));
                                        tv_pre_delivery_time.setText("到店自取(建议" + orderInfo.pre_delivery_time + "前出餐)");//预送达时间
                                    }

                                }
                                tv_client_name.setText(orderInfo.name);//
                                tv_client_phone.setText(orderInfo.phone);//
                                tv_client_address.setText(orderInfo.address + orderInfo.building_card + "(" + orderInfo.mileage + "km)");//
                                tv_projected_income.setText("￥" + orderInfo.expected_income);//
                                tv_add_time.setText(orderInfo.add_time);//
                                et_order_number.setText(orderInfo.ordernum);//
                                // 打开导航
                                ImageView img_map = holder.getView(R.id.img_map);
                                img_map.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String jd = orderInfo.jd;
                                        String wd = orderInfo.wd;
                                        String address = orderInfo.address;
                                        // 打开高德app
                                        toNavigation(getActivity(), jd, wd, address);

                                    }
                                });
                                //  长按复制订单
                                et_order_number.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                        cm.setText(orderInfo.ordernum);
                                        Toast.makeText(getActivity(), "订单号已复制到剪切板，快去粘贴吧~", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });
                                // 商品是否展示
                                final TextView tv_good_is_unfold = holder.getView(R.id.tv_good_is_unfold);
                                final ImageView img_is_expand = holder.getView(R.id.img_is_expand);
                                final ListView lv_good = holder.getView(R.id.lv_good);
                                tv_good_is_unfold.setOnClickListener(new View.OnClickListener() {
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
                                ListViewAdapter<goods> listViewAdapter = new ListViewAdapter<goods>(getActivity(), R.layout.item_good, dateGood) {
                                    @Override
                                    public void convert(ViewHolder holder, goods s, int id, int selectedPosition) {
                                        TextView tv_dishes = holder.getView(R.id.tv_dishes);
                                        TextView tv_dishes_number = holder.getView(R.id.tv_dishes_number);
                                        TextView tv_dishes_prices = holder.getView(R.id.tv_dishes_prices);
                                        if (Valid.isNotNullOrEmpty(s.norms)) {
                                            tv_dishes.setText(s.goods_name + "(" + s.norms + ")");
                                        } else {
                                            tv_dishes.setText(s.goods_name);
                                        }
                                        tv_dishes_number.setText("×" + s.num);
                                        tv_dishes_prices.setText("￥" + s.total);
                                    }
                                };
                                lv_good.setAdapter(listViewAdapter);
                                listViewAdapter.notifyDataSetChanged();
                                setPullLvHeight(lv_good);
                                // 退款
                                // 显示退款信息
                                final TextView tv_refund_expand = holder.getView(R.id.tv_refund_expand);
                                final ImageView img_refund_expand = holder.getView(R.id.img_refund_expand);
                                final LinearLayout ll_refund = holder.getView(R.id.ll_refund);
                                tv_refund_expand.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (tv_refund_expand.getText().toString().equals("收起")) {
                                            ll_refund.setVisibility(View.GONE);
                                            tv_refund_expand.setText("展开");
                                            img_refund_expand.setBackgroundResource(R.mipmap.isexpand);
                                        } else {
                                            tv_refund_expand.setText("收起");
                                            ll_refund.setVisibility(View.VISIBLE);
                                            img_refund_expand.setBackgroundResource(R.mipmap.expand);
                                        }

                                    }
                                });
                                // 退款信息
                                if (Valid.isNotNullOrEmpty(orderInfo.other_reason)) {
                                    holder.setText(R.id.tv_refund_reason, "理由：" + orderInfo.reason + "(" + orderInfo.other_reason + ")");
                                } else {
                                    holder.setText(R.id.tv_refund_reason, "理由：" + orderInfo.reason);
                                }
//                                holder.setText(R.id.tv_refund_style, "");
//                                holder.setText(R.id.tv_refund_reason, orderInfo.other_reason);
                                // 同意退款
                                final Button bt_agree_refund = holder.getView(R.id.bt_agree_refund);
                                // 拒绝退款
                                final Button bt_repulse_refund = holder.getView(R.id.bt_repulse_refund);
                                // 同意退款
                                bt_agree_refund.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        loading();
                                        btAgreeRefund = bt_agree_refund;
                                        btRepulseRefund = bt_repulse_refund;
                                        netService.approvalRefund(Integer.parseInt(orderInfo.order_main_id), Hander_approvalRefund);
                                    }
                                });
                                // 拒绝退款
                                bt_repulse_refund.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        loading();
                                        btRepulseRefund = bt_repulse_refund;
                                        btAgreeRefund = bt_agree_refund;
                                        netService.RefuseRefund(Integer.parseInt(orderInfo.order_main_id), Hander_approvalRefund);
                                    }
                                });

                            }
                        };
                        xlvOrder.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
        //第一次进来
        // 订单的列表

        mDate = new ArrayList<>();

        mAdapter = new ListViewAdapter<OrderInfo>(getActivity(), mLayoutId, mDate) {
            @Override
            public void convert(ViewHolder holder, final OrderInfo orderInfo, int id, int selectedPosition) {
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
                TextView tv_projected_income = holder.getView(R.id.tv_projected_income);//预计收入
                TextView tv_add_time = holder.getView(R.id.tv_add_time);//下单时间
                EditText et_order_number = holder.getView(R.id.et_order_number);//订单编号
                // 平台服务费
                TextView tv_service_charge = holder.getView(R.id.tv_service_charge);
                tv_service_charge.setText("-￥" + orderInfo.service_price);
                TextView tv_peisong_fee = holder.getView(R.id.tv_peisong_fee);//配送费
                if (orderInfo.distribution_info.equals("0")) {
                    tv_peisong_fee.setText("+￥" + orderInfo.distribution_fee);
                } else {
                    tv_peisong_fee.setText("-￥" + orderInfo.distribution_fee);
                }
                TextView tv_canhe = holder.getView(R.id.tv_canhe);//餐盒费
                tv_canhe.setText("+￥" + orderInfo.lunch_box_fee);
                //总金额
                TextView tv_all_fee = holder.getView(R.id.tv_all_fee);
                tv_all_fee.setText("￥" + orderInfo.total_fee);
                //  is_pro_order	int	是否为预订单 0:否 1:是
                //  dispatch_type	int	1.外送 2.自取
                if (orderInfo.is_pro_order.equals("1")) {
                    tv_states.setBackgroundResource(R.mipmap.chuli2);
                    tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.blue));
                    tv_pre_delivery_time.setText("预订单(要求" + orderInfo.expected_time + "前送达)");//预送达时间
                } else {
                    if (orderInfo.dispatch_type.equals("1")) {
                        tv_states.setBackgroundResource(R.mipmap.order_tips);
                        tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.red));
                        tv_pre_delivery_time.setText("立即送达(建议" + orderInfo.pre_delivery_time + "前送达)");//预送达时间
                    } else {
                        tv_states.setBackgroundResource(R.mipmap.chuli3);
                        tv_pre_delivery_time.setTextColor(getResources().getColor(R.color.orange));
                        tv_pre_delivery_time.setText("到店自取(建议" + orderInfo.pre_delivery_time + "前出餐)");//预送达时间
                    }

                }

                // 今日单号
                tv_states.setText("#" + orderInfo.odd_numbers);
                tv_client_name.setText(orderInfo.name);//
                tv_client_phone.setText(orderInfo.phone);//
                tv_client_address.setText(orderInfo.address + orderInfo.building_card + "(" + orderInfo.mileage + "km)");//地址和距离
                tv_projected_income.setText("￥" + orderInfo.expected_income);//预计收入
                tv_add_time.setText(orderInfo.add_time);//
                et_order_number.setText(orderInfo.ordernum);//
                // 打开导航
                ImageView img_map = holder.getView(R.id.img_map);
                img_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String jd = orderInfo.jd;
                        String wd = orderInfo.wd;
                        String address = orderInfo.address;
                        // 打开高德app
                        toNavigation(getActivity(), jd, wd, address);

                    }
                });
                //  长按复制订单
                et_order_number.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        if (cm != null) {
                            cm.setText(orderInfo.ordernum);
                        }
                        Toast.makeText(getActivity(), "订单号已复制到剪切板，快去粘贴吧~", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                // 商品是否展示
                final TextView tv_good_is_unfold = holder.getView(R.id.tv_good_is_unfold);
                final ImageView img_is_expand = holder.getView(R.id.img_is_expand);
                final ListView lv_good = holder.getView(R.id.lv_good);
                tv_good_is_unfold.setOnClickListener(new View.OnClickListener() {
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
                ListViewAdapter<goods> listViewAdapter = new ListViewAdapter<goods>(getActivity(), R.layout.item_good, dateGood) {
                    @Override
                    public void convert(ViewHolder holder, goods s, int id, int selectedPosition) {
                        TextView tv_dishes = holder.getView(R.id.tv_dishes);
                        TextView tv_dishes_number = holder.getView(R.id.tv_dishes_number);
                        TextView tv_dishes_prices = holder.getView(R.id.tv_dishes_prices);
                        if (Valid.isNotNullOrEmpty(s.norms)) {
                            tv_dishes.setText(s.goods_name + "(" + s.norms + ")");
                        } else {
                            tv_dishes.setText(s.goods_name);
                        }
                        tv_dishes_number.setText("×" + s.num);
                        tv_dishes_prices.setText("￥" + s.total);
                    }
                };
                lv_good.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();
                setPullLvHeight(lv_good);
                // 新订单
                if (rdb_pending_new_order.isChecked()) {
                    //  联系用户
                    final Button bt_cancel_order = holder.getView(R.id.bt_cancel_order);
                    bt_cancel_order.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            diallPhone(orderInfo.phone);
                        }
                    });
                    // 接单
                    final Button bt_order_receiving = holder.getView(R.id.bt_order_receiving);
                    bt_order_receiving.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bt = bt_order_receiving;
                            //判断是否为预订单，不是预订单到待发配送，
                            if (orderInfo.is_pro_order.equals("1")) {
                                netService.manualReceipt(Integer.parseInt(orderInfo.oid), Hander_manualReceiptAdvance);
                            } else {
                                netService.manualReceipt(Integer.parseInt(orderInfo.oid), Hander_manualReceipt);
                            }
                            bt_cancel_order.setVisibility(View.GONE);
                            bt_order_receiving.setClickable(false);
                        }
                    });
                }


            }
        }

        ;
        xlvOrder.setAdapter(mAdapter);
        xlvOrder.setXListViewListener(this);
        mAdapter.notifyDataSetChanged();
        loading();
        netService.showUntreated(storeId, showUntreated);

    }

    private void initEvent(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void loading() {
        loadBuilder = new LoadingDialog.Builder(getActivity())
                .setMessage("")
                .setCancelable(true)
                .setCancelOutside(true);
        dialog = loadBuilder.create();
        dialog.show();
    }

    //重新计算listview高度
    public void setPullLvHeight(ListView pull) {
        int totalHeight = 0;
        ListAdapter adapter = pull.getAdapter();
        for (int i = 0, len = adapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, pull);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = pull.getLayoutParams();
        params.height = totalHeight + (pull.getDividerHeight() * (pull.getCount() - 1));
        pull.setLayoutParams(params);
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //  新订单
                if (rdb_pending_new_order.isChecked()) {
                    netService.showUntreated(storeId, showUntreated);
                }
//                        待处理
                if (rdb_pending_wait_distribution.isChecked()) {
                    netService.waitDistribution(storeId, showUntreated);
                }
//                        催单
                if (rdb_pending_reminder.isChecked()) {
                    netService.showFinishOrder(storeId, 7, showUntreated);
                }
//                        退单
                if (rdb_pending_refund.isChecked()) {
                    netService.requestRefund(storeId, showUntreated);
                }
                if (mDate.size() >= 10) {
                    xlvOrder.setPullLoadEnable(true);
//                    xlvOrder.stopLoadMore();
                }
//
                xlvOrder.stopRefresh();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDate.size() >= 10) {
                    xlvOrder.setPullLoadEnable(true);
                }
                xlvOrder.stopLoadMore();
                xlvOrder.stopRefresh();
            }
        }, 2000);
    }

    @SuppressLint("HandlerLeak")
    Handler Hander_manualReceipt = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    bt.setText("已接单");
                    bt.setClickable(false);
                    //接单成功选中代发配送
                    rdb_pending_wait_distribution.setChecked(true);
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2001:
                    bt.setClickable(true);
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 1001:
                    Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler Hander_manualReceiptAdvance = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    bt.setText("已接单");
                    bt.setClickable(false);
//                    //接单成功选中代发配送
//                    rdb_pending_wait_distribution.setChecked(true);
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2001:
                    bt.setClickable(true);
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 1001:
                    Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler Hander_approvalRefund = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    // 正在显示就销毁
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    btAgreeRefund.setVisibility(View.GONE);
                    btRepulseRefund.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("class_name", "RefundSuccess");
                    MainActivity.mActivity.finish();
                    startActivity(intent);
                    break;
                case 2001:
//                    bt.setClickable(true);
                    // 正在显示就销毁
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 1001:
                    // 正在显示就销毁
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler Hander_foodDelivery = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    btMealsWheels.setText("已配送");
                    btMealsWheels.setClickable(false);
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2001:
                    btMealsWheels.setClickable(true);
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 1001:
                    Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler Hander_ShopOutFood = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    btMealsWheels.setText("已出餐");
                    btMealsWheels.setClickable(false);
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2001:
                    btMealsWheels.setClickable(true);
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 1001:
                    Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    //订单信息
    private final SetshopPutForwardHandler showUntreated = new SetshopPutForwardHandler(this);

    private static class SetshopPutForwardHandler extends Handler {
        private final WeakReference<Pending> mActivity;

        private SetshopPutForwardHandler(Pending activity) {
            mActivity = new WeakReference<Pending>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Pending activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 2000:
                        // 正在显示就销毁
                        if (activity.dialog.isShowing()) {
                            activity.dialog.dismiss();
                        }
                        mDate.clear();
                        mDate.addAll((Collection<? extends OrderInfo>) msg.obj);
                        mAdapter.notifyDataSetChanged();
                        if (mDate.size() == 0) {
                            xlvOrder.setVisibility(View.GONE);
                            // 无数据显示默认图
                            ll_order_no.setVisibility(View.VISIBLE);
//                        新订单
                            if (rdb_pending_new_order.isChecked()) {
                                img_order_no.setBackgroundResource(R.mipmap.pending_new_order);
                            }
//                        待处理
                            if (activity.rdb_pending_wait_distribution.isChecked()) {
                                img_order_no.setBackgroundResource(R.mipmap.pending_wait_distribution);
                            }
//                        催单
                            if (activity.rdb_pending_reminder.isChecked()) {
                                img_order_no.setBackgroundResource(R.mipmap.pending_reminder);
                            }
//                        退单
                            if (activity.rdb_pending_refund.isChecked()) {
                                img_order_no.setBackgroundResource(R.mipmap.pending_refund);
                            }
                        } else {
                            xlvOrder.setVisibility(View.VISIBLE);
                            ll_order_no.setVisibility(View.GONE);
                        }
                        break;
                    case 2001:
                        // 正在显示就销毁
                        if (activity.dialog.isShowing()) {
                            activity.dialog.dismiss();
                        }
                        break;
                    case 1001:
                        // 正在显示就销毁
                        if (activity.dialog.isShowing()) {
                            activity.dialog.dismiss();
                        }
                        Toast.makeText(activity.getActivity(), activity.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);

            }
        }
    }

    //刷新新订单数据
    public static class OrderNewReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object orderinfo = intent.getSerializableExtra("new_order");
            mDate.clear();
            mDate.addAll((Collection<? extends OrderInfo>) orderinfo);
            mAdapter.notifyDataSetChanged();
            if (mDate.size() == 0) {
                xlvOrder.setVisibility(View.GONE);
                // 无数据显示默认图
                ll_order_no.setVisibility(View.VISIBLE);
//                        新订单
                if (rdb_pending_new_order.isChecked()) {
                    img_order_no.setBackgroundResource(R.mipmap.pending_new_order);
                }

            } else {
                xlvOrder.setVisibility(View.VISIBLE);
                ll_order_no.setVisibility(View.GONE);

            }
        }
    }


    //    //**
//     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
//     *
//     * @param phoneNum 电话号码
//     */
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
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
        if (isInstalled) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("androidamap://navi?");
            try {
                //填写应用名称
                stringBuilder.append("sourceApplication=" + URLEncoder.encode(getString(R.string.app_name), "utf-8"));
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
            intent.putExtra("point_jd", wd);
            intent.putExtra("point_wd", jd);
            context.startActivity(intent);
        }
    }
}
