package com.flowmate.backend.user.dto;

import com.flowmate.backend.user.domain.User;

public record SignUpResponse(
		String name,
		String email
) {

	public static SignUpResponse from(User user) {
		return new SignUpResponse(user.getName(), user.getEmail());
	}
}
