package com.xinzuokeji.boxtakeawaybusiness.me.SoreSettings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.ACache;
import com.xinzuokeji.boxtakeawaybusiness.util.PictureUtil;
import com.xinzuokeji.boxtakeawaybusiness.util.TakePictureManager;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
/**
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *            佛曰:
 *                   写字楼里写字间，写字间里程序员；
 *                   程序人员写程序，又拿程序换酒钱。
 *                   酒醒只在网上坐，酒醉还来网下眠；
 *                   酒醉酒醒日复日，网上网下年复年。
 *                   但愿老死电脑间，不愿鞠躬老板前；
 *                   奔驰宝马贵者趣，公交自行程序员。
 *                   别人笑我忒疯癫，我笑自己命太贱；
 *                   不见满街漂亮妹，哪个归得程序员？
 */


//门店设置
public class StoreSettingNew extends BaseActivity {
    private ImageButton header_back;
    LinearLayout ll_shop_announcement, ll_change_phone, ll_store_address, ll_management_standard,
            ll_agreement, ll_yingye_time, ll_yingye_qualifications, ll_shop_introduce, ll_shop_photo, ll_shipping_information, ll_me_thumbnail;
    NetService netService;
    private TextView tv_shop_status, tv_shop_phone, tv_shop_address, tv_business_hours;
    private int bid;
    private double price;
    EditText et_send_price;
    ImageView circleImageView;//头像
    /* 请求码*/
    private static final int CROPIMAGE_REQUEST_CODE = 1002;
    private static final int mCropImageWidth = 192;
    private static final int STOREYINGYETIME = 2;
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    private File mFileCacheDirectory;
    private Uri mImageUri;
    private String business_week = "", shop_phone;
    private String business_hours = "";
    private String mUploadImageId = "";
    String url = "";
    String Bucket;
    String picName;
    private TakePictureManager takePictureManager;
    private String sheng, shi, qu, shop_address;
    String longitude, dimension, code;
    //    实例化数据库
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    public void initView() {
        super.initView();
        SharedPreferences mSharedPreferences = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();

        setContentView(R.layout.activity_store_setting_new);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        takePictureManager = new TakePictureManager(this);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("门店设置");
        circleImageView = findViewById(R.id.btn_me_thumbnail);
        ll_me_thumbnail = findViewById(R.id.ll_me_thumbnail);
        ll_shop_announcement = findViewById(R.id.ll_shop_announcement);
        ll_shop_introduce = findViewById(R.id.ll_shop_introduce);
        ll_shop_photo = findViewById(R.id.ll_shop_photo);
        ll_change_phone = findViewById(R.id.ll_change_phone);
        ll_agreement = findViewById(R.id.ll_agreement);
        ll_yingye_time = findViewById(R.id.ll_yingye_time);
        ll_yingye_qualifications = findViewById(R.id.ll_yingye_qualifications);
        // 配送信息
        ll_shipping_information = findViewById(R.id.ll_shipping_information);
        // 餐厅地址
        ll_store_address = findViewById(R.id.ll_store_address);
        //管理规范
        ll_management_standard = findViewById(R.id.ll_management_standard);
        //显示数据
        tv_shop_status = findViewById(R.id.tv_shop_status);
        et_send_price = findViewById(R.id.et_send_price);//修改价格
        et_send_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Valid.isNotNullOrEmpty(et_send_price.getText().toString())) {
                    price = Double.parseDouble(et_send_price.getText().toString());
                } else {
                    price = 0.0;
                }
            }
        });
        tv_shop_phone = findViewById(R.id.tv_shop_phone);
        tv_shop_address = findViewById(R.id.tv_shop_address);
        tv_business_hours = findViewById(R.id.tv_business_hours);
        // 如果数据库不为空从本地数据库中获取
        if (Valid.isNotNullOrEmpty(getstoreIcon())) {
            ImageLoader.getInstance().displayImage(getstoreIcon(), circleImageView, GSApplication.getInstance().optionshead);
        }
        netService = new NetService(this);
        //初始化调用接口Integer.parseInt(getuserinfoId())
        netService.ShowShopdata(GetstoreId(), ShowShopdata);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_shop_announcement.setOnClickListener(this);
        ll_change_phone.setOnClickListener(this);
        ll_agreement.setOnClickListener(this);
        ll_yingye_time.setOnClickListener(this);
        ll_yingye_qualifications.setOnClickListener(this);
        tv_shop_status.setOnClickListener(this);
        registerForContextMenu(circleImageView);//注册菜单
        circleImageView.setOnClickListener(this);
        ll_shop_introduce.setOnClickListener(this);
        ll_shop_photo.setOnClickListener(this);
        ll_shipping_information.setOnClickListener(this);
        ll_me_thumbnail.setOnClickListener(this);
        ll_store_address.setOnClickListener(this);
        ll_management_standard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                if (isNetworkAvailable(this)) {
                    netService.updSendprice(GetstoreId(), price, updSendprice);//修改价格
                    if (tv_shop_status.getText().toString().equals("营业中")) {
                        setOperatingState("营业中");
                    } else {
                        setOperatingState("未营业");
                    }
                }
                finish();

                break;
            case R.id.btn_me_thumbnail://修改头像
                openContextMenu(circleImageView);
                break;
            case R.id.ll_me_thumbnail://修改头像
                openContextMenu(circleImageView);
                break;
            case R.id.tv_shop_status://修改营业状态
                netService.updShopstatus(GetstoreId(), bid, updShopstatus);
                break;
            case R.id.ll_shop_announcement://店铺公告
                HashMap<String, String> extras_announcement = new HashMap<>();
                extras_announcement.put("shop_type", "店铺公告");
                try {
                    gotoActivity(ShopAnnouncement.class.getName(), extras_announcement);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_shop_introduce://店铺简介
                HashMap<String, String> extras_introduce = new HashMap<>();
                extras_introduce.put("shop_type", "店铺简介");
                try {
                    gotoActivity(ShopAnnouncement.class.getName(), extras_introduce);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_shop_photo://店铺照片
                try {
                    gotoActivity(ShopPhoto.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_change_phone://修改电话
                Intent intent = new Intent(this, StoreChangePhone.class);
                intent.putExtra("storePhone", shop_phone);
                startActivity(intent);

                break;
            case R.id.ll_store_address://修改餐厅地址
                Intent Address = new Intent(this, ActSelectBusinessCardAddress.class);
                Address.putExtra("sheng", sheng);
                Address.putExtra("shi", shi);
                Address.putExtra("qu", qu);
                Address.putExtra("address", shop_address);
                Address.putExtra("dimension", dimension);
                Address.putExtra("longitude", longitude);
                Address.putExtra("code", code);
                startActivityForResult(Address, 10);
                break;

            case R.id.ll_management_standard://管理规范

                break;
            case R.id.ll_agreement://签署协议
                try {
                    gotoActivity(ShopAgreement.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_yingye_time://营业时间
                Intent StoreYingyeTime = new Intent(this, StoreYingyeTime.class);
                StoreYingyeTime.putExtra("YingyeWeek", business_week);
                StoreYingyeTime.putExtra("business_hours", business_hours);
                startActivityForResult(StoreYingyeTime, STOREYINGYETIME);
                break;
            case R.id.ll_yingye_qualifications://营业资质
                try {
                    gotoActivity(YingyeQualifications.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_shipping_information://配送信息
                try {
                    gotoActivity(ShippingInformation.class.getName(), null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            default:

                break;
        }
    }

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
            //图片的存储位置
            switch (item.getItemId()) {
                case 101:
                    takePictureManager = new TakePictureManager(this);
                    takePictureManager.setTailor(1, 1, 350, 350);
                    takePictureManager.startTakeWayByAlbum();
                    takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
                        @Override
                        public void successful(boolean isTailor, File outFile, Uri filePath) {
                            //显示头像
                            mUploadImageId = outFile.getPath();
                            netService.appSts(handler_appstr);
                            ImageLoader.getInstance().clearDiskCache();
                            ImageLoader.getInstance().clearMemoryCache();
                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), circleImageView, GSApplication.getInstance().optionshead);
                        }

                        @Override
                        public void failed(int errorCode, List<String> deniedPermissions) {

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
                            //显示头像
                            mUploadImageId = outFile.getPath();
                            netService.appSts(handler_appstr);
                            ImageLoader.getInstance().clearDiskCache();
                            ImageLoader.getInstance().clearMemoryCache();
                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), circleImageView, GSApplication.getInstance().optionshead);
                        }

                        //失败回调
                        @Override
                        public void failed(int errorCode, List<String> deniedPermissions) {
                            Log.e("==w", deniedPermissions.toString());
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

    //把本地的onActivityResult()方法回调绑定到对象
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 10) {
            //从地址返回
            super.onActivityResult(requestCode, resultCode, data);
            sheng = data.getStringExtra("sheng");
            shi = data.getStringExtra("shi");
            qu = data.getStringExtra("qu");
            dimension = data.getStringExtra("dimension");
            longitude = data.getStringExtra("longitude");
            code = data.getStringExtra("code");
            shop_address = data.getStringExtra("jiedao");
            tv_shop_address.setText(sheng + shi + qu + shop_address);

        }
        if (requestCode == 2) {
            super.onActivityResult(requestCode, resultCode, data);
            ACache aCache = ACache.get(this);
            business_hours = aCache.getAsString("business_hours");
            business_week = aCache.getAsString("YingyeWeek");
            if (Valid.isNotNullOrEmpty(business_hours)) {
                tv_business_hours.setText(business_hours);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            takePictureManager.attachToActivityForResult(requestCode, resultCode, data);
        }

    }

    //onRequestPermissionsResult()方法权限回调绑定到对象
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePictureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                        final String BuchetName = jsonObject.getString("Title");
                        Bucket = jsonObject.getString("Bucket");
//                        //  实例化
                        GSApplication.getInstance().initOss(AccessKeyId, AccessKeySecret, SecurityToken, EndPoint);
                        //拼接文件名
                        picName = GSApplication.pictures + "/" + year() + "/" + getIdentity() + ".jpg";
                        upfileOss(BuchetName, picName, PictureUtil.getBytes(PictureUtil.getSmallBitmap(mUploadImageId)));
//                        Oos oos = new Oos();
//                        String imgs = oos.img(StoreSettingNew.this, mUploadImageId, BuchetName, Bucket);
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
                url = "https://" + Bucket + "/" + picName;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        netService.upload_logo(GetstoreId(), url, upload_logo);
                    }
                });
                // 你上传的资源objectKey;
                Log.d("PutObject", "UploadSuccess" + result.toString() + "--" + result.getServerCallbackReturnBody());
                Log.d("ETag", url);
                Log.d("RequestId", result.getRequestId());
                return url;
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
        try {
            String s = String.valueOf(task.getResult());
            Log.i("ssss", s + "++++");
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //头像裁剪方法
    private void onCropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", mCropImageWidth);// 输出图片大小
        intent.putExtra("outputY", mCropImageWidth);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROPIMAGE_REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Valid.isNotNullOrEmpty(getsuerphone())) {
            tv_shop_phone.setText(getsuerphone());
            shop_phone = getsuerphone();
        }

    }

    @SuppressLint("HandlerLeak")
    Handler ShowShopdata = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        JSONObject object = new JSONObject(jsonObject.getString("status"));
                        String message = object.getString("message");
                        if (object.getString("code").equals("200")) {
                            String objectdate = new JSONObject(jsonObject.getString("data")).toString();
                            String objectdateeNew = objectdate.replaceAll("[\\[\\]]", "");//去掉中括号
                            if (Valid.isNotNullOrEmpty(objectdate)) {
                                JSONObject object1 = new JSONObject(objectdate);
                                bid = object1.getInt("bid");//商家id
                                String head_img = object1.getString("logo");//店铺头像
                                int shop_status = object1.getInt("shop_status");//店铺营业状态，0：未营业；2：营业
                                double send_price = object1.getDouble("send_price");//起送价
                                shop_phone = object1.getString("shop_phone");//店铺联系电话
                                business_week = object1.getString("business_week");//工作周期
                                business_hours = object1.getString("business_hours");//工作时间
                                //将店铺时间缓存本地
                                ACache aCache = ACache.get(StoreSettingNew.this);
                                aCache.put("business_hours", business_hours);
                                aCache.put("YingyeWeek", business_week);

                                int distribution_info = object1.getInt("distribution_info");//配送信息 0：自己配送 1：专送
                                sheng = object1.getString("sheng");//省
                                shi = object1.getString("shi");//市
                                qu = object1.getString("qu");//区
                                shop_address = object1.getString("shop_address");//店铺地址
                                longitude = object1.getString("longitude");//经度
                                dimension = object1.getString("dimension");//维度
                                code = object1.getString("code");
                                if (Valid.isNotNullOrEmpty(head_img)) {
                                    ImageLoader.getInstance().clearDiskCache();
                                    ImageLoader.getInstance().clearMemoryCache();
                                    ImageLoader.getInstance().displayImage(head_img, circleImageView, GSApplication.getInstance().optionshead);
                                }
                                if (shop_status == 0) {
                                    tv_shop_status.setText("未营业");
                                    setOperatingState("未营业");
                                } else {
                                    tv_shop_status.setText("营业中");
                                    setOperatingState("营业中");
                                }
                                et_send_price.setText(send_price + "");
                                tv_shop_phone.setText(shop_phone);
                                tv_shop_address.setText(sheng + shi + qu + shop_address);
                                tv_business_hours.setText(business_hours);
                            }
                        } else {
                            Toast.makeText(StoreSettingNew.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
    @SuppressLint("HandlerLeak")
    Handler updShopstatus = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String date = jsonObject.getString("data");
                        JSONObject status = new JSONObject(jsonObject.getString("status"));
                        String mesg = status.getString("message");
                        Toast.makeText(StoreSettingNew.this, mesg, Toast.LENGTH_SHORT).show();
                        if (date.equals("1")) {
                            editor.putString("OperatingState", "营业中");
                            editor.commit();
                            tv_shop_status.setText("营业中");
                        } else {
                            editor.putString("OperatingState", "未营业");
                            editor.commit();
                            tv_shop_status.setText("未营业");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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
    @SuppressLint("HandlerLeak")
    Handler updSendprice = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String date = jsonObject.getString("data");
                        JSONObject status = new JSONObject(jsonObject.getString("status"));
                        String mesg = status.getString("message");
//                        Toast.makeText(StoreSettingNew.this, mesg, Toast.LENGTH_SHORT).show();
//                        if (date.equals("1")) {
////                            tv_send_price.setText(price + "元");
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
    @SuppressLint("HandlerLeak")
    Handler upload_logo = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    setstoreIcon(url);
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
}
