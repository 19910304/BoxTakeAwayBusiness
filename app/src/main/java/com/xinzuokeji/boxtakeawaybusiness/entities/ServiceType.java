package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/6/11.
 */
//商家服务中心显示问题分类接口
public class ServiceType implements Serializable {
    //    id": 1,
//            "type": "常见问题"
    public String id;
    public String type;
    public List<pro> pro;
}
