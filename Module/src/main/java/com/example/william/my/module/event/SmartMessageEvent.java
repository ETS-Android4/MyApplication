package com.example.william.my.module.event;

import com.jeremyliao.eventbus.base.annotation.SmartEvent;

@SmartEvent(keys = {"event1", "event2", "event3"})
public class SmartMessageEvent {

    private final String message;

    public SmartMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
