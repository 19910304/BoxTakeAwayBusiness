package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/6/13.
 */

public class Shoptype implements Serializable {
    //    "id": 1,
//            "typename": "美食"
    public String id;//经营类型id
    public String typename;//经营类型名称
    public String pid;//上级id
    public String shop_type;//店铺类型id
    public List<Sanji> Sanji;//下级类型集合

}
