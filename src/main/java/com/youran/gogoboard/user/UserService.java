package com.youran.gogoboard.user;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.youran.gogoboard.exception.UnauthorizedException;
import com.youran.gogoboard.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	
	@Autowired
	private UserDAO dao;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwt;
	
	private Map<String, UserVO> refreshTokenStore = new HashMap<>(); 
	
	public List<UserVO> getAllUsers() throws Exception {
		return dao.selectAllUsers();
	}
	
	public void addUser(UserVO userVO) throws Exception {
		UserVO user = dao.findUserByEmail(userVO.getEmail());
		if(null != user) {
			throw new KeyAlreadyExistsException("이미 존재하는 이메일입니다.");
		}
		String encPassword = passwordEncoder.encode(userVO.getPassword());
		userVO.setPassword(encPassword);
		dao.insertUser(userVO);
	}

	public AuthVO login(UserVO loginInfo) throws Exception {
		
		UserVO user = dao.findUserByEmail(loginInfo.getEmail());

		if(null == user || ! passwordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
			throw new UnauthorizedException("이메일 또는 비밀번호가 일치하지 않습니다.");
		}

		user.setPassword(null);

		return generateAuthVO(user);
	}
	
	public AuthVO refresh(String refreshToken) throws Exception {
		
		UserVO user = refreshTokenStore.remove(refreshToken);

		DecodedJWT decodedJWT = jwt.verifyRefreshToken(refreshToken);
		if(null == decodedJWT || null == user) {
			throw new UnauthorizedException("토큰이 유효하지 않습니다.");
		}
		if(decodedJWT.getExpiresAt().before(Date.from(Instant.now()))) {
			throw new UnauthorizedException("토큰이 만료되었습니다.");
		}
		
		return generateAuthVO(user);
	}
	
	private AuthVO generateAuthVO(UserVO user) {
		
		AuthVO authVO = new AuthVO();
		String accessToken = jwt.generateAccessToken(user.getId());
		String refreshToken = jwt.generateRefreshToken();
		refreshTokenStore.put(refreshToken, user);
		authVO.setAccessToken(accessToken);
		authVO.setRefreshToken(refreshToken);
		DecodedJWT decodedJWT = jwt.verifyRefreshToken(refreshToken);
		authVO.setRefreshTokenExpiresAt(decodedJWT.getExpiresAt());
		authVO.setUser(user);
		
		return authVO;
		
	}

}
