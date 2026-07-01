package com.flowmate.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cookie.jwt")
public record CookieProperties(
		String name,
		boolean httpOnly,
		boolean secure,
		String path,
		int maxAge
) {
}
