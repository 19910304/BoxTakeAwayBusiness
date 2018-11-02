package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/13.
 */

public class Shopbasic implements Serializable {
    //    "shop_phone": "987654321",
//            "contacts": "张三",
//            "contacts_phone": "123456789",
//            "business_hours": "8:00-21:00",
//            "sheng": "重庆市",
//            "shi": "重庆",
//            "qu": "九龙坡区",
//            "shop_address": "石桥铺西科商城",
//            "longitude": "102",
//            "dimension": "35",
//            "out_img": "http://localhost/public/uploads/shop/20180613\\7e83eb4fa230be9f6556f823023a2a18.jpeg",
//            "in_img": "http://localhost/public/uploads/shop/20180613\\42d3f6fd0aa58ba2057ac76844bcebcc.jpeg",
//            "logo": "http://lo
    public String shop_phone;
    public String contacts;
    public String contacts_phone;
    public String business_hours;
    public String sheng;
    // 地址id为0，表示新增加
    public String address_id;
    public String shi;
    public String qu;
    public String shop_address;
    public String longitude;
    public String dimension;
    public String out_img;
    public String in_img;
    public String logo;
    public String code;
}
