package com.chaticat.chatservice.sender;

public interface MessageSender<T> {

    void send(T message);
}
