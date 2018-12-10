package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/10/12.
 */

public class BusinessNotice implements Serializable {
    //     "id": 1,
//             "shopid": 1,
//             "notice": "刺激红魔馆"
    public String id;
    public String shopid;
    // 显示店铺公告
    public String notice;
    // 显示店铺简介
    public String shop_info;
}
