package com.xinzuokeji.boxtakeawaybusiness.storeOperations.commodityManage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.List;

//编辑菜单
public class CaidanBiande extends BaseActivity {
    private TextView header_back;
    private LinearLayout ll_header_back, ll_delete_menu;
    private Button updMenu;
    private EditText etMenuName, et_menu_description;
    private NetService netService;
    private String mMenuid, menuName, menuDescribe;
    private List<GoodsMenu> mDate = new ArrayList<>();

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_caidan_bianjide);
//        mDate = (List<GoodsMenu>) getIntent().getSerializableExtra("mdateMenu");
        mMenuid = getIntent().getStringExtra("menu_id");
        menuName = getIntent().getStringExtra("menu_name");
        menuDescribe = getIntent().getStringExtra("describe");
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.back_bl);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("编辑菜单");
        //返回
        ll_header_back = findViewById(R.id.ll_header_back);
        // 保存
        updMenu = findViewById(R.id.upd_save);
        // 删除
        ll_delete_menu = findViewById(R.id.ll_delete_menu);
        etMenuName = findViewById(R.id.et_menu_name);
        etMenuName.setText(menuName);
        et_menu_description = findViewById(R.id.et_menu_description);
        et_menu_description.setText(menuDescribe);
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
        updMenu.setOnClickListener(this);
        ll_delete_menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_header_back:
                finish();
                break;
            case R.id.upd_save://保存
                if (Valid.isNotNullOrEmpty(etMenuName.getText().toString().trim()) && Valid.isNotNullOrEmpty(et_menu_description.getText().toString().trim())) {
                    netService.updMenu(Integer.parseInt(mMenuid), GetstoreId(), etMenuName.getText().toString().trim(), et_menu_description.getText().toString().trim(), Hander_updMenu);
                } else {
                    showTip("请填写菜单信息！", Toast.LENGTH_SHORT);
                }

                break;
            case R.id.ll_delete_menu://删除
                dialogShow2();

                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler Hander_updMenu = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    Intent intent = new Intent(CaidanBiande.this, CaidanBianji.class);
//                    intent.putExtra("mdateMenu", (Serializable) mDate);
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
    @SuppressLint("HandlerLeak")
    Handler Hander_delMenu = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    Intent intent = new Intent(CaidanBiande.this, CaidanBianji.class);
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
    }

    private void dialogShow2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.manage_dialog, null);
        TextView content = (TextView) v.findViewById(R.id.dialog_content);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        builder.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();
//        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                netService.delMenu(Integer.parseInt(mMenuid), Hander_delMenu);
                dialog.dismiss();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();

            }
        });
    }
}
