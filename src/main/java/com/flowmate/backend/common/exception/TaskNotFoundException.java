package com.flowmate.backend.common.exception;

public class TaskNotFoundException extends RuntimeException {

	public TaskNotFoundException(Integer taskId) {
		super("작업을 찾을 수 없습니다. id=" + taskId);
	}
}
