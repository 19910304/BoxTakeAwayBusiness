package com.xinzuokeji.boxtakeawaybusiness.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//SD卡文件缓存，缓存目录为当前应用程序名称命名的目录
public class SDCardFileCache {

    // 手机存在SD卡，可以缓存文件
    public static final long MAX_CACHE_TIME = 7 * 24 * 60 * 60 * 1000;
    public boolean mCanSDCardCache = true;
    // 文件存储目录
    private File mFileCacheDirectory;

    // 构造函数
    public SDCardFileCache(Context context) {
        // 文件存储目录
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            mFileCacheDirectory = new File(
                    Environment.getExternalStorageDirectory(),
                    context.getPackageName());

            // 判断当前目录是否存在
            if (!mFileCacheDirectory.exists()) {
                mFileCacheDirectory.mkdir();
            } else if (mFileCacheDirectory.isDirectory()) {
                // 清除陈旧的缓存文件，超过一个星期
                clearHistoryFileCache();
            }

        } else {
            mCanSDCardCache = false;
        }
    }

    // 清除陈旧的缓存文件，超过一个星期
    private void clearHistoryFileCache() {
        File[] files = mFileCacheDirectory.listFiles();

        // 没有缓存，直接退出
        if (files == null) {
            return;
        }

        // 清除一个星期前的缓存
        for (File file : files) {
            if (System.currentTimeMillis() - file.lastModified() > MAX_CACHE_TIME) {
                file.delete();
                continue;
            }
        }
    }

    // 从SDCARD中读取文件
    public Bitmap getFileFromFileCache(String fileName) {
        Bitmap image;
        File file = getLocationFile(fileName);
        if (file.exists()) {
            try {
                FileInputStream fs = null;
                fs = new FileInputStream(file);
                if (fs != null) {
                    BitmapFactory.Options bfOptions = new BitmapFactory.Options();
                    bfOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                    image = BitmapFactory.decodeStream(fs, null, bfOptions);
                    return image;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 创建一个本地存储的文件
    private File getLocationFile(String fileName) {
        return new File(mFileCacheDirectory,
                String.valueOf(fileName.hashCode()));
    }

    // 将文件写入到SDCARD
    public void putImageToFileCache(String fileName, Bitmap image) {
        Log.i("Services Status", "PUT_IMAGE_TO_SDCARD" + fileName);
        // 获取本地文件
        File file = getLocationFile(fileName);
        try {
            // 将图片写入到文件
            FileOutputStream fileOutStream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100,
                    fileOutStream);
            fileOutStream.flush();
            fileOutStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
