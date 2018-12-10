package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.CardInfoStype;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.Constants;

//资质信息
public class SetUpShopThree extends BaseActivity {
    private ImageButton header_back;
    LinearLayout ll_business_license, ll_corporate_id, ll_licence;
    Button bt_next_shop;
    public static Activity mActivity;
    NetService netService;
    private TextView CardLicense, IdCard, CardLicence;


    @Override
    public void initView() {
        super.initView();
        mActivity = this;
        netService = new NetService(this);
        setContentView(R.layout.activity_set_up_shop_three);
        header_back = findViewById(R.id.header_back);
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back.setVisibility(View.VISIBLE);
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("资质信息");
        textView.setTextColor(getResources().getColor(R.color.white));
        ll_business_license = findViewById(R.id.ll_business_license);
        ll_corporate_id = findViewById(R.id.ll_corporate_id);
        ll_licence = findViewById(R.id.ll_licence);
        bt_next_shop = findViewById(R.id.bt_next_shop);
        // 显示证件是否上传
        CardLicense = findViewById(R.id.tv_card_license);
        IdCard = findViewById(R.id.tv_id_card);
        CardLicence = findViewById(R.id.tv_card_licence);
        netService.showCardInfo(getChangeStoreId(), Hander_showCardInfo);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_business_license.setOnClickListener(this);
        ll_corporate_id.setOnClickListener(this);
        ll_licence.setOnClickListener(this);
        bt_next_shop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_business_license://营业执照
                Intent intentLicense = new Intent(this, SUSThreeBusinessLicense.class);
                startActivityForResult(intentLicense, Constants.BUSINESSLICENSE);

                break;
            case R.id.ll_corporate_id://身份证
                Intent intentIdentityCard = new Intent(this, SUSThreeIdentityCard.class);
                intentIdentityCard.putExtra("from_name", "注册");
                startActivityForResult(intentIdentityCard, Constants.IDENTITYCARD);
                break;
            case R.id.ll_licence://许可证
                Intent intentLicence = new Intent(this, SUSThreeLicence.class);
                startActivityForResult(intentLicence, Constants.LICENCE);
                break;
            case R.id.bt_next_shop://验证所有证件填写完整
                if (CardLicence.getText().toString().equals("完成") && IdCard.getText().toString().equals("完成") && CardLicense.getText().toString().equals("完成")) {
                    try {
                        gotoActivity(SetUpShopFour.class.getName(), null);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
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
        //这里需要填写结果码，没有结果码时默认0以下不会执行
        switch (resultCode) {
            case Constants.BUSINESSLICENSE://营业执照
                String intentLicense = data.getStringExtra("intentLicense");
                Log.i("inten", intentLicense);
                CardLicense.setText(intentLicense);
                break;
            case Constants.IDENTITYCARD://身份信息
                String identitycard = data.getStringExtra("identitycard");
                IdCard.setText(identitycard);
                break;
            case Constants.LICENCE://许可证
                String licence = data.getStringExtra("licence");
                CardLicence.setText(licence);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("HandlerLeak")
    Handler Hander_showCardInfo = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    CardInfoStype cardInfoStype = (CardInfoStype) msg.obj;
                    if (cardInfoStype != null) {
                        if (cardInfoStype.card_licence.equals("0")) {
                            CardLicence.setText("未完成");
                        } else {
                            CardLicence.setText("完成");
                        }
                        if (cardInfoStype.id_card.equals("0")) {
                            IdCard.setText("未完成");
                        } else {
                            IdCard.setText("完成");
                        }
                        if (cardInfoStype.card_license.equals("0")) {
                            CardLicense.setText("未完成");
                        } else {
                            CardLicense.setText("完成");
                        }
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
    };
}
