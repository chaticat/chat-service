package com.chaticat.chatservice.payload;

import com.chaticat.chatservice.enumeration.MessageStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Message {

    @NotNull
    private String chatId;

    @NotBlank
    private Sender from;

    @NotBlank
    private String content;

    private LocalDateTime timestamp;

    @NotNull
    private MessageStatus status;
}
