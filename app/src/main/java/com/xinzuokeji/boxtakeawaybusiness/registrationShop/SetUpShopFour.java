package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.BankInfo;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.BankCardTextWatcher;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.util.ArrayList;
import java.util.List;

//收款信息
public class SetUpShopFour extends BaseActivity {
    private ImageButton header_back;
    NetService netService;
    private CheckBox ck_account_public, ck_account_private;
    private EditText etaccount_name, etaccount_card;
    private TextView tv_choose_bank;
    private Button bt_finish_shop;
    private LinearLayout ll_bank_card;
    //银行名字
    private String bankName, bankCode;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_set_up_shop_four);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back.setVisibility(View.VISIBLE);
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("收款信息");
        textView.setTextColor(getResources().getColor(R.color.white));
        ll_bank_card = findViewById(R.id.ll_bank_card);
//        账户对公/对私
        ck_account_public = findViewById(R.id.ck_public);
        ck_account_private = findViewById(R.id.ck_private);
        ck_account_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_account_private.setChecked(true);
                ck_account_public.setChecked(false);
            }
        });
        ck_account_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_account_private.setChecked(false);
                ck_account_public.setChecked(true);
            }
        });

        //开户人姓名和账号
        etaccount_name = findViewById(R.id.et_account_name);
        etaccount_card = findViewById(R.id.et_account_card);
        //绑定监听银行卡四位空格
        BankCardTextWatcher.bind(etaccount_card);
        etaccount_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                etaccount_name.getText().toString().trim();
            }
        });
        etaccount_card.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                etaccount_card.getText().toString().trim();
            }
        });
        tv_choose_bank = findViewById(R.id.tv_choose_bank);
//        完成申请
        bt_finish_shop = findViewById(R.id.bt_finish_shop);

        netService.showBank(getChangeStoreId(), handler_showBank);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_finish_shop.setOnClickListener(this);
        ll_bank_card.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_bank_card://获取银行卡列表
                Intent intent = new Intent(this, BankCard.class);
                intent.putExtra("bank", "");
                startActivityForResult(intent, 1);
                break;
            case R.id.bt_finish_shop:
                //  账户类型 1:对公 2:对私
                int accountType = 1;
                if (ck_account_private.isChecked()) {
                    accountType = 2;
                }
                if (ck_account_public.isChecked()) {
                    accountType = 1;
                }
                if (Valid.isNotNullOrEmpty(etaccount_name.getText().toString().trim()) && Valid.isNotNullOrEmpty(etaccount_card.getText().toString().trim()) && Valid.isNotNullOrEmpty(bankName)) {
                    //  验证是否为银行卡
                    if (Valid.checkBankCard(etaccount_card.getText().toString().trim().replace(" ", ""))) {
                        netService.addBank(getChangeStoreId(), accountType, etaccount_name.getText().toString().trim(), etaccount_card.getText().toString().trim().replace(" ", ""), "", bankCode, bankName, handler_addBank);
                    } else {
                        showTip("请正确填写银行卡信息", Toast.LENGTH_SHORT);
                    }
                } else {
                    showTip("请将信息填写完整", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case 1:
                if (data != null) {
                    bankName = data.getStringExtra("bank_name");
                    bankCode = data.getStringExtra("bankCode");
                    tv_choose_bank.setText(bankName);

                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 列表
     */
    private void dialogList() {
        final String items[] = {"刘德华", "张柏芝", "蔡依林", "张学友"};
        List<String> strings = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(this, 3);
        builder.setTitle("列表");
        // builder.setMessage("是否确认退出?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);
        // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(SetUpShopFour.this, items[which],
                        Toast.LENGTH_SHORT).show();

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(SetUpShopFour.this, "确定", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        builder.create().show();
    }

    @SuppressLint("HandlerLeak")
    Handler handler_showBank = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        com.xinzuokeji.boxtakeawaybusiness.entities.BankInfo bankInfo = (BankInfo) msg.obj;
                        if (bankInfo.account_type.equals("1")) {
                            ck_account_public.setChecked(true);
                            ck_account_private.setChecked(false);
                        }
                        if (bankInfo.account_type.equals("2")) {
                            ck_account_private.setChecked(true);
                            ck_account_public.setChecked(false);
                        }
                        etaccount_name.setText(bankInfo.name);
                        etaccount_card.setText(bankInfo.card);
                        tv_choose_bank.setText(bankInfo.opening_bank);
                        bankName = bankInfo.opening_bank;
                        bankCode = bankInfo.bank_code;
                    }
                    break;
                case 2001:

                    break;
                case 1001:
//                    showTip(getString(R.string.network_error), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler_addBank = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    SetUpShopOne.mActivity.finish();
                    SetUpShopTwo.mActivity.finish();
                    SetUpShopThree.mActivity.finish();
                    Intent intent = new Intent(SetUpShopFour.this, ApplicationRecord.class);
//                    if (intent.resolveActivity(getPackageManager()) != null) {
//                        ApplicationRecord.mActivity.finish();
//                        // 说明系统中不存在这个activity
//                    }
                    startActivity(intent);
                    finish();
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
        }
    };
}
