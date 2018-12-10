package com.xinzuokeji.boxtakeawaybusiness.me.SoreSettings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.DisList;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.LocationUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/8/2.
 */
//配送信息 (字列表已写)
public class ShippingInformation extends BaseActivity implements AMapLocationListener, LocationSource {
    private ImageButton header_back;

    //高德定位
    private MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener = null;
    //定位监听器
    private LocationUtil locationUtil;
    //添加时间段
    private Button bt_add_time;
    //配送方式
    TextView tv_style_distribution;
    private ListView lvDistribution;
    private List<DisList> mDate = new ArrayList<>();
    private ListViewAdapter<DisList> listViewAdapter;
    private NetService netService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_information);
        openGPS(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        netService = new NetService(this);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("配送信息");
        mapView = (MapView) findViewById(R.id.mapview);//找到地图控件
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        //开启高德定位
        init();
        header_back.setOnClickListener(this);
        bt_add_time = findViewById(R.id.bt_add_time);
        bt_add_time.setOnClickListener(this);
        tv_style_distribution = findViewById(R.id.tv_style_distribution);
        //配送列表
        lvDistribution = findViewById(R.id.lv_distribution);
        listViewAdapter = new ListViewAdapter<DisList>(this, R.layout.item_shipping_information, mDate) {
            @Override
            public void convert(ViewHolder holder, final DisList disList, int id, int selectedPosition) {
                if (disList.dis_type.equals("1")) {
                    tv_style_distribution.setText("平台配送");
                } else {
                    tv_style_distribution.setText("商家配送");
                }

                TextView tv_bianji = holder.getView(R.id.tv_bianji);
                //起送价
                TextView tv_minimum_delivery_amount = holder.getView(R.id.tv_minimum_delivery_amount);
                tv_minimum_delivery_amount.setText("起送价" + disList.start_price);
                // 配送费配送费
                TextView tv_delivery_cost = holder.getView(R.id.tv_delivery_cost);
                tv_delivery_cost.setText("配送费" + disList.dis_money);
                if (disList.dis_money.equals("0")) {
                    tv_delivery_cost.setText("免配送费");
                }
                //常规时段或特殊时段
                TextView tv_peisong_name = holder.getView(R.id.tv_peisong_name);
                TextView tv_regular_intervals = holder.getView(R.id.tv_regular_intervals);
                TextView tv_time_quantum = holder.getView(R.id.tv_time_quantum);
                if (disList.time_type.equals("1")) {
                    tv_peisong_name.setText("常规时段");
                    tv_regular_intervals.setVisibility(View.VISIBLE);
                    tv_time_quantum.setVisibility(View.GONE);
                }
                if (disList.time_type.equals("2")) {
                    tv_peisong_name.setText("特殊时段");
                    tv_regular_intervals.setVisibility(View.GONE);
                    tv_time_quantum.setVisibility(View.VISIBLE);
                    tv_time_quantum.setText("(" + disList.open + "-" + disList.close + ")");
                }
                tv_bianji.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ShippingInformation.this, ChangeShippingInformation.class);
                        intent.putExtra("addTime", "0");
                        intent.putExtra("disList", disList);
                        startActivity(intent);

                    }
                });
                TextView tv_indication_range = holder.getView(R.id.tv_indication_range);
                tv_indication_range.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ShippingInformation.this, GeoFenceActivity.class);
                        intent.putExtra("id", disList.id);
                        intent.putExtra("point_set", (Serializable) disList.point_set);
                        startActivity(intent);

                    }
                });
            }
        };
        lvDistribution.setAdapter(listViewAdapter);
        setPullLvHeight(lvDistribution);
        listViewAdapter.notifyDataSetChanged();
        netService.getDisList(GetstoreId(), handerDisList);
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        setLocationCallBack();
        //设置定位监听
        aMap.setLocationSource(this);
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//        显示定位层并可触发，默认false
        aMap.setMyLocationEnabled(true);
    }

    private void setLocationCallBack() {
        locationUtil = new LocationUtil();
        locationUtil.setLocationCallBack(new LocationUtil.ILocationCallBack() {
            @Override
            public void callBack(String str, double lat, double lgt, AMapLocation aMapLocation) {
//         根据获取的经纬度，将地图移动到定位位置
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lgt)));
                mListener.onLocationChanged(aMapLocation);        //添加定位图标
                aMap.addMarker(locationUtil.getMarkerOption(str, lat, lgt));
            }
        });
    }

    @Override
    public void initView() {
        super.initView();

    }

    @Override
    public void initEvent() {
        super.initEvent();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.bt_add_time:
                // 添加时间段
                HashMap<String, String> extras = new HashMap<>();
                extras.put("addTime", "1");
                try {
                    gotoActivity(ChangeShippingInformation.class.getName(), extras);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handerDisList = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    mDate.clear();
                    mDate.addAll((Collection<? extends DisList>) msg.obj);
                    listViewAdapter.notifyDataSetChanged();
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }

    //定位激活回调
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        locationUtil.startLocate(getApplicationContext());
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        重新绘制加载地图
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
