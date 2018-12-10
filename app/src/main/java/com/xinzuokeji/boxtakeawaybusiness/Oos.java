package com.xinzuokeji.boxtakeawaybusiness;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.luck.picture.lib.entity.LocalMedia;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2018/9/1.
 */

public class Oos {
    public OSS oss;
    String url;
    /**
     * 上传次数
     */
    int number;
    //    LocalMedia 换成自己的实体
    private List<LocalMedia> selectList = new ArrayList<>();
    public static final String pictures = "waimai_img";
    public String pathimg;

    //实例化阿里云
    public void initOss(Context context, String ACCESS_ID, String AccessKeySecret, String SecurityToken, String OSS_ENDPOINT) {
        //实例化阿里云
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ACCESS_ID, AccessKeySecret, SecurityToken);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(12); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        // oss为全局变量，OSS_ENDPOINT是一个OSS区域地址
        oss = new OSSClient(context, OSS_ENDPOINT, credentialProvider);
    }

    //
    public String imgs(Context context, List<LocalMedia> selectList, String BuchetName, String bucket) {
        //拼接文件名
        String paths = "";
        for (int i = 0; i < selectList.size(); i++) {
            String picName = pictures + "/" + year() + "/" + getIdentity(context) + ".jpg";
            if (Valid.isNotNullOrEmpty(url)) {
                url = url + "," + "https://" + bucket + "/" + picName;
            } else {
                url = "https://" + bucket + "/" + picName;
            }
            paths = upfileOss(url, BuchetName, picName, getBytes(getSmallBitmap(selectList.get(i).getPath())));

        }
        return paths;
    }

    public String img(Context context, String mUploadImageId, String BuchetName, String bucket) {
        //拼接文件名
        String picName = pictures + "/" + year() + "/" + getIdentity(context) + ".jpg";
        url = "https://" + bucket + "/" + picName;
        String path = upfileOss(url, BuchetName, picName, getBytes(getSmallBitmap(mUploadImageId)));
        return path;
    }

    //上传图片到服务器
    public void upimgs(Activity context) {
        number++;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (number == selectList.size()) {
                    number = 0;
                    //当number 等于要上传的数组大小时，上传服务器

                }
            }
        });
    }

    //此处这个bucketName是在阿里云平台上配置的 用来储存文件 可以设置私有公有等属性
    // objectKey为文件的名字 用来从平台上查询图片
    // FilePath为所选文件的uri路径
    public String upfileOss(final String urlPath, String bucketName, String objectKey, byte[] uploadFilePath) {
        final CountDownLatch latch = new CountDownLatch(1);
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

                pathimg = urlPath;
                Log.i("oath", pathimg + "__-------");
                // 上传服务器
                latch.countDown();
//                你上传的资源objectKey;
                Log.i("pathing", pathimg + "0");
                return pathimg;
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                pathimg = "";
                latch.countDown();
                Log.i("pathing", pathimg + "1");
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
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pathimg;
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();

        try {
            outputStream.close();
        } catch (IOException e) {
            Log.e("BitmapUtil", "close output stream error.", e);
        }

        return bytes;
    }

    public Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    //获取当前年月日
    public String year() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
//        time1.setText("Date获取当前日期时间" + simpleDateFormat.format(date));
        return time;
    }

    //生成uuid
    public String getIdentity(Context context) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String identity = preference.getString("identity", null);
        if (identity == null) {
            identity = java.util.UUID.randomUUID().toString();
            preference.edit().putString("identity", identity);
        }
        return identity;
    }
}
