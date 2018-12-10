package com.xinzuokeji.boxtakeawaybusiness.orderManagement;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.xinzuokeji.boxtakeawaybusiness.BaseMapActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

/**
 * Created by Administrator on 2018/8/17.
 */
//商家到用户路线规划
public class OrderMap extends BaseMapActivity implements View.OnClickListener, AMap.OnMapLoadedListener {
    private LinearLayout ll_header_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

        setContentView(R.layout.activity_order_map);
        // 标题信息
        ll_header_back = findViewById(R.id.ll_header_back);
        ll_header_back.setOnClickListener(this);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("路线规划");
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
//        mAMapNaviView.setOnMapLoadedListener(this);
    }

    //106.655282,29.503484
//    106.670374,29.503359
    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        mAMapNavi.calculateRideRoute(new NaviLatLng(29.503484, 106.655282), new NaviLatLng(29.503359, 106.670374));
//        mAMapNavi.calculateRideRoute(new NaviLatLng(106.48387175426588, 29.52989070674608), new NaviLatLng(106.48387175426598, 29.52989070674908));
    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_header_back:
                finish();
                break;

            default:
                break;
        }
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

    @Override
    public void onMapLoaded() {

    }
}
