package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.util.LocationUtil;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.xinzuokeji.boxtakeawaybusiness.GSApplication.REQUESTCODE;

/**
 * Created by Administrator on 2018/9/8.
 */
//商家开店位置
public class GspLocation extends BaseActivity implements AMapLocationListener, LocationSource, AMap.OnMapClickListener, PoiSearch.OnPoiSearchListener {
    private LinearLayout ll_header_back;
    private TextView tv_save;
    //高德定位
    private MapView mapView;
    private AMap aMap;
    //定位监听器
    private LocationUtil locationUtil;
    private LocationSource.OnLocationChangedListener mListener = null;
    private String lats, lgts;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    //省
    String shengsheng = "", city;
    //市
    String shishi;
    //区
    String ququ, CityCode, AdCode;
    //街道
    String jiedao;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;

    //搜索框
    private EditText et_search_map;
    PoiSearch.Query query;
    private ListView lv_map_information;
    private ListViewAdapter<PoiItem> listViewAdapter;
    ArrayList<PoiItem> poiItems;
    MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_location);
        poiItems = new ArrayList<>();
        openGPS(this);
        ll_header_back = findViewById(R.id.ll_header_back);
        ll_header_back.setOnClickListener(this);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("标记位置");
        tv_save = findViewById(R.id.tv_save);
        tv_save.setOnClickListener(this);
        // 搜索按钮
        et_search_map.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //  输入查询地图位置信息
                if (Valid.isNotNullOrEmpty(et_search_map.getText().toString())) {
                    lv_map_information.setVisibility(View.VISIBLE);
                    query = new PoiSearch.Query(et_search_map.getText().toString(), "", "");
                    query.setPageSize(10);// 设置每页最多返回多少条poiitem
                    query.setPageNum(1);//设置查询页码
                    PoiSearch poiSearch = new PoiSearch(GspLocation.this, query);
                    poiSearch.setOnPoiSearchListener(GspLocation.this);
                    poiSearch.searchPOIAsyn();
                } else {
                    lv_map_information.setVisibility(View.GONE);
                }
            }
        });
        mapView = (MapView) findViewById(R.id.mapview);//找到控件
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        init();
        location();
        lv_map_information.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击列表子项获取位置坐标以及省市区
                lats = String.valueOf(poiItems.get(position).getLatLonPoint().getLatitude());
                lgts = String.valueOf(poiItems.get(position).getLatLonPoint().getLongitude());
                shengsheng = poiItems.get(position).getProvinceName();
                shishi = poiItems.get(position).getCityName();
                ququ = poiItems.get(position).getAdName();
                CityCode = poiItems.get(position).getCityCode();
                AdCode = poiItems.get(position).getAdCode();
                jiedao = poiItems.get(position).getBusinessArea();
//                Log.i("sssssssss", shengsheng + shishi + ququ + CityCode + AdCode + jiedao + lgts);
                if (Valid.isNotNullOrEmpty(shengsheng) && Valid.isNotNullOrEmpty(shishi) && Valid.isNotNullOrEmpty(ququ) && Valid.isNotNullOrEmpty(AdCode)
                        && Valid.isNotNullOrEmpty(CityCode) && !lats.equals("0.0") && !lgts.equals("0.0")) {
                    LatLng latLng = new LatLng(Double.parseDouble(lats), Double.parseDouble(lgts));
                    markerOptions.position(latLng);
//                    markerOptions.title("我的位置");
//                    markerOptions.snippet("i am here");
                    markerOptions.visible(true);
                    Marker marker = aMap.addMarker(markerOptions);
                    marker.showInfoWindow();
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                    et_search_map.setText(shengsheng + shishi + ququ + jiedao);
                    lv_map_information.setVisibility(View.GONE);
                } else {
                    Toast.makeText(GspLocation.this, "请输入精确地址", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        poiItems = poiResult.getPois();
        if (rCode == 1000) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 搜索列表
                    listViewAdapter = new ListViewAdapter<PoiItem>(GspLocation.this, R.layout.item_map_list, poiItems) {
                        @Override
                        public void convert(ViewHolder holder, PoiItem poiItem, int id, int selectedPosition) {
                            holder.setText(R.id.map_address_name, poiItem.toString());
                        }
                    };
                    lv_map_information.setAdapter(listViewAdapter);
                    listViewAdapter.notifyDataSetChanged();
                }
            });

        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 某个点是否在区域内
     *
     * @param aMap       地图元素
     * @param latLngList 区域坐标合集
     * @param latLng     需要判断的点
     * @return
     */
    public static boolean polygonCon(AMap aMap, List<LatLng> latLngList, LatLng latLng) {
        PolygonOptions options = new PolygonOptions();
        for (LatLng i : latLngList) {
            options.add(i);
        }
        options.visible(false); //设置区域是否显示
        Polygon polygon = aMap.addPolygon(options);
        boolean contains = polygon.contains(latLng);
        return contains;
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
////        显示定位层并可触发，默认false
//        aMap.setMyLocationEnabled(true);
        // setLocationCallBack();
        //设置定位监听
        aMap.setLocationSource(this);
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//        显示定位层并可触发，默认false
//        aMap.setMyLocationEnabled(true);
        setUpMap();
    }


    private void setUpMap() {
        aMap.setOnMapClickListener(this);
        markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_through));

//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap());
        lats = String.valueOf(GSApplication.getInstance().getLatitude());
        lgts = String.valueOf(GSApplication.getInstance().getLongitude());
        if (lats.equals("0.0") || lgts.equals("0.0")) {
            lgts = "106.484064";
            lats = "29.530191";
        }
        markerOptions.position(new LatLng(Double.parseDouble(lats), Double.parseDouble(lgts)));
        aMap.addMarker(markerOptions);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Double.parseDouble(lats), Double.parseDouble(lgts))));
    }

    private void setLocationCallBack() {
        locationUtil = new LocationUtil();
        locationUtil.setLocationCallBack(new LocationUtil.ILocationCallBack() {
            @Override
            public void callBack(String str, double lat, double lgt, AMapLocation aMapLocation) {
                //根据获取的经纬度，将地图移动到定位位置
                if (Valid.isNotNullOrEmpty(lats) || Valid.isNotNullOrEmpty(lgts)) {
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Double.parseDouble(lats), Double.parseDouble(lgts))));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_through));
                    LatLng latLng = new LatLng(Double.parseDouble(lats), Double.parseDouble(lgts));
                    markerOptions.position(latLng);
                    aMap.addMarker(markerOptions);
                    lats = String.valueOf(lat);
                    lgts = String.valueOf(lgt);
                } else {
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lgt)));
                    mListener.onLocationChanged(aMapLocation);        //添加定位图标
                    aMap.addMarker(locationUtil.getMarkerOption(str, lat, lgt));
                    lats = String.valueOf(lat);
                    lgts = String.valueOf(lgt);
                }

//                Log.i("lat", lats + "--" + lgts + "-" + str + "--" + aMapLocation.getLatitude() + "--" + aMapLocation.getLongitude());

            }
        });
    }

    @Override
    public void onMapClick(LatLng latLng) {
        aMap.clear();
        lats = String.valueOf(latLng.latitude);
        lgts = String.valueOf(latLng.longitude);
        GSApplication.getInstance().setLatitude(latLng.latitude);
        GSApplication.getInstance().setLongitude(latLng.longitude);
        Log.i("postion", lats + "++++" + lgts);
        markerOptions.position(latLng);
        aMap.addMarker(markerOptions);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onGeocodeSearched(GeocodeResult result, int rCode) {

            }

            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {


                Log.e("formatAddress", "formatAddress:" );
                Log.e("formatAddress", "rCode:" + rCode);
//                city = result.getRegeocodeAddress().getFormatAddress();
                //省
                shengsheng = result.getRegeocodeAddress().getProvince();
                //市
                shishi = result.getRegeocodeAddress().getCity();
                //区
                ququ = result.getRegeocodeAddress().getDistrict();
                CityCode = result.getRegeocodeAddress().getCityCode();
                AdCode = result.getRegeocodeAddress().getAdCode();
                //街道
                jiedao = result.getRegeocodeAddress().getStreetNumber().getStreet() + result.getRegeocodeAddress().getStreetNumber().getNumber();
                Log.i("address", shengsheng + "-" + shishi + ququ + "--" + CityCode + "===" + AdCode);
                et_search_map.setText(shengsheng + shishi + ququ + jiedao);

            }
        });
        LatLonPoint lp = new LatLonPoint(Double.parseDouble(lats), Double.parseDouble(lgts));
        RegeocodeQuery query = new RegeocodeQuery(lp, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_header_back:
                finish();
                break;
            case R.id.tv_save://保存地图信息返回开店第二步
                if (shengsheng.equals("")) {
                    showTip("未获取定位信息，请重新定位", Toast.LENGTH_SHORT);
                } else {
                    handler.sendEmptyMessage(REQUESTCODE);
                }

                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("lat", lats);
            bundle.putSerializable("lgt", lgts);
            bundle.putSerializable("sheng", shengsheng);
            bundle.putSerializable("shi", shishi);
            bundle.putSerializable("qu", ququ);
            bundle.putSerializable("CityCode", CityCode);
            bundle.putSerializable("AdCode", AdCode);
            bundle.putSerializable("jiedao", jiedao);
            data.putExtras(bundle);
            setResult(REQUESTCODE, data);
            finish();
        }

    };

    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 修改网络协议为https；适配9.0
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTPS);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                double la = aMapLocation.getLatitude();//获取纬度
                double lo = aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码
                //将坐标设置成全局
                GSApplication.getInstance().setLatitude(la);
                GSApplication.getInstance().setLongitude(lo);
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    //将地图移动到定位点
                    //点击定位按钮 能够将地图的中心移动到定位点
                    //添加图钉
                    //  aMap.addMarker(getMarkerOptions(amapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getCity() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getDistrict() + ""
                            + aMapLocation.getStreet() + ""
                            + aMapLocation.getStreetNum());

                    isFirstLoc = false;
                    //将坐标设置成全局
                    GSApplication.getInstance().setLatitude(aMapLocation.getLatitude());
                    GSApplication.getInstance().setLongitude(aMapLocation.getLongitude());
                }
//                Log.i("sheng", aMapLocation.getProvince() + ""
//                        + aMapLocation.getCity() + ""
//                        + aMapLocation.getDistrict() + "");
                if (shengsheng.equals("")) {
                    shengsheng = aMapLocation.getProvince();
                    shishi = aMapLocation.getCity();
                    ququ = aMapLocation.getDistrict();
                    CityCode = aMapLocation.getCityCode();
                    AdCode = aMapLocation.getAdCode();
                    jiedao = aMapLocation.getStreet() + aMapLocation.getStreetNum();
                }
                et_search_map.setText(shengsheng + shishi + ququ + jiedao);
                Log.i("address", jiedao + "-" + shishi + ququ + "--" + CityCode + "===" + AdCode);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
//                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
//        locationUtil.startLocate(getApplicationContext());
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


}
