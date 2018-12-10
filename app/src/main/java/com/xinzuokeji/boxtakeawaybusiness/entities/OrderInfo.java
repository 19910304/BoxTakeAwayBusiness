package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/6/22.
 */

public class OrderInfo implements Serializable {
    //订单id
    public String oid;
    //订单编号
    public String ordernum;
    //主订单id
    public String order_main_id;
    //用户id
    public String user_id;
    //地址id
    public String address_id;
    //店铺id
    public String shop_id;
    //总价
    public String total_fee;
    //餐盒费
    public String lunch_box_fee;
    //配送费
    public String distribution_fee;
    //平台奖励
    public String platform_fee;
    //订单状态 0 未付款 1已取消 2已付款 等待商家接单
// 3 商家接单 正在备餐（等待骑手接单）
// 4 骑手待取货 5骑手配送中（带送达）
// 6 已送达（待评价）7 催单 8 用户申请退款
// 9 商家退款 10系统派单（骑手待确认）
    public String status = "";
    //    退款状态 0. 用户申请退款 1.商家同意退款 2.退款中 3.退款成功 4.退款失败 5商家拒绝退款
    public String refund_status = "";
    //下单时间
    public String add_time;
    //期望送达时间
    public String expected_time;
    //预送达时间
    public String pre_delivery_time;
    //预出餐时间
    public String pre_meal_time;
    //送达时间
    public String delivery_time;
    //今日单号
    public String odd_numbers;
    //是否为预订单 0:否 1:是
    public String is_pro_order;
    //1.外送 2.自取
    public String dispatch_type;
    //  骑手id
    public String rider_id = "0";
    //骑手名字
    public String rider_name;
    //骑手头像
    public String img;
    // 待发配送骑手
    public String rider_img;
    //骑手电话
    public String rider_phone;
    //骑手电话
    public String riderphone;
    //用户id
    public String id;
    //收货人名字
    public String name;
    //收货人性别 0
    public String sex;
    //收货人电话
    public String phone;
    //省(收货人)
    public String sheng;
    //市(收货人)
    public String shi;
    //区(收货人)
    public String qu;
    //详细地址(收货人)
    public String address;
    //楼层号(收货人)
    public String building_card;
    //经度(收货人)
    public String jd;
    //维度(收货人)
    public String wd;
    //店铺id
    public String shopid;
    //店铺经度
    public String longitude;
    //店铺维度
    public String dimension;
    //平台服务费
    public String service_price;
    //  店铺配送方式 0商家自配 1平台配送
    public String distribution_info;
    //退款用户类型 1:用户 2:店铺
    public String user_type = "";
    // 退款原因
    public String reason;
    // 其他原因
    public String other_reason;
    //申请退款时间
    public String re_add_time;
    //商家同意退款时间
    public String shop_time;
    //退款中时间
    public String con_time;
    // 该订单里程
    public String mileage;
    // 预计收入
    public String expected_income;
    //备注
    public String remarks;
    //商家是否已经评价过骑手 0：未评价 其他：已评价
    public String evaluate_rider;
    //    是否已出餐 0没出餐 1已出餐
    public String is_outmeal = "";
    //商品列表
    public List<goods> goods;
}
