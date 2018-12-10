package com.xinzuokeji.boxtakeawaybusiness.storeOperations.jingyingShuju;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.Tree;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.netutil.JSONDeserialize;
import com.xinzuokeji.boxtakeawaybusiness.storeOperations.jingyingShuju.businessStatistics.BusinessStatistics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//经营数据
public class JingyingShuju extends BaseActivity {
    private ImageButton header_back;
    ImageView img_yesterday, img_seven_day;
    LinearLayout ll_merchants_check_up, ll_bus_statistics, ll_customer_analyze, ll_traffic_statistics, ll_commodity_analysis;
    NetService netService;
    private TextView tv_zuotian_fangke, tv_shangsheng_people, tv_xiadan_geke, tv_xiajiang, tv_zhuanhualv,
            tv_yesNewGuestnum, tv_contrastNewGuest, tv_yesOldGuestnum, tv_contrastOldGuest;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_jingying_shuju);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("经营数据");
        ll_merchants_check_up = findViewById(R.id.ll_merchants_check_up);//商家体检
        ll_bus_statistics = findViewById(R.id.ll_bus_statistics);
        ll_customer_analyze = findViewById(R.id.ll_customer_analyze);//顾客分析
        ll_traffic_statistics = findViewById(R.id.ll_traffic_statistics);//流量统计
        ll_commodity_analysis = findViewById(R.id.ll_commodity_analysis);//商品分析
        //昨日流量概况
        tv_zuotian_fangke = findViewById(R.id.tv_zuotian_fangke);
        tv_shangsheng_people = findViewById(R.id.tv_shangsheng_people);
        tv_xiadan_geke = findViewById(R.id.tv_xiadan_geke);
        tv_xiajiang = findViewById(R.id.tv_xiajiang);
        tv_zhuanhualv = findViewById(R.id.tv_zhuanhualv);
        // 昨日下单顾客概览
        tv_yesNewGuestnum = findViewById(R.id.tv_yesNewGuestnum);
        tv_contrastNewGuest = findViewById(R.id.tv_contrastNewGuest);
        tv_yesOldGuestnum = findViewById(R.id.tv_yesOldGuestnum);
        tv_contrastOldGuest = findViewById(R.id.tv_contrastOldGuest);
        // 选择昨日统计和七日统计
        RadioGroup rdg_jingyin = findViewById(R.id.rdg_jingyin);
        RadioButton rdb_jingyin_yesterday = findViewById(R.id.rdb_jingyin_yesterday);
        RadioButton rdb_jingyin_seven_day = findViewById(R.id.rdb_jingyin_seven_day);
        img_yesterday = findViewById(R.id.img_yesterday);
        img_seven_day = findViewById(R.id.img_seven_day);
        rdg_jingyin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_jingyin_yesterday:
                        img_yesterday.setVisibility(View.VISIBLE);
                        img_seven_day.setVisibility(View.INVISIBLE);
                        netService.yesterdayOperadata(GetstoreId(), hander_yesterdayOperadata);
                        break;
                    case R.id.rdb_jingyin_seven_day:
                        img_yesterday.setVisibility(View.INVISIBLE);
                        img_seven_day.setVisibility(View.VISIBLE);
                        netService.sevendayOperadata(GetstoreId(), hander_sevendayOperadata);
                        break;
                }
            }
        });
        netService.yesterdayOperadata(GetstoreId(), hander_yesterdayOperadata);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_merchants_check_up.setOnClickListener(this);
        ll_bus_statistics.setOnClickListener(this);
        ll_customer_analyze.setOnClickListener(this);
        ll_traffic_statistics.setOnClickListener(this);
        ll_commodity_analysis.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_merchants_check_up://商家体检
                try {
                    gotoActivity(MerchantsCheckUp.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_bus_statistics://营业统计
                try {
                    gotoActivity(BusinessStatistics.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_customer_analyze://顾客分析
                try {
                    gotoActivity(Customeranalyze.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_traffic_statistics://流量统计
                try {
                    gotoActivity(MobileCounter.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_commodity_analysis://商品分析
                try {
                    gotoActivity(CommercialAnalysis.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }

    @SuppressLint("HandlerLeak")
    Handler hander_yesterdayOperadata = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        int contrastVisit = jsonObject.getInt("contrastVisit");
                        jsonObject.getString("yesterdayOrder");
                        int contrastOrder = jsonObject.getInt("contrastOrder");
                        jsonObject.getString("conversionRate");
                        jsonObject.getString("yesNewGuestnum");
                        int contrastNewGuest = jsonObject.getInt("contrastNewGuest");
                        jsonObject.getString("yesOldGuestnum");
                        int contrastOldGuest = jsonObject.getInt("contrastOldGuest");
                        tv_zuotian_fangke.setText(jsonObject.getString("yesvisit"));
                        if (contrastVisit >= 0) {
                            tv_shangsheng_people.setText("上升" + contrastVisit + "人");
                        } else {
                            String contraVisit = String.valueOf(contrastVisit).substring(1);
                            tv_shangsheng_people.setText("下降" + contraVisit + "人");
                        }
                        tv_xiadan_geke.setText(jsonObject.getString("yesterdayOrder"));
                        if (contrastOrder >= 0) {
                            tv_xiajiang.setText("上升" + contrastOrder + "人");
                        } else {
                            String contraOrder = String.valueOf(contrastOrder).substring(1);
                            tv_xiajiang.setText("下降" + contraOrder + "人");
                        }
                        tv_zhuanhualv.setText(jsonObject.getString("conversionRate") + "%");
                        // 昨日下单顾客概览   tv_yesNewGuestnum, tv_contrastNewGuest, tv_yesOldGuestnum, tv_contrastOldGuest;
                        tv_yesNewGuestnum.setText(jsonObject.getString("yesNewGuestnum"));
                        if (contrastNewGuest >= 0) {
                            tv_contrastNewGuest.setText("上升" + contrastNewGuest + "人");
                        } else {
                            String conNewGuest = String.valueOf(contrastNewGuest).substring(1);
                            tv_contrastNewGuest.setText("下降" + conNewGuest + "人");
                        }
                        tv_yesOldGuestnum.setText(jsonObject.getString("yesOldGuestnum"));
                        if (contrastOldGuest >= 0) {
                            tv_contrastOldGuest.setText("上升" + contrastNewGuest + "人");
                        } else {
                            String conOldGuest = String.valueOf(contrastOldGuest).substring(1);
                            tv_contrastOldGuest.setText("下降" + conOldGuest + "人");
                        }
                        List<Tree> mDate = new ArrayList<>();
                        if (msg.obj.toString().equals("[]")) {
//                            mAdapter.notifyDataSetChanged();
                        } else {
                            mDate.clear();
                            mDate.addAll(new JSONDeserialize<>(Tree.class, jsonObject.getString("tree")).toObjects());
//                            mAdapter.notifyDataSetChanged();
                        }
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
    @SuppressLint("HandlerLeak")
    Handler hander_sevendayOperadata = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:


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
