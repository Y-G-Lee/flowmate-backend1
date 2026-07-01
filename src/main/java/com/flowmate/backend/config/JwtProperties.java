package com.flowmate.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
		String secret,
		String iss,
		String sub,
		long expirationMilliseconds
) {
}
