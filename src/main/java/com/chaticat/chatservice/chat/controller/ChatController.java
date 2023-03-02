package com.chaticat.chatservice.chat.controller;

import com.chaticat.chatservice.payload.MessageRequest;
import com.chaticat.chatservice.sender.IncomingMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Validated
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final IncomingMessageSender incomingMessageSender;

    @MessageMapping("/chat")
    public void handleIncomingMessage(@Payload MessageRequest messageRequest, SimpMessageHeaderAccessor headerAccessor) {
        messageRequest.getMessage().setTimestamp(LocalDateTime.now());
        incomingMessageSender.send(messageRequest);
    }
}
