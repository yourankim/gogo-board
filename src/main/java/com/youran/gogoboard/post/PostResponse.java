package com.youran.gogoboard.post;

import java.util.List;

import lombok.Data;

@Data
public class PostResponse {
	
	List<PostVO> posts;
	PostVO post;
	String message;

}
