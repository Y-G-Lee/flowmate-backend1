package com.flowmate.backend.common.exception;

import java.time.OffsetDateTime;
import java.util.List;

public record ErrorResponse(
		int status,
		String message,
		List<FieldError> errors,
		OffsetDateTime timestamp
) {

	public record FieldError(String field, String message) {
	}

	public static ErrorResponse of(int status, String message) {
		return new ErrorResponse(status, message, List.of(), OffsetDateTime.now());
	}

	public static ErrorResponse of(int status, String message, List<FieldError> errors) {
		return new ErrorResponse(status, message, errors, OffsetDateTime.now());
	}
}
