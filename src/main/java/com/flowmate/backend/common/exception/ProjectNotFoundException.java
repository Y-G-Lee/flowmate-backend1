package com.flowmate.backend.common.exception;

public class ProjectNotFoundException extends RuntimeException {

	public ProjectNotFoundException(Integer projectId) {
		super("프로젝트를 찾을 수 없습니다: " + projectId);
	}
}
