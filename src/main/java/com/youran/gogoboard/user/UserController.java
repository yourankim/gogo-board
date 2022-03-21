package com.youran.gogoboard.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youran.gogoboard.HomeController;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private UserService service;
	
	@PostMapping
	public ResponseEntity<String> addUser(@RequestBody UserVO userVO) {
		
		try {	
			service.addUser(userVO);
			
		} catch(Exception e) {
			logger.error("Exception in addUser: {}", e.getMessage());
			return new ResponseEntity<String>("Fail...", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>("Success!",HttpStatus.OK);
		
	}

}
