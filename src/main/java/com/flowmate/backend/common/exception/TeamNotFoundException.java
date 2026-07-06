package com.flowmate.backend.common.exception;

public class TeamNotFoundException extends RuntimeException {

	public TeamNotFoundException(Integer teamId) {
		super("팀을 찾을 수 없습니다. (teamId=" + teamId + ")");
	}
}
