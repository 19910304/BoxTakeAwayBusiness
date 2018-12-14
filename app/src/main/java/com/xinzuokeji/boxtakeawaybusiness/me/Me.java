package com.xinzuokeji.boxtakeawaybusiness.me;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CommonCallback;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.StoreInfo;
import com.xinzuokeji.boxtakeawaybusiness.me.SoreSettings.StoreSettingNew;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.registrationShop.ApplicationRecord;
import com.xinzuokeji.boxtakeawaybusiness.registrationShop.PassWordLogin;
import com.xinzuokeji.boxtakeawaybusiness.util.AletDialog;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class Me extends Fragment implements View.OnClickListener {
    LinearLayout ll_store_setting, ll_dayin_setting, ll_message_setting, ll_order_setting,
            ll_merchant_service_enter, ll_feedback, ll_me_zh, ll_exit_login, ll_store_apply;
    ImageButton img_erweima, switcher_store;
    //    商户头像i
    private ImageView circleImageView;
    private TextView tv_shop_name, tv_account_name, tv_operating_state, tv_operating_state_new;
    //    开启状态
    private Switch sw_bus_dispatching;
    private NetService netService;
    int user_id, storeId;
    //手机号
    private String phone;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private CanRefreshLayout crl_information;
    private Handler mHandler;
    // 配送方式
    private LinearLayout ll_peisong_style;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 加载布局
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view, inflater, container);
        initEvent(view);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(View view, LayoutInflater inflater, ViewGroup container) {
//        crl_information = (CanRefreshLayout) view.findViewById(R.id.crl_information);
        netService = new NetService(getActivity());
        mHandler = new Handler();
        sp = GSApplication.getInstance().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //取得user_id
        user_id = sp.getInt("user_id", 0);
        storeId = sp.getInt("storeId", 0);
        phone = sp.getString("user_phone", "");
        String shopName = sp.getString("shop_name", "");//店铺名字
        String accountName = sp.getString("account_name", "");//用户名字
        String headImg = sp.getString("storeIcon", "");
        ll_store_setting = view.findViewById(R.id.ll_store_setting);
        ll_dayin_setting = view.findViewById(R.id.ll_dayin_setting);
        ll_message_setting = view.findViewById(R.id.ll_message_setting);
        ll_order_setting = view.findViewById(R.id.ll_order_setting);
        ll_merchant_service_enter = view.findViewById(R.id.ll_merchant_service_enter);
        ll_feedback = view.findViewById(R.id.ll_feedback);
        ll_me_zh = view.findViewById(R.id.ll_me_zh);
        // 配送方式
        ll_peisong_style = view.findViewById(R.id.ll_peisong_style);
        img_erweima = view.findViewById(R.id.img_erweima);
        switcher_store = view.findViewById(R.id.switcher_store);//切换门店
        ll_exit_login = view.findViewById(R.id.ll_exit_login);//退出登录
        ll_store_apply = view.findViewById(R.id.ll_store_apply);//店铺申请
        //    商户头像
        circleImageView = view.findViewById(R.id.btn_me_thumbnail);
        tv_shop_name = view.findViewById(R.id.tv_store_name);
        tv_account_name = view.findViewById(R.id.tv_sogo_name);
        // 本地获取用户名字
        tv_shop_name.setText(shopName);
        if (Valid.isNotNullOrEmpty(accountName)) {
            tv_account_name.setText(accountName);

        }
        ImageLoader.getInstance().displayImage(headImg, circleImageView, GSApplication.getInstance().optionshead);
        tv_operating_state = view.findViewById(R.id.tv_operating_state);
        tv_operating_state_new = view.findViewById(R.id.tv_operating_state_new);
        //调取页面信息
        netService.showDataMe(user_id, storeId, showDataMe);
    }

    private void initEvent(View view) {
        ll_store_setting.setOnClickListener(this);
        ll_dayin_setting.setOnClickListener(this);
        ll_message_setting.setOnClickListener(this);
        ll_order_setting.setOnClickListener(this);
        img_erweima.setOnClickListener(this);
        ll_merchant_service_enter.setOnClickListener(this);
        ll_feedback.setOnClickListener(this);
        ll_me_zh.setOnClickListener(this);
        ll_exit_login.setOnClickListener(this);
        switcher_store.setOnClickListener(this);
        ll_store_apply.setOnClickListener(this);
        ll_peisong_style.setOnClickListener(this);
        // 下拉刷新，上拉加载
//        crl_information.setOnRefreshListener(this);
//        crl_information.setOnLoadMoreListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        String storeState = sp.getString("OperatingState", "");
        if (storeState.equals("未营业")) {
            tv_operating_state.setText("未营业");
            tv_operating_state_new.setText("未营业");
        } else {
            tv_operating_state.setText("营业中");
            tv_operating_state_new.setText("营业中");
        }
        String storeIcon = sp.getString("storeIcon", "");
        if (Valid.isNotNullOrEmpty(storeIcon)) {
            ImageLoader.getInstance().displayImage(storeIcon, circleImageView, GSApplication.getInstance().optionshead);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switcher_store://门店切换
                Intent intentswitcher_store = new Intent(getActivity(), StoreSetting.class);
                startActivity(intentswitcher_store);
                break;
            case R.id.ll_store_setting://门店设置
                Intent intentStoreSetting = new Intent(getActivity(), StoreSettingNew.class);
                startActivity(intentStoreSetting);
                break;
            case R.id.ll_dayin_setting://打印设置
                Intent intent = new Intent(getActivity(), DayinSetting.class);
                startActivity(intent);

                break;
            case R.id.ll_message_setting://消息设置
                Intent intentMessageSetting = new Intent(getActivity(), MessageSetting.class);
                startActivity(intentMessageSetting);
                break;
            case R.id.ll_order_setting://订单设置
                Intent intentOrder = new Intent(getActivity(), OrderSetting.class);
                startActivity(intentOrder);
                break;
            case R.id.img_erweima://二维码
                Intent intent_erweima = new Intent(getActivity(), Erweima.class);
                startActivity(intent_erweima);
                break;
            case R.id.ll_merchant_service_enter://商家服务中心
                Intent intentServiceEnter = new Intent(getActivity(), MerchantServiceEnter.class);
                startActivity(intentServiceEnter);
                break;
            case R.id.ll_feedback://意见
                Intent intentfeedback = new Intent(getActivity(), Feedback.class);
                startActivity(intentfeedback);
                break;
            case R.id.ll_me_zh://账号
                Intent intentMeAccount = new Intent(getActivity(), MeAccount.class);
                startActivity(intentMeAccount);
                break;
            case R.id.ll_store_apply://申请记录
                Intent intentStoreApply = new Intent(getActivity(), ApplicationRecord.class);
                startActivity(intentStoreApply);
                break;
            case R.id.ll_exit_login://退出登录
                netService.signOut(user_id, outLoginHandler);
                break;
            case R.id.ll_peisong_style:
                //对话框
                LogCall();
                break;
            default:
                break;
        }
    }

    //拨打业务经理电话
    private void LogCall() {
        final AletDialog dialog = new AletDialog(getActivity(), getString(R.string.change_shipping_method), getString(R.string.shipping_contents), getString(R.string.cancel), getString(R.string.call_jingli), new AletDialog.OnAlertDialogListener() {
            @Override
            public void Confirm() {
                diallPhone("123");

            }
        });
        dialog.getWindow().setWindowAnimations(R.style.DialogOutAndInStyle);   //设置dialog的显示动画
        dialog.show();


    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    @SuppressLint("HandlerLeak")
    Handler showDataMe = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        com.xinzuokeji.boxtakeawaybusiness.entities.StoreInfo storeInfo = (StoreInfo) msg.obj;
                        if (Valid.isNotNullOrEmpty(storeInfo.shopid)) {
                            editor = sp.edit();
                            editor.putInt("storeId", Integer.parseInt(storeInfo.shopid));
                            editor.putInt("user_id", Integer.parseInt(storeInfo.bid));
                            editor.putString("shop_name", storeInfo.shop_name);
                            editor.putString("account_name", storeInfo.account_name);
                            editor.putString("storeIcon", storeInfo.head_img);
                            editor.apply();
                            tv_shop_name.setText(storeInfo.shop_name);
                            tv_account_name.setText(storeInfo.account_name);

                            if (storeInfo.shop_status.equals("0")) {
                                tv_operating_state.setText("未营业");
                                tv_operating_state_new.setText("未营业");
                                editor.putString("OperatingState", "未营业");
                            } else {
                                tv_operating_state.setText("营业中");
                                tv_operating_state_new.setText("营业中");
                                editor.putString("OperatingState", "营业中");
                            }

                            if (Valid.isNotNullOrEmpty(storeInfo.head_img)) {
                                ImageLoader.getInstance().displayImage(storeInfo.head_img, circleImageView, GSApplication.getInstance().optionshead);
                            }

                        }
                    }

                    break;
                case 2001:

                    break;
                case 1001:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    //退出登录
    private final OutLoginHandler outLoginHandler = new OutLoginHandler(this);

    private static class OutLoginHandler extends Handler {
        private final WeakReference<Me> mActivity;

        private OutLoginHandler(Me activity) {
            mActivity = new WeakReference<Me>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Me activity = mActivity.get();
            switch (msg.what) {
                case 2000:
                    Toast.makeText(activity.getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    // 清除数据
                    SharedPreferences sp = GSApplication.getInstance().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.apply();
                    //判断getActivity()不为空时执行runOnUiThread ，防止空指针
                    if (activity.getActivity() != null) {
                        activity.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GSApplication.getInstance().pushService.unbindAccount(new CommonCallback() {
                                    @Override
                                    public void onSuccess(String s) {
                                        Log.i("unphone", "unphone");
                                    }

                                    @Override
                                    public void onFailed(String s, String s1) {
                                        Log.i("unphone", "unphone111");
                                    }
                                });
                            }
                        });
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Objects.requireNonNull(activity.getActivity()).finish();
                    } else {
                        activity.getActivity().finish();
                    }
                    // 退出登录返回到登录页面
                    Intent intentPassWordLogin = new Intent(activity.getActivity(), PassWordLogin.class);
                    activity.startActivity(intentPassWordLogin);
                    break;
                case 2001:
                    Toast.makeText(activity.getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 1001:
                    Toast.makeText(activity.getActivity(), activity.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }


//    @Override
//    public void onLoadMore() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                crl_information.loadMoreComplete();
//            }
//        }, 1000);
//    }
//
//    @Override
//    public void onRefresh() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                crl_information.refreshComplete();
//            }
//        }, 1000);
//    }
}
