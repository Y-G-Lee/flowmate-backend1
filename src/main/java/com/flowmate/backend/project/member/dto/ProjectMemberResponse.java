package com.flowmate.backend.project.member.dto;

import java.time.LocalDateTime;

public record ProjectMemberResponse(
		Integer userId,
		String nickname,
		String email,
		String role,
		LocalDateTime joinedAt
) {
}
