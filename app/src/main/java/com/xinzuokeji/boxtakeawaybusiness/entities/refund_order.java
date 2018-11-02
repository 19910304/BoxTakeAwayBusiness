package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/26.
 */

public class refund_order implements Serializable {
    //  "status": 14,
//          "add_time": "2018.10.21 16:57",
//          "money": 0.02
    public String order_id;//订单实际支付
    public String status;//订单状态
    public String add_time;//订单退款时间
    public String money;//订单退款金额
}
