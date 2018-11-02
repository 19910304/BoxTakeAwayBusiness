package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.BankName;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2018/8/20.
 */
//获取银行列表
public class BankCard extends BaseActivity {
    private ImageButton header_back;
    ListView lv_bank_card;

    private List<BankName> mDate = new ArrayList<>();
    private ListViewAdapter<BankName> listViewAdapter;
    NetService netService;
    private String bank_name, bankCode;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_bank_card);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("银行列表");
        lv_bank_card = findViewById(R.id.lv_bank_card);
        listViewAdapter = new ListViewAdapter<BankName>(this, R.layout.item_bank_card, mDate) {
            @Override
            public void convert(ViewHolder holder, BankName bankName, int id, int selectedPosition) {
                holder.setText(R.id.tv_bank_name, bankName.bank_name);
            }
        };
        lv_bank_card.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bank_name = mDate.get(i).bank_name;
                bankCode = mDate.get(i).bank_code;
                handler.sendEmptyMessage(1);
            }
        });
        lv_bank_card.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
        netService.getBank(handler_getBank);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("bank_name", bank_name);
            bundle.putSerializable("bankCode", bankCode);
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
            finish();
        }

    };

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
    Handler handler_getBank = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    mDate.clear();
                    mDate.addAll((Collection<? extends BankName>) msg.obj);
                    listViewAdapter.notifyDataSetChanged();
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
                    showTip("获取失败", Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
        }
    };
}
