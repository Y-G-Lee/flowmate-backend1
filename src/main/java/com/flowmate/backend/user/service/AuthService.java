package com.flowmate.backend.user.service;

import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flowmate.backend.common.exception.InvalidLoginException;
import com.flowmate.backend.config.CookieProperties;
import com.flowmate.backend.config.JwtTokenProvider;
import com.flowmate.backend.user.domain.User;
import com.flowmate.backend.user.dto.LoginRequest;
import com.flowmate.backend.user.dto.LoginResponse;
import com.flowmate.backend.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final CookieProperties cookieProperties;

	public LoginResult login(LoginRequest request) {
		User user = userMapper.findByEmail(request.email())
				.orElseThrow(InvalidLoginException::new);

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new InvalidLoginException();
		}

		String accessToken = jwtTokenProvider.createToken(user.getId(), user.getEmail());

		LoginResponse response = new LoginResponse(
				user.getId(),
				user.getName(),
				user.getEmail(),
				accessToken
		);

		ResponseCookie cookie = ResponseCookie.from(cookieProperties.name(), accessToken)
				.httpOnly(cookieProperties.httpOnly())
				.secure(cookieProperties.secure())
				.path(cookieProperties.path())
				.maxAge(cookieProperties.maxAge())
				.sameSite("Lax")
				.build();

		return new LoginResult(response, cookie);
	}

	public record LoginResult(LoginResponse response, ResponseCookie cookie) {
	}
}
