package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/6/23.
 */
//用户评价
public class Evaluate implements Serializable {
    //   评论id
    public String id;
    //星星数
    public String num;
    //评论内容
    public String content;
    //图片集合
    public String imgurl;
    //评论的时间
    public String at;
    //是否是匿名 0:否 1:是
    public String is_anonymous;
    //匿名名称
    public String an_name;
    //订单添加时间
    public String add_time;
    //订单送达时间
    public String delivery_time;
    //用户昵称
    public String nickname;
    //用户头像
    public String head_img;
    //多少分钟送达
    public String songda;
    //商家回复
    public String reply_content;
    public List<replay> replay;

}
