package com.youran.gogoboard.user;

import lombok.Data;

@Data
public class AuthVO {
	
	private String accessToken;
	private String refreshToken;
}
