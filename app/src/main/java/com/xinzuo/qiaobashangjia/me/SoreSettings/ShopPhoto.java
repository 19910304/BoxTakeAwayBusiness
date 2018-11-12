package com.xinzuo.qiaobashangjia.me.SoreSettings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinzuo.qiaobashangjia.BaseActivity;
import com.xinzuo.qiaobashangjia.GSApplication;
import com.xinzuo.qiaobashangjia.R;
import com.xinzuo.qiaobashangjia.adapter.ViewHolder;
import com.xinzuo.qiaobashangjia.entities.ShopPhone;
import com.xinzuo.qiaobashangjia.netService.NetService;
import com.xinzuo.qiaobashangjia.util.LoadingDialog;
import com.xinzuo.qiaobashangjia.util.PictureUtil;
import com.xinzuo.qiaobashangjia.util.Valid;
import com.xinzuo.qiaobashangjia.util.customprovider.OssService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2018/7/17.
 */
//店铺图片
public class ShopPhoto extends BaseActivity {
    OSS oss;
    String editBucketName;
    private LinearLayout ll_header_back;
    GridView gv_shop_photo;
    private List<ShopPhone> mDate;
    private ListViewAdapter<ShopPhone> listViewAdapter;
    NetService netService;
    Button bt_delete_photo;
    private TextView tv_save;
    //定义一个变量，默认值是false
    private boolean isManager;

    String shopimgId;
    private String mUploadImageId = "";
    String url = "";
    String Bucket;
    String picName;
    List<String> shopimgids = new ArrayList<>();
    private int chooseMode = PictureMimeType.ofImage();
    private int maxSelectNum = 12;
    private List<LocalMedia> selectList = new ArrayList<>();
    //OSS的上传下载
    private OssService ossService;
    String imgPath = "";
    /**
     * 上传次数
     */
    int number;

    LoadingDialog.Builder loadBuilder;
    LoadingDialog dialog = null;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_shop_photo);
        netService = new NetService(this);
        ll_header_back = findViewById(R.id.ll_header_back);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("店铺图片");
        tv_save = findViewById(R.id.tv_save);
        tv_save.setText("编辑");
        bt_delete_photo = findViewById(R.id.bt_delete_photo);
        gv_shop_photo = findViewById(R.id.gv_shop_photo);
        mDate = new ArrayList<>();
        listViewAdapter = new ListViewAdapter<ShopPhone>(this, R.layout.item_shop_photo, mDate, isManager) {
            @Override
            public void convert(ViewHolder holder, final ShopPhone shopPhone, final int id, int selectedPosition) {
                ImageView img_shop_photo = holder.getView(R.id.img_shop_photo);
                final CheckBox ck_item_shop_photo = holder.getView(R.id.ck_item_shop_photo);
                if (isManager) {
                    ck_item_shop_photo.setVisibility(View.VISIBLE);
                    if (id == mDatas.size() - 1) {
                        ck_item_shop_photo.setVisibility(View.GONE);
                    }
                } else {
                    ck_item_shop_photo.setVisibility(View.GONE);
                }
                ck_item_shop_photo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (ck_item_shop_photo.isChecked()) {
                            //  数组下标越界
                            shopimgids.add(mDate.get(id).id);
                        } else {
                            remove(shopimgids, mDatas.get(id).id);
                        }
                    }
                });
                //最后item显示
                if (id == mDatas.size() - 1) {
                    LayoutInflater.from(ShopPhoto.this).inflate(R.layout.item_shop_photo, null);
                    ImageLoader.getInstance().displayImage("drawable://" + R.mipmap.iv_add_the_pic, img_shop_photo);
                    img_shop_photo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // 进入相册 以下是例子：不需要的api可以不写
//                            取消编辑才可以选择图片
                            if (tv_save.getText().toString().equals("编辑")) {
                                setSelectorPhoto();
                                shopimgids.clear();
                            } else {
                                showTip("请取消编辑后再选择图片", Toast.LENGTH_SHORT);
                            }


                        }
                    });
                } else {
                    ImageLoader.getInstance().displayImage(shopPhone.imgurl, img_shop_photo, GSApplication.getInstance().imageOptions);
                }

            }
        };
        gv_shop_photo.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
        netService.showShopPhoto(GetstoreId(), handlershowShopPhoto);

    }

    /*
      * 移除数组元素
      *
      */
    public void remove(List<String> list, String target) {
        for (int i = list.size() - 1; i >= 0; i--) {
            String item = list.get(i);
            if (target.equals(item)) {
                list.remove(item);
            }
        }
        print(list);
    }

    private void print(List<String> list) {
        for (String item : list) {
            System.out.println("元素值：" + item);
        }
    }

    JSONArray jsonArray;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    loadBuilder = new LoadingDialog.Builder(ShopPhoto.this)
                            .setMessage("")
                            .setCancelable(true)
                            .setCancelOutside(true);
                    dialog = loadBuilder.create();
                    dialog.show();
                    Log.i("selec", selectList.size() + "--" + imgPath + "----");
                    netService.appSts(handler_appstr);
                    break;
            }
        }
    }


    //进入选择照片页面的设置
    private void setSelectorPhoto() {
        PictureSelector.create(ShopPhoto.this)
                .openGallery(chooseMode)
//                                    .theme(themeId)
                .maxSelectNum(13 - mDate.size())
                .minSelectNum(1)
                .selectionMode(2)//2多选1单选
                .previewImage(true)
                .previewVideo(false)
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(false)
                .enableCrop(false)
                .compress(true)
                .glideOverride(160, 160)
                .previewEggs(true)
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
//                                    .withAspectRatio(aspect_ratio_x, aspect_ratio_y)
                .hideBottomControls(false)
                .isGif(false)
                .freeStyleCropEnabled(false)
                .circleDimmedLayer(false)
                .showCropFrame(false)
                .showCropGrid(false)
                .openClickSound(true)
//                .selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ll_header_back.setOnClickListener(this);
        bt_delete_photo.setOnClickListener(this);
        tv_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_header_back:
                finish();
                break;
            case R.id.tv_save://编辑图片
                isManager = !isManager;
                tv_save.setText(isManager ? "取消" : "编辑");
                if (isManager) {
                    bt_delete_photo.setVisibility(View.VISIBLE);
                } else {
                    bt_delete_photo.setVisibility(View.GONE);
                }
                //为自定义方法--控制另外一个变量
                listViewAdapter.changetShowDelImage(isManager);
                break;
            case R.id.bt_delete_photo://删除图片
                if (shopimgids.size() != 0) {
                    for (int i = 0; i < shopimgids.size(); i++) {
                        if (Valid.isNotNullOrEmpty(shopimgId)) {
                            shopimgId = shopimgId + "," + shopimgids.get(i);
                        } else {
                            shopimgId = shopimgids.get(i);
                        }

                    }
                    netService.delShopPhoto(shopimgId, handlerdelShopPhoto);
                } else {
                    showTip("请选择图片", Toast.LENGTH_SHORT);
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
                        for (int i = 0; i < selectList.size(); i++) {
                            picName = GSApplication.pictures + "/" + year() + "/" + getIdentity() + ".jpg";
                            upfileOss(BuchetName, picName, PictureUtil.getBytes(PictureUtil.getSmallBitmap(selectList.get(i).getPath())));
                            if (Valid.isNotNullOrEmpty(url)) {
                                url = url + "," + "https://" + Bucket + "/" + picName;
                            } else {
                                url = "https://" + Bucket + "/" + picName;
                            }

                        }


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
    @SuppressLint("HandlerLeak")
    Handler handlershowShopPhoto = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    ShopPhone shopPhone = new ShopPhone();
                    if (msg.obj.toString().equals("[]")) {
                        mDate.clear();
                        mDate.add(0, shopPhone);
                        Toast.makeText(ShopPhoto.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    } else {
                        mDate.clear();
                        mDate.addAll((Collection<? extends ShopPhone>) msg.obj);
                        mDate.add(mDate.size(), shopPhone);
                    }
                    listViewAdapter.notifyDataSetChanged();
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
            super.

                    handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handlerdelShopPhoto = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    // 删除完id设置为空
                    shopimgId = "";
//                    tv_save.setText("编辑");
//                    bt_delete_photo.setVisibility(View.GONE);
                    //为自定义方法--控制另外一个变量
//                    listViewAdapter.changetShowDelImage(!isManager);
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    netService.showShopPhoto(GetstoreId(), handlershowShopPhoto);
                    shopimgids.clear();
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
                    showTip("系统错误", Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
            super.

                    handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler Hander_addShopPhoto = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    showTip("添加成功", Toast.LENGTH_SHORT);
                    netService.showShopPhoto(GetstoreId(), handlershowShopPhoto);
                    url = "";
                    shopimgids.clear();
                    dialog.dismiss();
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
                number++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (number == selectList.size()) {
                            Log.i("imgid", url + "--");
                            number = 0;
                            netService.addShopPhoto(GetstoreId(), url, Hander_addShopPhoto);
                        }
                    }
                });
//                你上传的资源objectKey;
                Log.d("PutObject", "UploadSuccess" + result.toString() + "--");
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

    private abstract class ListViewAdapter<T> extends BaseAdapter {

        protected Context mContext;
        protected List<T> mDatas;
        protected LayoutInflater mInflater;
        protected int mLayoutId;
        //	private int mStart,mEnd;
//	private static String[] urls;
        private int selectedPosition = 0;
        //是否显示编辑按钮
        private boolean isShow;

        // 构造函数
        public ListViewAdapter(Context context, int layoutId, List<T> datas, boolean isShow) {
            this.mContext = context;
            this.mLayoutId = layoutId;
            this.mInflater = LayoutInflater.from(context);
            this.mDatas = datas;
            this.isShow = isShow;
        }


        public void changetShowDelImage(boolean isShow) {
            this.isShow = isShow;
            notifyDataSetChanged();
        }

        // Count
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mDatas == null ? 0 : mDatas.size();

        }

        // Item
        @Override
        public T getItem(int position) {
            // TODO Auto-generated method stub
            return mDatas.get(position);
        }

        // ItemId
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        //这句是把listview的点击position,传递过来
        public void clearSelection(int position) {
            selectedPosition = position;
        }

        // View
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mLayoutId, position);
            // 公布用户需要重写的方法
            convert(holder, getItem(position), position, selectedPosition);
            return holder.getConvertView();
        }

        // 用户实现控件和数据绑定的方法
        public abstract void convert(ViewHolder holder, T t, int id, int selectedPosition);


    }
}
