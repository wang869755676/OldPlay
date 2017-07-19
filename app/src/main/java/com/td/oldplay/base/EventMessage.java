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
    public int total;
    public EventMessage(String action) {
        this.action = action;
    }

   /* public EventMessage setAction(String action) {
        this.action = action;
        return this;
    }*/
}
