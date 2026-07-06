package com.flowmate.backend.common.exception;

public class DuplicateProjectMemberException extends RuntimeException {

	public DuplicateProjectMemberException(Integer projectId, Integer userId) {
		super("이미 프로젝트에 참여 중인 사용자입니다. projectId=" + projectId + ", userId=" + userId);
	}
}
