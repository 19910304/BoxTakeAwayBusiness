package com.xinzuokeji.boxtakeawaybusiness.storeOperations.commodityManage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.xinzuokeji.boxtakeawaybusiness.entities.GoodsDetail;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.PictureUtil;
import com.xinzuokeji.boxtakeawaybusiness.util.TakePictureManager;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

//商品管理编辑
public class CMCompile extends BaseActivity {
    private ImageButton header_back;
    LinearLayout ll_change_guige, ll_xuanze_caidan, ll_good_img, ll_change_taste;
    NetService netService;
    ImageView img_good_icon;
    EditText et_good_name, et_good_price, et_biaoqian1, et_biaoqian2, et_biaoqian3, et_lunch_box_num, et_lunch_box_price, et_describe, et_stock;
    TextView startTime, endTime, tv_caidan_menus;
    //    初始化calender
    Calendar calendar = Calendar.getInstance();
    TextView save_good;
    String goodId;
    String menuid;
    private String guigiId = "";
    // 商品价格和餐盒价格
    private double goodPrice, lunch_box_price;
    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 1000;
    private static final int CAMERA_REQUEST_CODE = 1001;
    private static final int CROPIMAGE_REQUEST_CODE = 1002;
    private static final int mCropImageWidth = 192;


    private String mUploadImageId = "";
    String url;
    String Bucket;
    String picName;
    private TakePictureManager takePictureManager;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_cmcompile);
        takePictureManager = new TakePictureManager(this);
        netService = new NetService(this);
        // 商品id
        goodId = getIntent().getStringExtra("good_id");
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("编辑");
        // 保存商品
        save_good = findViewById(R.id.header_edit1);
        save_good.setVisibility(View.VISIBLE);
        ll_change_guige = findViewById(R.id.ll_change_guige);
        ll_xuanze_caidan = findViewById(R.id.ll_xuanze_caidan);
        ll_good_img = findViewById(R.id.ll_good_img);
        tv_caidan_menus = findViewById(R.id.tv_caidan_menus);
//        商品图片
        img_good_icon = findViewById(R.id.img_good_icon);
        //商品名称
        et_good_name = findViewById(R.id.et_good_name);
        et_good_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_good_name.getText().toString().trim();
            }
        });
        //商品价格
        et_good_price = findViewById(R.id.et_good_price);
        et_good_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 判断是否为空，否则转换会报空指针
                if (Valid.isNotNullOrEmpty(et_good_price.getText().toString().trim())) {
                    goodPrice = Double.parseDouble(et_good_price.getText().toString().trim());
                }

            }
        });
        //开始结束时间
        startTime = findViewById(R.id.tv_star_time);
        endTime = findViewById(R.id.tv_end_time);
        //标签一
        et_biaoqian1 = findViewById(R.id.et_biaoqian1);
        et_biaoqian1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_biaoqian1.getText().toString().trim();
            }
        });
        //标签二
        et_biaoqian2 = findViewById(R.id.et_biaoqian2);
        et_biaoqian2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_biaoqian2.getText().toString().trim();
            }
        });
        //标签三
        et_biaoqian3 = findViewById(R.id.et_biaoqian3);
        et_biaoqian3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_biaoqian3.getText().toString().trim();
            }
        });
        //餐盒数
        et_lunch_box_num = findViewById(R.id.et_lunch_box_num);
        et_lunch_box_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_lunch_box_num.getText().toString().trim();
            }
        });
        //餐盒价格
        et_lunch_box_price = findViewById(R.id.et_lunch_box_price);
        et_lunch_box_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 判断是否为空，否则转换会报空指针
                if (Valid.isNotNullOrEmpty(et_lunch_box_price.getText().toString().trim())) {
                    lunch_box_price = Double.parseDouble(et_lunch_box_price.getText().toString().trim());
                }
            }
        });
        //商品描述
        et_describe = findViewById(R.id.et_describe);
        et_describe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_describe.getText().toString().trim();
            }
        });
        //库存
        et_stock = findViewById(R.id.et_stock);
        et_stock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_stock.getText().toString().trim();
            }
        });
        netService.GoodsDetail(Integer.parseInt(goodId), showGoodsDetail);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        ll_change_guige.setOnClickListener(this);
        ll_xuanze_caidan.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        save_good.setOnClickListener(this);
        img_good_icon.setOnClickListener(this);
        ll_good_img.setOnClickListener(this);
        registerForContextMenu(img_good_icon);//注册菜单
        ll_change_taste.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.img_good_icon:
                openContextMenu(img_good_icon);
                break;
            case R.id.ll_good_img:
                openContextMenu(img_good_icon);
                break;
            case R.id.header_edit1://保存商品信息
                if (startTime.getText().toString().equals("开始")) {
                    startTime.setText("00:00");
                }
                if (endTime.getText().toString().equals("结束")) {
                    endTime.setText("23:59");
                }
                long startime = getStringToDate(startTime.getText().toString().trim(), "HH:mm");
                long endtime = getStringToDate(endTime.getText().toString().trim(), "HH:mm");
                if (Valid.isNotNullOrEmpty(url) && Valid.isNotNullOrEmpty(et_good_name.getText().toString().trim()) && Valid.isNotNullOrEmpty(et_good_price.getText().toString().trim()) && Valid.isNotNullOrEmpty(startTime.getText().toString().trim()) && Valid.isNotNullOrEmpty(endTime.getText().toString().trim())
                        && Valid.isNotNullOrEmpty(menuid) && Valid.isNotNullOrEmpty(et_lunch_box_price.getText().toString().trim()) && Valid.isNotNullOrEmpty(et_stock.getText().toString().trim())) {
                    if (startime < endtime) {
                        netService.addGoods(Integer.parseInt(goodId), GetstoreId(), et_good_name.getText().toString().trim(), goodPrice, startTime.getText().toString().trim(), endTime.getText().toString().trim(), Integer.parseInt(menuid), et_biaoqian1.getText().toString().trim(), et_biaoqian2.getText().toString().trim(),
                                et_biaoqian3.getText().toString().trim(), lunch_box_price, et_describe.getText().toString().trim(), Integer.parseInt(et_stock.getText().toString().trim()), guigiId, addGoods);
                    } else {
                        showTip("营业开始时间不得晚于营业结束时间", Toast.LENGTH_SHORT);
                    }
                } else {
                    showTip("请填写完整的商品信息", Toast.LENGTH_SHORT);
                }

//                Log.i("price", goodPrice + "---" + url + "---" + et_good_name.getText().toString().trim() + "---" + et_good_price.getText().toString() + "---" + startTime.getText().toString() + "---" + endTime.getText().toString() + "---" + menuid + "---" + et_lunch_box_price.getText().toString() + "---" + et_stock.getText().toString());

                break;
            case R.id.tv_star_time:
                showTimePickerDialog(this, 3, startTime, calendar);
                break;
            case R.id.tv_end_time:
                showTimePickerDialog(this, 3, endTime, calendar);
                break;
            case R.id.ll_change_guige://修改规格

                Intent intent = new Intent(this, GuigeList.class);
                intent.putExtra("good_id", goodId);
                intent.putExtra("list_type", "GuigeList");
                startActivityForResult(intent, 0);

                break;
            case R.id.ll_xuanze_caidan://选择菜单
                Intent intent_choice_menus = new Intent(this, ChoiceMenus.class);
//                intent.putExtra("good_id", goodId);
                startActivityForResult(intent_choice_menus, 1);

                break;
            //  增加口味
            case R.id.ll_change_taste:
                // 如果-1则为口味列表
                Intent intent_taste = new Intent(this, GuigeList.class);
                intent_taste.putExtra("good_id", goodId);
                intent_taste.putExtra("list_type", "tasteList");
                startActivityForResult(intent_taste, 2);
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
                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), img_good_icon, GSApplication.getInstance().imageOptions);
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
                            //显示头像
                            mUploadImageId = outFile.getPath();
                            netService.appSts(handler_appstr);
                            ImageLoader.getInstance().clearDiskCache();
                            ImageLoader.getInstance().clearMemoryCache();
                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), img_good_icon, GSApplication.getInstance().imageOptions);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 请求码是0得到规格id;
        if (requestCode == 0) {
            if (data != null) {
                guigiId = data.getExtras().getString("guige_id");
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
        // 选择菜单返回
        if (requestCode == 1) {
            if (data != null) {
                String menu_name = data.getExtras().getString("menu_name");
                String menu_id = data.getExtras().getString("menu_id");
                tv_caidan_menus.setText(menu_name);
                menuid = menu_id;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (RESULT_OK == resultCode) {
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
                url = "https://" + Bucket + "/" + picName;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //   上传到服务器
                        netService.addGoodsimg(Integer.parseInt(goodId), GetstoreId(), url, handler_UploadFile);
                    }
                });
//                你上传的资源objectKey;
                Log.d("PutObject", "UploadSuccess" + result.toString() + "--" + result.getServerCallbackReturnBody());
                Log.d("ETag", url);
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

    @SuppressLint("HandlerLeak")
    Handler addGoods = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    }
                    finish();
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
    Handler showGoodsDetail = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        GoodsDetail goodsDetail = (GoodsDetail) msg.obj;
                        ImageLoader.getInstance().displayImage(goodsDetail.goods_img, img_good_icon, GSApplication.getInstance().imageOptions);
                        //菜单图片
                        url = goodsDetail.goods_img;
                        // 菜单id
                        menuid = goodsDetail.menu_id;
                        et_biaoqian1.setText(goodsDetail.label_one);
                        et_biaoqian2.setText(goodsDetail.label_two);
                        et_biaoqian3.setText(goodsDetail.label_three);
                        et_describe.setText(goodsDetail.describe);
                        et_good_name.setText(goodsDetail.goods_name);
                        et_good_price.setText(goodsDetail.goods_price);
                        goodPrice = Double.parseDouble(goodsDetail.goods_price);
                        startTime.setText(goodsDetail.begin_time);
                        endTime.setText(goodsDetail.end_time);
//                        et_lunch_box_num.setText(goodsDetail.lunch_box_num);
                        et_lunch_box_price.setText(goodsDetail.lunch_box_price);
                        lunch_box_price = Double.parseDouble(goodsDetail.lunch_box_price);
                        et_stock.setText(goodsDetail.stock);
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
    @SuppressLint("HandlerLeak")
    Handler handler_UploadFile = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    Log.i("img", msg.obj.toString() + "--");
                    break;
                case 2001:

                    break;
                case 1001:
//                    showTip(getString(R.string.network_error), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
        }
    };

    // 上传文件 contentType 需要文件的内容类型
    public void UploadFileGood(String id, String shopid, String filename, String url, final Handler handler) {
        try {
            String serverFileName = new HttpPostFileAsyncTask().execute(id, shopid, filename, url).get();
            Message message = new Message();
            message.what = DATA_SUCCESS;
            message.obj = serverFileName;
            handler.sendMessage(message);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    private static final String BOUNDARY = "";

    //上传文件
    private String PostFileToStream(String id, String shopid, String filename, String url) {
        final String end = "\r\n";
//        FILEUPLOADURL = "http://waimai.023wx.cn/shop/Shopimg/upload_Identity?shopid=%s&legal_person=%s&register_number=%s";
        String FILEUPLOADURL = String.format(url, id, shopid);
        Log.i("FILEUPLOADURL", FILEUPLOADURL);
        HttpURLConnection connection = null;
        try {
            String cookie;
            // 清空服务器返回的内容
            String response = "";

            // 请求服务器数据
            connection = (HttpURLConnection) new URL(FILEUPLOADURL)
                    .openConnection();
            // 请求方式
            connection.setRequestMethod("POST");
            // 超时时间
            connection.setReadTimeout(3 * 60 * 1000);
            connection.setConnectTimeout(3 * 60 * 1000);
            // POST请求必须设置允许输入、输出,不允许缓存
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            //开始上传文件
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            //文件流
            File file = new File(filename);
            outputStream.writeBytes("--" + BOUNDARY + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "img"
                    + "\"; filename=\"" + URLEncoder.encode(file.getName()) + "\"\r\n");

            outputStream.writeBytes("Content-Type:image/jpeg\r\n");
            outputStream.writeBytes("\r\n");
            FileInputStream in = new FileInputStream(file);
            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                outputStream.write(b, 0, n);
            }
            in.close();
            outputStream.writeBytes("\r\n");
            //--------------------
            outputStream.writeBytes("--" + BOUNDARY + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"contentType\"\r\n");
            outputStream.writeBytes("\r\n");
            outputStream.writeBytes("image/jpeg\r\n");
            //文件結束
            outputStream.writeBytes("--" + BOUNDARY + "--" + "\r\n");
            outputStream.writeBytes("\r\n");
            outputStream.flush();
            outputStream.close();

            // 实际上开发发送数据是从下边的getInputStream开始的
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "utf-8");
                // 缓冲??
                BufferedReader br = new BufferedReader(isr);
                // 读取的行数据
                String line = "";
                // 循环读取所有服务器返回
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                isr.close();
            } else {
                Log.e("Services Status",
                        FILEUPLOADURL + "(Code:" + connection.getResponseCode() + ")");
            }

            // 得到服务器返回的结果
            return response;

        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    class HttpPostFileAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("onPreExecute", "onPreExecute");
        }

        @Override
        protected String doInBackground(String... params) {
            // 提交数据到服务器并得到返回值
            String response = PostFileToStream(params[0], params[1], params[2], params[3]);
            Log.i("Services Status", "服务器返回-->" + response);
            String fileName = "";
//            try {
//                fileName = new JSONObject(new JSONObject(response).getString("Obj")).getString("FileName");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            return response;
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.i("onProgressUpdate", "onProgressUpdate");
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("onPostExecute", "onPostExecute");
        }


    }
}
