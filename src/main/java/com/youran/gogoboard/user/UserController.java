package com.youran.gogoboard.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.youran.gogoboard.HomeController;

@RestController
public class UserController {
	
private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private UserService service;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<UserVO> getAllUsers() {
		
		List<UserVO> users = Collections.emptyList();
		try {
			users = service.getAllUsers();
			
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
		
		return users;
		
	}

}
