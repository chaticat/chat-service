package com.chaticat.chatservice.listener;

public interface MessageListener<T> {

    void listen(T message);
}
