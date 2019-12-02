package com.ychong.baselib.entity;

public class EventBusMessage {
    public String msg;
    public int code;
    public Object object;

    public EventBusMessage(String msg,int code){
        this.msg = msg;
        this.code = code;
    }
    public EventBusMessage(String msg,Object object){
        this.msg = msg;
        this.object = object;
    }
}
