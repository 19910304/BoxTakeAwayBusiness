package com.xinzuokeji.boxtakeawaybusiness.storeOperations.jingyingShuju;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.YesCS;
import com.xinzuokeji.boxtakeawaybusiness.entities.YesDT;
import com.xinzuokeji.boxtakeawaybusiness.entities.YesOverview;
import com.xinzuokeji.boxtakeawaybusiness.entities.YesRO;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.netutil.JSONDeserialize;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/6/5.
 */
//顾客分析
public class Customeranalyze extends BaseActivity {
    private ImageButton header_back;
    NetService netService;
    private TextView tv_yesOrderPerson, tv_yesCompare, tv_yesNewGuestnum,
            tv_contrastNewGuest, tv_NewPersonRatio, tv_yesOldGuest,
            tv_contrastOldGuest, tv_OldPersonRatio, tv_yesRepetitionRate,
            tv_yesRRCompare, tv_yesRepeatPerson, tv_yesRepeatCompare;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_customer_analyze);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("顾客分析");
        tv_yesOrderPerson = findViewById(R.id.tv_yesOrderPerson);
        tv_yesCompare = findViewById(R.id.tv_yesCompare);
        tv_yesNewGuestnum = findViewById(R.id.tv_yesNewGuestnum);
        tv_contrastNewGuest = findViewById(R.id.tv_contrastNewGuest);
        tv_NewPersonRatio = findViewById(R.id.tv_NewPersonRatio);
        tv_yesOldGuest = findViewById(R.id.tv_yesOldGuest);
        tv_contrastOldGuest = findViewById(R.id.tv_contrastOldGuest);
        tv_OldPersonRatio = findViewById(R.id.tv_OldPersonRatio);
        //昨日重复订单数据
        tv_yesRepetitionRate = findViewById(R.id.tv_yesRepetitionRate);
        tv_yesRRCompare = findViewById(R.id.tv_yesRRCompare);
        tv_yesRepeatPerson = findViewById(R.id.tv_yesRepeatPerson);
        tv_yesRepeatCompare = findViewById(R.id.tv_yesRepeatCompare);
        netService.ShowYesterdayAnalysis(GetstoreId(), hander_ShowYesterdayAnalysis);
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

    @SuppressLint("HandlerLeak")
    Handler hander_ShowYesterdayAnalysis = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        //  昨日总览模块数据
                        jsonObject.getString("YesOverview");
                        YesOverview yesOverview = new JSONDeserialize<>(YesOverview.class, jsonObject.getString("YesOverview")).toObject();
                        tv_contrastNewGuest.setText(yesOverview.contrastNewGuest);
                        tv_contrastOldGuest.setText(yesOverview.contrastOldGuest);
                        tv_yesOrderPerson.setText(yesOverview.yesOrderPerson);
                        tv_NewPersonRatio.setText(yesOverview.NewPersonRatio);
                        tv_OldPersonRatio.setText(yesOverview.OldPersonRatio);
                        tv_yesCompare.setText(yesOverview.yesCompare);
                        tv_yesNewGuestnum.setText(yesOverview.yesNewGuestnum);
                        //重复下单
                        jsonObject.getString("YesRO");
                        YesRO yesRO = new JSONDeserialize<>(YesRO.class, jsonObject.getString("YesRO")).toObject();
                        tv_yesRepeatCompare.setText(yesRO.yesRepeatCompare);
                        tv_yesRRCompare.setText(yesRO.yesRRCompare);
                        tv_yesRepeatPerson.setText(yesRO.yesRepeatPerson);
                        tv_yesRepetitionRate.setText(yesRO.yesRepetitionRate);
                        //YesCS
                        YesCS yesCS = new JSONDeserialize<>(YesCS.class, jsonObject.getString("YesCS")).toObject();
                        YesDT yesDT = new JSONDeserialize<>(YesDT.class, jsonObject.getString("YesDT")).toObject();
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
}
