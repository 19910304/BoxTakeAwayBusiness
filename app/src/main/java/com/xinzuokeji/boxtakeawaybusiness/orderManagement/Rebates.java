package com.xinzuokeji.boxtakeawaybusiness.orderManagement;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinzuokeji.boxtakeawaybusiness.BaseActivity;
import com.xinzuokeji.boxtakeawaybusiness.GSApplication;
import com.xinzuokeji.boxtakeawaybusiness.R;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ListViewAdapter;
import com.xinzuokeji.boxtakeawaybusiness.adapter.ViewHolder;
import com.xinzuokeji.boxtakeawaybusiness.entities.Reason;
import com.xinzuokeji.boxtakeawaybusiness.entities.RefundGoods;
import com.xinzuokeji.boxtakeawaybusiness.entities.RefundGoodsNew;
import com.xinzuokeji.boxtakeawaybusiness.netService.NetService;
import com.xinzuokeji.boxtakeawaybusiness.netutil.JSONDeserialize;
import com.xinzuokeji.boxtakeawaybusiness.util.Utility;
import com.xinzuokeji.boxtakeawaybusiness.util.Valid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//申请退款
public class Rebates extends BaseActivity {
    private ImageButton header_back;
    private ListViewAdapter<RefundGoodsNew> mAdatperCaipin;
    private ListViewAdapter<Reason> mAdapterReason;
    private List<RefundGoodsNew> mDateCaipin;
    private List<Reason> mDateReason;
    private ListView lv_caipin, lv_Reason;
    private String oid, order_main_id;
    Button bt_sub_rebates;
    private NetService netService;
    //退款选择原因
    private String rebates;
    private String etRebates;
    private EditText et_rebates;
    private TextView tv_rebates_all_money, tv_call_suer;
    Double totalnew;
    HashMap<String, String> goodsisCheck = new HashMap<>();
    Double distribution_fee;
    private String phoneUser;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_rebates);
        order_main_id = getIntent().getStringExtra("order_main_id");
        oid = getIntent().getStringExtra("oid");
        //接收用户电话
        phoneUser = getIntent().getStringExtra("phone");
        netService = new NetService(this);
        header_back = findViewById(R.id.header_back);
        header_back.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.header_title);
        textView.setText("申请退款");
        lv_caipin = findViewById(R.id.lv_caipin);
        lv_Reason = findViewById(R.id.lv_Reason);
        //提交退款
        bt_sub_rebates = findViewById(R.id.bt_sub_rebates);
        et_rebates = findViewById(R.id.et_rebates);
        // 联系用户
        tv_call_suer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Valid.isNotNullOrEmpty(phoneUser)) {
                    diallPhone(phoneUser);
                } else {
                    //提示无手机号码
                }

            }
        });
        // 退款钱数
        tv_rebates_all_money = findViewById(R.id.tv_rebates_all_money);
        et_rebates.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                etRebates = et_rebates.getText().toString().trim();
            }
        });
        mDateCaipin = new ArrayList<>();
        mDateReason = new ArrayList<>();
        // 退款商品列表
        mAdatperCaipin = new ListViewAdapter<RefundGoodsNew>(this, R.layout.item_caipin_rebates, mDateCaipin) {
            @Override
            public void convert(ViewHolder holder, final RefundGoodsNew refundGoods, final int id, int selectedPosition) {

                final CheckBox ck_caipin = holder.getView(R.id.ck_caipin);
                final LinearLayout ll_change_caipin_number = holder.getView(R.id.ll_change_caipin_number);//加减的布局
                //菜品数量
                final TextView tv_caipin_number = holder.getView(R.id.tv_caipin_number);
                tv_caipin_number.setText("x" + refundGoods.num);
                // 将商品数量赋值给不变量用来比较是否可点击改变
                refundGoods.setNumnew(refundGoods.num);
                if (refundGoods.getSelected().equals("1")) {
                    refundGoods.setChecked(true);
                } else {
                    refundGoods.setChecked(false);
                }
                //菜品名称
                TextView tv_caipin_name = holder.getView(R.id.tv_caipin_name);
                tv_caipin_name.setText(refundGoods.goods_name);
                //菜品图片
                ImageView img_caipin = holder.getView(R.id.img_caipin);
                ImageLoader.getInstance().displayImage(refundGoods.image, img_caipin, GSApplication.getInstance().imageOptions);
                // 商品总价
                final TextView tv_goods_price = holder.getView(R.id.tv_goods_price);
                tv_goods_price.setText(refundGoods.total_money);
                final ImageButton igb_minus = holder.getView(R.id.igb_jian);
                //  菜品个数小于等于1不能再减少
                if (Integer.valueOf(refundGoods.status) <= 1) {
                    igb_minus.setEnabled(false);
                    igb_minus.setBackgroundResource(R.mipmap.minus);
                } else {
                    igb_minus.setEnabled(true);
                    igb_minus.setBackgroundResource(R.mipmap.minusred);
                }
                final ImageButton igb_plus = holder.getView(R.id.igb_jia);
                // 改变的商品数小于商品定数可以点击
                if (Integer.valueOf(refundGoods.status) < Integer.parseInt(refundGoods.num)) {
                    igb_plus.setBackgroundResource(R.mipmap.addred);
                } else {
                    igb_plus.setBackgroundResource(R.mipmap.add);
                }
                final TextView tv_caipin_number1 = holder.getView(R.id.tv_caipin_number1);
                tv_caipin_number1.setText(refundGoods.status);
                if (Integer.parseInt(refundGoods.getNum()) > 1) {
                    ll_change_caipin_number.setVisibility(View.VISIBLE);
                } else {
                    ll_change_caipin_number.setVisibility(View.GONE);
                }
                final int goodNums = Integer.parseInt(refundGoods.num);
                //  选中check 可以改变菜品数量
                ck_caipin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        //存选中状态
                        if (isChecked) {
                            refundGoods.setChecked(true);
                        } else {
                            refundGoods.setChecked(false);
                        }
                    }
                });
                //选中商品计算
                ck_caipin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        float allPrice;
                        if (refundGoods.getChecked().equals(true)) {
//                            Log.i("is", refundGoods.IsChecked + "---");
//                            allPrice = Float.valueOf(refundGoods.getNum()) * refundGoods.getSale();
//                            refundGoods.setTotal(String.valueOf(allPrice));
                            netService.showRefundGoodsSelect(Integer.parseInt(oid), Integer.parseInt(refundGoods.getOrder_goods_id()), Hander_showRefundGoodsnew);
                        } else {
                            netService.showRefundGoodsUnselect(Integer.parseInt(oid), Integer.parseInt(refundGoods.getOrder_goods_id()), Hander_showRefundGoodsnew);
                        }
//
//                        BigDecimal pSum = new BigDecimal("0.00");
//                        pSum = pSum.add(new BigDecimal(refundGoods.getTotal()));
//                        totalnew = 0.00;
                        //循环得到总钱数
                        int nums = 0, numnews = 0;
                        for (int i = 0; i < mDateCaipin.size(); i++) {
                            if (mDateCaipin.get(i).getChecked().equals(true)) {
                                goodsisCheck.put(String.valueOf(i), mDateCaipin.get(i).getChecked().toString());
                            } else {
                                goodsisCheck.clear();
                            }
                            nums = nums + Integer.parseInt(mDateCaipin.get(i).num);
                            numnews = numnews + Integer.parseInt(mDateCaipin.get(i).numnew);
//                            totalnew = Utility.add(totalnew, Double.valueOf(mDateCaipin.get(i).getTotal()));
                        }
//                        if (goodsisCheck.size() == mDateCaipin.size() && nums == numnews) {
//                            //  distribution_fee
//                            tv_rebates_all_money.setText("退款金额 ￥" + (Utility.add(totalnew, distribution_fee)));
//                        } else {
//                            tv_rebates_all_money.setText("退款金额 ￥" + totalnew);
//                        }

                        if (ck_caipin.isChecked()) {
                            if (Integer.parseInt(refundGoods.getNumnew()) > 1) {
                                ll_change_caipin_number.setVisibility(View.VISIBLE);
                            } else {
                                ll_change_caipin_number.setVisibility(View.GONE);
                            }
                        } else {
                            ll_change_caipin_number.setVisibility(View.GONE);
                        }

                    }
                });
                igb_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tvgoods_price = tv_goods_price;
                        TextView tvCaiPinNumber1 = tv_caipin_number1;
                        ImageButton igbMinus = igb_minus;
                        ImageButton igbPlus = igb_plus;
                        netService.showRefundGoodsReduce(Integer.parseInt(oid), Integer.parseInt(refundGoods.getOrder_goods_id()), Hander_showRefundGoodsnew);
                        // 设置商品数量
//                        setGoodsNumbers(refundGoods, true, igbMinus, igbPlus, tvCaiPinNumber1, tvgoods_price, refundGoods.sale);
                    }
                });
                igb_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tvgoods_price = tv_goods_price;
                        TextView tvCaiPinNumber1 = tv_caipin_number1;
                        ImageButton igbMinus = igb_minus;
                        ImageButton igbPlus = igb_plus;
                        // 设置商品数量
                        if (Integer.parseInt(refundGoods.getStatus()) < Integer.parseInt(refundGoods.getNum())) {
                            netService.showRefundGoodsAdd(Integer.parseInt(oid), Integer.parseInt(refundGoods.getOrder_goods_id()), Hander_showRefundGoodsnew);
//                            setGoodsNumbers(refundGoods, false, igbMinus, igbPlus, tvCaiPinNumber1, tvgoods_price, refundGoods.sale);
                        } else {
                        }
                    }
                });
            }
        };
        lv_caipin.setAdapter(mAdatperCaipin);
        setPullLvHeight(lv_caipin);
        mAdatperCaipin.notifyDataSetChanged();
        // 原因
        mAdapterReason = new ListViewAdapter<Reason>(this, R.layout.item_reason_rebates, mDateReason) {
            @Override
            public void convert(ViewHolder holder, Reason reason, int id, int selectedPosition) {
                TextView tv_reason_rebates = holder.getView(R.id.tv_reason_rebates);
                tv_reason_rebates.setText(reason.title);
                ImageView img_reason_rebates = holder.getView(R.id.img_reason_rebates);
                // 设置选中状态
                if (id == selectedPosition) {
                    img_reason_rebates.setBackgroundResource(R.mipmap.rebate_pitch);
                } else {
                    img_reason_rebates.setBackgroundResource(R.mipmap.rebate_unselected);
                }
            }
        };
        lv_Reason.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mAdapterReason.clearSelection(position);
                mAdapterReason.notifyDataSetChanged();
                rebates = mDateReason.get(position).title;
            }
        });

        lv_Reason.setAdapter(mAdapterReason);
        //显示传副id
        netService.showRefundGoods(Integer.parseInt(oid), Hander_showRefundGoods);
        netService.showRefundGoodsinit(Integer.parseInt(oid), Hander_showRefundGoodsnew);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        header_back.setOnClickListener(this);
        bt_sub_rebates.setOnClickListener(this);
    }

    LoadingDailog dialog;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.bt_sub_rebates://提交退款
                if (Valid.isNotNullOrEmpty(etRebates)) {
                    JSONArray goodsArray = new JSONArray();
                    // 选中的商品添加到退款项中
                    for (int i = 0; i < mDateCaipin.size(); i++) {
                        if (mDateCaipin.get(i).IsChecked) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("order_goods_id", mDateCaipin.get(i).getOrder_goods_id());
                                jsonObject.put("goods_id", mDateCaipin.get(i).getGoods_id());
                                jsonObject.put("num", mDateCaipin.get(i).getNum());
                                jsonObject.put("sale", mDateCaipin.get(i).getRefund_money());
                                goodsArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (goodsArray.length() > 0) {
                        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                                .setMessage("加载中...")
                                .setCancelable(true)
                                .setCancelOutside(true);
                        dialog = loadBuilder.create();
                        dialog.show();
                        netService.activeRefund(Integer.parseInt(order_main_id), totalnew, rebates, etRebates, goodsArray, Hander_activeRefund);
                    } else {
                        showTip("请选择退款菜品", Toast.LENGTH_SHORT);
                    }
                } else {
                    showTip("请填写退款原因", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    //设置商品数量
    private void setGoodsNumbers(RefundGoods refundGoods, Boolean isMinus, ImageButton btn_goods_minus, ImageButton btn_goods_plus, TextView goods_numbers, TextView tv_good_price, Float goodPrice) {

        //加减操作
        if (isMinus) {
            int goodnum = Integer.parseInt(refundGoods.num);
            //  商品个数自减 并赋值给实体
            goodnum--;
            refundGoods.setNum(String.valueOf(goodnum));
            //保持精度两位
            BigDecimal bg = new BigDecimal(goodnum * goodPrice);
            double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //显示商品价格
            tv_good_price.setText(String.valueOf(f1));
            refundGoods.setTotal(String.valueOf(f1));
            // 显示商品个数
            goods_numbers.setText(String.valueOf(goodnum));
//            Log.i("num", String.valueOf(goodnum) + "--" + goodPrice + "f1:" + f1);
            if (goodnum <= 1) {
                btn_goods_minus.setEnabled(false);
                btn_goods_minus.setBackgroundResource(R.mipmap.minus);
            } else {
                btn_goods_minus.setEnabled(true);
                btn_goods_minus.setBackgroundResource(R.mipmap.minusred);
            }
            // 改变的商品数小于商品定数可以点击
            if (goodnum < Integer.parseInt(refundGoods.numnew)) {
                btn_goods_plus.setBackgroundResource(R.mipmap.addred);
            } else {
                btn_goods_plus.setBackgroundResource(R.mipmap.add);
            }
//
        } else {
            int goodnum = Integer.parseInt(refundGoods.num);
            goodnum++;
            refundGoods.setNum(String.valueOf(goodnum));

            BigDecimal bg = new BigDecimal(goodnum * goodPrice);
            double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            tv_good_price.setText(String.valueOf(f1));
            refundGoods.setTotal(String.valueOf(f1));
            goods_numbers.setText(String.valueOf(goodnum));
            if (goodnum <= 1) {
                btn_goods_minus.setEnabled(false);
                btn_goods_minus.setBackgroundResource(R.mipmap.minus);
            } else {
                btn_goods_minus.setEnabled(true);
                btn_goods_minus.setBackgroundResource(R.mipmap.minusred);
            }
            // 改变的商品数小于商品定数可以点击
            if (goodnum < Integer.parseInt(refundGoods.numnew)) {
                btn_goods_plus.setBackgroundResource(R.mipmap.addred);
            } else {
                btn_goods_plus.setBackgroundResource(R.mipmap.add);
            }
        }

        // 计算退款总数
        totalnew = 0.00;
        //循环得到总钱数
        int nums = 0, numnews = 0;
        for (int i = 0; i < mDateCaipin.size(); i++) {
            if (mDateCaipin.get(i).getChecked().equals(true)) {
                goodsisCheck.put(String.valueOf(i), mDateCaipin.get(i).getChecked().toString());
            } else {
                goodsisCheck.clear();
            }

            nums = nums + Integer.parseInt(mDateCaipin.get(i).num);
            numnews = numnews + Integer.parseInt(mDateCaipin.get(i).numnew);
//            totalnew = Utility.add(totalnew, Double.valueOf(mDateCaipin.get(i).getTotal()));
        }
        //判断要不要加配送费
        if (goodsisCheck.size() == mDateCaipin.size() && nums == numnews) {
            tv_rebates_all_money.setText(new StringBuilder().append("退款金额 ￥").append(Utility.add(totalnew, distribution_fee)).toString());
        } else {
            tv_rebates_all_money.setText(new StringBuilder().append("退款金额 ￥").append(totalnew).toString());
        }

    }

    @SuppressLint("HandlerLeak")
    Handler Hander_showRefundGoods = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String reason = jsonObject.getString("reason");
                        //  加载退款原因
                        mDateReason.clear();
                        mDateReason.addAll(new JSONDeserialize<>(Reason.class, reason).toObjects());
                        if (mDateReason != null && mDateReason.size() != 0) {
                            rebates = mDateReason.get(0).title;
                        }
                        setPullLvHeight(lv_Reason);
                        mAdapterReason.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
    Handler Hander_activeRefund = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        gotoActivityAndFinish(RefundSuccess.class.getName(), null);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 2001:
                    dialog.dismiss();
                    showTip(msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
                case 1001:
                    dialog.dismiss();
                    showTip(getString(R.string.network_error), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler Hander_showRefundGoodsnew = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String allMoney = jsonObject.getString("refund_money");
                        tv_rebates_all_money.setText("退款金额 ￥" + allMoney);
                        totalnew = Double.valueOf(allMoney);
                        String orderGoods = jsonObject.getString("can_order_goods");
                        mDateCaipin.clear();
                        // 加载菜品数据
                        mDateCaipin.addAll(new JSONDeserialize<>(RefundGoodsNew.class, orderGoods).toObjects());
                        setPullLvHeight(lv_caipin);
                        mAdatperCaipin.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    @Override
    protected void onStop() {
        super.onStop();

    }
}
