package com.youran.gogoboard.user;

import java.security.Principal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youran.gogoboard.exception.UnauthorizedException;
import com.youran.gogoboard.security.UserAuthentication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/users", produces = "application/json; charset=UTF-8")
public class UserController {


	@Autowired
	private UserService service;
	
	@PostMapping
	public ResponseEntity<String> addUser(@RequestBody UserVO userVO) {
		
		try {	
			service.addUser(userVO);
			
		} catch(Exception e) {
			log.error("Exception in addUser: {}", e.getMessage());
			return new ResponseEntity<String>("Fail...", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>("Success!",HttpStatus.OK);
		
	}
	
	@PostMapping("/auth")
	public ResponseEntity<String> login(@RequestBody UserVO userVO, Authentication authentication, HttpServletResponse response) {
		
		AuthVO authVO = new AuthVO();
		
		try {
			authVO = service.login(userVO);
			Cookie cookie = new Cookie("refreshToken", authVO.getRefreshToken());
			cookie.setMaxAge(24 * 60 * 60);
			//cookie.setSecure(true); // https 사용시 설정 
			cookie.setHttpOnly(true); // servlet-api 3 이후 사용 가능 
			response.addCookie(cookie);
			authentication = new UserAuthentication(
					authVO.getUserId(), authVO.getAccessToken()
					);
			log.debug("authentication: {}", authentication);
		} catch(UnauthorizedException ue) {
			return new ResponseEntity<String>(ue.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch(Exception e) {
			log.error("Exception in login: {}", e.getMessage());
			return new ResponseEntity<String>("Fail...", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(authVO.getAccessToken(),HttpStatus.OK);
	}
	
	@PostMapping("/auth/refresh")
	public ResponseEntity<String> refresh(
			@CookieValue(value="refreshToken", required=true) Cookie cookie, 
			Principal principal, 
			HttpServletResponse response) {
		
		AuthVO authVO = new AuthVO();
		
		try {
			authVO.setRefreshToken(cookie.getValue());	
			authVO = service.refresh(authVO);
			cookie.setValue(authVO.getRefreshToken());
			
			log.debug("principal!!! {}", principal);
			
		} catch(UnauthorizedException ue) {
			return new ResponseEntity<String>(ue.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch(Exception e) {
			log.error("Exception in login: {}", e.getMessage());
			return new ResponseEntity<String>("Fail...", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(authVO.getAccessToken(),HttpStatus.OK);
	}
	
	

}
