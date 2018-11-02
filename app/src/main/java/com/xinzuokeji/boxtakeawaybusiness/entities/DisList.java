package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/22.
 */
//查询配送时间段列表接口
public class DisList implements Serializable {
    //     "id": 1,
//             "shop_id": 1,
//             "dis_type": 1,
//             "time_type": 1,
//             "open": "06:00",
//             "close": "08:00",
//             "dis_money": "5.00",
//             "start_price": "13.00",
//             "point_set":
    public String id;
    public String shop_id;
    public String dis_type;
    public String time_type;
    public String open;
    public String close;
    public String dis_money;
    public String start_price;
    public List<point_set> point_set;
}
