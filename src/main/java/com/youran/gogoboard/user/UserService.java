package com.youran.gogoboard.user;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Inject
	private UserDAO dao;
	
	public List<UserVO> getAllUsers() throws Exception {
		return dao.selectAllUsers();
	}

}
