package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.Shopbasic;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.AppUtil;
import com.xinzuokeji.boxtakeawaybusiness.util.Constants;
import com.xinzuokeji.boxtakeawaybusiness.util.LoadingDialog;
import com.xinzuokeji.boxtakeawaybusiness.util.PictureUtil;
import com.xinzuokeji.boxtakeawaybusiness.util.TakePictureManager;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;
import com.ywp.addresspickerlib.AddressPickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.xinzuokeji.boxtakeawaybusiness.GSApplication.REQUESTCODE;

//申请开店（基本信息）
public class SetUpShopTwo extends BaseActivity implements AMapLocationListener {
    private PopupWindow popupWindow;
    private TextView pop_location;
    String hours = "";
    String week = "";
    private ImageButton header_back;
    private Button bt_next_shop;
    private TextView tv_address, tv_business_hours, tv_end_business_hours, sheng_tv_address;
    LocationManager lm;
    NetService netService;
    EditText et_shop_phone, et_contacts, et_contacts_phone, et_shop_address;//外卖电话
    private String shop_phone, contacts, contacts_phone, shop_address, sheng, shi, qu, CityCode, AdCode, longitude = "29.62713", dimension = "106.476795";
    //三张店铺图以及共同的iv
    ImageView out_img, in_img, logo, iv_store_img;

    private TextView tv_out_img, tv_im_img, tv_logo, tv_stors_img;
    //    上传图片的地址
    String urlImgout;
    String urlImgin;
    String urlImglogo;
    String ImgName;
    LinearLayout ll_business_time, ll_sheng_shi_qu, ll_yingye_time;
    Calendar c = Calendar.getInstance();
    public static Activity mActivity;
    private String mUploadImageId = "";
    String url = "";
    String Bucket;
    String picName;
    private TakePictureManager takePictureManager;

    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    LoadingDialog.Builder loadBuilder;
    LoadingDialog dialog = null;

    @SuppressLint("MissingPermission")
    @Override
    public void initView() {
        super.initView();
        mActivity = this;
        setContentView(R.layout.activity_set_up_shop_two);
//        如果定位没打开，强行打开
        if (!isOPen(this)) {
            openGPS(this);
        }
        netService = new NetService(this);
        popupWindow = new PopupWindow(this);
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("基本信息");
        textView.setTextColor(getResources().getColor(R.color.white));
        bt_next_shop = findViewById(R.id.bt_next_shop);
        tv_address = findViewById(R.id.tv_address);//标记地图位置
        et_shop_phone = findViewById(R.id.et_shop_phone);//外卖电话
        et_contacts = findViewById(R.id.et_contacts);//联系人姓名
        et_contacts_phone = findViewById(R.id.et_contacts_phone);//联系人电话
        et_shop_address = findViewById(R.id.et_shop_address);//详细地址
        tv_business_hours = findViewById(R.id.tv_business_hours);//营业时间
        tv_end_business_hours = findViewById(R.id.tv_end_business_hours);
        sheng_tv_address = findViewById(R.id.sheng_tv_address);//选择省市区
        ll_business_time = findViewById(R.id.ll_business_time);//选择时间
        ll_sheng_shi_qu = findViewById(R.id.ll_sheng_shi_qu);//选择省市区
        // 门店照
        tv_out_img = findViewById(R.id.tv_out_img);
        tv_im_img = findViewById(R.id.tv_im_img);
        tv_logo = findViewById(R.id.tv_logo);
        out_img = findViewById(R.id.img_out_img);
        in_img = findViewById(R.id.in_img);
        logo = findViewById(R.id.logo);
        //tv_pop_location 只是标记popwindow 位置
        pop_location = findViewById(R.id.tv_pop_location);
        et_shop_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //外卖电话
                shop_phone = et_shop_phone.getText().toString().trim();
            }
        });
        et_contacts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //联系人姓名
                contacts = et_contacts.getText().toString().trim();
            }
        });
        et_contacts_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //联系人电话
                contacts_phone = et_contacts_phone.getText().toString().trim();
            }
        });
        et_shop_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //店铺地址
                shop_address = et_shop_address.getText().toString().trim();
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.ACCESS_FINE_LOCATION_COMMANDS_REQUEST_CODE);
        } else {
            /**
             * 初始化用户位置
             */
            location();
        }

        //获取店铺信息
        netService.showShopbasic(getChangeStoreId(), showShopbasic);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_next_shop.setOnClickListener(this);
        tv_address.setOnClickListener(this);
        registerForContextMenu(out_img);//注册菜单
        out_img.setOnClickListener(this);
        registerForContextMenu(in_img);//注册菜单
        in_img.setOnClickListener(this);
        registerForContextMenu(logo);//注册菜单
        logo.setOnClickListener(this);
//        ll_business_time.setOnClickListener(this);
        tv_business_hours.setOnClickListener(this);
        tv_end_business_hours.setOnClickListener(this);
        ll_sheng_shi_qu.setOnClickListener(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;

            case R.id.tv_business_hours:

                showTimePickerDialog(this, 3, tv_business_hours, c);
                break;
            case R.id.tv_end_business_hours:

                showTimePickerDialog(this, 3, tv_end_business_hours, c);
                break;
            case R.id.ll_sheng_shi_qu://选择省市区
//                showAddressPickerPop();
                checkLocationPermission();

                break;
            //  将iv赋给共同的view以便显示
            // 1，门脸照，2，店内照，3，门店
            case R.id.img_out_img:
                iv_store_img = out_img;
                tv_stors_img = tv_out_img;//要隐藏的十字
                ImgName = "1";
                openContextMenu(out_img);
                break;
            case R.id.in_img:
                iv_store_img = in_img;
                tv_stors_img = tv_im_img;
                ImgName = "2";
                openContextMenu(in_img);
                break;
            case R.id.logo:
                iv_store_img = logo;
                tv_stors_img = tv_logo;
                ImgName = "3";
                openContextMenu(logo);
                break;
            case R.id.bt_next_shop:
                //更新店铺信息
                if (Valid.isPhone(contacts_phone) && Valid.isPhone(shop_phone)) {
                    // Log.i("ssss", dimension + "===" + tv_business_hours.getText().toString() + "-" + tv_end_business_hours.getText().toString() + "--" + contacts_phone + "---" + contacts + "--" + shop_address + "--" + longitude + "--" + urlImgout + "--" + urlImgin);
                    if (Valid.isNotNullOrEmpty(sheng) && Valid.isNotNullOrEmpty(tv_business_hours.getText().toString()) && Valid.isNotNullOrEmpty(tv_end_business_hours.getText().toString()) && Valid.isNotNullOrEmpty(contacts_phone) && Valid.isNotNullOrEmpty(contacts) && Valid.isNotNullOrEmpty(shop_phone) && Valid.isNotNullOrEmpty(shop_address) && Valid.isNotNullOrEmpty(longitude) && Valid.isNotNullOrEmpty(dimension) && Valid.isNotNullOrEmpty(urlImgout) && Valid.isNotNullOrEmpty(urlImgin) && Valid.isNotNullOrEmpty(urlImglogo)) {
                        if (tv_business_hours.getText().toString().equals("开始")) {
                            tv_business_hours.setText("00:00");
                        }
                        if (tv_end_business_hours.getText().toString().equals("结束")) {
                            tv_end_business_hours.setText("23:59");
                        }
                        long startime = getStringToDate(tv_business_hours.getText().toString().trim(), "HH:mm");
                        long endtime = getStringToDate(tv_end_business_hours.getText().toString().trim(), "HH:mm");
                        if (startime < endtime) {
                            //每次都是添加，没有修改
                            // 添加店铺基本信息add
                            netService.addShopbasic(getChangeStoreId(), contacts_phone, contacts, shop_phone, tv_business_hours.getText().toString() + "-" + tv_end_business_hours.getText().toString(), sheng, shi, qu, shop_address, longitude, dimension, urlImgout, urlImgin, urlImglogo, AdCode, Handle_addShopbasic);
//                        if (BaseActivity.getRecordStype().equals(shopUpdate)) {
//                            //  修改店铺基本信息
//                            netService.updShopbasic(getChangeStoreId(), contacts_phone, contacts, shop_phone, tv_business_hours.getText().toString() + "-" + tv_end_business_hours.getText().toString(), sheng, shi, qu, shop_address, dimension, longitude, urlImgout, urlImgin, urlImglogo, AdCode, Handle_updShopbasic);
//                        } else {
//
//                        }
                        } else {
                            showTip("营业开始时间不得晚于营业结束时间", Toast.LENGTH_SHORT);
                        }
                    } else {
                        showTip("请将信息填写完整", Toast.LENGTH_SHORT);
                    }
                } else {
                    showTip("请正确填写手机号码", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.tv_address://跳转到地图定位
//                Intent intent = new Intent(this, GspLocation.class);
//                startActivityForResult(intent, REQUESTCODE);

                break;
            default:
                break;
        }
    }


    /**
     * 检查定位服务、权限
     */
    private void checkLocationPermission() {
        if (!AppUtil.isLocServiceEnable(this)) {//检测是否开启定位服务
//            if (netErrorDialog == null || !netErrorDialog.isShowing()) {
//                locErrorDialog = DialogUtil.showLocErrorDialog(activity, 0);
//            }
            showLocIgnoredDialog(this);
        } else {//检测用户是否将当前应用的定位权限拒绝
            int checkResult = AppUtil.checkOp(this, 2, AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW);
            //其中2代表AppOpsManager.OP_GPS，如果要判断悬浮框权限，第二个参数需换成24即AppOpsManager。OP_SYSTEM_ALERT_WINDOW及，第三个参数需要换成AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW
            int checkResult2 = AppUtil.checkOp(this, 1, AppOpsManager.OPSTR_FINE_LOCATION);
            if (AppOpsManagerCompat.MODE_IGNORED == checkResult || AppOpsManagerCompat.MODE_IGNORED == checkResult2) {
//                if (netErrorDialog == null || !netErrorDialog.isShowing()) {
//                    locErrorDialog = DialogUtil.showLocErrorDialog(activity, 1);
//                }
                showLocIgnoredDialog(this);
            } else {
                Intent intent = new Intent(this, GspLocation.class);
                startActivityForResult(intent, REQUESTCODE);
            }
        }
    }

    /**
     * 显示定位权限被拒绝对话框
     */
    public void showLocIgnoredDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("手机已关闭位置权限");
        builder.setMessage("请在 设置-应用权限 (将位置权限打开))");

        //监听下方button点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    localIntent.setAction(Intent.ACTION_VIEW);
                    localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                }
                context.startActivity(localIntent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        //设置对话框是可取消的
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.show();
    }


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
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
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
                Log.i("city", aMapLocation.getCity() + "--");
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

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();//停止定位
        mLocationClient.onDestroy();//销毁定位客户端。

    }

    /**
     * 显示地址选择的pop
     */
    private void showAddressPickerPop() {

        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        View rootView = LayoutInflater.from(this).inflate(R.layout.pop_address_picker, null, false);
        final AddressPickerView addressView = rootView.findViewById(R.id.apvAddress);
        addressView.setOnAddressPickerSure(new AddressPickerView.OnAddressPickerSureListener() {
            @Override
            public void onSureClick(String address, String provinceCode, String cityCode, String districtCode) {
                sheng_tv_address.setText(address);
                popupWindow.dismiss();
                //截取省市区
                String[] words = address.split(" ");
                for (int i = 0; i < words.length; i++) {
                    sheng = words[0];
                    shi = words[1];
                    qu = words[2];
                }

            }
        });
        popupWindow.setContentView(rootView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.showAsDropDown(pop_location);

    }

    // 监听返回按键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            } else {
                //绑定返回按键和返回按钮
                ImageButton header_back = findViewById(R.id.header_back);
                if (header_back != null) {
                    header_back.performClick();
                }
            }


            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    @SuppressLint("HandlerLeak")
    Handler Handle_updShopbasic = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        gotoActivity(SetUpShopThree.class.getName(), null);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler Handle_addShopbasic = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        gotoActivity(SetUpShopThree.class.getName(), null);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
                    showTip(getString(R.string.network_error), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler showShopbasic = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        com.xinzuokeji.boxtakeawaybusiness.entities.Shopbasic shopbasic = (Shopbasic) msg.obj;
                        // addressid 为0增加店铺否则修改店铺
                        if (shopbasic.address_id.equals("0")) {
                            BaseActivity.setRecordStype(shopAdd);
                        } else {
                            BaseActivity.setRecordStype(shopUpdate);
                        }
                        et_shop_phone.setText(shopbasic.shop_phone);
                        et_contacts.setText(shopbasic.contacts);
                        et_contacts_phone.setText(shopbasic.contacts_phone);
                        et_shop_address.setText(shopbasic.shop_address);
                        if (Valid.isNotNullOrEmpty(shopbasic.sheng)) {
                            sheng_tv_address.setText(shopbasic.sheng + "-" + shopbasic.shi + "-" + shopbasic.qu);
                        }
                        shop_phone = shopbasic.shop_phone;
                        contacts = shopbasic.contacts;
                        contacts_phone = shopbasic.contacts_phone;
                        shop_address = shopbasic.shop_address;
                        sheng = shopbasic.sheng;
                        shi = shopbasic.shi;
                        qu = shopbasic.qu;
                        longitude = shopbasic.longitude;
                        dimension = shopbasic.dimension;
                        AdCode = shopbasic.code;
                        //  是否获取到坐标
                        if (!Valid.isNotNullOrEmpty(longitude) || !Valid.isNotNullOrEmpty(dimension) || longitude.equals("0.0") || dimension.equals("0.0")) {
                            tv_address.setText("标记地图位置");
                        } else {
                            tv_address.setText("已标记");
                        }
                        // 营业时间段
                        hours = shopbasic.business_hours;
                        if (Valid.isNotNullOrEmpty(shopbasic.business_hours)) {
                            String start_hours = shopbasic.business_hours.substring(0, shopbasic.business_hours.indexOf("-"));
                            String end_hours = shopbasic.business_hours.substring(6, shopbasic.business_hours.length());
                            tv_business_hours.setText(start_hours);
                            tv_end_business_hours.setText(end_hours);
                        }

                        //
                        if (Valid.isNotNullOrEmpty(shopbasic.in_img)) {
                            tv_im_img.setVisibility(View.GONE);
                            urlImgin = shopbasic.in_img;
                            ImageLoader.getInstance().displayImage(shopbasic.in_img, in_img, GSApplication.getInstance().imageOptions);
                        }
                        if (Valid.isNotNullOrEmpty(shopbasic.out_img)) {
                            tv_out_img.setVisibility(View.GONE);
                            urlImgout = shopbasic.out_img;
                            ImageLoader.getInstance().displayImage(shopbasic.out_img, out_img, GSApplication.getInstance().imageOptions);
                        }
                        if (Valid.isNotNullOrEmpty(shopbasic.logo)) {
                            tv_logo.setVisibility(View.GONE);
                            urlImglogo = shopbasic.logo;
                            ImageLoader.getInstance().displayImage(shopbasic.logo, logo, GSApplication.getInstance().imageOptions);
                        }
                    } else {
//                        BaseActivity.setRecordStype(shopAdd);
                    }

                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
                    showTip(getString(R.string.network_error), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(getResources().getString(R.string.fragment_me_thumbnail));
        menu.add(0, 101, 1, R.string.action_pick);
        menu.add(0, 102, 2, R.string.action_capture);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {


            switch (item.getItemId()) {
                case 101:
                    takePictureManager = new TakePictureManager(this);
                    takePictureManager.setTailor(1, 1, 350, 350);
                    takePictureManager.startTakeWayByAlbum();
                    takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
                        @Override
                        public void successful(boolean isTailor, File outFile, Uri filePath) {
                            loadBuilder = new LoadingDialog.Builder(SetUpShopTwo.this)
                                    .setMessage("")
                                    .setCancelable(true)
                                    .setCancelOutside(true);
                            dialog = loadBuilder.create();
                            dialog.show();
                            //显示头像
                            mUploadImageId = outFile.getPath();
                            netService.appSts(handler_appstr);
                            ImageLoader.getInstance().clearDiskCache();
                            ImageLoader.getInstance().clearMemoryCache();
                            tv_stors_img.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), iv_store_img, GSApplication.getInstance().imageOptions);
                        }

                        @Override
                        public void failed(int errorCode, List<String> deniedPermissions) {
                            showTip("图片选择失败", Toast.LENGTH_SHORT);
                        }

                    });
                    break;
                case 102:
                    takePictureManager = new TakePictureManager(this);
                    //开启裁剪 比例 1:3 宽高 350 350  (默认不裁剪)
                    takePictureManager.setTailor(1, 1, 350, 350);
                    //拍照方式
                    takePictureManager.startTakeWayByCarema();
                    //回调
                    takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
                        //成功拿到图片,isTailor 是否裁剪？ ,outFile 拿到的文件 ,filePath拿到的URl
                        @Override
                        public void successful(boolean isTailor, File outFile, Uri filePath) {
                            Log.i("outFile", outFile.getPath() + "---" + filePath);
                            loadBuilder = new LoadingDialog.Builder(SetUpShopTwo.this)
                                    .setMessage("")
                                    .setCancelable(true)
                                    .setCancelOutside(true);
                            dialog = loadBuilder.create();
                            dialog.show();
                            //显示头像
                            mUploadImageId = outFile.getPath();
                            netService.appSts(handler_appstr);
                            ImageLoader.getInstance().clearDiskCache();
                            ImageLoader.getInstance().clearMemoryCache();
                            tv_stors_img.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), iv_store_img, GSApplication.getInstance().imageOptions);
                        }

                        //失败回调
                        @Override
                        public void failed(int errorCode, List<String> deniedPermissions) {
                            Log.e("==w", deniedPermissions.toString());
                            showTip("图片选择失败", Toast.LENGTH_SHORT);
                        }
                    });
                    break;
                default:
                    break;
            }
            return super.onContextItemSelected(item);
        } else {
            Toast.makeText(getApplicationContext(), "没找到手机SD卡，头像无法上传！", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    //onRequestPermissionsResult()方法权限回调绑定到对象
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePictureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 选择时间（暂时没用）
        if (requestCode == 10) {
            if (data != null) {
                tv_business_hours.setText(data.getStringExtra("business_hours"));
                hours = data.getStringExtra("business_hours");
            }

        }
        //地图回调
        if (resultCode == REQUESTCODE) {
            if (data != null) {
                dimension = data.getStringExtra("lat");
                longitude = data.getStringExtra("lgt");
                sheng = data.getStringExtra("sheng");
                shi = data.getStringExtra("shi");
                qu = data.getStringExtra("qu");
                CityCode = data.getStringExtra("CityCode");
                AdCode = data.getStringExtra("AdCode");
                tv_address.setText("已标记");
                sheng_tv_address.setText(sheng + "-" + shi + "-" + qu);
            } else {
                tv_address.setText("标记地图位置");
            }

            Log.i("location", longitude + "--" + dimension + sheng + shi + qu + CityCode + "---" + AdCode);
        }

        // 相机回调绑定
        if (RESULT_OK == resultCode) {
            takePictureManager.attachToActivityForResult(requestCode, resultCode, data);
        }
    }


    @SuppressLint("HandlerLeak")
    Handler handler_appstr = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String AccessKeyId = jsonObject.getString("AccessKeyId");
                        String AccessKeySecret = jsonObject.getString("AccessKeySecret");
                        String SecurityToken = jsonObject.getString("SecurityToken");
                        String EndPoint = jsonObject.getString("EndPoint");
                        String BuchetName = jsonObject.getString("Title");
                        Bucket = jsonObject.getString("Bucket");
                        //  实例化
                        GSApplication.getInstance().initOss(AccessKeyId, AccessKeySecret, SecurityToken, EndPoint);
                        //拼接文件名
                        picName = GSApplication.pictures + "/" + year() + "/" + getIdentity() + ".jpg";
                        upfileOss(BuchetName, picName, PictureUtil.getBytes(PictureUtil.getSmallBitmap(mUploadImageId)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 2001:
                    Log.d("msd", "---22");
                    break;
                case 1001:
                    Log.d("msd", "---33");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //此处这个bucketName是在阿里云平台上配置的 用来储存文件 可以设置私有公有等属性
    // objectKey为文件的名字 用来从平台上查询图片
    // FilePath为所选文件的uri路径

    public void upfileOss(String bucketName, String objectKey, byte[] uploadFilePath) {
        Log.i("sign", bucketName + "----" + objectKey + "---" + uploadFilePath);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, uploadFilePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>()

        {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = GSApplication.getInstance().ossNew.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public String onSuccess(PutObjectRequest request, PutObjectResult result) {
                dialog.dismiss();
                if (ImgName.equals("1")) {
                    urlImgout = "https://" + Bucket + "/" + picName;
                }
                if (ImgName.equals("2")) {
                    urlImgin = "https://" + Bucket + "/" + picName;
                }
                if (ImgName.equals("3")) {
                    urlImglogo = "https://" + Bucket + "/" + picName;
                }

//                你上传的资源objectKey;
                Log.d("PutObject", "UploadSuccess" + result.toString() + "--" + result.getServerCallbackReturnBody());
                Log.d("ETag", urlImgout + "--" + urlImgin + "--" + urlImglogo);
                Log.d("RequestId", result.getRequestId());
                return null;
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    Log.i("sign", "2222" + request.getUploadFilePath() + "--" + request.getBucketName() + "--" + clientExcepion.getMessage());
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {

                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }
}
