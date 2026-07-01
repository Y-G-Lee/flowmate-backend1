package com.flowmate.backend.user.dto;

public record LoginResponse(
		Integer id,
		String name,
		String email,
		String accessToken
) {
}
