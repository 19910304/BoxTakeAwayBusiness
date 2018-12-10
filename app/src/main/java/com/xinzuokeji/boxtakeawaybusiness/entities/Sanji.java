package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/13.
 */

public class Sanji implements Serializable {
    //     "id": 3,
//             "pid": 1,
//             "typename": "奶茶",
//             "shop_type": 1
    public String id;//经营类型id
    public String pid;
    public String typename;
    public String shop_type;

    public String getShop_select_type() {
        return shop_select_type;
    }

    public void setShop_select_type(String shop_select_type) {
        this.shop_select_type = shop_select_type;
    }

    //选中的状态 1选中0未选中
    public String shop_select_type = "";
}
