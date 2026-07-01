package com.flowmate.backend.user.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {

	private Integer id;
	private String name;
	private String email;
	private String password;
	private LocalDateTime createdAt;
}
