package com.youran.gogoboard.user;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.youran.gogoboard.user.exception.UnauthorizedException;

@Service
public class UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Inject
	private UserDAO dao;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
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
		
		if(user == null || ! passwordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
			throw new UnauthorizedException("이메일 또는 비밀번호가 일치하지 않습니다.");
		}
		
		AuthVO authVO = new AuthVO();
		authVO.setAccessToken("jwt1");
		authVO.setRefreshToken("jwt2");
		
		return authVO;
	}
	
    

}
