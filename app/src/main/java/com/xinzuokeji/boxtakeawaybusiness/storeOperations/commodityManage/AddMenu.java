package com.xinzuokeji.boxtakeawaybusiness.storeOperations.commodityManage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.GoodsMenu;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/11.
 */
//新增菜单
public class AddMenu extends BaseActivity {

    private LinearLayout ll_header_back;
    private Button bt_save_menu;
    private EditText etMenuName, et_menu_description;
    private NetService netService;
    private List<GoodsMenu> mDate = new ArrayList<>();

    @Override

    public void initView() {
        super.initView();
        setContentView(R.layout.activity_add_menu);
        mDate = (List<GoodsMenu>) getIntent().getSerializableExtra("mdateMenu");
        netService = new NetService(this);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("新增菜单");
        ll_header_back = findViewById(R.id.ll_header_back);
        bt_save_menu = findViewById(R.id.bt_save_menu);
        etMenuName = findViewById(R.id.et_menu_name);
        et_menu_description = findViewById(R.id.et_menu_description);
//        菜单名称
        etMenuName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                etMenuName.getText().toString().trim();
                etMenuName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});  //其中4最大输入字数
            }
        });
//        菜单描述
        et_menu_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_menu_description.getText().toString().trim();
            }
        });
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ll_header_back.setOnClickListener(this);
        bt_save_menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_header_back:
                finish();
                break;
            case R.id.bt_save_menu:
                if (Valid.isNotNullOrEmpty(etMenuName.getText().toString().trim()) && Valid.isNotNullOrEmpty(et_menu_description.getText().toString().trim())) {
                    netService.addMenu(GetstoreId(), etMenuName.getText().toString().trim(), et_menu_description.getText().toString().trim(), addMenu);
                } else {
                    showTip("请填写菜单信息！", Toast.LENGTH_SHORT);
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
            LinearLayout header_back = findViewById(R.id.ll_header_back);
            if (header_back != null) {
                header_back.performClick();
            }

            return true;
        }
        return super.dispatchKeyEvent(event);
    }   // 显示对应菜单下的商品

    @SuppressLint("HandlerLeak")
    Handler addMenu = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    Intent intent = new Intent(AddMenu.this, CaidanBianji.class);
                    intent.putExtra("mdateMenu", (Serializable) mDate);
                    if (getPackageManager().resolveActivity(intent, 0) != null) {
                        CaidanBianji.mActivity.finish();
                        // 说明系统中不存在这个activity
                    }
                    startActivity(intent);
                    finish();
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
