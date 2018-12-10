package com.xinzuokeji.boxtakeawaybusiness.storeOperations.caiwu;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.BalanceWaterList;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.listviewshangxia.XListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2018/10/20.
 */
//流水的列表
public class Watercoureslist extends BaseActivity implements XListView.IXListViewListener {
    private ImageButton header_back;
    private XListView xlv_watercourse;
    private ListViewAdapter<BalanceWaterList> listViewAdapter;
    private List<BalanceWaterList> mData;
    private Handler mHandler;
    private String style;
    NetService netService;

    //style余额流水类型 1支付宝充值 2微信充值 3广告推送 4提现 5余额充值,6余额进账
    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_watercourse_list);
        netService = new NetService(this);
        mHandler = new Handler();
        // 接收类型
        style = getIntent().getStringExtra("waterStyle");
        // title
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        if (style.equals("4")) {
            textView.setText("提现");
        }
        if (style.equals("6")) {
            textView.setText("余额");
        }
        // 设置xlistview判断是否出现“查看更多”
        xlv_watercourse.setPullLoadEnable(false);
        xlv_watercourse.setPullRefreshEnable(true);
        mData = new ArrayList<>();
        listViewAdapter = new ListViewAdapter<BalanceWaterList>(this, R.layout.item_watercourse, mData) {
            @Override
            public void convert(ViewHolder holder, BalanceWaterList balanceWaterList, int id, int selectedPosition) {
                // 图标
                ImageView img_style = holder.getView(R.id.img_style);
                //五种类型
                TextView tv_money_style = holder.getView(R.id.tv_money_style);
                //时间
                TextView tv_time = holder.getView(R.id.tv_time);
                //钱数
                TextView tv_money = holder.getView(R.id.tv_money);
                //提现状态（其他类型没有）
                TextView tv_money_status = holder.getView(R.id.tv_money_status);
                //style余额流水类型 1支付宝充值 2微信充值 3广告推送 4提现 5余额充值,6余额进账
                if (style.equals("4")) {
                    //status  提现状态 1 提交成功 2 审核成功 3 审核失败 4 已受理 5 提现失败 6 提现成功
                    img_style.setBackgroundResource(R.mipmap.tixian);
                    if (balanceWaterList.status.equals("1")) {
                        tv_money_status.setText("提交成功");
                    }
                    if (balanceWaterList.status.equals("2")) {
                        tv_money_status.setText("审核成功");
                    }
                    if (balanceWaterList.status.equals("3")) {
                        tv_money_status.setText("审核失败");
                    }
                    if (balanceWaterList.status.equals("4")) {
                        tv_money_status.setText("已受理");
                    }
                    if (balanceWaterList.status.equals("5")) {
                        tv_money_status.setText("提现失败");
                    }
                    if (balanceWaterList.status.equals("6")) {
                        tv_money_status.setText("提现成功");
                    }
                    tv_money_style.setText("提现");
                    //时间
                    tv_time.setText(balanceWaterList.time);
                    // 钱数
                    tv_money.setText(String.valueOf(doubleDigit(Double.parseDouble(balanceWaterList.money))));
                }
                if (style.equals("6")) {
                    tv_money_style.setText("余额");
                    img_style.setBackgroundResource(R.mipmap.balance_recharge);
                    //时间
                    tv_time.setText(balanceWaterList.add_time);
                    // 钱数
                    if (balanceWaterList.type.equals("3")) {
                        tv_money.setText("+" + String.valueOf(doubleDigit(Double.parseDouble(balanceWaterList.account_current))));
                    } else {
                        tv_money.setText("-" + String.valueOf(doubleDigit(Double.parseDouble(balanceWaterList.account_current))));
                    }

                }
            }
        };
        xlv_watercourse.setAdapter(listViewAdapter);
        xlv_watercourse.setXListViewListener(this);
        listViewAdapter.notifyDataSetChanged();
        netService.balanceWaterList(GetstoreId(), Integer.parseInt(style), balanceWaterlsitHander);
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

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                netService.balanceWaterList(GetstoreId(), Integer.parseInt(style), balanceWaterlsitHander);
                xlv_watercourse.stopLoadMore();
                xlv_watercourse.stopRefresh();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mData.size() >= 1000) {
                    xlv_watercourse.setPullLoadEnable(true);
                }
                xlv_watercourse.stopLoadMore();
                xlv_watercourse.stopRefresh();
            }
        }, 1000);
    }

    //弱引用，避免内存溢出
    private final BalanceWaterListHandler balanceWaterlsitHander = new BalanceWaterListHandler(this);

    private static class BalanceWaterListHandler extends Handler {
        private final WeakReference<Watercoureslist> mActivity;

        private BalanceWaterListHandler(Watercoureslist activity) {
            mActivity = new WeakReference<Watercoureslist>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Watercoureslist activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 2000:
                        if (msg.obj != null) {
                            activity.mData.clear();
                            activity.mData.addAll((Collection<? extends BalanceWaterList>) msg.obj);
                            activity.listViewAdapter.notifyDataSetChanged();
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
        }
    }
}
