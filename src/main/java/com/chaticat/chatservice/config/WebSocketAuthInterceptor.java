package com.chaticat.chatservice.config;

import com.chaticat.chatservice.security.JwtTokenProvider;
import com.chaticat.chatservice.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        final var command = accessor.getCommand();

        if (StompCommand.CONNECT == command) {
            final var accessToken = accessor.getFirstNativeHeader("Authorization");
            final var refreshToken = accessor.getFirstNativeHeader("RefreshToken");

            authorize(accessToken, refreshToken);

        }
        return message;
    }

    private void authorize(String accessToken, String refreshToken) {
        try {
            if (StringUtils.hasText(accessToken)
                    && tokenProvider.validateToken(accessToken)
                    && tokenProvider.checkExpirationAccessToken(refreshToken)) {

                UUID userId = tokenProvider.getUserIdFromToken(accessToken);

                var userPrincipal = new UserPrincipal(userId, null);
                var authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, List.of());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }
    }

}