package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */

public class Finance implements Serializable {
    //    "money": "0.00",
//            "takeout_ordernum": 0,
//            "takeout_order": "0.00",
//            "refund_ordernum": 0,
//            "refund_order": "0.00",
//            "all_order": [
//    {
//        "id": 1,
//            "delivery_time": "2018.07.14 11:07",
//            "total_fee": "90.00"
//    },
    public String money;
    public String takeout_ordernum;
    public String takeout_order;
    public String refund_ordernum;
    public String refund_order;
    public List<com.xinzuokeji.boxtakeawaybusiness.entities.all_order> all_order;


}
