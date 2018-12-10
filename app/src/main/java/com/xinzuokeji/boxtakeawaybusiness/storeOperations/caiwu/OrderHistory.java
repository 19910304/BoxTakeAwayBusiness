package com.xinzuokeji.boxtakeawaybusiness.storeOperations.caiwu;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.all_order;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;
import com.xinzuokeji.boxtakeawaybusiness.util.listviewshangxia.XListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/7/19.
 */
//历史订单
public class OrderHistory extends BaseActivity implements XListView.IXListViewListener {
    private ImageButton header_back;
    ListViewAdapter<all_order> listViewAdapter;
    private XListView xListView;
    private List<all_order> mDate;
    NetService netService;
    private Handler mHandler;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_order_history);
        mHandler = new Handler();
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("历史订单");
        xListView = findViewById(R.id.xlv_order_history);
        // 设置xlistview判断是否出现“查看更多”
        xListView.setPullLoadEnable(false);
        xListView.setPullRefreshEnable(true);
        mDate = new ArrayList<>();
        listViewAdapter = new ListViewAdapter<all_order>(this, R.layout.item_order_history, mDate) {
            @Override
            public void convert(ViewHolder holder, all_order allOrder, int id, int selectedPosition) {
                TextView tv_time_type = holder.getView(R.id.tv_order_time);
                if (allOrder != null) {
                    if (Valid.isNotNullOrEmpty(allOrder.delivery_time)) {
                        //避免字符串截取错误，判断字符串长度必须大于等于截取长度
                        if (allOrder.delivery_time.length() >= 8) {
                            String timeType1 = allOrder.delivery_time.substring(0, 7);
                            if (mDate.size() != 0) {
                                if (id == 0) {//id等于0显示月标签且当前时间等于返回时间
                                    tv_time_type.setVisibility(View.VISIBLE);
                                    tv_time_type.setText(timeType1);
                                } else {//时间年不同或者月不同显示标签
                                    if (timeType1.equals(mDate.get(id - 1).delivery_time.substring(0, 7))) {
                                        tv_time_type.setVisibility(View.GONE);
                                    } else {
                                        tv_time_type.setVisibility(View.VISIBLE);
                                        tv_time_type.setText(timeType1);
                                    }
                                }
                            }
                        }
                    }
                    // 日期
                    holder.setText(R.id.tv_order_day, allOrder.delivery_time);
                    TextView total_fee = holder.getView(R.id.tv_order_all_money);
                    //订单当天钱数6表示已完成订单其他退款
                    if (allOrder.status.equals("6")) {
                        total_fee.setText("+￥ " + allOrder.total_fee);
                    } else {
                        total_fee.setText("-￥ " + allOrder.total_fee);
                    }
                }
            }
        };
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> extras = new HashMap<>();
                extras.put("order_id", mDate.get(i - 1).id);
                try {
                    gotoActivity(OrderDetails.class.getName(), extras);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        xListView.setAdapter(listViewAdapter);
        xListView.setXListViewListener(this);
        netService.OrderHistoryList(GetstoreId(), OrderHistoryList);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                netService.OrderHistoryList(GetstoreId(), OrderHistoryList);
                xListView.stopLoadMore();
                xListView.stopRefresh();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDate.size() >= 1000) {
                    xListView.setPullLoadEnable(true);
                }
                xListView.stopLoadMore();
                xListView.stopRefresh();
            }
        }, 2000);
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
    Handler OrderHistoryList = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    //所有订单
                    if (msg.obj.toString().equals("[]")) {
                        mDate.clear();
                        listViewAdapter.notifyDataSetChanged();
                    } else {
                        mDate.clear();
                        mDate.addAll((Collection<? extends all_order>) msg.obj);
                        listViewAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
                    showTip(getString(R.string.network_error), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
