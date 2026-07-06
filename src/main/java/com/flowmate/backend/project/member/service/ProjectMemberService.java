package com.flowmate.backend.project.member.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowmate.backend.common.auth.AuthUserResolver;
import com.flowmate.backend.common.exception.DuplicateProjectMemberException;
import com.flowmate.backend.common.exception.ForbiddenAccessException;
import com.flowmate.backend.common.exception.ProjectMemberNotFoundException;
import com.flowmate.backend.common.exception.ProjectNotFoundException;
import com.flowmate.backend.common.exception.UserNotFoundException;
import com.flowmate.backend.project.mapper.ProjectMapper;
import com.flowmate.backend.project.member.dto.ProjectMemberAddRequest;
import com.flowmate.backend.project.member.dto.ProjectMemberResponse;
import com.flowmate.backend.project.member.mapper.ProjectMemberMapper;
import com.flowmate.backend.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

	private static final String ROLE_OWNER = "OWNER";
	private static final String ROLE_MEMBER = "MEMBER";
	private static final String DEFAULT_TEAM_ROLE = "MEMBER";

	private final ProjectMemberMapper projectMemberMapper;
	private final ProjectMapper projectMapper;
	private final UserMapper userMapper;
	private final AuthUserResolver authUserResolver;

	@Transactional(readOnly = true)
	public List<ProjectMemberResponse> getProjectMembers(Integer projectId) {
		Integer userId = authUserResolver.getCurrentUserId();
		verifyProjectMemberAccess(projectId, userId);

		return projectMemberMapper.findMembersByProjectId(projectId);
	}

	@Transactional
	public ProjectMemberResponse addProjectMember(Integer projectId, ProjectMemberAddRequest request) {
		Integer currentUserId = authUserResolver.getCurrentUserId();
		verifyOwnerAccess(projectId, currentUserId);

		Integer targetUserId = request.userId();
		if (userMapper.findById(targetUserId).isEmpty()) {
			throw new UserNotFoundException(targetUserId);
		}

		if (projectMemberMapper.existsProjectMember(projectId, targetUserId)) {
			throw new DuplicateProjectMemberException(projectId, targetUserId);
		}

		projectMemberMapper.insertProjectMember(projectId, targetUserId, ROLE_MEMBER);
		ensureTeamMembership(projectId, targetUserId);

		return projectMemberMapper.findMembersByProjectId(projectId).stream()
				.filter(member -> member.userId().equals(targetUserId))
				.findFirst()
				.orElseThrow(() -> new UserNotFoundException(targetUserId));
	}

	@Transactional
	public void addOwnerOnProjectCreate(Integer projectId, Integer creatorUserId) {
		if (!projectMemberMapper.existsProjectMember(projectId, creatorUserId)) {
			projectMemberMapper.insertProjectMember(projectId, creatorUserId, ROLE_OWNER);
		}
	}

	@Transactional
	public void deleteMembersByProjectId(Integer projectId) {
		projectMemberMapper.deleteByProjectId(projectId);
	}

	@Transactional
	public void removeProjectMember(Integer projectId, Integer targetUserId) {
		Integer currentUserId = authUserResolver.getCurrentUserId();
		verifyOwnerAccess(projectId, currentUserId);

		if (currentUserId.equals(targetUserId)) {
			throw new ForbiddenAccessException("OWNER는 자기 자신을 삭제할 수 없습니다.");
		}

		if (!projectMemberMapper.existsProjectMember(projectId, targetUserId)) {
			throw new ProjectMemberNotFoundException(projectId, targetUserId);
		}

		int deleted = projectMemberMapper.deleteProjectMember(projectId, targetUserId);
		if (deleted == 0) {
			throw new ProjectMemberNotFoundException(projectId, targetUserId);
		}
	}

	private void ensureTeamMembership(Integer projectId, Integer userId) {
		Integer teamId = projectMemberMapper.findTeamIdByProjectId(projectId);
		if (teamId == null || teamId <= 0) {
			throw new ProjectNotFoundException(projectId);
		}

		if (!projectMapper.existsTeamMember(teamId, userId)) {
			projectMapper.insertTeamMember(teamId, userId, DEFAULT_TEAM_ROLE);
		}
	}

	private void verifyProjectMemberAccess(Integer projectId, Integer userId) {
		if (!projectMemberMapper.existsProjectMember(projectId, userId)) {
			throw new ForbiddenAccessException("프로젝트 멤버만 조회할 수 있습니다.");
		}
	}

	private void verifyOwnerAccess(Integer projectId, Integer userId) {
		String role = projectMemberMapper.findMemberRole(projectId, userId)
				.orElseThrow(() -> new ForbiddenAccessException("프로젝트 멤버만 멤버를 추가할 수 있습니다."));

		if (!ROLE_OWNER.equals(role)) {
			throw new ForbiddenAccessException("프로젝트 OWNER만 멤버를 추가할 수 있습니다.");
		}
	}
}
