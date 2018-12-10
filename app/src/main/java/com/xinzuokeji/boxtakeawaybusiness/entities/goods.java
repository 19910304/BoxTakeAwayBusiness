package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/22.
 */

public class goods implements Serializable {
    //     "order_id": 1,
//             "goods_id": 1,
//             "num": 2,
//             "total": "1.20",
//             "goods_name": "卤肉饭",
//             "goods_price": "12.00"
//    订单id
    public String order_id;
    //商品id
    public String goods_id;
    //商品数量
    public String num;
    //商品总价
    public String total;
    //商品名字
    public String goods_name;
    //属性
    public String norms;
    //商品单价
    public String goods_price;
    public String sale;
}
