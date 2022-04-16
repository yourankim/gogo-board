package com.youran.gogoboard.security;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.youran.gogoboard.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	
	private JwtUtil jwt;
	
	public JwtTokenFilter(JwtUtil jwt) {
		log.debug("JwtTokenFilter");
		this.jwt = jwt;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		log.debug("JwtTokenFilter.doFilterInternal");
		
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(! StringUtils.hasText(authHeader) || ! authHeader.startsWith("Bearer ")) {
			log.debug("헤더에 토큰 없음");
			filterChain.doFilter(request, response);
			return;
		}
		
		final String accessToken = authHeader.replace("Bearer ", "").trim();
		final DecodedJWT decodedJwt = jwt.verifyAccessToken(accessToken);
		if(null == decodedJwt) {
			log.debug("유효하지 않은 토큰: {}",accessToken);
			filterChain.doFilter(request, response);
			return;
		}
		if(decodedJwt.getExpiresAt().before(Date.from(Instant.now()))) {
			log.debug("토큰 만료 - expiredAt {}", decodedJwt.getExpiresAt());
			filterChain.doFilter(request, response);
			return;
		}

		UserAuthentication authentication = new UserAuthentication(decodedJwt.getClaim("identifier"), accessToken);
		//TODO:setDetail 필요할까?
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}

}
