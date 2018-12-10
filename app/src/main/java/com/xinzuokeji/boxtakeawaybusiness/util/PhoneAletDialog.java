package com.xinzuokeji.boxtakeawaybusiness.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.R;


public class PhoneAletDialog extends Dialog {
    private Context mContext;
    private String mTitle, mMessage, mMessage1, mMessage2, positiveButtonText, negativeButtonText;
    private TextView dialog_alert_message, dialog_alert_message1, dialog_alert_message2, dialog_alert_title;
    private Button dialog_button_cancel, dialog_button_comfirm;
    private OnPhoneAlertDialogListener mComfirmClickListener;

    public PhoneAletDialog(Context context, String title, String content, String content1, String content2, String cancelText, String confrimText, OnPhoneAlertDialogListener comfirmClickListener) {
        super(context, R.style.dialog_fullscreen);
        mContext = context;
        mTitle = title;
        mMessage = content;
        mMessage1 = content1;
        mMessage2 = content2;
        positiveButtonText = cancelText;
        negativeButtonText = confrimText;
        mComfirmClickListener = comfirmClickListener;
    }

    //onCreate方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_dialog);

        //标题
        dialog_alert_title = (TextView) findViewById(R.id.tv_dialog_title);
        dialog_alert_title.setText(mTitle);

        //提示信息
        dialog_alert_message = (TextView) findViewById(R.id.tv_dialog_content);
        dialog_alert_message.setText(mMessage);
        //提示信息
        dialog_alert_message1 = (TextView) findViewById(R.id.tv_dialog_content1);
        dialog_alert_message1.setText(mMessage1);
        //提示信息
        dialog_alert_message2 = (TextView) findViewById(R.id.tv_dialog_content2);
        dialog_alert_message2.setText(mMessage2);
        //取消按钮
        dialog_button_cancel = (Button) findViewById(R.id.btn_dialog_left);
        dialog_button_cancel.setText(positiveButtonText);
        dialog_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAletDialog.this.dismiss();
                mComfirmClickListener.Cancle();
            }
        });

        //确定按钮
        dialog_button_comfirm = (Button) findViewById(R.id.btn_dialog_right);
        dialog_button_comfirm.setText(negativeButtonText);
        dialog_button_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAletDialog.this.dismiss();
                mComfirmClickListener.Confirm();
            }
        });
    }

    //
    public interface OnPhoneAlertDialogListener {
        void Confirm();
        void Cancle();
    }
}
