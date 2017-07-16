package com.td.oldplay.base;

/**
 * eventbus 消息传递
 */

public class EventMessage {
    public String action;

    public EventMessage(String action) {
        this.action = action;
    }

   /* public EventMessage setAction(String action) {
        this.action = action;
        return this;
    }*/
}
