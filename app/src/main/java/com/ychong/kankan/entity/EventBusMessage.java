package com.ychong.kankan.entity;

import org.greenrobot.eventbus.EventBus;

public class EventBusMessage {
    public String msg;
    public int code;

    public EventBusMessage(String msg,int code){
        this.msg = msg;
    }
}
