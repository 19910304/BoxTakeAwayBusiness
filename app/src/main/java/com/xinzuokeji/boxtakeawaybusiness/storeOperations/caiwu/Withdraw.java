package com.xinzuokeji.boxtakeawaybusiness.storeOperations.caiwu;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.AppUtil;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.lang.ref.WeakReference;

//提现
public class Withdraw extends BaseActivity {
    private ImageButton header_back;
    LinearLayout ll_bank_card;
    public TextView tv_bank_card, tv_withdraw_money, tv_all_withdraw;
    EditText et_money;
    Button bt_withdraw;
    // 实际提现金额
    private String withdrawMoney;
    private NetService netService;
    // 可提现全部金额
    private String allMoney;
    com.xinzuokeji.boxtakeawaybusiness.entities.Withdraw withdraw;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_withdraw);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("提现");
        ll_bank_card = findViewById(R.id.ll_bank_card);
        tv_bank_card = findViewById(R.id.tv_bank_card);
        tv_withdraw_money = findViewById(R.id.tv_withdraw_money);
        et_money = findViewById(R.id.et_money);
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                withdrawMoney = et_money.getText().toString().trim();


            }
        });
        tv_all_withdraw = findViewById(R.id.tv_all_withdraw);
        bt_withdraw = findViewById(R.id.bt_withdraw);
        //显示提现信息
        netService.withdraw(GetstoreId(), handler_withdraw);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_bank_card.setOnClickListener(this);
        tv_all_withdraw.setOnClickListener(this);
        bt_withdraw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.header_back:
                finish();
                break;
            case R.id.ll_bank_card:
                //选择银行卡
                break;
            case R.id.tv_all_withdraw://全部提现
                et_money.setText(allMoney);
                break;
            case R.id.bt_withdraw://提现
                if (Valid.isNotNullOrEmpty(withdrawMoney)) {
                    if (Double.parseDouble(withdrawMoney) > 0.0) {
                        if (Valid.isNotNullOrEmpty(withdraw.bank_card) && Valid.isNotNullOrEmpty(withdraw.bank_code)) {
                            netService.setshopPutForward(GetstoreId(), Double.parseDouble(withdrawMoney), withdraw.bank_card, Integer.parseInt(withdraw.bank_code), Hander_setshopPutForward);
                        } else {
                            showTip("银行卡信息有误", Toast.LENGTH_SHORT);
                        }
                    } else {
                        showTip("请输入提现金额", Toast.LENGTH_SHORT);
                    }
                } else {
                    showTip("请输入提现金额", Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    //显示提现信息
    // 内部类声明弱引用<引用外部类>对象
    // 内部类构造时创建”弱引用<引用外部类>”对象
    // 内部类的方法通过弱引用获取外部类对象,进行判断非空再操作
    private final WithdrawHandler handler_withdraw = new WithdrawHandler(this);

    private static class WithdrawHandler extends Handler {
        private final WeakReference<Withdraw> mActivity;

        public WithdrawHandler(Withdraw activity) {
            mActivity = new WeakReference<Withdraw>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Withdraw activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 2000:
                        if (msg.obj != null) {
                            activity.withdraw = (com.xinzuokeji.boxtakeawaybusiness.entities.Withdraw) msg.obj;
                            //银行卡名字和可提现的金额
                            activity.tv_bank_card.setText(activity.withdraw.bank_name);
                            if (Valid.isNotNullOrEmpty(activity.withdraw.money)) {
                                activity.tv_withdraw_money.setText(AppUtil.douTwo(Double.parseDouble(activity.withdraw.money)));
                                activity.allMoney = AppUtil.douTwo(Double.parseDouble(activity.withdraw.money));
                            }

                        }
                        break;
                    case 2001:
                        Log.d("msd", "---22");
                        break;
                    case 1001:
                        Log.d("msd", "---33");
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);

            }
        }
    }

    //申请提现返回结果
    private final SetshopPutForwardHandler Hander_setshopPutForward = new SetshopPutForwardHandler(this);

    private static class SetshopPutForwardHandler extends Handler {
        private final WeakReference<Withdraw> mActivity;

        public SetshopPutForwardHandler(Withdraw activity) {
            mActivity = new WeakReference<Withdraw>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Withdraw activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 2000:
                        activity.showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                        activity.finish();
                        break;
                    case 2001:
                        activity.showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                        Log.d("msd", "---22");
                        break;
                    case 1001:
                        Log.d("msd", "---33");
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);

            }
        }
    }

}
