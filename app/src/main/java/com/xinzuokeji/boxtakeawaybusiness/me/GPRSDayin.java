package com.xinzuokeji.boxtakeawaybusiness.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ldnet.libzxing.activity.CaptureActivity;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;

public class GPRSDayin extends BaseActivity {
    private ImageButton header_back;
    Button bu_saoma_lianjie;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_gprs_dayin);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.arrow_left_n);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("GPRS打印机");
        bu_saoma_lianjie = findViewById(R.id.bu_saoma_lianjie);
        TextView tv_gprs_presentation = findViewById(R.id.tv_gprs_presentation);
        tv_gprs_presentation.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bu_saoma_lianjie.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.bu_saoma_lianjie://扫码连接
                Intent openCameraIntent = new Intent(this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                data.getDataString();
                Bundle extras = data.getExtras();
                Log.i("getDataString", data.getDataString() + "--------" + extras);
//                Bundle bundle = data.getExtras();
//                assert bundle != null;
//                String id = bundle.getString("result");
                break;
        }
    }
}
