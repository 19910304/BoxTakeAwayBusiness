package com.xinzuokeji.boxtakeawaybusiness.storeOperations.commodityManage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.GuiGe;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/7/23.
 */
//规格列表
public class GuigeList extends BaseActivity {
    private ImageButton header_back;
    private TextView add_guige;
    private List<GuiGe> mDate;
    private ListViewAdapter<GuiGe> listViewAdapter;
    ListView lv_guige;
    NetService netService;
    String goodid;
    private String guigeId;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_changeguige_list);
        goodid = getIntent().getStringExtra("good_id");
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("商品规格");
        add_guige = findViewById(R.id.header_edit1);
        add_guige.setVisibility(View.VISIBLE);
        add_guige.setText("新增");
        lv_guige = findViewById(R.id.lv_guige);
        mDate = new ArrayList<>();
        listViewAdapter = new ListViewAdapter<GuiGe>(this, R.layout.item_guige, mDate) {
            @Override
            public void convert(ViewHolder holder, GuiGe guiGe, int id, int selectedPosition) {
                holder.setText(R.id.tv_guige_name, guiGe.norms);
            }
        };
        // 修改规格
        lv_guige.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(GuigeList.this, ChangeGuige.class);
                intent.putExtra("guige", mDate.get(i));
                intent.putExtra("good_id", goodid);
                startActivity(intent);
            }
        });
        lv_guige.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        HandlerThread mHandlerThread = new HandlerThread("handler-thread");
        mHandlerThread.start();
        Handler mHandler = new Handler(mHandlerThread.getLooper()) {
            public void handleMessage(Message msg) {
                netService.showNormsNew(Integer.parseInt(goodid), showNormsNew);
            }
        };
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        add_guige.setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("guige_id", guigeId);
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
            finish();
        }

    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                //   规格个数不等于0拼接规格id
                if (mDate != null && mDate.size() != 0) {
                    for (int i = 0; i < mDate.size(); i++) {
                        if (Valid.isNotNullOrEmpty(guigeId)) {
                            guigeId = guigeId + "," + mDate.get(i).id;
                        } else {
                            guigeId = mDate.get(i).id;
                        }
                    }
                }
//                Log.i("guigeid", guigeId);
                handler.sendEmptyMessage(0);

                break;
            case R.id.header_edit1://新增规格
                HashMap<String, String> extras = new HashMap<>();
                extras.put("good_id", String.valueOf(goodid));
                try {
                    gotoActivity(ChangeGuige.class.getName(), extras);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler showNormsNew = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj.toString().equals("[]")) {
                        mDate.clear();
                        listViewAdapter.notifyDataSetChanged();
                    } else {
                        mDate.clear();
                        mDate.addAll((Collection<? extends GuiGe>) msg.obj);
                        listViewAdapter.notifyDataSetChanged();
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
