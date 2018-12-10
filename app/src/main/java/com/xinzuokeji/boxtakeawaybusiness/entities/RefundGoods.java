package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/11.
 */

public class RefundGoods implements Serializable {
    //     "order_id": 12,
//             "num": 1,
//             "sale": "0.00",
//             "total": "10.00",
//             "goods_id": 2,
//             "goods_name": "扬州炒饭"
//  副订单id
    public String order_id;
    //
    public String order_goods_id;
    //   商品数量
    public String num;
    public String numnew;
    //是否被选中
    public Boolean IsChecked = true;

    //商品单价
    public Float sale;
    //商品总价
    public String total;
    //商品id
    public String goods_id;
    //商品名字
    public String goods_name;
    public String goods_img;
    public String lunch_box_price;
    //配送费
    public Float distribution_fee;

    public String getOrder_goods_id() {
        return order_goods_id;
    }

    public void setOrder_goods_id(String order_goods_id) {
        this.order_goods_id = order_goods_id;
    }


    public Boolean getChecked() {
        return IsChecked;
    }

    public void setChecked(Boolean checked) {
        IsChecked = checked;
    }


    public String getNumnew() {
        return numnew;
    }

    public void setNumnew(String numnew) {
        this.numnew = numnew;
    }


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Float getSale() {
        return sale;
    }

    public void setSale(Float sale) {
        this.sale = sale;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }
}
