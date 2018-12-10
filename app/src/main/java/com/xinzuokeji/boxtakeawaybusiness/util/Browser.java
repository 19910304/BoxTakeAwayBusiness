package com.xinzuokeji.boxtakeawaybusiness.util;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/******************************************************
 * 网页浏览
 ****************************************************/
public class Browser extends BaseActivity {

    //浏览器
    private WebView wv_browser;
    //浏览的Web页面Url
    //微软在线预览office文档
    private final String MicrosoftOnlinePreviewUrl = "https://view.officeapps.live.com/op/view.aspx?src=";
    String mTitle = "s";
    private Bitmap bmp;
    private static final String APP_ID = "wxa4207e39a8e5cf0f";
    private SDCardFileCache mFileCaches;
    //关于进度条的定义
    protected static final int STOP = 0x10000;
    protected static final int NEXT = 0x10001;
    private int iCount = 0;

    private ImageButton header_back;
    String url = "https://www.baidu.com/";
    private ProgressDialog mProgressDialog;

    // 下载失败
    public static final int DOWNLOAD_ERROR = 2;
    // 下载成功
    public static final int DOWNLOAD_SUCCESS = 1;

    // 进度条
    private ProgressBar mProgressBar;
    TextView textView;
    String nameclass = "";

    // 初始化控件
    @Override
    public void initView() {

        mFileCaches = new SDCardFileCache(getApplication());
        //得到参数
//        url = getIntent().getStringExtra("linemsg");
//        String nameclass = getIntent().getStringExtra("name_class");
        // 设置布局
        setContentView(R.layout.activity_web);
        //--------------BUTTON BACK-------------
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        textView = findViewById(R.id.header_title);
        //进度条
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_loading);
        //--------------WEBVIEW-------------

        wv_browser = (WebView) findViewById(R.id.wv);
        if (nameclass.equals("register")) {
            wv_browser.loadUrl(MicrosoftOnlinePreviewUrl + url);

            textView.setText(R.string.app_name);
        } else {
            wv_browser.loadUrl(url);
        }
        //Settings
        WebSettings webSettings = wv_browser.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页

        //设置Web视图
        wv_browser.setWebViewClient(new webViewClient());
        //ChromeClient
        WebChromeClient chromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                textView.setText(title);
                super.onReceivedTitle(view, title);
            }

            // 网页进度条的加载
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == mProgressBar.getVisibility()) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        };
        wv_browser.setWebChromeClient(chromeClient);
        // 使用返回键的方式防止网页重定向
        wv_browser.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && wv_browser.canGoBack()) {
                        wv_browser.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }


    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                网页加载完成进度条消失
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            default:
                break;
        }

    }

    //监听返回上一个页面
    // 监听返回按键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //绑定返回按键和返回按钮
            ImageButton header_back = findViewById(R.id.header_back);

            if (wv_browser.canGoBack()) {
                wv_browser.goBack();//返回上一页面
                return true;
            } else {
                if (header_back != null) {
                    header_back.performClick();
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private Bitmap imageCreateByUrl(String url) {
        Bitmap image;
        InputStream is = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            image = BitmapFactory.decodeStream(is);

            // 返回图片
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载完成后  直接打开文件
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DOWNLOAD_SUCCESS:
                    File file = (File) msg.obj;
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(file), "application/doc");
//              startActivity(intent);
                    startActivity(Intent.createChooser(intent, "标题"));
                    /**
                     * 弹出选择框   把本activity销毁
                     */
                    finish();
                    break;
                case DOWNLOAD_ERROR:
//                    Util.showToast(act, "文件加载失败");
                    break;
            }
        }
    };
/**
 *
 */


    /**
     * 传入文件 url  文件路径  和 弹出的dialog  进行 下载文档
     */
    public static File downLoad(String serverpath, String savedfilepath, ProgressDialog pd) {
        try {
            URL url = new URL(serverpath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            if (conn.getResponseCode() == 200) {
                int max = conn.getContentLength();
                pd.setMax(max);
                InputStream is = conn.getInputStream();
                File file = new File(savedfilepath);
                FileOutputStream fos = new FileOutputStream(file);
                int len = 0;
                byte[] buffer = new byte[1024];
                int total = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    total += len;
                    pd.setProgress(total);
                }
                fos.flush();
                fos.close();
                is.close();
                return file;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getFileName(String serverurl) {
        return serverurl.substring(serverurl.lastIndexOf("/") + 1);
    }


}
