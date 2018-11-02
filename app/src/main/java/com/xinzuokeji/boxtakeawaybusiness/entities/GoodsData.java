package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/9.
 */

public class GoodsData implements Serializable {
    //    显示对应菜单下的商品接口

    //    商品图片
    public String goods_img;
    //    商品名字
    public String goods_name;
    //    商品单价
    public String goods_price;
    //    是否下架 1 下架 0 未下架
    public String is_lower;
    //   序号
    public String sort;
    //    商品id
    public String goods_id;
    //    销售量
    public String goods_num;
}
