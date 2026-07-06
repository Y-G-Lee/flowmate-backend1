package com.flowmate.backend.project.member.dto;

import jakarta.validation.constraints.NotNull;

public record ProjectMemberAddRequest(
		@NotNull(message = "userId는 필수입니다.")
		Integer userId
) {
}
