package com.youran.gogoboard.post;

import java.util.Date;

import com.youran.gogoboard.user.UserVO;

import lombok.Data;

@Data
public class PostVO {
	//TODO: Date타입 전달해서 프론트에서도 문제 없다면 User도 수정하기 
	private String id;
	private String title;
	private String content;
	private Date created;
	private Date updated;
	private UserVO user;

}
