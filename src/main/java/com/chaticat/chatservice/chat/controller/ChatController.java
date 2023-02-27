package com.chaticat.chatservice.chat.controller;

import com.chaticat.chatservice.chat.model.MessageRequest;
import com.chaticat.chatservice.sender.IncomingMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final IncomingMessageSender incomingMessageSender;

    @MessageMapping("/chat")
    public void handleIncomingMessage(@Payload MessageRequest message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        log.info("Chat incoming message request: sessionId: {} and message: {}", sessionId, message.getMessage());
        incomingMessageSender.send(message.getMessage());
    }
}
