package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/8.
 */
//显示商品属性详情
public class Attribute implements Serializable {
    public String id;
    public String contentTip = "";
    public String content ;

    public String getContentTip() {
        return contentTip;
    }

    public void setContentTip(String contentTip) {
        this.contentTip = contentTip;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
