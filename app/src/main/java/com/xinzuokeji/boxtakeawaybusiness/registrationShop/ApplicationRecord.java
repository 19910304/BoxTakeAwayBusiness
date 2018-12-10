package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.MainActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.ShopRecord;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.ACache;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//申请记录
public class ApplicationRecord extends BaseActivity {
    private ImageButton header_back;

    private ListView lv_application_store;
    private ListViewAdapter<ShopRecord> mAdapter;
    private List<ShopRecord> mDate;
    RadioGroup rdg_app_record;
    RadioButton rdb_app_record_pending, rdb_app_record_under_review, rdb_app_record_completed;
    ImageView img_pending, img_under_review, img_completed;
    private NetService netService;
    private String RecordStype = "2";
    private Button btNewStore;
    public static Activity mActivity;

    @Override
    public void initView() {
        super.initView();
        mActivity = this;
        setContentView(R.layout.activity_application_record);
        netService = new NetService(this);
        // 头信息
        header_back = findViewById(R.id.header_back);
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back.setVisibility(View.VISIBLE);
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);

        TextView textView = findViewById(R.id.header_title);
        textView.setText("申请记录");
        textView.setTextColor(getResources().getColor(R.color.white));
        // 新建店铺
        btNewStore = findViewById(R.id.bt_next_shop);
        rdg_app_record = findViewById(R.id.rdg_app_record);
        rdb_app_record_pending = findViewById(R.id.rdb_app_record_pending);
        rdb_app_record_under_review = findViewById(R.id.rdb_app_record_under_review);
        rdb_app_record_completed = findViewById(R.id.rdb_app_record_completed);
        // 显示/隐藏的显示条
        img_pending = findViewById(R.id.img_pending);
        img_under_review = findViewById(R.id.img_under_review);
        img_completed = findViewById(R.id.img_completed);
        rdg_app_record.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_app_record_pending://待处理
                        img_pending.setVisibility(View.VISIBLE);
                        img_under_review.setVisibility(View.INVISIBLE);
                        img_completed.setVisibility(View.INVISIBLE);
                        RecordStype = "1";
                        netService.formalRecord(getuserinfoId(), handler_formalRecord);

                        break;
                    case R.id.rdb_app_record_under_review://申请中
                        img_pending.setVisibility(View.INVISIBLE);
                        img_under_review.setVisibility(View.VISIBLE);
                        img_completed.setVisibility(View.INVISIBLE);
                        RecordStype = "2";
                        netService.auditRecord(getuserinfoId(), handler_auditRecord);

                        break;
                    case R.id.rdb_app_record_completed://已完成
                        img_pending.setVisibility(View.INVISIBLE);
                        img_under_review.setVisibility(View.INVISIBLE);
                        img_completed.setVisibility(View.VISIBLE);
                        RecordStype = "3";
                        netService.finishRecord(getuserinfoId(), handler_finishRecord);

                        break;
                }
            }
        });
        lv_application_store = findViewById(R.id.lv_application_store);
        mDate = new ArrayList<>();

        mAdapter = new ListViewAdapter<ShopRecord>(this, R.layout.item_application_record, mDate) {
            @Override
            public void convert(ViewHolder holder, ShopRecord shopRecord, int id, int selectedPosition) {
                ImageView shop_logo = holder.getView(R.id.img_shop_logo);
                // 默认图标
                TextView shop_name = holder.getView(R.id.tv_shop_name);
                TextView shop_type = holder.getView(R.id.tv_shop_type);
                TextView upd_time = holder.getView(R.id.tv_upd_time);
                TextView tv_auditRecord = holder.getView(R.id.tv_auditRecord);
                if (RecordStype.equals("1")) {
                    tv_auditRecord.setText("待处理");
                }
                if (RecordStype.equals("2")) {
                    tv_auditRecord.setText("审核中");
                }
                if (RecordStype.equals("3")) {
                    if (shopRecord.examine_status.equals("2")) {
                        tv_auditRecord.setText("已完成");
                    } else {
                        tv_auditRecord.setText("审核失败");
                    }
                }
                if (Valid.isNotNullOrEmpty(shopRecord.logo)) {
                    ImageLoader.getInstance().displayImage(shopRecord.logo, shop_logo, GSApplication.getInstance().imageOptions);
                } else {
                    //  设置前景
                    shop_logo.setImageDrawable(getResources().getDrawable(R.mipmap.no_picture));
//                    shop_logo.setBackgroundResource(R.mipmap.no_picture);
                }
                shop_name.setText(shopRecord.shop_name);
                shop_type.setText(shopRecord.type_name);
                upd_time.setText("最后编辑：" + shopRecord.upd_time);
            }
        };
        lv_application_store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //等于3并且审核状态等于2跳转主页，1继续编辑开店信息
                if (RecordStype.equals("3")) {
                    if (mDate.get(position).examine_status.equals("2")) {
                        BaseActivity.setRecordStype("3");
                        // 存储店铺id
                        SetstoreId(Integer.parseInt(mDate.get(position).id));
                        // 开店中临时使用的店铺id
                        setChangeStoreId(Integer.parseInt(mDate.get(position).id));
                        suerinfoId(getuserinfoId());
                        Intent intent = new Intent(ApplicationRecord.this, MainActivity.class);
                        intent.putExtra("class_name", "StoreSetting");
                        if (intent.resolveActivity(getPackageManager()) != null) {
//                            MainActivity.mActivity.finish();
                            // 说明系统中不存在这个activity
                        }
                        startActivity(intent);
                        finish();
                    } else {

                        setChangeStoreId(Integer.parseInt(mDate.get(position).id));
                        //将店铺的选择状态设置成全局变量
                        BaseActivity.setRecordStype("1");

                        try {
                            gotoActivity(SetUpShopOne.class.getName(), null);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                }
                if (RecordStype.equals("1")) {
                    setChangeStoreId(Integer.parseInt(mDate.get(position).id));
                    //将店铺的选择状态设置成全局变量
                    BaseActivity.setRecordStype("1");
                    try {
                        gotoActivity(SetUpShopOne.class.getName(), null);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        lv_application_store.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        netService.auditRecord(getuserinfoId(), handler_auditRecord);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        btNewStore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.bt_next_shop://新建店铺
                //新建店铺店铺id传0，状态为3，清空店铺名字和类型id缓存
                setChangeStoreId(0);
                BaseActivity.setRecordStype("3");
                ACache aCache = ACache.get(this);
                aCache.getAsString("manage_type_id");
                if (Valid.isNotNullOrEmpty(aCache.getAsString("manage_type_id")) || Valid.isNotNullOrEmpty(aCache.getAsString("typename"))) {
                    aCache.remove("manage_type_id");
                    aCache.remove("typename");
                }
                try {
                    gotoActivity(SetUpShopOne.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    int a = 0;
    @SuppressLint("HandlerLeak")
    Handler handler_auditRecord = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    mDate.clear();
                    if (msg.obj != null) {
                        mDate.addAll((Collection<? extends ShopRecord>) msg.obj);
                        mAdapter.notifyDataSetChanged();
                        // 判断审核中数组是否为0，有数据并且是第一次进来选中审核中
                        if (mDate.size() == 0) {
                            if (a == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rdb_app_record_pending.setChecked(true);
                                    }
                                });

                            }
                            a++;

                        }
                    }
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
//
                    break;
                default:
                    break;
            }
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler_formalRecord = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    mDate.clear();
                    if (msg.obj != null) {
                        mDate.addAll((Collection<? extends ShopRecord>) msg.obj);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
//
                    break;
                default:
                    break;
            }
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler_finishRecord = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    mDate.clear();
                    if (msg.obj != null) {

                        mDate.addAll((Collection<? extends ShopRecord>) msg.obj);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
//
                    break;
                default:
                    break;
            }
        }
    };
}
