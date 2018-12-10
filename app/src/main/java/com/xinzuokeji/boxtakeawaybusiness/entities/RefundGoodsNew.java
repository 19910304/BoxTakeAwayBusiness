package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/21.
 */

public class RefundGoodsNew implements Serializable {
    //  "id": 98,
    // "order_main_id": 310,
    // "goods_id": 1240,
//          "goods_name": "牛排02",
//          "num": "1",
//          "order_goods_id": 168,
//          "money": "15.00",
//          "refund_money": "10.00",
//          "status": "1",
//          "add_time": 0,
//          "selected": 1,
//          "refunded": 0,
//          "image": "",
//          "total_money": "10.00"
//
    public String id;
    public String order_main_id;
    public String goods_id;
    public String goods_name;
    public String num;



    public String order_goods_id;
    public String money;
    public String refund_money;
    // 选中变化的商品个数
    public String status;
    public String add_time;
    public String selected;
    public String refunded;
    public String image;
    public String total_money;
    public String numnew;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_main_id() {
        return order_main_id;
    }

    public void setOrder_main_id(String order_main_id) {
        this.order_main_id = order_main_id;
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOrder_goods_id() {
        return order_goods_id;
    }

    public void setOrder_goods_id(String order_goods_id) {
        this.order_goods_id = order_goods_id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRefund_money() {
        return refund_money;
    }

    public void setRefund_money(String refund_money) {
        this.refund_money = refund_money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getRefunded() {
        return refunded;
    }

    public void setRefunded(String refunded) {
        this.refunded = refunded;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getNumnew() {
        return numnew;
    }

    public void setNumnew(String numnew) {
        this.numnew = numnew;
    }

    public Boolean getChecked() {
        return IsChecked;
    }

    public void setChecked(Boolean checked) {
        IsChecked = checked;
    }

    //是否被选中
    public Boolean IsChecked = true;
}
