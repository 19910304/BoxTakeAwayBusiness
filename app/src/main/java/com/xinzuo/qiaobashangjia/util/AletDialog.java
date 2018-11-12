package com.xinzuo.qiaobashangjia.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xinzuo.qiaobashangjia.R;


/**
 * Created
 */
public class AletDialog extends Dialog {
    private Context mContext;
    private String mTitle, mMessage, positiveButtonText, negativeButtonText;
    private TextView dialog_alert_message, dialog_alert_title;
    private Button dialog_button_cancel, dialog_button_comfirm;
    private OnAlertDialogListener mComfirmClickListener;

    public AletDialog(Context context, String title, String content, String cancelText, String confrimText, OnAlertDialogListener comfirmClickListener) {
        super(context, R.style.dialog_fullscreen);
        mContext = context;
        mTitle = title;
        mMessage = content;
        positiveButtonText = cancelText;
        negativeButtonText = confrimText;
        mComfirmClickListener = comfirmClickListener;
    }

    //onCreate方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert);

        //标题
        dialog_alert_title = (TextView) findViewById(R.id.tv_dialog_title);
        dialog_alert_title.setText(mTitle);

        //提示信息
        dialog_alert_message = (TextView) findViewById(R.id.tv_dialog_content);
        dialog_alert_message.setText(mMessage);

        //取消按钮
        dialog_button_cancel = (Button) findViewById(R.id.btn_dialog_left);
        dialog_button_cancel.setText(positiveButtonText);
        dialog_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AletDialog.this.cancel();
            }
        });

        //确定按钮
        dialog_button_comfirm = (Button) findViewById(R.id.btn_dialog_right);
        dialog_button_comfirm.setText(negativeButtonText);
        dialog_button_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AletDialog.this.dismiss();
                mComfirmClickListener.Confirm();
            }
        });
    }

    //
    public interface OnAlertDialogListener {
        void Confirm();


    }
}
