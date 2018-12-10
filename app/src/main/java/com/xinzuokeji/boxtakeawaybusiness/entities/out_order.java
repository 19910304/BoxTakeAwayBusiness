package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/26.
 */

public class out_order implements Serializable {
    //   "id": 1038,
//           "order_main_id": 1051,
//           "delivery_time": "2018.10.21 00:34",
//           "total_fee": "0.01"
    public String order_id;//订单id
    public String order_main_id;//主订单id
    public String delivery_time;//订单送达时间
    public String total_fee;//订单总金额


}
