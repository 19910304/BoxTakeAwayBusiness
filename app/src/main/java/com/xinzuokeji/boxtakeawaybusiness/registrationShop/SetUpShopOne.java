package com.xinzuokeji.boxtakeawaybusiness.registrationShop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.Shopname;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.ACache;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

//开店第一步
public class SetUpShopOne extends BaseActivity {
    private ImageButton header_back;
    Button bt_next_shop;
    LinearLayout ll_selector_store;
    NetService netService;
    EditText et_shop_name;
    TextView tv_shop_type;
    //    更改店铺信息
    private int shop_id, shop_typeid;
    private String shop_name = "";
    ACache aCache;
    //
    public static Activity mActivity;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_set_up_shop_one);
        mActivity = this;
        aCache = ACache.get(this);
        netService = new NetService(this);
//        接收店铺id
        LinearLayout ll_action_header = findViewById(R.id.ll_action_header);
        ll_action_header.setBackgroundColor(getResources().getColor(R.color.red));
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        ImageView img_header_back = findViewById(R.id.img_header_back);
        img_header_back.setBackgroundResource(R.mipmap.back_white);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("店铺介绍");
        textView.setTextColor(getResources().getColor(R.color.white));
        bt_next_shop = findViewById(R.id.bt_next_shop);
        ll_selector_store = findViewById(R.id.ll_selector_store);
        et_shop_name = findViewById(R.id.et_shop_name);//店铺名字
        et_shop_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                shop_name = et_shop_name.getText().toString().trim();
            }
        });
        tv_shop_type = findViewById(R.id.tv_shop_type);//店铺类型
        netService.showShopname(getChangeStoreId(), showShopname);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_next_shop.setOnClickListener(this);
        ll_selector_store.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //从缓存中获取经营id和经营名字
        String shoptypeid = aCache.getAsString("manage_type_id");
        Log.i("typeid", shoptypeid + "--");
        if (Valid.isNotNullOrEmpty(shoptypeid)) {
            shop_typeid = Integer.parseInt(shoptypeid);
            aCache.put("manage_type_id", shoptypeid);
        }
        String typename = aCache.getAsString("typename");
        //  如果缓存中不存在，则从服务器获取
        if (Valid.isNotNullOrEmpty(typename)) {
            tv_shop_type.setText(typename);
            aCache.put("typename", typename);

        } else {
//            netService.showShopname(getChangeStoreId(), Second_showShopname);
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.ll_selector_store://跳转选择分类
                String typename = aCache.getAsString("typename");
                String manage_type_id = aCache.getAsString("manage_type_id");
                HashMap<String, String> extras = new HashMap<>();
                //如果没有缓存传空字符串
                if (Valid.isNotNullOrEmpty(typename)) {
                    extras.put("typeName", typename);
                    extras.put("manage_type_id", manage_type_id);
                } else {
                    extras.put("typeName", "");
                }
//                Log.i("typename", typename + "--");
                try {
                    gotoActivity(SelectorStoreType.class.getName(), extras);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_next_shop://修改店铺类型和名字
                //如果店铺状态是1为编辑,3为新建
                if (Valid.isNotNullOrEmpty(shop_name) && shop_typeid != 0) {
                    if (BaseActivity.getRecordStype().equals("1")) {
                        netService.updShop(getChangeStoreId(), shop_typeid, shop_name, updShop);
                    } else {
                        netService.addShop(getuserinfoId(), shop_typeid, shop_name, addShop);
                    }
                } else {
                    showTip("请输入店铺名称和店铺类型", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler updShop = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
//                        aCache.remove("manage_type_id");
//                        aCache.remove("typename");
                        try {
                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
                            JSONObject status = jsonObject.getJSONObject("status");
                            String code = status.getString("code");
                            String message = status.getString("message");
                            if (code.equals("200")) {
                                // 成功之后将店铺id存起来
                                int storeid = jsonObject.getInt("data");
                                setChangeStoreId(storeid);
                                try {
                                    gotoActivity(SetUpShopTwo.class.getName(), null);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showTip(message, Toast.LENGTH_SHORT);
                            }
//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler addShop = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
//                        aCache.remove("manage_type_id");
//                        aCache.remove("typename");
                        try {
                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
                            int storeid = jsonObject.getInt("data");
                            setChangeStoreId(storeid);
                            JSONObject status = jsonObject.getJSONObject("status");
                            String code = status.getString("code");
                            String message = status.getString("message");
                            if (code.equals("200")) {
                                BaseActivity.setRecordStype("3");
                                try {
                                    gotoActivity(SetUpShopTwo.class.getName(), null);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showTip(message, Toast.LENGTH_SHORT);
                            }
//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    //    店铺名字和类型
    @SuppressLint("HandlerLeak")
    Handler showShopname = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        com.xinzuokeji.boxtakeawaybusiness.entities.Shopname shopname = (Shopname) msg.obj;
                        shop_id = Integer.parseInt(shopname.id);//店铺id
                        shop_typeid = Integer.parseInt(shopname.tid);//经营类型id，应该从选择页面获取
                        et_shop_name.setText(shopname.shop_name);
                        tv_shop_type.setText(shopname.typename);
                        aCache.put("typename", shopname.typename);
                        aCache.put("manage_type_id", shopname.tid);

                    }
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    //    店铺名字和类型
    @SuppressLint("HandlerLeak")
    Handler Second_showShopname = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        com.xinzuokeji.boxtakeawaybusiness.entities.Shopname shopname = (Shopname) msg.obj;
                        shop_id = Integer.parseInt(shopname.id);//店铺id
                        shop_typeid = Integer.parseInt(shopname.tid);//经营类型id，应该从选择页面获取
                        et_shop_name.setText(shopname.shop_name);
                        tv_shop_type.setText(shopname.typename);
                        aCache.put("typename", shopname.typename);
                        aCache.put("manage_type_id", shopname.tid);

                    }
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
