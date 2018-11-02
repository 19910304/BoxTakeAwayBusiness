package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/10/16.
 */
//提现信息
public class Withdraw implements Serializable {
    //       "money": "0.000",
//               "bank_card": "6210987910010995757",
//               "bank_code": "1066",
//               "bank_name": "邮储银行"
    public String money;
    public String bank_card;
    public String bank_code;
    public String bank_name;
    public String name;
}
