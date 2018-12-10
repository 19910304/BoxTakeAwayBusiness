package com.xinzuokeji.boxtakeawaybusiness.netService;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.xinzuokeji.boxtakeawaybusiness.entities.AllShop;
import com.xinzuokeji.boxtakeawaybusiness.entities.BalanceWater;
import com.xinzuokeji.boxtakeawaybusiness.entities.BalanceWaterList;
import com.xinzuokeji.boxtakeawaybusiness.entities.BankInfo;
import com.xinzuokeji.boxtakeawaybusiness.entities.BankName;
import com.xinzuokeji.boxtakeawaybusiness.entities.BusinessNotice;
import com.xinzuokeji.boxtakeawaybusiness.entities.CardInfoStype;
import com.xinzuokeji.boxtakeawaybusiness.entities.DisList;
import com.xinzuokeji.boxtakeawaybusiness.entities.Evaluate;
import com.xinzuokeji.boxtakeawaybusiness.entities.GoodsData;
import com.xinzuokeji.boxtakeawaybusiness.entities.GoodsDetail;
import com.xinzuokeji.boxtakeawaybusiness.entities.GoodsMenu;
import com.xinzuokeji.boxtakeawaybusiness.entities.GuiGe;
import com.xinzuokeji.boxtakeawaybusiness.entities.License;
import com.xinzuokeji.boxtakeawaybusiness.entities.LoginInfo;
import com.xinzuokeji.boxtakeawaybusiness.entities.OrderInfo;
import com.xinzuokeji.boxtakeawaybusiness.entities.ServiceProblem;
import com.xinzuokeji.boxtakeawaybusiness.entities.ServiceType;
import com.xinzuokeji.boxtakeawaybusiness.entities.Sheng;
import com.xinzuokeji.boxtakeawaybusiness.entities.ShopPhone;
import com.xinzuokeji.boxtakeawaybusiness.entities.ShopRecord;
import com.xinzuokeji.boxtakeawaybusiness.entities.Shopbasic;
import com.xinzuokeji.boxtakeawaybusiness.entities.Shopname;
import com.xinzuokeji.boxtakeawaybusiness.entities.Shoptype;
import com.xinzuokeji.boxtakeawaybusiness.entities.ShowPreOrderSet;
import com.xinzuokeji.boxtakeawaybusiness.entities.StoreInfo;
import com.xinzuokeji.boxtakeawaybusiness.entities.TodayOrder;
import com.xinzuokeji.boxtakeawaybusiness.entities.Withdraw;
import com.xinzuokeji.boxtakeawaybusiness.entities.all_order;
import com.xinzuokeji.boxtakeawaybusiness.netutil.AsyncHttpClient;
import com.xinzuokeji.boxtakeawaybusiness.netutil.JSONDeserialize;
import com.xinzuokeji.boxtakeawaybusiness.util.LogUtils;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class NetService extends BaseServices {

    // 所有请求的上下文关系

    public RequestHandle Handle_Login;
    // 发送验证码
    public RequestHandle Handle_getSendsms;
    // 验证验证码接口
    public RequestHandle Handle_checkCode;
    // 注册账号
    public RequestHandle Handle_register;
    // 验证码登录
    public RequestHandle Handle_codeLogin;
    // 退出登录
    public RequestHandle Handle_signOut;
    //重置密码
    public RequestHandle Handle_backPwd;
    //验证手机号和验证码
    public RequestHandle Handle_checkCodePhone;
    //验证原手机号和验证码是否正确
    public RequestHandle Handle_checkOldPhone;
    //验证新手机号是否存在和验证码是否正确
    public RequestHandle Handle_checkNewPhone;
    public RequestHandle Handle_GetContarcts;
    public RequestHandle Handle_Send_SMS;
    public RequestHandle Handle_ShowShopdata;
    public RequestHandle Handle_updBusinessNotice;
    //修改店铺简介
    public RequestHandle Handle_updShopinfo;
    public RequestHandle Handle_updSendprice;
    //修改餐厅地址
    public RequestHandle Handle_updShopaddress;
    public RequestHandle Handle_updShop;
    //添加店铺
    public RequestHandle Handle_addShop;
    //商家确认送达
    public RequestHandle Handle_BuConDelivery;
    //修改基本信息
    public RequestHandle Handle_updShopbasic;
    // 添加基本信息
    public RequestHandle Handle_addShopbasic;
    //显示法人身份证
    public RequestHandle Handle_showIdentity;
    // 上传法人身份证信息
    public RequestHandle Handle_Identity;
    //上传营业执照/许可证图片
    public RequestHandle Handle_upload_license;
    public RequestHandle Handle_showShopbasic;
    public RequestHandle Handle_showCardInfo;
    public RequestHandle Handle_showLicense;
    public RequestHandle Handle_showLicence;
    public RequestHandle Handle_addlegal;
    public RequestHandle Handle_showBank;
    public RequestHandle Handle_addBank;
    public RequestHandle Handle_getBank;
    //
    public RequestHandle Handle_formalRecord;

    public RequestHandle Handle_auditRecord;
    public RequestHandle Handle_finishRecord;
    public RequestHandle Handle_negativeComment;
    public RequestHandle Handle_seCommentNum;

    public RequestHandle Handle_showEvaluate;

    public RequestHandle showEvaluateHuifu;
    public RequestHandle allShop;
    public RequestHandle todayOrder;
    public RequestHandle showCancelOrder;
    // 商家送餐
    public RequestHandle Handle_foodDelivery;
    // 商家出餐
    public RequestHandle Handle_ShopOutFood;
    // 商家手动接单
    public RequestHandle Handle_manualReceipt;
    // 预订单
    public RequestHandle showPreorder;
    // 显示菜单列表接口
    public RequestHandle showMenu;
    //显示对应菜单下的商品接口
    public RequestHandle showGoodsData;
    //    显示商品信息
    public RequestHandle GoodsDetail;
    //    添加/修改商品信息接口
    public RequestHandle addGoods;
    //    编辑菜单
    public RequestHandle updMenu;
    //    删除菜单
    public RequestHandle delMenu;
    //    显示我的账户信息接口
    public RequestHandle showMyAccount;
    public RequestHandle showAlipay;
    // 显示店铺图片接口
    public RequestHandle showShopPhoto;
    // 删除店铺图片
    public RequestHandle delShopPhoto;
    // 添加店铺图片
    public RequestHandle addShopPhoto;
    //    显示餐饮食品安全等级图片接口
    public RequestHandle showFoodSafety;
    //上传/修改餐饮食品安全等级图片
    public RequestHandle Hander_upload_FoodSafety;
    // 财务对账的显示数据接口
    public RequestHandle showFinance;
    //    显示提现信息
    public RequestHandle Hander_withdraw;
    //    提现接口
    public RequestHandle Hander_setshopPutForward;
    //查询配送时间段列表
    public RequestHandle Hander_DisList;
    //电子围栏
    public RequestHandle Hander_updDisList;
    //修改配送费和起送价
    public RequestHandle Hander_updDisPrice;
    // 商品上下架
    public RequestHandle showNorms;
    // 显示商品规格
    public RequestHandle showNormsnew;
    //删除商品属性接口
    public RequestHandle Hander_deleteAttribute;
    //删除商品规格
    public RequestHandle Hander_deleteNorms;
    //显示商品属性列表接口
    public RequestHandle Hander_showAttribute;
    // 显示商品属性详情接口
    public RequestHandle Hander_showAttrDetails;
    //查询今日订单明细列表接口
    public RequestHandle TodayOrderList;
    // 查询订单明细详情接口
    public RequestHandle hander_OrderListDe;
    //昨日顾客分析的总数据显示
    public RequestHandle hander_ShowYesterdayAnalysis;
    //经营数据里昨日经营报告数据
    public RequestHandle hander_yesterdayOperadata;
    // 经营数据里七日经营报告数据接口
    public RequestHandle hander_sevendayOperadata;
    //    获取上传文件的相关参数
    public RequestHandle hander_appSts;
    //商家评价骑手
    public RequestHandle hander_JudgeRider;
    // 店铺同意退款
    public RequestHandle hander_approvalRefund;
    //退款初始化
    public RequestHandle hander_approvalRefundNew;
    //店铺拒绝退款
    public RequestHandle hander_RefuseRefund;
    // 骑手对店铺的全部评价
    public RequestHandle Handle_showRiderEva;
    // 显示骑手对店铺的全部差评/好评接口
    public RequestHandle Handle_RiderGoodsOrBad;
    // 查询骑手对商家好评/差评数量
    public RequestHandle Handle_ReCommentNum;
    // 添加/修改商家预定日期接口
    public RequestHandle hander_addShopPerDate;
    //添加/修改商家预定单提醒时间
    public RequestHandle hander_addShopReminTime;
    // 修改商家是否接收预订单
    public RequestHandle hander_updatePreOrderSet;
    // 查询商家预订单设置
    public RequestHandle hander_showPreOrderSet;
    // 商品排序
    public RequestHandle updGoodsSort;
    // 上传商品图片
    public RequestHandle addGoodsimg;
    // 上传店铺logo
    public RequestHandle upload_logo;
    public RequestHandle Handle_Sheng;
    //显示店铺简介
    public RequestHandle Handle_showShopinfo;
    //显示店铺公告
    public RequestHandle Handle_showBusinessNotice;
    //    余额流水
    public RequestHandle Hander_balanceWater;
    // 添加/修改商品属性接口
    public RequestHandle Hander_addAttribute;
    //----测试服务器 http://www.kvaiya.cn
    private static String mHost = "http://waimai.023wx.cn/";
//    private static String mHost = "http://www.qiaoba.ren/";
    //    private static String mHost = "http://www.kvaiya.cn/";
    private static String sheng = "http://xx.com/api/geograph/getprovince";
    private static String shi = "http://xx.com/api/geograph/getCity";
    private static String qu = "http://xx.com/api/geograph/getArea";

    // 构造函数
    public NetService(Context context) {
        mContext = context;
    }

    //
    public void sheng(final Handler handler) {
        // 请求的URL地址
        String url = "";
        url = String.format(sheng);
        // 服务请求
        Handle_Sheng = AsyncHttpClient.get(mContext, url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject object = jsonObject.getJSONObject("status");
                    String code = object.getString("code");
                    if (code.equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(Sheng.class, response.getString("data")).toObjects();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i("LD.NET_DEBUG_INFO", responseString);

                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // 系统登录
    public void Login(String phone, String passwords, String equipment, String target_value, final Handler handler) {

        // 请求的URL地址
        String url = mHost + "shop/Register/login";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
            jsonObject.put("pwd", passwords);
            jsonObject.put("equipment", equipment);
            jsonObject.put("target_value", target_value);
            jsonObject.put("equipment_type", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.i(jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_Login = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        message.obj = new JSONDeserialize<>(UserInfo.class, response.getString("data")).toObjects();
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE_NET;
                handler.sendMessage(message);
            }
        });
    }

    //发送验证码
    // http://waimai.023wx.cn/shop/Register/getSendsms
    public void getSendsms(String phone, String type, final Handler handler) {
        //验证码类型 1：注册 2：登录
        // 请求的URL地址
        String url = mHost + "shop/Register/getSendsms";
        // 请求参数
        ByteArrayEntity entity = null;
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("login", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_getSendsms = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
                    message.obj = response.toString();
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i("LD.NET_DEBUG_INFO", responseString);

                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //验证手机号和验证码
    // http://waimai.023wx.cn/shop/Register/checkCodePhone
    public void checkCodePhone(String phone, int code, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Register/checkCodePhone";
        // 请求参数
        ByteArrayEntity entity = null;
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
            jsonObject.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("login", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_checkCodePhone = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {

                    Message message = new Message();
                    message.what = DATA_SUCCESS;
                    message.obj = response.toString();
                    handler.sendMessage(message);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i("LD.NET_DEBUG_INFO", responseString);

                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Register/checkOldPhone
    public void checkOldPhone(int bid, String phone, int code, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Register/checkOldPhone";
        // 请求参数
        ByteArrayEntity entity = null;
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bid", bid);
            jsonObject.put("phone", phone);
            jsonObject.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("checkOldPhone", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_checkCodePhone = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i("LD.NET_DEBUG_INFO", responseString);

                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //验证新手机号是否存在和验证码是否正确
    // http://waimai.023wx.cn/shop/Register/checkNewPhone
    public void checkNewPhone(int bid, String phone, int code, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Register/checkNewPhone";
        // 请求参数
        ByteArrayEntity entity = null;
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bid", bid);
            jsonObject.put("phone", phone);
            jsonObject.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("checkNewPhone", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_checkCodePhone = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i("LD.NET_DEBUG_INFO", responseString);

                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // 重置密码
    // http://waimai.023wx.cn/shop/Register/backPwd
    public void backPwd(String phone, String pwd, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Register/backPwd";
        // 请求参数
        ByteArrayEntity entity = null;
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
            jsonObject.put("pwd", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("backPwd", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_backPwd = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {

                    Message message = new Message();
                    message.what = DATA_SUCCESS;
                    message.obj = response.toString();
                    handler.sendMessage(message);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i("LD.NET_DEBUG_INFO", responseString);

                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //
    //商家验证码登录
    //http://waimai.023wx.cn/shop/Register/codeLogin
    public void codeLogin(String phone, int code, String equipment, String target_value, final Handler handler) {
        //验证码类型 1：注册 2：登录
        // 请求的URL地址
        String url = mHost + "shop/Register/codeLogin";
//        设备类型 1：Android 2：IOS
        // 请求参数
        ByteArrayEntity entity = null;
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
            jsonObject.put("code", code);
            jsonObject.put("equipment", equipment);
            jsonObject.put("target_value", target_value);
            jsonObject.put("equipment_type", 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("codeLogin", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_codeLogin = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        message.obj = new JSONDeserialize<>(UserInfo.class, response.getString("data")).toObjects();
                        message.obj = response.getString("data");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i("LD.NET_DEBUG_INFO", responseString);

                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //退出登录
    //http://waimai.023wx.cn/shop/Register/signOut
    public void signOut(int bid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Register/signOut";
        // 请求参数
        ByteArrayEntity entity = null;
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bid", bid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("signOut", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_signOut = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i("LD.NET_DEBUG_INFO", responseString);

                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // 验证验证码
    //   http://waimai.023wx.cn/shop/Register/checkCode
    public void checkCode(String code, final Handler handler) {

        // 请求的URL地址
        String url = mHost + "shop/Register/checkCode";

        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("login", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_checkCode = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
                    message.obj = status.getString("message");
                    handler.sendMessage(message);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i("LD.NET_DEBUG_INFO", responseString);

                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // 注册账号
    //  http://waimai.023wx.cn/shop/Register/register
    public void register(String phone, String pwd, final Handler handler) {

        // 请求的URL地址
        String url = mHost + "shop/Register/register";

        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
            jsonObject.put("pwd", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("login", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_register = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {

                    Message message = new Message();
                    message.what = DATA_SUCCESS;
                    message.obj = response.toString();
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("data")).toObjects();
                    handler.sendMessage(message);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i("LD.NET_DEBUG_INFO", responseString);

                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // 显示店铺信息接口
    public void ShowShopdata(int shopid, final Handler handler) {

        // 请求的URL地址
        String url = mHost + "shop/Setshop/showShopdata";

        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("login", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_ShowShopdata = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i("LD.NET_DEBUG_INFO", responseString);

                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

// 修改店铺营业状态接口

    public void updShopstatus(int shopid, int bid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Personal/updShopstatus";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("bid", bid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updShopstatus", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_ShowShopdata = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
//                    message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示店铺公告
    public void showBusinessNotice(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Setshop/showBusinessNotice";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_showBusinessNotice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
                    message.obj = new JSONDeserialize<>(BusinessNotice.class, response.getString("data")).toObject();
//                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示店铺简介
    public void showShopinfo(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Setshop/showShopinfo";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showShopinfo", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_showShopinfo = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
                    message.obj = new JSONDeserialize<>(BusinessNotice.class, response.getString("data")).toObject();
//                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //添加/修改店铺公告
    public void updBusinessNotice(int shopid, String notice, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Setshop/updBusinessNotice";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("notice", notice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updBusinessNotice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
//                    message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //修改店铺简介
    public void updShopinfo(int shopid, String notice, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Setshop/updShopinfo";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("shopinfo", notice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updShopinfo = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
//                    message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //修改店铺起送价接口
    public void updSendprice(int shopid, double price, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Setshop/updSendprice";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("price", price);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updSendprice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
//                    message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Setshop/updShopaddress
    //修改店铺地址
    public void updShopaddress(int shopid, String sheng, String shi, String qu, String address, String longitude, String dimension, String code, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Setshop/updShopaddress";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("sheng", sheng);
            jsonObject.put("shi", shi);
            jsonObject.put("qu", qu);
            jsonObject.put("address", address);
            jsonObject.put("longitude", longitude);
            jsonObject.put("dimension", dimension);
            jsonObject.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updShopaddress", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updShopaddress = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
//                    message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //修改店铺电话接口
    public void updShopphone(int shopid, String phone, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Setshop/updShopphone";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updBusinessNotice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
//                    message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //修改营业时间接口
    public void updBusinesstime(int shopid, String week, String hour, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Setshop/updBusinesstime";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("week", week);
            jsonObject.put("hours", hour);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updBusinessNotice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
//                    message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //修改店铺地址接口
    public void updShopaddress(int shopid, String address, String longitude, String dimension, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Setshop/updShopaddress";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("address", address);
            jsonObject.put("longitude", longitude);
            jsonObject.put("dimension", dimension);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updBusinessNotice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
//                    message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示店铺信息接口
    public void showCertificates(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Setshop/showCertificates";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updBusinessNotice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    Message message = new Message();
                    message.what = DATA_SUCCESS;
//                    message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                    message.obj = response.toString();
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //个人中心显示店铺信息接口
    public void showDataMe(int bid, int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/personal/showData";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bid", bid);
            jsonObject.put("shopid", shopid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updBusinessNotice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(StoreInfo.class, new JSONObject(response.getString("data")).toString()).toObject();
//                    message.obj = response.toString();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // 上传文件 contentType 需要文件的内容类型
    public void UploadFile(String shopid, String positive, String opposite, String legal_person, String register_number, final Handler handler) {
        try {
            String serverFileName = new HttpPostFileAsyncTask().execute(shopid, positive, opposite, legal_person, register_number).get();
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
    private String PostFileToStream(String shopid, String positive, String opposite, String legal_person, String register_number) {
        final String end = "\r\n";
        String FILEUPLOADURL = mHost + "shop/Shopimg/upload_Identity?shopid=%s&legal_person=%s&register_number=%s";
        FILEUPLOADURL = String.format(FILEUPLOADURL, shopid, legal_person, register_number);
        Log.i("FILEUPLOADURL", FILEUPLOADURL);
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
            File file = new File(positive);
            outputStream.writeBytes("--" + BOUNDARY + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "positive"
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
            outputStream.writeBytes("\r\n");

            //文件流--------------
            File fileopposite = new File(opposite);
            outputStream.writeBytes("--" + BOUNDARY + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "opposite"
                    + "\"; filename=\"" + URLEncoder.encode(fileopposite.getName()) + "\"\r\n");

            outputStream.writeBytes("Content-Type:image/jpeg\r\n");
            outputStream.writeBytes("\r\n");
            FileInputStream infileopposite = new FileInputStream(fileopposite);
            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] bopposite = new byte[1024];
            int nopposite;
            while ((nopposite = infileopposite.read(bopposite)) != -1) {
                outputStream.write(bopposite, 0, nopposite);
            }
            infileopposite.close();
            //outputStream.write(out.toByteArray());
            outputStream.writeBytes("\r\n");
            //--------------------
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
            String response = PostFileToStream(params[0], params[1], params[2], params[3], params[4]);
            Log.i("Services Status", "服务器返回-->" + response);
            String fileName = "";
//            try {
//                fileName = new JSONObject(new JSONObject(response).getString("Obj")).getString("FileName");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            return response;
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

    //商家服务中心显示问题分类接口
    public void serviceType(final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/personal/serviceType";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();

        Log.i("updBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updBusinessNotice = AsyncHttpClient.post(mContext, url, null, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(ServiceType.class, response.getString("data")).toObjects();
//                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //商家服务中心显示问题接口
    public void serviceProblem(int typeid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/personal/serviceProblem";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", typeid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updBusinessNotice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String objectdateeNew = response.getString("data").replaceAll("[\\[\\]]", "");//去掉中括号
                        message.obj = new JSONDeserialize<>(ServiceProblem.class, objectdateeNew).toObject();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //意见反馈接口
    public void businessFeedback(String fid, String phone, String content, String img, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/personal/businessFeedback";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fid", fid);
            jsonObject.put("phone", phone);
            jsonObject.put("content", content);
            jsonObject.put("img", img);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updBusinessNotice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        message.obj = new JSONDeserialize<>(StoreInfo.class, new JSONObject(response.getString("data")).toString()).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //修改密码接口
    public void updPwd(int bid, String oldpwd, String newpwd, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Personal/updPwd";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bid", bid);
            jsonObject.put("oldpwd", oldpwd);
            jsonObject.put("newpwd", newpwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updBusinessNotice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        message.obj = new JSONDeserialize<>(StoreInfo.class, new JSONObject(response.getString("data")).toString()).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示店铺类型接口
    public void showShoptype(final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/showShoptype";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("bid", bid);
//            jsonObject.put("oldpwd", oldpwd);
//            jsonObject.put("newpwd", newpwd);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Log.i("updBusinessNotice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updBusinessNotice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(Shoptype.class, response.getString("data")).toObjects();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示经营类型接口
    public void show_businesstype(int shop_typeid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/show_businesstype";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shop_typeid", shop_typeid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("show_businesstype", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updBusinessNotice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(Shoptype.class, response.getString("data")).toObjects();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示店铺基本信息接口
    public void showShopbasic(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/showShopbasic";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showShopbasic", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_showShopbasic = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(Shopbasic.class, response.getString("data")).toObject();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示证件是否上传
    public void showCardInfo(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/showCardInfo";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showCardInfo", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_showCardInfo = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        String objectdateeNew = response.getString("data").replaceAll("[\\[\\]]", "");//去掉中括号
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(CardInfoStype.class, response.getString("data")).toObject();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //修改店铺的基本信息接口
//    http://waimai.023wx.cn/shop/Shop/updShopbasic
//    id	是	int	店铺id
//    contacts_phone	是	string	联系人电话
//    contacts	是	string	联系人姓名
//    shop_phone	是	string	店铺电话
//    business_hours	是	string	营业时间
//    sheng	是	string	省
//    shi	是	string	shi
//    qu	是	string	区
//    shop_address	是	string	店铺详细地址
//    longitude	是	string	经度
//    dimension	是	string	维度
    public void updShopbasic(int shopid, String contacts_phone, String contacts, String shop_phone, String business_hours, String sheng, String shi, String qu, String shop_address, String longitude, String dimension, String out_img, String in_img, String logo, String code, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/updShopbasic";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", shopid);
            jsonObject.put("contacts_phone", contacts_phone);
            jsonObject.put("contacts", contacts);
            jsonObject.put("shop_phone", shop_phone);
            jsonObject.put("business_hours", business_hours);
            jsonObject.put("sheng", sheng);
            jsonObject.put("shi", shi);
            jsonObject.put("qu", qu);
            jsonObject.put("shop_address", shop_address);
            jsonObject.put("longitude", longitude);
            jsonObject.put("dimension", dimension);
            jsonObject.put("out_img", out_img);
            jsonObject.put("in_img", in_img);
            jsonObject.put("logo", logo);
            jsonObject.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showShopbasic", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updShopbasic = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //    http://waimai.023wx.cn/shop/Shop/addShopbasic
    // 添加店铺基本信息
    public void addShopbasic(int shopid, String contacts_phone, String contacts, String shop_phone, String business_hours, String sheng, String shi, String qu, String shop_address, String longitude, String dimension, String out_img, String in_img, String logo, String code, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/addShopbasic";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", shopid);
            jsonObject.put("contacts_phone", contacts_phone);
            jsonObject.put("contacts", contacts);
            jsonObject.put("shop_phone", shop_phone);
            jsonObject.put("business_hours", business_hours);
            jsonObject.put("sheng", sheng);
            jsonObject.put("shi", shi);
            jsonObject.put("qu", qu);
            jsonObject.put("shop_address", shop_address);
            jsonObject.put("longitude", longitude);
            jsonObject.put("dimension", dimension);
            jsonObject.put("out_img", out_img);
            jsonObject.put("in_img", in_img);
            jsonObject.put("logo", logo);
            jsonObject.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("addShopbasic", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_addShopbasic = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //上传法人身份证信息
    public void upload_Identity(int shopid, String positive, String opposite, String legal_person, String register_number, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shopimg/upload_Identity";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("positive", positive);
            jsonObject.put("opposite", opposite);
            jsonObject.put("legal_person", legal_person);
            jsonObject.put("register_number", register_number);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("upload_Identity", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_Identity = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //上传营业执照/许可证图片
    public void upload_license(int shopid, String img, String card_type, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shopimg/upload_license";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("img", img);
            jsonObject.put("card_type", card_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("upload_Identity", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_upload_license = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //上传营业执照信息
    // http://waimai.023wx.cn/shop/Shop/addLicense
//    shop_id	是	int	店铺id
//    certificates_name	是	string	证件名称
//    legal_person	是	string	法人代表姓名
//    register_number	是	string	证件注册号
//    certificates_place	是	string	证件所在地
//    validity_type	是	int	证件有效类型(1:长期有效 0:短期有效)
//    validity_term	是	string	长期有效可以不传值，短期有效要传值 例:2019-01-01
    public void addLicense(int shopid, String certificates_name, String legal_person, String register_number, String certificates_place, int validity_type, String validity_term, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/addLicense";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shop_id", shopid);
            jsonObject.put("certificates_name", certificates_name);
            jsonObject.put("legal_person", legal_person);
            jsonObject.put("register_number", register_number);
            jsonObject.put("certificates_place", certificates_place);
            jsonObject.put("validity_type", validity_type);
            jsonObject.put("validity_term", validity_term);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showShopbasic", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updShopbasic = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示营业执照接口
    //http://waimai.023wx.cn/shop/Shop/showLicense
    public void showLicense(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/showLicense";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showShopbasic", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_showLicense = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String objectdateeNew = response.getString("data").replaceAll("[\\[\\]]", "");
                        message.obj = new JSONDeserialize<>(License.class, objectdateeNew).toObject();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // 上传许可证信息接口
    //http://waimai.023wx.cn/shop/Shop/addLicence
    public void addLicence(int shopid, String certificates_name, String legal_person, String register_number, String certificates_place, int validity_type, String validity_term, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/addLicence";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shop_id", shopid);
            jsonObject.put("certificates_name", certificates_name);
            jsonObject.put("legal_person", legal_person);
            jsonObject.put("register_number", register_number);
            jsonObject.put("certificates_place", certificates_place);
            jsonObject.put("validity_type", validity_type);
            jsonObject.put("validity_term", validity_term);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showShopbasic", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_showLicense = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示许可证信息接口c
    //http://waimai.023wx.cn/shop/Shop/showLicence
    public void showLicence(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/showLicence";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showShopbasic", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_showLicence = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String objectdateeNew = response.getString("data").replaceAll("[\\[\\]]", "");
                        message.obj = new JSONDeserialize<>(License.class, objectdateeNew).toObject();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //    显示法人身份证接口
    // http://waimai.023wx.cn/shop/Shop/showIdentity
    public void showIdentity(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/showIdentity";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showIdentity", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_showIdentity = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String objectdateeNew = response.getString("data").replaceAll("[\\[\\]]", "");
                        message.obj = new JSONDeserialize<>(License.class, objectdateeNew).toObject();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // 上传法人身份证照信息接口
    //http://waimai.023wx.cn/shop/Shop/addlegal
    public void addlegal(int shopid, String legal_person, String register_number, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/addlegal";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shop_id", shopid);
            jsonObject.put("legal_person", legal_person);
            jsonObject.put("register_number", register_number);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("addlegal", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_addlegal = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示收款信息
    //http://waimai.023wx.cn/shop/Shop/showBank
    public void showBank(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/showBank";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shop_id", shopid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showBank", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_showBank = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String objectdateeNew = response.getString("data").replaceAll("[\\[\\]]", "");
                        message.obj = new JSONDeserialize<>(BankInfo.class, objectdateeNew).toObject();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //添加/修改收款信息
    //http://waimai.023wx.cn/shop/Shop/addBank
    public void addBank(int shopid, int account_type, String name, String card, String city, String bank_code, String opening_bank, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/addBank";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shop_id", shopid);
            jsonObject.put("account_type", account_type);
            jsonObject.put("name", name);
            jsonObject.put("card", card);
            jsonObject.put("city", city);
            jsonObject.put("bank_code", bank_code);
            jsonObject.put("opening_bank", opening_bank);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("addBank", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_addBank = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;

                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示银行卡列表
    public void getBank(final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/getBankList";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();

//        Log.i("getBank", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_getBank = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(BankName.class, response.getString("data")).toObjects();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Shop/formalRecord
    // 显示待处理接口
    public void formalRecord(int bid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/formalRecord";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bid", bid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("formalRecord", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_formalRecord = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(ShopRecord.class, response.getString("data")).toObjects();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Shop/auditRecord
    // 显示待处理接口
    public void auditRecord(int bid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/auditRecord";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bid", bid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("auditRecord", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_auditRecord = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(ShopRecord.class, response.getString("data")).toObjects();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // 显示已完成接口
    public void finishRecord(int bid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/finishRecord";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bid", bid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("finishRecord", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_finishRecord = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(ShopRecord.class, response.getString("data")).toObjects();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示店铺名字和类型
    public void showShopname(int shop_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/showShopname";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shop_id", shop_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showShopbasic", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updBusinessNotice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
                        message.obj = new JSONDeserialize<>(Shopname.class, date).toObject();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Shop/addShop
    // 添加店铺的店铺名称和类型
    public void addShop(int bid, int shop_type, String shop_name, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/addShop";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bid", bid);
            jsonObject.put("shop_type", shop_type);
            jsonObject.put("shop_name", shop_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("addShop", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_addShop = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //修改店铺的店铺名称和类型
    public void updShop(int shop_id, int shop_type, String shop_name, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shop/updShop";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shop_id", shop_id);
            jsonObject.put("shop_type", shop_type);
            jsonObject.put("shop_name", shop_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updShop", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updShop = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //    商家确认送达 http://waimai.023wx.cn/shop/Order/BuConDelivery
    public void BuConDelivery(int order_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/BuConDelivery";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("BuConDelivery", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_BuConDelivery = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, response.getString("Obj")).toObject();
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //   查询进行中的订单
    // http://waimai.023wx.cn/shop/Order/showJingOrder
    public void showJingOrder(int shop_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/showJingOrder";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shop_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showJingOrder", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updShop = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(OrderInfo.class, response.getString("data")).toObjects();
//                        } else {
//                            message.obj = response.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //    查询新订单
    // http://waimai.023wx.cn/shop/Order/showUntreated
    public void showUntreated(int shop_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/showUntreated";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shop_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showUntreated", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updShop = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(OrderInfo.class, response.getString("data")).toObjects();
//                        } else {
//                            message.obj = response.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //  http://waimai.023wx.cn/shop/Order/manualReceipt
    // 商家手动接单
    public void manualReceipt(int orderid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/manualReceipt";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", orderid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("manualReceipt", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_manualReceipt = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //商家配送
    // http://waimai.023wx.cn/shop/Order/foodDelivery
    public void foodDelivery(int orderid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/foodDelivery";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", orderid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("manualReceipt", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_foodDelivery = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //商家出餐
    // http://waimai.023wx.cn/shop/Order/ShopOutFood
    public void ShopOutFood(int orderid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/ShopOutFood";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", orderid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("ShopOutFood", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_ShopOutFood = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }
    //查询待发配送的订单
// http://waimai.023wx.cn/shop/Order/waitDistribution

    public void waitDistribution(int shop_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/waitDistribution";
        // 请求参数
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shop_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("waitDistribution", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updShop = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(OrderInfo.class, response.getString("data")).toObjects();
//                        } else {
//                            message.obj = response.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //查询催单的订单接口
    //http://waimai.023wx.cn/shop/Order/showFinishOrder
    public void showFinishOrder(int shop_id, int order_type, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/showFinishOrder";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shop_id);
            jsonObject.put("order_type", order_type);//订单类型 6:已完成 7:催单
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("waitDistribution", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updShop = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(OrderInfo.class, response.getString("data")).toObjects();
//                        } else {
//                            message.obj = response.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //查询退款订单
    //http://waimai.023wx.cn/shop/Order/requestRefund
    public void requestRefund(int shop_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/requestRefund";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shop_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("waitDistribution", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updShop = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(OrderInfo.class, response.getString("data")).toObjects();
//                        } else {
//                            message.obj = response.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //店铺同意退款

    //http://waimai.023wx.cn/shop/Refund/approvalRefund
    public void approvalRefund1(int orderid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/approvalRefund";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", orderid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("approvalRefund1", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_updShop = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(OrderInfo.class, response.getString("data")).toObjects();
//                        } else {
                        message.obj = response.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }
//    显示用户对店铺的全部评价http://waimai.023wx.cn/shop/Evaluate/showEvaluate

    public void showEvaluate(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Evaluate/showEvaluate";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showEvaluate", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_showEvaluate = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(Evaluate.class, response.getString("data")).toObjects();
//                        } else {
//                        message.obj = response.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示用户对店铺的全部差评接口
    public void negativeComment(int shopid, int type, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Evaluate/negativeComment";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("negativeComment", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_negativeComment = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(Evaluate.class, response.getString("data")).toObjects();
//                        } else {
//                        message.obj = response.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //查询好评/差评数量接口
// http://waimai.023wx.cn/shop/Evaluate/seCommentNum
    public void seCommentNum(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Evaluate/seCommentNum";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("seCommentNum", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_seCommentNum = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(Evaluate.class, response.getString("data")).toObjects();
//                        } else {
                        message.obj = response.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //骑手对店铺的全部评价
    // http://waimai.023wx.cn/shop/Evaluate/showRiderEva
    public void showRiderEva(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Evaluate/showRiderEva";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showRiderEva", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_showRiderEva = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(Evaluate.class, response.getString("data")).toObjects();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        //
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示骑手对店铺的全部差评/好评
    // http://waimai.023wx.cn/shop/Evaluate/RiderGoodsOrBad
    public void RiderGoodsOrBad(int shopid, int type, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Evaluate/RiderGoodsOrBad";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("RiderGoodsOrBad", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_RiderGoodsOrBad = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(Evaluate.class, response.getString("data")).toObjects();
//                        } else {
//                        message.obj = response.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //查询骑手对商家好评/差评数量
    // http://waimai.023wx.cn/shop/Evaluate/ReCommentNum
    public void ReCommentNum(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Evaluate/ReCommentNum";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("ReCommentNum", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Handle_ReCommentNum = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(Evaluate.class, response.getString("data")).toObjects();
//                        } else {
                        message.obj = response.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
//
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Evaluate/showEvaluate
    // 商家回复用户评价
    public void showEvaluateReply(int shopid, int replyid, String reply_content, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Evaluate/Reply";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("replyid", replyid);
            jsonObject.put("reply_content", reply_content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showEvaluatehuifu", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showEvaluateHuifu = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(Evaluate.class, response.getString("data")).toObjects();
//                        } else {
                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // 显示商家所有门店
    public void allShop(int bid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/personal/allShop";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bid", bid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("allShop", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        allShop = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(AllShop.class, response.getString("data")).toObjects();
//                        } else {
//                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // 门店运营的显示数据
    // http://waimai.023wx.cn/shop/Order/todayOrder
    public void todayOrder(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/todayOrder";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("allShop", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        todayOrder = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String date = response.getString("data");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(TodayOrder.class, date).toObject();
//                        } else {
//                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //查询已取消的订单
    // http://waimai.023wx.cn/shop/Order/showCancelOrder
    public void showCancelOrder(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/showCancelOrder";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showCancelOrder", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showCancelOrder = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(OrderInfo.class, response.getString("data")).toObjects();
//                        } else {
//                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Order/showPreorder
    // 显示预订单接口
    public void showPreorder(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/showPreorder";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showCancelOrder", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showPreorder = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(OrderInfo.class, response.getString("data")).toObjects();
//                        } else {
//                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示菜单列表接口
//    http://waimai.023wx.cn/shop/Goods/showMenu
    public void showMenu(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/showMenu";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showCancelOrder", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showMenu = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(GoodsMenu.class, response.getString("data")).toObjects();
//                        } else {
//                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示菜单列表接口
    //http://waimai.023wx.cn/shop/Goods/showMenu
    public void updGoodsSort(List<String> oldgoodLIST, List<String> goodLIST, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/updGoodsSort";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        RequestParams params = new RequestParams();
        params.put("after", goodLIST);
        params.put("before", oldgoodLIST);
//        try {
//            jsonObject.put("after", goodLIST);
//            jsonObject.put("before", oldgoodLIST);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Log.i("updGoodsSort", params.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        updGoodsSort = AsyncHttpClient.post(mContext, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示对应菜单下的商品接口
// http://waimai.023wx.cn/shop/Goods/showGoodsData
//
    public void showGoodsData(int shopid, int menuid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/showGoodsData";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("menuid", menuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showMenu", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showGoodsData = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(GoodsData.class, response.getString("data")).toObjects();
//                        } else {
//                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示商品信息接口
// http://waimai.023wx.cn/shop/Goods/GoodsDetail
    public void GoodsDetail(int goodid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/GoodsDetail";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", goodid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("GoodsDetail", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        GoodsDetail = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(GoodsDetail.class, date).toObject();
//                        } else {
//                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //添加/修改商品信息
    // http://waimai.023wx.cn/public/index.php/shop/Goods/addGoods
    public void addGoods(int goodid, int shopid, String goods_name, double goods_price, String begin_time, String end_time, int menu_id, String label_one, String label_two, String label_three, double lunch_box_price, String describe, int stock, String norms_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/addGoods";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
//        shopid	是	int	店铺id
//        goods_name	是	string	商品名字
//        goods_price	是	double	商品价格
//        begin_time	是	string	出售开始时间
//        end_time	是	string	出售结束时间
//        menu_id	是	int	菜单id
//        label_one	是	string	标签1(可为空)
//                label_two	是	string	标签2(可为空)
//        label_three	是	string	标签3(可为空)
//                lunch_box_price	是	double	餐盒价格
//        describe	是	string	商品描述(可为空)
//                stock	是	int	库存
        try {
            jsonObject.put("id", goodid);
            jsonObject.put("shopid", shopid);
            jsonObject.put("goods_name", goods_name);
            jsonObject.put("goods_price", goods_price);
            jsonObject.put("begin_time", begin_time);
            jsonObject.put("end_time", end_time);
            jsonObject.put("menu_id", menu_id);
            jsonObject.put("label_one", label_one);
            jsonObject.put("label_two", label_two);
            jsonObject.put("label_three", label_three);
            jsonObject.put("lunch_box_price", lunch_box_price);
            jsonObject.put("describe", describe);
            jsonObject.put("stock", stock);
            jsonObject.put("norms_id", norms_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("addGoods", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        addGoods = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(GoodsDetail.class, date).toObject();
//                        } else {
                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Goods/addMenu
    // 添加菜单
    public void addMenu(int shopid, String menu_name, String describe, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/addMenu";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("menu_name", menu_name);
            jsonObject.put("describe", describe);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("addMenu", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        addGoods = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(GoodsDetail.class, date).toObject();
//                        } else {
                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Goods/updMenu
    // 编辑菜单
    public void updMenu(int id, int shopid, String menu_name, String describe, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/updMenu";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("shopid", shopid);
            jsonObject.put("menu_name", menu_name);
            jsonObject.put("describe", describe);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updMenu", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        updMenu = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(GoodsDetail.class, date).toObject();
//                        } else {
                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //删除菜单接口
    // http://waimai.023wx.cn/shop/Goods/delMenu
    public void delMenu(int id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/delMenu";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("delMenu", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        delMenu = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(GoodsDetail.class, date).toObject();
//                        } else {
                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示我的账户信息接口
    // http://waimai.023wx.cn/shop/Personal/showMyAccount
    public void showMyAccount(int bid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Personal/showMyAccount";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bid", bid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("delMenu", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showMyAccount = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(LoginInfo.class, date).toObject();
//                        } else {
//                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //
    public void GetContarcts(String lt, String wt, final Handler handler) {
        // 请求的URL地址+%s+ , +%s +& type = 010
        String url = "http://gc.ditu.aliyun.com/regeocoding?l= " + lt + "" + "," + wt + "" + "&type=010";
        url = String.format(url);

        // 服务请求
        Handle_GetContarcts = AsyncHttpClient.get(mContext, url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject object = (JSONObject) jsonObject.getJSONArray("addrList").get(0);
                    if (object.getString("status").equals("1")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = object;
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response;
                        handler.sendMessage(message);
                    }

//                    if (response.getBoolean("Status")) {
//                        Message message = new Message();
//                        message.what = DATA_SUCCESS;
//
////                        mACache = ACache.get(mContext);
////                        mACache.put("Contarcts", response.getString("Obj"));//缓存
////                        message.obj = new JSONDeserialize<>(Contacts_Group.class, response.getString("Obj")).toObjects();
//                        handler.sendMessage(message);
//                    } else {
//
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.i("LD.NET_DEBUG_INFO", responseString);

                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //    http://waimai.023wx.cn/api/Payment/alipay
    //支付宝APP接口
    public void alipay(String out_trade_no, int total_fee, String body, String spbill_create_ip, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "api/Payment/alipay";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("out_trade_no", out_trade_no);
            jsonObject.put("total_fee ", total_fee);
            jsonObject.put("body", body);
            jsonObject.put("spbill_create_ip", spbill_create_ip);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("alipay", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showAlipay = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.toString());
                    status.getString("code");
                    String date = status.getString("data");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, date).toObject();
//                        } else {
                        message.obj = date;
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //    微信支付接口
    public void wxPay(String out_trade_no, int total_fee, String spbill_create_ip, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "api/Payment/wxPay";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("body", "微信");
            jsonObject.put("detail", "微信充值");
            jsonObject.put("out_trade_no", out_trade_no);
            jsonObject.put("total_fee", total_fee);
            jsonObject.put("spbill_create_ip", spbill_create_ip);
            jsonObject.put("type", "APP");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("wxPay", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showAlipay = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.toString());
                    status.getString("code");
                    String date = status.getString("data");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(LoginInfo.class, date).toObject();
//                        } else {
                        message.obj = date;
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示店铺图片
    public void showShopPhoto(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Setshop/showShopPhoto";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showShopPhoto", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showShopPhoto = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(ShopPhone.class, response.getString("data")).toObjects();
//                        } else {
//                        message.obj = response.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //    删除店铺图片
// http://waimai.023wx.cn/shop/Setshop/delShopPhoto
    public void delShopPhoto(String id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Setshop/delShopPhoto";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showShopPhoto", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        delShopPhoto = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //    http://waimai.023wx.cn/shop/Setshop/addShopPhoto
    // 添加店铺图片接口
    public void addShopPhoto(int shopid, String imgurl, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Setshop/addShopPhoto";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("imgurl", imgurl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("addShopPhoto", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        addShopPhoto = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(ShopPhone.class, response.getString("data")).toObjects();
//                        } else {
                        message.obj = response.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Shopimg/showFoodSafety
    // 显示餐饮食品安全等级图片接口
    public void showFoodSafety(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shopimg/showFoodSafety";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showFoodSafety", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showFoodSafety = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(ShopPhone.class, response.getString("data")).toObjects();
//                        } else {
                        JSONObject data = new JSONObject(response.getString("data"));
                        message.obj = data.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Shopimg/upload_FoodSafety
    // 上传/修改餐饮食品安全等级图片
    public void upload_FoodSafety(int shopid, String shopSafeIng, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shopimg/upload_FoodSafety";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("img", shopSafeIng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("upload_FoodSafety", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Hander_upload_FoodSafety = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //查询配送时间段列表
    public void getDisList(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Distribution/getDisList";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shop_id", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("getDisList", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Hander_DisList = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(DisList.class, response.getString("data")).toObjects();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //电子围栏
    public void updDisList(int id, JSONArray pointSets, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Distribution/updDisList";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("point_set", pointSets);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updDisList", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Hander_updDisList = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //修改配送费和起送价
    public void updDisPrice(int id, int shopId, String dis_money, String start_price, String startTime, String endTime, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Distribution/updDisPrice";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("shop_id", shopId);
            jsonObject.put("dis_money", dis_money);
            jsonObject.put("start_price", start_price);
            jsonObject.put("open", startTime);
            jsonObject.put("close", endTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updDisPrice", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Hander_updDisPrice = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //店铺提现显示数据
    //http://waimai.023wx.cn/shop/Order/presentData
    public void withdraw(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/presentData";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("withdraw", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Hander_withdraw = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(Withdraw.class, response.getString("data")).toObject();
//                        } else {
//                        JSONObject data = new JSONObject(response.getString("data"));
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //店铺提现
//    waimai.023wx.cn/api/putforwards/setRiderPutForward
    //http://xx.com/api/putforwards/setRiderPutForward
    public void setshopPutForward(int shopid, double money, String bank_id, int bank_code, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "api/putforwards/setRiderPutForward";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", shopid);
            jsonObject.put("money", money);
            jsonObject.put("bank_id", bank_id);
            jsonObject.put("bank_code", bank_code);
            jsonObject.put("identity", 3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("setshopPutForward", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Hander_setshopPutForward = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(Withdraw.class, response.getString("data")).toObject();
//                        } else {
//                        JSONObject data = new JSONObject(response.getString("data"));
                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //财务对账的显示数据接口
    //http://waimai.023wx.cn/shop/Order/Finance
    public void Finance(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/Finance";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showFinance", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showFinance = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(Finance.class, response.getString("data")).toObject();
//                        } else {
                        JSONObject data = new JSONObject(response.getString("data"));
                        message.obj = data.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // http://waimai.023wx.cn/shop/Goods/isLower
    // 商品上下架
    public void showNorms(int id, int type, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/isLower";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showNorms", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showNorms = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(Finance.class, response.getString("data")).toObject();
//                        } else {
//                        JSONObject data = new JSONObject(response.getString("data"));
                        message.obj = response.getString("data");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // http://waimai.023wx.cn/shop/Goods/isLower
    // 上传商品图片
    public void addGoodsimg(int id, int shopid, String ingFath, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/addGoodsimg";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("shopid", shopid);
            jsonObject.put("img", ingFath);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("addGoodsimg", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        addGoodsimg = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Shopimg/upload_logo
//    上传店铺logo
    public void upload_logo(int shopid, String ingFath, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Shopimg/upload_logo";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("img", ingFath);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("upload_logo", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        upload_logo = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // http://waimai.023wx.cn/shop/Goods/showNorms
    // 显示商品规格
    public void showNormsNew(int goods_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/showNorms";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("goods_id", goods_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showNormsNew", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showNormsnew = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(GuiGe.class, response.getString("data")).toObjects();
//                        } else {
//                        JSONObject data = new JSONObject(response.getString("data"));
//                        message.obj = response.getString("data");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //  删除商品规格
    // http://waimai.023wx.cn/index.php/shop/Goods/deleteNorms
    public void deleteNorms(int goods_id, int norms_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "index.php/shop/Goods/deleteNorms";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("goods_id", goods_id);
            jsonObject.put("norms_id", norms_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("deleteNorms", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Hander_deleteNorms = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //  删除商品属性
    // http://waimai.023wx.cn/index.php/shop/Goods/deleteAttribute
    public void deleteAttribute(int attribute_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "index.php/shop/Goods/deleteAttribute";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("attribute_id", attribute_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("deleteAttribute", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Hander_deleteAttribute = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示商品属性列表
    public void showAttribute(int goods_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/showAttribute";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("goods_id", goods_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showAttribute", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Hander_showAttribute = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(GuiGe.class, response.getString("data")).toObjects();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //显示商品属性详情
    public void showAttrDetails(int attribute_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/showAttrDetails";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("attribute_id", attribute_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showAttrDetails", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Hander_showAttrDetails = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // http://waimai.023wx.cn/shop/Goods/addNorms
    // 添加商品规格接口
//    norms	是	string	规格
//    price	是	string	价格
//    stock	是	string	库存
//    lunch_box_price	是	string	餐盒价格
//    upc_code	是	string	条形码
//    sku_code	是	string	库存量单位码
//    position_code	是	string	位置码
    public void addNorms(int goods_id, String norms, String price, String stock, String lunch_box_price, String upc_code, String sku_code, String position_code, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/addNorms";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("goods_id", goods_id);
            jsonObject.put("norms", norms);
            jsonObject.put("price", price);
            jsonObject.put("stock", stock);
            jsonObject.put("lunch_box_price", lunch_box_price);
            jsonObject.put("upc_code", upc_code);
            jsonObject.put("sku_code", sku_code);
            jsonObject.put("position_code", position_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showNormsNew", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showNormsnew = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //添加/修改商品属性
    public void addAttribute(int goods_id, String shop_id, String name, String attribute, String attribute_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/addAttribute";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("goods_id", goods_id);
            jsonObject.put("shop_id", shop_id);
            jsonObject.put("name", name);
            jsonObject.put("attribute", attribute);
            jsonObject.put("attribute_id", attribute_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showNormsNew", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Hander_addAttribute = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Goods/updNorms
    // 修改商品规格
    public void updNorms(int goods_id, String norms, String price, String stock, String lunch_box_price, String upc_code, String sku_code, String position_code, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Goods/updNorms";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", goods_id);
            jsonObject.put("norms", norms);
            jsonObject.put("price", price);
            jsonObject.put("stock", stock);
            jsonObject.put("lunch_box_price", lunch_box_price);
            jsonObject.put("upc_code", upc_code);
            jsonObject.put("sku_code", sku_code);
            jsonObject.put("position_code", position_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updNorms", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showNormsnew = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(GuiGe.class, response.getString("data")).toObjects();
//                        } else {
//                        JSONObject data = new JSONObject(response.getString("data"));
                        message.obj = status.getString("message");
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Order/balanceWater
    // 余额流水数据
    public void balanceWater(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/balanceWater";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("balanceWater", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Hander_balanceWater = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");

                        message.obj = new JSONDeserialize<>(BalanceWater.class, response.getString("data")).toObject();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Order/balanceWaterList
    // 流水数据列表 余额流水类型 1支付宝充值 2微信充值 3广告推送 4提现 5余额充值 6余额进账
    public void balanceWaterList(int shopid, int style, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/balanceWaterList";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("type", style);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("balanceWaterList", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        Hander_balanceWater = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(BalanceWaterList.class, response.getString("data")).toObjects();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // http://waimai.023wx.cn/shop/Order/OrderHistoryList
    // 查询历史订单
    public void OrderHistoryList(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/OrderHistoryList";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updNorms", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        showNormsnew = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                        message.obj = new JSONDeserialize<>(all_order.class, response.getString("data")).toObjects();
//                        } else {
//                        JSONObject data = new JSONObject(response.getString("data"));
//                        message.obj = data.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // http://waimai.023wx.cn/shop/Order/TodayOrderList
    // 查询今日订单明细列表接口
    public void TodayOrderList(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/TodayOrderList";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updNorms", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        TodayOrderList = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
                            JSONObject data = new JSONObject(response.getString("data"));
                            message.obj = data.toString();
                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // 查询订单明细详情接口
    // http://waimai.023wx.cn/shop/Order/OrderListDe
    public void OrderListDe(int order_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Order/OrderListDe";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", order_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updNorms", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_OrderListDe = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(all_order.class, response.getString("data")).toObjects();
//                        } else {
                        JSONObject data = new JSONObject(response.getString("data"));
                        message.obj = data.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //    昨日顾客分析的总数据显示
// http://waimai.023wx.cn/shop/Customeranalysis/ShowYesterdayAnalysis
    public void ShowYesterdayAnalysis(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Customeranalysis/ShowYesterdayAnalysis";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("ShowYesterdayAnalysis", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_ShowYesterdayAnalysis = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(all_order.class, response.getString("data")).toObjects();
//                        } else {
                        JSONObject data = new JSONObject(response.getString("data"));
                        message.obj = data.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //  经营数据里昨日经营报告数据
    //  http://waimai.023wx.cn/shop/Operatingdata/yesterdayOperadata
    public void yesterdayOperadata(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Operatingdata/yesterdayOperadata";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("yesterdayOperadata", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_yesterdayOperadata = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(all_order.class, response.getString("data")).toObjects();
//                        } else {
                        JSONObject data = new JSONObject(response.getString("data"));
                        message.obj = data.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // http://waimai.023wx.cn/shop/Operatingdata/sevendayOperadata
    // 经营数据里七日经营报告数据接口
    public void sevendayOperadata(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Operatingdata/sevendayOperadata";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("sevendayOperadata", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_sevendayOperadata = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());

                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                        message.obj = new JSONDeserialize<>(all_order.class, response.getString("data")).toObjects();
//                        } else {
                        JSONObject data = new JSONObject(response.getString("data"));
                        message.obj = data.toString();
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //上传阿里云
    public void appSts(final Handler handler) {
        // 请求的URL地址
        String url = mHost + "api/Aliupload/appSts";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        Log.i("appSts", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_appSts = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                Message message = new Message();
                message.what = DATA_SUCCESS;
                message.obj = response.toString();
                handler.sendMessage(message);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Evaluate/JudgeRider
    // 商家评价骑手接口
//    uid	是	int	店铺id
//    rid	是	int	骑手id
//    oid	是	int	订单id
//    evaluate	是	int	星星数量
//    reason	是	string	评价内容
    public void JudgeRider(int uid, int rid, int order_main_id, int evaluate, String reason, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Evaluate/JudgeRider";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("rid", rid);
            jsonObject.put("order_main_id", order_main_id);
            jsonObject.put("evaluate", evaluate);
            jsonObject.put("reason", reason);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("JudgeRider", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_JudgeRider = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
//                        String data = status.getString("message");

                        String date = response.getString("data");
                        message.obj = date;
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                            message.obj = new JSONDeserialize<>(all_order.class, response.getString("data")).toObjects();
//                        } else {
//                            JSONObject data = new JSONObject(response.getString("data"));
//
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //店铺同意退款接口
    // http://waimai.023wx.cn/shop/Refund/approvalRefund
    // 主订单id
    public void approvalRefund(int orderid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Refund/approvalRefund";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", orderid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("approvalRefund", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_approvalRefund = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String date = status.getString("message");
                        message.obj = date;
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                            message.obj = new JSONDeserialize<>(all_order.class, response.getString("data")).toObjects();
//                        } else {
//                            JSONObject data = new JSONObject(response.getString("data"));
//
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        String date = status.getString("message");
                        message.obj = date;
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //店铺店铺拒绝退款
    // http://waimai.023wx.cn/shop/Refund/RefuseRefund
    // 主订单id
    public void RefuseRefund(int orderid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Refund/RefuseRefund";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", orderid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("RefuseRefund", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_RefuseRefund = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String date = status.getString("message");
                        message.obj = date;
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                            message.obj = new JSONDeserialize<>(all_order.class, response.getString("data")).toObjects();
//                        } else {
//                            JSONObject data = new JSONObject(response.getString("data"));
//
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        String date = status.getString("message");
                        message.obj = date;
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //http://waimai.023wx.cn/shop/Refund/showRefundGoods
    // 申请退款中显示数据
    public void showRefundGoods(int orderid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Refund/showRefundGoods";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", orderid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showRefundGoods", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_approvalRefund = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = response.getString("data");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                            message.obj = new JSONDeserialize<>(all_order.class, response.getString("data")).toObjects();
//                        } else {
//                            JSONObject data = new JSONObject(response.getString("data"));
//
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //申请退款订单显示new  /userapi/shop_refund/init
    public void showRefundGoodsinit(int orderid, final Handler handler) {
        // 请求的URL地址 userapi/shop_refund/init
        String url = mHost + "userapi/shop_refund/init";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_model_id", orderid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showRefundGoods", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_approvalRefundNew = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = response.getString("data");

                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //勾选退款项目new  userapi/refund/select
    public void showRefundGoodsSelect(int orderid, int goods_id, final Handler handler) {
        // 请求的URL地址 userapi/shop_refund/init
        String url = mHost + "userapi/shop_refund/select";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_model_id", orderid);
            jsonObject.put("order_goods_id", goods_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showRefundGoodsSelect", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_approvalRefundNew = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = response.getString("data");

                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    } //申请退款订单显示new  /userapi/refund/init

    // 取消勾选退款项目userapi/refund/unselect
    public void showRefundGoodsUnselect(int orderid, int goods_id, final Handler handler) {
        // 请求的URL地址 userapi/shop_refund/init
        String url = mHost + "userapi/shop_refund/unselect";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_model_id", orderid);
            jsonObject.put("order_goods_id", goods_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showRefundGoodsUnselect", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_approvalRefundNew = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = response.getString("data");

                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //增加退款项目数量userapi/refund/add
    public void showRefundGoodsAdd(int orderid, int goods_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "userapi/shop_refund/add";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_model_id", orderid);
            jsonObject.put("order_goods_id", goods_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showRefundGoodsAdd", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_approvalRefundNew = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = response.getString("data");

                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //减少退款项目数量
    public void showRefundGoodsReduce(int orderid, int goods_id, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "userapi/shop_refund/reduce";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_model_id", orderid);
            jsonObject.put("order_goods_id", goods_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showRefundGoodsReduce", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_approvalRefundNew = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = response.getString("data");
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //商家主动退款(全部/部分)
    // http://waimai.023wx.cn/shop/Refund/activeRefund
    public void activeRefund(int order_id, double money, String reason, String other_reason, JSONArray s, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Refund/activeRefund";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", order_id);
            jsonObject.put("money", money);
            jsonObject.put("reason", reason);
            jsonObject.put("other_reason", other_reason);
            jsonObject.put("goodlist", s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("activeRefund", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_JudgeRider = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String data = status.getString("message");
                        message.obj = data;
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = status.getString("message");
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //    添加/修改商家预定日期
    // http://waimai.023wx.cn/shop/Personal/addShopPerDate
    public void addShopPerDate(int shopid, String accept_date, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Personal/addShopPerDate";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("accept_date", accept_date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("addShopPerDate", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_addShopPerDate = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String data = status.getString("message");
                        message.obj = data;

                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //    http://waimai.023wx.cn/shop/Personal/addShopReminTime
    // 添加/修改商家预定单提醒时间接口
    public void addShopReminTime(int shopid, String remind_time, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Personal/addShopReminTime";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
            jsonObject.put("remind_time", remind_time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("addShopReminTime", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_addShopReminTime = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String data = status.getString("message");
                        message.obj = data;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                            message.obj = new JSONDeserialize<>(all_order.class, response.getString("data")).toObjects();
//                        } else {
//                            JSONObject data = new JSONObject(response.getString("data"));
//
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    // 修改商家是否接收预订单接口
    // http://waimai.023wx.cn/shop/Personal/updatePreOrderSet
    public void updatePreOrderSet(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Personal/updatePreOrderSet";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("updatePreOrderSet", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_updatePreOrderSet = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        String data = response.getString("data");
                        message.obj = data;
//                        String date = response.getString("data").replaceAll("[\\[\\]]", "");
//                        if (Valid.isNotNullOrEmpty(response.getString("data"))) {
//                            message.obj = new JSONDeserialize<>(all_order.class, response.getString("data")).toObjects();
//                        } else {
//                            JSONObject data = new JSONObject(response.getString("data"));
//
//                        }
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    //    查询商家预订单设置
//    http://waimai.023wx.cn/shop/Personal/showPreOrderSet
    public void showPreOrderSet(int shopid, final Handler handler) {
        // 请求的URL地址
        String url = mHost + "shop/Personal/showPreOrderSet";
        // 请求参数order_type
        ByteArrayEntity entity = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopid", shopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("showPreOrderSet", jsonObject.toString());
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 服务请求
        hander_showPreOrderSet = AsyncHttpClient.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i("LD.NET_DEBUG_INFO", response.toString());
                try {
                    JSONObject status = new JSONObject(response.getString("status"));
                    status.getString("code");
                    status.getString("message");
                    if (status.getString("code").equals("200")) {
                        Message message = new Message();
                        message.what = DATA_SUCCESS;
                        message.obj = new JSONDeserialize<>(ShowPreOrderSet.class, response.getString("data")).toObject();
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = DATA_FAILURE;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("LD.NET_DEBUG_INFO11", responseString);
                Message message = new Message();
                message.what = AsyncHttpClient.REQUEST_FAILURE;
                handler.sendMessage(message);
            }
        });
    }
}
