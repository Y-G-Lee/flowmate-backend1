package com.flowmate.backend.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectUpdateRequest(
		@NotBlank(message = "프로젝트 이름은 필수입니다.")
		@Size(max = 100, message = "프로젝트 이름은 100자 이하여야 합니다.")
		String name,

		@Size(max = 2000, message = "설명은 2000자 이하여야 합니다.")
		String description,

		@Size(max = 30, message = "상태 값이 너무 깁니다.")
		String status,

		@Size(max = 50, message = "마감일은 50자 이하여야 합니다.")
		String deadline
) {
}
