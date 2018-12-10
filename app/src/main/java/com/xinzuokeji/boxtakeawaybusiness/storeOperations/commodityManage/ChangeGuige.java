package com.xinzuokeji.boxtakeawaybusiness.storeOperations.commodityManage;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.entities.GuiGe;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

//添加/修改规格
public class ChangeGuige extends BaseActivity {
    private ImageButton header_back;
    private NetService netService;
    private EditText et_guige, et_price, et_kucu, et_lunch_box_price, et_lunch_box_num, et_upc, et_sku, et_position_code;
    public String guige, price, kucu, lunch_box_price, lunch_box_num, upc = "", sku = "", position_code = "";
    private TextView save_guige;
    String goodid;
    GuiGe guiGe;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_changeguige);
        goodid = getIntent().getStringExtra("good_id");
        guiGe = (GuiGe) getIntent().getSerializableExtra("guige");
//        Log.i("goodid", goodid);
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("添加/修改规格");
        //保存
        save_guige = findViewById(R.id.header_edit1);
        save_guige.setVisibility(View.VISIBLE);
        save_guige.setText("保存");

        et_guige = findViewById(R.id.et_guige);
        et_price = findViewById(R.id.et_price);
        et_kucu = findViewById(R.id.et_kucu);
        et_lunch_box_price = findViewById(R.id.et_lunch_box_price);
        et_lunch_box_num = findViewById(R.id.et_lunch_box_num);
        et_upc = findViewById(R.id.et_upc);
        et_sku = findViewById(R.id.et_sku);
        et_position_code = findViewById(R.id.et_position_code);
        //  显示规格信息
        if (guiGe != null) {
            et_guige.setText(guiGe.norms);
            et_price.setText(guiGe.price);
            et_kucu.setText(guiGe.stock);
            et_lunch_box_price.setText(guiGe.lunch_box_price);
            et_upc.setText(guiGe.upc_code);
            et_sku.setText(guiGe.sku_code);
            et_position_code.setText(guiGe.position_code);
            guige = guiGe.norms;
            price = guiGe.price;
            kucu = guiGe.stock;
            lunch_box_price = guiGe.lunch_box_price;
            upc = guiGe.upc_code;
            sku = guiGe.sku_code;
            position_code = guiGe.position_code;
        }

        et_guige.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                guige = et_guige.getText().toString().trim();
            }
        });
        et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                price = et_price.getText().toString().trim();
            }
        });
        et_kucu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                kucu = et_kucu.getText().toString().trim();
            }
        });
        et_lunch_box_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                lunch_box_price = et_lunch_box_price.getText().toString().trim();
            }
        });
        et_lunch_box_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                lunch_box_num = et_lunch_box_num.getText().toString().trim();
            }
        });
        et_upc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                upc = et_upc.getText().toString().trim();
            }
        });
        et_sku.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                sku = et_sku.getText().toString().trim();
            }
        });
        et_position_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                position_code = et_position_code.getText().toString().trim();
            }
        });
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        save_guige.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.header_edit1://保存
                if (Valid.isNotNullOrEmpty(guige) && Valid.isNotNullOrEmpty(price) && Valid.isNotNullOrEmpty(kucu) && Valid.isNotNullOrEmpty(lunch_box_price)) {
                    if (guiGe != null) {
                        // 修改规格
                        netService.updNorms(Integer.parseInt(guiGe.id), guige, price, kucu, lunch_box_price, upc, sku, position_code, addNorms);
                    } else {
                        //添加规格
                        netService.addNorms(Integer.parseInt(goodid), guige, price, kucu, lunch_box_price, upc, sku, position_code, addNorms);
                    }
                } else {
                    showTip("请填写完整的规格信息", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler addNorms = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
