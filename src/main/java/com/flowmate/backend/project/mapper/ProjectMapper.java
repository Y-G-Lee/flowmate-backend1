package com.flowmate.backend.project.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.flowmate.backend.project.domain.Project;
import com.flowmate.backend.project.domain.Team;
import com.flowmate.backend.project.dto.ProjectResponse;

@Mapper
public interface ProjectMapper {

	Integer findTeamIdByUserAndTeamName(
			@Param("userId") Integer userId,
			@Param("teamName") String teamName
	);

	boolean existsTeamMember(
			@Param("teamId") Integer teamId,
			@Param("userId") Integer userId
	);

	int insertTeam(Team team);

	int insertTeamMember(
			@Param("teamId") Integer teamId,
			@Param("userId") Integer userId,
			@Param("teamRole") String teamRole
	);

	int insertProject(Project project);

	int updateProject(Project project);

	int deleteTasksByProjectId(@Param("projectId") Integer projectId);

	int deleteProject(@Param("projectId") Integer projectId);

	List<ProjectResponse> findProjectsByUserId(@Param("userId") Integer userId);

	Optional<ProjectResponse> findProjectByIdAndUserId(
			@Param("projectId") Integer projectId,
			@Param("userId") Integer userId
	);
}
