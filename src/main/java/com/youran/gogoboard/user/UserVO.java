package com.youran.gogoboard.user;

import lombok.Data;

@Data
public class UserVO {
	
	private String id;
    private String email;
    private String name;
    private String password;
    private String created;
    private String updated;
    
}
