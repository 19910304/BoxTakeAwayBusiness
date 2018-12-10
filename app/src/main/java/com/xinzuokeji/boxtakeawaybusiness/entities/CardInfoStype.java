package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/31.
 */

public class CardInfoStype implements Serializable {
    //    "card_license": 1,
//            "id_card": 2,
//            "card_licence": 0
    //营业执照
    public String card_license;
    //法人身份证
    public String id_card;
    //许可证
    public String card_licence;
}
