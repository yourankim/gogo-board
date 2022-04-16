package com.youran.gogoboard.user;


import java.time.Instant;
import java.util.Date;

import javax.servlet.http.Cookie;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class AuthVO {
	private UserVO user;
	private String accessToken;
	private String refreshToken;
	private Date refreshTokenExpiresAt;
	

	public Cookie generateRefreshTokenCookie() {
		
		if(refreshToken == null) {
			log.debug("refreshToken not found");
			return new Cookie("refreshToken", "");
		}
		
		Cookie cookie = new Cookie("gogoboard-rtk", refreshToken);
		
		long duration = refreshTokenExpiresAt.getTime()-Date.from(Instant.now()).getTime();
		int maxAge = (int) (duration/1000);
		cookie.setMaxAge(maxAge);
		//cookie.setSecure(true); // https 사용시 설정 
		cookie.setHttpOnly(true); // servlet-api 3 이후 사용 가능 
		cookie.setPath("/"); // 경로설정해야 쿠키가 중복으로 생성되지 않는다 
		return cookie;
	}
}
