package com.xinzuokeji.boxtakeawaybusiness.storeOperations;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.util.CustomPopWindow;

import java.util.ArrayList;
import java.util.List;

//消息中心
public class MessageCenter extends BaseActivity {
    private ImageButton header_back;
    TextView textView;
    CustomPopWindow mCustomPopWindow;
    PopupWindow mPopWindow;
    ListView listView;
    ListViewAdapter<String> listViewAdapter;
    List<String> mdate = new ArrayList<>();
    LinearLayout ll_back;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_message_center);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        header_back.setBackgroundResource(R.mipmap.back_white);
        textView = findViewById(R.id.header_title);
        textView.setText("系统消息");
        listView = findViewById(R.id.lv_message);
        ll_back = findViewById(R.id.ll_back);
        listViewAdapter = new ListViewAdapter<String>(this, R.layout.item_lanyadayin, mdate) {
            @Override
            public void convert(ViewHolder holder, String o, int id, int selectedPosition) {

            }
        };
        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void initEvent() {
        super.initEvent();
//        header_back.setOnClickListener(this);
        textView.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    // 监听返回按键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            //绑定返回按键和返回按钮
            LinearLayout header_back = findViewById(R.id.ll_back);
            if (header_back != null) {
                header_back.performClick();
            }

            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.ll_back:
                finish();
                break;
            case R.id.header_title:

                showPopupWindow();

                break;
            case R.id.tv_message_pop_xitong:
                mPopWindow.dismiss();
                Toast.makeText(MessageCenter.this, "ddd", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_message_pop:
                mPopWindow.dismiss();
                break;
            default:
                break;

        }
    }


    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_popwindow_message, null);

        mPopWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, false);
        if (mPopWindow.isShowing()) {
            mPopWindow.dismiss();
        }
        mPopWindow.setContentView(contentView);
        mPopWindow.setFocusable(true);
        mPopWindow.setOutsideTouchable(true);
        //这句是为了防止弹出菜单获取焦点之后，点击activity的其他组件没有响应
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        //设置各个控件的点击响应
        TextView tv1 = (TextView) contentView.findViewById(R.id.tv_message_pop_xitong);
        TextView tv2 = (TextView) contentView.findViewById(R.id.tv_message_pop);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);

        //显示PopupWindow
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_message_center, null);
        mPopWindow.showAtLocation(rootview, Gravity.TOP, 0, 0);

    }


}
