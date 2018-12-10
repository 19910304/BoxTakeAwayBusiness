package com.xinzuokeji.boxtakeawaybusiness.storeOperations.caiwu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xinzuokeji.boxtakeawaybusiness.R;


public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI api;
    String appid = "wx90521f47ed8d03c4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_wxpay_entry);
        api = WXAPIFactory.createWXAPI(this, appid);
//        api.registerApp(appid);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.i("baseReq", baseReq.getType() + "");
        switch (baseReq.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }

//
//        String msg = "";
//        /*
//         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
//         */
//        final String str = String.valueOf(baseReq.getType());
//        if (str.equalsIgnoreCase("success")) {
//            msg = "充值成功！";
//        } else if (str.equalsIgnoreCase("fail")) {
//            msg = "充值失败！";
//        } else if (str.equalsIgnoreCase("cancel")) {
//            msg = "用户取消了充值";
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("充值结果通知");
//        builder.setMessage(msg);
//        builder.setInverseBackgroundForced(true);
////         builder.setCustomTitle();
//        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//                if (str.equalsIgnoreCase("success")) {
//                    Intent intent = new Intent(WXPayEntryActivity.this, Recharge.class);
//                    startActivity(intent);
//                    finish();
//                }
//
//
//            }
//        });
//        builder.create().show();
    }
    //...


    @Override
    public void onResp(BaseResp baseResp) {
        Log.i("ssss", baseResp.getType() + "");
        if (baseResp.errCode == 0) {
            Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(WXPayEntryActivity.this, Rearch.class);
            intent.putExtra("PAYCODE", baseResp.errCode + "");
            startActivity(intent);
            finish();
        }
        if (baseResp.errCode == -2) {
            Toast.makeText(this, "取消支付", Toast.LENGTH_SHORT).show();
            finish();

        }
        if (baseResp.errCode == -1) {
            Toast.makeText(this, "充值失败", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
