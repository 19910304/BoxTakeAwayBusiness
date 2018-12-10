package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/6.
 */

public class ShowPreOrderSet implements Serializable {
    //     "id": 1,
//             "shop_id": 1,
//             "accept_date": "第一天",
//             "remind_time": "10",
//             "open_preorder": 1
//    编号
    public String id;
    //店铺id
    public String shop_id;
    //商家可接受预定日期
    public String accept_date;
    //预订单提醒时间
    public String remind_time;
    //是否停止营业时接收预订单 1:不停止 2:停止
    public String open_preorder;
}
