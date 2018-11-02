package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/19.
 */
//显示营业执照
public class License implements Serializable {
    //     "id": 11,
//             "shop_id": 14,
//             "card_type": 1,
//             "certificates_name": "",
//             "legal_person": "张三",
//             "register_number": "1234567891011",
//             "certificates_place": "重庆",
//             "validity_type": 1,
//             "validity_term": "",
//             "certificates_img": ""
    public String id;//证件id
    public String shop_id;//店铺id
    public String card_type;//证件类型 1:营业执照 2:法人身份证 3:许可证 4:特许证件
    public String certificates_name;//证件名称
    public String legal_person;//法人代表姓名
    public String register_number;//证件注册号

    public String certificates_place;//证件所在地
    public String validity_type;//证件有效类型 1:长期有效 0:短期有效
    public String validity_term;//有效期限
    public String certificates_img;//证件图片
}
