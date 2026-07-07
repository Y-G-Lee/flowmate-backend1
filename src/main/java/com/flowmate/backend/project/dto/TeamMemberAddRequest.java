package com.flowmate.backend.project.dto;

import jakarta.validation.constraints.NotNull;

public record TeamMemberAddRequest(
		@NotNull(message = "userId는 필수입니다.")
		Integer userId
) {
}
