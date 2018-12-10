package com.xinzuokeji.boxtakeawaybusiness.ceshi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;

import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.util.maptts.TTSController;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/25.
 */
//测试
public class AmapNaviActivity extends BaseActivity implements AMapNaviListener,
        AMapNaviViewListener, AMapLocationListener {
    private static final String TAG = AmapNaviActivity.class.getName();

    /**
     * 3D导航地图对象
     */
    private AMapNaviView mAMapNaviView;

    /**
     * 导航对象
     */
    private AMapNavi mAMapNavi;

    /**
     * 语音对象
     */
    private TTSController mTtsManager;

    /**
     * 起点坐标
     */
    private final List<NaviLatLng> startList = new ArrayList<NaviLatLng>();

    /**
     * 终点坐标
     */
    private final List<NaviLatLng> endList = new ArrayList<NaviLatLng>();

    /**
     * 途经点坐标
     */
    private List<NaviLatLng> mWayPointList = new ArrayList<NaviLatLng>();

    /**
     * 声明mLocationOption对象
     */
    private AMapLocationClientOption mLocationOption = null;

    /**
     * 声明mlocationClient对象
     */
    private AMapLocationClient mlocationClient = null;

    /**
     * 导航方式
     */
//    private NaviWay naviWay;

    /**
     * 导航参数
     */
    private JSONArray naviData;

    /**
     * 线程句柄
     */
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAMapNaviView = new AMapNaviView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mAMapNaviView.setLayoutParams(layoutParams);
        setContentView(mAMapNaviView);

//        naviWay = (NaviWay) getIntent().getSerializableExtra(AMapNaviPlugin.NAVI_WAY);
//        try {
//            naviData = new JSONArray(getIntent().getStringExtra(AMapNaviPlugin.NAVI_DATA));
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//            finish();
//        }

//        initNaviData();
        try {
            // 起点坐标为空,则获取设备当前坐标作为起点
//            JSONObject startPos = naviData.getJSONObject(0);
            startList.add(new NaviLatLng(40.084894, 116.603039));
            endList.add(new NaviLatLng(39.825934, 116.342972));
            initNavi();
        } catch (Exception e) {
            // 使用定位,获取当前坐标
            Log.i(TAG, "起点坐标为空,获取设备定位坐标作为起点.");
            initLocation();
        }

        mAMapNaviView.onCreate(savedInstanceState);
        setAmapNaviViewOptions();
        mAMapNaviView.setAMapNaviViewListener(this);
    }

    /**
     * 初始化导航起始点、途经点参数
     */
//    public void initNaviData() {
//        JSONObject endPos = null;
//        if (naviWay == AMapNaviPlugin.NaviWay.WALK) {
//            // 步行导航方式
//            endPos = naviData.optJSONObject(1);
//            endList.add(new NaviLatLng(endPos.optDouble("latitude"), endPos.optDouble("longitude")));
//        } else {
//            // 驾车导航方式
//            endPos = naviData.optJSONObject(2);
//            endList.add(new NaviLatLng(endPos.optDouble("latitude"), endPos.optDouble("longitude")));
//            // 途经点,最多设置3个
//            JSONArray wayPosArray = naviData.optJSONArray(1) == null ? new JSONArray() : naviData.optJSONArray(1);
//            int length = wayPosArray.length() > 3 ? 3 : wayPosArray.length();
//            for (int i = 0; i < length; i++) {
//                JSONObject wayPos = naviData.optJSONObject(i);
//                mWayPointList.add(new NaviLatLng(wayPos.optDouble("latitude"), wayPos.optDouble("longitude")));
//            }
//        }
//    }

    /**
     * 初始化导航
     */
    public void initNavi() {
        // 实例化语音引擎
        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.init();
//        mTtsManager.startSpeaking();

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        mAMapNavi.addAMapNaviListener(mTtsManager);

        // 设置模拟导航的行车速度
        mAMapNavi.setEmulatorNaviSpeed(75);
    }

    /**
     * 设置导航参数
     */
    private void setAmapNaviViewOptions() {
        if (mAMapNaviView == null) {
            return;
        }
        AMapNaviViewOptions viewOptions = new AMapNaviViewOptions();
        // viewOptions.setTilt(0); // 0 表示使用2D地图
        // viewOptions.setSettingMenuEnabled(true);//设置菜单按钮是否在导航界面显示
//         viewOptions.setNaviNight(mDayNightFlag);//设置导航界面是否显示黑夜模式
        viewOptions.setReCalculateRouteForYaw(true);// 设置偏航时是否重新计算路径mDeviationFlag
        // viewOptions.setReCalculateRouteForTrafficJam(mJamFlag);//前方拥堵时是否重新计算路径
        // viewOptions.setTrafficInfoUpdateEnabled(mTrafficFlag);//设置交通播报是否打开
        // viewOptions.setCameraInfoUpdateEnabled(mCameraFlag);//设置摄像头播报是否打开
        // viewOptions.setScreenAlwaysBright(mScreenFlag);//设置导航状态下屏幕是否一直开启。
        // viewOptions.setNaviViewTopic(mThemeStle);//设置导航界面的主题
        mAMapNaviView.setViewOptions(viewOptions);
    }

    /**
     * 获取定位坐标
     */
    public void initLocation() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    /**
     * 定位回调接口实现
     */
    @SuppressLint("SimpleDateFormat")
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                startList.add(new NaviLatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
                initNavi();
                onInitNaviSuccess();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() +
                        ", errInfo:" + amapLocation.getErrorInfo());
                Toast.makeText(AmapNaviActivity.this, "location Error, ErrCode:" + amapLocation.getErrorCode() +
                        ", errInfo:" + amapLocation.getErrorInfo(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mAMapNaviView.onPause();
            // 仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
            mTtsManager.stopSpeaking();
            // 停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
            // mAMapNavi.stopNavi();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mAMapNaviView.onDestroy();
            // since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
            mAMapNavi.stopNavi();
            mAMapNavi.destroy();
            mTtsManager.destroy();
            if (null != mlocationClient) {
                /**
                 * 如果AMapLocationClient是在当前Activity实例化的，
                 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
                 */
                mlocationClient.onDestroy();
                mlocationClient = null;
                mLocationOption = null;
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onInitNaviFailure() {
        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitNaviSuccess() {
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion,
         * avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         * 	说明:
         *		以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *		注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        mAMapNavi.calculateWalkRoute(startList.get(0), endList.get(0)); // 步行导航
        mAMapNavi.calculateDriveRoute(startList, endList, mWayPointList, strategy);
//        if (naviWay == AMapNaviPlugin.NaviWay.WALK) {
//            mAMapNavi.calculateWalkRoute(startList.get(0), endList.get(0)); // 步行导航
//        } else {
//            mAMapNavi.calculateDriveRoute(startList, endList, mWayPointList, strategy);
//        }
    }

    @Override
    public void onStartNavi(int type) {
    }

    @Override
    public void onTrafficStatusUpdate() {
    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {
    }

    @Override
    public void onGetNavigationText(int type, String text) {
    }

    @Override
    public void onGetNavigationText(String s) {

    }

    /**
     * 模拟导航结束
     */
    @Override
    public void onEndEmulatorNavi() {
    }

    /**
     * 到达目的地
     */
    @Override
    public void onArriveDestination() {
    }

    /**
     * 路径规划完毕,导航开始
     */
    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        Log.i(TAG, "路径规划完毕,开始导航.");
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                boolean naviType = true; // true表示模拟导航，false表示真实GPS导航（默认true）
//                if (naviWay == AMapNaviPlugin.NaviWay.WALK) {
//                    naviType = naviData.optBoolean(2, true);
//                } else {
//                    naviType = naviData.optBoolean(3, true);
//                }
                if (naviType) {
                    mAMapNavi.startNavi(NaviType.EMULATOR);
                } else {
                    mAMapNavi.startNavi(NaviType.GPS);
                }
            }
        }, 3000);
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
        Toast.makeText(AmapNaviActivity.this, "onCalculateRouteFailure code : " + errorInfo, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReCalculateRouteForYaw() {
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
    }

    @Override
    public void onArrivedWayPoint(int wayID) {
    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
    }

    @Override
    public void onNaviSetting() {
    }

    @Override
    public void onNaviMapMode(int isLock) {
    }

    /**
     * 导航结束
     */
    @Override
    public void onNaviCancel() {
        Log.i(TAG, "导航结束.");
        finish();
    }

    @Override
    public void onNaviTurnClick() {
    }

    @Override
    public void onNextRoadClick() {
    }

    @Override
    public void onScanViewButtonClick() {
    }

    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
    }

    @Override
    public void hideCross() {
    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {
    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {
    }


    @Override
    public void notifyParallelRoad(int i) {
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
    }

    @Override
    public void updateAimlessModeCongestionInfo(
            AimLessModeCongestionInfo aimLessModeCongestionInfo) {
    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

//    @Override
//    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {
//
//    }

    @Override
    public void onLockMap(boolean isLock) {
    }

    @Override
    public void onNaviViewLoaded() {
        Log.d("wlx", "导航页面加载成功");
        Log.d("wlx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }

//    @Override
//    public void onMapTypeChanged(int i) {
//
//    }

//    @Override
//    public void onNaviViewShowMode(int i) {
//
//    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    /**
     * 返回键处理事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
