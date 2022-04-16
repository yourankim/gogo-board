package com.youran.gogoboard.util;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Component
public class JwtUtil {
	
	@Value("${jwt.issuer}")
	private String issuer;
	@Value("${jwt.access.secret}")
	private String accessSecret;
	@Value("${jwt.refresh.secret}")
	private String refreshSecret;
	@Value("${jwt.access.duration}")
	private long accessDuration;
	@Value("${jwt.refresh.duration}")
	private long refreshDuration;
	
	
	public String generateAccessToken(String claim) {
		
		Date accessTokenExpiresAt = Date.from(Instant.now().plusMillis(accessDuration));
		
		return JWT.create().withIssuer(issuer)
					.withClaim("identifier", claim)
					.withExpiresAt(accessTokenExpiresAt)
					.sign(Algorithm.HMAC256(accessSecret));
	}
	
	public DecodedJWT verifyAccessToken(String token) {
		DecodedJWT decodedJWT = null;
		try {
			decodedJWT = JWT.require(Algorithm.HMAC256(accessSecret))
					.withIssuer(issuer)
					.build()
					.verify(token);
		} catch(JWTVerificationException e) {
			log.debug(e.getMessage());
		}
		return decodedJWT;
	}
	
	public String generateRefreshToken() {
		
		Date refreshTokenExpiresAt = Date.from(Instant.now().plusMillis(refreshDuration));
		log.debug("refreshTokenExpiresAt", refreshTokenExpiresAt);
		return JWT.create().withIssuer(issuer)
					.withClaim("identifier", UUID.randomUUID().toString())
					.withExpiresAt(refreshTokenExpiresAt)
					.sign(Algorithm.HMAC256(refreshSecret));
	}
	
	public DecodedJWT verifyRefreshToken(String token) {
		DecodedJWT decodedJWT = null;
		try {
			decodedJWT = JWT.require(Algorithm.HMAC256(refreshSecret))
					.withIssuer(issuer)
					.build()
					.verify(token);
		} catch(JWTVerificationException e) {
			log.debug(e.getMessage());
		}
		return decodedJWT;

	}


}
