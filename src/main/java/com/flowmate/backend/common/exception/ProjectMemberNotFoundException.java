package com.flowmate.backend.common.exception;

public class ProjectMemberNotFoundException extends RuntimeException {

	public ProjectMemberNotFoundException(Integer projectId, Integer userId) {
		super("프로젝트 멤버를 찾을 수 없습니다. projectId=" + projectId + ", userId=" + userId);
	}
}
