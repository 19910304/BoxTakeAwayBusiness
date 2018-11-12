package com.xinzuo.qiaobashangjia.me;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xinzuo.qiaobashangjia.BaseActivity;
import com.xinzuo.qiaobashangjia.R;
import com.xinzuo.qiaobashangjia.entities.ServiceProblem;
import com.xinzuo.qiaobashangjia.netService.NetService;

/**
 * Created by Administrator on 2018/6/12.
 */
//问题详细页
public class MerchantServiceEnterDe extends BaseActivity {
    private ImageButton header_back;

    NetService netService;
    TextView tv_problem_title, tv_problem_title_con;
    TextView textView;

    @Override

    public void initView() {
        super.initView();
        setContentView(R.layout.activity_merchant_service_enter_de);
        String id = getIntent().getStringExtra("typeid");
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        textView = findViewById(R.id.header_title);
        tv_problem_title = findViewById(R.id.tv_problem_title);
        tv_problem_title_con = findViewById(R.id.tv_problem_title_con);
        netService.serviceProblem(Integer.parseInt(id), serviceProblem);
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
    Handler serviceProblem = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        ServiceProblem serviceProblem = (ServiceProblem) msg.obj;
                        textView.setText(serviceProblem.problem);
                        tv_problem_title.setText(serviceProblem.problem);
                        tv_problem_title_con.setText(serviceProblem.content);
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
