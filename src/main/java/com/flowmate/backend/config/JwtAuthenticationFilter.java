package com.flowmate.backend.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.flowmate.backend.common.auth.AuthUserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String BEARER_PREFIX = "Bearer ";

	private final JwtTokenProvider jwtTokenProvider;
	private final CookieProperties cookieProperties;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {
		String token = resolveToken(request);

		if (StringUtils.hasText(token)) {
			try {
				Claims claims = jwtTokenProvider.parseToken(token);
				AuthUserPrincipal principal = new AuthUserPrincipal(
						jwtTokenProvider.getUserId(claims),
						jwtTokenProvider.getEmail(claims)
				);

				var authentication = new UsernamePasswordAuthenticationToken(
						principal,
						null,
						java.util.List.of()
				);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (JwtException ex) {
				SecurityContextHolder.clearContext();
				String message = jwtTokenProvider.isTokenExpiredException(ex)
						? "토큰이 만료되었습니다."
						: "유효하지 않은 토큰입니다.";
				jwtTokenProvider.writeUnauthorizedResponse(response, message);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER_PREFIX)) {
			return authorization.substring(BEARER_PREFIX.length());
		}

		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (cookieProperties.name().equals(cookie.getName())) {
				return cookie.getValue();
			}
		}

		return null;
	}
}
