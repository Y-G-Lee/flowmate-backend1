package com.flowmate.backend.common.auth;

public record AuthUserPrincipal(
		Integer userId,
		String email
) {
}
