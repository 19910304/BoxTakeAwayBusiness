package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/9.
 */
//显示菜单列表
public class GoodsMenu implements Serializable {
    //    "id": 1,
//            "shop_id": 1,
//            "menu_name": "冒菜"
    //  菜单id
    public String id;
    //店铺id
    public String shop_id;
    //菜单名字
    public String menu_name;
    //菜单描述
    public String describe;
    //菜单下的商品数量
    public String goods_count;

}
