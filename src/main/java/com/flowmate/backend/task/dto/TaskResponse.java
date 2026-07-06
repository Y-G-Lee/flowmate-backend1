package com.flowmate.backend.task.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponse(
		Integer id,
		Integer projectId,
		String projectName,
		String title,
		String description,
		String status,
		String priority,
		Integer assigneeId,
		String assigneeName,
		LocalDate dueDate,
		LocalDateTime createdAt
) {
}
