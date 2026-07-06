package com.flowmate.backend.common.exception;

public class ForbiddenAccessException extends RuntimeException {

	public ForbiddenAccessException(String message) {
		super(message);
	}
}
