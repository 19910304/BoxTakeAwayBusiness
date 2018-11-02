package com.xinzuokeji.boxtakeawaybusiness.me.SoreSettings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.point_set;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/21.
 */
//电子围栏
public class GeoFenceActivity extends BaseActivity implements View.OnClickListener,
        GeoFenceListener,
        AMap.OnMapClickListener,
        LocationSource,
        AMapLocationListener {
    final String tag = GeoFenceActivity.class.getSimpleName();
    final int REQ_LOCATION = 0x12;
    final int REQ_GEO_FENCE = 0x13;
    final String ACTION_GEO_FENCE = "geo fence action";
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private IntentFilter intentFilter;
    private Vibrator vibrator;
    private LatLng centerLatLng;
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener onLocationChangedListener;
    private LinearLayout ll_header_back;
    private TextView tv_save;
    private String id;
    private List<point_set> pointSets;
    private NetService netService;
    // 地理围栏客户端
    private GeoFenceClient fenceClient = null;
    private BitmapDescriptor bitmap = null;
    private MarkerOptions markerOption = null;
    // 地理围栏的广播action
    private static final String GEOFENCE_BROADCAST_ACTION = "com.example.geofence.polygon";
    // 多边形围栏的边界点
    private List<LatLng> polygonPoints = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence);
        netService = new NetService(this);
        id = getIntent().getStringExtra("id");
        pointSets = (List<point_set>) getIntent().getSerializableExtra("point_set");
//        标题信息
        ll_header_back = findViewById(R.id.ll_header_back);
        ll_header_back.setOnClickListener(this);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("配送范围");
        tv_save = findViewById(R.id.tv_save);
        tv_save.setVisibility(View.GONE);
        tv_save.setOnClickListener(this);

        mapView = findViewById(R.id.mp_geofence);
        mapView.onCreate(savedInstanceState);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mLocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(2000);
        mLocationClient.setLocationOption(mLocationOption);
        applyPermission();
        // 初始化地理围栏
        fenceClient = new GeoFenceClient(getApplicationContext());
        bitmap = BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
        markerOption = new MarkerOptions().icon(bitmap).draggable(true);
        //处理进出地理围栏事件
//        intentFilter = new IntentFilter();
//        intentFilter.addAction(ACTION_GEO_FENCE);
//        //show my location
//        aMap.setLocationSource(this);
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);
//        aMap.setMyLocationEnabled(true);
//        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
        init();
        addPolygonFence();
    }

    void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.getUiSettings().setRotateGesturesEnabled(false);
            aMap.moveCamera(CameraUpdateFactory.zoomBy(6));
            setUpMap();
        }


        IntentFilter filter = new IntentFilter();
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
//        registerReceiver(mGeoFenceReceiver, filter);
        /**
         * 创建pendingIntent
         */
        fenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        fenceClient.setGeoFenceListener(this);
        /**
         * 设置地理围栏的触发行为,默认为进入
         */
        fenceClient.setActivateAction(GeoFenceClient.GEOFENCE_IN);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setOnMapClickListener(this);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
//        myLocationStyle.myLocationIcon(
//                BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
//        this.registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 接收触发围栏后的广播,当添加围栏成功之后，会立即对所有围栏状态进行一次侦测，如果当前状态与用户设置的触发行为相符将会立即触发一次围栏广播；
     * 只有当触发围栏之后才会收到广播,对于同一触发行为只会发送一次广播不会重复发送，除非位置和围栏的关系再次发生了改变。
     */
    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收广播
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                Bundle bundle = intent.getExtras();
                String customId = bundle
                        .getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                String fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
                //status标识的是当前的围栏状态，不是围栏行为
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                StringBuffer sb = new StringBuffer();
                switch (status) {
                    case GeoFence.STATUS_LOCFAIL:
                        sb.append("定位失败");
                        break;
                    case GeoFence.STATUS_IN:
                        sb.append("进入围栏 ");
                        break;
                    case GeoFence.STATUS_OUT:
                        sb.append("离开围栏 ");
                        break;
                    case GeoFence.STATUS_STAYED:
                        sb.append("停留在围栏内 ");
                        break;
                    default:
                        break;
                }
                if (status != GeoFence.STATUS_LOCFAIL) {
                    if (!TextUtils.isEmpty(customId)) {
                        sb.append(" customId: " + customId);
                    }
                    sb.append(" fenceId: " + fenceId);
                }
                String str = sb.toString();
                Message msg = Message.obtain();
                msg.obj = str;
                msg.what = 2;
//                handler.sendMessage(msg);
            }
        }
    };

    // 监听返回按键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            //绑定返回按键和返回按钮
            LinearLayout header_back = findViewById(R.id.ll_header_back);
            if (header_back != null) {
                header_back.performClick();
            }

            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    public void onClick(View i) {
        switch (i.getId()) {
            case R.id.ll_header_back:
                finish();
                break;
            case R.id.tv_save://保存信息
                JSONArray pointsArray = new JSONArray();
                for (int j = 0; j < pointSets.size(); j++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("lng", pointSets.get(j).lng);
                        jsonObject.put("lat", pointSets.get(j).lat);
                        pointsArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                break;
            default:
                break;
        }
    }

    /**
     * 添加多边形围栏
     *
     * @author hongming.wang
     * @since 3.2.0
     */
    private void addPolygonFence() {
//        String customId = etCustomId.getText().toString();
        if (null == pointSets || pointSets.size() < 3) {
            Toast.makeText(getApplicationContext(), "参数不全", Toast.LENGTH_SHORT)
                    .show();
//            btAddFence.setEnabled(true);
            return;
        }
        List<DPoint> pointList = new ArrayList<DPoint>();
        for (point_set latLng : pointSets) {
            pointList.add(new DPoint(Double.valueOf(latLng.lat), Double.valueOf(latLng.lng)));
        }
        fenceClient.addGeoFence(pointList, "");
    }

    @SuppressLint("HandlerLeak")
    Handler Hander_updDisList = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    finish();
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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

    BroadcastReceiver
            broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收广播
            if (intent.getAction().equals(ACTION_GEO_FENCE)) {
                Bundle bundle = intent.getExtras();
                // 根据广播的event来确定是在区域内还是在区域外
                int status = bundle.getInt("event");
                String geoFenceId = bundle.getString("fenceId");
                if (status == 1) {
                    Toast.makeText(GeoFenceActivity.this, "进入地理围栏~", Toast.LENGTH_LONG).show();
                    vibrator.vibrate(3000);
                } else if (status == 2) {
                    // 离开围栏区域
                    Toast.makeText(GeoFenceActivity.this, "离开地理围栏~", Toast.LENGTH_LONG).show();
                    vibrator.vibrate(3000);
                }
            }
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
//        this.unregisterReceiver(broadcastReceiver);
    }

    public void applyPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationClient.startLocation();
            } else {
                Toast.makeText(GeoFenceActivity.this, "没有权限，无法获取位置信息~", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (loc != null && loc.getErrorCode() == 0) {
            //设置地理围栏
            if (centerLatLng == null) {
                centerLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                Intent intent = new Intent(ACTION_GEO_FENCE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQ_GEO_FENCE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                //100:是围栏半径（测试发现，设置的太小，不会发出广播）；-1：是超时时间（单位：ms，-1代表永不超时）
//                mLocationClient.addGeoFenceAlert("fenceId", centerLatLng.latitude, centerLatLng.longitude, 100, -1, pendingIntent);

                addCircle(centerLatLng, 100);
            } else {
                double latitude = loc.getLatitude();
                double longitude = loc.getLongitude();
                Log.d(tag, "当前经纬度: " + latitude + "," + longitude);
                LatLng endLatlng = new LatLng(loc.getLatitude(), loc.getLongitude());
// 计算量坐标点距离
                double distances = AMapUtils.calculateLineDistance(centerLatLng, endLatlng);
                Toast.makeText(GeoFenceActivity.this, "当前距离中心点：" + ((int) distances), Toast.LENGTH_LONG).show();
                if (onLocationChangedListener != null) {
                    onLocationChangedListener.onLocationChanged(loc);
                }
            }
        }
    }

    public void addCircle(LatLng latLng, int radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeWidth(4);
        circleOptions.strokeColor(Color.RED);
        circleOptions.fillColor(Color.BLUE);
        aMap.addCircle(circleOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }

    }


    @Override
    public void onGeoFenceCreateFinished(List<GeoFence> list, int i, String s) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}
