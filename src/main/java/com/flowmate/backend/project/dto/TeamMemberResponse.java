package com.flowmate.backend.project.dto;

import java.time.LocalDateTime;

public record TeamMemberResponse(
		Integer userId,
		String name,
		String email,
		String teamRole,
		LocalDateTime joinedAt
) {
}
