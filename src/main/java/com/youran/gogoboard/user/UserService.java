package com.youran.gogoboard.user;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.youran.gogoboard.user.exception.UnauthorizedException;
import com.youran.gogoboard.util.JwtUtil;

@Service
public class UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Inject
	private UserDAO dao;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwt;
	
	private Map<String, String> refreshTokenStore = new HashMap<>(); 
	
	public List<UserVO> getAllUsers() throws Exception {
		return dao.selectAllUsers();
	}
	
	public void addUser(UserVO userVO) throws Exception {
		String encPassword = passwordEncoder.encode(userVO.getPassword());
		userVO.setPassword(encPassword);
		dao.insertUser(userVO);
	}

	public AuthVO login(UserVO loginInfo) throws Exception {
		
		UserVO user = dao.findUserByEmail(loginInfo.getEmail());

		if(null == user || ! passwordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
			throw new UnauthorizedException("이메일 또는 비밀번호가 일치하지 않습니다.");
		}

		String accessToken = jwt.generateAccessToken(user.getId());
		String refreshToken = jwt.generateRefreshToken();
		refreshTokenStore.put(refreshToken, user.getId());
		AuthVO authVO = new AuthVO();
		authVO.setAccessToken(accessToken);
		authVO.setRefreshToken(refreshToken);

		return authVO;
	}
	
	public AuthVO refresh(AuthVO authVO) throws Exception {
		String refreshToken = authVO.getRefreshToken();
		logger.debug("refreshTokenStore: {}", refreshTokenStore.keySet().toString());
		String userId = refreshTokenStore.remove(refreshToken);
	
		
		if(null == userId) {
			throw new UnauthorizedException("토큰을 찾을 수 없습니다.");
		}

		DecodedJWT decodedJWT = jwt.verifyRefreshToken(refreshToken);
		if(null == decodedJWT) {
			throw new UnauthorizedException("토큰이 유효하지 않습니다.");
		}
		if(decodedJWT.getExpiresAt().before(Date.from(Instant.now()))) {
			throw new UnauthorizedException("토큰이 만료되었습니다.");
		}
		
		AuthVO newAuthVO = new AuthVO();
		String newAccessToken = jwt.generateAccessToken(userId.toString());
		String newRefreshToken = jwt.generateRefreshToken();
		refreshTokenStore.put(refreshToken, userId);
		newAuthVO.setAccessToken(newAccessToken);
		newAuthVO.setRefreshToken(newRefreshToken);
		
		return newAuthVO;
	}
	
    

}
