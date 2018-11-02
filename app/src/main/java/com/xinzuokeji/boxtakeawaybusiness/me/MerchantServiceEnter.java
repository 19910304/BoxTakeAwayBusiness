package com.xinzuokeji.boxtakeawaybusiness.me;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.ServiceType;
import com.xinzuokeji.boxtakeawaybusiness.entities.pro;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

//商家服务中心
public class MerchantServiceEnter extends BaseActivity {
    private ImageButton header_back;
    NetService netService;
    List<ServiceType> mDateType = new ArrayList<>();
    ListViewAdapter<ServiceType> serviceTypeListViewAdapter;
    ListView lv_service_type;
//    List<ServiceProblem> dateProblem;
//    ListViewAdapter<ServiceProblem> listViewAdapterProblem;
//    ListView lv_service_problem;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_merchant_service_enter);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("商家服务中心");
        lv_service_type = findViewById(R.id.lv_service_type);
        serviceTypeListViewAdapter = new ListViewAdapter<ServiceType>(this, R.layout.item_service_enter_type, mDateType) {
            @Override
            public void convert(ViewHolder holder, ServiceType serviceType, int id, int selectedPosition) {
                //服务类型
                TextView tv_service_type = holder.getView(R.id.tv_service_type);
                LinearLayout ll_problem_title = holder.getView(R.id.ll_problem_title);
                tv_service_type.setText(serviceType.type);
//                if (serviceType.pro.size() > 0) {
//                    tv_service_type.setText(serviceType.type);
//                } else {
//                    ll_problem_title.setBackgroundColor(getResources().getColor(R.color.white));
//                    tv_service_type.setVisibility(View.GONE);
//                }

                //  具体问题
                ListView lv_service_problem = holder.getView(R.id.lv_service_problem);
                List<pro> dateProblem = new ArrayList<>();
                dateProblem = serviceType.pro;

                ListViewAdapter<pro> listViewAdapterProblem = new ListViewAdapter<pro>(MerchantServiceEnter.this, R.layout.item_service_enter_type_problem, dateProblem) {
                    @Override
                    public void convert(ViewHolder holder, pro serviceProblem, int id, int selectedPosition) {
                        //  问题标题
                        TextView tv_problem = holder.getView(R.id.tv_problem);
                        tv_problem.setText(serviceProblem.problem);
                    }
                };
                final List<pro> finalDateProblem = dateProblem;
                lv_service_problem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // 跳入详细页
                        try {
                            HashMap<String, String> extras = new HashMap<>();
                            extras.put("typeid", finalDateProblem.get(i).id);
                            gotoActivity(MerchantServiceEnterDe.class.getName(), extras);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
                lv_service_problem.setAdapter(listViewAdapterProblem);
                listViewAdapterProblem.notifyDataSetChanged();
                setPullLvHeight(lv_service_problem);
            }

        };
        lv_service_type.setAdapter(serviceTypeListViewAdapter);
        serviceTypeListViewAdapter.notifyDataSetChanged();
        netService.serviceType(serviceType);
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
    Handler serviceType = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        mDateType.clear();
                        mDateType.addAll((Collection<? extends ServiceType>) msg.obj);
                        serviceTypeListViewAdapter.notifyDataSetChanged();
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
