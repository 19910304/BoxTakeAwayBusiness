package com.xinzuokeji.boxtakeawaybusiness.orderManagement;

import android.annotation.SuppressLint;
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
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.RideRouteCalculateActivity;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.OrderInfo;
import com.xinzuokeji.boxtakeawaybusiness.entities.goods;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.ACache;
import com.xinzuokeji.boxtakeawaybusiness.util.LoadingDialog;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;
import com.xinzuokeji.boxtakeawaybusiness.util.listviewshangxia.XListView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//订单管理
public class OrderManagement extends Fragment implements View.OnClickListener, XListView.IXListViewListener, INaviInfoCallback {
    private RadioGroup rdg_order;
    private RadioButton rdb_underway, rdb_finish, rdb_cancel;
    private TextView tv_underway, tv_finish, tv_cancel;
    XListView xlvOrder;
    ListViewAdapter<OrderInfo> mAdapter;
    List<OrderInfo> mDate;
    private TextView tv_advance_order;
    private Handler mHandler;
    NetService netService;
    int storeId;
    public static int FINSH_ORDER = 6;
    //天气情况
    TextView tv_weather, tv_temperature, tv_city;
    Button btRebates;
    LatLng p2 = new LatLng(39.917337, 116.397056);//故宫博物院
    LatLng p3 = new LatLng(39.904556, 116.427231);//北京站
    RelativeLayout header_title;
    private LinearLayout llWeatherSearch;
    LoadingDialog.Builder loadBuilder;
    LoadingDialog dialog = null;
    String RefundSuccess = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Intent i = getIntent();
//        Bundle data = i.getExtras();

        // 加载布局
        View view = inflater.inflate(R.layout.fragment_order_management, container, false);
        initView(view, inflater, container);
        initEvent(view);
        return view;

    }


    private void initView(View view, LayoutInflater inflater, ViewGroup container) {
        // 搜索按钮
        header_title = view.findViewById(R.id.rl_search_order);
        //天气情况
        llWeatherSearch = view.findViewById(R.id.ll_weatherSearch);
        tv_weather = view.findViewById(R.id.tv_weather);
        tv_temperature = view.findViewById(R.id.tv_temperature);
        tv_city = view.findViewById(R.id.tv_city);
        // 获取天气信息
        getWeatherSearch();

        mHandler = new Handler();
        SharedPreferences sp = GSApplication.getInstance().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //storeId
        storeId = sp.getInt("storeId", 0);
        netService = new NetService(getActivity());
        //订单的三个状态
        rdg_order = view.findViewById(R.id.rdg_order);
        rdb_underway = view.findViewById(R.id.rdb_underway);
        rdb_finish = view.findViewById(R.id.rdb_finish);
        rdb_cancel = view.findViewById(R.id.rdb_cancel);
        //选中的三个状态
        tv_underway = view.findViewById(R.id.tv_underway);
        tv_finish = view.findViewById(R.id.tv_finish);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_advance_order = view.findViewById(R.id.tv_advance_order);
        //  缓存中拿到退款状态 ，退款成功后刷新数据
        ACache aCache = ACache.get(getActivity());
        RefundSuccess = aCache.getAsString("RefundSuccess");
        if (Valid.isNotNullOrEmpty(RefundSuccess)) {
            if (RefundSuccess.equals("RefundSuccess")) {
                rdb_cancel.setChecked(true);
                tv_underway.setVisibility(View.INVISIBLE);
                tv_cancel.setVisibility(View.VISIBLE);
                tv_finish.setVisibility(View.INVISIBLE);
                tv_underway.setBackgroundResource(R.color.white);
                tv_cancel.setBackgroundResource(R.color.yellow);
                tv_finish.setBackgroundResource(R.color.white);
                netService.showCancelOrder(storeId, showCancelOrder);
                aCache.remove("RefundSuccess");
            }
        }
        rdg_order.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_underway://进行中的订单
                        tv_underway.setVisibility(View.VISIBLE);
                        tv_underway.setBackgroundResource(R.color.yellow);
                        tv_cancel.setBackgroundResource(R.color.white);
                        tv_finish.setBackgroundResource(R.color.white);
                        tv_cancel.setVisibility(View.INVISIBLE);
                        tv_finish.setVisibility(View.INVISIBLE);
                        //获取数据
                        netService.showJingOrder(storeId, showJingOrder);
                        // 加载框
                        loading();
                        break;
                    case R.id.rdb_finish://完成的订单
                        tv_underway.setVisibility(View.INVISIBLE);
                        tv_cancel.setVisibility(View.INVISIBLE);
                        tv_finish.setVisibility(View.VISIBLE);
                        tv_underway.setBackgroundResource(R.color.white);
                        tv_cancel.setBackgroundResource(R.color.white);
                        tv_finish.setBackgroundResource(R.color.yellow);
                        netService.showFinishOrder(storeId, FINSH_ORDER, showFinshOrder);
                        // 加载框
                        loading();
                        break;
                    case R.id.rdb_cancel://取消的订单
                        tv_underway.setVisibility(View.INVISIBLE);
                        tv_cancel.setVisibility(View.VISIBLE);
                        tv_finish.setVisibility(View.INVISIBLE);
                        tv_underway.setBackgroundResource(R.color.white);
                        tv_cancel.setBackgroundResource(R.color.yellow);
                        tv_finish.setBackgroundResource(R.color.white);
                        netService.showCancelOrder(storeId, showCancelOrder);
                        // 加载框
                        loading();
                        break;
                }
            }
        });
        // 订单的列表
        xlvOrder = view.findViewById(R.id.xlv_order);
        // 设置xlistview判断是否出现“查看更多”
        xlvOrder.setPullLoadEnable(false);
        xlvOrder.setPullRefreshEnable(true);
        mDate = new ArrayList<>();
        mAdapter = new ListViewAdapter<OrderInfo>(getActivity(), R.layout.item_fragment_order, mDate) {
            @Override
            public void convert(ViewHolder holder, final OrderInfo orderInfo, int id, int selectedPosition) {
//                CustomDigitalClock remainTime = holder.getView(R.id.remainTime);
//                remainTime.setEndTime(System.currentTimeMillis() + 600L);
//                remainTime.setClockListener(new CustomDigitalClock.ClockListener() { // register the clock's listener
//
//                    @Override
//                    public void timeEnd() {
//                        // The clock time is ended.
//                        Toast.makeText(getActivity(), "时间到了", Toast.LENGTH_SHORT);
//                    }
//
//                    @Override
//                    public void remainFiveMinutes() {
//                        // The clock time is remain five minutes.
//                    }
//                });

                // 退款信息
                LinearLayout ll_refund_fee = holder.getView(R.id.ll_refund_fee);
                TextView tv_refund_time = holder.getView(R.id.tv_refund_time);
                TextView tv_cancel_time = holder.getView(R.id.tv_cancel_time);
                TextView tv_refund_reason = holder.getView(R.id.tv_refund_reason);
                TextView tv_cancel_stats = holder.getView(R.id.tv_cancel_stats);
                TextView tv_refund_infor = holder.getView(R.id.tv_refund_infor);
                TextView tv_greed_status = holder.getView(R.id.tv_greed_status);
                TextView tv_refund_1 = holder.getView(R.id.tv_refund_1);
                TextView tv_cancel_style = holder.getView(R.id.tv_cancel_style);
                LinearLayout ll_refund_status = holder.getView(R.id.ll_refund_status);
                // 退款时间
                LinearLayout ll_refund_time = holder.getView(R.id.ll_refund_time);
                // 联系方式
                LinearLayout ll_contact_information = holder.getView(R.id.ll_contact_information);
                // 打开导航
                ImageView img_map = holder.getView(R.id.img_map);
                img_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String wd = orderInfo.wd;
                        String jd = orderInfo.jd;
                        String address = orderInfo.address;
                        // 打开高德app
                        toNavigation(getActivity(), jd, wd, address);
                        // 自定义导航
//                        Intent intent = new Intent(getActivity(), RideRouteCalculateActivity.class);
//                        intent.putExtra("StartLatlng", orderInfo.longitude);
//                        intent.putExtra("Startdimension", orderInfo.dimension);
//                        intent.putExtra("EndLatlng", orderInfo.jd);
//                        intent.putExtra("Enddimension", orderInfo.wd);
//                        startActivity(intent);

//                        LatLng p2 = new LatLng(40.084894,  116.603039);//商家
//                        LatLng p3 = new LatLng(39.825934, 116.342972);//用户位置
//                        AmapNaviParams params = new AmapNaviParams(new Poi("商家", p2, ""), null, new Poi("用户", p3, ""), AmapNaviType.DRIVER);
//                        params.setUseInnerVoice(true);
//                      AmapNaviPage.getInstance().showRouteActivity(getActivity(), params, OrderManagement.this);
                    }
                });
                // 是否显示退款信息
                final LinearLayout ll_refund_is_unfold = holder.getView(R.id.ll_refund_is_unfold);
                final LinearLayout ll_refund = holder.getView(R.id.ll_refund);
                final TextView tv_refund_expand = holder.getView(R.id.tv_refund_expand);
                final ImageView img_refund_expand = holder.getView(R.id.img_refund_expand);
                ll_refund_is_unfold.setOnClickListener(new View.OnClickListener() {
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

                //骑手信息
                LinearLayout ll_nick_info = holder.getView(R.id.ll_nick_info);
                ImageView cimg_nick_icon = holder.getView(R.id.cimg_nick_icon);
                TextView tv_nickname = holder.getView(R.id.tv_nickname);
                TextView tv_nick_phone = holder.getView(R.id.tv_nick_phone);
                TextView tv_nick_status = holder.getView(R.id.tv_nick_status);
                //  骑手id等于0 则没有骑手信息
                if (orderInfo.rider_id.equals("0")) {
                    ll_nick_info.setVisibility(View.GONE);
                } else {
                    ll_nick_info.setVisibility(View.VISIBLE);
                    if (Valid.isNotNullOrEmpty(orderInfo.img)) {
                        ImageLoader.getInstance().displayImage(orderInfo.img, cimg_nick_icon, GSApplication.getInstance().optionsRider);
                    } else {
                        cimg_nick_icon.setBackgroundResource(R.mipmap.rider_icon);
                    }
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
                TextView tv_projected_income = holder.getView(R.id.tv_projected_income);//预计输入
                TextView tv_add_time = holder.getView(R.id.tv_add_time);//下单时间
                EditText et_order_number = holder.getView(R.id.et_order_number);//订单编号
                LinearLayout ll_zixun = holder.getView(R.id.ll_zixun);

                TextView tv_peisong_fee = holder.getView(R.id.tv_peisong_fee);//配送费
                if (orderInfo.distribution_info.equals("0")) {
                    tv_peisong_fee.setText("+￥" + orderInfo.distribution_fee);
                } else {
                    tv_peisong_fee.setText("+￥" + orderInfo.distribution_fee);
                }
                TextView tv_canhe = holder.getView(R.id.tv_canhe);//餐盒费
                tv_canhe.setText("+￥" + orderInfo.lunch_box_fee);
                TextView tv_all_fee = holder.getView(R.id.tv_all_fee);//总金额
                tv_all_fee.setText("￥" + orderInfo.total_fee);
                // 平台服务费
                TextView tv_service_charge = holder.getView(R.id.tv_service_charge);
                tv_service_charge.setText("￥" + orderInfo.service_price);
                //预送达时间
//                if () {
//                }
                tv_pre_delivery_time.setText("立即送达(建议" + orderInfo.pre_delivery_time + "前送达)");
                tv_client_name.setText(orderInfo.name);//收货人名字
                tv_client_phone.setText(orderInfo.phone);//收货人电话
                tv_client_address.setText(orderInfo.address + orderInfo.building_card + "(" + orderInfo.mileage + "km)");//详细地址(收货人)和距离
                tv_projected_income.setText("￥" + orderInfo.expected_income);//预计收入
                tv_add_time.setText(orderInfo.add_time);//下单时间
                et_order_number.setText(orderInfo.ordernum);//订单编号
                // 今日单号
                tv_states.setText("#" + orderInfo.odd_numbers);
                //  长按复制订单
                et_order_number.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        ClipboardManager cm = (ClipboardManager) GSApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(orderInfo.ordernum);
                        Toast.makeText(getActivity(), "订单号已复制到剪切板，快去粘贴吧~", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                //  联系收货人
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
                        // 单线程使用StringBuilder 效率更快
                        StringBuilder text = new StringBuilder("×");
                        tv_dishes_number.setText(text.append(s.num));
                        StringBuilder text1 = new StringBuilder("￥");
                        tv_dishes_prices.setText(text1.append(s.total));
                    }
                };
                lv_good.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();
                setPullLvHeight(lv_good);
                //  联系骑手
                final Button bt_call_rider = holder.getView(R.id.bt_call_rider);
                bt_call_rider.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 状态2 用户自取
                        if (bt_call_rider.getText().toString().equals("联系骑手")) {
                            diallPhone(orderInfo.riderphone);
                        }
                        if (bt_call_rider.getText().toString().equals("确认取餐")) {
                            netService.BuConDelivery(Integer.parseInt(orderInfo.oid), hander_BuConDelivery);
                        }
                        if (bt_call_rider.getText().toString().equals("确认送达")) {
                            netService.BuConDelivery(Integer.parseInt(orderInfo.oid), hander_BuConDelivery);
                        }
                    }
                });

                //  退款
                final Button bt_rebates = holder.getView(R.id.bt_rebates);
                bt_rebates.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 退款和评论骑手都是主id
                        if (rdb_finish.isChecked()) {
                            btRebates = bt_rebates;
                            //如果已完成跳转评论骑手
                            Intent intent = new Intent(getActivity(), EvaluateRider.class);
                            intent.putExtra("rid", orderInfo.rider_id);
                            intent.putExtra("order_main_id", orderInfo.order_main_id);
                            startActivityForResult(intent, 0);
                        } else {
                            Intent intent = new Intent(getActivity(), Rebates.class);
                            intent.putExtra("phone", orderInfo.phone);
                            intent.putExtra("order_main_id", orderInfo.order_main_id);
                            intent.putExtra("oid", orderInfo.oid);
                            startActivity(intent);
                        }

                    }
                });
                //联系用户
                Button bt_full_refund = holder.getView(R.id.bt_full_refund);
                bt_full_refund.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //联系用户
                        diallPhone(orderInfo.phone);

                    }
                });
                if (rdb_finish.isChecked()) {
                    ll_contact_information.setVisibility(View.VISIBLE);
                    //1:外送 2:自取（dispatch_type）
                    if (orderInfo.dispatch_type.equals("1")) {
                        tv_pre_delivery_time.setText("送达时间：" + orderInfo.delivery_time);
                    } else {
                        tv_pre_delivery_time.setText("用户取餐时间：" + orderInfo.delivery_time);
                    }
                    //看订单状态是否显示
                    if (orderInfo.rider_id.equals("0")) {
                        ll_nick_info.setVisibility(View.GONE);
                    } else {
                        ll_nick_info.setVisibility(View.VISIBLE);
                        // 显示骑手信息
                        tv_nickname.setText(orderInfo.rider_name);
                        tv_nick_phone.setText(orderInfo.riderphone);
                        if (orderInfo.status.equals("4")) {
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
                    }
                    bt_full_refund.setVisibility(View.VISIBLE);
                    bt_call_rider.setVisibility(View.GONE);
                    if (orderInfo.distribution_info.equals("1")) {
                        bt_rebates.setVisibility(View.VISIBLE);
                    } else {
                        bt_rebates.setVisibility(View.GONE);
                    }
                    // 评价完不能再评价
                    if (orderInfo.evaluate_rider.equals("0")) {
                        bt_rebates.setVisibility(View.VISIBLE);
                    } else {
                        bt_rebates.setVisibility(View.GONE);
                    }
                    bt_rebates.setText("评价骑手");
                    bt_full_refund.setText("联系用户");
                    ll_zixun.setVisibility(View.GONE);

                }
                if (rdb_underway.isChecked()) {
                    ll_contact_information.setVisibility(View.VISIBLE);
                    if (orderInfo.rider_id.equals("0")) {
                        ll_nick_info.setVisibility(View.GONE);

                    } else {
                        ll_nick_info.setVisibility(View.VISIBLE);
                        // 显示骑手信息
                        tv_nickname.setText(orderInfo.rider_name);
                        tv_nick_phone.setText(orderInfo.riderphone);
                        if (orderInfo.status.equals("4")) {
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
                    }
                    bt_call_rider.setVisibility(View.VISIBLE);
                    bt_rebates.setVisibility(View.VISIBLE);
                    bt_full_refund.setVisibility(View.VISIBLE);
                    bt_rebates.setText("申请退款");
                    bt_full_refund.setText("联系用户");

                    //  店铺配送方式 0商家自配 1平台配送
                    if (orderInfo.distribution_info.equals("0")) {
                        if (orderInfo.dispatch_type.equals("2")) {
                            bt_call_rider.setText("确认取餐");
                        } else {
                            bt_call_rider.setText("确认送达");
                        }
                        ll_nick_info.setVisibility(View.GONE);
                    } else {
                        if (orderInfo.dispatch_type.equals("2")) {
                            bt_call_rider.setText("确认取餐");
                        } else {
                            bt_call_rider.setText("联系骑手");
                            ll_nick_info.setVisibility(View.VISIBLE);
                        }
                    }
//                    ll_zixun.setVisibility(View.VISIBLE);
                }
                //  订单关闭
                if (rdb_cancel.isChecked()) {
                    //  订单关闭不显示联系按钮
                    ll_contact_information.setVisibility(View.GONE);
                    bt_call_rider.setVisibility(View.GONE);
                    bt_rebates.setVisibility(View.GONE);
                    bt_full_refund.setVisibility(View.GONE);
                    ll_nick_info.setVisibility(View.GONE);
                    tv_cancel_stats.setVisibility(View.VISIBLE);
                    ll_refund_fee.setVisibility(View.VISIBLE);
                    ll_refund_status.setVisibility(View.VISIBLE);
                    if (Valid.isNotNullOrEmpty(orderInfo.other_reason)) {
                        tv_refund_reason.setText("理由：" + orderInfo.reason + "(" + orderInfo.other_reason + ")");
                    } else {
                        tv_refund_reason.setText("理由：" + orderInfo.reason);
                    }
                    //商家同意退款时间
                    tv_refund_time.setText(orderInfo.shop_time);
                    //申请退款时间
                    tv_cancel_time.setText(orderInfo.re_add_time);
                    if (orderInfo.user_type.equals("1")) {
                        tv_cancel_style.setText("用户申请退款");
                        tv_refund_infor.setText("用户申请退款");
                        if (orderInfo.status.equals("1")) {
                            // 用户主动取消
                            ll_refund_status.setVisibility(View.GONE);
                            ll_refund_time.setVisibility(View.GONE);
                            tv_cancel_style.setText("用户取消订单");
                            tv_refund_infor.setText("用户取消订单");
                        } else {
                            ll_refund_status.setVisibility(View.VISIBLE);
                            ll_refund_time.setVisibility(View.VISIBLE);
                        }
                        if (orderInfo.refund_status.equals("1") || orderInfo.refund_status.equals("2") || orderInfo.refund_status.equals("3")) {
                            tv_greed_status.setText("已同意");
                            tv_refund_1.setText("商家同意退款");
                        }
                        if (orderInfo.refund_status.equals("4")) {
                            tv_greed_status.setText("退款失败");
                            tv_refund_1.setText("商家同意退款");
                        }
                        if (orderInfo.refund_status.equals("5")) {
                            tv_greed_status.setText("不同意");
                            tv_refund_1.setText("商家拒绝退款");
                        }
                        tv_cancel_stats.setText("用户已取消");
                    }
                    if (orderInfo.user_type.equals("2")) {
                        // 商家主动退款要显示退款状态和时间
                        ll_refund_status.setVisibility(View.VISIBLE);
                        ll_refund_time.setVisibility(View.VISIBLE);
                        tv_cancel_stats.setText("商家已取消");
                        tv_refund_infor.setText("商家主动退款");
                        tv_greed_status.setText("已退款");
                        tv_refund_1.setText("退款");
                        tv_cancel_style.setText("商家取消订单");
                    }
                } else {
                    ll_refund_fee.setVisibility(View.GONE);
                    tv_cancel_stats.setVisibility(View.GONE);
                    ll_refund_status.setVisibility(View.GONE);
                }

            }
        };
        xlvOrder.setAdapter(mAdapter);
        xlvOrder.setXListViewListener(this);
        mAdapter.notifyDataSetChanged();
        netService.showJingOrder(storeId, showJingOrder);
        loading();
    }

    private void initEvent(View view) {
        tv_advance_order.setOnClickListener(this);
        header_title.setOnClickListener(this);
        llWeatherSearch.setOnClickListener(this);
    }

    private void loading() {
        loadBuilder = new LoadingDialog.Builder(getActivity())
                .setMessage("")
                .setCancelable(true)
                .setCancelOutside(true);
        dialog = loadBuilder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_advance_order://预订单
                Intent intent = new Intent(getActivity(), AdvanceOrder.class);
                startActivity(intent);
                break;
            case R.id.rl_search_order://跳转搜索页面
                Intent intentSearchOrder = new Intent(getActivity(), SearchOrder.class);
                startActivity(intentSearchOrder);
                break;
            case R.id.ll_weatherSearch://更新天气信息

                getWeatherSearch();
                break;
            default:
                break;
        }
    }


    //获取天气
    private void getWeatherSearch() {
        //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
//        Log.i("citywww", getCity() + "===");
        WeatherSearchQuery mquery = new WeatherSearchQuery(getCity(), WeatherSearchQuery.WEATHER_TYPE_LIVE);
        WeatherSearch mweathersearch = new WeatherSearch(getActivity());
        mweathersearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
            @Override
            public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
                if (rCode == 1000) {
                    if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                        LocalWeatherLive weatherlive = weatherLiveResult.getLiveResult();
                        tv_weather.setText(weatherlive.getWeather());
                        tv_temperature.setText(weatherlive.getTemperature() + "°");
                        tv_city.setText(getCity());
//                        Log.i("weatherlive", weatherlive.getCity() + "---" + weatherlive.getWindDirection() + "--" + weatherlive.getTemperature() + "°");
                    } else {
//                        Log.i("weatherlive", rCode + "--11");
                    }
                } else {
                    tv_city.setText(getCity());
                }
            }

            @Override
            public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
                Log.i("weatherlive", "===");
//                localWeatherForecastResult.getForecastResult().getWeatherForecast().get(0).getDayWeather();
            }
        });
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("date", "===");
        switch (requestCode) {
            case 0:
                if (data != null) {
                    String date = data.getStringExtra("JudgeRider");
                    Log.i("date", date + "===");
                    if (date.equals("1")) {
                        btRebates.setVisibility(View.GONE);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                //  进行中
                if (rdb_underway.isChecked()) {
                    netService.showJingOrder(storeId, showJingOrder);
                }
                //   已完成
                if (rdb_finish.isChecked()) {
                    netService.showFinishOrder(storeId, FINSH_ORDER, showFinshOrder);
                }
                // 关闭
                if (rdb_cancel.isChecked()) {
                    netService.showCancelOrder(storeId, showCancelOrder);
                }

                if (mDate.size() >= 10) {
                    xlvOrder.setPullLoadEnable(true);
                }
//                xlvOrder.stopLoadMore();
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

    //获取mainactivity中存的城市
    public String getCity() {
        SharedPreferences sp = GSApplication.getInstance().getSharedPreferences("city", Context.MODE_PRIVATE);
        //取得user_name
        String user_name = sp.getString("city", "重庆市");
        return user_name;
    }

    //
    @SuppressLint("HandlerLeak")
    Handler showJingOrder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    // 正在显示就销毁
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (msg.obj.toString().equals("[]")) {
                        mDate.clear();
                        xlvOrder.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                    } else {
                        mDate.clear();
                        xlvOrder.setVisibility(View.VISIBLE);
                        mDate.addAll((Collection<? extends OrderInfo>) msg.obj);

                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                case 2001:
                    // 正在显示就销毁
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
//                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
    Handler hander_BuConDelivery = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2001:
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 1001:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    //
    @SuppressLint("HandlerLeak")
    Handler showFinshOrder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    // 正在显示就销毁
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (msg.obj.toString().equals("[]")) {
                        mDate.clear();
                        xlvOrder.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                    } else {
                        mDate.clear();
                        xlvOrder.setVisibility(View.VISIBLE);
                        mDate.addAll((Collection<? extends OrderInfo>) msg.obj);

                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                case 2001:
//                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    // 正在显示就销毁
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
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
    Handler showCancelOrder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    // 正在显示就销毁
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    mDate.clear();
                    mDate.addAll((Collection<? extends OrderInfo>) msg.obj);
                    if (mDate.size() == 0) {
                        xlvOrder.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                    } else {
                        xlvOrder.setVisibility(View.VISIBLE);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                case 2001:
                    // 正在显示就销毁
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
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

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public View getCustomNaviBottomView() {
        return null;
    }

    @Override
    public View getCustomNaviView() {
        return null;
    }

    @Override
    public void onArrivedWayPoint(int i) {

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
