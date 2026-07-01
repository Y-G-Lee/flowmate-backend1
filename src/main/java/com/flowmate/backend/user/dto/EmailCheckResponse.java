package com.flowmate.backend.user.dto;

public record EmailCheckResponse(
		String email,
		boolean available
) {
}
