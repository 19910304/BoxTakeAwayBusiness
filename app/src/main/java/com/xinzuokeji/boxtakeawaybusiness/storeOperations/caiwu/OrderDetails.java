package com.xinzuokeji.boxtakeawaybusiness.storeOperations.caiwu;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.goods;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.netutil.JSONDeserialize;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/23.
 */
//财务订单详细页
public class OrderDetails extends BaseActivity {
    private ImageButton header_back;
    private ListView lv_caipin;
    private List<goods> mDate = new ArrayList<>();
    private ListViewAdapter<goods> listViewAdapter;
    NetService netService;
    String order_id;
    private TextView lunch_box_fee, tv_distribution_fee, tv_total_fee, tv_discount, tv_total_fee1, tv_yongjin, tv_order_bianhao, tv_ordernum, tv_add_time, tv_delivery_time, tv_distribution;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_order_details);
        order_id = getIntent().getStringExtra("order_id");
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("订单详情");
        tv_total_fee = findViewById(R.id.tv_total_fee);
        tv_discount = findViewById(R.id.tv_discount);
        tv_total_fee1 = findViewById(R.id.tv_total_fee1);
        tv_yongjin = findViewById(R.id.tv_yongjin);
        tv_order_bianhao = findViewById(R.id.tv_order_bianhao);
        tv_ordernum = findViewById(R.id.tv_ordernum);
        tv_add_time = findViewById(R.id.tv_add_time);
        tv_delivery_time = findViewById(R.id.tv_delivery_time);
        tv_distribution = findViewById(R.id.tv_distribution);
        lv_caipin = findViewById(R.id.lv_shangpin);
        listViewAdapter = new ListViewAdapter<goods>(this, R.layout.item_good, mDate) {
            @Override
            public void convert(ViewHolder holder, goods s, int id, int selectedPosition) {
                TextView tv_dishes = holder.getView(R.id.tv_dishes);
                TextView tv_dishes_number = holder.getView(R.id.tv_dishes_number);
                TextView tv_dishes_prices = holder.getView(R.id.tv_dishes_prices);
                tv_dishes_prices.setText("￥" + s.sale);
                tv_dishes_number.setText("x" + s.num);
                tv_dishes.setText(s.goods_name);
            }
        };
        lv_caipin.setAdapter(listViewAdapter);

//        listViewAdapter.notifyDataSetChanged();
        netService.OrderListDe(Integer.parseInt(order_id), OrderListDe);
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
    Handler OrderListDe = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        //  餐盒费
                        lunch_box_fee.setText("+￥ " + jsonObject.getString("lunch_box_fee"));
                        tv_total_fee.setText("￥ " + jsonObject.getString("total_fee"));//订单结算金额
                        tv_discount.setText("-￥ " + jsonObject.getString("discount"));//商家活动支出
                        tv_total_fee1.setText("+￥ " + jsonObject.getString("total_fee"));//订单结算金额
                        tv_yongjin.setText("-￥ " + jsonObject.getString("service"));//平台佣金
                        tv_add_time.setText(jsonObject.getString("add_time"));//下单时间
                        tv_delivery_time.setText(jsonObject.getString("delivery_time"));//订单完成时间
                        //配送方式
                        String distributionStyle = jsonObject.getString("distribution_info");
                        tv_distribution.setText(jsonObject.getString("distribution"));
                        tv_ordernum.setText(jsonObject.getString("ordernum"));//订单号
                        tv_order_bianhao.setText("#" + jsonObject.getString("odd_numbers"));//订单编号
                        //平台配送负，商家自配正
                        //  订单配送费
                        String distribution_fee = jsonObject.getString("distribution_fee");
                        if (distributionStyle.equals("1")) {
                            tv_distribution_fee.setText("-￥ " + distribution_fee);
                        }
                        if (distributionStyle.equals("0")) {
                            tv_distribution_fee.setText("+￥ " + distribution_fee);
                        }
                        //所有订单
                        mDate.clear();
                        mDate.addAll(new JSONDeserialize<>(goods.class, jsonObject.getString("goods")).toObjects());
                        //重新计算listview高度
                        setPullLvHeight(lv_caipin);
                        listViewAdapter.notifyDataSetChanged();

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
