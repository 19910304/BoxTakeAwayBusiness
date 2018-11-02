package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/9.
 */

public class GoodsDetail implements Serializable {
    //       "id": 7,
//               "shop_id": 1,
//               "goods_img": "http://localhost/public/uploads/shop/20180606\\b8ab43a4ec7fedb31b6056fbcb640f80.jpeg",
//               "goods_name": "砂锅米线",
//               "goods_price": "12.00",
//               "menu_id": 5,
//               "begin_time": "8:00",
//               "end_time": "12:00",
//               "label_one": "招牌",
//               "label_two": "招牌",
//               "label_three": "招牌",
//               "lunch_box_num": 2,
//               "lunch_box_price": "2.00",
//               "describe": "",
//               "stock": 20,
//               "is_lower": 0,
//               "add_time": 1528271594,
//               "sort": 0
//  商品id
    public String id;
    //店铺id
    public String shop_id;
    //商品图片
    public String goods_img;
    //商品名称
    public String goods_name;
    //商品价格
    public String goods_price;
    //
    public String menu_id;
    //出售开始时间
    public String begin_time;
    //出售结束时间
    public String end_time;
    //标签一
    public String label_one;
    //标签一
    public String label_two;
    //标签一
    public String label_three;
    //	餐盒数
    public String lunch_box_num;
    //餐盒价格
    public String lunch_box_price;
    //	商品描述
    public String describe;
    //库存
    public String stock;
    //是否下架 0:没下架 1:已下架
    public String is_lower;
    //	商品添加时间
    public String add_time;
    //	商品序列号
    public String sort;
}
