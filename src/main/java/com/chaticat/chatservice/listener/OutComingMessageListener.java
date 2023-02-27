package com.chaticat.chatservice.listener;

import com.chaticat.chatservice.config.KafkaConsumerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutComingMessageListener implements MessageListener<String> {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "${kafka.topic.out-coming-message.name}", groupId = KafkaConsumerConfig.DEFAULT_GROUP_ID)
    @Override
    public void listen(String message) {
        log.info("message received: {}", message);
        messagingTemplate.convertAndSend("/topic/chat", message);
    }
}
