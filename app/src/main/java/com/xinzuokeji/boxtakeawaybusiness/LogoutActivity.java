package com.xinzuokeji.boxtakeawaybusiness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.xinzuokeji.boxtakeawaybusiness.registrationShop.PassWordLogin;

/**
 * Created by Administrator on 2018/9/30.
 */
//退出的对话框
public class LogoutActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        TextView text_logout_yes = findViewById(R.id.text_logout_yes);
        text_logout_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //销毁baseActivity下的所有activity
                MainActivity.mActivity.finish();
                GSApplication.getInstance().removeALLActivity_();
                Intent intent1 = new Intent(LogoutActivity.this, PassWordLogin.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        });
    }

    // 监听返回按键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 退出到后台
//            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
