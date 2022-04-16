package com.youran.gogoboard.user;

import lombok.Data;

@Data
public class AuthResponse {
	
	private UserVO user;
	private String accessToken;
	private String message;

}
