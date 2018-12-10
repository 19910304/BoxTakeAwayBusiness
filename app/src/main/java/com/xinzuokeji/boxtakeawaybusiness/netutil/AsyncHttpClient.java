package com.xinzuokeji.boxtakeawaybusiness.netutil;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import cz.msebera.android.httpclient.HttpEntity;

/**
 *
 */
public class AsyncHttpClient {

    // 请求成功
    public static Integer REQUEST_SUCCESS = 1000;
    // 请求失败
    public static Integer REQUEST_FAILURE = 1001;
    public static Integer REQUEST_FAILURE_NET = 1002;
    // 服务器HOST地址
    //----测试服务器
    private static String mHost = "";
    private static String Host = "";
    private static String Hostpay = "";
    //----正式服务器
//    private static String mHost = "";

    // 获取API的绝对路径
    private static String getAbsoluteUrl(String relativeUrl) {
        return relativeUrl;
    }

    private static String getAbsoluteUrlnew(String relativeUrl) {
        return relativeUrl;
    }

    private static String getAbsoluteUrlpay(String relativeUrl) {
        return relativeUrl;
    }

    // ASYNC HTTP CLIENT 静态对象
    private static com.loopj.android.http.AsyncHttpClient client = new com.loopj.android.http.AsyncHttpClient();

    // ASYNC HTTP CLIENT 配置
    static {
        //设置链接超时，如果不设置，默认为10s,此处设置为20s
        client.setTimeout(10000);

    }

    // GET 方法
    public static RequestHandle get(Context context, String url, AsyncHttpResponseHandler responseHandler) {
        return client.get(context, getAbsoluteUrl(url), responseHandler);
    }

    public static RequestHandle get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        return client.get(context, getAbsoluteUrl(url), params, responseHandler);
    }

    // POST 方法
    public static RequestHandle post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        return client.post(context, getAbsoluteUrl(url), params, responseHandler);
    }

    //     POST 方法
    public static RequestHandle post(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface handlerInterface) {

        return client.post(context, getAbsoluteUrl(url), entity, contentType, handlerInterface);
    }

    public static RequestHandle postnew(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface handlerInterface) {
        return client.post(context, getAbsoluteUrlnew(url), entity, contentType, handlerInterface);
    }

    public static RequestHandle postpay(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface handlerInterface) {
        return client.post(context, getAbsoluteUrlpay(url), entity, contentType, handlerInterface);
    }
}
