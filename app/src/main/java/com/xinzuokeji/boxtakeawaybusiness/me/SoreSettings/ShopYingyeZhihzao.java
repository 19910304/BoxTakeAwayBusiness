package com.xinzuokeji.boxtakeawaybusiness.me.SoreSettings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.CheckBox;
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
import com.xinzuokeji.boxtakeawaybusiness.entities.License;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.Constants;
import com.xinzuokeji.boxtakeawaybusiness.util.PictureUtil;
import com.xinzuokeji.boxtakeawaybusiness.util.TakePictureManager;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.List;

//营业执照
public class ShopYingyeZhihzao extends BaseActivity {
    private ImageButton header_back;
    String cardZhihzao = "";
    String certificates_name = "", legal_person = "", register_number = "", certificates_place = "", validity_term = "", certificates_img = "";
    String validity_type = "0", card_type = "";
    ImageView img_card_time, img_certificates_img;
    TextView tv_validity_term, tv_certificates_img;
    Calendar c = Calendar.getInstance();
    Button bt_yingye_zhizhao;
    NetService netService;
    EditText et_store_name, et_legal_person, et_register_number, et_certificates_place;
    CheckBox ck_long_time;
    CheckBox ck_guding_time;
    ImageView certificatesImg;
    String Bucket;
    String picName;
    private String url_license;
    private Button bt_sure_save;
    private TakePictureManager takePictureManager;

    private String mUploadImageId = "";
    //    选择时间布局
    private LinearLayout ll_validity_type;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_shop_yingye_zhizhao);
        netService = new NetService(this);
        card_type = getIntent().getStringExtra("card_type");//证件类型
//        certificates_name = getIntent().getStringExtra("certificates_name");//营业执照
//        legal_person = getIntent().getStringExtra("legal_person");
//        register_number = getIntent().getStringExtra("register_number");
//        certificates_place = getIntent().getStringExtra("certificates_place");//证书地址
//        validity_type = getIntent().getStringExtra("validity_type");//有效期 0:短期 1:长期
//        validity_term = getIntent().getStringExtra("validity_term");//有效期时长
//        certificates_img = getIntent().getStringExtra("certificates_img");//证件照片
        tv_certificates_img = findViewById(R.id.tv_certificates_img);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        TextView tv_certificate_type = findViewById(R.id.tv_certificate_type);

        if (card_type.equals("1")) {
            textView.setText("营业执照");
//            tv_certificate_type.setText("营业执照审核通过");
        }
        if (card_type.equals("3")) {
            textView.setText("餐饮许可证");
//            tv_certificate_type.setText("餐饮许可证审核通过");
        }
        et_store_name = findViewById(R.id.et_store_name);
//        et_store_name.setText(certificates_name);
        et_store_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                certificates_name = et_store_name.getText().toString();
            }
        });
        et_legal_person = findViewById(R.id.et_legal_person);//法人
//        et_legal_person.setText(legal_person);
        et_legal_person.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                legal_person = et_legal_person.getText().toString();
            }
        });
        et_register_number = findViewById(R.id.et_register_number);//注册号
//        et_register_number.setText(register_number);
        et_register_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                register_number = et_register_number.getText().toString();
            }
        });
        et_certificates_place = findViewById(R.id.et_certificates_place);//证书地址
//        et_certificates_place.setText(certificates_place);
        et_certificates_place.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                certificates_place = et_certificates_place.getText().toString();
            }
        });
        ck_long_time = findViewById(R.id.ck_long_time);
        ck_guding_time = findViewById(R.id.ck_guding_time);

        ck_long_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_long_time.setChecked(true);
                ck_guding_time.setChecked(false);
                validity_type = "1";
                ll_validity_type.setVisibility(View.GONE);
            }
        });

        ck_guding_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ck_long_time.setChecked(false);
                ck_guding_time.setChecked(true);
                validity_type = "0";
                ll_validity_type.setVisibility(View.VISIBLE);
            }
        });
        //证件时长;
        tv_validity_term = findViewById(R.id.tv_validity_term);
        certificatesImg = findViewById(R.id.img_certificates_img);

        img_card_time = findViewById(R.id.img_card_time);
        //提交按钮
        bt_yingye_zhizhao = findViewById(R.id.bt_yingye_zhizhao);
        if (card_type.equals("1")) {
            netService.showLicense(GetstoreId(), handler_showLicense);
        }
        if (card_type.equals("3")) {
            netService.showLicence(GetstoreId(), handler_showLicense);
        }
    }


    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        img_card_time.setOnClickListener(this);
        bt_yingye_zhizhao.setOnClickListener(this);
        certificatesImg.setOnClickListener(this);
        registerForContextMenu(certificatesImg);//注册菜单
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("intentLicense", "完成");
            bundle.putSerializable("licence", "完成");
            data.putExtras(bundle);
            if (card_type.equals("1")) {
                setResult(Constants.BUSINESSLICENSE, data);
            }
            if (card_type.equals("3")) {
                setResult(Constants.LICENCE, data);
            }
            finish();
        }

    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.img_card_time:
                showDatePickerDialog(this, 3, tv_validity_term, c);
                break;
            case R.id.img_certificates_img://上传营业执照
                //弹出菜单
                openContextMenu(certificatesImg);
                break;
            case R.id.bt_yingye_zhizhao:
                // url_license 图片地址Valid.isNotNullOrEmpty(url_license) &&
                if (Valid.isNotNullOrEmpty(certificates_name) && Valid.isNotNullOrEmpty(legal_person) && Valid.isNotNullOrEmpty(register_number) && Valid.isNotNullOrEmpty(certificates_place)) {
                    if (card_type.equals("1")) {
                        netService.addLicense(GetstoreId(), certificates_name, legal_person, register_number, certificates_place, Integer.parseInt(validity_type), tv_validity_term.getText().toString(), handler_addLicense);
                    }
                    if (card_type.equals("3")) {
                        netService.addLicence(GetstoreId(), certificates_name, legal_person, register_number, certificates_place, Integer.parseInt(validity_type), tv_validity_term.getText().toString(), handler_addLicense);
                    }
                } else {
                    showTip("请填写完整信息", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler_addLicense = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (card_type.equals("1")) {
                        handler.sendEmptyMessage(Constants.BUSINESSLICENSE);
                    }
                    if (card_type.equals("3")) {
                        handler.sendEmptyMessage(Constants.LICENCE);
                    }
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
        }
    };
    //    @SuppressLint("HandlerLeak")
//    Handler handler_showLicence = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 2000:
//                    if (msg.obj != null) {
//                        com.xinzuokeji.boxtakeawaybusiness.entities.License license = (License) msg.obj;
//                        //证件名称
//                        et_certificates_name.setText(license.certificates_name);
//                        //法人代表姓名
//                        et_legal_person.setText(license.legal_person);
//                        //证件注册号
//                        et_register_number.setText(license.register_number);
//                        //证件所在地
//                        et_certificates_place.setText(license.certificates_place);
//                        // 证件有效类型 1:长期有效 0:短期有效
//
//                        //有效期限
//                        validity_term.setText(license.validity_term);
//                        //证件图片
//                        if (Valid.isNotNullOrEmpty(license.certificates_img)) {
//                            url_license = license.certificates_img;
//                            tv_sus_licence.setVisibility(View.GONE);
//                            ImageLoader.getInstance().displayImage(license.certificates_img, iv_sus_licence, GSApplication.getInstance().imageOptions);
//                        }
//                    }
//                    break;
//                case 2001:
//
//                    break;
//                case 1001:
////                    showTip(getString(R.string.network_error), Toast.LENGTH_SHORT);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
    @SuppressLint("HandlerLeak")
    Handler handler_showLicense = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        com.xinzuokeji.boxtakeawaybusiness.entities.License license = (License) msg.obj;
                        //证件名称
                        et_store_name.setText(license.certificates_name);
                        //法人代表姓名
                        et_legal_person.setText(license.legal_person);
                        //证件注册号
                        et_register_number.setText(license.register_number);
                        //证件所在地
                        et_certificates_place.setText(license.certificates_place);
                        // 证件有效类型 1:长期有效 0:短期有效
                        if (license.validity_type.equals("1")) {
                            ck_long_time.setChecked(true);
                            validity_term = "1";
                            ll_validity_type.setVisibility(View.GONE);
                        } else {
                            validity_term = "0";
                            ck_guding_time.setChecked(true);
                            ll_validity_type.setVisibility(View.VISIBLE);
                        }
                        validity_type = license.validity_type;
                        //有效期限
                        tv_validity_term.setText(license.validity_term);
                        //证件图片
                        if (Valid.isNotNullOrEmpty(license.certificates_img)) {
                            url_license = license.certificates_img;
                            tv_certificates_img.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(license.certificates_img, certificatesImg, GSApplication.getInstance().imageOptions);
                        }
                    }
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(getResources().getString(R.string.action_picture_select));
//        menu.setHeaderIcon(R.mipmap.me_setting_p);
        menu.add(0, 101, 1, R.string.action_pick);
        menu.add(0, 102, 2, R.string.action_capture);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
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
                            tv_certificates_img.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), certificatesImg, GSApplication.getInstance().imageOptions);
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
                            tv_certificates_img.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), certificatesImg, GSApplication.getInstance().imageOptions);
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
            Toast.makeText(getApplicationContext(), R.string.not_find_phone_sd, Toast.LENGTH_LONG).show();
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
        takePictureManager.attachToActivityForResult(requestCode, resultCode, data);
    }

    @SuppressLint("HandlerLeak")
    Handler handler_UploadFile = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
        }
    };
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
                url_license = "https://" + Bucket + "/" + picName;
                // 上传到服务器
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (card_type.equals("1")) {
                            netService.upload_license(GetstoreId(), url_license, "1", handler_UploadFile);
                        }
                        if (card_type.equals("3")) {
                            netService.upload_license(GetstoreId(), url_license, "3", handler_UploadFile);
                        }
                    }
                });

//                你上传的资源objectKey;
                Log.d("PutObject", "UploadSuccess" + result.toString() + "--" + result.getServerCallbackReturnBody());
                Log.d("ETag", url_license);
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
