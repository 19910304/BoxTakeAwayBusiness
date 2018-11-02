package com.xinzuokeji.boxtakeawaybusiness.storeOperations.caiwu;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.AletDialog;

/**
 * Created by Administrator on 2018/7/18.
 */
//账户结算信息
public class Account extends BaseActivity {
    private ImageButton header_back;
    NetService netService;
    private TextView tv_bank_name, tv_bank_account, tv_account_name;
    private LinearLayout ll_change_bank_info;
    com.xinzuokeji.boxtakeawaybusiness.entities.Withdraw withdraw;

    @Override
    public void initView() {
        super.initView();
        netService = new NetService(this);
        setContentView(R.layout.activity_account);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("账户结算信息");
        netService.withdraw(GetstoreId(), handler_withdraw);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_change_bank_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_change_bank_info://打电话修改银行卡信息《low》
                Logout();
                break;
            default:
                break;
        }
    }

    //拨打业务经理电话
    private void Logout() {
        final AletDialog dialog = new AletDialog(this, getString(R.string.change_bank_info), getString(R.string.bank_contents), getString(R.string.cancel), getString(R.string.call_jingli), new AletDialog.OnAlertDialogListener() {
            @Override
            public void Confirm() {
                diallPhone("123");

            }
        });
        try {
            dialog.getWindow().setWindowAnimations(R.style.DialogOutAndInStyle);   //设置dialog的显示动画
        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.show();


    }


    @SuppressLint("HandlerLeak")
    Handler handler_withdraw = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        withdraw = (com.xinzuokeji.boxtakeawaybusiness.entities.Withdraw) msg.obj;
                        //银行卡名字和卡号以及 用户名
                        tv_bank_name.setText(withdraw.bank_name);
                        tv_bank_account.setText(withdraw.bank_card);
                        tv_account_name.setText(withdraw.name);
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
    };
}
