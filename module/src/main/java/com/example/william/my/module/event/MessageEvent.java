package com.example.william.my.module.event;

public class MessageEvent {

    private final String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
