package com.flowmate.backend.common.exception;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(Integer userId) {
		super("사용자를 찾을 수 없습니다. userId=" + userId);
	}
}
