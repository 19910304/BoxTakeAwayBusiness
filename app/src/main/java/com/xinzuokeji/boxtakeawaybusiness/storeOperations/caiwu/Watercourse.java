package com.xinzuokeji.boxtakeawaybusiness.storeOperations.caiwu;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.BalanceWater;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;

import java.lang.ref.WeakReference;
import java.util.HashMap;

//查看余额流水
public class Watercourse extends BaseActivity {
    private ImageButton header_back;
    // 点击进入流水列表
    private LinearLayout ll_tixian, ll_balance_watercourse;
    // 提现总数
    private TextView tv_withdraw, tv_balance;
    private NetService netService;

    //六种类型：1支付宝，2，微信，4，提现,6余额流水
    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_watercourse);
        // 实例化
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("查看余额流水");
        netService.balanceWater(GetstoreId(), balanceWaterHander);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_tixian.setOnClickListener(this);
        ll_balance_watercourse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_tixian://跳转提现列表
                HashMap<String, String> extras = new HashMap<>();
                extras.put("waterStyle", "4");
                try {
                    gotoActivity(Watercoureslist.class.getName(), extras);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_balance_watercourse://跳转余额列表
                HashMap<String, String> extras_balance = new HashMap<>();
                extras_balance.put("waterStyle", "6");
                try {
                    gotoActivity(Watercoureslist.class.getName(), extras_balance);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    //弱引用，避免内存溢出
    private final BalanceWaterHandler balanceWaterHander = new BalanceWaterHandler(this);

    private static class BalanceWaterHandler extends Handler {
        private final WeakReference<Watercourse> mActivity;

        public BalanceWaterHandler(Watercourse activity) {
            mActivity = new WeakReference<Watercourse>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Watercourse activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 2000:
                        BalanceWater balanceWater = (BalanceWater) msg.obj;
                        // 提现总额
                        activity.tv_withdraw.setText("-￥" + activity.doubleDigit(Double.parseDouble(balanceWater.put_forward)));
                        //余额总数
                        activity.tv_balance.setText("+￥" + activity.doubleDigit(Double.parseDouble(balanceWater.account_log)));
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
