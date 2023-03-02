package com.chaticat.chatservice.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncomingMessageSender implements MessageSender<Object> {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value(value = "${kafka.topic.incoming-message.name}")
    private String incomingMessageTopic;

    @SneakyThrows
    public void send(Object message) {
        String stringMessage = objectMapper.writeValueAsString(message);
        CompletableFuture<SendResult<String, String>> completableFuture = kafkaTemplate.send(incomingMessageTopic, stringMessage);
        completableFuture.whenComplete((msg, exp) -> {
            if (exp == null) {
                log.info("Sent message [ %s ] to topic [ %s ]".formatted(message, incomingMessageTopic));
            } else {
                log.error(exp.getMessage());
            }
        });
    }
}