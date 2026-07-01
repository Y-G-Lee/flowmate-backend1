package com.flowmate.backend.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowmate.backend.common.exception.DuplicateEmailException;
import com.flowmate.backend.user.domain.User;
import com.flowmate.backend.user.dto.EmailCheckResponse;
import com.flowmate.backend.user.dto.SignUpRequest;
import com.flowmate.backend.user.dto.SignUpResponse;
import com.flowmate.backend.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	public EmailCheckResponse checkEmailAvailability(String email) {
		boolean available = userMapper.findByEmail(email).isEmpty();
		return new EmailCheckResponse(email, available);
	}

	@Transactional
	public SignUpResponse signUp(SignUpRequest request) {
		userMapper.findByEmail(request.email())
				.ifPresent(user -> {
					throw new DuplicateEmailException(request.email());
				});

		User user = User.builder()
				.name(request.name())
				.email(request.email())
				.password(passwordEncoder.encode(request.password()))
				.build();

		userMapper.insert(user);

		return SignUpResponse.from(user);
	}
}
