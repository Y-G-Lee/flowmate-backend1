package com.flowmate.backend.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HexFormat;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowmate.backend.common.exception.ErrorResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenProvider {

	private final JwtProperties jwtProperties;
	private final SecretKey secretKey;
	private final ObjectMapper objectMapper;

	public JwtTokenProvider(JwtProperties jwtProperties, ObjectMapper objectMapper) {
		this.jwtProperties = jwtProperties;
		this.objectMapper = objectMapper;
		this.secretKey = Keys.hmacShaKeyFor(HexFormat.of().parseHex(jwtProperties.secret()));
	}

	public String createToken(Integer userId, String email) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtProperties.expirationMilliseconds());

		return Jwts.builder()
				.issuer(jwtProperties.iss())
				.subject(jwtProperties.sub())
				.claim("userId", userId)
				.claim("email", email)
				.issuedAt(now)
				.expiration(expiry)
				.signWith(secretKey)
				.compact();
	}

	public Claims parseToken(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public Integer getUserId(Claims claims) {
		return claims.get("userId", Integer.class);
	}

	public String getEmail(Claims claims) {
		return claims.get("email", String.class);
	}

	public void writeUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType("application/json;charset=UTF-8");
		objectMapper.writeValue(
				response.getWriter(),
				ErrorResponse.of(HttpServletResponse.SC_UNAUTHORIZED, message)
		);
	}

	public boolean isTokenExpiredException(JwtException ex) {
		return ex instanceof ExpiredJwtException;
	}
}
