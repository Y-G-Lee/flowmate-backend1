package com.flowmate.backend.task.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCreateRequest(
		@NotBlank(message = "작업 제목은 필수입니다.")
		@Size(max = 200, message = "작업 제목은 200자 이하여야 합니다.")
		String title,

		@Size(max = 2000, message = "설명은 2000자 이하여야 합니다.")
		String description,

		@Size(max = 30, message = "상태 값이 너무 깁니다.")
		String status,

		@Size(max = 20, message = "우선순위 값이 너무 깁니다.")
		String priority,

		LocalDate dueDate,

		Integer assigneeId
) {
}
