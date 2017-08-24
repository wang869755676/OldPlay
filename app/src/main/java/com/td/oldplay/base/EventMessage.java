package com.td.oldplay.base;

/**
 * eventbus 消息传递
 */

public class EventMessage {
    public String action;
    // 购物车相关的信息
    public int num;
    public String colorId;
    public String size;
    public float total;
    public int WxPayCode;

   public  String contentUrl;// 视频播放的路径
    public EventMessage(String action) {
        this.action = action;
    }

    public EventMessage setNum(int num) {
        this.num = num;
        return this;
    }

    public EventMessage setUrl(String url) {
        this.contentUrl=url;
        return this;
    }
  /* public EventMessage setAction(String action) {
        this.action = action;
        return this;
    }*/
}
