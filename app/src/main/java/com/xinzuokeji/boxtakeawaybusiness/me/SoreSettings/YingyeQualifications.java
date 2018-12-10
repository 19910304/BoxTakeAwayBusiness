package com.xinzuokeji.boxtakeawaybusiness.me.SoreSettings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.CardInfoStype;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.registrationShop.SUSThreeIdentityCard;
import com.xinzuokeji.boxtakeawaybusiness.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//营业资质
public class YingyeQualifications extends BaseActivity {
    private ImageButton header_back;
    LinearLayout ll_yingye_zhihzao, ll_charter_documents, ll_identity_card, ll_person_card;
    NetService netService;
    List<Map<String, Object>> list = new ArrayList<>();
    private LinearLayout ll_canyin_class;
    private TextView CardLicense, IdCard, CardLicence;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_yingye_qualifications);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("营业资质");
        ll_yingye_zhihzao = findViewById(R.id.ll_yingye_zhihzao);//营业执照
        ll_charter_documents = findViewById(R.id.ll_charter_documents);//特许证件
        ll_identity_card = findViewById(R.id.ll_identity_card);//餐饮许可证
        ll_person_card = findViewById(R.id.ll_person_card);//身份信息
        ll_canyin_class = findViewById(R.id.ll_canyin_class);
        netService = new NetService(this);
        // 显示证件是否上传
        CardLicense = findViewById(R.id.tv_card_license);
        IdCard = findViewById(R.id.tv_id_card);
        CardLicence = findViewById(R.id.tv_card_licence);
        netService.showCertificates(GetstoreId(), showCertificates);
        netService.showCardInfo(GetstoreId(), Hander_showCardInfo);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_yingye_zhihzao.setOnClickListener(this);
        ll_charter_documents.setOnClickListener(this);
        ll_identity_card.setOnClickListener(this);
        ll_person_card.setOnClickListener(this);
        ll_canyin_class.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_yingye_zhihzao://营业执照
                Intent intentLicense = new Intent(this, ShopYingyeZhihzao.class);
                intentLicense.putExtra("card_type", "1");
                startActivityForResult(intentLicense, Constants.BUSINESSLICENSE);
//                for (int i = 0; i < list.size(); i++) {
//                    if (list.get(i).get("card_type").toString().equals("1")) {
//                        Intent intentLicense = new Intent(this, ShopYingyeZhihzao.class);
//                        intentLicense.putExtra("card_type", list.get(i).get("card_type").toString());
//                        intentLicense.putExtra("certificates_name", list.get(i).get("certificates_name").toString());
//                        intentLicense.putExtra("legal_person", list.get(i).get("legal_person").toString());
//                        intentLicense.putExtra("register_number", list.get(i).get("register_number").toString());
//                        intentLicense.putExtra("certificates_place", list.get(i).get("certificates_place").toString());
//                        intentLicense.putExtra("validity_type", list.get(i).get("validity_type").toString());
//                        intentLicense.putExtra("validity_term", list.get(i).get("validity_term").toString());
//                        intentLicense.putExtra("certificates_img", list.get(i).get("certificates_img").toString());
//                        startActivityForResult(intentLicense, Constants.BUSINESSLICENSE);
//                    }
//                }

                break;
            case R.id.ll_charter_documents://特许证件
                try {
                    gotoActivity(ShopCharterDocuments.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_person_card://法人身份证
                Intent intentIdentityCard = new Intent(this, SUSThreeIdentityCard.class);
                intentIdentityCard.putExtra("from_name", "");
                startActivityForResult(intentIdentityCard, Constants.IDENTITYCARD);
                break;
            case R.id.ll_identity_card://餐饮许可证
                Intent intentLicence = new Intent(this, ShopYingyeZhihzao.class);
                intentLicence.putExtra("card_type", "3");
                startActivityForResult(intentLicence, Constants.LICENCE);
                break;
            case R.id.ll_canyin_class://餐饮等级
                try {
                    gotoActivity(CanYinClass.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
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
                CardLicense.setText(intentLicense);
            case Constants.IDENTITYCARD://身份信息
                String identitycard = data.getStringExtra("identitycard");
                IdCard.setText(identitycard);
            case Constants.LICENCE://许可证
                String licence = data.getStringExtra("licence");
                CardLicence.setText(licence);
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("HandlerLeak")
    Handler showCertificates = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        JSONArray date = jsonObject.getJSONArray("data");
                        JSONObject status = new JSONObject(jsonObject.getString("status"));
                        String code = status.getString("code");
                        String mesg = status.getString("message");
                        if (code.equals("200")) {
                            for (int i = 0; i < date.length(); i++) {
                                JSONObject jsonObjectsss = date.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                //获取到json数据中的activity数组里的内容name
                                int id = jsonObjectsss.getInt("id");//证书id
                                int shop_id = jsonObjectsss.getInt("shop_id");//店铺id
                                int card_type = jsonObjectsss.getInt("card_type");//证书类型 1: 营业执照 2：法人身份证 3：许可证
                                String certificates_name = jsonObjectsss.getString("certificates_name");//证书名称
                                String legal_person = jsonObjectsss.getString("legal_person");//法人
                                String register_number = jsonObjectsss.getString("register_number");//注册号
                                String certificates_place = jsonObjectsss.getString("certificates_place");//证书地址
                                int validity_type = jsonObjectsss.getInt("validity_type");//有效期 0:短期 1:长期
                                String validity_term = jsonObjectsss.getString("validity_term");//有效期时长
                                String certificates_img = jsonObjectsss.getString("certificates_img");//证件照片
                                //存入map
                                map.put("id", id);
                                map.put("shop_id", shop_id);
                                map.put("card_type", card_type);
                                map.put("certificates_name", certificates_name);
                                map.put("legal_person", legal_person);
                                map.put("register_number", register_number);
                                map.put("certificates_place", certificates_place);
                                map.put("validity_type", validity_type);
                                map.put("validity_term", validity_term);
                                map.put("certificates_img", certificates_img);
                                //ArrayList集合
                                list.add(map);
                            }
                        } else {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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
