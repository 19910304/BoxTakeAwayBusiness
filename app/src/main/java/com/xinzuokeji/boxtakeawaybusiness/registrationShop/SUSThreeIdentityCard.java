package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import java.text.ParseException;
import java.util.List;

//上传身份证
public class SUSThreeIdentityCard extends BaseActivity {
    private ImageButton header_back;
    private TextView header_edit1;
    EditText et_name, et_id_card_number;
    String name = "", id_card_number = "";
    private LinearLayout ll_id_card1, ll_id_card2, mllIdCard;

    private String mImageIds = "", mUploadImageIdcard1 = "", mUploadImageIdcard2 = "";
    //    private static final int IMAGE_REQUEST_CODE = 1000;
//    private static final int CAMERA_REQUEST_CODE = 1001;
//    private static final String IMAGE_FILE_NAME_positive = "Identity_card1.jpg";
//    private static final String IMAGE_FILE_NAME_opposite = "Identity_card2.jpg";
//    private static final String IMAGE_TEMP_FILE_NAME = "RCITempIdentity_card.jpg";
    private ImageView igb_card1, igb_card2, mImageCard;
    String from_name = "", legal_person = "", register_number = "", mUploadImageId = "", card_type = "";
    NetService netService;
    private ProgressDialog dialog;
    String Bucket;
    String picName;
    //获取图片的路径
    String cardimg1 = "", cardimg2 = "";
    private Button bt_sure_save;
    private TakePictureManager takePictureManager;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_sus_three_identity_card);
        netService = new NetService(this);
        from_name = getIntent().getStringExtra("from_name");//证件类型

        //头信息----
        header_back = findViewById(R.id.header_back);
        header_edit1 = findViewById(R.id.header_edit1);//保存
        //  header_edit1.setVisibility(View.VISIBLE);
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back.setVisibility(View.VISIBLE);
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("法人身份证照");
        textView.setTextColor(getResources().getColor(R.color.white));
        bt_sure_save = findViewById(R.id.bt_sure_save);
        // ------
        et_name = findViewById(R.id.et_name);
//        et_name.setText(legal_person);
        et_id_card_number = findViewById(R.id.et_id_card_number);
//        et_id_card_number.setText(register_number);
        ll_id_card1 = findViewById(R.id.ll_id_card1);
        ll_id_card2 = findViewById(R.id.ll_id_card2);
        igb_card1 = findViewById(R.id.igb_card1);
        igb_card2 = findViewById(R.id.igb_card2);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = et_name.getText().toString().trim();
            }
        });
        et_id_card_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                id_card_number = et_id_card_number.getText().toString().trim();
            }
        });
        if (from_name.equals("注册")) {
            netService.showIdentity(getChangeStoreId(), handler_showIdentity);
        } else {
            netService.showIdentity(GetstoreId(), handler_showIdentity);
        }

    }

    @Override
    public void initEvent() {
        super.initEvent();
        igb_card1.setOnClickListener(this);
        igb_card2.setOnClickListener(this);
        registerForContextMenu(igb_card1);//

        registerForContextMenu(igb_card2);//

        header_back.setOnClickListener(this);
        header_edit1.setOnClickListener(this);
        bt_sure_save.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.header_edit1://保存身份信息
                //没用
                break;

            case R.id.igb_card1://正面
                //弹出菜单
                openContextMenu(igb_card1);
                mImageCard = igb_card1;
                mllIdCard = ll_id_card1;
                card_type = "1";
                break;
            case R.id.igb_card2://反面
                openContextMenu(igb_card2);
                mImageCard = igb_card2;
                mllIdCard = ll_id_card2;
                card_type = "2";
                break;
            case R.id.bt_sure_save://保存身份信息
                if (Valid.isNotNullOrEmpty(name) && Valid.isNotNullOrEmpty(id_card_number) && Valid.isNotNullOrEmpty(mUploadImageIdcard1) && Valid.isNotNullOrEmpty(mUploadImageIdcard2)) {
                    try {
                        if (Valid.IDCardValidate(id_card_number)) {
                            dialog = new ProgressDialog(SUSThreeIdentityCard.this);
                            dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
                            dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                            // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
                            dialog.setTitle("正在上传...");
                            dialog.show();
                            if (from_name.equals("注册")) {
                                netService.upload_Identity(getChangeStoreId(), mUploadImageIdcard1, mUploadImageIdcard2, name, id_card_number, handler_addlegal);
                            } else {
                                netService.upload_Identity(GetstoreId(), mUploadImageIdcard1, mUploadImageIdcard2, name, id_card_number, handler_addlegal);
                            }
                        } else {
                            showTip("请正确填写身份信息", Toast.LENGTH_SHORT);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
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
                if (card_type.equals("1")) {
                    mUploadImageIdcard1 = "https://" + Bucket + "/" + picName;
                }
                if (card_type.equals("2")) {
                    mUploadImageIdcard2 = "https://" + Bucket + "/" + picName;
                }
//                你上传的资源objectKey;
                Log.d("PutObject", "UploadSuccess" + result.toString() + "--" + result.getServerCallbackReturnBody());
                Log.d("ETag", mUploadImageIdcard1 + "---" + mUploadImageIdcard2);
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
    Handler handler_showIdentity = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        com.xinzuokeji.boxtakeawaybusiness.entities.License license = (License) msg.obj;
                        //证件名称
//                        et_certificates_name.setText(license.certificates_name);
                        //法人代表姓名
                        et_name.setText(license.legal_person);
                        //证件注册号
                        et_id_card_number.setText(license.register_number);
                        //证件所在地
//                        et_certificates_place.setText(license.certificates_place);
                        // 证件有效类型 1:长期有效 0:短期有效

                        //有效期限
//                        validity_term.setText(license.validity_term);
                        //证件图片
                        if (Valid.isNotNullOrEmpty(license.certificates_img)) {
                            // 正面
                            cardimg1 = license.certificates_img.substring(0, license.certificates_img.indexOf(","));
                            ll_id_card1.setVisibility(View.GONE);
                            mUploadImageIdcard1 = cardimg1;
                            ImageLoader imageLoader = ImageLoader.getInstance();
                            imageLoader.displayImage(cardimg1, igb_card1, GSApplication.getInstance().imageOptions);
                            // 反面
                            cardimg2 = license.certificates_img.substring(license.certificates_img.indexOf(",")).substring(1);
                            mUploadImageIdcard2 = cardimg2;
                            ll_id_card2.setVisibility(View.GONE);
                            imageLoader.displayImage(cardimg2, igb_card2, GSApplication.getInstance().imageOptions);

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

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("identitycard", "完成");
            data.putExtras(bundle);
            setResult(Constants.IDENTITYCARD, data);
            finish();
        }

    };
    @SuppressLint("HandlerLeak")
    Handler handler_addlegal = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    handler.sendEmptyMessage(Constants.IDENTITYCARD);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
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

            //图片的存储位置
//            mImageUri = Uri.fromFile(new File(mFileCacheDirectory.getPath(), IMAGE_FILE_NAME_positive));
//            if (mImageCard == igb_card1) {
//                mImageUri = Uri.fromFile(new File(mFileCacheDirectory.getPath(), IMAGE_FILE_NAME_positive));
//            }
//            if (mImageCard == igb_card2) {
//                mImageUri = Uri.fromFile(new File(mFileCacheDirectory.getPath(), IMAGE_FILE_NAME_opposite));
//            }
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
                            mllIdCard.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), mImageCard, GSApplication.getInstance().imageOptions);
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
                            mllIdCard.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(String.valueOf(filePath), mImageCard, GSApplication.getInstance().imageOptions);
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
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
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
//            //图片处理，另存为数据较小的JPEG文件，然后上传
//            String imageAbsolutePath = "";
//            if (requestCode == IMAGE_REQUEST_CODE) {
//                Uri selectedImage = data.getData();
//                selectedImage = geturi(data);
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imageAbsolutePath = cursor.getString(columnIndex);
//                cursor.close();
//            } else if (requestCode == CAMERA_REQUEST_CODE) {
////                progressDialog = ProgressDialog.show(this, "", "正在上传图片...", false, true);
//                imageAbsolutePath = mTemplateImageUri.getPath();
//            }
//            Bitmap image = getimage(imageAbsolutePath);
//            if (image != null) {
//                //上传服务器
//                // 获取本地文件
//                mImageIds = mImageUri.getPath();
////                if (TextUtils.isEmpty(mImageIds)) {
////
////                } else {
////                    mImageIds += "," + mImageUri.getPath();
////                }
//                File file = new File(mImageIds);
//                try {
//                    // 将图片写入到文件
//                    FileOutputStream fileOutStream = new FileOutputStream(file);
//                    image.compress(Bitmap.CompressFormat.JPEG, 100,
//                            fileOutStream);
//                    fileOutStream.flush();
//                    fileOutStream.close();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//
//                //显示头像
//
//                filePath = file.getAbsolutePath();
//                netService.appSts(handler_appstr);
//                mllIdCard.setVisibility(View.GONE);
//                mImageCard.setImageBitmap(image);
//
//            }
        }
    }


}
