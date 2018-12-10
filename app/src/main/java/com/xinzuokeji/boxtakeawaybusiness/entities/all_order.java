package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/20.
 */

public class all_order implements Serializable {
    //"id": 1,
////            "delivery_time": "2018.07.14 11:07",
////            "total_fee": "90.00"
    public String id;
    public String delivery_time = "";
    public String total_fee;
    // 订单状态 1和14表示退款款 6显示完成订单
    public String status;
}
