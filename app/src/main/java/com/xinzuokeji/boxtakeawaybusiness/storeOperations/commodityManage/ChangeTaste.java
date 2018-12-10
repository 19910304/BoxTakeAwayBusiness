package com.xinzuokeji.boxtakeawaybusiness.storeOperations.commodityManage;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ChangeTasteAdapter;
import com.xinzuokeji.boxtakeawaybusiness.entities.Attribute;
import com.xinzuokeji.boxtakeawaybusiness.entities.GuiGe;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.netutil.JSONDeserialize;
import com.xinzuokeji.boxtakeawaybusiness.util.ACache;
import com.xinzuokeji.boxtakeawaybusiness.util.LoadingDialog;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/5.
 */
//增加口味
public class ChangeTaste extends BaseActivity {
    TextView tv_back, header_edit1, header_title;
    ImageButton header_back;
    ChangeTasteAdapter listViewAdapter;
    GridView gv_taste;
    //数据源
    private List<Attribute> mDate;
    private NetService netService;
    private String goodid, attribute_id;
    private EditText et_taste_title;
    private String taste_title = "";
    private String tasteContent;
    List<String> comtent;
    ACache aCache;
    private EditText et_taste_content1, et_taste_content2, et_taste_content3, et_taste_content4, et_taste_content5, et_taste_content6, et_taste_content7, et_taste_content8;
    private String taste_content1 = "", taste_content2 = "", taste_content3 = "", taste_content4 = "", taste_content5 = "", taste_content6 = "", taste_content7 = "", taste_content8 = "";
    Attribute attribute;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_change_taste);
        goodid = getIntent().getStringExtra("good_id");
        attribute_id = getIntent().getStringExtra("attribute_id");
        aCache = ACache.get(this);
        netService = new NetService(this);
        header_title.setText("添加/修改口味");
        tv_back.setVisibility(View.INVISIBLE);
        header_edit1.setVisibility(View.VISIBLE);
        header_edit1.setText("保存");
        mDate = new ArrayList<>();
        gv_taste = findViewById(R.id.gv_taste);
        comtent = new ArrayList<>();
        et_taste_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                taste_title = et_taste_title.getText().toString().trim();
            }
        });
        et_taste_content1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                taste_content1 = et_taste_content1.getText().toString().trim();

            }
        });
        et_taste_content2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                taste_content2 = et_taste_content2.getText().toString().trim();

            }
        });
        et_taste_content3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                taste_content3 = et_taste_content3.getText().toString().trim();

            }
        });
        et_taste_content4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                taste_content4 = et_taste_content4.getText().toString().trim();

            }
        });
        et_taste_content5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                taste_content5 = et_taste_content5.getText().toString().trim();

            }
        });
        et_taste_content6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                taste_content6 = et_taste_content6.getText().toString().trim();

            }
        });
        et_taste_content7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                taste_content7 = et_taste_content7.getText().toString().trim();

            }
        });
        et_taste_content8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                taste_content8 = et_taste_content8.getText().toString().trim();

            }
        });
        if (!attribute_id.equals("0")) {
            netService.showAttrDetails(Integer.parseInt(attribute_id), tasteHandler);
        } else {
            attribute_id = "0";
            netService.showAttrDetails(Integer.parseInt(attribute_id), tasteHandler);
        }

    }


    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        header_edit1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back://返回
                finish();
                aCache.clear();
                break;
            case R.id.header_edit1://保存
                if (Valid.isNotNullOrEmpty(taste_title)) {
                    if (!Valid.isNotNullOrEmpty(taste_content1) &&
                            !Valid.isNotNullOrEmpty(taste_content2) && !Valid.isNotNullOrEmpty(taste_content3)
                            && !Valid.isNotNullOrEmpty(taste_content4) && !Valid.isNotNullOrEmpty(taste_content5) && !Valid.isNotNullOrEmpty(taste_content6) && !Valid.isNotNullOrEmpty(taste_content7) && !Valid.isNotNullOrEmpty(taste_content8)) {
                        showTip("请输入具体分类", Toast.LENGTH_SHORT);
                    } else {
                        comtent.add(0, taste_content1);
                        comtent.add(1, taste_content2);
                        comtent.add(2, taste_content3);
                        comtent.add(3, taste_content4);
                        comtent.add(4, taste_content5);
                        comtent.add(5, taste_content6);
                        comtent.add(6, taste_content7);
                        comtent.add(7, taste_content8);
                        for (int i = 0; i < comtent.size(); i++) {
                            if (Valid.isNotNullOrEmpty(comtent.get(i))) {
                                if (Valid.isNotNullOrEmpty(tasteContent)) {
                                    tasteContent = tasteContent + "," + comtent.get(i);
                                } else {
                                    tasteContent = comtent.get(i);
                                }
                            }
                        }
                        loading();
                        if (Valid.isNotNullOrEmpty(goodid)) {
                            netService.addAttribute(Integer.parseInt(goodid), String.valueOf(GetstoreId()), taste_title, tasteContent, attribute_id, showUntreated);
                        } else {
                            netService.addAttribute(Integer.parseInt(goodid), String.valueOf(GetstoreId()), taste_title, tasteContent, attribute_id, showUntreated);
                        }
                    }
                } else {
                    showTip("请输入属性名称", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    // 监听返回按键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            //绑定返回按键和返回按钮
            header_back = findViewById(R.id.header_back);
            if (header_back != null) {
                header_back.performClick();
            }

            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    LoadingDialog.Builder loadBuilder;
    public LoadingDialog dialog = null;

    private void loading() {
        loadBuilder = new LoadingDialog.Builder(this)
                .setMessage("")
                .setCancelable(true)
                .setCancelOutside(true);
        dialog = loadBuilder.create();
        dialog.show();
    }

    //显示属性信息
    private final ShowTasteHandler tasteHandler = new ShowTasteHandler(this);

    private class ShowTasteHandler extends Handler {
        private final WeakReference<ChangeTaste> mActivity;

        public ShowTasteHandler(ChangeTaste activity) {
            mActivity = new WeakReference<ChangeTaste>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ChangeTaste activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 2000:
                        JSONObject jsonObject;
                        Gson gson = new Gson();
                        GuiGe guiGe = new GuiGe();

                        try {
                            jsonObject = new JSONObject(msg.obj.toString());
                            guiGe = new JSONDeserialize<>(GuiGe.class, jsonObject.getString("data")).toObject();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (Valid.isNotNullOrEmpty(guiGe.name)) {
                            activity.et_taste_title.setText(guiGe.name);
                        }
                        if (Valid.isNotNullOrEmpty(guiGe.attribute)) {
                            //先转JsonObject
                            JsonObject jsonObject1 = new JsonParser().parse(msg.obj.toString()).getAsJsonObject();
                            jsonObject1.getAsJsonObject("data");
                            //再转JsonArray 加上数据头
                            JsonArray jsonArray = jsonObject1.getAsJsonObject("data").getAsJsonArray("attribute");
                            //for循环遍历JsonArray
                            activity.mDate.clear();

                            for (int i = 0; i < jsonArray.size(); i++) {
                                attribute = new Attribute();
                                attribute.setContent(jsonArray.get(i).getAsString());
                                activity.mDate.add(attribute);
                                Log.i("attribute", "44" + attribute.getContent());
                                if (Valid.isNotNullOrEmpty(attribute.getContent())) {
                                    if (i == 0) {
                                        taste_content1 = attribute.getContent();
                                        activity.et_taste_content1.setText(attribute.getContent());
                                    }
                                    if (i == 1) {
                                        taste_content2 = attribute.getContent();
                                        activity.et_taste_content2.setText(attribute.getContent());
                                    }
                                    if (i == 2) {
                                        taste_content3 = attribute.getContent();
                                        activity.et_taste_content3.setText(attribute.getContent());
                                    }
                                    if (i == 3) {
                                        taste_content4 = attribute.getContent();
                                        activity.et_taste_content4.setText(attribute.getContent());
                                    }
                                    if (i == 4) {
                                        taste_content5 = attribute.getContent();
                                        activity.et_taste_content5.setText(attribute.getContent());
                                    }
                                    if (i == 5) {
                                        taste_content6 = attribute.getContent();
                                        activity.et_taste_content6.setText(attribute.getContent());
                                    }
                                    if (i == 6) {
                                        taste_content7 = attribute.getContent();
                                        activity.et_taste_content7.setText(attribute.getContent());
                                    }
                                    if (i == 7) {
                                        taste_content8 = attribute.getContent();
                                        activity.et_taste_content8.setText(attribute.getContent());
                                    }

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
        }
    }

    //
    private final AddTasteHandler showUntreated = new AddTasteHandler(this);

    private static class AddTasteHandler extends Handler {
        private final WeakReference<ChangeTaste> mActivity;

        public AddTasteHandler(ChangeTaste activity) {
            mActivity = new WeakReference<ChangeTaste>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ChangeTaste activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 2000:
                        // 正在显示就销毁
                        if (activity.dialog.isShowing()) {
                            activity.dialog.dismiss();
                        }
                        activity.finish();
                        activity.aCache.clear();
                        activity.tasteContent = "";
                        activity.showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                        break;
                    case 2001:
                        // 正在显示就销毁
                        if (activity.dialog.isShowing()) {
                            activity.dialog.dismiss();
                        }
                        activity.showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                        break;
                    case 1001:
                        // 正在显示就销毁
                        if (activity.dialog.isShowing()) {
                            activity.dialog.dismiss();
                        }
//                        Toast.makeText(activity.ChangeTaste.this, activity.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);

            }
        }
    }

}
