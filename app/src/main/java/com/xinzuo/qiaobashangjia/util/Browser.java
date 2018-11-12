package com.xinzuo.qiaobashangjia.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.xinzuo.qiaobashangjia.BaseActivity;
import com.xinzuo.qiaobashangjia.R;

import java.io.BufferedInputStream;
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

    String mTitle = "s";
    private Bitmap bmp;
    private static final String APP_ID = "wxa4207e39a8e5cf0f";
    private SDCardFileCache mFileCaches;
    //关于进度条的定义
    protected static final int STOP = 0x10000;
    protected static final int NEXT = 0x10001;
    private int iCount = 0;
    private Handler mHandler;
    private ProgressBar progressBar;
    private ImageButton header_back;

    // 初始化控件
    @Override
    public void initView() {

        mFileCaches = new SDCardFileCache(getApplication());
        //得到参数
//        mTitle = getIntent().getStringExtra("PAGE_TITLE");

        // 设置布局
        setContentView(R.layout.activity_web);
        //--------------BUTTON BACK-------------
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        //进度条
//        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_loading);
        //--------------WEBVIEW-------------
        wv_browser = (WebView) findViewById(R.id.wv);
        //Settings
        WebSettings webSettings = wv_browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setLoadsImagesAutomatically(true);
        if (!Valid.isNotNullOrEmpty(mTitle)) {
            //ChromeClient
            WebChromeClient chromeClient = new WebChromeClient() {
                @Override
                public void onReceivedTitle(WebView view, String title) {
//                    tv_page_title.setText(title);
                    super.onReceivedTitle(view, title);
                }

                // 网页进度条的加载
//                @Override
//                public void onProgressChanged(WebView view, int newProgress) {
//                    if (newProgress == 100) {
//                        mProgressBar.setVisibility(View.INVISIBLE);
//                    } else {
//                        if (View.INVISIBLE == mProgressBar.getVisibility()) {
//                            mProgressBar.setVisibility(View.VISIBLE);
//                        }
//                        mProgressBar.setProgress(newProgress);
//                    }
//                    super.onProgressChanged(view, newProgress);
//                }
            };
            wv_browser.setWebChromeClient(chromeClient);
        } else {
//            tv_page_title.setText(mTitle);
        }
        //Client
        WebViewClient client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                网页加载完成进度条消失
//                mProgressBar.setVisibility(View.GONE);
            }
        };
        wv_browser.setWebViewClient(client);

        wv_browser.loadUrl("http://www.265call.com");

//        image = getIntent().getStringExtra("PAGE_IMAGE");
//        Log.i("Services Status", "ImageUrl--->" + service.getImageUrl(image));
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
        }

    }

    //监听返回上一个页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wv_browser.canGoBack()) {
            wv_browser.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
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


}
