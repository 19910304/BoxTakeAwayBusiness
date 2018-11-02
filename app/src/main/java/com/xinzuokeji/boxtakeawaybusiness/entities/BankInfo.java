package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/20.
 */

public class BankInfo implements Serializable {
    //     "id": 3,
//             "uid": 15,
//             "account_type": 2,
//             "name": "张三",
//             "card": "98756214859685",
//             "city": "重庆",
//             "opening_bank": "农业银行",
//             "add_time": "2018-06-14 16:06:30"
//   银行卡id
    public String id;
    //    店铺id
    public String uid;
    //账户类型 1:对公 2:对私
    public String account_type;
    //开户人名字
    public String name;
    //银行卡号
    public String card;
    //开户人地址
    public String city;
    //开户行
    public String opening_bank;
    //添加的时间
    public String add_time;
    public String bank_code;
}
