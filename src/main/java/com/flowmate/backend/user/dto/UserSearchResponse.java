package com.flowmate.backend.user.dto;

public record UserSearchResponse(
		Integer id,
		String name,
		String email
) {
}
