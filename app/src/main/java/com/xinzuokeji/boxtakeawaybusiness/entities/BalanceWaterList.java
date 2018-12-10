package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/10/20.
 */

public class BalanceWaterList implements Serializable {
    //     "money": "0.10",
//             "status": 1,
//             "time": "2018-10-20 09:16:24"
//    状态 提现状态：1 提交成功 2 审核成功 3 审核失败 4 已打款 5 打款失败
    public String money;
    public String status;
    public String time;
    //    余额
    public String id;
    public String account_id;
    //3 正，5负
    public String type;
    public String original_money;
    public String account_current;
    public String account_money;
    public String add_time;
//    id":1,"type":3,"account_id":70,"original_money":"0.000","account_current":"4.300","account_money":"4.300","add_time":"2018-10-26"}
}
