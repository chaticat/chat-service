package com.chaticat.chatservice.listener;

import com.chaticat.chatservice.enumeration.MessageStatus;
import com.chaticat.chatservice.payload.MessageResponse;
import com.chaticat.chatservice.config.KafkaConsumerConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutComingMessageListener implements MessageListener<String> {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @KafkaListener(topics = "${kafka.topic.out-coming-message.name}", groupId = KafkaConsumerConfig.DEFAULT_GROUP_ID)
    @Override
    public void listen(String response) {
        log.info("message received: {}", response);

        MessageResponse messageResponse = objectMapper.readValue(response, MessageResponse.class);
        messageResponse.getMessage().setStatus(MessageStatus.DELIVERED);

        String destination = "/topic/chat/%s".formatted(messageResponse.getMessage().getChatId());
        messagingTemplate.convertAndSend(destination, messageResponse.getMessage());
    }
}
