package com.xinzuokeji.boxtakeawaybusiness.me;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.xinzuokeji.boxtakeawaybusiness.util.PictureUtil;
import com.xinzuokeji.boxtakeawaybusiness.util.TakePictureManager;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

//意见反馈
public class Feedback extends BaseActivity {
    private ImageButton header_back;
    NetService netService;
    EditText et_content, et_feed_phone;
    Button bt_feed_commit;
    ImageView bt_add_picture;
    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 1000;
    private static final int CAMERA_REQUEST_CODE = 1001;
    private static final int CROPIMAGE_REQUEST_CODE = 1002;
    private static final int mCropImageWidth = 192;

    private static final String IMAGE_FILE_NAME = "feedImage.jpg";
    private File mFileCacheDirectory;
    private Uri mImageUri;
    private String  mUploadImageId = "";
    String url = "";
    String Bucket;
    String picName;
    private TakePictureManager takePictureManager;
    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_feedback);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("意见反馈");
        netService = new NetService(this);
        et_content = findViewById(R.id.et_content);
        bt_add_picture = findViewById(R.id.bt_add_picture);
        et_feed_phone = findViewById(R.id.et_feed_phone);
        bt_feed_commit = findViewById(R.id.bt_feed_commit);
        //意见内容
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_feed_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_add_picture.setOnClickListener(this);
        bt_feed_commit.setOnClickListener(this);
        registerForContextMenu(bt_add_picture);//注册菜单
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.bt_add_picture:
                openContextMenu(bt_add_picture);
                break;
            case R.id.bt_feed_commit://提交意见
                if (Valid.isNotNullOrEmpty(et_content.getText().toString().trim()) && Valid.isPhone(et_feed_phone.getText().toString().trim())) {
                    netService.businessFeedback(String.valueOf(getuserinfoId()), et_feed_phone.getText().toString().trim(), et_content.getText().toString().trim(), url, businessFeedback);
//                    netService.UploadFile(String.valueOf(getuserinfoId()), et_feed_phone.getText().toString().trim(), et_content.getText().toString().trim(), mUploadImageId, businessFeedback);
                } else {
                    showTip("意见和手机号不能为空！", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler businessFeedback = new Handler() {
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
//            mFileCacheDirectory = new File(
//                    Environment.getExternalStorageDirectory(),
//                    getPackageName());
//
//            // 判断当前目录是否存在
//            if (!mFileCacheDirectory.exists()) {
//                mFileCacheDirectory.mkdir();
//            }
//
//            //图片的存储位置
//            mImageUri = Uri.fromFile(new File(mFileCacheDirectory.getPath(), IMAGE_FILE_NAME));

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

                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), bt_add_picture, GSApplication.getInstance().imageOptions);
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

                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), bt_add_picture, GSApplication.getInstance().imageOptions);
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
//                        final Oos oos = new Oos();
//                        oos.initOss(Feedback.this, AccessKeyId, AccessKeySecret, SecurityToken, EndPoint);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                oos.img(Feedback.this, mUploadImageId, BuchetName, Bucket);
//                                Log.i("path", oos.pathimg + "++");
//                            }
//                        });


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
//                你上传的资源objectKey;
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
    }
}
