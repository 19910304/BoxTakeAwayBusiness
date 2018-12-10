package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;

/**
 * Created by Administrator on 2018/7/31.
 */
//准备材料
public class Preparation extends BaseActivity {
    private ImageButton header_back;
    NetService netService;
    Button bt_sure_preparation;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_preparation);
        netService = new NetService(this);
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("准备材料");
        textView.setTextColor(getResources().getColor(R.color.white));
        bt_sure_preparation = findViewById(R.id.bt_sure_preparation);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_sure_preparation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                try {
                    gotoActivityAndFinish(SetPassWord.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_sure_preparation://跳转开店
                setChangeStoreId(0);
                // 状态3新建店铺，1修改店铺
                BaseActivity.setRecordStype("3");
                try {
                    gotoActivityAndFinish(SetUpShopOne.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
