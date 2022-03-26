package com.youran.gogoboard.util;

import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	
	private String issuer;
	private String accessSecret;
	private String refreshSecret;
	private long accessDuration;
	private long refreshDuration;
	
	
	public String generateAccessToken(String claim) {
		return JWT.create().withIssuer(issuer)
					.withClaim("identifier", claim)
					.withExpiresAt(Date.from(Instant.now().plusMillis(accessDuration)))
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
			logger.debug(e.getMessage());
		}
		return decodedJWT;
	}
	
	public String generateRefreshToken() {
		return JWT.create().withIssuer(issuer)
					.withClaim("identifier", UUID.randomUUID().toString())
					.withExpiresAt(Date.from(Instant.now().plusMillis(refreshDuration)))
					.sign(Algorithm.HMAC256(refreshSecret));
	}
	
	public DecodedJWT verifyRefreshToken(String token) {
		return JWT.require(Algorithm.HMAC256(refreshSecret))
				.withIssuer(issuer)
				.build()
				.verify(token);

	}


}
