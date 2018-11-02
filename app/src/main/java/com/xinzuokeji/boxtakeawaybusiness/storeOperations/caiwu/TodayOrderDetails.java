package com.xinzuokeji.boxtakeawaybusiness.storeOperations.caiwu;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.out_order;
import com.xinzuokeji.boxtakeawaybusiness.entities.refund_order;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.netutil.JSONDeserialize;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/7/19.
 */
//今日订单明细
public class TodayOrderDetails extends BaseActivity {
    private ImageButton header_back;
    private List<out_order> mDateOrder = new ArrayList<>();
    private List<refund_order> mDateRefund = new ArrayList<>();
    private ListViewAdapter<out_order> listViewAdapterOrder;
    private ListViewAdapter<refund_order> listViewAdapterRefund;
    private ListView lvOrder, lvRefund;
    private NetService netService;
    private TextView tv_today_time, tv_today_all_money, tv_all_num, tv_waima_money, tv_all_number_refund, tv_rebates_money;
    //  退款订单和正常订单的标题
    private LinearLayout ll_out_order, ll_refund_order;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_today_order_details);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("订单明细");
        tv_today_time = findViewById(R.id.tv_today_time);
        tv_today_all_money = findViewById(R.id.tv_today_all_money);
        tv_all_num = findViewById(R.id.tv_all_num);
        tv_all_number_refund = findViewById(R.id.tv_all_number_refund);
        tv_waima_money = findViewById(R.id.tv_waima_money);
        tv_rebates_money = findViewById(R.id.tv_rebates_money);

        lvOrder = findViewById(R.id.lv_order_item);
        lvRefund = findViewById(R.id.lv_refund_order_item);
        //正常订单
        listViewAdapterOrder = new ListViewAdapter<out_order>(this, R.layout.item_order_history_item, mDateOrder) {
            @Override
            public void convert(ViewHolder holder, out_order outOrder, int id, int selectedPosition) {
                holder.setText(R.id.tv_order_day, outOrder.delivery_time);
                holder.setText(R.id.tv_order_all_money, "￥" +doubleDigit(Double.parseDouble(outOrder.total_fee)));
            }
        };
        //点击进入详细页
        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> extras = new HashMap<>();
                extras.put("order_id", mDateOrder.get(i).order_id);
                try {
                    gotoActivity(OrderDetails.class.getName(), extras);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        lvOrder.setAdapter(listViewAdapterOrder);
        setPullLvHeight(lvOrder);
        listViewAdapterOrder.notifyDataSetChanged();
        // 退款
        listViewAdapterRefund = new ListViewAdapter<refund_order>(this, R.layout.item_order_history, mDateRefund) {
            @Override
            public void convert(ViewHolder holder, refund_order refundOrder, int id, int selectedPosition) {
                holder.setText(R.id.tv_order_day, refundOrder.add_time);
                holder.setText(R.id.tv_order_all_money, "￥" + doubleDigit(Double.parseDouble(refundOrder.money)));
            }
        };
        //点击进入详细页
        lvRefund.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> extras = new HashMap<>();
                extras.put("order_id", mDateRefund.get(i).order_id);
                try {
                    gotoActivity(OrderDetails.class.getName(), extras);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        lvRefund.setAdapter(listViewAdapterRefund);
        setPullLvHeight(lvRefund);
        listViewAdapterRefund.notifyDataSetChanged();
        netService.TodayOrderList(GetstoreId(), hander_TodayOrderList);
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


    @SuppressLint("HandlerLeak")
    Handler hander_TodayOrderList = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        //所有订单
                        mDateOrder.clear();
                        mDateOrder.addAll(new JSONDeserialize<>(out_order.class, jsonObject.getString("out_order")).toObjects());
                        //重新计算listview高度
                        listViewAdapterOrder.notifyDataSetChanged();
                        mDateRefund.clear();
                        mDateRefund.addAll(new JSONDeserialize<>(refund_order.class, jsonObject.getString("refund_order")).toObjects());
                        //重新计算listview高度
//                        Log.i("mDateRefund", mDateRefund.size() + "---");
                        listViewAdapterRefund.notifyDataSetChanged();
                        if (mDateOrder.size() != 0) {
                            ll_out_order.setVisibility(View.VISIBLE);
                        } else {
                            ll_out_order.setVisibility(View.GONE);
                        }
                        if (mDateRefund.size() != 0) {
                            ll_refund_order.setVisibility(View.VISIBLE);
                        } else {
                            ll_refund_order.setVisibility(View.GONE);
                        }
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
