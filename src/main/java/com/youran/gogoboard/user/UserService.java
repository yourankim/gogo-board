package com.youran.gogoboard.user;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
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

}
