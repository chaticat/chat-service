package com.chaticat.chatservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.chaticat.chatservice.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final PropertiesConfig.JwtProperties properties;

    public UUID getUserIdFromToken(String authToken) {
        var decodedJWT = getDecodedJWT(authToken);
        return UUID.fromString(decodedJWT.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            return getDecodedJWT(authToken) != null;
        } catch (Exception e) {
            throw new RuntimeException("Expired or invalid JWT token");
        }
    }

    private DecodedJWT getDecodedJWT(String authToken) {
        return JWT.require(Algorithm.HMAC512(properties.getSecretKey()))
                .build()
                .verify(authToken);
    }

    public Long getExpirationFromJWT(String authToken) {
        var decodedJWT = getDecodedJWT(authToken);
        return decodedJWT.getExpiresAt().getTime();
    }

    public Boolean checkExpirationAccessToken(String refreshToken) {
        Long expirationRefreshToken = getExpirationFromJWT(refreshToken);

        return expirationRefreshToken - getConstantExpirationRefreshToken() < 0;
    }

    public Long getConstantExpirationRefreshToken() {
        return new Date().getTime() + properties.getExpirationRefreshToken();
    }

}
