package com.youran.gogoboard.post;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youran.gogoboard.user.UserVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostService {
	
	@Autowired
	private PostDAO dao;
	
	public List<PostVO> getPosts(int page) throws Exception {
		PageVO pageVO = new PageVO();
		pageVO.setPage(page);
		return dao.selectPosts(pageVO);
	}
	
	public PostVO findPostById(int id) throws Exception {
		return dao.selectPostById(id);
	}
	
	public void createNewPost(PostVO postVO, String userId) throws Exception {
		UserVO user = new UserVO();
		user.setId(userId);
		postVO.setUser(user);
		dao.insertPost(postVO);
	}
	
	public void updatePost(PostVO postVO, String userId) throws Exception {
		UserVO user = new UserVO();
		user.setId(userId);
		postVO.setUser(user);
		dao.updatePost(postVO);
	}
	
	public void deletePost(int postId, String userId) throws Exception {
		PostVO post = new PostVO();
		UserVO user = new UserVO();
		user.setId(userId);
		post.setId(postId);
		post.setUser(user);
		dao.deletePost(post);
	};

}
