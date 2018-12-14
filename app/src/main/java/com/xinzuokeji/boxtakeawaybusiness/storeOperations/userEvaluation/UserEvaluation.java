package com.xinzuokeji.boxtakeawaybusiness.storeOperations.userEvaluation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.Evaluate;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.util.MyGridView;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;
import com.xinzuokeji.boxtakeawaybusiness.util.listviewshangxia.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//用户评价
public class UserEvaluation extends BaseActivity implements XListView.IXListViewListener {
    private ImageButton header_back;
    RadioGroup radioGroup;
    RadioButton rdb_all_pinjia, rdb_chapin, rdb_haoping;
    private com.xinzuokeji.boxtakeawaybusiness.util.listviewshangxia.XListView listView;
    private ListViewAdapter<Evaluate> mAdapter;
    List<Evaluate> mDate;

    Handler mHandler;
    private NetService netService;

    LinearLayout ll_back;
    PopupWindow mPopWindow;
    private static final String SEP2 = ",";
    private static final String SEP3 = "=";
    //   点击回复传递的控件
    LinearLayout ll_EvaluateReply;
    Button bt_EvaluateReply;
    TextView tv_EvaluateReply;
    String str_EvaluateReply = "";
    private EditText et_EvaluateReply;
    InputMethodManager imm;
    String type;
    private static Integer CHAPING = 1;
    private static Integer HAOPING = 2;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_user_evaluation);
        type = getIntent().getStringExtra("type");
        netService = new NetService(this);
        header_back = findViewById(R.id.header_backnew);
        header_back.setVisibility(View.VISIBLE);
//        header_back.setBackgroundResource(R.mipmap.back_white);
        ll_back = findViewById(R.id.ll_back);//返回

        mHandler = new Handler();
        listView = findViewById(R.id.lv_pingjia);
        // 设置xlistview判断是否出现“查看更多”
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(true);

        radioGroup = findViewById(R.id.rdg_pinjia);
        rdb_all_pinjia = findViewById(R.id.rdb_all_pinjia);
        rdb_chapin = findViewById(R.id.rdb_chapin);
        rdb_haoping = findViewById(R.id.rdb_haoping);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_all_pinjia://全部
                        if (type.equals("user")) {
                            netService.showEvaluate(GetstoreId(), showEvaluate);
                        } else {
                            netService.showRiderEva(GetstoreId(), Handle_showRiderEva);
                        }

//                        Toast.makeText(UserEvaluation.this, "全部", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rdb_chapin://差评
                        if (type.equals("user")) {
                            netService.negativeComment(GetstoreId(), CHAPING, negativeComment);
                        } else {
                            netService.RiderGoodsOrBad(GetstoreId(), CHAPING, Handle_RiderGoodsOrBad);
                        }

//                        Toast.makeText(UserEvaluation.this, "差评", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rdb_haoping://好评
                        if (type.equals("user")) {
                            netService.negativeComment(GetstoreId(), HAOPING, negativeComment);
                        } else {
                            netService.RiderGoodsOrBad(GetstoreId(), HAOPING, Handle_RiderGoodsOrBad);
                        }

//                        Toast.makeText(UserEvaluation.this, "好评", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        // 评价列表
        mDate = new ArrayList<>();
        mAdapter = new ListViewAdapter<Evaluate>(this, R.layout.item_user_evaluation, mDate) {
            @Override
            public void convert(ViewHolder holder, final Evaluate evaluate, int id, int selectedPosition) {
                TextView tv_songda = holder.getView(R.id.tv_songda);
                //用户昵称
                if (type.equals("user")) {
                    holder.setText(R.id.tv_nickname, evaluate.nickname);
                    //多少分钟送达
                    tv_songda.setText(evaluate.songda + "分钟送达");
                    tv_songda.setVisibility(View.VISIBLE);
                } else {
                    holder.setText(R.id.tv_nickname, "匿名");
                    tv_songda.setVisibility(View.GONE);
                }
                // 评价星数
                com.xinzuokeji.boxtakeawaybusiness.util.RatingBarNew star = holder.getView(R.id.star);
                star.setClickable(false);
                star.setStar(Float.parseFloat(evaluate.num));
                //评论的时间
                holder.setText(R.id.tv_at, evaluate.at);

                //评论内容
                holder.setText(R.id.tv_content, evaluate.content);
                // 加载头像
                ImageView icon = holder.getView(R.id.btn_me_thumbnail);
                if (type.equals("user")) {
                    ImageLoader.getInstance().displayImage(evaluate.head_img, icon, GSApplication.getInstance().optionshead);
                } else {
                    if (Valid.isNotNullOrEmpty(evaluate.head_img)) {
                        ImageLoader.getInstance().displayImage(evaluate.head_img, icon, GSApplication.getInstance().optionshead);
                    } else {
                        icon.setBackgroundResource(R.mipmap.rider_icon);
                    }

//                    ImageLoader.getInstance().displayImage("mipmap://" + R.mipmap.rider_icon, icon);
                }
                //1.创建图片集合
                List<String> imgurl = new ArrayList<>();
                try {
                    //2.解析添加数据
                    JSONArray jsonArray = new JSONArray(evaluate.imgurl);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        imgurl.add(jsonArray.get(i).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MyGridView myGvImg = holder.getView(R.id.gv_evaluation_pictures);
                ListViewAdapter<String> imgAdapter = new ListViewAdapter<String>(UserEvaluation.this, R.layout.item_evaluation_pictures, imgurl) {
                    @Override
                    public void convert(ViewHolder holder, String imgurl, int id, int selectedPosition) {
                        ImageView img_evaluation_picture = holder.getView(R.id.img_evaluation_picture);
                        if (String.valueOf(imgurl).equals("")) {

                        } else {
                            ImageLoader.getInstance().displayImage(imgurl, img_evaluation_picture, GSApplication.getInstance().imageOptions);
                        }

                    }
                };
                myGvImg.setAdapter(imgAdapter);
                imgAdapter.notifyDataSetChanged();
//                if (img.size() != 0) {
//                    for (int i = 0; i < img.size(); i++) {
////                    ImageView imageView=new ImageView(this);
//                    }
//                }

                final Button bt_huifu = holder.getView(R.id.bt_huifu);
                // 商家回复评论
                final LinearLayout ll_huifu = holder.getView(R.id.ll_huifu);
                final TextView tv_huifu_content = holder.getView(R.id.tv_huifu_content);
                if (type.equals("user")) {
                    if (Valid.isNotNullOrEmpty(evaluate.reply_content)) {
                        bt_huifu.setVisibility(View.GONE);
                        ll_huifu.setVisibility(View.VISIBLE);
                        tv_huifu_content.setText("商家回复：" + evaluate.reply_content);
                    } else {
                        bt_huifu.setVisibility(View.VISIBLE);
                        ll_huifu.setVisibility(View.GONE);
                    }
                } else {
                    bt_huifu.setVisibility(View.GONE);
                    ll_huifu.setVisibility(View.GONE);
                }
                bt_huifu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        bt_EvaluateReply = bt_huifu;
                        ll_EvaluateReply = ll_huifu;
                        tv_EvaluateReply = tv_huifu_content;
                        showPopupWindow(Integer.parseInt(evaluate.id));
                    }
                });
            }
        };
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        listView.setXListViewListener(this);
        if (type.equals("user")) {
            netService.showEvaluate(GetstoreId(), showEvaluate);
            netService.seCommentNum(GetstoreId(), shownum);
        } else {
            netService.showRiderEva(GetstoreId(), showEvaluate);
            netService.ReCommentNum(GetstoreId(), shownum);
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);

    }


    //将字符串转换成集合
    public List<Object> StringToList(String listText) {
        if (listText == null || listText.equals("")) {
            return null;
        }
//        listText = listText.substring(1);

        listText = listText;

        List<Object> list = new ArrayList<Object>();
        String[] text = listText.split(SEP2);
        for (String str : text) {
            if (str.charAt(0) == 'M') {
                Map<?, ?> map = StringToMap(str);
                list.add(map);
            } else if (str.charAt(0) == 'L') {
                List<?> lists = StringToList(str);
                list.add(lists);
            } else {
                list.add(str);
            }
        }
        return list;
    }

    public Map<String, Object> StringToMap(String mapText) {

        if (mapText == null || mapText.equals("")) {
            return null;
        }
        mapText = mapText.substring(1);

        mapText = mapText;

        Map<String, Object> map = new HashMap<String, Object>();
        String[] text = mapText.split("\\" + SEP2); // 转换为数组
        for (String str : text) {
            String[] keyText = str.split(SEP3); // 转换key与value的数组
            if (keyText.length < 1) {
                continue;
            }
            String key = keyText[0]; // key
            String value = keyText[1]; // value
            if (value.charAt(0) == 'M') {
                Map<?, ?> map1 = StringToMap(value);
                map.put(key, map1);
            } else if (value.charAt(0) == 'L') {
                List<?> list = StringToList(value);
                map.put(key, list);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    //回复PopupWindow
    private void showPopupWindow(final int evaluationId) {
        //设置contentView
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_popwindow_user_evaluation, null);
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
                KeyBoardCancle();
//                hideInput(mPopWindow);
            }
        });


        //设置各个控件的点击响应
        final EditText etEvaluntionContent = contentView.findViewById(R.id.et_evaluation_content);
        showSoftInputFromWindow(UserEvaluation.this, etEvaluntionContent);
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Button btSendEvaluation = contentView.findViewById(R.id.bt_send_evaluation);
        etEvaluntionContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_EvaluateReply = etEvaluntionContent;
                str_EvaluateReply = etEvaluntionContent.getText().toString();
            }
        });
        btSendEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Valid.isNotNullOrEmpty(str_EvaluateReply)) {
                    et_EvaluateReply.clearFocus();
                    mPopWindow.dismiss();

                    netService.showEvaluateReply(GetstoreId(), evaluationId, str_EvaluateReply, showEvaluatehuifu);
                }

            }
        });
        //显示PopupWindow
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_user_evaluation, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);

    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    @Override
    public void initEvent() {
        super.initEvent();
        ll_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.ll_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void KeyBoardCancle() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // type为user 用户对商家的评价,rider为骑手对店铺的评价
                if (type.equals("user")) {
                    if (rdb_all_pinjia.isChecked()) {
                        netService.showEvaluate(GetstoreId(), showEvaluate);
                    }
                    if (rdb_chapin.isChecked()) {
                        netService.negativeComment(GetstoreId(), CHAPING, negativeComment);
                    }
                    if (rdb_haoping.isChecked()) {
                        netService.negativeComment(GetstoreId(), HAOPING, negativeComment);
                    }
                } else {
                    if (rdb_all_pinjia.isChecked()) {
                        netService.showRiderEva(GetstoreId(), showEvaluate);
                    }
                    if (rdb_chapin.isChecked()) {
                        netService.RiderGoodsOrBad(GetstoreId(), CHAPING, Handle_RiderGoodsOrBad);
                    }
                    if (rdb_haoping.isChecked()) {
                        netService.RiderGoodsOrBad(GetstoreId(), HAOPING, Handle_RiderGoodsOrBad);
                    }
                }
                listView.stopLoadMore();
                listView.stopRefresh();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.stopLoadMore();
                listView.stopRefresh();
            }
        }, 1000);
    }

    @SuppressLint("HandlerLeak")
    Handler shownum = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj != null) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(msg.obj.toString());
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String goodnum = jsonObject1.getString("goodNum");
                            rdb_haoping.setText("好评(" + goodnum + ")");
                            String badnum = jsonObject1.getString("badNum");
                            rdb_chapin.setText("差评(" + badnum + ")");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    break;
                case 2001:
//                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
    Handler showEvaluate = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj.toString().equals("[]")) {
                        mDate.clear();
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(UserEvaluation.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    } else {
                        mDate.clear();
                        mDate.addAll((Collection<? extends Evaluate>) msg.obj);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2001:
//                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
    Handler Handle_showRiderEva = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj.toString().equals("[]")) {
                        mDate.clear();
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(UserEvaluation.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    } else {
                        mDate.clear();
                        mDate.addAll((Collection<? extends Evaluate>) msg.obj);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2001:
//                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
    Handler negativeComment = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj.toString().equals("[]")) {
//                        rdb_chapin.setText("差评");
                        mDate.clear();
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(UserEvaluation.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    } else {
                        mDate.clear();
                        mDate.addAll((Collection<? extends Evaluate>) msg.obj);
//                        rdb_chapin.setText("差评（" + mDate.size() + "）");
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2001:
//                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
    Handler Handle_RiderGoodsOrBad = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    if (msg.obj.toString().equals("[]")) {
//                        rdb_chapin.setText("差评");
                        mDate.clear();
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(UserEvaluation.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    } else {
                        mDate.clear();
                        mDate.addAll((Collection<? extends Evaluate>) msg.obj);
//                        rdb_chapin.setText("差评（" + mDate.size() + "）");
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2001:
//                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
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
    Handler showEvaluatehuifu = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    ll_EvaluateReply.setVisibility(View.VISIBLE);
                    tv_EvaluateReply.setText("商家回复：" + str_EvaluateReply);//显示回复内容
                    bt_EvaluateReply.setVisibility(View.GONE);//回复成功隐藏回复按钮
                    //et_EvaluateReply为接受软键盘输入的视图，
//                    hideInput(bt_EvaluateReply); //强制隐藏键盘
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 2001:
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
                    showTip(getString(R.string.network_error), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //强制隐藏输入法键盘
    private void hideInput(View view) {
        InputMethodManager mm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mm.isActive()) {
            mm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }
}
