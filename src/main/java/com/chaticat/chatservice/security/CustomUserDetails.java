package com.chaticat.chatservice.security;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private Long id;
    private Long chatId;

    public CustomUserDetails(Long id, Long chatId) {
        super(String.valueOf(1), null, Set.of());
        this.id = id;
        this.chatId = chatId;
    }
}
