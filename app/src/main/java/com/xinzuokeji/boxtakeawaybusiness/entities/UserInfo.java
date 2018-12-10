package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/18.
 */

public class UserInfo implements Serializable {
    //    id	int	店铺id
//    bid	int	商家id
//    examine_status	int	审核状态 0待处理 1申请 2通过 3失败
//    logo	string	店铺logo
//    shop_name	string	店铺名字
//    upd_time	string	时间
//    account_name	string	商家名字
//    phone	string	商家电话
    public String id;
    public String bid;
    public String examine_status;
    public String logo;
    public String shop_name;
    public String upd_time;
    public String account_name;
    public String phone;
}
