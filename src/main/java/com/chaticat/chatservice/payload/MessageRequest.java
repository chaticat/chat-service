package com.chaticat.chatservice.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {

    @NotNull
    private Message message;
}
