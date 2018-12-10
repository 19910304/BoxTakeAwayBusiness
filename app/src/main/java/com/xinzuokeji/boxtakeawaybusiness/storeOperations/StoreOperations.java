package com.xinzuokeji.boxtakeawaybusiness.storeOperations;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.NetworkImageHolderView;
import com.xinzuokeji.boxtakeawaybusiness.entities.StoreOperations_Banner;
import com.xinzuokeji.boxtakeawaybusiness.entities.TodayOrder;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.storeOperations.activeConfiguration.ActiveConfiguration;
import com.xinzuokeji.boxtakeawaybusiness.storeOperations.advertisement.Avertisement;
import com.xinzuokeji.boxtakeawaybusiness.storeOperations.caiwu.Caiwu;
import com.xinzuokeji.boxtakeawaybusiness.storeOperations.commodityManage.CommodityManage;
import com.xinzuokeji.boxtakeawaybusiness.storeOperations.jingyingShuju.JingyingShuju;
import com.xinzuokeji.boxtakeawaybusiness.storeOperations.userEvaluation.UserEvaluationManage;
import com.xinzuokeji.boxtakeawaybusiness.util.ACache;
import com.xinzuokeji.boxtakeawaybusiness.util.AppUtil;
import com.xinzuokeji.boxtakeawaybusiness.util.Utility;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;
import com.xinzuokeji.convenientbanner.holder.CBViewHolderCreator;
import com.xinzuokeji.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//门店运营
public class StoreOperations extends Fragment implements View.OnClickListener {
    private LinearLayout ll_message, ll_commodity_management, ll_user_pinjia, ll_caiwu, ll_active_configuration, ll_avertisement, ll_speed_refund, ll_jingying_shuju;
    private com.xinzuokeji.convenientbanner.ConvenientBanner convenientBanner;
    private List<StoreOperations_Banner> banners = new ArrayList<>();
    //订单的收入和数量
    @SuppressLint("StaticFieldLeak")
    public static TextView tv_order_money;
    @SuppressLint("StaticFieldLeak")
    public static TextView tv_order_number;
    private NetService netService;
    // 店铺id
    private int storeId;
    ACache aCache;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 加载布局
        View view = inflater.inflate(R.layout.fragment_store_operations, container, false);
        netService = new NetService(getActivity());

        SharedPreferences sp = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //storeId
        storeId = sp.getInt("storeId", 1);
        initView(view, inflater, container);
        initEvent(view);

        return view;
    }

    public void initView(View view, LayoutInflater inflater, ViewGroup container) {
        //订单的收入和数量
        tv_order_money = view.findViewById(R.id.tv_order_money);
        tv_order_number = view.findViewById(R.id.tv_order_number);
        aCache = ACache.get(getActivity());
        ll_message = view.findViewById(R.id.ll_message);
        ll_commodity_management = view.findViewById(R.id.ll_commodity_management);
        ll_user_pinjia = view.findViewById(R.id.ll_user_pinjia);
        ll_caiwu = view.findViewById(R.id.ll_caiwu);
        ll_active_configuration = view.findViewById(R.id.ll_active_configuration);
        ll_avertisement = view.findViewById(R.id.ll_avertisement);//广告
        ll_speed_refund = view.findViewById(R.id.ll_speed_refund);
        ll_jingying_shuju = view.findViewById(R.id.ll_jingying_shuju);//经营数据
        // 轮播图
        convenientBanner = view.findViewById(R.id.convenientBanner);
        // 改线ViewPager的高度
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) convenientBanner.getLayoutParams();
        linearParams.height = (int) (Utility.getScreenWidthforPX(getActivity()) / 2.42f);
        convenientBanner.setLayoutParams(linearParams);

        convenientBanner.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                    }
                });
        //订单的收入和数量
        netService.todayOrder(storeId, Handle_todayOrder);
    }

    private void initEvent(View view) {
        ll_message.setOnClickListener(this);
        ll_commodity_management.setOnClickListener(this);
        ll_user_pinjia.setOnClickListener(this);
        ll_caiwu.setOnClickListener(this);
        ll_active_configuration.setOnClickListener(this);
        ll_avertisement.setOnClickListener(this);
        ll_speed_refund.setOnClickListener(this);
        ll_jingying_shuju.setOnClickListener(this);
    }

    //返回刷新商户收入
    @Override
    public void onResume() {
        super.onResume();
        String money = aCache.getAsString("money");
        String num = aCache.getAsString("num");
//        Log.i("money", money + "--");
        if (Valid.isNotNullOrEmpty(money)) {
            if (money.equals("0")) {
                tv_order_money.setText("0.00");
            } else {
                tv_order_money.setText(AppUtil.douTwo(Double.parseDouble(money)));
            }
            tv_order_number.setText(num);
        }
    }

    //刷新商家收入信息
    public static class MyMessageReceivernew extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ACache aCache = ACache.get(context);
            String money = aCache.getAsString("money");
            String num = aCache.getAsString("num");
//            Log.i("money", money + "--");
            if (Valid.isNotNullOrEmpty(money)) {
                if (money.equals("0")) {
                    tv_order_money.setText("0.00");
                } else {
                    tv_order_money.setText(AppUtil.douTwo(Double.parseDouble(money)));
                }
                tv_order_number.setText(num);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_message://消息中心
                Intent intentMessageCenter = new Intent(getActivity(), MessageCenter.class);
                startActivity(intentMessageCenter);
                break;
            case R.id.ll_commodity_management://商品管理
                Intent intentcommodity_management = new Intent(getActivity(), CommodityManage.class);
                startActivity(intentcommodity_management);
                break;
            case R.id.ll_user_pinjia://评价
                Intent intentUserEvaluation = new Intent(getActivity(), UserEvaluationManage.class);
                startActivity(intentUserEvaluation);
                break;
            case R.id.ll_caiwu://财务对账
                Intent intentCaiwu = new Intent(getActivity(), Caiwu.class);
                startActivity(intentCaiwu);
                break;
            case R.id.ll_active_configuration://活动配置
                Intent intentActiveConfiguration = new Intent(getActivity(), ActiveConfiguration.class);
                startActivity(intentActiveConfiguration);
                break;
            case R.id.ll_avertisement://广告推送
                Intent intentAvertisement = new Intent(getActivity(), Avertisement.class);
                startActivity(intentAvertisement);
                break;
            case R.id.ll_speed_refund://极速退款
//                Intent intentSpeedRefund = new Intent(getActivity(), SpeedRefund.class);
//                startActivity(intentSpeedRefund);
                break;
            case R.id.ll_jingying_shuju://经营数据
                Intent intentJingyingShuju = new Intent(getActivity(), JingyingShuju.class);//
                startActivity(intentJingyingShuju);
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handlerBanner = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        banners.addAll((Collection<? extends StoreOperations_Banner>) msg.obj);
                        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                            @Override
                            public NetworkImageHolderView createHolder() {
                                return new NetworkImageHolderView();
                            }
                        }, banners);
                        //开始自动翻页
                        convenientBanner.startTurning(3000);

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
    @SuppressLint("HandlerLeak")
    Handler Handle_todayOrder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        TodayOrder todayOrder = (TodayOrder) msg.obj;
                        if (Valid.isNotNullOrEmpty(todayOrder.money)) {
                            if (todayOrder.money.equals("0")) {
                                tv_order_money.setText("0.00");
                            } else {
                                tv_order_money.setText(AppUtil.douTwo(Double.parseDouble(todayOrder.money)));
                            }
                            tv_order_number.setText(todayOrder.num);
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
    };
}
