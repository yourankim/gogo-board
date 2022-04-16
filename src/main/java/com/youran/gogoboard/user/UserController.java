package com.youran.gogoboard.user;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.servlet.http.HttpServletResponse;

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
			
		} catch(KeyAlreadyExistsException ke) {
			return new ResponseEntity<String>(ke.getMessage(), HttpStatus.CONFLICT);
		} catch(Exception e) {
			log.error("Exception in addUser: {}", e.getMessage());
			return new ResponseEntity<String>("Fail...", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>("Success!",HttpStatus.OK);
		
	}
	
	@PostMapping("/auth")
	public ResponseEntity<AuthResponse> login(@RequestBody UserVO userVO, 
			Authentication authentication, HttpServletResponse response) {
		
		AuthResponse authResponse = new AuthResponse();
		
		try {
			AuthVO authVO = service.login(userVO);
			response.addCookie(authVO.generateRefreshTokenCookie());
			
			authentication = new UserAuthentication((Object)authVO.getUser().getId(), authVO.getAccessToken());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			authResponse.setUser(authVO.getUser());
			authResponse.setAccessToken(authVO.getAccessToken());
			
		} catch(UnauthorizedException ue) {
			authResponse.setMessage(ue.getMessage());
			return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.UNAUTHORIZED);
		} catch(Exception e) {
			log.error("Exception in login: {}", e.getMessage());
			authResponse.setMessage(e.getMessage());
			return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
	}
	
	@PostMapping("/auth/refresh")
	public ResponseEntity<AuthResponse> refresh(
			@CookieValue(value="gogoboard-rtk", required=true, defaultValue="no token") String refreshToken, 
			HttpServletResponse response) {

		AuthResponse authResponse = new AuthResponse();
	
		try {
			log.debug("refreshToken from cookie: {}",refreshToken);
			AuthVO authVO = service.refresh(refreshToken);
			response.addCookie(authVO.generateRefreshTokenCookie());
			authResponse.setUser(authVO.getUser());
			authResponse.setAccessToken(authVO.getAccessToken());
			
		} catch(UnauthorizedException ue) {
			authResponse.setMessage(ue.getMessage());
			log.error("UnauthorizedException in refresh: {}", ue.getMessage());
			return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.UNAUTHORIZED);
		} catch(Exception e) {
			log.error("Exception in refresh: {}", e.getMessage());
			authResponse.setMessage(e.getMessage());
			return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
	}
	
	

}
