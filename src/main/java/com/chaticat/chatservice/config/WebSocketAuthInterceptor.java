package com.chaticat.chatservice.config;

import com.chaticat.chatservice.security.CustomUserDetails;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        final var command = accessor.getCommand();

        System.out.println("intercepted");
        if (StompCommand.CONNECT == command) {
            final var requestTokenHeader = accessor.getFirstNativeHeader("Authorization");

            System.out.println("connect");

            CustomUserDetails userDetails = new CustomUserDetails(1L, 100L);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        return message;
    }

}