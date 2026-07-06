package com.flowmate.backend.project.member.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.flowmate.backend.project.member.dto.ProjectMemberResponse;

@Mapper
public interface ProjectMemberMapper {

	List<ProjectMemberResponse> findMembersByProjectId(@Param("projectId") Integer projectId);

	Optional<String> findMemberRole(
			@Param("projectId") Integer projectId,
			@Param("userId") Integer userId
	);

	boolean existsProjectMember(
			@Param("projectId") Integer projectId,
			@Param("userId") Integer userId
	);

	int insertProjectMember(
			@Param("projectId") Integer projectId,
			@Param("userId") Integer userId,
			@Param("role") String role
	);

	int deleteByProjectId(@Param("projectId") Integer projectId);

	int deleteProjectMember(
			@Param("projectId") Integer projectId,
			@Param("userId") Integer userId
	);

	Integer findTeamIdByProjectId(@Param("projectId") Integer projectId);
}
