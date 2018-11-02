package com.xinzuokeji.boxtakeawaybusiness.netService;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.xinzuokeji.boxtakeawaybusiness.netutil.AsyncHttpClient;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;

/**
 *
 */
public class BaseServices {
    // 请求成功
    public static Integer DATA_SUCCESS = 2000;
    // 请求失败
    public static Integer DATA_FAILURE = 2001;

    // 请求上下文关系
    protected Context mContext;
    public RequestHandle Handle_UploadAPK;
    public RequestHandle Handle_Province;
    public RequestHandle Handle_City;
    // 服务器地址
    private String mHost = "http://192.168.0.105:8040/";

    //正式服务器
//    private String mHost = "http://api.goldwg.com/";
    // 构造函数

    // 获取图片的链接地址
    public String GetImageUrl(String imageId) {
        return String.format(mHost + "API/Common/Images/%s", imageId);
    }

    // 上传文件 contentType 需要文件的内容类型
    public void UploadFileBase(String fileName, String shopid, String url, String card_type, final Handler handler) {
        try {
            String serverFileName = new HttpPostFileAsyncTask().execute(fileName, shopid, url, card_type).get();
            Message message = new Message();
            message.what = DATA_SUCCESS;
            message.obj = serverFileName;
            handler.sendMessage(message);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    private static final String BOUNDARY = "";

    //上传文件
    private String PostFileToStream(String fileName, String shopid, String url, String card_type) {
        String FILEUPLOADURL = url + "?shopid=%s&card_type=%s";
        FILEUPLOADURL = String.format(FILEUPLOADURL, shopid, card_type);
        HttpURLConnection connection = null;
        try {
            String cookie;
            // 清空服务器返回的内容
            String response = "";

            // 请求服务器数据
            connection = (HttpURLConnection) new URL(FILEUPLOADURL)
                    .openConnection();
            // 请求方式
            connection.setRequestMethod("POST");
            // 超时时间
            connection.setReadTimeout(3 * 60 * 1000);
            connection.setConnectTimeout(3 * 60 * 1000);
            // POST请求必须设置允许输入、输出,不允许缓存
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            //开始上传文件
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            //文件流
            File file = new File(fileName);
            outputStream.writeBytes("--" + BOUNDARY + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "img"
                    + "\"; filename=\"" + URLEncoder.encode(file.getName()) + "\"\r\n");
            outputStream.writeBytes("Content-Type:image/jpeg\r\n");
            outputStream.writeBytes("\r\n");
            FileInputStream in = new FileInputStream(file);
            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                outputStream.write(b, 0, n);
            }
            in.close();
            //outputStream.write(out.toByteArray());
            outputStream.writeBytes("\r\n");

            outputStream.writeBytes("--" + BOUNDARY + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"contentType\"\r\n");
            outputStream.writeBytes("\r\n");
            outputStream.writeBytes("image/jpeg\r\n");
            //文件結束
            outputStream.writeBytes("--" + BOUNDARY + "--" + "\r\n");
            outputStream.writeBytes("\r\n");
            outputStream.flush();
            outputStream.close();

            // 实际上开发发送数据是从下边的getInputStream开始的
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "utf-8");
                // 缓冲??
                BufferedReader br = new BufferedReader(isr);
                // 读取的行数据
                String line = "";
                // 循环读取所有服务器返回
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                isr.close();
            } else {
                Log.e("Services Status",
                        FILEUPLOADURL + "(Code:" + connection.getResponseCode() + ")");
            }

            // 得到服务器返回的结果
            return response;

        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    class HttpPostFileAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("onPreExecute", "onPreExecute");
        }

        @Override
        protected String doInBackground(String... params) {
            // 提交数据到服务器并得到返回值
            String response = PostFileToStream(params[0], params[1], params[2], params[3]);
            Log.i("Services Status", "服务器返回-->" + response);
            String fileName = "";
//            try {
//                fileName = new JSONObject(new JSONObject(response).getString("Obj")).getString("FileName");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            return fileName;
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.i("onProgressUpdate", "onProgressUpdate");
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("onPostExecute", "onPostExecute");
        }


    }

    //GET API/Common/GetAllProvince
    // 获取所有省
    public void getProvince(final Handler handler) {
        // 请求的URL地址
        String url = "/API/Common/GetAllProvince";
        url = String.format(url);
        Handle_Province = AsyncHttpClient.get(mContext, url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    if (response.getBoolean("Status")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        if (!response.isNull("Obj")) {
//                            message.obj = new JSONDeserialize<>(Province.class, response.getString("Obj")).toObjects();
                        } else {
                            message.obj = null;
                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.getString("Message");
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //GET API/Common/GetProvinceCityById/{id}
    // 获取省下的所有市
    public void getProvinceCityById(String id, final Handler handler) {
        // 请求的URL地址
        String url = "API/Common/GetProvinceCityById/%s";
        url = String.format(url, id);
        Handle_City = AsyncHttpClient.get(mContext, url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    if (response.getBoolean("Status")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        if (!response.isNull("Obj")) {
//                            message.obj = new JSONDeserialize<>(Province.class, response.getString("Obj")).toObjects();
                        } else {
                            message.obj = null;
                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.getString("Message");
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

}
