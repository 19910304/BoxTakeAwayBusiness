package com.xinzuokeji.boxtakeawaybusiness.storeOperations.caiwu;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.all_order;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.netutil.JSONDeserialize;
import com.xinzuokeji.boxtakeawaybusiness.util.AppUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//财务对账
public class Caiwu extends BaseActivity {
    private ImageButton header_back;
    TextView tv_recharge, tv_tixian;
    LinearLayout ll_balance_watercourse, ll_account, ll_action_header, ll_order_history, ll_today_order_de;
    NetService netService;
    TextView tv_yu_e, tv_today_all_earnings, tv_waima_money, tv_rebates_money, tv_all_num, tv_all_number_refund;
    private ListView lv_order_history;
    private List<com.xinzuokeji.boxtakeawaybusiness.entities.all_order> allOrderList = new ArrayList<>();

    ListViewAdapter<com.xinzuokeji.boxtakeawaybusiness.entities.all_order> allOrderListViewAdapter;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_caiwu);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        // 返回按钮
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);
        TextView textView = findViewById(R.id.header_title);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setText("财务对账");
        ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        tv_recharge = findViewById(R.id.tv_recharge);
        tv_tixian = findViewById(R.id.tv_tixian);
        ll_balance_watercourse = findViewById(R.id.ll_balance_watercourse);
        ll_account = findViewById(R.id.ll_account);
        ll_order_history = findViewById(R.id.ll_order_history);
        ll_today_order_de = findViewById(R.id.ll_today_order_de);

        tv_yu_e = findViewById(R.id.tv_yu_e);
        tv_today_all_earnings = findViewById(R.id.tv_today_all_earnings);
        tv_waima_money = findViewById(R.id.tv_waima_money);
        tv_rebates_money = findViewById(R.id.tv_rebates_money);
        tv_all_num = findViewById(R.id.tv_all_num);
        tv_all_number_refund = findViewById(R.id.tv_all_number_refund);
        // 历史订单
        lv_order_history = findViewById(R.id.lv_order_history);
        lv_order_history.setFocusable(false);
        allOrderListViewAdapter = new ListViewAdapter<com.xinzuokeji.boxtakeawaybusiness.entities.all_order>(this, R.layout.item_order_history_item, allOrderList) {
            @Override
            public void convert(ViewHolder holder, com.xinzuokeji.boxtakeawaybusiness.entities.all_order allOrder, int id, int selectedPosition) {
                holder.setText(R.id.tv_order_day, allOrder.delivery_time);//订单日期
//                holder.setText(R.id.tv_order_all_money, "￥ " + allOrder.total_fee);//订单当天钱数
                TextView total_fee = holder.getView(R.id.tv_order_all_money);
                //订单当天钱数6表示已完成订单其他退款
                if (allOrder.status.equals("6")) {
                    total_fee.setText("+￥ " + allOrder.total_fee);
                } else {
                    total_fee.setText("-￥ " + allOrder.total_fee);
                }
            }
        };
        // 点击进入订单详情
        lv_order_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                showTip("订单详情", Toast.LENGTH_SHORT);
                HashMap<String, String> extras = new HashMap<>();
                extras.put("order_id", allOrderList.get(i).id);
                try {
                    gotoActivity(OrderDetails.class.getName(), extras);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        lv_order_history.setAdapter(allOrderListViewAdapter);
        allOrderListViewAdapter.notifyDataSetChanged();
        netService.Finance(GetstoreId(), Hander_Finance);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        tv_recharge.setOnClickListener(this);
        tv_tixian.setOnClickListener(this);
        ll_balance_watercourse.setOnClickListener(this);
        ll_account.setOnClickListener(this);
        ll_order_history.setOnClickListener(this);
        ll_today_order_de.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.tv_recharge://充值
                try {
                    gotoActivity(Rearch.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.tv_tixian://提现
                try {
                    gotoActivity(Withdraw.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_balance_watercourse://流水
                try {
                    gotoActivity(Watercourse.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_account://账户信息
                try {
                    gotoActivity(Account.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_order_history://历史订单
                try {
                    gotoActivity(OrderHistory.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_today_order_de://今日订单详情
                try {
                    gotoActivity(TodayOrderDetails.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    Handler Hander_Finance = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        //保留两位小数
                        tv_yu_e.setText("￥ " + AppUtil.douTwo(Double.parseDouble(jsonObject.getString("money"))));
                        tv_today_all_earnings.setText("￥ " + jsonObject.getString("takeout_order"));
                        tv_waima_money.setText("￥ " + jsonObject.getString("takeout_order"));
                        tv_rebates_money.setText("￥ " + jsonObject.getString("refund_order"));
                        tv_all_num.setText("共" + jsonObject.getString("takeout_ordernum") + "笔");
                        tv_all_number_refund.setText("共" + jsonObject.getString("refund_ordernum") + "笔");
                        //所有订单
                        allOrderList.clear();
                        allOrderList.addAll(new JSONDeserialize<>(all_order.class, jsonObject.getString("all_order")).toObjects());
                        //重新计算listview高度
                        setPullLvHeight(lv_order_history);
                        allOrderListViewAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
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
