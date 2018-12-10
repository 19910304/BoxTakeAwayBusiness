package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.List;

//许可证
public class SUSThreeLicence extends BaseActivity {
    private ImageButton header_back;
    NetService netService;
    //证件名称 法人代表姓名 证件注册号 证件所在地
    private EditText et_certificates_name, et_legal_person, et_register_number, et_certificates_place;
    //证件名称 法人代表姓名 证件注册号 证件所在地
    String certificates_name, legal_person, register_number, certificates_place;
    private TextView validity_term;
    Calendar c = Calendar.getInstance();
    ImageView iv_sus_licence;
    TextView tv_sus_licence;
    String Bucket;
    String picName;
    private String mUploadImageId = "";
    private String url_license = "";
    private Button bt_sure_save;
    private TakePictureManager takePictureManager;
    //许可证时间类型
    private int time_type;
    // 许可证有效期
    private CheckBox ck_long_time, ck_fixation_time;
    //    选择时间
    LinearLayout ll_certificates_time;
    private LinearLayout ll_time_type;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_sus_three_licence);
        header_back = findViewById(R.id.header_back);
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back.setVisibility(View.VISIBLE);
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("上传许可证");
        textView.setTextColor(getResources().getColor(R.color.white));
        netService = new NetService(this);
        bt_sure_save = findViewById(R.id.bt_sure_save);
        //证件名称 法人代表姓名 证件注册号 证件所在地
        et_certificates_name = findViewById(R.id.et_certificates_name);
        et_legal_person = findViewById(R.id.et_legal_person);
        et_register_number = findViewById(R.id.et_register_number);
        et_certificates_place = findViewById(R.id.et_certificates_place);
        //许可证时间类型
        ck_long_time = findViewById(R.id.ck_long_time);
        ck_fixation_time = findViewById(R.id.ck_fixation_time);
        ck_long_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time_type = 1;
                ck_long_time.setChecked(true);
                ck_fixation_time.setChecked(false);
                //长期隐藏掉选择时间
                ll_time_type.setVisibility(View.GONE);
            }
        });
        ck_fixation_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time_type = 0;
                ck_long_time.setChecked(false);
                ck_fixation_time.setChecked(true);
                ll_time_type.setVisibility(View.VISIBLE);
            }
        });
        //点击选择时间
        ll_certificates_time = findViewById(R.id.ll_certificates_time);
        et_certificates_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                certificates_name = et_certificates_name.getText().toString().trim();
            }
        });
        et_legal_person.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                legal_person = et_legal_person.getText().toString().trim();
            }
        });
        et_register_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                register_number = et_register_number.getText().toString().trim();
            }
        });
        et_certificates_place.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                certificates_place = et_certificates_place.getText().toString().trim();
            }
        });
        iv_sus_licence = findViewById(R.id.iv_sus_licence);
        tv_sus_licence = findViewById(R.id.tv_sus_licence);
        validity_term = findViewById(R.id.tv_validity_term);
        netService.showLicence(getChangeStoreId(), handler_showLicence);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        validity_term.setOnClickListener(this);
        iv_sus_licence.setOnClickListener(this);
        bt_sure_save.setOnClickListener(this);
        ll_certificates_time.setOnClickListener(this);
        registerForContextMenu(iv_sus_licence);//注册菜单
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:

                finish();
                break;
            case R.id.iv_sus_licence:
                //弹出菜单
                openContextMenu(iv_sus_licence);
                break;
            case R.id.tv_validity_term:
//                showDatePickerDialog(this, 3, validity_term, c);
                break;
            case R.id.ll_certificates_time:
                showDatePickerDialog(this, 3, validity_term, c);
                break;
            case R.id.bt_sure_save:
                if (Valid.isNotNullOrEmpty(url_license) && Valid.isNotNullOrEmpty(certificates_name) && Valid.isNotNullOrEmpty(legal_person) && Valid.isNotNullOrEmpty(register_number) && Valid.isNotNullOrEmpty(certificates_place)) {
                    netService.addLicence(getChangeStoreId(), certificates_name, legal_person, register_number, certificates_place, time_type, validity_term.getText().toString(), handler_addLicence);
                } else {
                    showTip("请填写完整信息", Toast.LENGTH_SHORT);
                }
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
            bundle.putSerializable("licence", "完成");
            data.putExtras(bundle);
            setResult(Constants.LICENCE, data);
            finish();
        }

    };
    @SuppressLint("HandlerLeak")
    Handler handler_addLicence = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    handler.sendEmptyMessage(Constants.LICENCE);
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);

                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
//                    showTip(getString(R.string.network_error), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler_showLicence = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        com.xinzuokeji.boxtakeawaybusiness.entities.License license = (License) msg.obj;
                        //证件名称
                        et_certificates_name.setText(license.certificates_name);
                        //法人代表姓名
                        et_legal_person.setText(license.legal_person);
                        //证件注册号
                        et_register_number.setText(license.register_number);
                        //证件所在地
                        et_certificates_place.setText(license.certificates_place);
                        // 证件有效类型 1:长期有效 0:短期有效
                        if (license.validity_type.equals("1")) {
                            ck_long_time.setChecked(true);
                            time_type = 1;
                            ll_time_type.setVisibility(View.GONE);
                        } else {
                            ck_fixation_time.setChecked(true);
                            time_type = 0;
                            ll_time_type.setVisibility(View.VISIBLE);
                        }
                        //有效期限
                        validity_term.setText(license.validity_term);
                        //证件图片
                        if (Valid.isNotNullOrEmpty(license.certificates_img)) {
                            url_license = license.certificates_img;
                            tv_sus_licence.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(license.certificates_img, iv_sus_licence, GSApplication.getInstance().imageOptions);
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
//            mFileCacheDirectory = new File(Environment.getExternalStorageDirectory(), getPackageName());
//
//            // 判断当前目录是否存在
//            if (!mFileCacheDirectory.exists()) {
//                mFileCacheDirectory.mkdir();
//            }
//
//            //图片的存储位置
//            mImageUri = Uri.fromFile(new File(mFileCacheDirectory.getPath(), IMAGE_FILE_NAME));
//            mTemplateImageUri = Uri.fromFile(new File(mFileCacheDirectory.getPath(), IMAGE_TEMP_FILE_NAME));

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
                            tv_sus_licence.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), iv_sus_licence, GSApplication.getInstance().imageOptions);
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
                            tv_sus_licence.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), iv_sus_licence, GSApplication.getInstance().imageOptions);
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
        if (RESULT_OK == resultCode) {
            super.onActivityResult(requestCode, resultCode, data);
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
                url_license = "https://" + Bucket + "/" + picName;
                // 上传到服务器
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        netService.upload_license(getChangeStoreId(), url_license, "3", handler_UploadFile);
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
//                    showTip(getString(R.string.network_error), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 解决小米手机上获取图片路径为null的情况
     *
     * @param intent
     * @return
     */
    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    //根据分辨率压缩图片
    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1280f;//这里设置高度为800f
        float ww = 800f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    //图片质量压缩
    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 300) {  //循环判断如果压缩后图片是否大于300kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}
